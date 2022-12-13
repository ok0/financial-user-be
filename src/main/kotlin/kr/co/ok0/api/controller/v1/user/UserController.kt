package kr.co.ok0.api.controller.v1.user

import kr.co.ok0.api.controller.v1.dto.*
import kr.co.ok0.api.service.UserService
import kr.co.ok0.api.service.dto.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/user")
class UserController(
  private val userService: UserService
) {
  @PostMapping("/{user-id}/join")
  fun postJoin(
    @PathVariable("user-id") userId: String,
    @RequestBody reqI: UserReqI
  ) = userService.save(reqI.toS(userId)).toI().let {
    when (it.result) {
      UserResultIType.SUCCESS -> ResponseEntity.ok(it)
      else -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(it)
    }
  }

  @PostMapping("/{user-id}/login")
  fun postLogin(
    @PathVariable("user-id") userId: String,
    @RequestBody reqI: UserLoginReqI
  ) = userService.getLogin(userId, reqI.toS()).toI().let {
    when (it.result) {
      UserLoginResultIType.SUCCESS -> ResponseEntity.ok(it)
      else -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(it)
    }
  }

  private fun UserReqI.toS(userId: String) = UserParamS(
    userId = userId,
    password = this.password,
    userName = this.userName,
    userNickName = this.userNickName
  )

  private fun UserResultS.toI() = UserResI(
    result = when (this.result) {
      UserResultSType.SUCCESS -> UserResultIType.SUCCESS
      UserResultSType.UNKNOWN_ERROR -> UserResultIType.UNKNOWN_ERROR
      UserResultSType.EXISTS_ID -> UserResultIType.EXISTS_ID
      UserResultSType.EXISTS_NICKNAME -> UserResultIType.EXISTS_NICKNAME
      UserResultSType.PASSWORD_ERROR -> UserResultIType.PASSWORD_ERROR
    },
    user = this.user?.let {
      UserI(
        userNo = it.userNo,
        userId = it.userId,
        userName = it.userName,
        userNickName = it.userNickName,
        loggedInCount = it.loggedInCount,
        latLoggedIn = it.latLoggedIn
      )
    }
  )

  private fun UserLoginReqI.toS() = UserLoginParamS(
    password = this.password
  )

  private fun UserLoginResultS.toI() = UserLoginResI(
    result = when (this.result) {
      UserLoginResultSType.SUCCESS -> UserLoginResultIType.SUCCESS
      UserLoginResultSType.NOT_FOUND_ID -> UserLoginResultIType.NOT_FOUND_ID
      UserLoginResultSType.PASSWORD_NOT_MATCHED -> UserLoginResultIType.PASSWORD_NOT_MATCHED
    }
  )
}