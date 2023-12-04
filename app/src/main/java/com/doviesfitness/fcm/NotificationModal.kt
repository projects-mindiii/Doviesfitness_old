package com.doviesfitness.fcm

import java.io.Serializable

class NotificationModal : Serializable {

    public var type = ""
    public  var titleMsg = ""
    public  var body = ""

    public var module_id = ""
    public var module_name = ""
    public var mediaUrl = ""
}