package com.cooltechworks.creditcarddesign;

import android.support.annotation.NonNull;

import java.util.Calendar;
import java.util.regex.Pattern;

/**
 * Created by Harish on 03/01/16.
 */
public class CreditCardUtils {
    public enum CardType {
        UNKNOWN_CARD, AMEX_CARD, MASTER_CARD, VISA_CARD, DISCOVER_CARD
    }

    private static final String PATTERN_AMEX = "^3(4|7)[0-9 ]*";
    private static final String PATTERN_VISA = "^4[0-9 ]*";
    private static final String PATTERN_MASTER = "^5[0-9 ]*";
    private static final String PATTERN_DISCOVER = "^6[0-9 ]*";

    public static final int MAX_LENGTH_CARD_NUMBER = 16;
    public static final int MAX_LENGTH_CARD_NUMBER_AMEX = 15;

    public static final String CARD_NUMBER_FORMAT = "XXXX XXXX XXXX XXXX";
    public static final String CARD_NUMBER_FORMAT_AMEX = "XXXX XXXXXX XXXXX";

    public static final String EXTRA_CARD_NUMBER = "card_number";
    public static final String EXTRA_CARD_CVV = "card_cvv";
    public static final String EXTRA_CARD_EXPIRY = "card_expiry";
    public static final String EXTRA_CARD_HOLDER_NAME = "card_holder_name";
    public static final String EXTRA_CARD_SHOW_CARD_SIDE = "card_side";
    public static final String EXTRA_VALIDATE_EXPIRY_DATE = "expiry_date";
    public static final String EXTRA_ENTRY_START_PAGE = "start_page";

    public static final int CARD_SIDE_FRONT = 1,CARD_SIDE_BACK=0;

    public static final int CARD_NUMBER_PAGE = 0, CARD_EXPIRY_PAGE = 1;
    public static final int CARD_CVV_PAGE = 2, CARD_NAME_PAGE = 3;

    public static final String SPACE_SEPERATOR = " ";
    public static final String SLASH_SEPERATOR = "/";
    public static final char CHAR_X = 'X';

    public static String handleCardNumber(String inputCardNumber) {

        return handleCardNumber(inputCardNumber,SPACE_SEPERATOR);
    }

    public static CardType selectCardType(String cardNumber) {
        Pattern pCardType = Pattern.compile(PATTERN_VISA);
        if(pCardType.matcher(cardNumber).matches())
            return CardType.VISA_CARD;
        pCardType = Pattern.compile(PATTERN_MASTER);
        if(pCardType.matcher(cardNumber).matches())
            return CardType.MASTER_CARD;
        pCardType = Pattern.compile(PATTERN_AMEX);
        if(pCardType.matcher(cardNumber).matches())
            return CardType.AMEX_CARD;
        pCardType = Pattern.compile(PATTERN_DISCOVER);
        if(pCardType.matcher(cardNumber).matches())
            return CardType.DISCOVER_CARD;
        return CardType.UNKNOWN_CARD;
    }

    public static int selectCardLength(CardType cardType) {
        return cardType == CardType.AMEX_CARD ? MAX_LENGTH_CARD_NUMBER_AMEX : MAX_LENGTH_CARD_NUMBER;
    }

    public static String handleCardNumber(String inputCardNumber, String seperator) {
        String unformattedText = inputCardNumber.replace(seperator, "");
        CardType cardType = selectCardType(inputCardNumber);
        String format = (cardType == CardType.AMEX_CARD) ? CARD_NUMBER_FORMAT_AMEX : CARD_NUMBER_FORMAT;
        StringBuilder sbFormattedNumber = new StringBuilder();
        for(int iIdx = 0, jIdx = 0; (iIdx < format.length()) && (unformattedText.length() > jIdx); iIdx++) {
            if(format.charAt(iIdx) == CHAR_X)
                sbFormattedNumber.append(unformattedText.charAt(jIdx++));
            else
                sbFormattedNumber.append(format.charAt(iIdx));
        }

        return sbFormattedNumber.toString();
    }

    public static String formatCardNumber(String inputCardNumber, String seperator) {
        String unformattedText = inputCardNumber.replace(seperator, "");
        CardType cardType = selectCardType(inputCardNumber);
        String format = (cardType == CardType.AMEX_CARD) ? CARD_NUMBER_FORMAT_AMEX : CARD_NUMBER_FORMAT;
        StringBuilder sbFormattedNumber = new StringBuilder();
        for(int iIdx = 0, jIdx = 0; iIdx < format.length(); iIdx++) {
            if((format.charAt(iIdx) == CHAR_X) && (unformattedText.length() > jIdx))
                sbFormattedNumber.append(unformattedText.charAt(jIdx++));
            else
                sbFormattedNumber.append(format.charAt(iIdx));
        }

        return sbFormattedNumber.toString().replace(SPACE_SEPERATOR, SPACE_SEPERATOR + SPACE_SEPERATOR);
    }

    public static String handleExpiration(String month, String year) {

        return handleExpiration(month+year);
    }


    public static String handleExpiration(@NonNull String dateYear) {

        String expiryString = dateYear.replace(SLASH_SEPERATOR, "");

        String text;
        if(expiryString.length() >= 2) {
            String mm = expiryString.substring(0, 2);
            String yy;
            text = mm;

            try {
                if (Integer.parseInt(mm) > 12) {
                    mm = "12"; // Cannot be more than 12.
                }
            }
            catch (Exception e) {
                mm = "01";
            }

            if(expiryString.length() >=4) {
                yy = expiryString.substring(2,4);

                try{
                    Integer.parseInt(yy);
                }catch (Exception e) {

                    Calendar calendar = Calendar.getInstance();
                    int year = calendar.get(Calendar.YEAR);
                    yy = String.valueOf(year).substring(2);
                }

                text = mm + SLASH_SEPERATOR + yy;

            }
            else if(expiryString.length() > 2){
                yy = expiryString.substring(2);
                text = mm + SLASH_SEPERATOR + yy;
            }
        }
        else {
            text = expiryString;
        }

        return text;
    }
}
