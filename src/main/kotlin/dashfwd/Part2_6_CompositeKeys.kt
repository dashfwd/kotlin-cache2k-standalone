package dashfwd

import org.cache2k.Cache2kBuilder
import org.cache2k.CacheEntry
import java.util.concurrent.TimeUnit
import kotlin.system.measureTimeMillis

/***
 * In the previous example the key for the cache was a concatenation of the
 * origin and destination.  In this example, a class called "Route" is used
 * to represent the key.
 *
 * In Java, you would create the Route class and implement the hashCode() and
 * equals() methods.  In Kotlin you can use a Data Class, which automatically
 * generates those methods for you.
 *
 * See https://cache2k.org/docs/latest/user-guide.html#composite-keys
 */
class Part2_6_CompositeKeys {
    private val airlineDB = FavoriteAirlineDatabase()

    // Key = Route("MUC", "SFO"), Value = "Yeti Jet"
    private val routeToAirline = object : Cache2kBuilder<Route, String>() {}
        .name("routeToAirline")
        .loader(airlineDB::findFavoriteAirline)
        .expiryPolicy(this::expiryPolicy) // have a policy for expiration.
        .permitNullValues(true)      // allow null values in the cache
        .build()

    /**
     * Bonus: This example also uses an Expiry Policy which allows us to configure
     * the expiration time of an item in the cache, based on the key, value, or loadTime
     *
     * Read more at https://cache2k.org/docs/latest/user-guide.html#expiry-and-refresh
     */
    private fun expiryPolicy(key:Route, value:String?, loadTime:Long, oldEntry:CacheEntry<Route, String>?):Long {
        return if (key.origin === "SFO") { // our favorite SFO airline changes frequently
            System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(10)
        } else { // everybody else we assume an hour
            System.currentTimeMillis() + TimeUnit.HOURS.toMillis(1)
        }
    }

    fun lookupFavoriteAirline(route: Route) = routeToAirline.get(route)
}

fun main() {
    val example = Part2_6_CompositeKeys()
    for (i in 1..5) {
        var airline:String? = null
        val elapsed = measureTimeMillis {
            airline = example.lookupFavoriteAirline(Route("MUC", "SFO"))
        }
        println("[CompositeKeys] For run #$i, elapsed time was $elapsed; airline=$airline")
    }
}
