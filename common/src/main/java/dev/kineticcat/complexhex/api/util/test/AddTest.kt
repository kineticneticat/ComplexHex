package dev.kineticcat.complexhex.api.util.test


import dev.kineticcat.complexhex.api.util.Symbol
import dev.kineticcat.complexhex.api.util.Value

class AddTest {

    @org.junit.jupiter.api.Test
    fun testEquals() {
        val A = Symbol.U + 3.0
        val B = Symbol.U + 3.0
        val C = Symbol.V + 6.0
        assert(A == B)
        assert(A != C)
    }

    @org.junit.jupiter.api.Test
    fun hasSymbol() {
        val A = Symbol.V + 4.5
        assert(A.contains(Symbol.V))
    }

    @org.junit.jupiter.api.Test
    fun substitute() {
        val A = Symbol.X + 32.0
        val B = Symbol.Y + 32.0
        assert(A.substitute(Symbol.X, Symbol.Y) == B)
    }

    @org.junit.jupiter.api.Test
    fun simplify() {
        val A = Value(32.0) + 3.2
        assert(A.simplify() == Value(35.2))
    }

    @org.junit.jupiter.api.Test
    fun diff() {
        val A = Symbol.X + Symbol.Y
        assert(A.diff(Symbol.X).simplify() == Value.ONE)
    }
}