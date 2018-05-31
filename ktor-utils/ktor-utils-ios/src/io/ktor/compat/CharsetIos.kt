package io.ktor.compat

actual fun Char.isLowerCase(): Boolean = toLowerCase() == this
