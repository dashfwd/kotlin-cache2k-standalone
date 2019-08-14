package dashfwd

import org.cache2k.Cache2kBuilder
import kotlin.system.measureTimeMillis

/**
 * In the "Cache Aside" example, we had to code our logic for looking up values
 * in the cache and loading them.
 *
 * In this example, we provide Cache2K with a "loader" callback(lamdba) that tells it
 * how to load values.  This allows us to skip the boilerplate code and have
 * Cache2K automatically load items when needed.
 *
 * See https://cache2k.org/docs/latest/user-guide.html#read-through
 *
 * Note that loaders also are the foundation of more advanced features such
 * as "Refresh Ahead" which allows Cache2K to automatically refresh your caches
 * for you prior to expiration.
 *
 * https://cache2k.org/docs/latest/user-guide.html#refresh-ahead
 */
class Part2_4_ReadThrough {
    private val airlineDB = FavoriteAirlineDatabase()

    // Key = "MUC-SFO", Value = "Yeti Jet"
    private val routeToAirline = object : Cache2kBuilder<String, String>() {}
        .name("routeToAirline")
        .loader(airlineDB::findFavoriteAirline)
        .build()

    fun lookupFavoriteAirline(origin: String, destination: String): String? {
        return routeToAirline.get("$origin-$destination") // note: won't work for null "airline" values
    }
}

fun main() {
    val example = Part2_4_ReadThrough()
    for (i in 1..5) {
        val elapsed = measureTimeMillis {
            example.lookupFavoriteAirline("MUC", "SFO")
        }
        println("[ReadThrough] For run #$i, elapsed time was $elapsed")
    }
}
