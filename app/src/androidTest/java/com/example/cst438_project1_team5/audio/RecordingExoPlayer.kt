package com.example.cst438_project1_team5.audio

import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy

internal class RecordingExoPlayer : InvocationHandler {
    val calls = mutableListOf<String>()
    var listener: Player.Listener? = null

    val player: ExoPlayer = Proxy.newProxyInstance(
        ExoPlayer::class.java.classLoader,
        arrayOf(ExoPlayer::class.java),
        this
    ) as ExoPlayer

    override fun invoke(proxy: Any, method: Method, args: Array<out Any?>?): Any? {
        when (method.name) {
            "addListener" -> listener = args?.firstOrNull() as Player.Listener
            "setMediaItem", "prepare", "play", "stop", "release" -> calls += method.name
        }

        return defaultValue(method.returnType)
    }

    private fun defaultValue(returnType: Class<*>): Any? = when (returnType) {
        Boolean::class.javaPrimitiveType -> false
        Byte::class.javaPrimitiveType -> 0.toByte()
        Char::class.javaPrimitiveType -> 0.toChar()
        Double::class.javaPrimitiveType -> 0.0
        Float::class.javaPrimitiveType -> 0f
        Int::class.javaPrimitiveType -> 0
        Long::class.javaPrimitiveType -> 0L
        Short::class.javaPrimitiveType -> 0.toShort()
        Void.TYPE -> null
        else -> null
    }
}
