package core.order

import core.objects.Indeterminate
import core.objects.Monomial
import utils.ensure

fun interface MonomialOrdering<T : Number> : Comparator<Monomial<T>> {
    override fun compare(x: Monomial<T>, y: Monomial<T>): Int
}

object CommonOrderings {
    fun <T : Number> lexicographicOrder(): MonomialOrdering<T> {
        return object : MonomialOrdering<T> {
            override fun compare(x: Monomial<T>, y: Monomial<T>): Int {
                ensure("The monomials must be defined over the same polynomial ring") {
                    x.ring == y.ring
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
                ensure("The monomials must be defined over the same polynomial ring") {
                    x.ring == y.ring
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
                ensure("The monomials must be defined over the same polynomial ring") {
                    x.ring == y.ring
                }

                val degreeDifference = x.degree - y.degree
                if (degreeDifference != 0) {
                    return degreeDifference
                }

                return -1 * lexicographicOrder<T>().compare(x, y)
            }
        }
    }

    fun <T : Number> eliminationOrder(
        eliminationVariables: Set<Indeterminate>,
        retainedVariables: Set<Indeterminate>,
        eliminationVariablesOrdering: MonomialOrdering<T> = gradedLexicographicOrder(),
        retainedVariablesOrdering: MonomialOrdering<T> = lexicographicOrder()
    ): MonomialOrdering<T> {
        return object : MonomialOrdering<T> {
            override fun compare(x: Monomial<T>, y: Monomial<T>): Int {
                ensure("Indeterminates for the ordering must belong to the ring") {
                    val indeterminateSet = x.ring.indeterminates.toSet()
                    val requiredSet = eliminationVariables + retainedVariables
                    for (indeterminate in requiredSet) {
                        if (!indeterminateSet.contains(indeterminate)) {
                            return@ensure false
                        }
                    }

                    ensure("Indeterminates in the ring do not match the required variables for the ordering") {
                        indeterminateSet.size == requiredSet.size
                    }

                    true
                }

                ensure("The monomials must be defined over the same polynomial ring") {
                    x.ring == y.ring
                }

                val xElim = x.copy(exponents = x.exponents.filter { eliminationVariables.contains(it.key) })
                val yElim = y.copy(exponents = y.exponents.filter { eliminationVariables.contains(it.key) })
                val eliminationComparison = eliminationVariablesOrdering.compare(xElim, yElim)
                if (eliminationComparison != 0) {
                    return eliminationComparison
                }

                val xRetained = x.copy(exponents = x.exponents.filter { retainedVariables.contains(it.key) })
                val yRetained = y.copy(exponents = y.exponents.filter { retainedVariables.contains(it.key) })
                return retainedVariablesOrdering.compare(xRetained, yRetained)
            }
        }
    }

    fun <T : Number> weightOrder(
        weights: Map<Indeterminate, Int>,
        tieBreaker: MonomialOrdering<T> = lexicographicOrder()
    ): MonomialOrdering<T> {
        return object : MonomialOrdering<T> {
            override fun compare(x: Monomial<T>, y: Monomial<T>): Int {
                ensure("Indeterminates in the ring must match the required variables for the ordering") {
                    val weightsIndeterminates = weights.keys
                    val ringIndeterminates = x.ring.indeterminates.toSet()
                    weightsIndeterminates == ringIndeterminates
                }

                // Computing dot products
                var xProduct = 0
                var yProduct = 0
                for (indeterminate in x.ring.indeterminates) {
                    if (x.exponents.containsKey(indeterminate)) {
                        xProduct += x.exponents[indeterminate]!! * weights[indeterminate]!!
                    }

                    if (y.exponents.containsKey(indeterminate)) {
                        yProduct += y.exponents[indeterminate]!! * weights[indeterminate]!!
                    }
                }

                if (xProduct != yProduct) {
                    return (xProduct - yProduct)
                }

                return tieBreaker.compare(x, y)
            }
        }
    }
}
