package com.cooltechworks.creditcarddesign.pager

interface IActionListener {
    fun onActionComplete(fragment: CreditCardFragment)
    fun onEdit(fragment: CreditCardFragment, edit: String)

}