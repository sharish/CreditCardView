package com.cooltechworks.creditcarddesign.pager;

import android.support.v4.app.Fragment;
import android.text.TextWatcher;

/**
 * Created by sharish on 9/1/15.
 */
public abstract  class CreditCardFragment extends Fragment implements TextWatcher, IFocus {

    protected IActionListener mActionListener;

    public void setActionListener(IActionListener listener) {
        mActionListener = listener;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }


    public void onEdit(String edit) {

        if(mActionListener != null) {
            mActionListener.onEdit(this,edit);
        }

    }

    public void onComplete() {

        if(mActionListener != null) {
            mActionListener.onActionComplete(this);
        }

    }

}
