package dashfwd

import org.cache2k.Cache2kBuilder

/**
 * see https://cache2k.org/docs/latest/user-guide.html#using-a-cache
 */
class Part2_2_UsingACache {
    private val cache = object : Cache2kBuilder<String, String>() {}
        .name("routeToAirline")
        .eternal(true) // alternately expireAfterWrite(2, TimeUnit.MINUTES)
        .entryCapacity(100)
        .build()

    fun execute() {
        // populate with our favorites
        cache.put("MUC-SFO", "Yeti Jet")
        cache.put("SFO-LHR", "Quality Air")
        cache.put("LHR-SYD", "Grasshopper Lifting")

        // query the cache
        println("Airline is ${cache.peek("ORD-MKE")}") // null
        println("Airline is ${cache.peek("SFO-LHR")}") // "QualityAir"

        // overwrite a key
        cache.put("SFO-LHR", "Quality Airlines")
        println("Airline is ${cache.peek("SFO-LHR")}") // QualityAirlines

        // Built-in atomic operations
        // See also: https://cache2k.org/docs/latest/user-guide.html#built-in-atomic-operations
        val oldValue = cache.peekAndReplace("SFO-LHR", "QualityAir (again)")
        println("Old value was $oldValue")
        println("Airline is ${cache.peek("SFO-LHR")}") // QualityAirlines

        // remove a key
        cache.remove("SFO-LHR")
        println("Airline is ${cache.peek("SFO-LHR")}") // null

        // query cache size via key iterator
        println("Cache size=${cache.keys().map { it }.size}") // 2

        // clear the cache
        cache.clear() // clear all elements from the cache
        println("Cache size=${cache.keys().map { it }.size}") // 0
    }
}

fun main() {
    Part2_2_UsingACache().execute()
}
