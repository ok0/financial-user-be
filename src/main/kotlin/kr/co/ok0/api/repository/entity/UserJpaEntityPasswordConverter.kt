package kr.co.ok0.api.repository.entity

import kr.co.ok0.encoder.PasswordConverter
import org.springframework.stereotype.Component
import javax.persistence.AttributeConverter
import javax.persistence.Converter

@Converter
@Component
class UserJpaEntityPasswordConverter(
  private val passwordConverter: PasswordConverter
): AttributeConverter<String?, String?> {
  override fun convertToDatabaseColumn(attribute: String?): String? {
    return passwordConverter.encode(attribute)
  }

  override fun convertToEntityAttribute(dbData: String?): String? {
    return passwordConverter.decode(dbData)
  }
}