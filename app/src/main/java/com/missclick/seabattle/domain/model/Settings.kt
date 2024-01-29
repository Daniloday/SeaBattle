package com.missclick.seabattle.domain.model

import android.os.CombinedVibration
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream
import java.io.Serializable
import javax.inject.Inject

@kotlinx.serialization.Serializable
data class Settings(
    val vibration: Boolean = true,
    val volume : Boolean = true,
) : Serializable

class SettingsSerializer @Inject constructor() : Serializer<Settings> {

    override val defaultValue: Settings = Settings()

    override suspend fun readFrom(input: InputStream): Settings =
        try {
            Json.decodeFromString(
                Settings.serializer(), input.readBytes().decodeToString()
            )
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }


    override suspend fun writeTo(t: Settings, output: OutputStream) {
        output.write(
            Json.encodeToString(Settings.serializer(), t)
                .encodeToByteArray()
        )
    }

}