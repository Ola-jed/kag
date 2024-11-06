package core.operators

import core.Operation
import core.objects.Monomial
import core.objects.Polynomial
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

fun <T : Number> Polynomial<T>.op(rhs: Polynomial<T>, operation: Operation): Polynomial<T> {
    var i = 0
    var j = 0
    var resultingMonomials = mutableListOf<Pair<T, Monomial<T>>>()

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
                resultingMonomials.add(rhsCurrentTerm)
                j++
            }
        } else if (i < this.monomials.size) {
            // rhs iteration is done, just copy the rest of lhs terms into the result
            resultingMonomials.addAll(this.monomials.subList(i, this.monomials.size))
            i = this.monomials.size
        } else {
            // lhs iteration is done, just copy the rest of rhs terms into the result
            resultingMonomials.addAll(rhs.monomials.subList(j, rhs.monomials.size))
            j = rhs.monomials.size
        }
    }

    return Polynomial(_monomials = resultingMonomials, ordering = this.ordering)
}
