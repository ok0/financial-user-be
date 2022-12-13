package kr.co.ok0.api.service.impl

import kr.co.ok0.Log
import kr.co.ok0.api.repository.UserDetailRepository
import kr.co.ok0.api.repository.UserRepository
import kr.co.ok0.api.repository.entity.UserDetailJpaEntity
import kr.co.ok0.api.repository.entity.UserJpaEntity
import kr.co.ok0.api.service.UserService
import kr.co.ok0.api.service.dto.*
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class UserServiceImpl(
  private val userRepository: UserRepository,
  private val userDetailRepository: UserDetailRepository,
  private val passwordEncoder: PasswordEncoder
) :Log, UserService {
  override fun isMatchedPasswordPattern(password: String): UserPasswordCheckResultS {
    return UserPasswordCheckResultS(
      result = when {
        password.length > 50 -> {
          logger.error("Password Length Error, ${password.length}")
          UserPasswordCheckResultSType.LENGTH_ERROR
        }
        password.length < 5 -> {
          logger.error("Password Length Error, ${password.length}")
          UserPasswordCheckResultSType.LENGTH_ERROR
        }

        !"[a-zA-Z0-9!@#$%^&*()_+\\-=]+".toRegex().matches(password) -> {
          logger.error("Password Pattern Error, ${"[a-zA-Z0-9!@#$%^&*()_+\\-=]+".toRegex().matches(password)}")
          UserPasswordCheckResultSType.PATTERN_ERROR
        }
        else -> {
          UserPasswordCheckResultSType.SUCCESS
        }
      }
    )
  }

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

      (this.isMatchedPasswordPattern(paramS.password).result != UserPasswordCheckResultSType.SUCCESS) -> {
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
              userPassword = passwordEncoder.encode(paramS.password),
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

  override fun isMatchedPasswordWhenLogin(id: String, password: String): Boolean =
    userRepository.findByUserId(id)?.userPassword == password

  override fun getLogin(id: String, paramS: UserLoginParamS): UserLoginResultS {
    return userRepository.findByUserId(id)?.let { user ->
      if (passwordEncoder.matches(paramS.password, user.userPassword)) {
        userDetailRepository.findByIdOrNull(user.userNo)?.let { userDetail ->
          userDetail.latLoggedIn = Instant.now()
          userDetail.loggedInCount++
          userDetailRepository.save(userDetail)
        }

        UserLoginResultS(
          result = UserLoginResultSType.SUCCESS
        )
      } else {
        UserLoginResultS(
          result = UserLoginResultSType.PASSWORD_NOT_MATCHED
        )
      }
    } ?: UserLoginResultS(
      result = UserLoginResultSType.NOT_FOUND_ID
    )
  }

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