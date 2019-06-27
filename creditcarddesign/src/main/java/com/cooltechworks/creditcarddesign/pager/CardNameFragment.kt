package com.cooltechworks.creditcarddesign.pager

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText

import com.cooltechworks.creditcarddesign.R

import com.cooltechworks.creditcarddesign.CreditCardUtils.EXTRA_CARD_CVV
import com.cooltechworks.creditcarddesign.CreditCardUtils.EXTRA_CARD_HOLDER_NAME

/**
 * Created by sharish on 9/1/15.
 */
class CardNameFragment : CreditCardFragment() {


    private var mCardNameView: EditText? = null

    override fun onCreateView(inflater: LayoutInflater, group: ViewGroup?, state: Bundle?): View? {

        val v = inflater.inflate(R.layout.lyt_card_holder_name, group, false)
        mCardNameView = v.findViewById<View>(R.id.card_name) as EditText

        var name: String? = ""
        if (arguments != null && arguments?.containsKey(EXTRA_CARD_HOLDER_NAME) ?: false) {
            name = arguments!!.getString(EXTRA_CARD_HOLDER_NAME)
        }


        if (name == null) {
            name = ""
        }

        mCardNameView!!.setText(name)
        mCardNameView!!.addTextChangedListener(this)

        return v
    }

    override fun afterTextChanged(s: Editable) {

        onEdit(s.toString())
        if (s.length == resources.getInteger(R.integer.card_name_len)) {
            onComplete()
        }
    }

    override fun focus() {

        if (isAdded) {
            mCardNameView!!.selectAll()
        }
    }
}
