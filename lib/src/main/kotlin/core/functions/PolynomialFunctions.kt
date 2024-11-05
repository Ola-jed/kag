package core.functions

import core.objects.Polynomial
import core.operators.*
import utils.Numbers

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