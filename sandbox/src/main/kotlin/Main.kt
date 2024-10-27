import core.objects.PolynomialRing
import core.order.CommonOrderings

fun main() {
    val ring = PolynomialRing<Int>("x", "y")
    val x = ring.monomial("x" to 1)
    val yPow3 = ring.monomial("y" to 3)

    println(CommonOrderings.lexicographicOrder<Int>().compare(x, yPow3))
    println(CommonOrderings.gradedLexicographicOrder<Int>().compare(x, yPow3))
}
