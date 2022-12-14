package kr.co.ok0.api.repository

import kr.co.ok0.api.repository.entity.UserJpaEntity
import org.springframework.data.repository.CrudRepository

interface UserRepository: CrudRepository<UserJpaEntity, Long> {
  fun findByUserIdOrNull(id: String): UserJpaEntity?
  fun findByUserNickNameOrNull(nickname: String): UserJpaEntity?
}