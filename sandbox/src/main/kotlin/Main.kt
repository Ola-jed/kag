import core.algorithms.buchberger
import core.algorithms.f4
import core.objects.Polynomial
import core.objects.PolynomialRing

fun main() {
    val ring = PolynomialRing<Double>('x', 'y')
    val x = ring.monomial('x' to 1)
    val x2 = ring.monomial('x' to 2)
    val y = ring.monomial('y' to 1)
    val xy = ring.monomial('x' to 1, 'y' to 1)
    val y2 = ring.monomial('y' to 2)
    val one = ring.monomial()

    val f = Polynomial<Double>(2.0 to x2, -4.0 to x, 1.0 to y2, -4.0 to y, 3.0 to one)
    val g = Polynomial<Double>(1.0 to x2, -2.0 to x, 3.0 to y2, -12.0 to y, 9.0 to one)

    println("Buchberger")
    val gb = buchberger(listOf(f, g)) // - 5/2 y² + 10y - 15/2
    gb.forEach(::println)

    val f4 = f4(listOf(f, g))
    println("F4 Grobner basis")
    f4.forEach(::println)
}
