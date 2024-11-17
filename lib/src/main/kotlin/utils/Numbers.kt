package utils

object Numbers {
    @Suppress("UNCHECKED_CAST")
    fun <T : Number> add(a: T, b: T): T {
        return when {
            a is Int && b is Int       -> (a.toInt() + b.toInt())
            a is Byte && b is Byte     -> (a.toByte() + b.toByte())
            a is Short && b is Short   -> (a.toShort() + b.toShort())
            a is Long && b is Long     -> (a.toLong() + b.toLong())
            a is Float && b is Float   -> (a.toFloat() + b.toFloat())
            a is Double && b is Double -> (a.toDouble() + b.toDouble())
            else                       -> throw IllegalArgumentException()
        } as T
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Number> subtract(a: T, b: T): T {
        return when {
            a is Int && b is Int       -> (a.toInt() - b.toInt())
            a is Byte && b is Byte     -> (a.toByte() - b.toByte())
            a is Short && b is Short   -> (a.toShort() - b.toShort())
            a is Long && b is Long     -> (a.toLong() - b.toLong())
            a is Float && b is Float   -> (a.toFloat() - b.toFloat())
            a is Double && b is Double -> (a.toDouble() - b.toDouble())
            else                       -> throw IllegalArgumentException()
        } as T
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Number> times(a: T, b: T): T {
        return when {
            a is Int && b is Int       -> (a.toInt() * b.toInt())
            a is Byte && b is Byte     -> (a.toByte() * b.toByte())
            a is Short && b is Short   -> (a.toShort() * b.toShort())
            a is Long && b is Long     -> (a.toLong() * b.toLong())
            a is Float && b is Float   -> (a.toFloat() * b.toFloat())
            a is Double && b is Double -> (a.toDouble() * b.toDouble())
            else                       -> throw IllegalArgumentException()
        } as T
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Number> inverse(x: T): T {
        return when (x) {
            is Int    -> (1 / x)
            is Byte   -> (1.toByte() / x)
            is Short  -> (1.toShort() / x)
            is Long   -> (1L / x)
            is Float  -> (1F / x)
            is Double -> (1.0 / x)
            else      -> throw IllegalArgumentException()
        } as T
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Number> opposite(x: T): T {
        return when (x) {
            is Int    -> -x
            is Byte   -> -x
            is Short  -> -x
            is Long   -> -x
            is Float  -> -x
            is Double -> -x
            else      -> throw IllegalArgumentException()
        } as T
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Number> div(a: T, b: T): T {
        return when {
            a is Int && b is Int       -> (a.toInt() / b.toInt())
            a is Byte && b is Byte     -> (a.toByte() / b.toByte())
            a is Short && b is Short   -> (a.toShort() / b.toShort())
            a is Long && b is Long     -> (a.toLong() / b.toLong())
            a is Float && b is Float   -> (a.toFloat() / b.toFloat())
            a is Double && b is Double -> (a.toDouble() / b.toDouble())
            else                       -> throw IllegalArgumentException()
        } as T
    }

    fun <T : Number> isZero(value: T): Boolean {
        return when (value) {
            is Int    -> value == 0
            is Long   -> value == 0L
            is Float  -> value == 0.0f
            is Double -> value == 0.0
            is Short  -> value == 0.toShort()
            is Byte   -> value == 0.toByte()
            else      -> throw IllegalArgumentException("Unsupported number type")
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Number> one(value: T): T {
        return when (value) {
            is Int    -> 1
            is Long   -> 1L
            is Float  -> 1.0f
            is Double -> 1.0
            is Short  -> 1.toShort()
            is Byte   -> 1.toByte()
            else      -> throw IllegalArgumentException("Unsupported number type")
        } as T
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Number> zero(value: T): T {
        return when (value) {
            is Int    -> 0
            is Long   -> 0L
            is Float  -> 0.0f
            is Double -> 0.0
            is Short  -> 0.toShort()
            is Byte   -> 0.toByte()
            else      -> throw IllegalArgumentException("Unsupported number type")
        } as T
    }
}