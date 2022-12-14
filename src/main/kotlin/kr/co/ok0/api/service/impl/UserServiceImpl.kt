package kr.co.ok0.api.service.impl

import kr.co.ok0.Log
import kr.co.ok0.api.repository.UserDetailRepository
import kr.co.ok0.api.repository.UserRepository
import kr.co.ok0.api.repository.entity.UserDetailJpaEntity
import kr.co.ok0.api.repository.entity.UserJpaEntity
import kr.co.ok0.api.service.UserService
import kr.co.ok0.api.service.dto.*
import kr.co.ok0.api.service.exception.DataNotFoundExceptionWhenFindUser
import kr.co.ok0.api.service.exception.DataNotFoundExceptionWhenSaveUser
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
class UserServiceImpl(
  private val userRepository: UserRepository,
  private val userDetailRepository: UserDetailRepository,
  private val passwordEncoder: PasswordEncoder
) : Log, UserService {
  @Transactional(readOnly = false)
  override fun save(paramS: UserParamS): UserResultS {
    val user = userRepository.findByUserIdOrNull(paramS.userId)

    return when {
      (user == null && isExistsUserId(paramS.userId).result != UserIdCheckResultSType.SUCCESS) -> {
        UserResultS(
          result = UserResultSType.EXISTS_ID,
          user = null
        )
      }

      (user == null && isExistsUserNickName(paramS.userNickName).result != UserNickNameCheckResultSType.SUCCESS) -> {
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
        if (user == null) {
          val inserted = userRepository.save(
            UserJpaEntity(
              userNo = 0,
              userDetail = UserDetailJpaEntity(
                userNo = 0,
                loggedInCount = 0,
                latLoggedIn = Instant.now()
              ),
              userId = paramS.userId,
              userPassword = passwordEncoder.encode(paramS.password),
              userName = paramS.userName,
              userNickName = paramS.userNickName
            )
          )

          userRepository.findByUserIdOrNull(inserted.userId)?.toS()
            ?: throw DataNotFoundExceptionWhenSaveUser("User Not Found Error.")
        } else {
          user.userName = paramS.userName
          user.userNickName = paramS.userNickName
          user.userPassword = passwordEncoder.encode(paramS.password)
          userRepository.save(user).toS()
        }
      }
    }
  }

  @Transactional(readOnly = true)
  override fun getUserByUserId(id: String): UserResultS {
    return userRepository.findByUserIdOrNull(id)?.toS()
      ?: throw DataNotFoundExceptionWhenFindUser("Member Not Found Error")
  }

  @Transactional(readOnly = false)
  override fun getLogin(id: String, paramS: UserLoginParamS): UserLoginResultS {
    return userRepository.findByUserIdOrNull(id)?.let { user ->
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

  override fun isExistsUserId(id: String): UserIdCheckResultS {
    return UserIdCheckResultS(
      result = userRepository.findByUserIdOrNull(id)?.let {
        UserIdCheckResultSType.EXISTS
      } ?: UserIdCheckResultSType.SUCCESS
    )
  }

  override fun isExistsUserNickName(nickName: String): UserNickNameCheckResultS {
    return UserNickNameCheckResultS(
      result = userRepository.findByUserNickNameOrNull(nickName)?.let {
        UserNickNameCheckResultSType.EXISTS
      } ?: UserNickNameCheckResultSType.SUCCESS
    )
  }

  @Transactional(readOnly = true)
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

  private fun UserJpaEntity.toS() = UserResultS(
    result = UserResultSType.SUCCESS,
    user = UserS(
      userNo = this.userNo,
      userId = this.userId,
      userName = this.userName,
      userNickName = this.userNickName,
      loggedInCount = this.userDetail.loggedInCount,
      latLoggedIn = this.userDetail.latLoggedIn
    )
  )
}