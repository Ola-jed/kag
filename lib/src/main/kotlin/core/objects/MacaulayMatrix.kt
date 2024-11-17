package core.objects

import core.order.MonomialOrdering
import utils.Numbers
import utils.binarySearchMonomial
import utils.ensure
import java.util.Stack

class MacaulayMatrix<T : Number> {
    private var monomials: Array<Monomial<T>>
    private var coefficients: MutableList<MutableList<T>>
    private var zero: T
    private var ordering: MonomialOrdering<T>

    constructor(polynomials: List<Polynomial<T>>) {
        ordering = polynomials[0].ordering
        var allMonomials = sortedSetOf<Monomial<T>>(Comparator<Monomial<T>> { a, b -> -1 * ordering.compare(a, b) })
        for (polynomial in polynomials) {
            for (pair in polynomial.monomials) {
                allMonomials.add(pair.second)
            }
        }

        monomials = allMonomials.toTypedArray()
        zero = Numbers.subtract(polynomials[0].monomials[0].first, polynomials[0].monomials[0].first)
        coefficients = MutableList(polynomials.size) { MutableList(monomials.size) { zero } }

        var polynomialIndex = 0
        for (polynomial in polynomials) {
            for (monomialAndCoefficient in polynomial.monomials) {
                val monomialIndex = binarySearchMonomial(monomials, monomialAndCoefficient.second, ordering)
                coefficients[polynomialIndex][monomialIndex] = monomialAndCoefficient.first
            }

            polynomialIndex++
        }
    }

    fun rowEchelonReduction() {
        val pivots = Stack<Pair<Int, Int>>()
        var leftmostNonZeroColumnIndex = findLeftmostNonZeroColumn()
        if (leftmostNonZeroColumnIndex == -1) {
            return
        }

        var pivot = makeTopmostColumnElementOne(leftmostNonZeroColumnIndex)
        pivots.push(pivot.copy())
        putZerosUnderPivot(pivot.first, pivot.second)
        while (nonZeroRowUnderPivot(pivot.first)) {
            // Consider pivot
            // Get leftmost non-zero column
            // Make topmost according to pivot one
            // Put zeros under pivot

            leftmostNonZeroColumnIndex = findLeftmostNonZeroColumn(pivot.first, pivot.second)
            if (leftmostNonZeroColumnIndex == -1) {
                break
            }

            pivot = makeTopmostColumnElementOne(leftmostNonZeroColumnIndex, pivot.first)
            pivots.push(pivot.copy())
            putZerosUnderPivot(pivot.first, pivot.second)
        }

        // The matrix is in row-echelon form
        // Pop from the stack all the pivots, and then, erase all non-zero entries above
        while (pivots.isNotEmpty()) {
            val p = pivots.pop()
            putZerosAbovePivot(p.first, p.second)
        }
    }

    fun polynomials(): List<Polynomial<T>> {
        return coefficients.map { coefficientRow ->
            val m = mutableListOf<Pair<T, Monomial<T>>>()
            for (i in monomials.indices) {
                if (coefficientRow[i] != zero) {
                    m.add(coefficientRow[i] to monomials[i])
                }
            }

            Polynomial(m, ordering)
        }.filter { it.monomials.isNotEmpty() }
    }

    // Elementary row operations
    fun rowSwap(x: Int, y: Int) {
        ensure("Invalid row index") {
            x >= 0 && x < coefficients.size && y >= 0 && y < coefficients.size
        }

        val temp = coefficients[x]
        coefficients[x] = coefficients[y]
        coefficients[y] = temp
    }

    fun scalarMultiplication(x: Int, scalar: T) {
        ensure("Invalid row index") {
            x >= 0 && x < coefficients.size
        }

        for (i in coefficients[x].indices) {
            coefficients[x][i] = Numbers.times(coefficients[x][i], scalar)
        }
    }

    fun difference(x: Int, y: Int, yScalar: T) {
        ensure("Invalid row index") {
            x >= 0 && x < coefficients.size && y >= 0 && y < coefficients.size
        }

        for (i in coefficients[x].indices) {
            coefficients[x][i] = Numbers.subtract(coefficients[x][i], Numbers.times(coefficients[y][i], yScalar))
        }
    }

    // Util methods for reduced row echelon
    fun findLeftmostNonZeroColumn(x: Int = -1, y: Int = -1): Int {
        for (j in y + 1 until coefficients[0].size) {
            for (i in x + 1 until coefficients.size) {
                if (coefficients[i][j] != zero) {
                    return j
                }
            }
        }

        return -1
    }

    fun makeTopmostColumnElementOne(y: Int, pivotRow: Int = -1): Pair<Int, Int> {
        for (x in pivotRow + 1 until coefficients.size) {
            if (coefficients[x][y] != zero) {
                scalarMultiplication(x, Numbers.inverse(coefficients[x][y]))
                rowSwap(x, pivotRow + 1)
                return pivotRow + 1 to y
            }
        }


        return -1 to -1 // Should never happen
    }

    fun putZerosUnderPivot(x: Int, y: Int) {
        for (i in x + 1 until coefficients.size) {
            // Line i, add -1 * coefficients [i][y] * pivot line to make it zero
            // difference(i, x, coefficients[i][y])
            difference(i, x, coefficients[i][y])
        }
    }

    fun nonZeroRowUnderPivot(x: Int): Boolean {
        for (i in x + 1 until coefficients.size) {
            for (j in coefficients[i].indices) {
                if (coefficients[i][j] != zero) {
                    return true
                }
            }
        }

        return false
    }

    fun putZerosAbovePivot(x: Int, y: Int) {
        for (i in x - 1 downTo 0) {
            // Line i, add -1 * coefficients [i][y] * pivot line to make it zero
            // difference(i, x, coefficients[i][y])
            difference(i, x, coefficients[i][y])
        }
    }

    override fun toString(): String {
        val builder = StringBuilder()
            .append(monomials.joinToString(", ") { it.toString() })
            .append("\n")
        coefficients.forEach { builder.append(it.joinToString(", ")).append("\n") }
        return builder.toString()
    }
}