package com.noordwind.apps.collectively.data.model

class AuthRequest(val email: String, val password: String, val provider: String)

class ChangePasswordRequest(val currentPassword: String, val newPassword: String)

class FacebookAuthRequest(val accessToken: String, val provider: String)

class AuthResponse(val token: String)

class SignUpRequest(val name:String, val email: String, val password: String)

class ResetPasswordRequest(val email: String)

class SetNickNameRequest(val name: String)

