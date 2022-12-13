package kr.co.ok0.api.service

import kr.co.ok0.api.service.dto.*

interface UserService {
  fun getLogin(id:String, paramS: UserLoginParamS): UserLoginResultS
  fun save(paramS: UserParamS): UserResultS
  fun isExistsUserId(id: String): UserIdCheckResultS
  fun isExistsUserNickName(nickName: String): UserNickNameCheckResultS
  fun isMatchedPasswordPattern(password: String): UserPasswordCheckResultS
  fun isMatchedPasswordWhenLogin(id: String, password: String): Boolean
}