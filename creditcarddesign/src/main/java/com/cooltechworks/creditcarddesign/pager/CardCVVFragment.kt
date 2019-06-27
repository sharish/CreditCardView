package com.cooltechworks.creditcarddesign.pager

import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText

import com.cooltechworks.creditcarddesign.CardSelector
import com.cooltechworks.creditcarddesign.R

import com.cooltechworks.creditcarddesign.CreditCardUtils.EXTRA_CARD_CVV
import kotlinx.android.synthetic.main.lyt_card_cvv.*

/**
 * Created by sharish on 9/1/15.
 */
class CardCVVFragment : CreditCardFragment() {

    private var mMaxCVV = CardSelector.CVV_LENGHT_DEFAULT

    override fun onCreateView(inflater: LayoutInflater, group: ViewGroup?, state: Bundle?): View? {
        val v = inflater.inflate(R.layout.lyt_card_cvv, group, false)

        var cvv: String? = null
        if (arguments != null && arguments?.containsKey(EXTRA_CARD_CVV) ?: false) {
            cvv = arguments?.getString(EXTRA_CARD_CVV)
        }

        if (cvv == null) {
            cvv = ""
        }

        card_cvv?.setText(cvv)
        card_cvv?.addTextChangedListener(this)

        return v
    }

    override fun afterTextChanged(s: Editable) {
        onEdit(s.toString())
        if (s.length == mMaxCVV) {
            onComplete()
        }
    }

    override fun focus() {
        if (isAdded) {
            card_cvv?.selectAll()
        }
    }

    fun setMaxCVV(maxCVVLength: Int) {
        if (card_cvv != null && card_cvv?.text.toString().length > maxCVVLength) {
            card_cvv?.setText(card_cvv?.text.toString().substring(0, maxCVVLength))
        }

        val FilterArray = arrayOfNulls<InputFilter>(1)
        FilterArray[0] = InputFilter.LengthFilter(maxCVVLength)
        card_cvv?.filters = FilterArray
        mMaxCVV = maxCVVLength
        var hintCVV = ""
        for (i in 0 until maxCVVLength) {
            hintCVV += "X"
        }
        card_cvv?.hint = hintCVV
    }
}
