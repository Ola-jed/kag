package core.operators

import core.objects.Indeterminate
import core.objects.Monomial
import core.objects.Polynomial
import utils.ensure

operator fun <T : Number> Monomial<T>.times(rhs: Monomial<T>): Monomial<T> {
    ensure("Monomial multiplication can only be done in the same ring") {
        this.ring == rhs.ring
    }

    val resultExponents = HashMap<Indeterminate, Int>(this.exponents.size + rhs.exponents.size)
    for ((key, value) in this.exponents) {
        resultExponents[key] = value
    }

    for ((key, value) in rhs.exponents) {
        resultExponents[key] = resultExponents.getOrDefault(key, 0) + value
    }

    return Monomial(this.ring, exponents = resultExponents)
}

operator fun <T : Number> Monomial<T>.div(rhs: Monomial<T>): Monomial<T> {
    ensure("Monomial division can only be done in the same ring") {
        this.ring == rhs.ring
    }

    var resultExponents = mutableMapOf<Indeterminate, Int>()
    for (indeterminateAndExponent in rhs.exponents) {
        var difference = this.exponents.getOrDefault(indeterminateAndExponent.key, 0) - indeterminateAndExponent.value
        if (difference >= 0) {
            resultExponents.put(indeterminateAndExponent.key, difference)
        }
    }

    if (resultExponents.size == rhs.exponents.size) {
        for (indeterminateAndExponent in this.exponents) {
            resultExponents.putIfAbsent(indeterminateAndExponent.key, indeterminateAndExponent.value)
        }
    }

    return Monomial(this.ring, exponents = resultExponents)
}

operator fun <T : Number> Monomial<T>.rem(rhs: Monomial<T>): Monomial<T> {
    ensure("Monomial remainder can only be done in the same ring") {
        this.ring == rhs.ring
    }

    var resultExponents = mutableMapOf<Indeterminate, Int>()
    for (indeterminateAndExponent in this.exponents) {
        if (!rhs.exponents.containsKey(indeterminateAndExponent.key)) {
            resultExponents.put(indeterminateAndExponent.key, indeterminateAndExponent.value)
        }
    }

    return Monomial(this.ring, exponents = resultExponents)
}

operator fun <T : Number> Monomial<T>.times(rhs: Polynomial<T>): Polynomial<T> {
    val monomials = rhs.monomials.map {
        ensure("Monomial multiplication can only be done in the same ring") {
            this.ring == it.second.ring
        }

        Pair(it.first, it.second * this)
    }

    return Polynomial(_monomials = monomials, ordering = rhs.ordering)
}