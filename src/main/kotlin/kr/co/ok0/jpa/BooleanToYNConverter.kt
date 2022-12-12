package kr.co.ok0.jpa

import javax.persistence.AttributeConverter
import javax.persistence.Converter

@Converter
class BooleanToYNConverter: AttributeConverter<Boolean, String> {
  override fun convertToDatabaseColumn(attribute: Boolean?): String {
    return if (attribute != null && attribute) "Y" else "N"
  }

  override fun convertToEntityAttribute(dbData: String?): Boolean {
    return "Y" == dbData
  }
}