package kr.co.ok0.api.repository.entity

import kr.co.ok0.encoder.PasswordConverter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.persistence.AttributeConverter
import javax.persistence.Converter

@Converter
class UserJpaEntityPasswordConverter: AttributeConverter<String?, String?> {
  @Autowired
  private lateinit var passwordConverter: PasswordConverter

  override fun convertToDatabaseColumn(attribute: String?): String? {
    return this.passwordConverter.encode(attribute)
  }

  override fun convertToEntityAttribute(dbData: String?): String? {
    return this.passwordConverter.decode(dbData)
  }
}