package pl.adriankremski.collectively.model

class User(
        val userId: String,
        val email: String,
        val name: String,
        val provider: String,
        val pictureUrl: String,
        val role: String,
        val state: String,
        val externalUserId: String,
        val createdAt: String
)
