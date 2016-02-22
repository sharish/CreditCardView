# CreditCardView

### Intro

CreditCardView is a rich UX custom view to accomodate Credit Cards / Debit Cards while handling payment systems.  The library consists of 

* CreditCardView which looks like below

|     FRONT VIEW                |        BACK VIEW              | 
| ----------------------------  | ----------------------------- | 
| ![MASTER FRONT][master_front] | ![MASTER BACK][master_back]   |

* CardEditActivity which behaves as below.

![](https://d13yacurqjgara.cloudfront.net/users/484057/screenshots/2177105/checkout_generic.gif)




## Creating a CreditCardView

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

## Calling CardEditActivity to fetch new card details from User

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


## Prefilling CardEditActivity with details of existing card.

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

### Sample Demo Video
[![Demo Video](https://cloud.githubusercontent.com/assets/13122232/13137455/25a15b6a-d647-11e5-90d0-5c410e6f64f7.png)](https://youtu.be/uPJr0WrO-X0 "CreditCardView Demo Video")


## Adding to your project

- Add the following configuration in your build.gradle file.

```gradle
repositories {
    jcenter()
    maven { url "https://jitpack.io" }
}

dependencies {
    compile 'com.github.cooltechworks:CreditCardView:v1.0'
}
```
- Add the following activity to your AndroidManifest.xml

```xml
 <activity android:name="com.cooltechworks.creditcarddesign.CardEditActivity"
            android:screenOrientation="portrait"
            />
```

Design Credits
============
* Ramakrishna - <https://dribbble.com/RamakrishnaUX>

Developed By
============

* Harish Sridharan - <harish.sridhar@gmail.com>

[master_front]:https://cloud.githubusercontent.com/assets/13122232/12871102/8b681fae-cd8e-11e5-8831-7b1fc1970194.png
[master_back]:https://cloud.githubusercontent.com/assets/13122232/12871095/4ce9a234-cd8e-11e5-8c4e-384ce6874029.png
[visa_front]:https://cloud.githubusercontent.com/assets/13122232/12871145/b57f92f2-cd90-11e5-8f36-086b227d06c3.png
[amex_front]:https://cloud.githubusercontent.com/assets/13122232/12871146/c22cf74c-cd90-11e5-85e4-3ab45d50ba76.png
[amex_back]:https://cloud.githubusercontent.com/assets/13122232/12871156/3124d570-cd91-11e5-8b71-f333e46192bb.png
[visa_back]:https://cloud.githubusercontent.com/assets/13122232/12871157/31625b5c-cd91-11e5-87f7-f42a6404842b.png




