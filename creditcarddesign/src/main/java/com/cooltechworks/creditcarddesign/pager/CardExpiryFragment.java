package com.cooltechworks.creditcarddesign.pager;

import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import com.cooltechworks.creditcarddesign.CreditCardUtils;
import com.cooltechworks.creditcarddesign.R;

import java.util.Calendar;

import static com.cooltechworks.creditcarddesign.CreditCardUtils.EXTRA_CARD_EXPIRY;

/**
 * Created by sharish on 9/1/15.
 */
public class CardExpiryFragment extends CreditCardFragment {

    EditText cardExpiryView;
    public CardExpiryFragment() {

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle state) {

        View v = inflater.inflate(R.layout.lyt_card_expiry, group, false);
        cardExpiryView = (EditText) v.findViewById(R.id.card_expiry);

        String expiry = "";
        if(getArguments() != null && getArguments().containsKey(EXTRA_CARD_EXPIRY)) {
            expiry = getArguments().getString(EXTRA_CARD_EXPIRY);
        }

        if(expiry == null) {
            expiry = "";
        }

        cardExpiryView.setText(expiry);

        cardExpiryView.addTextChangedListener(this);

        return v;
    }

    @Override
    public void afterTextChanged(Editable s) {

        String text = s.toString().replace(CreditCardUtils.SLASH_SEPERATOR, "");

        String month="", year="";
        if(text.length() >= 2) {
            month = text.substring(0, 2);
            int mm = Integer.parseInt(month);

            if(mm <= 0 || mm >= 13) {
                cardExpiryView.setError("Invalid month");
                return;
            }

            year = text.substring(2);

            if(text.length() >= 4) {

                int yy = Integer.parseInt(year);

                final Calendar calendar = Calendar.getInstance();
                int currentYear =calendar.get(Calendar.YEAR);
                int currentMonth = calendar.get(Calendar.MONTH) + 1;

                int millenium = (currentYear / 1000) * 1000;


                if(yy + millenium < currentYear) {
                    cardExpiryView.setError("Card expired");
                    return;
                } else if (yy + millenium == currentYear && mm < currentMonth) {
                    cardExpiryView.setError("Card expired");
                    return;
                }
            }

        }
        else {
            month = text;
        }

        int previousLength = cardExpiryView.getText().length();
        int cursorPosition = cardExpiryView.getSelectionEnd();

        text = CreditCardUtils.handleExpiration(month,year);

        cardExpiryView.removeTextChangedListener(this);
        cardExpiryView.setText(text);
        cardExpiryView.setSelection(text.length());
        cardExpiryView.addTextChangedListener(this);

        int modifiedLength = text.length();

        if(modifiedLength <= previousLength && cursorPosition < modifiedLength) {
            cardExpiryView.setSelection(cursorPosition);
        }

        onEdit(text);

        if(text.length() == 5) {
            onComplete();
        }

    }


    @Override
    public void focus() {

        if(isAdded()) {
            cardExpiryView.selectAll();
        }
    }
}
