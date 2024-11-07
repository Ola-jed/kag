package core.objects

import utils.ensure

data class Monomial<T : Number>(
    val ring: PolynomialRing<T>,
    val exponents: Map<Indeterminate, Int>
) {
    val degree: Int by lazy { exponents.values.sum() }

    init {
        ensure("All variables used in the monomial must belong to the polynomial ring.") {
            exponents.keys.all { it in ring.indeterminates }
        }
    }

    fun toPolynomial(coefficient: T): Polynomial<T> {
        return Polynomial(coefficient to this)
    }

    override fun toString(): String {
        val exponentString = exponents.entries
            .joinToString(" * ") { (variable, exponent) ->
                if (exponent == 1) "$variable" else "$variable^$exponent"
            }

        return if (exponentString.isEmpty()) "0" else exponentString
    }

    override fun equals(other: Any?): Boolean {
        if (other == null) {
            return false
        }

        if (other !is Monomial<T>) {
            return false
        }

        if (ring != other.ring) {
            return false
        }

        if (exponents.filter { it.value != 0 } != other.exponents.filter { it.value != 0 }) {
            return false
        }

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + ring.hashCode()
        result = 31 * result + exponents.filter { it.value != 0 }.hashCode()
        return result
    }
}