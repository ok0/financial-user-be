package kr.co.ok0.api.repository

import kr.co.ok0.api.repository.entity.UserDetailJpaEntity
import org.springframework.data.repository.CrudRepository

interface UserDetailRepository: CrudRepository<UserDetailJpaEntity, Long> {
}