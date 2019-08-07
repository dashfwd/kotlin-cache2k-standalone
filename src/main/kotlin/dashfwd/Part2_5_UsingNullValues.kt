package dashfwd

import org.cache2k.Cache
import org.cache2k.Cache2kBuilder
import org.cache2k.CacheEntry
import org.cache2k.expiry.ExpiryTimeValues.ETERNAL
import org.cache2k.expiry.ExpiryTimeValues.NO_CACHE
import kotlin.system.measureTimeMillis

// see https://cache2k.org/docs/latest/user-guide.html#using-null-values
class Part2_5_UsingNullValues {
    val favoriteAirlineDatabase = FavoriteAirlineDatabase()

    // Key = "MUC-SFO", Value = "Yeti Jet"
    val routeToAirline = object : Cache2kBuilder<String, String>() {}
        .name("routeToAirline")
        .loader(favoriteAirlineDatabase::findFavoriteAirline)
        .expiryPolicy(this::expiryPolicy) // change expiry based on whether things are null
        .permitNullValues(true)      // allow null values in the cache
        .build()
            as Cache<String, String>

    fun lookupFavoriteAirline(origin: String, destination: String): String? {
        return routeToAirline.get("$origin-$destination")
    }

    fun expiryPolicy(key:String, value:String?, loadTime:Long, oldEntry:CacheEntry<String, String>?):Long {
        return if (value == null) {  NO_CACHE } else { ETERNAL } // don't cache null
    }
}

fun main() {
    val example = Part2_5_UsingNullValues()
    for (i in 1..5) {
        val elapsed = measureTimeMillis {
            example.lookupFavoriteAirline("ORD", "SFO")
        }
        println("[UsingNullValues] For run #$i, elapsed time was $elapsed")
    }
}
