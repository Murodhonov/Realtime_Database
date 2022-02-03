package uz.umarxon.realtimedatabase.models

class User {
    var idToken:String? = null
    var name:String? = null
    var number:String? = null

    constructor(idToken: String?, name: String?, number: String?) {
        this.idToken = idToken
        this.name = name
        this.number = number
    }

    constructor(name: String?, number: String?) {
        this.name = name
        this.number = number
    }

    constructor()
}