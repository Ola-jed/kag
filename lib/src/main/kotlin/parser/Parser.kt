package parser

import core.objects.Indeterminate
import core.objects.Monomial
import core.objects.Polynomial
import core.objects.PolynomialRing
import parser.tokens.Token
import parser.tokens.TokenType

class Parser(val tokens: List<Token>) {
    private lateinit var ring: PolynomialRing<Double>
    private var current: Int = 0
    private var firstPair = true

    fun parse(): Polynomial<Double> {
        // First pass, get all the indeterminates and sort them lexically to build the ring
        val indeterminates = tokens.filter { it.type == TokenType.INDETERMINATE }
            .map { it.value as Char }
            .toSet()
            .sortedBy { it.lowercaseChar() }
            .map { Indeterminate(it) }
            .toTypedArray()
        ring = PolynomialRing(indeterminates)
        val terms = mutableListOf<Pair<Double, Monomial<Double>>>()
        while (!isAtEnd()) {
            val term = term()
            terms.add(term)
        }

        return Polynomial(_monomials = terms)
    }

    private fun term(): Pair<Double, Monomial<Double>> {
        var coefficient = 1.0

        if (!firstPair && !checkType(TokenType.PLUS, TokenType.MINUS)) {
            throw IllegalStateException("Expected operator before term.")
        }

        if (checkType(TokenType.NUMBER)) {
            coefficient = advance().value as Double
        } else if (match(TokenType.MINUS)) {
            coefficient = if (!checkType(TokenType.NUMBER)) -1.0 else -1 * advance().value as Double
        } else if (match(TokenType.PLUS)) {
            coefficient = (if (!checkType(TokenType.NUMBER)) 1.0 else advance().value as Double)
        }

        firstPair = false
        return coefficient to monomial()
    }


    private fun monomial(): Monomial<Double> {
        val exponents = mutableMapOf<Indeterminate, Int>()

        while (checkType(TokenType.INDETERMINATE)) {
            val singleTerm = singleTermExpression()
            exponents.put(singleTerm.first, singleTerm.second)
        }

        return Monomial(ring, exponents = exponents)
    }

    private fun singleTermExpression(): Pair<Indeterminate, Int> {
        if (!checkType(TokenType.INDETERMINATE)) {
            throw IllegalStateException("Expected indeterminate in single term expression.")
        }

        val indeterminate = Indeterminate(advance().value as Char)
        var exponent = 1

        if (match(TokenType.EXPONENT)) {
            if (!checkType(TokenType.NUMBER)) {
                throw IllegalStateException("Expected numeric value after exponent sign.")
            }

            val exponentToken = advance()
            exponent = (exponentToken.value as Double).toInt()
        }


        return indeterminate to exponent
    }

    private fun match(vararg tokenTypes: TokenType): Boolean {
        for (tokenType in tokenTypes) {
            if (checkType(tokenType)) {
                advance()
                return true
            }
        }

        return false
    }

    private fun checkType(vararg type: TokenType): Boolean {
        if (isAtEnd()) {
            return false
        }

        val next = peek()
        for (tokenType in type) {
            if (tokenType == next.type) {
                return true
            }
        }

        return false
    }

    private fun advance(): Token {
        if (!isAtEnd()) {
            current++
        }

        return previous()
    }

    private fun isAtEnd(): Boolean {
        return peek().type === TokenType.EOF
    }

    private fun peek(): Token {
        return tokens[current]
    }

    private fun previous(): Token {
        return tokens[current - 1]
    }
}