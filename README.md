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



[master_front]:https://cloud.githubusercontent.com/assets/13122232/12871102/8b681fae-cd8e-11e5-8831-7b1fc1970194.png
[master_back]:https://cloud.githubusercontent.com/assets/13122232/12871095/4ce9a234-cd8e-11e5-8c4e-384ce6874029.png
[visa_front]:https://cloud.githubusercontent.com/assets/13122232/12871145/b57f92f2-cd90-11e5-8f36-086b227d06c3.png
[amex_front]:https://cloud.githubusercontent.com/assets/13122232/12871146/c22cf74c-cd90-11e5-85e4-3ab45d50ba76.png
[amex_back]:https://cloud.githubusercontent.com/assets/13122232/12871156/3124d570-cd91-11e5-8b71-f333e46192bb.png
[visa_back]:https://cloud.githubusercontent.com/assets/13122232/12871157/31625b5c-cd91-11e5-87f7-f42a6404842b.png




