package utils

object Numbers {
    @Suppress("UNCHECKED_CAST")
    fun <T : Number> add(a: T, b: T): T {
        return when {
            a is Int && b is Int       -> (a.toInt() + b.toInt()) as T
            a is Byte && b is Byte     -> (a.toByte() + b.toByte()) as T
            a is Short && b is Short   -> (a.toShort() + b.toShort()) as T
            a is Long && b is Long     -> (a.toLong() + b.toLong()) as T
            a is Float && b is Float   -> (a.toFloat() + b.toFloat()) as T
            a is Double && b is Double -> (a.toDouble() + b.toDouble()) as T
            else                       -> throw IllegalArgumentException()
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Number> subtract(a: T, b: T): T {
        return when {
            a is Int && b is Int       -> (a.toInt() - b.toInt()) as T
            a is Byte && b is Byte     -> (a.toByte() - b.toByte()) as T
            a is Short && b is Short   -> (a.toShort() - b.toShort()) as T
            a is Long && b is Long     -> (a.toLong() - b.toLong()) as T
            a is Float && b is Float   -> (a.toFloat() - b.toFloat()) as T
            a is Double && b is Double -> (a.toDouble() - b.toDouble()) as T
            else                       -> throw IllegalArgumentException()
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Number> times(a: T, b: T): T {
        return when {
            a is Int && b is Int       -> (a.toInt() * b.toInt()) as T
            a is Byte && b is Byte     -> (a.toByte() * b.toByte()) as T
            a is Short && b is Short   -> (a.toShort() * b.toShort()) as T
            a is Long && b is Long     -> (a.toLong() * b.toLong()) as T
            a is Float && b is Float   -> (a.toFloat() * b.toFloat()) as T
            a is Double && b is Double -> (a.toDouble() * b.toDouble()) as T
            else                       -> throw IllegalArgumentException()
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Number> inverse(x: T): T {
        return when (x) {
            is Int    -> (1 / x) as T
            is Byte   -> (1.toByte() / x) as T
            is Short  -> (1.toShort() / x) as T
            is Long   -> (1L / x) as T
            is Float  -> (1F / x) as T
            is Double -> (1.0 / x) as T
            else      -> throw IllegalArgumentException()
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Number> opposite(x: T): T {
        return when (x) {
            is Int    -> -x as T
            is Byte   -> -x as T
            is Short  -> -x as T
            is Long   -> -x as T
            is Float  -> -x as T
            is Double -> -x as T
            else      -> throw IllegalArgumentException()
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Number> div(a: T, b: T): T {
        return when {
            a is Int && b is Int       -> (a.toInt() / b.toInt()) as T
            a is Byte && b is Byte     -> (a.toByte() / b.toByte()) as T
            a is Short && b is Short   -> (a.toShort() / b.toShort()) as T
            a is Long && b is Long     -> (a.toLong() / b.toLong()) as T
            a is Float && b is Float   -> (a.toFloat() / b.toFloat()) as T
            a is Double && b is Double -> (a.toDouble() / b.toDouble()) as T
            else                       -> throw IllegalArgumentException()
        }
    }
}