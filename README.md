# CreditCardView

CreditCardView is a rich UX custom view to accomodate Credit Cards / Debit Cards while handling payment systems. The View looks like below :

### How a MASTERCARD looks like with this View

|     MASTER FRONT VIEW         |        MASTER BACK VIEW       | 
| ----------------------------  | ----------------------------- | 
| ![MASTER FRONT][master_front] | ![MASTER BACK][master_back]   |


### How VISA card looks like with this View

|     VISA FRONT VIEW           |        VISA BACK VIEW         | 
| ----------------------------  | ----------------------------- | 
| ![VISA FRONT][visa_front] | ![VISA BACK][visa_back]   |


### How AMEX looks like with this View

|     AMEX FRONT VIEW           |        AMEX BACK VIEW         | 
| ----------------------------  | ----------------------------- | 
| ![AMEX FRONT][amex_front] | ![AMEX BACK][amex_back]   |



## Creating a View

##### XML
```xml
<com.cooltechworks.creditcarddesign.CreditCardView
          android:id="@+id/card_5"
          android:layout_width="wrap_content"
          android:layout_height="wrap_content"
          app:card_number="38056789000000000"
          app:card_holder_name="HARISH SRIDHARAN"
          app:cvv="522"
          app:card_expiration="01/17"
          />
```

##### JAVA
```java

   CreditCardView creditCardView = new CreditCardView(getContext());
   
   String name = "HARISH SRIDHARAN";
   String cvv = "522";
   String expiry = "01/17";
   String cardNumber = "38056789000000000";

   creditCardView.setCVV(cvv);
   creditCardView.setCardHolderName(name);
   creditCardView.setCardExpiry(expiry);
   creditCardView.setCardNumber(cardNumber);

```

## Getting a Card information from the User

To get a card information from the user, you can simply start the CardEditActivity as below and get the details of the card
from onActivityResult() in your activity.

##### Starting the activity
```java
final int GET_NEW_CARD = 2;

Intent intent = new Intent(MainActivity.this, CardEditActivity.class);
startActivityForResult(intent,GET_NEW_CARD);
```

##### Getting the card details
```java

public void onActivityResult(int reqCode, int resultCode, Intent data) {

        if(resultCode == RESULT_OK) {

                String cardHolderName = data.getStringExtra(CreditCardUtils.EXTRA_CARD_HOLDER_NAME);
                String cardNumber = data.getStringExtra(CreditCardUtils.EXTRA_CARD_NUMBER);
                String expiry = data.getStringExtra(CreditCardUtils.EXTRA_CARD_EXPIRY);
                String cvv = data.getStringExtra(CreditCardUtils.EXTRA_CARD_CVV);
          
                // Your processing goes here.

            }
        }
    }
```


## Editing a Card infomation

To edit the card details, you can start CardEditActivity passing the extras and get back the edited card information in onActivityResult() method of your activity just like above.
```java

final int EDIT_CARD = 5;
Intent intent = new Intent(MainActivity.this, CardEditActivity.class);
intent.putExtra(CreditCardUtils.EXTRA_CARD_HOLDER_NAME, cardHolderName);
intent.putExtra(CreditCardUtils.EXTRA_CARD_NUMBER, cardNumber);
intent.putExtra(CreditCardUtils.EXTRA_CARD_EXPIRY, expiry);
intent.putExtra(CreditCardUtils.EXTRA_CARD_SHOW_CARD_SIDE, CreditCardUtils.CARD_SIDE_BACK);

startActivityForResult(intent, EDIT_CARD);
```
                    

[master_front]:https://cloud.githubusercontent.com/assets/13122232/12871102/8b681fae-cd8e-11e5-8831-7b1fc1970194.png
[master_back]:https://cloud.githubusercontent.com/assets/13122232/12871095/4ce9a234-cd8e-11e5-8c4e-384ce6874029.png
[visa_front]:https://cloud.githubusercontent.com/assets/13122232/12871145/b57f92f2-cd90-11e5-8f36-086b227d06c3.png
[amex_front]:https://cloud.githubusercontent.com/assets/13122232/12871146/c22cf74c-cd90-11e5-85e4-3ab45d50ba76.png
[amex_back]:https://cloud.githubusercontent.com/assets/13122232/12871156/3124d570-cd91-11e5-8b71-f333e46192bb.png
[visa_back]:https://cloud.githubusercontent.com/assets/13122232/12871157/31625b5c-cd91-11e5-87f7-f42a6404842b.png




