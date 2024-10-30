package core.objects

data class Indeterminate(val label: String) {
    override fun toString(): String {
        return label
    }
}