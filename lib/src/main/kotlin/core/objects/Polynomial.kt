package core.objects

import core.order.CommonOrderings
import core.order.MonomialOrdering
import utils.Numbers

data class Polynomial<T : Number>(
    private var _monomials: List<Pair<T, Monomial<T>>>,
    val ordering: MonomialOrdering<T> = CommonOrderings.lexicographicOrder()
) {
    val monomials: List<Pair<T, Monomial<T>>> get() = _monomials

    constructor(
        vararg monomials: Pair<T, Monomial<T>>,
        ordering: MonomialOrdering<T> = CommonOrderings.lexicographicOrder()
    ) : this(monomials.asList(), ordering)

    init {
        // Sort monomials by decreasing order and group by monomial part for simplification
        this._monomials = this._monomials
            .groupBy { it.second }
            .mapValues { (_, terms) ->
                var sum = terms[0].first
                sum = Numbers.subtract(sum, sum)
                for (term in terms) {
                    sum = Numbers.add(sum, term.first)
                }
                sum
            }
            .toList()
            .sortedWith { x, y -> -1 * ordering.compare(x.first, y.first) }
            .map { Pair(it.second, it.first) }.toList()
    }

    fun multiDegree(order: MonomialOrdering<T>): Map<Indeterminate, Int> {
        val lm = _monomials.asSequence()
            .map { it.second }
            .maxWith(order)
        var degrees = mutableMapOf<Indeterminate, Int>()
        for (indeterminate in lm.ring.indeterminates) {
            degrees.put(indeterminate, lm.exponents.getOrDefault(indeterminate, 0))
        }
        return degrees
    }

    fun leadingCoefficient(order: MonomialOrdering<T>): T {
        return _monomials.asSequence()
            .maxWith { x, y -> order.compare(x.second, y.second) }
            .first
    }

    fun leadingMonomial(order: MonomialOrdering<T>): Monomial<T> {
        return _monomials.asSequence()
            .map { it.second }
            .maxWith(order)
    }

    fun leadingTerm(order: MonomialOrdering<T>): Pair<T, Monomial<T>> {
        return _monomials.asSequence().maxWith { x, y -> order.compare(x.second, y.second) }
    }

    companion object {
        fun <T : Number> from(
            value: T,
            ring: PolynomialRing<T>,
            ordering: MonomialOrdering<T> = CommonOrderings.lexicographicOrder()
        ): Polynomial<T> {
            return Polynomial(value to Monomial(ring, mapOf()), ordering = ordering)
        }
    }

    override fun toString(): String {
        return _monomials.toString()
    }
}