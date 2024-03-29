package kr.co.ok0.api.repository

import kr.co.ok0.api.repository.entity.UserJpaEntity
import org.springframework.data.repository.CrudRepository

interface UserRepository: CrudRepository<UserJpaEntity, Long> {
  fun findByUserId(id: String): UserJpaEntity?
  fun findByUserNickName(nickname: String): UserJpaEntity?
}