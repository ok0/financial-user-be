package kr.co.ok0.api.service

import kr.co.ok0.api.service.dto.*

interface UserService {
  fun save(paramS: UserParamS): UserResultS
  fun getUserByUserId(id: String): UserResultS
  fun getLogin(id:String, paramS: UserLoginParamS): UserLoginResultS
  fun isExistsUserId(id: String): UserIdCheckResultS
  fun isExistsUserNickName(nickName: String): UserNickNameCheckResultS
  fun isMatchedPasswordPattern(password: String): UserPasswordCheckResultS
}