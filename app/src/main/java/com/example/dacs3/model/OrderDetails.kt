package com.example.dacs3.model

import android.os.Parcel
import android.os.Parcelable

class OrderDetails() :Parcelable {
    var userUid:String?=null
    var userName:String?=null
    var address:String?=null
    var phone:String?=null
    var currentTime:Long=0
    var drinkNames:MutableList<String>?=null
    var drinkPrices:MutableList<String>?=null
    var drinkImages:MutableList<String>?=null
    var drinkQuantities:MutableList<Int>?=null
    var totalPrice:String?=null
    var itemPushKey:String?=null
    var orderAccept:Boolean=false
    var paymentReceive:Boolean=false

    constructor(parcel: Parcel) : this() {
        userUid = parcel.readString()
        userName = parcel.readString()
        address = parcel.readString()
        phone = parcel.readString()
        currentTime = parcel.readLong()
        totalPrice = parcel.readString()
        itemPushKey = parcel.readString()
        orderAccept = parcel.readByte() != 0.toByte()
        paymentReceive = parcel.readByte() != 0.toByte()
    }
    constructor(
        userId:String,
        name :String,
        address:String,
        phone:String,
        time:Long,
        drinkItemName:ArrayList<String>,
        drinkItemPrice:ArrayList<String>,
        drinkItemImage:ArrayList<String>,
        drinkItemQuantities:ArrayList<Int>,
        totalAmount:String,
        itemPushKey:String?,
        b:Boolean,
        b1:Boolean
    ) : this(){
        this.userUid= userId
        this.userName=name
        this.drinkNames=drinkItemName
        this.drinkImages=drinkItemImage
        this.drinkPrices=drinkItemPrice
        this.drinkQuantities=drinkItemQuantities
        this.address=address
        this.phone=phone
        this.currentTime=time
        this.totalPrice=totalAmount
        this.itemPushKey=itemPushKey
        this.orderAccept=b
        this.paymentReceive=b1
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(userUid)
        parcel.writeString(userName)
        parcel.writeString(address)
        parcel.writeString(phone)
        parcel.writeLong(currentTime)
        parcel.writeString(totalPrice)
        parcel.writeString(itemPushKey)
        parcel.writeByte(if (orderAccept) 1 else 0)
        parcel.writeByte(if (paymentReceive) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<OrderDetails> {
        override fun createFromParcel(parcel: Parcel): OrderDetails {
            return OrderDetails(parcel)
        }

        override fun newArray(size: Int): Array<OrderDetails?> {
            return arrayOfNulls(size)
        }
    }


}