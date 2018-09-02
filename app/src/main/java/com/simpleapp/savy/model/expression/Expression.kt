package com.simpleapp.savy.model.expression

class Expression(
        var leftOperand: Long,
        var operator: Char,
        var rightOperand: Long
) {
    var leftExpression: Expression? = null
    var rightExpression: Expression? = null

    fun calculate() : Long? {
        val result : Long?
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