package core.algorithms

import core.objects.Polynomial
import core.operators.*

fun <T : Number> minimizeBasis(grobnerBasis: List<Polynomial<T>>): List<Polynomial<T>> {
    val newBasis = grobnerBasis.map { it / it.leadingCoefficient() }.toMutableList()

    val toRemove = mutableSetOf<Int>()
    val leadingTerms = newBasis.map(Polynomial<T>::leadingMonomial)
    for (i in newBasis.indices) {
        for (j in newBasis.indices) {
            if (i != j && !toRemove.contains(j) && (leadingTerms[i] % leadingTerms[j]).exponents.isEmpty()) {
                toRemove.add(i)
                break
            }
        }
    }

    toRemove.sortedDescending().forEach(newBasis::removeAt)
    for (i in newBasis.indices) {
        newBasis[i] = newBasis[i] % newBasis.filterIndexed { j, _ -> j != i }
    }

    return newBasis
}