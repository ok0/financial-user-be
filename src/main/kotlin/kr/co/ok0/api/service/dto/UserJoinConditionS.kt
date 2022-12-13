package kr.co.ok0.api.service.dto

data class UserIdCheckResultS(
  var result: UserIdCheckResultSType
)
enum class UserIdCheckResultSType {
  SUCCESS, EXISTS
}

data class UserNickNameCheckResultS(
  var result: UserNickNameCheckResultSType
)
enum class UserNickNameCheckResultSType {
  SUCCESS, EXISTS
}

data class UserPasswordCheckResultS(
  var result: UserPasswordCheckResultSType
)
enum class UserPasswordCheckResultSType {
  SUCCESS, LENGTH_ERROR, PATTERN_ERROR
}