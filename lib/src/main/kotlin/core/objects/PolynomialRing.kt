package core.objects

data class PolynomialRing<T : Number>(val indeterminates: List<Indeterminate>) {
    constructor(vararg variables: String) : this(variables.asList().map(::Indeterminate))

    fun monomial(vararg exponents: Pair<String, Int>): Monomial<T> {
        val indeterminatesMap = mutableMapOf<Indeterminate, Int>()
        for (pair in exponents) {
            require(pair.first in indeterminates.map { it.label }) {
                "Unknown indeterminate in the ring"
            }

            indeterminatesMap.put(Indeterminate(pair.first), pair.second)
        }

        return Monomial(this, indeterminatesMap)
    }
}