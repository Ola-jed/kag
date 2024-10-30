import core.objects.Indeterminate
import core.objects.Polynomial
import core.objects.PolynomialRing
import core.order.CommonOrderings

fun main() {
    val ring = PolynomialRing<Int>("x", "y", "z")
    val xy2 = ring.monomial("x" to 1, "y" to 2)
    val yPow3z7 = ring.monomial("y" to 3, "z" to 7)
    val poly = Polynomial(1 to xy2, 1 to yPow3z7)
    println(poly.multiDegree(CommonOrderings.lexicographicOrder()))
    println(poly.leadingTerm(CommonOrderings.lexicographicOrder()))
    println(poly.leadingCoefficient(CommonOrderings.lexicographicOrder()))
    println(poly.leadingMonomial(CommonOrderings.lexicographicOrder()))
    println(xy2.toPolynomial(1).leadingMonomial(CommonOrderings.lexicographicOrder()))
}
