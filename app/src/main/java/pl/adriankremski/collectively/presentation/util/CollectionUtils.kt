package pl.adriankremski.collectively.presentation.util

import java.util.*

class CollectionUtils {
    companion object {
        fun <K, V> mapOfEntries(vararg entries: Pair<K, V>): Map<K, V> {
            val map = HashMap<K, V>()
            for (entry in entries) {
                map.put(entry.first, entry.second)
            }
            return map
        }

        fun <K, Any> mapOfObjectEntries(vararg entries: Pair<K, Any>): Map<K, Any> {
            val map = HashMap<K, Any>()
            for (entry in entries) {
                map.put(entry.first, entry.second)
            }
            return map
        }
    }
}
