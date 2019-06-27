package com.cooltechworks.creditcarddesign.pager

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText

import com.cooltechworks.creditcarddesign.CreditCardUtils
import com.cooltechworks.creditcarddesign.R

import com.cooltechworks.creditcarddesign.CreditCardUtils.CARD_NUMBER_FORMAT
import com.cooltechworks.creditcarddesign.CreditCardUtils.CARD_NUMBER_FORMAT_AMEX
import com.cooltechworks.creditcarddesign.CreditCardUtils.EXTRA_CARD_NUMBER

/**
 * Created by sharish on 9/1/15.
 */
class CardNumberFragment : CreditCardFragment() {

    lateinit var mCardNumberView: EditText

    override fun onCreateView(inflater: LayoutInflater, group: ViewGroup?, state: Bundle?): View? {
        val v = inflater.inflate(R.layout.lyt_card_number, group, false)
        mCardNumberView = v.findViewById<View>(R.id.card_number_field) as EditText

        var number: String? = ""

        if (arguments != null && arguments?.containsKey(EXTRA_CARD_NUMBER) ?: false) {
            number = arguments?.getString(EXTRA_CARD_NUMBER)
        }

        if (number == null) {
            number = ""
        }

        mCardNumberView.setText(number)
        mCardNumberView.addTextChangedListener(this)

        return v
    }


    override fun afterTextChanged(s: Editable) {
        val cursorPosition = mCardNumberView.selectionEnd
        val previousLength = mCardNumberView.text.length

        val cardNumber = CreditCardUtils.handleCardNumber(s.toString())
        val modifiedLength = cardNumber.length

        mCardNumberView.removeTextChangedListener(this)
        mCardNumberView.setText(cardNumber)
        val rawCardNumber = cardNumber.replace(CreditCardUtils.SPACE_SEPERATOR, "")
        val cardType = CreditCardUtils.selectCardType(rawCardNumber)
        val maxLengthWithSpaces = (if (cardType == CreditCardUtils.CardType.AMEX_CARD) CARD_NUMBER_FORMAT_AMEX else CARD_NUMBER_FORMAT).length
        mCardNumberView.setSelection(if (cardNumber.length > maxLengthWithSpaces) maxLengthWithSpaces else cardNumber.length)
        mCardNumberView.addTextChangedListener(this)

        if (modifiedLength <= previousLength && cursorPosition < modifiedLength) {
            mCardNumberView.setSelection(cursorPosition)
        }

        onEdit(cardNumber)

        if (rawCardNumber.length == CreditCardUtils.selectCardLength(cardType)) {
            onComplete()
        }
    }

    override fun focus() {
        if (isAdded) {
            mCardNumberView.selectAll()
        }
    }
}
