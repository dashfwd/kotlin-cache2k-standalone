package dashfwd

import org.cache2k.Cache2kBuilder
import kotlin.system.measureTimeMillis

/**
 * In this example, we'll implement a typical pattern for cache lookup and
 * population called "Cache Aside".
 *
 * For an expensive (slow) operation, you can check whether the item exists
 * in the cache.
 *
 * If it exists in the cache ->
 *      We return the item in the cache and skip the expensive operation
 * If it does not exist in the cache ->
 *      We do the expensive operation and put it in the cache, then return the value
 *
 * Note: While this pattern is fine, it's often preferable to use a "loader"
 * as described Part2_4_ReadThrough.kt to save yourself some typing.
 *
 * See also https://cache2k.org/docs/latest/user-guide.html#cache-aside
 */

class Part2_3_CacheAside {
    private val airlineDB = FavoriteAirlineDatabase()

    // Key = "MUC-SFO", Value = "Yeti Jet"
    private val routeToAirline = object : Cache2kBuilder<String, String>() {}
        .name("routeToAirline")
        .build()

    fun lookupFavoriteAirline(origin: String, destination: String): String? {
        val route = "$origin-$destination"
        return routeToAirline.peek(route) ?: run {
            airlineDB.findFavoriteAirline(origin, destination)?.
                also { airline ->
                    routeToAirline.put(route, airline)
                }
        }

        /**
         * The above code is done using idiomatic Kotlin which might not be
         * as familiar to newer Kotlin developers or Java developers.
         *
         * Here's the equivalent Java code:
         *
         *   String airline = routeToAirline.peek(route);
         *   if (airline == null) {
         *      airline = favoriteAirlineDatabase.findFavoriteAirline(origin, destination);
         *      if (airline != null) {
         *          routeToAirline.put(route, airline);
         *      }
         *   }
         *   return airline;
         */
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
