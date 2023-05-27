package com.example.tipcalculator

import android.animation.ArgbEvaluator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView

private const val TAG = "MainActivity"
private const val INITIAL_TIP_PERCENT = 15
private const val INITIAL_PEOPLE_SPLIT_BY = 1

class MainActivity : AppCompatActivity() {
    private lateinit var etBaseAmount: EditText
    private lateinit var sbTip: SeekBar
    private lateinit var tvTipPercent: TextView
    private lateinit var tvTipAmount: TextView
    private lateinit var tvTotal: TextView
    private lateinit var tvTipDescription: TextView
    private lateinit var sbPeople: SeekBar
    private lateinit var tvSplitByField: TextView
    private lateinit var tvPerPersonValue: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etBaseAmount = findViewById(R.id.edBaseAmount);
        sbTip = findViewById(R.id.sbTip)
        tvTipPercent = findViewById(R.id.tvTipPercentLabel)
        tvTipAmount = findViewById(R.id.tvTipAmount)
        tvTotal = findViewById(R.id.tvTotal)
        tvTipDescription = findViewById(R.id.tvTipDescription)
        sbPeople = findViewById(R.id.sbPeople)
        tvSplitByField = findViewById(R.id.tvSplitByField)
        tvPerPersonValue = findViewById(R.id.tvPerPersonValue)

        sbTip.progress = INITIAL_TIP_PERCENT
        tvTipPercent.text = getString(R.string.percent_string, INITIAL_TIP_PERCENT)
        updateTipDescription(INITIAL_TIP_PERCENT)

        sbPeople.progress = INITIAL_PEOPLE_SPLIT_BY
        tvSplitByField.text = getString(R.string.people_string, INITIAL_PEOPLE_SPLIT_BY)

        sbTip.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                tvTipPercent.text = getString(R.string.percent_string, progress)
                computeTipAndTotal()
                updateTipDescription(progress)
                calculatePerPerson()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        sbPeople.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                tvSplitByField.text = getString(R.string.people_string, progress)
                calculatePerPerson()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        etBaseAmount.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                computeTipAndTotal()
                calculatePerPerson()
            }
        })
    }

    private fun computeTipAndTotal() {
        if (etBaseAmount.text.isEmpty()) {
            tvTipAmount.text = ""
            tvTotal.text = ""
            return
        }

        val baseAmount = etBaseAmount.text.toString().toDouble()
        val tipPercent = sbTip.progress

        val tipAmount = baseAmount * tipPercent / 100.0
        val totalAmount = tipAmount + baseAmount

        tvTipAmount.text = String.format("%.2f", tipAmount)
        tvTotal.text = String.format("%.2f", totalAmount)
    }

    private fun updateTipDescription(tipPercent: Int) {
        val tipDescription = when (tipPercent) {
            in 0..9 -> "\uD83D\uDE21"
            in 10..14 -> "\uD83D\uDE10"
            in 15..19 -> "\uD83D\uDE00"
            in 20..24 -> "\uD83D\uDE0A"
            else -> "\uD83D\uDE0E"
        }
        tvTipDescription.text = tipDescription
        val color = ArgbEvaluator().evaluate(
            tipPercent.toFloat() / sbTip.max,
            R.color.color_worst_tip,
            R.color.color_best_tip
        ) as Int
        tvTipDescription.setTextColor(color)
    }

    private fun calculatePerPerson() {
        if (etBaseAmount.text.isEmpty()) {
            tvTipAmount.text = ""
            tvTotal.text = ""
            return
        }
        val total = tvTotal.text.toString().toDouble()
        val perPerson = total / sbPeople.progress
        tvPerPersonValue.text = String.format("%.2f", perPerson)
    }

}

