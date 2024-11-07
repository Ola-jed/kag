package core.objects

data class Indeterminate(val label: Char) {
    override fun toString(): String {
        return label.toString()
    }
}