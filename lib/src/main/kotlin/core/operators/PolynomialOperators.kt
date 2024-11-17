package core.operators

import core.enums.Operation
import core.objects.Monomial
import core.objects.Polynomial
import extensions.toPolynomial
import utils.Numbers
import utils.ensure

operator fun <T : Number> Polynomial<T>.plus(rhs: Polynomial<T>): Polynomial<T> {
    return this.op(rhs, Operation.PLUS)
}

operator fun <T : Number> Polynomial<T>.minus(rhs: Polynomial<T>): Polynomial<T> {
    return this.op(rhs, Operation.MINUS)
}

operator fun <T : Number> Polynomial<T>.times(rhs: T): Polynomial<T> {
    return Polynomial(
        monomials = this.monomials.map { Numbers.times(it.first, rhs) to it.second }.toTypedArray(),
        ordering = this.ordering
    )
}

operator fun <T : Number> Polynomial<T>.times(rhs: Monomial<T>): Polynomial<T> {
    val monomials = this.monomials.map {
        ensure("Monomial multiplication can only be done in the same ring") {
            rhs.ring == it.second.ring
        }

        Pair(it.first, it.second * rhs)
    }

    return Polynomial(_monomials = monomials, ordering = this.ordering)
}

operator fun <T : Number> Polynomial<T>.times(rhs: Polynomial<T>): Polynomial<T> {
    if (this.monomials.isEmpty() || rhs.monomials.isEmpty()) {
        return Polynomial()
    }

    var resultingPolynomial = (this.monomials.first().second * rhs) * this.monomials.first().first
    for (i in 1..<this.monomials.size) {
        resultingPolynomial += (this.monomials[i].second * rhs) * this.monomials[i].first
    }

    return resultingPolynomial
}

operator fun <T : Number> Polynomial<T>.div(rhs: T): Polynomial<T> {
    return Polynomial(
        monomials = this.monomials.map { Numbers.div(it.first, rhs) to it.second }.toTypedArray(),
        ordering = this.ordering
    )
}

// Divide a polynomial by a set of polynomials, get the quotient for each polynomial of the set and the remainder
// We assume the polynomials are defined using the same ordering
operator fun <T : Number> Polynomial<T>.div(rhs: List<Polynomial<T>>): Pair<Array<Polynomial<T>>, Polynomial<T>> {
    var currentPolynomial = this
    var remainder = Polynomial(_monomials = listOf(), ordering = this.ordering)
    val leadingTerms = rhs.map { it.leadingTerm() }
    val quotients = Array<Polynomial<T>>(rhs.size) { Polynomial() }

    while (currentPolynomial.monomials.isNotEmpty()) {
        val currentLeadingTerm = currentPolynomial.leadingTerm()
        var divided = false

        for (i in leadingTerms.indices) {
            val term = leadingTerms[i]
            val divisionResult = currentLeadingTerm.second / term.second

            if (divisionResult.exponents.isNotEmpty()) {
                val resultAsPolynomial = divisionResult.toPolynomial(Numbers.div(currentLeadingTerm.first, term.first))
                quotients[i] += resultAsPolynomial
                currentPolynomial -= resultAsPolynomial * rhs[i]
                divided = true
                break
            }
        }

        if (!divided) {
            val fLeadingTermPolynomial = currentLeadingTerm.toPolynomial()
            remainder += fLeadingTermPolynomial
            currentPolynomial -= fLeadingTermPolynomial
        }
    }

    return quotients to remainder
}

// Get the remainder of the division by a set of polynomials
// We assume the polynomials are defined using the same ordering
operator fun <T : Number> Polynomial<T>.rem(rhs: List<Polynomial<T>>): Polynomial<T> {
    var currentPolynomial = this
    var remainder = Polynomial(_monomials = listOf(), ordering = this.ordering)
    val leadingTerms = rhs.map { it.leadingTerm() }

    while (currentPolynomial.monomials.isNotEmpty()) {
        val currentLeadingTerm = currentPolynomial.leadingTerm()
        var divided = false

        for (i in leadingTerms.indices) {
            val term = leadingTerms[i]
            val divisionResult = currentLeadingTerm.second / term.second

            if (divisionResult.exponents.isNotEmpty()) {
                val resultAsPolynomial = divisionResult.toPolynomial(Numbers.div(currentLeadingTerm.first, term.first))
                currentPolynomial -= resultAsPolynomial * rhs[i]
                divided = true
                break
            }
        }

        if (!divided) {
            val fLeadingTermPolynomial = currentLeadingTerm.toPolynomial()
            remainder += fLeadingTermPolynomial
            currentPolynomial -= fLeadingTermPolynomial
        }
    }

    return remainder
}

fun <T : Number> Polynomial<T>.op(rhs: Polynomial<T>, operation: Operation): Polynomial<T> {
    var i = 0
    var j = 0
    var resultingMonomials = mutableListOf<Pair<T, Monomial<T>>>()
    val transformation: (Pair<T, Monomial<T>>) -> Pair<T, Monomial<T>> = { x: Pair<T, Monomial<T>> ->
        if (operation == Operation.PLUS) {
            x
        } else {
            Numbers.opposite(x.first) to x.second
        }
    }

    while (i < this.monomials.size || j < rhs.monomials.size) {
        if (i < this.monomials.size && j < rhs.monomials.size) {
            val lhsCurrentTerm = this.monomials[i]
            val rhsCurrentTerm = rhs.monomials[j]
            val comparisonResult = this.ordering.compare(lhsCurrentTerm.second, rhsCurrentTerm.second)

            if (comparisonResult == 0) {
                var result = if (operation == Operation.PLUS) {
                    Numbers.add(lhsCurrentTerm.first, rhsCurrentTerm.first)
                } else {
                    Numbers.subtract(lhsCurrentTerm.first, rhsCurrentTerm.first)
                }

                if (result.toDouble() != 0.0) {
                    resultingMonomials.add(Pair(result, lhsCurrentTerm.second))
                }

                i++
                j++
            } else if (comparisonResult < 0) {
                resultingMonomials.add(lhsCurrentTerm)
                i++
            } else {
                resultingMonomials.add(transformation(rhsCurrentTerm))
                j++
            }
        } else if (i < this.monomials.size) {
            // rhs iteration is done, just copy the rest of lhs terms into the result
            resultingMonomials.addAll(this.monomials.subList(i, this.monomials.size))
            i = this.monomials.size
        } else {
            // lhs iteration is done, just copy the rest of rhs terms into the result
            resultingMonomials.addAll(rhs.monomials.subList(j, rhs.monomials.size).map(transformation))
            j = rhs.monomials.size
        }
    }

    return Polynomial(_monomials = resultingMonomials, ordering = this.ordering)
}
