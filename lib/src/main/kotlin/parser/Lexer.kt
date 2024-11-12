package parser

import parser.tokens.Token
import parser.tokens.TokenType


class Lexer {
    private val tokens: MutableList<Token> = mutableListOf()
    private var start: Int = 0
    private var current: Int = 0
    private lateinit var source: String

    fun scan(source: String): List<Token> {
        tokens.clear()
        start = 0
        current = 0
        this.source = source

        while (!isAtEnd()) {
            start = current
            scanToken()
        }

        tokens.add(Token(TokenType.EOF))
        return tokens
    }


    private fun scanToken() {
        when (val char = advance()) {
            in TokenType.mappings.keys -> tokens.add(Token(TokenType.mappings[char]!!))
            else                       -> {
                if (isDigit(char)) {
                    number()
                } else if (isAlpha(char)) {
                    tokens.add(Token(TokenType.INDETERMINATE, char))
                } else if (char.isWhitespace()) {
                    // A space, nothing to do
                } else {
                    throw IllegalArgumentException("Unexpected character $char")
                }
            }
        }
    }

    private fun number() {
        while (isDigit(peek())) {
            advance()
        }

        if (peek() == '.' && isDigit(peekNext())) {
            advance()
            while (isDigit(peek())) advance()
        }

        tokens.add(Token(TokenType.NUMBER, source.substring(start, current).toDouble()))
    }

    private fun isDigit(char: Char): Boolean {
        return char in '0'..'9'
    }

    private fun isAlpha(char: Char): Boolean {
        return (char in 'a'..'z') || (char in 'A'..'Z')
    }

    private fun advance(): Char {
        current++
        return source[current - 1]
    }

    private fun peek(): Char {
        return if (isAtEnd()) 0.toChar() else source[current]
    }

    private fun peekNext(): Char {
        if (current + 1 >= source.length) return 0.toChar()
        return source[current + 1]
    }


    private fun isAtEnd(): Boolean {
        return current >= source.length
    }
}