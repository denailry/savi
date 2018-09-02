package com.simpleapp.savy.calculator

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.simpleapp.savy.R
import com.simpleapp.savy.lib.PublicMethods
import com.simpleapp.savy.model.expression.ExpressionList
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.activity_calculator.*

class CalculatorActivity : AppCompatActivity() {

    var onClickEqualOKListener: OnClickEqualOKListener? = null
    var formatter: Formatter? = Formatter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculator)
        setupInitialValue()
        setupOnClickListener()
    }

    private fun setupInitialValue() {
        val initialValue = intent.getLongExtra("value", 0)
        if (initialValue == 0L) {
            tvResult.text = initialValue.toString()
        } else {
            formatter?.insert(initialValue.toString())
        }
    }

    inner class Formatter {
        var currentNumber = ""
        var numberList = ArrayList<String>()
        var operatorList = ArrayList<Char>()

        fun insert(s: String) {
            if (s[0] !in ExpressionList.operators) {
                currentNumber += s
                format()
            } else {
                numberList.add(currentNumber)
                operatorList.add(s[0])
                currentNumber = ""
            }
        }

        fun drop() {
            if (currentNumber.isEmpty()) {
                currentNumber = numberList.last()
                numberList.removeAt(numberList.size-1)
                operatorList.removeAt(operatorList.size-1)
            } else {
                currentNumber = currentNumber.dropLast(1)
            }
            format()
        }

        fun format() {
            var formattedString = ""
            for (i in 0.until(numberList.size)) {
                formattedString += PublicMethods.moneyFormat(numberList[i])
                formattedString += operatorList[i]
            }
            formattedString += PublicMethods.moneyFormat(currentNumber)
            tvResult.text = formattedString
        }

        fun reset() {
            currentNumber = ""
            numberList = ArrayList<String>()
            operatorList = ArrayList<Char>()
        }

        fun getOriginal() : String {
            var originalString = ""
            for (i in 0.until(numberList.size)) {
                originalString += numberList[i]
                originalString += operatorList[i]
            }
            originalString += currentNumber
            return originalString
        }
    }

    inner class StringAdder(val s : String) : View.OnClickListener {
        override fun onClick(v: View?) {
            if (tvResult.text.toString() == "0") {
                if (s != "0" && s != "000" && s[0] !in ExpressionList.operators) {
                    tvResult.text = s
                    formatter?.insert(s)
                }
            } else {
                if (s[0] in ExpressionList.operators) {
                    if (tvResult.text.last() !in ExpressionList.operators) {
                        val newString = tvResult.text.toString() + s
                        tvResult.text = newString
                        formatter?.insert(s)
                        onClickEqualOKListener?.invalidate()
                    } else {
                        var newString = tvResult.text.dropLast(1).toString()
                        formatter?.drop()
                        newString += s
                        tvResult.text = newString
                        formatter?.insert(s)
                    }
                } else {
                    val newString = tvResult.text.toString() + s
                    tvResult.text = newString
                    formatter?.insert(s)
                }
            }
        }
    }

    inner class OnClickEqualOKListener : View.OnClickListener {
        var invalid = 0

        override fun onClick(v: View?) {
            if (invalid > 0) {
                val newText = ExpressionList.evaluate(formatter!!.getOriginal()).toString()
                tvResult.text = newText
                formatter?.reset()
                formatter?.insert(newText)
                reset()
            } else {
                try {
                    intent = Intent()
                    intent.putExtra("value", formatter!!.getOriginal().toLong())
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                } catch (e: Exception) {
                    Toast.makeText(applicationContext, getString(R.string.large_number_exception), Toast.LENGTH_SHORT).show()
                }
            }
        }

        fun invalidate() {
            if (invalid == 0) {
                tvEqualOK.text = getString(R.string.calculator_equal)
            }
            invalid++
        }

        fun validate() {
            invalid--
            if (invalid == 0) {
                tvEqualOK.text = getString(R.string.calculator_save)
            }

    }
        fun reset() {
            tvEqualOK.text = getString(R.string.calculator_save)
            invalid = 0
        }
    }

    private fun setupOnClickListener() {
        val deleteAllListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                tvResult.text = "0"
                onClickEqualOKListener?.reset()
                formatter?.reset()
            }
        }
        val deleteListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                if (tvResult.length() == 1) {
                    tvResult.text = "0"
                    formatter?.reset()
                } else {
                    if (tvResult.text.last() in ExpressionList.operators) {
                        onClickEqualOKListener?.validate()
                    }
                    formatter?.drop()
                }
            }
        }

        tvDeleteAll.setOnClickListener(deleteAllListener)
        viewDeleteAll.setOnClickListener(deleteAllListener)
        tvDelete.setOnClickListener(deleteListener)
        viewDelete.setOnClickListener(deleteListener)

        onClickEqualOKListener = OnClickEqualOKListener()
        tv1.setOnClickListener(StringAdder("1"))
        tv2.setOnClickListener(StringAdder("2"))
        tv3.setOnClickListener(StringAdder("3"))
        tv4.setOnClickListener(StringAdder("4"))
        tv5.setOnClickListener(StringAdder("5"))
        tv6.setOnClickListener(StringAdder("6"))
        tv7.setOnClickListener(StringAdder("7"))
        tv8.setOnClickListener(StringAdder("8"))
        tv9.setOnClickListener(StringAdder("9"))
        tvZero.setOnClickListener(StringAdder("0"))
        tvTripleZero.setOnClickListener(StringAdder("000"))
        tvPlus.setOnClickListener(StringAdder("+"))
        tvMinus.setOnClickListener(StringAdder("-"))
        tvDivide.setOnClickListener(StringAdder("/"))
        tvMultiply.setOnClickListener(StringAdder("*"))
        tvEqualOK.setOnClickListener(onClickEqualOKListener)
    }
}
