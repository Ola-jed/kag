import core.objects.Polynomial
import core.objects.PolynomialRing
import core.operators.*

fun main() {
    val ring = PolynomialRing<Int>('x', 'y')

    val xy = ring.monomial('x' to 1, 'y' to 1)
    val xy2 = ring.monomial('x' to 1, 'y' to 2)
    val x = ring.monomial('x' to 1)
    val y2 = ring.monomial('y' to 2)
    val one = ring.monomial('x' to 0)


    val f = Polynomial<Int>(1 to xy2, -1 to x)
    val g = Polynomial<Int>(1 to xy, 1 to one)
    val h = Polynomial<Int>(1 to y2, -1 to one)

    val polynomials = arrayOf(h, g)

    val result = f / polynomials
    for (i in result.first.indices) {
        println("${result.first[i]} for ${polynomials[i]}")
    }

    println("Remainder : ${result.second}")
}
