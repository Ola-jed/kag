package extensions

import core.objects.Monomial
import core.objects.Polynomial
import core.order.CommonOrderings
import core.order.MonomialOrdering

fun <T : Number> Pair<T, Monomial<T>>.toPolynomial(
    ordering: MonomialOrdering<T> = CommonOrderings.lexicographicOrder()
): Polynomial<T> {
    return Polynomial(_monomials = listOf(this), ordering)
}