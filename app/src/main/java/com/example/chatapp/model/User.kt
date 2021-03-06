package com.example.chatapp.model

data class User(
    val name: String,
    val imageUrl: String,
    val thumbImage: String,
    val uid: String,// user id, that we get from authentication
    val deviceToken: String,// This will be used to send notifications
    val status: String,// Whats app status
    val onlineStatus: String// status for whether the user is online or not. Can be boolean too
    // You can even set the typing status : val isTyping: Boolean
) {
    constructor() : this("", "", "", "", "", "", "")
    constructor(
        name: String,
        imageUrl: String,
        thumbImage: String,
        uid: String
    )  : this(
    name,
    imageUrl,
    thumbImage,
    uid,
    "",
    "Hey There, I am using whatsapp",
    ""
    )
}