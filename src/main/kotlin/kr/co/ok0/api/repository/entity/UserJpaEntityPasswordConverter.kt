package kr.co.ok0.api.repository.entity

import kr.co.ok0.encoder.PasswordConverter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.persistence.AttributeConverter
import javax.persistence.Converter

@Converter
@Component
class UserJpaEntityPasswordConverter: AttributeConverter<String?, String?> {
  companion object {
    lateinit var converter: PasswordConverter
  }

  @Autowired
  fun setPasswordConverter(passwordConverter: PasswordConverter) {
    converter = passwordConverter
  }

  override fun convertToDatabaseColumn(attribute: String?): String? {
    return converter.encode(attribute)
  }

  override fun convertToEntityAttribute(dbData: String?): String? {
    return converter.decode(dbData)
  }
}