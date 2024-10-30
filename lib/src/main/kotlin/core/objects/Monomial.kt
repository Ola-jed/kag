package core.objects

import utils.ensure

data class Monomial<T : Number>(
    val ring: PolynomialRing<T>,
    val exponents: Map<Indeterminate, Int>
) {
    val degree: Int by lazy {
        exponents.values.sum()
    }

    init {
        ensure("All variables used in the monomial must belong to the polynomial ring.") {
            exponents.keys.all { it in ring.indeterminates }
        }
    }

    fun toPolynomial(coefficient: T): Polynomial<T> {
        return Polynomial(coefficient to this)
    }
}