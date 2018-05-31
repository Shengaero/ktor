package io.ktor.compat

internal abstract class MapWithComparator<Key, Value> : MutableMap<Key, Value> {

    protected abstract fun keyEquals(first: Key, second: Key): Boolean

    protected abstract fun keyHash(first: Key): Int

    private var sizeField: Int = 0
    private var container = mutableListOf<MutableList<HashEntry<Key, Value>>>()

    override val size: Int get() = sizeField

    override fun containsKey(key: Key): Boolean {
        val hash = keyHash(key) % container.size
        return container[hash].any { keyEquals(key, it.key) }
    }

    override fun containsValue(value: Value): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun get(key: Key): Value? {
        val hash = keyHash(key) % container.size
        return container[hash].find { keyEquals(key, it.key) }?.value
    }

    override fun isEmpty(): Boolean = size == 0

    override val entries: MutableSet<MutableMap.MutableEntry<Key, Value>>
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override val keys: MutableSet<Key>
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override val values: MutableCollection<Value>
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override fun clear() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun put(key: Key, value: Value): Value? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun putAll(from: Map<out Key, Value>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun remove(key: Key): Value? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

private class HashEntry<Key, Value>(val key: Key, val value: Value)
