package dashfwd

// Note: Kotlin data classes already implement hashCode() and equals(),
// and we'll declare the properties as immutable
data class Route(val origin:String, val destination: String);
