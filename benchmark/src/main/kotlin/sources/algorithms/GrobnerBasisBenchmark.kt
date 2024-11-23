package sources.algorithms

import core.algorithms.minimizeBasis
import core.algorithms.reduceBasis
import core.objects.Polynomial
import core.objects.PolynomialRing
import core.order.CommonOrderings
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.BenchmarkMode
import org.openjdk.jmh.annotations.Measurement
import org.openjdk.jmh.annotations.Mode
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.Setup
import org.openjdk.jmh.annotations.State
import java.util.concurrent.TimeUnit

@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@Measurement(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS)
class GrobnerBasisBenchmark {
    private lateinit var grobnerBasis: Collection<Polynomial<Double>>

    @Setup
    fun setUp() {
        val ring = PolynomialRing<Double>('x', 'y', 'z', 'w')
        val x = ring.monomial('x' to 1)
        val y = ring.monomial('y' to 1)
        val z = ring.monomial('z' to 1)
        val w = ring.monomial('w' to 1)

        val ordering = CommonOrderings.gradedLexicographicOrder<Double>()

        grobnerBasis = listOf(
            Polynomial(3.0 to x, -6.0 to y, -2.0 to z, ordering = ordering),
            Polynomial(2.0 to x, -4.0 to y, -4.0 to w, ordering = ordering),
            Polynomial(1.0 to x, -2.0 to y, -1.0 to z, -1.0 to w, ordering = ordering),
            Polynomial(1.0 to z, 3.0 to w, ordering = ordering),
        )
    }

    @Benchmark
    fun minimizationBenchmark(): Collection<Polynomial<Double>> {
        return minimizeBasis(grobnerBasis)
    }

    @Benchmark
    fun reductionBenchmark(): Collection<Polynomial<Double>> {
        return reduceBasis(grobnerBasis)
    }
}