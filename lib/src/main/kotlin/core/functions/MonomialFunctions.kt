package core.functions

import core.objects.Indeterminate
import core.objects.Monomial
import utils.ensure
import kotlin.math.max

fun <T : Number> lcm(x: Monomial<T>, y: Monomial<T>): Monomial<T> {
    ensure("LCM can only be computed in monomials of the same ring") {
        x.ring == y.ring
    }

    var exponents = mutableMapOf<Indeterminate, Int>()
    for (indeterminate in x.ring.indeterminates) {
        exponents[indeterminate] = max(
            x.exponents.getOrDefault(indeterminate, 0),
            y.exponents.getOrDefault(indeterminate, 0)
        )
    }

    return Monomial(ring = x.ring, exponents = exponents)
}