package core.objects

data class Monomial<T : Number>(
    val ring: PolynomialRing<T>,
    val exponents: Map<Indeterminate, Int>
) {
    val degree: Int by lazy {
        exponents.values.sum()
    }

    init {
        require(exponents.keys.all { it in ring.indeterminates }) {
            "All variables used in the monomial must belong to the polynomial ring."
        }
    }
}
