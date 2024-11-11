package parser.tokens

enum class TokenType {
    MINUS,
    PLUS,
    DIVIDE,
    MODULO,
    EXPONENT,
    NUMBER,
    INDETERMINATE,
    LEFT_PARENTHESIS,
    RIGHT_PARENTHESIS,
    EOF;

    companion object {
        val mappings = mapOf(
            '-' to MINUS,
            '+' to PLUS,
            '/' to DIVIDE,
            '%' to MODULO,
            '^' to EXPONENT,
            '(' to LEFT_PARENTHESIS,
            ')' to RIGHT_PARENTHESIS
        )
    }
}