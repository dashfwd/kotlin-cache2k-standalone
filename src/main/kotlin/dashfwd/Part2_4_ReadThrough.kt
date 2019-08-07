package dashfwd

import org.cache2k.Cache
import org.cache2k.Cache2kBuilder
import kotlin.system.measureTimeMillis

// see https://cache2k.org/docs/latest/user-guide.html#read-through
class Part2_4_ReadThrough {
    val favoriteAirlineDatabase = FavoriteAirlineDatabase()

    // Key = "MUC-SFO", Value = "Yeti Jet"
    val routeToAirline = object : Cache2kBuilder<String, String>() {}
        .name("routeToAirline")
        .loader(favoriteAirlineDatabase::findFavoriteAirline)
        .build()
            as Cache<String, String>

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
