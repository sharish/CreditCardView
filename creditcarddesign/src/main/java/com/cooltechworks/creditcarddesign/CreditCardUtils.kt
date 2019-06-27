package com.cooltechworks.creditcarddesign

import java.util.Calendar
import java.util.regex.Pattern

/**
 * Created by Harish on 03/01/16.
 */
public object CreditCardUtils {

    private val PATTERN_AMEX = "^3(4|7)[0-9 ]*"
    private val PATTERN_VISA = "^4[0-9 ]*"
    private val PATTERN_MASTER = "^5[0-9 ]*"
    private val PATTERN_DISCOVER = "^6[0-9 ]*"

    val MAX_LENGTH_CARD_NUMBER = 16
    val MAX_LENGTH_CARD_NUMBER_AMEX = 15

    val CARD_NUMBER_FORMAT = "XXXX XXXX XXXX XXXX"
    val CARD_NUMBER_FORMAT_AMEX = "XXXX XXXXXX XXXXX"

    val EXTRA_CARD_NUMBER = "card_number"
    val EXTRA_CARD_CVV = "card_cvv"
    val EXTRA_CARD_EXPIRY = "card_expiry"
    val EXTRA_CARD_HOLDER_NAME = "card_holder_name"
    val EXTRA_CARD_SHOW_CARD_SIDE = "card_side"
    val EXTRA_VALIDATE_EXPIRY_DATE = "expiry_date"
    val EXTRA_ENTRY_START_PAGE = "start_page"

    val CARD_SIDE_FRONT = 1
    val CARD_SIDE_BACK = 0

    val CARD_NUMBER_PAGE = 0
    val CARD_EXPIRY_PAGE = 1
    val CARD_CVV_PAGE = 2
    val CARD_NAME_PAGE = 3

    val SPACE_SEPERATOR = " "
    val SLASH_SEPERATOR = "/"
    val CHAR_X = 'X'

    enum class CardType {
        UNKNOWN_CARD, AMEX_CARD, MASTER_CARD, VISA_CARD, DISCOVER_CARD
    }

    fun selectCardType(cardNumber: String): CardType {
        var pCardType = Pattern.compile(PATTERN_VISA)
        if (pCardType.matcher(cardNumber).matches())
            return CardType.VISA_CARD
        pCardType = Pattern.compile(PATTERN_MASTER)
        if (pCardType.matcher(cardNumber).matches())
            return CardType.MASTER_CARD
        pCardType = Pattern.compile(PATTERN_AMEX)
        if (pCardType.matcher(cardNumber).matches())
            return CardType.AMEX_CARD
        pCardType = Pattern.compile(PATTERN_DISCOVER)
        return if (pCardType.matcher(cardNumber).matches()) CardType.DISCOVER_CARD else CardType.UNKNOWN_CARD
    }

    fun selectCardLength(cardType: CardType): Int {
        return if (cardType == CardType.AMEX_CARD) MAX_LENGTH_CARD_NUMBER_AMEX else MAX_LENGTH_CARD_NUMBER
    }

    @JvmOverloads
    fun handleCardNumber(inputCardNumber: String, seperator: String = SPACE_SEPERATOR): String {
        val unformattedText = inputCardNumber.replace(seperator, "")
        val cardType = selectCardType(inputCardNumber)
        val format = if (cardType == CardType.AMEX_CARD) CARD_NUMBER_FORMAT_AMEX else CARD_NUMBER_FORMAT
        val sbFormattedNumber = StringBuilder()
        var iIdx = 0
        var jIdx = 0
        while (iIdx < format.length && unformattedText.length > jIdx) {
            if (format[iIdx] == CHAR_X)
                sbFormattedNumber.append(unformattedText[jIdx++])
            else
                sbFormattedNumber.append(format[iIdx])
            iIdx++
        }

        return sbFormattedNumber.toString()
    }

    fun formatCardNumber(inputCardNumber: String, seperator: String): String {
        val unformattedText = inputCardNumber.replace(seperator, "")
        val cardType = selectCardType(inputCardNumber)
        val format = if (cardType == CardType.AMEX_CARD) CARD_NUMBER_FORMAT_AMEX else CARD_NUMBER_FORMAT
        val sbFormattedNumber = StringBuilder()
        var iIdx = 0
        var jIdx = 0
        while (iIdx < format.length) {
            if (format[iIdx] == CHAR_X && unformattedText.length > jIdx)
                sbFormattedNumber.append(unformattedText[jIdx++])
            else
                sbFormattedNumber.append(format[iIdx])
            iIdx++
        }

        return sbFormattedNumber.toString().replace(SPACE_SEPERATOR, SPACE_SEPERATOR + SPACE_SEPERATOR)
    }

    fun handleExpiration(month: String, year: String): String {

        return handleExpiration(month + year)
    }


    fun handleExpiration(dateYear: String): String {

        val expiryString = dateYear.replace(SLASH_SEPERATOR, "")

        var text: String
        if (expiryString.length >= 2) {
            var mm = expiryString.substring(0, 2)
            var yy: String
            text = mm

            try {
                if (Integer.parseInt(mm) > 12) {
                    mm = "12" // Cannot be more than 12.
                }
            } catch (e: Exception) {
                mm = "01"
            }

            if (expiryString.length >= 4) {
                yy = expiryString.substring(2, 4)

                try {
                    Integer.parseInt(yy)
                } catch (e: Exception) {

                    val calendar = Calendar.getInstance()
                    val year = calendar.get(Calendar.YEAR)
                    yy = year.toString().substring(2)
                }

                text = mm + SLASH_SEPERATOR + yy

            } else if (expiryString.length > 2) {
                yy = expiryString.substring(2)
                text = mm + SLASH_SEPERATOR + yy
            }
        } else {
            text = expiryString
        }

        return text
    }
}
