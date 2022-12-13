package kr.co.ok0.encoder

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@ConfigurationProperties("user.secret")
@Configuration
class PasswordConverterConfiguration (
  var key: String = "",
  val cipherTransformation: String = "AES/ECB/PKCS5PADDING",
  val keyAlgorithm: String = "AES"
)