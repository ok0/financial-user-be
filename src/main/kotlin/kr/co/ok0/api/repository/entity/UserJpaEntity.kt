package kr.co.ok0.api.repository.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import kr.co.ok0.jpa.AbstractJpaEntity
import javax.persistence.*

@Entity
@Table(name = "USER")
class UserJpaEntity(
  @Id
  @Column(name = "USER_NO", nullable = false)
  var userNo: Long,

  @MapsId
  @OneToOne(optional = false)
  @JoinColumn(name = "USER_NO")
  var userDetail: UserDetailJpaEntity,

  @Column(name = "USER_ID", nullable = false)
  var userId: String,

  @Column(name = "USER_PASSWORD", nullable = false)
  @JsonIgnore
  var userPassword: String,

  @Column(name = "USER_NAME", nullable = false)
  @Convert(converter = UserJpaEntityEncryptionConverter::class)
  var userName: String,

  @Column(name = "USER_NICKNAME", nullable = false)
  var userNickName: String,
): AbstractJpaEntity() {


  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false
    if (!super.equals(other)) return false

    other as UserJpaEntity

    if (userNo != other.userNo) return false
    if (userDetail != other.userDetail) return false
    if (userId != other.userId) return false
    if (userPassword != other.userPassword) return false
    if (userName != other.userName) return false
    if (userNickName != other.userNickName) return false

    return true
  }

  override fun hashCode(): Int {
    var result = super.hashCode()
    result = 31 * result + userNo.hashCode()
    result = 31 * result + userDetail.hashCode()
    result = 31 * result + userId.hashCode()
    result = 31 * result + userPassword.hashCode()
    result = 31 * result + userName.hashCode()
    result = 31 * result + userNickName.hashCode()
    return result
  }

  override fun toString(): String {
    return "UserJpaEntity(userNo=$userNo, userDetail=$userDetail, userId='$userId', userPassword='$userPassword', userName='$userName', userNickName='$userNickName')"
  }
}