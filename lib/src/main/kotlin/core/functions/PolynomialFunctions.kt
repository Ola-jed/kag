package core.functions

import core.objects.Polynomial
import core.operators.*
import utils.Numbers
import utils.cartesianProduct
import utils.generateUniquePairs

fun <T : Number> sPolynomial(x: Polynomial<T>, y: Polynomial<T>): Polynomial<T> {
    val xLeadingTerm = x.leadingTerm()
    val yLeadingTerm = y.leadingTerm()

    val xLeadingMonomial = xLeadingTerm.second
    val yLeadingMonomial = yLeadingTerm.second

    val lcm = lcm(xLeadingMonomial, yLeadingMonomial)
    val firstFactor = (lcm / xLeadingMonomial).toPolynomial(Numbers.inverse<T>(xLeadingTerm.first))
    val secondFactor = (lcm / yLeadingMonomial).toPolynomial(Numbers.inverse<T>(yLeadingTerm.first))

    return (firstFactor * x) - (secondFactor * y)
}

fun <T : Number> buchberger(polynomials: List<Polynomial<T>>): Collection<Polynomial<T>> {
    val grobnerBasis = polynomials.toMutableList()
    val criticalPairs = ArrayDeque(generateUniquePairs(polynomials))

    while (criticalPairs.isNotEmpty()) {
        val pair = criticalPairs.removeLast()
        val sPolynomial = sPolynomial(pair.first, pair.second)
        val reduction = sPolynomial % grobnerBasis

        if (reduction.monomials.isNotEmpty()) {
            criticalPairs.addAll(cartesianProduct(grobnerBasis, listOf(reduction)))
            grobnerBasis.add(reduction)
        }
    }

    return grobnerBasis
}