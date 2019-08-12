package dashfwd

import org.cache2k.Cache2kBuilder
import org.cache2k.CacheEntry
import org.cache2k.expiry.ExpiryTimeValues.ETERNAL
import org.cache2k.expiry.ExpiryTimeValues.NO_CACHE
import kotlin.system.measureTimeMillis

// see https://cache2k.org/docs/latest/user-guide.html#using-null-values
class Part2_5_UsingNullValues {
    private val favoriteAirlineDatabase = FavoriteAirlineDatabase()

    // Key = "MUC-SFO", Value = "Yeti Jet"
    private val routeToAirline = object : Cache2kBuilder<String, String>() {}
        .name("routeToAirline")
        .loader(favoriteAirlineDatabase::findFavoriteAirline)
        .expiryPolicy(this::expiryPolicy) // change expiry based on whether things are null
        .permitNullValues(true)      // allow null values in the cache
        .build()

    fun lookupFavoriteAirline(origin: String, destination: String): String? {
        return routeToAirline.get("$origin-$destination")
    }

    private fun expiryPolicy(key:String, value:String?, loadTime:Long, oldEntry:CacheEntry<String, String>?):Long {
        return if (value == null) {  NO_CACHE } else { ETERNAL } // don't cache null
    }
}

fun main() {
    val example = Part2_5_UsingNullValues()
    for (i in 1..5) {
        val elapsed = measureTimeMillis {
            // There is no "ORD" to "SFO" favorite airline; this will return null.
            // Our cache allows nulls using permitNullValues() but then immediately
            // expires the null value from the cache using the expiryPolicy(...).  Sometimes
            // you'll want to cache the null values and other times you won't; the example
            // here shows how you handle the case that you do not want to cache nulls.
            example.lookupFavoriteAirline("ORD", "SFO")
        }
        println("[UsingNullValues] For run #$i, elapsed time was $elapsed")
    }
}
