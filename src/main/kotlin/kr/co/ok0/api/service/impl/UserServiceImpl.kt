package kr.co.ok0.api.service.impl

import kr.co.ok0.api.repository.UserDetailRepository
import kr.co.ok0.api.repository.UserRepository
import kr.co.ok0.api.repository.entity.UserDetailJpaEntity
import kr.co.ok0.api.repository.entity.UserJpaEntity
import kr.co.ok0.api.service.UserService
import kr.co.ok0.api.service.dto.*
import java.time.Instant

class UserServiceImpl(
  private val userRepository: UserRepository,
  private val userDetailRepository: UserDetailRepository
): UserService {
  override fun save(paramS: UserParamS): UserResultS {
    return when {
      (isExistsUserId(paramS.userId).result != UserIdCheckResultSType.SUCCESS) -> {
        UserResultS(
          result = UserResultSType.EXISTS_ID,
          user = null
        )
      }

      (isExistsUserNickName(paramS.userNickName).result != UserNickNameCheckResultSType.SUCCESS) -> {
        UserResultS(
          result = UserResultSType.EXISTS_NICKNAME,
          user = null
        )
      }

      (this.isMatchedPasswordPattern(paramS.password).result != UserPasswordCheckResultSType.SUCCESS)-> {
        UserResultS(
          result = UserResultSType.PASSWORD_ERROR,
          user = null
        )
      }

      else -> {
        userDetailRepository.save(
          UserDetailJpaEntity(
            userNo = 0,
            user = UserJpaEntity(
              userNo = 0,
              userId = paramS.userId,
              userPassword = paramS.password,
              userName = paramS.userName,
              userNickName = paramS.userNickName
            ),
            loggedInCount = 0,
            latLoggedIn = Instant.now()
          )
        ).toS()
      }
    }
  }

  override fun isExistsUserId(id: String): UserIdCheckResultS {
    return UserIdCheckResultS(
      result = userRepository.findByUserId(id)?.let {
        UserIdCheckResultSType.EXISTS
      } ?: UserIdCheckResultSType.SUCCESS
    )
  }

  override fun isExistsUserNickName(nickName: String): UserNickNameCheckResultS {
    return UserNickNameCheckResultS(
      result = userRepository.findByUserNickName(nickName)?.let {
        UserNickNameCheckResultSType.EXISTS
      } ?: UserNickNameCheckResultSType.SUCCESS
    )
  }

  override fun isMatchedPasswordPattern(password: String): UserPasswordCheckResultS {
    return UserPasswordCheckResultS(
      result = when {
        password.length > 50 -> UserPasswordCheckResultSType.LENGTH_ERROR
        !"[A-Z0-9]+".toRegex().matches(password) -> UserPasswordCheckResultSType.PATTERN_ERROR
        else -> UserPasswordCheckResultSType.SUCCESS
      }
    )
  }

  override fun isMatchedPasswordWhenLogin(id: String, password: String): Boolean
    = userRepository.findByUserId(id)?.userPassword == password

  private fun UserDetailJpaEntity.toS() = UserResultS(
    result = UserResultSType.SUCCESS,
    user = UserS(
      userNo = this.userNo,
      userId = this.user.userId,
      userName = this.user.userName,
      userNickName = this.user.userNickName,
      loggedInCount = this.loggedInCount,
      latLoggedIn = this.latLoggedIn
    )
  )
}