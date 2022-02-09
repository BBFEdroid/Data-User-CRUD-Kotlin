package com.bb.crudkotlin

class DataUser {
    var user_id: Int? = null
    var user_name: String? = null
    var user_phone: String? = null
    var user_email: String? = null
    var user_alamat: String? = null

    constructor(id: Int, name: String, phone: String, email: String, alamat: String) {
        this.user_id = id
        this.user_name = name
        this.user_phone = phone
        this.user_email = email
        this.user_alamat = alamat
    }

}