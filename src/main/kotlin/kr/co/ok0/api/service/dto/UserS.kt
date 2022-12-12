package kr.co.ok0.api.service.dto

import java.time.Instant

data class UserParamS(
  var userId: String,
  var password: String,
  var userName: String,
  var userNickName: String,
)
data class UserResultS(
  var result: UserResultSType,
  var user: UserS?
)
enum class UserResultSType {
  SUCCESS,
  UNKNOWN_ERROR,
  EXISTS_ID,
  EXISTS_NICKNAME,
}
data class UserS(
  var userNo: Long,
  var userId: String,
  var userName: String,
  var userNickName: String,
  var loggedInCount: Int,
  var latLoggedIn: Instant,
)