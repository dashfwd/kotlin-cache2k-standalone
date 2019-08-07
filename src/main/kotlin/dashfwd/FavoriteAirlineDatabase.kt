package dashfwd

class FavoriteAirlineDatabase {
    private val databaseData = mapOf(
        Route("MUC", "SFO") to "Yeti Jet",
        Route("SFO", "LHR") to "Quality Air",
        Route("LHR", "SYD") to "Grasshopper Lifting"
    )

    fun findFavoriteAirline(route:Route): String? {
        Thread.sleep(1000) // simulate a slow database connection
        return databaseData[route]
    }

    /**
     * @param originAndDestination "MUC-SFO"
     */
    fun findFavoriteAirline(originAndDestination: String): String? {
        val (origin, destination) = originAndDestination.split("-")
        return findFavoriteAirline(Route(origin, destination))
    }

    /**
     * @param origin "MUC"
     * @param destination "SFO"
     */
    fun findFavoriteAirline(origin: String, destination: String): String? =
        findFavoriteAirline(Route(origin, destination))

}