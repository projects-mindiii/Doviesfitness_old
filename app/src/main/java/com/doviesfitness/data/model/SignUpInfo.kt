package com.doviesfitness.data.model

import java.io.Serializable

class SignUpInfo  : Serializable{
    private var name: String? = null
    private var email: String? = null
    private var mobile_number: String? = null
    private var isd_code: String? = null
    private var country_id: String? = null
    private var gender: String? = null
    private var height: String? = null
    private var email_upadates: String? = null
    private var password: String? = null
    private var weight: String? = null
    private var user_name: String? = null
    private var dob: String? = null

    private var device_type: String? = ""
    private var device_id: String? = ""
    private var device_token: String? = ""
    private var social_network_id: String? = ""
    private var social_network_type: String? = ""

    fun getSocialNetworkType(): String? {
        return social_network_type
    }

    fun setSocialNetworkType(social_network_type: String) {
        this.social_network_type = social_network_type
    }

    fun getSocialNetworkId(): String? {
        return social_network_id
    }

    fun setSocialNetworkId(social_network_id: String) {
        this.social_network_id = social_network_id
    }


    fun getDeviceId(): String? {
        return device_id
    }

    fun setDeviceId(device_id: String) {
        this.device_id = device_id
    }

    fun getDevicToken(): String? {
        return device_token
    }

    fun setDevicToken(device_token: String) {
        this.device_token = device_token
    }

    fun getDevicType(): String? {
        return device_type
    }

    fun setDevicType(device_type: String) {
        this.device_type = device_type
    }

    fun getEmail(): String? {
        return email
    }

    fun setEmail(email: String) {
        this.email = email
    }

    fun getMobile_number(): String? {
        return mobile_number
    }

    fun setMobile_number(mobile_number: String) {
        this.mobile_number = mobile_number
    }

    fun getIsd_code(): String? {
        return isd_code
    }

    fun setIsd_code(isd_code: String) {
        this.isd_code = isd_code
    }

    fun getCountry_id(): String? {
        return country_id
    }

    fun setCountry_id(country_id: String) {
        this.country_id = country_id
    }

    fun getGender(): String? {
        return gender
    }

    fun setGender(gender: String) {
        this.gender = gender
    }

    fun getHeight(): String? {
        return height
    }

    fun setHeight(height: String) {
        this.height = height
    }

    fun getEmail_upadates(): String? {
        return email_upadates
    }

    fun setEmail_upadates(email_upadates: String) {
        this.email_upadates = email_upadates
    }

    fun getPassword(): String? {
        return password
    }

    fun setPassword(password: String) {
        this.password = password
    }

    fun getWeight(): String? {
        return weight
    }

    fun setWeight(weight: String) {
        this.weight = weight
    }

    fun getUser_name(): String? {
        return user_name
    }

    fun setUser_name(user_name: String) {
        this.user_name = user_name
    }

    fun getDob(): String? {
        return dob
    }

    fun setDob(dob: String) {
        this.dob = dob
    }


    fun getName(): String? {
        return name
    }

    fun setName(name: String) {
        this.name = name
    }
}