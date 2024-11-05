package sources.polynomials

import config.KagConfig
import core.objects.Indeterminate
import core.objects.Monomial
import core.objects.Polynomial
import core.objects.PolynomialRing
import core.order.CommonOrderings
import core.operators.*
import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.annotations.BenchmarkMode
import java.util.concurrent.TimeUnit

@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@Measurement(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
class PolynomialOperationsBenchmark {
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
    private lateinit var ring: PolynomialRing<Int>
    private lateinit var polynomial1: Polynomial<Int>
    private lateinit var polynomial2: Polynomial<Int>

    @Setup
    fun setUp() {
        ring = PolynomialRing<Int>("a", "b", "c")

        val a = Monomial<Int>(ring = ring, exponents = mapOf(Indeterminate("a") to 1))
        val b = Monomial<Int>(ring = ring, exponents = mapOf(Indeterminate("b") to 1))
        val c = Monomial<Int>(ring = ring, exponents = mapOf(Indeterminate("c") to 1))
        val a2 = Monomial<Int>(ring = ring, exponents = mapOf(Indeterminate("a") to 2))
        val b2 = Monomial<Int>(ring = ring, exponents = mapOf(Indeterminate("b") to 2))
        val c2 = Monomial<Int>(ring = ring, exponents = mapOf(Indeterminate("c") to 2))

        polynomial1 = Polynomial(
            1 to a,
            1 to b,
            1 to c,
            1 to a2,
            1 to b2,
            1 to c2,
            ordering = when (orderingType) {
                "lexicographicOrder"              -> CommonOrderings.lexicographicOrder<Int>()
                "gradedLexicographicOrder"        -> CommonOrderings.gradedLexicographicOrder<Int>()
                "gradedReverseLexicographicOrder" -> CommonOrderings.gradedReverseLexicographicOrder<Int>()
                "weightOrder"                     -> CommonOrderings.weightOrder<Int>(
                    weights = mapOf(
                        Indeterminate("a") to 1,
                        Indeterminate("b") to 2,
                        Indeterminate("c") to 3,
                    )
                )

                else                              -> CommonOrderings.eliminationOrder<Int>(
                    eliminationVariables = setOf(Indeterminate("a")),
                    retainedVariables = setOf(Indeterminate("b"), Indeterminate("c"))
                )
            }
        )

        polynomial2 = Polynomial(
            1 to a2,
            1 to b2,
            1 to c2,
            ordering = when (orderingType) {
                "lexicographicOrder"              -> CommonOrderings.lexicographicOrder<Int>()
                "gradedLexicographicOrder"        -> CommonOrderings.gradedLexicographicOrder<Int>()
                "gradedReverseLexicographicOrder" -> CommonOrderings.gradedReverseLexicographicOrder<Int>()
                "weightOrder"                     -> CommonOrderings.weightOrder<Int>(
                    weights = mapOf(
                        Indeterminate("a") to 1,
                        Indeterminate("b") to 2,
                        Indeterminate("c") to 3,
                    )
                )

                else                              -> CommonOrderings.eliminationOrder<Int>(
                    eliminationVariables = setOf(Indeterminate("a")),
                    retainedVariables = setOf(Indeterminate("b"), Indeterminate("c"))
                )
            }
        )

        KagConfig.PRECONDITIONS_CHECKS_ENABLED = preconditionChecksEnabled == "true"
    }

    @Benchmark
    fun plus(): Polynomial<Int> {
        return polynomial1 + polynomial2
    }

    @Benchmark
    fun minus(): Polynomial<Int> {
        return polynomial1 - polynomial2

    }

    @Benchmark
    fun times(): Polynomial<Int> {
        return polynomial1 * polynomial2
    }
}