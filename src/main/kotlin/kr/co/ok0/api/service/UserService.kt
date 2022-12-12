package kr.co.ok0.api.service

import kr.co.ok0.api.service.dto.*

interface UserService {
  fun save(paramS: UserParamS): UserResultS
  fun isExistsUserId(id: String): UserIdCheckResultS
  fun isExistsUserNickName(nickName: String): UserNickNameCheckResultS
}