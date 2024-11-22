import core.algorithms.minimizeBasis
import core.objects.Polynomial
import core.objects.PolynomialRing
import core.order.CommonOrderings

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


    val gb = listOf(
        // g1 = x^3 - 2xy
        Polynomial(
            1.0 to ring.monomial('x' to 3),
            -2.0 to ring.monomial('x' to 1, 'y' to 1),
            ordering = CommonOrderings.gradedLexicographicOrder()
        ),
        // g2 = x^2y - 2y^2 + x
        Polynomial(
            1.0 to ring.monomial('x' to 2, 'y' to 1),
            -2.0 to ring.monomial('y' to 2),
            1.0 to ring.monomial('x' to 1),
            ordering = CommonOrderings.gradedLexicographicOrder()
        ),
        // g3 = x^2
        Polynomial(
            1.0 to ring.monomial('x' to 2),
            ordering = CommonOrderings.gradedLexicographicOrder()
        ),
        // g4 = xy
        Polynomial(
            1.0 to ring.monomial('x' to 1, 'y' to 1),
            ordering = CommonOrderings.gradedLexicographicOrder()
        ),
        // g5 = y^2 - (1/2)x
        Polynomial(
            1.0 to ring.monomial('y' to 2),
            -0.5 to ring.monomial('x' to 1),
            ordering = CommonOrderings.gradedLexicographicOrder()
        )
    )
    println(gb)

    println(minimizeBasis(gb))


//    println("Buchberger")
//    val gb = buchberger(listOf(f, g)) // - 5/2 y² + 10y - 15/2
//    gb.forEach(::println)
//
//    val f4 = f4(listOf(f, g))
//    println("F4 Grobner basis")
//    f4.forEach(::println)
}
