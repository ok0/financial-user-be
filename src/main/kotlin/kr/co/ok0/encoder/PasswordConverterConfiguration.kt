package kr.co.ok0.encoder

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("user.secret")
data class PasswordConverterConfiguration (
  var key: String,
  val cipherTransformation: String = "AES/ECB/PKCS5PADDING",
  val keyAlgorithm: String = "AES"
)