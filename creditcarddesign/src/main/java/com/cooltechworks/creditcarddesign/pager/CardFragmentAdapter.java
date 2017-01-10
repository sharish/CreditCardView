package com.cooltechworks.creditcarddesign.pager;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by sharish on 9/1/15.
 */
public class CardFragmentAdapter extends FragmentStatePagerAdapter implements IActionListener {

    public void focus(int position) {
        ((IFocus) getItem(position)).focus();
    }

    public interface ICardEntryCompleteListener {
        void onCardEntryComplete(int currentIndex);

        void onCardEntryEdit(int currentIndex, String entryValue);
    }

    private final CardNumberFragment mCardNumberFragment;
    private final CardExpiryFragment mCardExpiryFragment;
    private final CardCVVFragment mCardCVVFragment;
    private final CardNameFragment mCardNameFragment;

    private ICardEntryCompleteListener mCardEntryCompleteListener;

    public CardFragmentAdapter(FragmentManager fm, Bundle args) {
        super(fm);

        mCardCVVFragment = new CardCVVFragment();
        mCardCVVFragment.setArguments(args);

        mCardNameFragment = new CardNameFragment();
        mCardNameFragment.setArguments(args);

        mCardNumberFragment = new CardNumberFragment();
        mCardNumberFragment.setArguments(args);

        mCardExpiryFragment = new CardExpiryFragment();
        mCardExpiryFragment.setArguments(args);

        mCardNameFragment.setActionListener(this);
        mCardNumberFragment.setActionListener(this);
        mCardExpiryFragment.setActionListener(this);
        mCardCVVFragment.setActionListener(this);
    }

    public void setOnCardEntryCompleteListener(ICardEntryCompleteListener listener) {
        this.mCardEntryCompleteListener = listener;
    }

    @Override
    public Fragment getItem(int position) {
        return new Fragment[]{mCardNumberFragment, mCardExpiryFragment, mCardCVVFragment, mCardNameFragment}[position];
    }

    @Override
    public int getCount() {
        return 4;
    }


    @Override
    public void onActionComplete(CreditCardFragment fragment) {
        int index = getIndex(fragment);
        if (index >= 0 && mCardEntryCompleteListener != null) {
            mCardEntryCompleteListener.onCardEntryComplete(index);
        }
    }

    public int getIndex(CreditCardFragment fragment) {
        int index = -1;
        if (fragment == mCardNumberFragment) {
            index = 0;
        } else if (fragment == mCardExpiryFragment) {
            index = 1;
        } else if (fragment == mCardCVVFragment) {
            index = 2;
        } else if (fragment == mCardNameFragment) {
            index = 3;
        }

        return index;
    }

    public void setMaxCVV(int maxCVV) {
        if (mCardCVVFragment != null) {
            mCardCVVFragment.setMaxCVV(maxCVV);
        }
    }

    @Override
    public void onEdit(CreditCardFragment fragment, String edit) {
        int index = getIndex(fragment);

        if (index >= 0 && mCardEntryCompleteListener != null) {
            mCardEntryCompleteListener.onCardEntryEdit(index, edit);
        }
    }

    @Override
    public void restoreState(Parcelable parcelable, ClassLoader classLoader) {
        //do nothing here! no call to super.restoreState(parcelable, classLoader);
    }
}
