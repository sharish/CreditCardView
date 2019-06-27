package com.cooltechworks.creditcarddesign.pager

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.cooltechworks.creditcarddesign.CreditCardUtils
import com.cooltechworks.creditcarddesign.R

import java.util.Calendar

import com.cooltechworks.creditcarddesign.CreditCardUtils.EXTRA_CARD_EXPIRY
import com.cooltechworks.creditcarddesign.CreditCardUtils.EXTRA_VALIDATE_EXPIRY_DATE

/**
 * Created by sharish on 9/1/15.
 */
class CardExpiryFragment : CreditCardFragment() {

    lateinit var cardExpiryView: EditText

    private var mValidateCard = true

    override fun onCreateView(inflater: LayoutInflater, group: ViewGroup?, state: Bundle?): View? {

        val v = inflater.inflate(R.layout.lyt_card_expiry, group, false)
        cardExpiryView = v.findViewById<View>(R.id.card_expiry) as EditText

        var expiry: String? = ""

        val args = arguments

        if (args != null) {

            if (args.containsKey(EXTRA_CARD_EXPIRY)) {
                expiry = arguments!!.getString(EXTRA_CARD_EXPIRY)
            }

            mValidateCard = args.getBoolean(EXTRA_VALIDATE_EXPIRY_DATE, true)
        }

        if (expiry == null) {
            expiry = ""
        }

        cardExpiryView.setText(expiry)

        cardExpiryView.addTextChangedListener(this)

        return v
    }

    override fun afterTextChanged(s: Editable) {

        var text = s.toString().replace(CreditCardUtils.SLASH_SEPERATOR, "")

        val month: String
        var year = ""
        if (text.length >= 2) {
            month = text.substring(0, 2)

            if (text.length > 2) {
                year = text.substring(2)
            }

            if (mValidateCard) {
                val mm = Integer.parseInt(month)

                if (mm <= 0 || mm >= 13) {
                    cardExpiryView.error = getString(R.string.error_invalid_month)
                    return
                }

                if (text.length >= 4) {

                    val yy = Integer.parseInt(year)

                    val calendar = Calendar.getInstance()
                    val currentYear = calendar.get(Calendar.YEAR)
                    val currentMonth = calendar.get(Calendar.MONTH) + 1

                    val millenium = currentYear / 1000 * 1000


                    if (yy + millenium < currentYear) {
                        cardExpiryView.error = getString(R.string.error_card_expired)
                        return
                    } else if (yy + millenium == currentYear && mm < currentMonth) {
                        cardExpiryView.error = getString(R.string.error_card_expired)
                        return
                    }
                }
            }

        } else {
            month = text
        }

        val previousLength = cardExpiryView.text.length
        val cursorPosition = cardExpiryView.selectionEnd

        text = CreditCardUtils.handleExpiration(month, year)

        cardExpiryView.removeTextChangedListener(this)
        cardExpiryView.setText(text)
        cardExpiryView.setSelection(text.length)
        cardExpiryView.addTextChangedListener(this)

        val modifiedLength = text.length

        if (modifiedLength <= previousLength && cursorPosition < modifiedLength) {
            cardExpiryView.setSelection(cursorPosition)
        }

        onEdit(text)

        if (text.length == 5) {
            onComplete()
        }

    }


    override fun focus() {

        if (isAdded) {
            cardExpiryView.selectAll()
        }
    }


    override fun onSaveInstanceState(outState: Bundle) {

        outState.putBoolean(EXTRA_VALIDATE_EXPIRY_DATE, mValidateCard)
        super.onSaveInstanceState(outState)
    }

    override fun onActivityCreated(instate: Bundle?) {

        if (instate != null) {
            mValidateCard = instate.getBoolean(EXTRA_VALIDATE_EXPIRY_DATE, mValidateCard)
        }

        super.onActivityCreated(instate)
    }
}
