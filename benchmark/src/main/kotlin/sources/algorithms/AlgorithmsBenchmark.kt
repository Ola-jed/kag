package sources.algorithms

import config.KagConfig
import core.algorithms.buchberger
import core.algorithms.f4
import core.objects.Indeterminate
import core.objects.Monomial
import core.objects.Polynomial
import core.objects.PolynomialRing
import core.order.CommonOrderings
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.BenchmarkMode
import org.openjdk.jmh.annotations.Measurement
import org.openjdk.jmh.annotations.Mode
import org.openjdk.jmh.annotations.Param
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.Setup
import org.openjdk.jmh.annotations.State
import java.util.concurrent.TimeUnit

@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@Measurement(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
class AlgorithmsBenchmark {
    @Param(
        "lexicographicOrder",
        "gradedLexicographicOrder",
        "gradedReverseLexicographicOrder",
        "eliminationOrder",
        "weightOrder"
    )
    var orderingType: String = ""

    @Param("true", "false")
    var preconditionChecksEnabled: String = "false"
    private lateinit var ring: PolynomialRing<Double>
    private lateinit var polynomial1: Polynomial<Double>
    private lateinit var polynomial2: Polynomial<Double>

    @Setup
    fun setUp() {
        ring = PolynomialRing<Double>('a', 'b', 'c')

        val a = Monomial(ring = ring, exponents = mapOf(Indeterminate('a') to 1))
        val b = Monomial(ring = ring, exponents = mapOf(Indeterminate('b') to 1))
        val c = Monomial(ring = ring, exponents = mapOf(Indeterminate('c') to 1))
        val a2 = Monomial(ring = ring, exponents = mapOf(Indeterminate('a') to 2))
        val b2 = Monomial(ring = ring, exponents = mapOf(Indeterminate('b') to 2))
        val c2 = Monomial(ring = ring, exponents = mapOf(Indeterminate('c') to 2))

        polynomial1 = Polynomial(
            1.0 to a,
            1.0 to b,
            1.0 to c,
            1.0 to a2,
            1.0 to b2,
            1.0 to c2,
            ordering = when (orderingType) {
                "lexicographicOrder"              -> CommonOrderings.lexicographicOrder()
                "gradedLexicographicOrder"        -> CommonOrderings.gradedLexicographicOrder()
                "gradedReverseLexicographicOrder" -> CommonOrderings.gradedReverseLexicographicOrder()
                "weightOrder"                     -> CommonOrderings.weightOrder(
                    weights = mapOf(
                        Indeterminate('a') to 1,
                        Indeterminate('b') to 2,
                        Indeterminate('c') to 3,
                    )
                )

                else                              -> CommonOrderings.eliminationOrder(
                    eliminationVariables = setOf(Indeterminate('a')),
                    retainedVariables = setOf(Indeterminate('b'), Indeterminate('c'))
                )
            }
        )

        polynomial2 = Polynomial(
            1.0 to a2,
            1.0 to b2,
            1.0 to c2,
            ordering = when (orderingType) {
                "lexicographicOrder"              -> CommonOrderings.lexicographicOrder()
                "gradedLexicographicOrder"        -> CommonOrderings.gradedLexicographicOrder()
                "gradedReverseLexicographicOrder" -> CommonOrderings.gradedReverseLexicographicOrder()
                "weightOrder"                     -> CommonOrderings.weightOrder(
                    weights = mapOf(Indeterminate('a') to 1, Indeterminate('b') to 2, Indeterminate('c') to 3)
                )

                else                              -> CommonOrderings.eliminationOrder(
                    eliminationVariables = setOf(Indeterminate('a')),
                    retainedVariables = setOf(Indeterminate('b'), Indeterminate('c'))
                )
            }
        )

        KagConfig.PRECONDITIONS_CHECKS_ENABLED = preconditionChecksEnabled == "true"
    }

    @Benchmark
    fun buchbergerBenchmark(): Collection<Polynomial<Double>> {
        return buchberger(listOf(polynomial1, polynomial2))
    }

    @Benchmark
    fun f4Benchmark(): Collection<Polynomial<Double>> {
        return f4(listOf(polynomial1, polynomial2))
    }
}