package sources

import core.objects.Indeterminate
import core.objects.Monomial
import core.objects.PolynomialRing
import core.operators.*
import org.openjdk.jmh.annotations.*
import java.util.concurrent.*

@State(Scope.Benchmark)
@Fork(1)
@Measurement(iterations = 10, time = 5, timeUnit = TimeUnit.SECONDS)
class MonomialBenchmark {
    private lateinit var ring: PolynomialRing<Int>
    private lateinit var monomial1: Monomial<Int>
    private lateinit var monomial2: Monomial<Int>
    private lateinit var monomial3: Monomial<Int>

    @Setup
    fun setUp() {
        ring = PolynomialRing<Int>("a", "b", "c", "d", "e")

        monomial1 = Monomial<Int>(
            ring = ring,
            exponents = mapOf(
                Indeterminate("a") to 1,
                Indeterminate("b") to 1,
                Indeterminate("c") to 1,
                Indeterminate("d") to 1,
                Indeterminate("e") to 1
            )
        )

        monomial2 = Monomial<Int>(
            ring = ring,
            exponents = mapOf(
                Indeterminate("a") to 2,
                Indeterminate("b") to 2,
                Indeterminate("c") to 2
            )
        )

        monomial3 = Monomial<Int>(
            ring = ring,
            exponents = mapOf(
                Indeterminate("d") to 2,
                Indeterminate("e") to 2,
            )
        )
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