package com.cooltechworks.creditcarddesign

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.os.Build
import android.os.Handler
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import io.codetail.animation.ViewAnimationUtils

/**
 * Created by Harish on 03/01/16.
 */
class CreditCardView : FrameLayout {


    private var mCurrentDrawable: Int = 0
    private var mRawCardNumber: String? = null
    private var mSelectorLogic: ICustomCardSelector? = null

    var cardHolderName: String? = null
        set(cardHolderName) {
            var cardHolderName = cardHolderName

            cardHolderName = cardHolderName ?: ""
            if (cardHolderName.length > mCardnameLen) {
                cardHolderName = cardHolderName.substring(0, mCardnameLen)
            }

            field = cardHolderName

            (findViewById<View>(TEXTVIEW_CARD_HOLDER_ID) as TextView).text = cardHolderName
        }
    var cvv: String? = null
    var expiry: String? = null
    var cardType: CreditCardUtils.CardType? = null
    private var changeCardColor: Boolean = false
    private var showCardAnimation: Boolean = false

    internal var mCardnameLen: Int = 0

    var cardNumber: String?
        get() = mRawCardNumber
        set(rawCardNumber) {


            this.mRawCardNumber = rawCardNumber ?: ""
            this.cardType = CreditCardUtils.selectCardType(this.mRawCardNumber!!)
            val cardNumber = CreditCardUtils.formatCardNumber(this.mRawCardNumber!!, CreditCardUtils.SPACE_SEPERATOR)

            (findViewById<View>(TEXTVIEW_CARD_NUMBER_ID) as TextView).text = cardNumber
            (findViewById<View>(TEXTVIEW_CARD_CVV_AMEX_ID) as TextView).visibility = if (cardType === CreditCardUtils.CardType.AMEX_CARD) View.VISIBLE else View.GONE

            if (this.cardType !== CreditCardUtils.CardType.UNKNOWN_CARD) {
                this.post { revealCardAnimation() }
            } else {
                paintCard()
            }

        }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    interface ICustomCardSelector {
        fun getCardSelector(cardNumber: String?): CardSelector
    }

    private fun init() {

        mCurrentDrawable = R.drawable.card_color_round_rect_default
        mRawCardNumber = ""
        mCardnameLen = resources.getInteger(R.integer.card_name_len)
        val inflater = context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.view_creditcard, this, true)

    }

    private fun init(attrs: AttributeSet) {

        init()

        val a = context.obtainStyledAttributes(attrs,
                R.styleable.creditcard, 0, 0)


        var cardHolderName = a.getString(R.styleable.creditcard_card_holder_name)
        val expiry = a.getString(R.styleable.creditcard_card_expiration)
        var cardNumber = a.getString(R.styleable.creditcard_card_number)

        changeCardColor = a.getBoolean(R.styleable.creditcard_change_card_color, true)
        showCardAnimation = a.getBoolean(R.styleable.creditcard_show_card_animation, true)
        val showChipOnCard = a.getBoolean(R.styleable.creditcard_show_chip_on_card, true)
        if (!showChipOnCard) {
            val chipContainer = findViewById<View>(R.id.chip_container)
            if (chipContainer != null) {
                chipContainer.visibility = View.INVISIBLE
            }
        }

        val cvv = a.getInt(R.styleable.creditcard_cvv, 0)
        val cardSide = a.getInt(R.styleable.creditcard_card_side, CreditCardUtils.CARD_SIDE_FRONT)

        cardNumber = cardNumber
        setCVV(cvv)
        setCardExpiry(expiry)
        cardHolderName = cardHolderName

        if (cardSide == CreditCardUtils.CARD_SIDE_BACK) {
            showBackImmediate()
        }

        paintCard()

        a.recycle()

    }


    private fun flip(ltr: Boolean, isImmediate: Boolean) {

        val layoutContainer = findViewById<View>(R.id.card_outline_container)
        val frontView = findViewById<View>(FRONT_CARD_OUTLINE_ID)
        val backView = findViewById<View>(BACK_CARD_OUTLINE_ID)

        val frontContentView = findViewById<View>(FRONT_CARD_ID)
        val backContentView = findViewById<View>(BACK_CARD_ID)
        val layoutContentContainer = findViewById<View>(R.id.card_container)


        if (isImmediate) {
            frontContentView.visibility = if (ltr) View.VISIBLE else View.GONE
            backContentView.visibility = if (ltr) View.GONE else View.VISIBLE

        } else {

            val duration = 600

            val flipAnimator = FlipAnimator(frontView, backView, frontView.width / 2, backView.height / 2)
            flipAnimator.interpolator = OvershootInterpolator(0.5f)
            flipAnimator.duration = duration.toLong()

            if (ltr) {
                flipAnimator.reverse()
            }

            flipAnimator.translateDirection = FlipAnimator.DIRECTION_Z
            flipAnimator.rotationDirection = FlipAnimator.DIRECTION_Y
            layoutContainer.startAnimation(flipAnimator)

            val flipAnimator1 = FlipAnimator(frontContentView, backContentView, frontContentView.width / 2, backContentView.height / 2)
            flipAnimator1.interpolator = OvershootInterpolator(0.5f)
            flipAnimator1.duration = duration.toLong()

            if (ltr) {
                flipAnimator1.reverse()
            }

            flipAnimator1.translateDirection = FlipAnimator.DIRECTION_Z
            flipAnimator1.rotationDirection = FlipAnimator.DIRECTION_Y

            layoutContentContainer.startAnimation(flipAnimator1)
        }

    }

    fun setCVV(cvvInt: Int) {

        if (cvvInt == 0) {
            setCVV("")
        } else {
            val cvv = cvvInt.toString()
            setCVV(cvv)
        }

    }

    fun showFront() {
        flip(true, false)
    }

    fun showFrontImmediate() {
        flip(true, true)
    }

    fun showBack() {
        flip(false, false)
    }

    fun showBackImmediate() {
        flip(false, true)
    }

    fun setCVV(cvv: String?) {
        var cvv = cvv
        if (cvv == null) {
            cvv = ""
        }

        this.cvv = cvv
        (findViewById<View>(TEXTVIEW_CARD_CVV_ID) as TextView).text = cvv
        (findViewById<View>(TEXTVIEW_CARD_CVV_AMEX_ID) as TextView).text = cvv
    }

    fun setCardExpiry(dateYear: String?) {
        var dateYear = dateYear

        dateYear = if (dateYear == null) "" else CreditCardUtils.handleExpiration(dateYear)

        this.expiry = dateYear

        (findViewById<View>(TEXTVIEW_CARD_EXPIRY_ID) as TextView).text = dateYear


    }

    fun paintCard() {

        val card = selectCard()

        val cardContainer = findViewById<View>(R.id.card_outline_container)

        val chipContainer = findViewById<View>(R.id.chip_container)
        val chipInner = findViewById<View>(R.id.chip_inner_view)

        val cardBack = findViewById<View>(BACK_CARD_OUTLINE_ID)
        val cardFront = findViewById<View>(FRONT_CARD_OUTLINE_ID)


        chipContainer.setBackgroundResource(card.resChipOuterId)
        chipInner.setBackgroundResource(card.resChipInnerId)

        val frontLogoImageView = cardContainer.findViewById<View>(R.id.logo_img) as ImageView
        frontLogoImageView.setImageResource(card.resLogoId)

        val centerImageView = cardContainer.findViewById<View>(R.id.logo_center_img) as ImageView
        centerImageView.setImageResource(card.resCenterImageId)


        val backLogoImageView = findViewById<View>(BACK_CARD_ID).findViewById<View>(R.id.logo_img) as ImageView
        backLogoImageView.setImageResource(card.resLogoId)

        if (changeCardColor) {
            cardBack.setBackgroundResource(card.resCardId)
            cardFront.setBackgroundResource(card.resCardId)
        }
    }


    fun revealCardAnimation() {

        val card = selectCard()

        val cardFront = findViewById<View>(FRONT_CARD_OUTLINE_ID)
        val cardContainer = findViewById<View>(R.id.card_outline_container)

        paintCard()

        if (showCardAnimation && changeCardColor) {
            animateChange(cardContainer, cardFront, card.resCardId)
        }
    }

    fun selectCard(): CardSelector {
        return if (mSelectorLogic != null) {
            mSelectorLogic!!.getCardSelector(mRawCardNumber)
        } else CardSelector.selectCard(mRawCardNumber)
    }

    fun animateChange(cardContainer: View, v: View, drawableId: Int) {
        showAnimation(cardContainer, v, drawableId)
    }

    fun showAnimation(cardContainer: View, v: View, drawableId: Int) {

        v.setBackgroundResource(drawableId)

        if (mCurrentDrawable == drawableId) {
            return
        }

        val duration = 1000
        val cx = v.left
        val cy = v.top

        val radius = Math.max(v.width, v.height) * 4

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {


            val animator = ViewAnimationUtils.createCircularReveal(v, cx, cy, 0f, radius.toFloat())
            animator.interpolator = AccelerateDecelerateInterpolator()
            animator.duration = duration.toLong()

            Handler().postDelayed({ cardContainer.setBackgroundResource(drawableId) }, duration.toLong())

            v.visibility = View.VISIBLE
            animator.start()
            mCurrentDrawable = drawableId

        } else {
            val anim = android.view.ViewAnimationUtils.createCircularReveal(v, cx, cy, 0f, radius.toFloat())
            v.visibility = View.VISIBLE
            anim.duration = duration.toLong()
            anim.start()
            anim.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)

                    cardContainer.setBackgroundResource(drawableId)
                }
            })

            mCurrentDrawable = drawableId
        }
    }

    fun setSelectorLogic(mSelectorLogic: ICustomCardSelector) {
        this.mSelectorLogic = mSelectorLogic
    }

    companion object {

        private val TEXTVIEW_CARD_HOLDER_ID = R.id.front_card_holder_name
        private val TEXTVIEW_CARD_EXPIRY_ID = R.id.front_card_expiry
        private val TEXTVIEW_CARD_NUMBER_ID = R.id.front_card_number
        private val TEXTVIEW_CARD_CVV_ID = R.id.back_card_cvv
        private val TEXTVIEW_CARD_CVV_AMEX_ID = R.id.front_card_cvv
        private val FRONT_CARD_ID = R.id.front_card_container
        private val BACK_CARD_ID = R.id.back_card_container
        private val FRONT_CARD_OUTLINE_ID = R.id.front_card_outline
        private val BACK_CARD_OUTLINE_ID = R.id.back_card_outline
    }


}
