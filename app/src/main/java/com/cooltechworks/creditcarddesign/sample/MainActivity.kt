package com.cooltechworks.creditcarddesign.sample

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.cooltechworks.checkoutflow.R
import com.cooltechworks.creditcarddesign.CardEditActivity
import com.cooltechworks.creditcarddesign.CreditCardUtils
import com.cooltechworks.creditcarddesign.CreditCardView
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Created by glarencezhao on 10/23/16.
 */

class MainActivity : AppCompatActivity() {

    private val CREATE_NEW_CARD = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initialize()
        listeners()
    }

    private fun initialize() {
        //        getSupportActionBar().setTitle("Payment");
        populate()
    }

    private fun populate() {
        val sampleCreditCardView = CreditCardView(this)

        val name = "Glarence Zhao"
        val cvv = "420"
        val expiry = "01/18"
        val cardNumber = "4242424242424242"

        sampleCreditCardView.cvv = cvv
        sampleCreditCardView.cardHolderName = name
        sampleCreditCardView.setCardExpiry(expiry)
        sampleCreditCardView.cardNumber = cardNumber

        card_container.addView(sampleCreditCardView)
        val index = card_container?.childCount ?: 1 - 1
        addCardListener(index, sampleCreditCardView)
    }

    private fun listeners() {
        add_card?.setOnClickListener {
            val intent = Intent(this@MainActivity, CardEditActivity::class.java)
            startActivityForResult(intent, CREATE_NEW_CARD)
        }
    }

    private fun addCardListener(index: Int, creditCardView: CreditCardView) {
        creditCardView.setOnClickListener { v ->
            val creditCardView = v as CreditCardView
            val cardNumber = creditCardView.cardNumber
            val expiry = creditCardView.expiry
            val cardHolderName = creditCardView.cardHolderName
            val cvv = creditCardView.cvv

            val intent = Intent(this@MainActivity, CardEditActivity::class.java)
            intent.putExtra(CreditCardUtils.EXTRA_CARD_HOLDER_NAME, cardHolderName)
            intent.putExtra(CreditCardUtils.EXTRA_CARD_NUMBER, cardNumber)
            intent.putExtra(CreditCardUtils.EXTRA_CARD_EXPIRY, expiry)
            intent.putExtra(CreditCardUtils.EXTRA_CARD_SHOW_CARD_SIDE, CreditCardUtils.CARD_SIDE_BACK)
            intent.putExtra(CreditCardUtils.EXTRA_VALIDATE_EXPIRY_DATE, false)

            // start at the CVV activity to edit it as it is not being passed
            intent.putExtra(CreditCardUtils.EXTRA_ENTRY_START_PAGE, CreditCardUtils.CARD_CVV_PAGE)
            startActivityForResult(intent, index)
        }
    }

    public override fun onActivityResult(reqCode: Int, resultCode: Int, data: Intent?) {

        if (resultCode == Activity.RESULT_OK) {
            //            Debug.printToast("Result Code is OK", getApplicationContext());

            val name = data?.getStringExtra(CreditCardUtils.EXTRA_CARD_HOLDER_NAME)
            val cardNumber = data?.getStringExtra(CreditCardUtils.EXTRA_CARD_NUMBER)
            val expiry = data?.getStringExtra(CreditCardUtils.EXTRA_CARD_EXPIRY)
            val cvv = data?.getStringExtra(CreditCardUtils.EXTRA_CARD_CVV)

            if (reqCode == CREATE_NEW_CARD) {

                val creditCardView = CreditCardView(this)

                creditCardView.cvv = cvv
                creditCardView.cardHolderName = name
                creditCardView.setCardExpiry(expiry)
                creditCardView.cardNumber = cardNumber

                card_container?.addView(creditCardView)
                val index = card_container?.childCount ?: 1 - 1
                addCardListener(index, creditCardView)

            } else {

                val creditCardView = card_container?.getChildAt(reqCode) as CreditCardView

                creditCardView.setCardExpiry(expiry)
                creditCardView.cardNumber = cardNumber
                creditCardView.cardHolderName = name
                creditCardView.cvv = cvv

            }
        }

    }

}