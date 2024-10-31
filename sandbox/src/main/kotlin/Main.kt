import core.objects.Polynomial
import core.objects.PolynomialRing
import core.operators.times

fun main() {
    val ring = PolynomialRing<Int>("x", "y", "z")
    val x4 = ring.monomial("x" to 4)
    val xy2 = ring.monomial("x" to 1, "y" to 2)
    val yPow3z2 = ring.monomial("y" to 3, "z" to 2)
    val poly = Polynomial(1 to xy2, 4 to xy2, 1 to yPow3z2, 1 to x4)

    val x2 = ring.monomial("x" to 2)
    val poly2 = Polynomial(3 to x4)

    println(poly)
    println(poly2)
    println(poly2 * 4)

}
