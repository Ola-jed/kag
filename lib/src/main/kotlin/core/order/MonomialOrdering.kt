package core.order

import core.objects.Monomial

fun interface MonomialOrdering<T : Number> {
    fun compare(x: Monomial<T>, y: Monomial<T>): Int
}

object CommonOrderings {
    fun <T : Number> lexicographicOrder(): MonomialOrdering<T> {
        return object : MonomialOrdering<T> {
            override fun compare(x: Monomial<T>, y: Monomial<T>): Int {
                require(x.ring == y.ring) {
                    "The monomials must be defined over the same polynomial ring"
                }

                for (indeterminate in x.ring.indeterminates) {
                    val diff = x.exponents.getOrDefault(indeterminate, 0) - y.exponents.getOrDefault(indeterminate, 0)
                    if (diff != 0) {
                        return diff
                    }
                }

                return 0
            }
        }
    }

    fun <T : Number> gradedLexicographicOrder(): MonomialOrdering<T> {
        return object : MonomialOrdering<T> {
            override fun compare(x: Monomial<T>, y: Monomial<T>): Int {
                require(x.ring == y.ring) {
                    "The monomials must be defined over the same polynomial ring"
                }

                val degreeDifference = x.degree - y.degree
                if (degreeDifference != 0) {
                    return degreeDifference
                }

                return lexicographicOrder<T>().compare(x, y)
            }
        }
    }

    fun <T : Number> gradedReverseLexicographicOrder(): MonomialOrdering<T> {
        return object : MonomialOrdering<T> {
            override fun compare(x: Monomial<T>, y: Monomial<T>): Int {
                require(x.ring == y.ring) {
                    "The monomials must be defined over the same polynomial ring"
                }

                val degreeDifference = x.degree - y.degree
                if (degreeDifference != 0) {
                    return degreeDifference
                }

                return -1 * lexicographicOrder<T>().compare(x, y)
            }
        }
    }

    // TODO : Add elimination order and weight order
}
