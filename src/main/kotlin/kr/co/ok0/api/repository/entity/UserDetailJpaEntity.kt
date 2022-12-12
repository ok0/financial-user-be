package kr.co.ok0.api.repository.entity

import kr.co.ok0.jpa.AbstractJpaEntity
import java.time.Instant
import javax.persistence.*

@Entity
@Table(name = "USER_DETAIL")
class UserDetailJpaEntity(
  @Id
  val userNo: Long,

  @MapsId
  @OneToOne(optional = false)
  @JoinColumn(name = "USER_NO")
  var user: UserJpaEntity,

  @Column(name = "LOGGEDIN_COUNT", nullable = false)
  var loggedInCount: Int,

  @Column(name = "LAST_LOGGEDIN", nullable = false)
  var latLoggedIn: Instant,
): AbstractJpaEntity() {

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false
    if (!super.equals(other)) return false

    other as UserDetailJpaEntity

    if (userNo != other.userNo) return false
    if (user != other.user) return false
    if (loggedInCount != other.loggedInCount) return false
    if (latLoggedIn != other.latLoggedIn) return false

    return true
  }

  override fun hashCode(): Int {
    var result = super.hashCode()
    result = 31 * result + userNo.hashCode()
    result = 31 * result + user.hashCode()
    result = 31 * result + loggedInCount
    result = 31 * result + latLoggedIn.hashCode()
    return result
  }

  override fun toString(): String {
    return "UserDetailJpaEntity(userNo=$userNo, user=$user, loggedInCount=$loggedInCount, latLoggedIn=$latLoggedIn)"
  }
}