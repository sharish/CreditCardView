package com.cooltechworks.creditcarddesign

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.cooltechworks.creditcarddesign.CreditCardUtils.CARD_NAME_PAGE
import com.cooltechworks.creditcarddesign.CreditCardUtils.EXTRA_CARD_CVV
import com.cooltechworks.creditcarddesign.CreditCardUtils.EXTRA_CARD_EXPIRY
import com.cooltechworks.creditcarddesign.CreditCardUtils.EXTRA_CARD_HOLDER_NAME
import com.cooltechworks.creditcarddesign.CreditCardUtils.EXTRA_CARD_NUMBER
import com.cooltechworks.creditcarddesign.CreditCardUtils.EXTRA_ENTRY_START_PAGE
import com.cooltechworks.creditcarddesign.pager.CardFragmentAdapter
import com.cooltechworks.creditcarddesign.pager.CardFragmentAdapter.ICardEntryCompleteListener
import com.cooltechworks.creditcarddesign.utils.hideKeyboard
import com.cooltechworks.creditcarddesign.utils.showKeyboard
import kotlinx.android.synthetic.main.activity_card_edit.*


class CardEditActivity : AppCompatActivity() {


    internal var mLastPageSelected = 0
    private var mCardNumber: String? = null
    private var mCVV: String? = null
    private var mCardHolderName: String? = null
    private var mExpiry: String? = null
    private var mStartPage = 0
    private var mCardAdapter: CardFragmentAdapter? = null

    internal val viewPager: ViewPager
        get() = findViewById<View>(R.id.card_field_container_pager) as ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_edit)

        next.setOnClickListener {
            val max = card_field_container_pager.adapter?.count
            if (card_field_container_pager.currentItem == ((max ?: 1) - 1)) {
                // if last card.
                onDoneTapped()
            } else {
                showNext()
            }
        }
        previous.setOnClickListener { showPrevious() }

        setKeyboardVisibility(true)
        val args = savedInstanceState ?: intent.extras

        loadPager(args)
        checkParams(args)
    }

    private fun checkParams(bundle: Bundle?) {
        if (bundle == null) {
            return
        }
        mCardHolderName = bundle.getString(EXTRA_CARD_HOLDER_NAME)
        mCVV = bundle.getString(EXTRA_CARD_CVV)
        mExpiry = bundle.getString(EXTRA_CARD_EXPIRY)
        mCardNumber = bundle.getString(EXTRA_CARD_NUMBER)
        mStartPage = bundle.getInt(EXTRA_ENTRY_START_PAGE)

        val maxCvvLength = CardSelector.selectCard(mCardNumber).cvvLength
        if (mCVV != null && mCVV?.length ?: 0 > maxCvvLength) {
            mCVV = mCVV?.substring(0, maxCvvLength)
        }

        credit_card_view?.cvv = mCVV
        credit_card_view?.cardHolderName = mCardHolderName
        credit_card_view?.setCardExpiry(mExpiry)
        credit_card_view?.cardNumber = mCardNumber

        if (mCardAdapter != null) {
            credit_card_view?.post {
                mCardAdapter?.setMaxCVV(maxCvvLength)
                mCardAdapter?.notifyDataSetChanged()
            }
        }

        val cardSide = bundle.getInt(CreditCardUtils.EXTRA_CARD_SHOW_CARD_SIDE, CreditCardUtils.CARD_SIDE_FRONT)
        if (cardSide == CreditCardUtils.CARD_SIDE_BACK) {
            credit_card_view?.showBack()
        }
        if (mStartPage > 0 && mStartPage <= CARD_NAME_PAGE) {
            viewPager.currentItem = mStartPage
        }
    }

    fun refreshNextButton() {
        val max = card_field_container_pager.adapter?.count
        var text = R.string.next
        if (card_field_container_pager.currentItem == ((max ?: 0) - 1)) {
            text = R.string.done
        }

        next?.setText(text)
    }

    private fun loadPager(bundle: Bundle?) {
        val pager = viewPager
        pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {

                mCardAdapter?.focus(position)

                if (credit_card_view?.cardType != CreditCardUtils.CardType.AMEX_CARD && position == 2) {
                    credit_card_view?.showBack()
                } else if ((position == 1 || position == 3) && mLastPageSelected == 2 && credit_card_view?.cardType != CreditCardUtils.CardType.AMEX_CARD) {
                    credit_card_view?.showFront()
                }

                mLastPageSelected = position

                refreshNextButton()

            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
        pager.offscreenPageLimit = 4

        mCardAdapter = CardFragmentAdapter(supportFragmentManager, bundle)
        mCardAdapter?.setOnCardEntryCompleteListener(object : ICardEntryCompleteListener {
            override fun onCardEntryComplete(currentIndex: Int) {
                showNext()
            }

            override fun onCardEntryEdit(currentIndex: Int, entryValue: String) {
                when (currentIndex) {
                    0 -> {
                        mCardNumber = entryValue.replace(CreditCardUtils.SPACE_SEPERATOR, "")
                        credit_card_view?.cardNumber = mCardNumber
                        if (mCardAdapter != null) {
                            mCardAdapter?.setMaxCVV(CardSelector.selectCard(mCardNumber).cvvLength)
                        }
                    }
                    1 -> {
                        mExpiry = entryValue
                        credit_card_view?.setCardExpiry(entryValue)
                    }
                    2 -> {
                        mCVV = entryValue
                        credit_card_view?.cvv = entryValue
                    }
                    3 -> {
                        mCardHolderName = entryValue
                        credit_card_view?.cardHolderName = entryValue
                    }
                }
            }
        })

        pager.adapter = mCardAdapter
    }

    public override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(EXTRA_CARD_CVV, mCVV)
        outState.putString(EXTRA_CARD_HOLDER_NAME, mCardHolderName)
        outState.putString(EXTRA_CARD_EXPIRY, mExpiry)
        outState.putString(EXTRA_CARD_NUMBER, mCardNumber)

        super.onSaveInstanceState(outState)
    }

    public override fun onRestoreInstanceState(inState: Bundle) {
        super.onRestoreInstanceState(inState)
        checkParams(inState)
    }


    fun showPrevious() {
        val currentIndex = card_field_container_pager.currentItem

        if (currentIndex == 0) {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }

        if (currentIndex - 1 >= 0) {
            card_field_container_pager.currentItem = currentIndex - 1
        }

        refreshNextButton()
    }

    fun showNext() {
        val adapter = card_field_container_pager.adapter as CardFragmentAdapter?

        val max = adapter?.count
        val currentIndex = card_field_container_pager.currentItem

        if (currentIndex + 1 < max ?: 0) {
            card_field_container_pager.currentItem = currentIndex + 1
        } else {
            // completed the card entry.
            setKeyboardVisibility(false)
        }

        refreshNextButton()
    }

    private fun onDoneTapped() {
        val intent = Intent()

        intent.putExtra(EXTRA_CARD_CVV, mCVV)
        intent.putExtra(EXTRA_CARD_HOLDER_NAME, mCardHolderName)
        intent.putExtra(EXTRA_CARD_EXPIRY, mExpiry)
        intent.putExtra(EXTRA_CARD_NUMBER, mCardNumber)

        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    // from the link above
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        // Checks whether a hardware keyboard is available
        if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO) {

            val parent = findViewById<View>(R.id.parent) as RelativeLayout
            val layoutParams = parent.layoutParams as RelativeLayout.LayoutParams
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, 0)
            parent.layoutParams = layoutParams

        }
    }

    private fun setKeyboardVisibility(visible: Boolean) {
        if (!visible) {
            next.hideKeyboard()
        } else {
            next.showKeyboard()
        }
    }

    override fun onBackPressed() {
        this.finish()
    }
}
