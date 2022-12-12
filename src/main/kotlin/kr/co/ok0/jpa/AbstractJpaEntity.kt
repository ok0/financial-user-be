package kr.co.ok0.jpa

import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant
import javax.persistence.Column
import javax.persistence.EntityListeners
import javax.persistence.MappedSuperclass

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class AbstractJpaEntity {
  @Column(name = "CREATED_AT", nullable = false, insertable = true, updatable = false)
  @CreatedDate
  lateinit var createdAt: Instant
    protected set

  @Column(name = "CREATED_BY", nullable = false, insertable = true, updatable = false)
  @CreatedBy
  lateinit var createdBy: String
    protected set

  @Column(name = "UPDATED_AT", nullable = false, insertable = true, updatable = true)
  @LastModifiedDate
  lateinit var updatedAt: Instant
    protected set

  @Column(name = "UPDATED_BY", nullable = false, insertable = true, updatable = true)
  @LastModifiedBy
  lateinit var updatedBy: String
    protected set

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as AbstractJpaEntity

    if (createdAt != other.createdAt) return false
    if (createdBy != other.createdBy) return false
    if (updatedAt != other.updatedAt) return false
    if (updatedBy != other.updatedBy) return false

    return true
  }

  override fun hashCode(): Int {
    var result = createdAt.hashCode()
    result = 31 * result + createdBy.hashCode()
    result = 31 * result + updatedAt.hashCode()
    result = 31 * result + updatedBy.hashCode()
    return result
  }

  override fun toString(): String {
    return "AbstractJpaEntity(createdAt=$createdAt, createdBy='$createdBy', updatedAt=$updatedAt, updatedBy='$updatedBy')"
  }
}