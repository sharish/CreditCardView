package com.cooltechworks.creditcarddesign.pager;

import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.cooltechworks.creditcarddesign.CreditCardUtils;
import com.cooltechworks.creditcarddesign.R;

import static com.cooltechworks.creditcarddesign.CreditCardUtils.EXTRA_CARD_NUMBER;
import static com.cooltechworks.creditcarddesign.CreditCardUtils.MAX_LENGTH_CARD_NUMBER;
import static com.cooltechworks.creditcarddesign.CreditCardUtils.MAX_LENGTH_CARD_NUMBER_WITH_SPACES;

/**
 * Created by sharish on 9/1/15.
 */
public class CardNumberFragment extends  CreditCardFragment {

    public CardNumberFragment() {

    }
    EditText mCardNumberView;

    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle state) {

        View v = inflater.inflate(R.layout.lyt_card_number, group, false);
        mCardNumberView = (EditText) v.findViewById(R.id.card_number_field);
        mCardNumberView.addTextChangedListener(this);

        String number = "";

        if(getArguments() != null && getArguments().containsKey(EXTRA_CARD_NUMBER)) {
            number = getArguments().getString(EXTRA_CARD_NUMBER);
        }

        if(number == null) {
            number = "";
        }

        mCardNumberView.setText(number);

        return v;
    }


    @Override
    public void afterTextChanged(Editable s) {

        int cursorPosition = mCardNumberView.getSelectionEnd();
        int previousLength = mCardNumberView.getText().length();

        String cardNumber = CreditCardUtils.handleCardNumber(s.toString());
        int modifiedLength = cardNumber.length();

        mCardNumberView.removeTextChangedListener(this);
        mCardNumberView.setText(cardNumber);
        mCardNumberView.setSelection(cardNumber.length() > MAX_LENGTH_CARD_NUMBER_WITH_SPACES ? MAX_LENGTH_CARD_NUMBER_WITH_SPACES : cardNumber.length());
        mCardNumberView.addTextChangedListener(this);

        if(modifiedLength <= previousLength && cursorPosition < modifiedLength) {
            mCardNumberView.setSelection(cursorPosition);
        }

        onEdit(cardNumber);


        if(cardNumber.replace(CreditCardUtils.SPACE_SEPERATOR,"").length() == MAX_LENGTH_CARD_NUMBER) {
            onComplete();
        }
    }

    @Override
    public void focus() {

        if(isAdded()) {
            mCardNumberView.selectAll();
        }
    }
}
