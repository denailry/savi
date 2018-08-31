package com.simpleapp.savy.calculator

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.simpleapp.savy.R
import com.simpleapp.savy.model.expression.ExpressionList
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.activity_calculator.*

class CalculatorActivity : AppCompatActivity() {

    var onClickEqualOKListener: OnClickEqualOKListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculator)
        setupInitialValue()
        setupOnClickListener()
    }

    private fun setupInitialValue() {
        val initialValue = intent.getLongExtra("value", 0)
        tvResult.text = initialValue.toString()
    }

    inner class StringAdder(override val containerView: View?, val s : String) : View.OnClickListener, LayoutContainer {
        override fun onClick(v: View?) {
            if (tvResult.text.toString() == "0") {
                if (s != "000" && s[0] !in ExpressionList.operators) {
                    tvResult.text = s
                }
            } else {
                if (s[0] in ExpressionList.operators) {
                    if (tvResult.text.last() !in ExpressionList.operators) {
                        val newString = tvResult.text.toString() + s
                        tvResult.text = newString
                        onClickEqualOKListener?.invalidate()
                    } else {
                        var newString = tvResult.text.dropLast(1).toString()
                        newString += s
                        tvResult.text = newString
                    }
                } else {
                    val newString = tvResult.text.toString() + s
                    tvResult.text = newString
                }
            }
        }
    }

    fun returnResult() {
        intent = Intent()
        intent.putExtra("value", tvResult.text.toString().toInt())
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    inner class OnClickEqualOKListener(override val containerView: View?) : View.OnClickListener, LayoutContainer {
        var invalid = 0

        override fun onClick(v: View?) {
            if (invalid > 0) {
                tvResult.text = ExpressionList.evaluate(tvResult.text.toString()).toString()
                reset()
            } else {
                returnResult()
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
        tvDeleteAll.setOnClickListener{_ ->
            tvResult.text = "0"
            onClickEqualOKListener?.reset()
        }
        tvDelete.setOnClickListener{_ ->
            if (tvResult.length() == 1) {
                tvResult.text = "0"
            } else {
                if (tvResult.text.last() in ExpressionList.operators) {
                    onClickEqualOKListener?.validate()
                }
                tvResult.text = tvResult.text.dropLast(1)
            }
        }

        onClickEqualOKListener = OnClickEqualOKListener(viewRoot)
        tv1.setOnClickListener(StringAdder(viewRoot, "1"))
        tv2.setOnClickListener(StringAdder(viewRoot, "2"))
        tv3.setOnClickListener(StringAdder(viewRoot, "3"))
        tv4.setOnClickListener(StringAdder(viewRoot, "4"))
        tv5.setOnClickListener(StringAdder(viewRoot, "5"))
        tv6.setOnClickListener(StringAdder(viewRoot, "6"))
        tv7.setOnClickListener(StringAdder(viewRoot, "7"))
        tv8.setOnClickListener(StringAdder(viewRoot, "8"))
        tv9.setOnClickListener(StringAdder(viewRoot, "9"))
        tvZero.setOnClickListener(StringAdder(viewRoot, "0"))
        tvTripleZero.setOnClickListener(StringAdder(viewRoot, "000"))
        tvPlus.setOnClickListener(StringAdder(viewRoot, "+"))
        tvMinus.setOnClickListener(StringAdder(viewRoot, "-"))
        tvDivide.setOnClickListener(StringAdder(viewRoot, "/"))
        tvMultiply.setOnClickListener(StringAdder(viewRoot, "*"))
        tvEqualOK.setOnClickListener(onClickEqualOKListener)
    }
}
