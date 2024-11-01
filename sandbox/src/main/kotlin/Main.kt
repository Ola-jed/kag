import core.objects.Polynomial
import core.objects.PolynomialRing
import core.operators.times
import core.operators.div
import core.operators.rem

fun main() {
    val ring = PolynomialRing<Int>("x", "y", "z")
    val x4 = ring.monomial("x" to 4)
    val xy2 = ring.monomial("x" to 1, "y" to 2)
    val yPow3z2 = ring.monomial("y" to 3, "z" to 2)
    val poly = Polynomial(1 to xy2, 4 to xy2, 1 to yPow3z2, 1 to x4)

    val x2 = ring.monomial("x" to 2)
    val y = ring.monomial("y" to 1)
    val poly2 = Polynomial(3 to x4)

    val a = y * poly
    val b = poly * y

    println(a)
    println(b)
}
