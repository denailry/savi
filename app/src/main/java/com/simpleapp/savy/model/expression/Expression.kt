package com.simpleapp.savy.model.expression

class Expression(
        var leftOperand: Int,
        var operator: Char,
        var rightOperand: Int
) {
    var leftExpression: Expression? = null
    var rightExpression: Expression? = null

    fun calculate() : Int? {
        val result : Int?
        when(operator){
            '+' -> result = leftOperand + rightOperand
            '-' -> result = leftOperand - rightOperand
            '*' -> result = leftOperand * rightOperand
            '/' -> result = leftOperand / rightOperand
            else -> result = null
        }
        result!!
        leftExpression?.rightOperand = result
        leftExpression?.rightExpression = rightExpression
        rightExpression?.leftOperand = result
        rightExpression?.leftExpression = leftExpression
        return result
    }
}