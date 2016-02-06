# CreditCardView

CreditCardView is a rich UX custom view to accomodate Credit Cards / Debit Cards while handling payment systems. The View looks like below :

|     VISA FRONT VIEW         |        MASTER FRONT VIEW      |     AMEX FRONT VIEW       | 
| --------------------------- | ----------------------------- | ------------------------- |
| ![VISA FRONT][visa_front] | ![MASTER FRONT][master_front] | | ![AMEX FRONT][amex_front] |
|     VISA BACK VIEW          |        MASTER BACK VIEW       |     AMEX BACK VIEW        | 
| ![VISA FRONT][visa_front] | ![MASTER FRONT][master_front] | | ![AMEX FRONT][amex_front] |


## Creating a View

```xml
<com.cooltechworks.creditcarddesign.CreditCardView
          android:id="@+id/card_5"
          android:layout_width="wrap_content"
          android:layout_height="wrap_content"
          app:card_number="38056789000000000"
          app:card_holder_name="JOHN MATTEW"
          app:cvv="522"
          app:card_expiration="01/12"
          />
```



[master_front]:https://cloud.githubusercontent.com/assets/13122232/12864941/adbca140-ccc3-11e5-8592-808dca8ea5ff.png 

