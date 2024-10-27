package core.objects

data class Polynomial<T : Number>(val monomials: List<Pair<T, Monomial<T>>>) {
    val degree: Int by lazy {
        monomials.map { it.second.degree }.max()
    }

    constructor(vararg monomials: Pair<T, Monomial<T>>) : this(monomials.asList())

    // TODO, given an ordering, get the multidegree, leading term, leading monomial, the leading coefficient
}