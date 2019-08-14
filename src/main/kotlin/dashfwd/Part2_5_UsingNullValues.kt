package dashfwd

import org.cache2k.Cache2kBuilder
import kotlin.system.measureTimeMillis

/**
 *  How are null values handled?
 *
 *  In the Cache Aside example, if our method returned a null
 *  value we didn't add the item to the cache, but this means that you are still
 *  incurring the cost of a slow lookup for values that don't exist in the database.
 *
 *  In the Read Through example returning a null value from the loader method
 *  would cause Cache2K to throw an exception, because by default null values are not
 *  cached.  Depending on your application that might be fine -- nulls might
 *  be a case that never happens.
 *
 *  But if you need nulls in your cache, there's another mechanism...
 *
 *  In this example we use "permitNullValues(true)" tell Cache2K that it's OK
 *  to cache null values.
 *
 *  See https://cache2k.org/docs/latest/user-guide.html#using-null-values
 */

class Part2_5_UsingNullValues {
    private val airlineDB = FavoriteAirlineDatabase()

    // Key = "MUC-SFO", Value = "Yeti Jet"
    private val routeToAirline = object : Cache2kBuilder<String, String>() {}
        .name("routeToAirline")
        .loader(airlineDB::findFavoriteAirline)
        .permitNullValues(true)      // allow null values in the cache
        .build()

    fun lookupFavoriteAirline(origin: String, destination: String): String? {
        return routeToAirline.get("$origin-$destination")
    }
}

fun main() {
    val example = Part2_5_UsingNullValues()
    for (i in 1..5) {
        val elapsed = measureTimeMillis {
            // Because there is no "ORD" to "SFO" favorite airline; this will return null,
            // To allow this null value to be cached "permitNullValues(true)" is used.
            example.lookupFavoriteAirline("ORD", "SFO")
        }
        println("[UsingNullValues] For run #$i, elapsed time was $elapsed")
    }
}
