package io.ktor.compat


actual fun <T> Set<T>.unmodifiable(): Set<T> = this

actual fun <Value> caseInsensitiveMap(
    capacity: Int
): MutableMap<String, Value> = object : MapWithComparator<String, Value>() {

    override fun keyEquals(first: String, second: String): Boolean = first.equals(second, ignoreCase = true)

    override fun keyHash(first: String): Int = first.toLowerCase().hashCode()
}

