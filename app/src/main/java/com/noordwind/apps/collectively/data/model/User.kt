package com.noordwind.apps.collectively.data.model

import java.io.Serializable


class User(val avatarUrl: String, val name: String, val userId: String) : Serializable {
}

class Avatar(val url: String) {

}

