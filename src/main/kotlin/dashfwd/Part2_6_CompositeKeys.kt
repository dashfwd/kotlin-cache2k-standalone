package dashfwd

import org.cache2k.Cache
import org.cache2k.Cache2kBuilder
import org.cache2k.CacheEntry
import org.cache2k.expiry.ExpiryTimeValues.ETERNAL
import org.cache2k.expiry.ExpiryTimeValues.NO_CACHE
import kotlin.system.measureTimeMillis

// See https://cache2k.org/docs/latest/user-guide.html#composite-keys
class Part2_6_CompositeKeys {
    val favoriteAirlineDatabase = FavoriteAirlineDatabase()

    // Key = Route("MUC", "SFO"), Value = "Yeti Jet"
    val routeToAirline = object : Cache2kBuilder<Route, String>() {}
        .name("routeToAirline")
        .loader(favoriteAirlineDatabase::findFavoriteAirline)
        .expiryPolicy(this::expiryPolicy) // change expiry based on whether things are null
        .permitNullValues(true)      // allow null values in the cache
        .build() as Cache<Route, String>

    // Use an expiry policy so nulls aren't cached
    fun expiryPolicy(key:Route, value:String?, loadTime:Long, oldEntry:CacheEntry<Route, String>?):Long {
        return if (value == null) { NO_CACHE } else { ETERNAL }
    }
}

fun main() {
    val example = Part2_6_CompositeKeys()
    for (i in 1..5) {
        var airline:String? = null
        val elapsed = measureTimeMillis {
            airline = example.routeToAirline.get(Route("MUC", "SFO"))
        }
        println("[CompositeKeys] For run #$i, elapsed time was $elapsed; airline=$airline")
    }
}
