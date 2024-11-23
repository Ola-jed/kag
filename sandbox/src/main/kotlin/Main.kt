import core.algorithms.minimizeBasis
import core.algorithms.reduceBasis
import core.objects.Polynomial
import core.objects.PolynomialRing
import core.order.CommonOrderings

fun main() {
    val ring = PolynomialRing<Double>('x', 'y', 'z', 'w')
    val x = ring.monomial('x' to 1)
    val y = ring.monomial('y' to 1)
    val z = ring.monomial('z' to 1)
    val w = ring.monomial('w' to 1)

    val ordering = CommonOrderings.gradedLexicographicOrder<Double>()

    val gb = listOf(
        Polynomial(3.0 to x, -6.0 to y, -2.0 to z, ordering = ordering),
        Polynomial(2.0 to x, -4.0 to y, -4.0 to w, ordering = ordering),
        Polynomial(1.0 to x, -2.0 to y, -1.0 to z, -1.0 to w, ordering = ordering),
        Polynomial(1.0 to z, 3.0 to w, ordering = ordering),
    )
    println(gb)
    println("====")
    println(minimizeBasis(gb))
    println("====")
    println(reduceBasis(gb))


//    println("Buchberger")
//    val gb = buchberger(listOf(f, g)) // - 5/2 y² + 10y - 15/2
//    gb.forEach(::println)
//
//    val f4 = f4(listOf(f, g))
//    println("F4 Grobner basis")
//    f4.forEach(::println)
}
