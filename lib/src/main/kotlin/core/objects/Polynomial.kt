package core.objects

import core.order.MonomialOrdering

data class Polynomial<T : Number>(val monomials: List<Pair<T, Monomial<T>>>) {
    val degree: Int by lazy { monomials.map { it.second.degree }.max() }
    constructor(vararg monomials: Pair<T, Monomial<T>>) : this(monomials.asList())

    fun multiDegree(order: MonomialOrdering<T>): Map<Indeterminate, Int> {
        val lm = monomials.asSequence()
            .map { it.second }
            .maxWith(order)
        var degrees = mutableMapOf<Indeterminate, Int>()
        for (indeterminate in lm.ring.indeterminates) {
            degrees.put(indeterminate, lm.exponents.getOrDefault(indeterminate, 0))
        }
        return degrees
    }

    fun leadingCoefficient(order: MonomialOrdering<T>): T {
        return monomials.asSequence()
            .maxWith { x, y -> order.compare(x.second, y.second) }
            .first
    }

    fun leadingMonomial(order: MonomialOrdering<T>): Monomial<T> {
        return monomials.asSequence()
            .map { it.second }
            .maxWith(order)
    }

    fun leadingTerm(order: MonomialOrdering<T>): Pair<T, Monomial<T>> {
        return monomials.asSequence().maxWith { x, y -> order.compare(x.second, y.second) }
    }

    // TODO : Addition, subtraction, merging same degree monomials, product
}