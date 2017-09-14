package com.cooltechworks.creditcarddesign;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;

/**
 * Created by Harish on 03/01/16.
 */
public class CreditCardView extends FrameLayout {

    private static final int TEXTVIEW_CARD_HOLDER_ID = R.id.front_card_holder_name;
    private static final int TEXTVIEW_CARD_EXPIRY_ID = R.id.front_card_expiry;
    private static final int TEXTVIEW_CARD_NUMBER_ID = R.id.front_card_number;
    private static final int TEXTVIEW_CARD_CVV_ID = R.id.back_card_cvv;
    private static final int TEXTVIEW_CARD_CVV_AMEX_ID = R.id.front_card_cvv;
    private static final int FRONT_CARD_ID = R.id.front_card_container;
    private static final int BACK_CARD_ID = R.id.back_card_container;
    private static final int FRONT_CARD_OUTLINE_ID = R.id.front_card_outline;
    private static final int BACK_CARD_OUTLINE_ID = R.id.back_card_outline;


    private int mCurrentDrawable;
    private String mRawCardNumber;
    private ICustomCardSelector mSelectorLogic;

    private String mCardHolderName, mCVV, mExpiry;

    private CreditCardUtils.CardType mCardType;

    int mCardnameLen;

    public CreditCardView(Context context) {
        super(context);
        init();
    }

    public CreditCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CreditCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public String getCardHolderName() {
        return mCardHolderName;
    }

    public String getCVV() {
        return mCVV;
    }

    public String getExpiry() {
        return mExpiry;
    }

    public CreditCardUtils.CardType getCardType() { return mCardType; }

    interface ICustomCardSelector {
        CardSelector getCardSelector(String cardNumber);
    }
    
    private void init() {

        mCurrentDrawable = R.drawable.card_color_round_rect_default;
        mRawCardNumber = "";
        mCardnameLen = getResources().getInteger(R.integer.card_name_len);
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_creditcard, this, true);

    }

    private void init(AttributeSet attrs) {

        init();

        TypedArray a = getContext().obtainStyledAttributes(attrs,
                R.styleable.creditcard, 0, 0);


        String cardHolderName = a.getString(R.styleable.creditcard_card_holder_name);
        String expiry = a.getString(R.styleable.creditcard_card_expiration);
        String cardNumber = a.getString(R.styleable.creditcard_card_number);

        int cvv = a.getInt(R.styleable.creditcard_cvv, 0);
        int cardSide = a.getInt(R.styleable.creditcard_card_side,CreditCardUtils.CARD_SIDE_FRONT);

        setCardNumber(cardNumber);
        setCVV(cvv);
        setCardExpiry(expiry);
        setCardHolderName(cardHolderName);

        if(cardSide == CreditCardUtils.CARD_SIDE_BACK) {
            showBackImmediate();
        }

        paintCard();

        a.recycle();

    }


    private void flip(final boolean ltr, boolean isImmediate) {

        View layoutContainer = findViewById(R.id.card_outline_container);
        View frontView = findViewById(FRONT_CARD_OUTLINE_ID);
        View backView = findViewById(BACK_CARD_OUTLINE_ID);

        final View frontContentView = findViewById(FRONT_CARD_ID);
        final View backContentView = findViewById(BACK_CARD_ID);
        View layoutContentContainer = findViewById(R.id.card_container);


        if(isImmediate) {
            frontContentView.setVisibility(ltr?VISIBLE:GONE);
            backContentView.setVisibility(ltr?GONE:VISIBLE);

        }
        else {

            int duration =  600;

            FlipAnimator flipAnimator = new FlipAnimator(frontView, backView, frontView.getWidth() / 2, backView.getHeight() / 2);
            flipAnimator.setInterpolator(new OvershootInterpolator(0.5f));
            flipAnimator.setDuration(duration);

            if (ltr) {
                flipAnimator.reverse();
            }

            flipAnimator.setTranslateDirection(FlipAnimator.DIRECTION_Z);
            flipAnimator.setRotationDirection(FlipAnimator.DIRECTION_Y);
            layoutContainer.startAnimation(flipAnimator);

            FlipAnimator flipAnimator1 = new FlipAnimator(frontContentView, backContentView, frontContentView.getWidth() / 2, backContentView.getHeight() / 2);
            flipAnimator1.setInterpolator(new OvershootInterpolator(0.5f));
            flipAnimator1.setDuration(duration);

            if (ltr) {
                flipAnimator1.reverse();
            }

            flipAnimator1.setTranslateDirection(FlipAnimator.DIRECTION_Z);
            flipAnimator1.setRotationDirection(FlipAnimator.DIRECTION_Y);

            layoutContentContainer.startAnimation(flipAnimator1);
        }

    }

    public void setCardNumber(String rawCardNumber) {


        this.mRawCardNumber = rawCardNumber == null ? "" : rawCardNumber;
        this.mCardType = CreditCardUtils.selectCardType(this.mRawCardNumber);
        String cardNumber = CreditCardUtils.formatCardNumber(this.mRawCardNumber, CreditCardUtils.SPACE_SEPERATOR);

        ((TextView)findViewById(TEXTVIEW_CARD_NUMBER_ID)).setText(cardNumber);
        ((TextView)findViewById(TEXTVIEW_CARD_CVV_AMEX_ID)).setVisibility(mCardType == CreditCardUtils.CardType.AMEX_CARD ? View.VISIBLE : View.GONE);

        if(this.mCardType != CreditCardUtils.CardType.UNKNOWN_CARD) {
            this.post(new Runnable() {
                @Override
                public void run() {
                    revealCardAnimation();
                }
            });
        }
        else {
            paintCard();
        }

    }

    public void setCVV(int cvvInt) {

        if(cvvInt == 0) {
            setCVV("");
        }
        else {
            String cvv = String.valueOf(cvvInt);
            setCVV(cvv);
        }

    }

    public void showFront() {
        flip(true,false);
    }

    public void showFrontImmediate() {
        flip(true,true);
    }

    public void showBack() {
        flip(false,false);
    }

    public void showBackImmediate() {
        flip(false,true);
    }

    public void setCVV(String cvv) {
        if(cvv == null) {
            cvv = "";
        }

        this.mCVV = cvv;
        ((TextView)findViewById(TEXTVIEW_CARD_CVV_ID)).setText(cvv);
        ((TextView)findViewById(TEXTVIEW_CARD_CVV_AMEX_ID)).setText(cvv);
    }

    public void setCardExpiry(String dateYear) {

        dateYear = dateYear == null ? "": CreditCardUtils.handleExpiration(dateYear);

        this.mExpiry = dateYear;

        ((TextView) findViewById(TEXTVIEW_CARD_EXPIRY_ID)).setText(dateYear);


    }

    public void setCardHolderName(String cardHolderName) {

        cardHolderName = cardHolderName == null ? "" : cardHolderName;
        if(cardHolderName.length() > mCardnameLen) {
            cardHolderName = cardHolderName.substring(0,mCardnameLen);
        }

        this.mCardHolderName = cardHolderName;

        ((TextView)findViewById(TEXTVIEW_CARD_HOLDER_ID)).setText(cardHolderName);
    }

    public void paintCard() {

        CardSelector card = selectCard();

        View cardContainer = findViewById(R.id.card_outline_container);

        View chipContainer = findViewById(R.id.chip_container);
        View chipInner = findViewById(R.id.chip_inner_view);

        View cardBack =  findViewById(BACK_CARD_OUTLINE_ID);
        View cardFront = findViewById(FRONT_CARD_OUTLINE_ID);


        chipContainer.setBackgroundResource(card.getResChipOuterId());
        chipInner.setBackgroundResource(card.getResChipInnerId());

        ImageView frontLogoImageView = (ImageView) cardContainer.findViewById(R.id.logo_img);
        frontLogoImageView.setImageResource(card.getResLogoId());

        ImageView centerImageView = (ImageView) cardContainer.findViewById(R.id.logo_center_img);
        centerImageView.setImageResource(card.getResCenterImageId());


        ImageView backLogoImageView = (ImageView) findViewById(BACK_CARD_ID).findViewById(R.id.logo_img);
        backLogoImageView.setImageResource(card.getResLogoId());

        cardBack.setBackgroundResource(card.getResCardId());
        cardFront.setBackgroundResource(card.getResCardId());
    }


    public void revealCardAnimation() {

        CardSelector card = selectCard();

        View cardFront = findViewById(FRONT_CARD_OUTLINE_ID);
        View cardContainer = findViewById(R.id.card_outline_container);

        paintCard();

        animateChange(cardContainer, cardFront, card.getResCardId());
    }

    public CardSelector selectCard() {
        if(mSelectorLogic != null) {
            return mSelectorLogic.getCardSelector(mRawCardNumber);
        }
        return CardSelector.selectCard(mRawCardNumber);
    }

    public void animateChange(final View cardContainer, final View v, final int drawableId) {
        showAnimation(cardContainer, v, drawableId);
    }

    public void showAnimation(final View cardContainer, final View v, final int drawableId) {

        final View mRevealView = v;
        mRevealView.setBackgroundResource(drawableId);

        if (mCurrentDrawable == drawableId) {
            return;
        }

        int duration = 1000;
        int cx = mRevealView.getLeft();
        int cy = mRevealView.getTop();

        int radius = Math.max(mRevealView.getWidth(), mRevealView.getHeight()) * 4;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {


            SupportAnimator animator =
                    ViewAnimationUtils.createCircularReveal(mRevealView, cx, cy, 0, radius);
            animator.setInterpolator(new AccelerateDecelerateInterpolator());
            animator.setDuration(duration);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    cardContainer.setBackgroundResource(drawableId);
                }
            }, duration);

            mRevealView.setVisibility(View.VISIBLE);
            animator.start();
            mCurrentDrawable = drawableId;

        } else {
            Animator anim = android.view.ViewAnimationUtils.createCircularReveal(mRevealView, cx, cy, 0, radius);
            mRevealView.setVisibility(View.VISIBLE);
            anim.setDuration(duration);
            anim.start();
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);

                    cardContainer.setBackgroundResource(drawableId);
                }
            });

            mCurrentDrawable = drawableId;
        }
    }

    public void setSelectorLogic(ICustomCardSelector mSelectorLogic) {
        this.mSelectorLogic = mSelectorLogic;
    }

    public String getCardNumber() {
        return mRawCardNumber;
    }


}
