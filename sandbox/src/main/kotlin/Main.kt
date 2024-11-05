import core.functions.sPolynomial
import core.objects.Indeterminate
import core.objects.Monomial
import core.objects.Polynomial
import core.objects.PolynomialRing

fun main() {
    val ring = PolynomialRing<Double>("x", "y", "z")

    val x3y = Monomial(ring = ring, mapOf(Indeterminate("x") to 3, Indeterminate("y") to 1))
    val xy = Monomial(ring = ring, mapOf(Indeterminate("x") to 1, Indeterminate("y") to 1))
    val y2 = Monomial(ring = ring, mapOf(Indeterminate("y") to 2))

    val xy2 = Monomial(ring = ring, mapOf(Indeterminate("x") to 1, Indeterminate("y") to 2))
    val y3 = Monomial(ring = ring, mapOf(Indeterminate("y") to 3))

    val f1 = Polynomial(3.0 to x3y, 2.0 to xy, -1.0 to y2)
    val f2 = Polynomial(2.0 to xy2, -5.0 to y3)

    println(sPolynomial(f1, f2))
}
