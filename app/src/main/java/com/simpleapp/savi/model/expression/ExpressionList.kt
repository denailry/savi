package com.simpleapp.savi.model.expression

class ExpressionList {
    companion object {
        val operators = arrayOf('+', '-', '*', '/')
        fun evaluate(s : String) : Long {
            val expList = ExpressionList()

            var current = ""
            var leftOperand: Long? = null
            var operator: Char? = null
            var rightOperand: Long
            for (c in s) {
                if (c in operators) {
                    if (leftOperand == null) {
                        leftOperand = current.toLong()
                    } else {
                        rightOperand = current.toLong()
                        expList.add(Expression(leftOperand, operator!!, rightOperand))
                        leftOperand = rightOperand
                    }
                    operator = c
                    current = ""
                } else {
                    current += c
                }
            }
            expList.add(Expression(leftOperand!!, operator!!, current.toLong()))
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

    fun evaluate() : Long {
        var result: Long? = null
        for (index in firstPriorIndexList) {
            result = expList[index].calculate()
        }
        for (index in secondPriorIndexList) {
            result = expList[index].calculate()
        }
        return result!!
    }
}