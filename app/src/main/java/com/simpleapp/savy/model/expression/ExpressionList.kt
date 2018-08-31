package com.simpleapp.savy.model.expression

class ExpressionList {
    companion object {
        val operators = arrayOf('+', '-', '*', '/')
        fun evaluate(s : String) : Int {
            val expList = ExpressionList()

            var current = ""
            var leftOperand: Int? = null
            var operator: Char? = null
            var rightOperand: Int
            for (c in s) {
                if (c in operators) {
                    if (leftOperand == null) {
                        leftOperand = current.toInt()
                    } else {
                        rightOperand = current.toInt()
                        expList.add(Expression(leftOperand, operator!!, rightOperand))
                        leftOperand = rightOperand
                    }
                    operator = c
                    current = ""
                } else {
                    current += c
                }
            }
            expList.add(Expression(leftOperand!!, operator!!, current.toInt()))
            return expList.evaluate()
        }
    }

    private val expList: ArrayList<Expression> = ArrayList()
    private val firstPriorIndexList: ArrayList<Int> = ArrayList()
    private val secondPriorIndexList: ArrayList<Int> = ArrayList()

    fun add(expression: Expression) {
        if (!expList.isEmpty()) {
            val lastExpression = expList.last()
            lastExpression.rightExpression = expression
            expression.leftExpression = lastExpression
        }
        expList.add(expression)
        if (expression.operator == '+' || expression.operator == '-') {
            secondPriorIndexList.add(expList.lastIndex)
        } else {
            firstPriorIndexList.add(expList.lastIndex)
        }
    }

    fun evaluate() : Int {
        var result: Int? = null
        for (index in firstPriorIndexList) {
            result = expList[index].calculate()
        }
        for (index in secondPriorIndexList) {
            result = expList[index].calculate()
        }
        return result!!
    }
}