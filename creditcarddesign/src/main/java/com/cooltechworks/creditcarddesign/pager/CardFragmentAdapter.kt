package com.cooltechworks.creditcarddesign.pager

import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

/**
 * Created by sharish on 9/1/15.
 */
class CardFragmentAdapter(fm: FragmentManager, args: Bundle?) : FragmentStatePagerAdapter(fm), IActionListener {

    private val mCardNumberFragment: CardNumberFragment
    private val mCardExpiryFragment: CardExpiryFragment
    private val mCardCVVFragment: CardCVVFragment
    private val mCardNameFragment: CardNameFragment

    private var mCardEntryCompleteListener: ICardEntryCompleteListener? = null

    fun focus(position: Int) {
        (getItem(position) as IFocus).focus()
    }

    interface ICardEntryCompleteListener {
        fun onCardEntryComplete(currentIndex: Int)

        fun onCardEntryEdit(currentIndex: Int, entryValue: String)
    }

    init {

        mCardCVVFragment = CardCVVFragment()
        mCardCVVFragment.arguments = args

        mCardNameFragment = CardNameFragment()
        mCardNameFragment.arguments = args

        mCardNumberFragment = CardNumberFragment()
        mCardNumberFragment.arguments = args

        mCardExpiryFragment = CardExpiryFragment()
        mCardExpiryFragment.arguments = args

        mCardNameFragment.setActionListener(this)
        mCardNumberFragment.setActionListener(this)
        mCardExpiryFragment.setActionListener(this)
        mCardCVVFragment.setActionListener(this)
    }

    fun setOnCardEntryCompleteListener(listener: ICardEntryCompleteListener) {
        this.mCardEntryCompleteListener = listener
    }

    override fun getItem(position: Int): Fragment {
        return arrayOf<Fragment>(mCardNumberFragment, mCardExpiryFragment, mCardCVVFragment, mCardNameFragment)[position]
    }

    override fun getCount(): Int {
        return 4
    }


    override fun onActionComplete(fragment: CreditCardFragment) {
        val index = getIndex(fragment)
        if (index >= 0 && mCardEntryCompleteListener != null) {
            mCardEntryCompleteListener!!.onCardEntryComplete(index)
        }
    }

    fun getIndex(fragment: CreditCardFragment): Int {
        var index = -1
        if (fragment === mCardNumberFragment) {
            index = 0
        } else if (fragment === mCardExpiryFragment) {
            index = 1
        } else if (fragment === mCardCVVFragment) {
            index = 2
        } else if (fragment === mCardNameFragment) {
            index = 3
        }

        return index
    }

    fun setMaxCVV(maxCVV: Int) {
        mCardCVVFragment.setMaxCVV(maxCVV)
    }

    override fun onEdit(fragment: CreditCardFragment, edit: String) {
        val index = getIndex(fragment)

        if (index >= 0 && mCardEntryCompleteListener != null) {
            mCardEntryCompleteListener!!.onCardEntryEdit(index, edit)
        }
    }

    override fun restoreState(parcelable: Parcelable?, classLoader: ClassLoader?) {
        //do nothing here! no call to super.restoreState(parcelable, classLoader);
    }
}
