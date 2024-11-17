package utils

import core.objects.Monomial
import core.order.MonomialOrdering

fun <T, U> cartesianProduct(c1: List<T>, c2: List<U>): MutableList<Pair<T, U>> {
    return c1.flatMap { lhsElem -> c2.map { rhsElem -> lhsElem to rhsElem } }.toMutableList()
}

fun <T> generateUniquePairs(list: List<T>): MutableList<Pair<T, T>> {
    val result = mutableListOf<Pair<T, T>>()
    for (i in list.indices) {
        for (j in i + 1 until list.size) {
            result.add(list[i] to list[j])
        }
    }

    return result
}

fun <T : Number> binarySearchMonomial(
    list: Array<Monomial<T>>,
    target: Monomial<T>,
    ordering: MonomialOrdering<T>
): Int {
    var low = 0
    var high = list.size - 1

    while (low <= high) {
        val mid = low + (high - low) / 2
        val comparison = ordering.compare(target, list[mid])

        when {
            comparison == 0 -> return mid
            comparison < 0  -> low = mid + 1
            else            -> high = mid - 1
        }
    }
    return -1
}
