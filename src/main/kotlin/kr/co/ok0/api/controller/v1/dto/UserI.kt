package kr.co.ok0.api.controller.v1.dto

import java.time.Instant

data class UserReqI(
  var password: String,
  var userName: String,
  var userNickName: String,
)

data class UserResI(
  var result: UserResultIType,
  var user: UserI?
)

enum class UserResultIType {
  SUCCESS,
  UNKNOWN_ERROR,
  EXISTS_ID,
  EXISTS_NICKNAME,
  PASSWORD_ERROR,
}

data class UserI(
  var userNo: Long,
  var userId: String,
  var userName: String,
  var userNickName: String,
  var loggedInCount: Int,
  var lastLoggedIn: Instant,
)

data class UserLoginResI(
  var result: UserLoginResultIType
)

enum class UserLoginResultIType {
  SUCCESS, NOT_FOUND_ID, PASSWORD_NOT_MATCHED
}