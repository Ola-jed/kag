package core.algorithms

import core.objects.Polynomial
import core.operators.*

fun <T : Number> minimizeBasis(grobnerBasis: Collection<Polynomial<T>>): Collection<Polynomial<T>> {
    val monicBasis = grobnerBasis.map { it / it.leadingCoefficient() }
    val toRemove = mutableSetOf<Int>()
    val leadingTerms = monicBasis.map(Polynomial<T>::leadingMonomial)

    for (i in monicBasis.indices) {
        for (j in monicBasis.indices) {
            if (i != j && !toRemove.contains(j) && (leadingTerms[i] % leadingTerms[j]).exponents.isEmpty()) {
                toRemove.add(i)
                break
            }
        }
    }

    return monicBasis.filterIndexed { index, _ -> !toRemove.contains(index) }
}

fun <T : Number> reduceBasis(grobnerBasis: Collection<Polynomial<T>>): Collection<Polynomial<T>> {
    val minimizedBasis = minimizeBasis(grobnerBasis).toMutableList()
    for (i in minimizedBasis.indices) {
        minimizedBasis[i] = minimizedBasis[i] % minimizedBasis.filterIndexed { j, _ -> j != i }
    }

    return minimizedBasis
}