package com.missclick.seabattle.domain.use_cases

import com.missclick.seabattle.common.Resource
import com.missclick.seabattle.common.asResult
import com.missclick.seabattle.data.remote.FireStore
import com.missclick.seabattle.data.remote.dto.cellsToDto
import com.missclick.seabattle.data.remote.dto.toGame
import com.missclick.seabattle.domain.Repository
import com.missclick.seabattle.domain.model.Cell
import com.missclick.seabattle.domain.model.Game
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject


class ObserveRoomUseCase @Inject constructor(
    private val repository: Repository,
    val fireStore: FireStore
) {

    operator fun invoke(code: String, isOwner: Boolean):
            Flow<Resource<Game>> = fireStore.observe(code).map { gameDto ->
        val game = gameDto.toGame(isOwner = isOwner)
        val newGame = game.copy(
            friendCells = game.friendCells.map { listCell ->
                listCell.map { cell ->
                    if (cell == Cell.SHIP_ALIVE) {
                        Cell.EMPTY
                    } else {
                        cell
                    }
                }
            },
            yourCells = markKilledShip(game.yourCells)
        )
        newGame
    }.onEach {  fireStore.doStep(code, isOwner, it.yourCells.cellsToDto()) }.asResult()


    fun markKilledShip(game: List<List<Cell>>): List<List<Cell>> {

        val listMark = mutableListOf<Pair<Int, Int>>()

        game.forEachIndexed { y, row ->
            row.forEachIndexed { x, cell ->
                if (cell == Cell.SHIP_DAMAGE) {
                    try {
                        listMark.addAll(getMarks(game, x, y))
                    } catch (e: Throwable) {
                        println("error")
                        println(e.message)
                    }

                }
            }
        }

        return game.mapIndexed { y, row ->
            row.mapIndexed { x, cell ->
                if (Pair(x, y) in listMark) {
                    Cell.DOT
                } else {
                    cell
                }
            }
        }


    }

    fun getMarks(game: List<List<Cell>>, shotX: Int, shotY: Int): MutableList<Pair<Int, Int>> {
        val shotCell = game[shotY][shotX]

        val shotCellUp = game.getOrNull(shotY - 1)?.getOrNull(shotX) ?: Cell.EMPTY
        val shotCellUp2 = game.getOrNull(shotY - 2)?.getOrNull(shotX) ?: Cell.EMPTY
        val shotCellUp3 = game.getOrNull(shotY - 3)?.getOrNull(shotX) ?: Cell.EMPTY

        val shotCellDown = game.getOrNull(shotY + 1)?.getOrNull(shotX) ?: Cell.EMPTY
        val shotCellDown2 = game.getOrNull(shotY + 2)?.getOrNull(shotX) ?: Cell.EMPTY
        val shotCellDown3 = game.getOrNull(shotY + 3)?.getOrNull(shotX) ?: Cell.EMPTY

        val shotCellRight = game.getOrNull(shotY)?.getOrNull(shotX + 1) ?: Cell.EMPTY
        val shotCellRight2 = game.getOrNull(shotY)?.getOrNull(shotX + 2) ?: Cell.EMPTY
        val shotCellRight3 = game.getOrNull(shotY)?.getOrNull(shotX + 3) ?: Cell.EMPTY

        val shotCellLeft = game.getOrNull(shotY)?.getOrNull(shotX - 1) ?: Cell.EMPTY
        val shotCellLeft2 = game.getOrNull(shotY)?.getOrNull(shotX - 2) ?: Cell.EMPTY
        val shotCellLeft3 = game.getOrNull(shotY)?.getOrNull(shotX - 3) ?: Cell.EMPTY

        val listMark = mutableListOf<Pair<Int, Int>>()


        val aroundStatus: AroundStatus = when {

            shotCellRight == Cell.SHIP_ALIVE || shotCellLeft == Cell.SHIP_ALIVE ||
                    shotCellDown == Cell.SHIP_ALIVE || shotCellUp == Cell.SHIP_ALIVE
            -> AroundStatus.Alive

            shotCellDown != Cell.SHIP_DAMAGE && shotCellUp == Cell.SHIP_DAMAGE -> AroundStatus.Up
            shotCellDown == Cell.SHIP_DAMAGE && shotCellUp != Cell.SHIP_DAMAGE -> AroundStatus.Down
            shotCellDown == Cell.SHIP_DAMAGE && shotCellUp == Cell.SHIP_DAMAGE -> AroundStatus.Vertical

            shotCellRight != Cell.SHIP_DAMAGE && shotCellLeft == Cell.SHIP_DAMAGE -> AroundStatus.Left
            shotCellRight == Cell.SHIP_DAMAGE && shotCellLeft != Cell.SHIP_DAMAGE -> AroundStatus.Right
            shotCellRight == Cell.SHIP_DAMAGE && shotCellLeft == Cell.SHIP_DAMAGE -> AroundStatus.Horizontal


            else -> {
                AroundStatus.Kill
            }

        }

        when (aroundStatus) {
            AroundStatus.Kill -> {
                listMark.addAll(
                    listOf(
                        Pair(shotX + 1, shotY + 1),
                        Pair(shotX - 1, shotY - 1),
                        Pair(shotX + 1, shotY - 1),
                        Pair(shotX - 1, shotY + 1),
                        Pair(shotX - 1, shotY),
                        Pair(shotX + 1, shotY),
                        Pair(shotX, shotY - 1),
                        Pair(shotX, shotY + 1)
                    )
                )
            }

            AroundStatus.Right -> {
                listMark.addAll(shipIsRight(shotX, shotY, shotCellRight2, shotCellRight3))
            }

            AroundStatus.Left -> {
                listMark.addAll(shipIsLeft(shotX, shotY, shotCellLeft2, shotCellLeft3))
            }

            AroundStatus.Down -> {
                listMark.addAll(shipIsDown(shotX, shotY, shotCellDown2, shotCellDown3))
            }

            AroundStatus.Up -> {
                listMark.addAll(shipIsUp(shotX, shotY, shotCellUp2, shotCellUp3))
            }

            AroundStatus.Vertical -> {
                listMark.addAll(shipIsVertical(shotX, shotY, shotCellUp2, shotCellDown2))
            }

            AroundStatus.Horizontal -> {
                listMark.addAll(shipIsHorizontal(shotX, shotY, shotCellRight2, shotCellLeft2))
            }

            AroundStatus.Alive -> {}


        }
        return listMark


    }


    private fun shipIsVertical(
        shotX: Int,
        shotY: Int,
        shotCellUp2: Cell,
        shotCellDown2: Cell
    ): List<Pair<Int, Int>> {
        return when {
            shotCellUp2 == Cell.SHIP_ALIVE || shotCellDown2 == Cell.SHIP_ALIVE -> {
                listOf()
            }
            shotCellUp2 == Cell.SHIP_DAMAGE -> {
                listOf(
                    Pair(shotX - 1, shotY),
                    Pair(shotX + 1, shotY),
                    Pair(shotX - 1, shotY - 1),
                    Pair(shotX + 1, shotY - 1),
                    Pair(shotX - 1, shotY -+ 1),
                    Pair(shotX + 1, shotY + 1),
                    Pair(shotX + 1, shotY + 2),
                    Pair(shotX - 1, shotY + 2),
                    Pair(shotX + 1, shotY - 2),
                    Pair(shotX - 1, shotY - 2),
                    Pair(shotX, shotY - 2),
                    Pair(shotX, shotY + 3),
                    Pair(shotX + 1, shotY + 3),
                    Pair(shotX - 1, shotY + 3),
                )
            }
            shotCellDown2 == Cell.SHIP_DAMAGE -> {
                listOf(
                    Pair(shotX - 1, shotY),
                    Pair(shotX + 1, shotY),
                    Pair(shotX - 1, shotY - 1),
                    Pair(shotX + 1, shotY - 1),
                    Pair(shotX - 1, shotY + 1),
                    Pair(shotX + 1, shotY + 1),
                    Pair(shotX + 1, shotY + 2),
                    Pair(shotX - 1, shotY + 2),
                    Pair(shotX + 1, shotY - 2),
                    Pair(shotX - 1, shotY - 2),
                    Pair(shotX, shotY - 2),
                    Pair(shotX, shotY - 3),
                    Pair(shotX + 1, shotY - 3),
                    Pair(shotX - 1, shotY - 3),
                )
            }
            else -> {
                listOf(
                    Pair(shotX - 1, shotY),
                    Pair(shotX + 1, shotY),
                    Pair(shotX - 1, shotY - 1),
                    Pair(shotX + 1, shotY - 1),
                    Pair(shotX - 1, shotY + 1),
                    Pair(shotX + 1, shotY + 1),
                    Pair(shotX + 1, shotY + 2),
                    Pair(shotX - 1, shotY + 2),
                    Pair(shotX + 1, shotY - 2),
                    Pair(shotX - 1, shotY - 2),
                    Pair(shotX, shotY - 2),
                    Pair(shotX, shotY + 2),
                )
            }
        }
    }

    private fun shipIsHorizontal(
        shotX: Int,
        shotY: Int,
        shotCellRight2: Cell,
        shotCellLeft2: Cell
    ): List<Pair<Int, Int>> {
        return when {
            shotCellRight2 == Cell.SHIP_ALIVE || shotCellLeft2 == Cell.SHIP_ALIVE -> {
                listOf()
            }
            shotCellRight2 == Cell.SHIP_DAMAGE -> {
                listOf(
                    Pair(shotX, shotY - 1),
                    Pair(shotX, shotY + 1),
                    Pair(shotX - 1, shotY - 1),
                    Pair(shotX - 1, shotY + 1),
                    Pair(shotX + 1, shotY - 1),
                    Pair(shotX + 1, shotY + 1),
                    Pair(shotX + 2, shotY + 1),
                    Pair(shotX + 2, shotY - 1),
                    Pair(shotX - 2, shotY + 1),
                    Pair(shotX - 2, shotY - 1),
                    Pair(shotX - 2, shotY),
                    Pair(shotX + 3, shotY),
                    Pair(shotX + 3, shotY + 1),
                    Pair(shotX + 3, shotY - 1),
                )
            }
            shotCellLeft2 == Cell.SHIP_DAMAGE -> {
                listOf(
                    Pair(shotX, shotY - 1),
                    Pair(shotX, shotY + 1),
                    Pair(shotX - 1, shotY - 1),
                    Pair(shotX - 1, shotY + 1),
                    Pair(shotX + 1, shotY - 1),
                    Pair(shotX + 1, shotY + 1),
                    Pair(shotX + 2, shotY + 1),
                    Pair(shotX + 2, shotY - 1),
                    Pair(shotX - 2, shotY + 1),
                    Pair(shotX - 2, shotY - 1),
                    Pair(shotX - 2, shotY),
                    Pair(shotX - 3, shotY),
                    Pair(shotX - 3, shotY + 1),
                    Pair(shotX - 3, shotY - 1),
                )
            }
            else -> {
                listOf(
                    Pair(shotX, shotY - 1),
                    Pair(shotX, shotY + 1),
                    Pair(shotX - 1, shotY - 1),
                    Pair(shotX - 1, shotY + 1),
                    Pair(shotX + 1, shotY - 1),
                    Pair(shotX + 1, shotY + 1),
                    Pair(shotX + 2, shotY + 1),
                    Pair(shotX + 2, shotY - 1),
                    Pair(shotX - 2, shotY + 1),
                    Pair(shotX - 2, shotY - 1),
                    Pair(shotX - 2, shotY),
                    Pair(shotX + 2, shotY),
                )
            }
        }
    }


    private fun shipIsRight(
        shotX: Int,
        shotY: Int,
        shotCellRight2: Cell,
        shotCellRight3: Cell
    ): List<Pair<Int, Int>> {
        return when (shotCellRight2) {
            Cell.SHIP_ALIVE -> {
                listOf()
            }

            Cell.SHIP_DAMAGE -> {
                when (shotCellRight3) {
                    Cell.SHIP_ALIVE -> {
                        listOf()
                    }

                    Cell.SHIP_DAMAGE -> {
                        listOf(
                            Pair(shotX + 1, shotY + 1),
                            Pair(shotX - 1, shotY - 1),
                            Pair(shotX + 1, shotY - 1),
                            Pair(shotX - 1, shotY + 1),
                            Pair(shotX - 1, shotY),
                            Pair(shotX + 4, shotY),
                            Pair(shotX + 4, shotY + 1),
                            Pair(shotX + 4, shotY - 1),
                            Pair(shotX + 3, shotY + 1),
                            Pair(shotX + 3, shotY - 1),
                            Pair(shotX + 2, shotY + 1),
                            Pair(shotX + 2, shotY - 1),
                            Pair(shotX, shotY - 1),
                            Pair(shotX, shotY + 1)
                        )

                    }

                    else -> {
                        listOf(
                            Pair(shotX + 1, shotY + 1),
                            Pair(shotX - 1, shotY - 1),
                            Pair(shotX + 1, shotY - 1),
                            Pair(shotX - 1, shotY + 1),
                            Pair(shotX - 1, shotY),
                            Pair(shotX + 3, shotY),
                            Pair(shotX + 3, shotY + 1),
                            Pair(shotX + 3, shotY - 1),
                            Pair(shotX + 2, shotY + 1),
                            Pair(shotX + 2, shotY - 1),
                            Pair(shotX, shotY - 1),
                            Pair(shotX, shotY + 1)
                        )

                    }
                }

            }

            else -> {

                listOf(
                    Pair(shotX + 1, shotY + 1),
                    Pair(shotX - 1, shotY - 1),
                    Pair(shotX + 1, shotY - 1),
                    Pair(shotX - 1, shotY + 1),
                    Pair(shotX - 1, shotY),
                    Pair(shotX + 2, shotY),
                    Pair(shotX + 2, shotY + 1),
                    Pair(shotX + 2, shotY - 1),
                    Pair(shotX, shotY - 1),
                    Pair(shotX, shotY + 1)
                )

            }
        }
    }

    private fun shipIsLeft(
        shotX: Int,
        shotY: Int,
        shotCellLeft2: Cell,
        shotCellLeft3: Cell
    ): List<Pair<Int, Int>> {
        return when (shotCellLeft2) {
            Cell.SHIP_ALIVE -> {
                listOf()
            }

            Cell.SHIP_DAMAGE -> {
                when (shotCellLeft3) {
                    Cell.SHIP_ALIVE -> {
                        listOf()
                    }

                    Cell.SHIP_DAMAGE -> {
                        listOf(
                            Pair(shotX - 1, shotY + 1),
                            Pair(shotX + 1, shotY - 1),
                            Pair(shotX - 1, shotY - 1),
                            Pair(shotX + 1, shotY + 1),
                            Pair(shotX + 1, shotY),
                            Pair(shotX - 4, shotY),
                            Pair(shotX - 4, shotY + 1),
                            Pair(shotX - 4, shotY - 1),
                            Pair(shotX - 3, shotY + 1),
                            Pair(shotX - 3, shotY - 1),
                            Pair(shotX - 2, shotY + 1),
                            Pair(shotX - 2, shotY - 1),
                            Pair(shotX, shotY - 1),
                            Pair(shotX, shotY + 1)
                        )

                    }

                    else -> {
                        listOf(
                            Pair(shotX - 1, shotY + 1),
                            Pair(shotX + 1, shotY - 1),
                            Pair(shotX - 1, shotY - 1),
                            Pair(shotX + 1, shotY + 1),
                            Pair(shotX + 1, shotY),
                            Pair(shotX - 3, shotY),
                            Pair(shotX - 3, shotY + 1),
                            Pair(shotX - 3, shotY - 1),
                            Pair(shotX - 2, shotY + 1),
                            Pair(shotX - 2, shotY - 1),
                            Pair(shotX, shotY - 1),
                            Pair(shotX, shotY + 1)
                        )

                    }
                }

            }

            else -> {

                listOf(
                    Pair(shotX - 1, shotY + 1),
                    Pair(shotX + 1, shotY - 1),
                    Pair(shotX - 1, shotY - 1),
                    Pair(shotX + 1, shotY + 1),
                    Pair(shotX + 1, shotY),
                    Pair(shotX - 2, shotY),
                    Pair(shotX - 2, shotY + 1),
                    Pair(shotX - 2, shotY - 1),
                    Pair(shotX, shotY - 1),
                    Pair(shotX, shotY + 1)
                )

            }
        }
    }

    private fun shipIsUp(
        shotX: Int,
        shotY: Int,
        shotCellUp2: Cell,
        shotCellUp3: Cell
    ): List<Pair<Int, Int>> {
        return when (shotCellUp2) {
            Cell.SHIP_ALIVE -> {
                listOf()
            }

            Cell.SHIP_DAMAGE -> {
                when (shotCellUp3) {
                    Cell.SHIP_ALIVE -> {
                        listOf()
                    }

                    Cell.SHIP_DAMAGE -> {
                        listOf(
                            Pair(shotX + 1, shotY - 1),
                            Pair(shotX - 1, shotY + 1),
                            Pair(shotX - 1, shotY - 1),
                            Pair(shotX + 1, shotY + 1),
                            Pair(shotX, shotY + 1),
                            Pair(shotX, shotY - 4),
                            Pair(shotX + 1, shotY - 4),
                            Pair(shotX - 1, shotY - 4),
                            Pair(shotX + 1, shotY - 3),
                            Pair(shotX - 1, shotY - 3),
                            Pair(shotX + 1, shotY - 2),
                            Pair(shotX - 1, shotY - 2),
                            Pair(shotX - 1, shotY),
                            Pair(shotX + 1, shotY)
                        )

                    }

                    else -> {
                        listOf(
                            Pair(shotX + 1, shotY - 1),
                            Pair(shotX - 1, shotY + 1),
                            Pair(shotX - 1, shotY - 1),
                            Pair(shotX + 1, shotY + 1),
                            Pair(shotX, shotY + 1),
                            Pair(shotX, shotY - 3),
                            Pair(shotX + 1, shotY - 3),
                            Pair(shotX - 1, shotY - 3),
                            Pair(shotX + 1, shotY - 2),
                            Pair(shotX - 1, shotY - 2),
                            Pair(shotX - 1, shotY),
                            Pair(shotX + 1, shotY)
                        )

                    }
                }

            }

            else -> {

                listOf(
                    Pair(shotX + 1, shotY - 1),
                    Pair(shotX - 1, shotY + 1),
                    Pair(shotX - 1, shotY - 1),
                    Pair(shotX + 1, shotY + 1),
                    Pair(shotX, shotY + 1),
                    Pair(shotX, shotY - 2),
                    Pair(shotX + 1, shotY - 2),
                    Pair(shotX - 1, shotY - 2),
                    Pair(shotX - 1, shotY),
                    Pair(shotX + 1, shotY)
                )

            }
        }
    }

    private fun shipIsDown(
        shotX: Int,
        shotY: Int,
        shotCellDown2: Cell,
        shotCellDown3: Cell
    ): List<Pair<Int, Int>> {
        return when (shotCellDown2) {
            Cell.SHIP_ALIVE -> {
                listOf()
            }

            Cell.SHIP_DAMAGE -> {
                when (shotCellDown3) {
                    Cell.SHIP_ALIVE -> {
                        listOf()
                    }

                    Cell.SHIP_DAMAGE -> {
                        listOf(
                            Pair(shotX + 1, shotY + 1),
                            Pair(shotX - 1, shotY - 1),
                            Pair(shotX - 1, shotY + 1),
                            Pair(shotX + 1, shotY - 1),
                            Pair(shotX, shotY - 1),
                            Pair(shotX, shotY + 4),
                            Pair(shotX + 1, shotY + 4),
                            Pair(shotX - 1, shotY + 4),
                            Pair(shotX + 1, shotY + 3),
                            Pair(shotX - 1, shotY + 3),
                            Pair(shotX + 1, shotY + 2),
                            Pair(shotX - 1, shotY + 2),
                            Pair(shotX - 1, shotY),
                            Pair(shotX + 1, shotY)
                        )

                    }

                    else -> {
                        listOf(
                            Pair(shotX + 1, shotY + 1),
                            Pair(shotX - 1, shotY - 1),
                            Pair(shotX - 1, shotY + 1),
                            Pair(shotX + 1, shotY - 1),
                            Pair(shotX, shotY - 1),
                            Pair(shotX, shotY + 3),
                            Pair(shotX + 1, shotY + 3),
                            Pair(shotX - 1, shotY + 3),
                            Pair(shotX + 1, shotY + 2),
                            Pair(shotX - 1, shotY + 2),
                            Pair(shotX - 1, shotY),
                            Pair(shotX + 1, shotY)
                        )

                    }
                }

            }

            else -> {

                listOf(
                    Pair(shotX + 1, shotY + 1),
                    Pair(shotX - 1, shotY - 1),
                    Pair(shotX - 1, shotY + 1),
                    Pair(shotX + 1, shotY - 1),
                    Pair(shotX, shotY - 1),
                    Pair(shotX, shotY + 2),
                    Pair(shotX + 1, shotY + 2),
                    Pair(shotX - 1, shotY + 2),
                    Pair(shotX - 1, shotY),
                    Pair(shotX + 1, shotY)
                )

            }
        }
    }


    enum class AroundStatus {
        Kill, Vertical, Horizontal, Up, Down, Left, Right, Alive
    }


}
