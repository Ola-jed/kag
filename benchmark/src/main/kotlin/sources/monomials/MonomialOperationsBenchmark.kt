package sources.monomials

import config.KagConfig
import core.objects.Indeterminate
import core.objects.Monomial
import core.objects.PolynomialRing
import core.operators.*
import org.openjdk.jmh.annotations.*
import java.util.concurrent.*

@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@Measurement(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
class MonomialOperationsBenchmark {
    @Param("true", "false")
    var preconditionChecksEnabled: String = "false"
    private lateinit var ring: PolynomialRing<Int>
    private lateinit var monomial1: Monomial<Int>
    private lateinit var monomial2: Monomial<Int>
    private lateinit var monomial3: Monomial<Int>

    @Setup
    fun setUp() {
        ring = PolynomialRing<Int>(('a'..'z').map { Indeterminate(it) }.toTypedArray())

        monomial1 = Monomial<Int>(
            ring = ring,
            exponents = ('a'..'z').associate { Indeterminate(it) to 1 }
        )

        monomial2 = Monomial<Int>(
            ring = ring,
            exponents = mapOf(
                Indeterminate('a') to 10,
                Indeterminate('b') to 8,
                Indeterminate('c') to 12,
                Indeterminate('d') to 5,
                Indeterminate('e') to 15,
                Indeterminate('f') to 7,
                Indeterminate('g') to 6
            )
        )

        monomial3 = Monomial<Int>(
            ring = ring,
            exponents = mapOf(
                Indeterminate('h') to 20,
                Indeterminate('i') to 25,
                Indeterminate('j') to 30,
                Indeterminate('k') to 10,
                Indeterminate('l') to 18,
                Indeterminate('m') to 22,
                Indeterminate('n') to 24
            )
        )

        KagConfig.PRECONDITIONS_CHECKS_ENABLED = preconditionChecksEnabled == "true"
    }

    @Benchmark
    fun timesBenchmarkWithNoOverlappingIndeterminate(): Monomial<Int> {
        return monomial2 * monomial3
    }

    @Benchmark
    fun timesBenchmarkWithOverlappingIndeterminate(): Monomial<Int> {
        return monomial1 * monomial2
    }

    @Benchmark
    fun divBenchmarkWithNoOverlappingIndeterminate(): Monomial<Int> {
        return monomial2 / monomial3
    }

    @Benchmark
    fun divBenchmarkWithOverlappingIndeterminate(): Monomial<Int> {
        return monomial1  / monomial2
    }

    @Benchmark
    fun remBenchmarkWithNoOverlappingIndeterminate(): Monomial<Int> {
        return monomial2 % monomial3
    }

    @Benchmark
    fun remBenchmarkWithOverlappingIndeterminate(): Monomial<Int> {
        return monomial1 % monomial3
    }
}