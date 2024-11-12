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
        // Only keep monomials that are nonzero
        this._monomials = this._monomials
            .filter { it.second.degree > 0 || it.first.toDouble() != 0.0 }
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
            .map { Pair(it.second, it.first) }
            .toList()

    }

    fun multiDegree(): Map<Indeterminate, Int> {
        val lm = _monomials.asSequence()
            .map { it.second }
            .maxWith(ordering)
        var degrees = mutableMapOf<Indeterminate, Int>()
        for (indeterminate in lm.ring.indeterminates) {
            degrees.put(indeterminate, lm.exponents.getOrDefault(indeterminate, 0))
        }
        return degrees
    }

    fun leadingCoefficient(): T {
        return _monomials.asSequence()
            .maxWith { x, y -> ordering.compare(x.second, y.second) }
            .first
    }

    fun leadingMonomial(): Monomial<T> {
        return _monomials.asSequence()
            .map { it.second }
            .maxWith(ordering)
    }

    fun leadingTerm(): Pair<T, Monomial<T>> {
        return _monomials.asSequence().maxWith { x, y -> ordering.compare(x.second, y.second) }
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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Polynomial<*>) return false

        return _monomials == other._monomials
    }

    override fun hashCode(): Int {
        var result = _monomials.hashCode()
        result = 31 * result + ordering.hashCode()
        return result
    }
}