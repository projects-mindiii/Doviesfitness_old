package com.doviesfitness.ui.authentication.signup.model

data class Country(
    var flag : Int,
    var country_name : String,
    var phone_code : String,
    var code : String,
    var isSelected : Boolean
)