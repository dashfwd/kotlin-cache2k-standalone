package dashfwd

import org.cache2k.Cache
import org.cache2k.Cache2kBuilder
import kotlin.system.measureTimeMillis

// see https://cache2k.org/docs/latest/user-guide.html#cache-aside
class Part2_3_CacheAside {
    val favoriteAirlineDatabase = FavoriteAirlineDatabase()

    // Key = "MUC-SFO", Value = "Yeti Jet"
    val routeToAirline = object : Cache2kBuilder<String, String>() {}
        .name("routeToAirline")
        .build()
            as Cache<String, String>

    fun lookupFavoriteAirline(origin: String, destination: String): String? {
        val route = "$origin-$destination"
        return routeToAirline.peek(route) ?: run { // if nothing in the cache, populate it
            val airline = favoriteAirlineDatabase.findFavoriteAirline(origin, destination)
            routeToAirline.put(route, airline) // note: won't work for null "airline" values
            return airline
        }
    }
}

fun main() {
    val example = Part2_3_CacheAside()
    for (i in 1..5) {
        val elapsed = measureTimeMillis {
            example.lookupFavoriteAirline("MUC", "SFO")
        }
        println("[CacheAside] For run #$i, elapsed time was $elapsed")
    }
}
