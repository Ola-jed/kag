package core.objects

import utils.ensure

data class PolynomialRing<T : Number>(val indeterminates: Array<Indeterminate>) {
    constructor(vararg variables: Char) : this(variables.map(::Indeterminate).toTypedArray())

    fun monomial(vararg exponents: Pair<Char, Int>): Monomial<T> {
        val indeterminatesMap = mutableMapOf<Indeterminate, Int>()
        for (pair in exponents) {
            ensure("Unknown indeterminate ${pair.first} in the ring") {
                pair.first in indeterminates.map { it.label }
            }

            indeterminatesMap.put(Indeterminate(pair.first), pair.second)
        }

        return Monomial(this, indeterminatesMap)
    }

    override fun toString(): String {
        val indeterminatesString = indeterminates.joinToString(", ")
        return "k[$indeterminatesString]"
    }
}