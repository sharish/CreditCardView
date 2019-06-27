package com.cooltechworks.creditcarddesign

/**
 * Created by Harish on 01/01/16.
 */
class CardSelector(var resCardId: Int, var resChipOuterId: Int, var resChipInnerId: Int, var resCenterImageId: Int, var resLogoId: Int, cvvLength: Int) {
    var cvvLength = CVV_LENGHT_DEFAULT

    init {
        this.cvvLength = cvvLength
    }

    companion object {

        val VISA = CardSelector(R.drawable.card_color_round_rect_purple, R.drawable.chip, R.drawable.chip_inner, android.R.color.transparent, R.drawable.ic_billing_visa_logo, CardSelector.CVV_LENGHT_DEFAULT)
        val MASTER = CardSelector(R.drawable.card_color_round_rect_pink, R.drawable.chip_yellow, R.drawable.chip_yellow_inner, android.R.color.transparent, R.drawable.ic_billing_mastercard_logo, CardSelector.CVV_LENGHT_DEFAULT)
        val AMEX = CardSelector(R.drawable.card_color_round_rect_green, android.R.color.transparent, android.R.color.transparent, R.drawable.img_amex_center_face, R.drawable.ic_billing_amex_logo1, CardSelector.CVV_LENGHT_AMEX)
        val DISCOVER = CardSelector(R.drawable.card_color_round_rect_brown, android.R.color.transparent, android.R.color.transparent, android.R.color.transparent, R.drawable.ic_billing_discover_logo, CardSelector.CVV_LENGHT_DEFAULT)
        val DEFAULT = CardSelector(R.drawable.card_color_round_rect_default, R.drawable.chip, R.drawable.chip_inner, android.R.color.transparent, android.R.color.transparent, CardSelector.CVV_LENGHT_DEFAULT)

        val CVV_LENGHT_DEFAULT = 3
        val CVV_LENGHT_AMEX = 4

        fun selectCardType(cardType: CreditCardUtils.CardType): CardSelector {
            when (cardType) {
                CreditCardUtils.CardType.AMEX_CARD -> return AMEX
                CreditCardUtils.CardType.DISCOVER_CARD -> return DISCOVER
                CreditCardUtils.CardType.MASTER_CARD -> return MASTER
                CreditCardUtils.CardType.VISA_CARD -> return VISA
                else -> return DEFAULT
            }
        }

        fun selectCard(cardNumber: String?): CardSelector {
            if (cardNumber != null && cardNumber.length >= 1) {
                val cardType = CreditCardUtils.selectCardType(cardNumber)
                val selector = selectCardType(cardType)

                if (selector !== DEFAULT && cardNumber.length >= 3) {
                    val drawables = intArrayOf(R.drawable.card_color_round_rect_brown, R.drawable.card_color_round_rect_green, R.drawable.card_color_round_rect_pink, R.drawable.card_color_round_rect_purple, R.drawable.card_color_round_rect_blue)
                    var hash = cardNumber.substring(0, 3).hashCode()

                    if (hash < 0) {
                        hash = hash * -1
                    }

                    val index = hash % drawables.size

                    val chipIndex = hash % 3
                    val chipOuter = intArrayOf(R.drawable.chip, R.drawable.chip_yellow, android.R.color.transparent)
                    val chipInner = intArrayOf(R.drawable.chip_inner, R.drawable.chip_yellow_inner, android.R.color.transparent)

                    selector.resCardId = drawables[index]
                    selector.resChipOuterId = chipOuter[chipIndex]
                    selector.resChipInnerId = chipInner[chipIndex]

                    return selector
                }
            }

            return DEFAULT
        }
    }
}
