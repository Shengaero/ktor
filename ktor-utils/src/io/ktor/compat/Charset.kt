package io.ktor.compat

expect fun Char.isLowerCase(): Boolean

fun String.toCharArray(): CharArray = CharArray(length) { get(it) }
