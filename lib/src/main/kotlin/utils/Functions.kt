package utils

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
