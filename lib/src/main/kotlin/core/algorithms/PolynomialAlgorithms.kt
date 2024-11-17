package core.algorithms

import core.objects.MacaulayMatrix
import core.objects.Monomial
import core.objects.Polynomial
import core.operators.*
import utils.Numbers
import utils.cartesianProduct
import utils.generateUniquePairs
import kotlin.math.min

fun <T : Number> sPolynomial(x: Polynomial<T>, y: Polynomial<T>): Polynomial<T> {
    val xLeadingTerm = x.leadingTerm()
    val yLeadingTerm = y.leadingTerm()

    val xLeadingMonomial = xLeadingTerm.second
    val yLeadingMonomial = yLeadingTerm.second

    val lcm = lcm(xLeadingMonomial, yLeadingMonomial)
    val firstFactor = (lcm / xLeadingMonomial).toPolynomial(Numbers.inverse(xLeadingTerm.first))
    val secondFactor = (lcm / yLeadingMonomial).toPolynomial(Numbers.inverse(yLeadingTerm.first))

    return (firstFactor * x) - (secondFactor * y)
}

fun <T : Number> buchberger(polynomials: List<Polynomial<T>>): Collection<Polynomial<T>> {
    val grobnerBasis = polynomials.toMutableList()
    val criticalPairs = ArrayDeque(generateUniquePairs(polynomials))

    while (criticalPairs.isNotEmpty()) {
        val pair = criticalPairs.removeLast()
        val sPolynomial = sPolynomial(pair.first, pair.second)
        val reduction = sPolynomial % grobnerBasis

        if (reduction.monomials.isNotEmpty()) {
            criticalPairs.addAll(cartesianProduct(grobnerBasis, listOf(reduction)))
            grobnerBasis.add(reduction)
        }
    }

    return grobnerBasis
}

fun <T : Number> f4(polynomials: List<Polynomial<T>>): Collection<Polynomial<T>> {
    val grobnerBasis = polynomials.toMutableList()
    val criticalPairs = generateUniquePairs(polynomials)

    while (criticalPairs.isNotEmpty()) {
        val selectedPairs = selection(criticalPairs)
        criticalPairs.removeAll(selectedPairs)
        val newBasis = reduction(selectedPairs, grobnerBasis)
        for (polynomial in newBasis) {
            grobnerBasis.add(polynomial)
            criticalPairs.addAll(generateUniquePairs(newBasis))
        }
    }

    return grobnerBasis
}

fun <T : Number> selection(
    criticalPairs: List<Pair<Polynomial<T>, Polynomial<T>>>
): List<Pair<Polynomial<T>, Polynomial<T>>> {
    var minimumDegree = Int.MAX_VALUE
    val pairsWithLcmDegree = criticalPairs.map {
        val degree = lcm(it.first.leadingMonomial(), it.second.leadingMonomial()).degree
        minimumDegree = min(minimumDegree, degree)
        it to degree
    }

    return pairsWithLcmDegree
        .filter { it.second == minimumDegree }
        .map { it.first }
}

fun <T : Number> left(
    criticalPairs: List<Pair<Polynomial<T>, Polynomial<T>>>
): List<Polynomial<T>> {
    return criticalPairs.map {
        val lt = it.first.leadingTerm()
        val lcm = lcm(lt.second, it.second.leadingMonomial())
        val quotient = (lcm / lt.second).toPolynomial(Numbers.inverse(lt.first))
        quotient * it.first
    }
}

fun <T : Number> right(
    criticalPairs: List<Pair<Polynomial<T>, Polynomial<T>>>
): List<Polynomial<T>> {
    return criticalPairs.map {
        val lt = it.second.leadingTerm()
        val lcm = lcm(it.first.leadingMonomial(), lt.second)
        val quotient = (lcm / lt.second).toPolynomial(Numbers.inverse(lt.first))
        quotient * it.second
    }
}

fun <T : Number> reduction(
    pairs: List<Pair<Polynomial<T>, Polynomial<T>>>,
    currentBasis: List<Polynomial<T>>
): List<Polynomial<T>> {
    val list = symbolicPreprocessing(pairs, currentBasis)
    val macaulayMatrix = MacaulayMatrix(list)
    macaulayMatrix.rowEchelonReduction()
    val polynomials = macaulayMatrix.polynomials()
    val allLeadingMonomials = list.map(Polynomial<T>::leadingMonomial).toSet()
    return polynomials.filter { !allLeadingMonomials.contains(it.leadingMonomial()) }
}

fun <T : Number> symbolicPreprocessing(
    pairs: List<Pair<Polynomial<T>, Polynomial<T>>>,
    currentBasis: List<Polynomial<T>>
): List<Polynomial<T>> {
    val list = (left(pairs) + right(pairs)).toMutableList()
    val done = list.map { it.leadingMonomial() }.toMutableSet()
    var allMonomials = allMonomials(list).toMutableSet()

    while (done != allMonomials) {
        val largestMonomial = (allMonomials - done)
            .maxWith { x, y -> currentBasis[0].ordering.compare(x, y) }

        done.add(largestMonomial)
        for (polynomial in currentBasis) {
            val lm = polynomial.leadingMonomial()
            val divisionResult = largestMonomial / lm

            if (divisionResult.exponents.isNotEmpty()) {
                val polynomialAdded = divisionResult * polynomial
                list.add(divisionResult * polynomial)
                allMonomials.addAll(polynomialAdded.monomials.map { it.second }.filter { it.degree > 0 })
                break
            }
        }
    }

    return list
}

fun <T : Number> allMonomials(
    polynomials: List<Polynomial<T>>
): Set<Monomial<T>> {
    return polynomials.flatMap { it.monomials.map { it.second } }.filter { it.degree > 0 }.toSet()
}