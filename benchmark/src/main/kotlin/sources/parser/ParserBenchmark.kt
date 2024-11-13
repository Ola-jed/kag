package sources.parser

import core.objects.Polynomial
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.BenchmarkMode
import org.openjdk.jmh.annotations.Measurement
import org.openjdk.jmh.annotations.Mode
import org.openjdk.jmh.annotations.Param
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.State
import parser.Lexer
import parser.Parser
import java.util.concurrent.TimeUnit

@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@Measurement(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
class ParserBenchmark {
    @Param(
        "x + y + z + w",
        "x^2 + 2xy - y^2 + z^3 - w",
        "2a^3b^2 + 3c^2d - 4e + 5",
        "p^5q^3 - 7pq^2r + r^2s - t",
        "u^4v^3w - 3uvw + v^2 + 2",
        "x^10y^5z^2 - 4y^8z^6 + 3",
        "x^100 + y^50 + z - 1",
        "-3x^2y^2 + 7y^3z - 5z^4",
        "x + y + z + w - 0.5",
        "a^3b - b^3c + c^2d - 4d^5e + f"
    )
    var polynomialStr: String = ""

    @Benchmark
    fun parse(): Polynomial<Double> {
        return Parser(Lexer().scan(polynomialStr)).parse()
    }
}