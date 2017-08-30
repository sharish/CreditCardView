package com.cooltechworks.creditcarddesign.pager;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.cooltechworks.creditcarddesign.CardSelector;
import com.cooltechworks.creditcarddesign.R;

import static com.cooltechworks.creditcarddesign.CreditCardUtils.EXTRA_CARD_CVV;
import static com.cooltechworks.creditcarddesign.CreditCardUtils.EXTRA_CARD_EXPIRY;

/**
 * Created by sharish on 9/1/15.
 */
public class CardCVVFragment extends CreditCardFragment {

    private EditText mCardCVVView;
    private int mMaxCVV = CardSelector.CVV_LENGHT_DEFAULT;

    public CardCVVFragment() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle state) {
        View v = inflater.inflate(R.layout.lyt_card_cvv, group, false);
        mCardCVVView = (EditText) v.findViewById(R.id.card_cvv);

        String cvv = null;
        if (getArguments() != null && getArguments().containsKey(EXTRA_CARD_CVV)) {
            cvv = getArguments().getString(EXTRA_CARD_CVV);
        }

        if (cvv == null) {
            cvv = "";
        }

        mCardCVVView.setText(cvv);
        mCardCVVView.addTextChangedListener(this);

        return v;
    }

    @Override
    public void afterTextChanged(Editable s) {
        onEdit(s.toString());
        if (s.length() == mMaxCVV) {
            onComplete();
        }
    }

    @Override
    public void focus() {
        if (isAdded()) {
            mCardCVVView.selectAll();
        }
    }

    public void setMaxCVV(int maxCVVLength) {
        if (mCardCVVView != null && (mCardCVVView.getText().toString().length() > maxCVVLength)) {
            mCardCVVView.setText(mCardCVVView.getText().toString().substring(0, maxCVVLength));
        }

        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(maxCVVLength);
        mCardCVVView.setFilters(FilterArray);
        mMaxCVV = maxCVVLength;
        String hintCVV = "";
        for (int i = 0; i < maxCVVLength; i++) {
            hintCVV += "X";
        }
        mCardCVVView.setHint(hintCVV);
    }
}
