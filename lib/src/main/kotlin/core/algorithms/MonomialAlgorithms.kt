package core.algorithms

import core.objects.Monomial
import utils.ensure
import kotlin.math.max

fun <T : Number> lcm(x: Monomial<T>, y: Monomial<T>): Monomial<T> {
    ensure("LCM can only be computed in monomials of the same ring") {
        x.ring == y.ring
    }

    val exponents = x.ring.indeterminates.associateWith { max(x.exponents[it] ?: 0, y.exponents[it] ?: 0) }
    return Monomial(ring = x.ring, exponents = exponents)
}