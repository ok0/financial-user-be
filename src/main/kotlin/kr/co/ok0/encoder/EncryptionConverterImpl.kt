package kr.co.ok0.encoder

import org.springframework.stereotype.Service
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

@Service
class EncryptionConverterImpl (
  private val configuration: EncryptionConverterConfiguration
): EncryptionConverter {
  private var cipherEncoder: Cipher = Cipher.getInstance(configuration.cipherTransformation)
  private var base64Encoder: Base64.Encoder = Base64.getMimeEncoder()

  private var cipherDecoder: Cipher = Cipher.getInstance(configuration.cipherTransformation)
  private var base64Decoder: Base64.Decoder = Base64.getMimeDecoder()

  init {
    val keyBytes = ByteArray(16)
    val oldKeyBytes = configuration.key.toByteArray(Charsets.UTF_8)
    oldKeyBytes.copyInto(keyBytes)
    val keySpec = SecretKeySpec(keyBytes, configuration.keyAlgorithm)
    cipherEncoder.init(Cipher.ENCRYPT_MODE, keySpec)
    cipherDecoder.init(Cipher.DECRYPT_MODE, keySpec)
  }

  override fun encode(text: String?): String?
    = base64Encoder.encodeToString(cipherEncoder.doFinal(text?.toByteArray(Charsets.UTF_8)))

  override fun decode(text: String?): String?
    = cipherDecoder.doFinal(base64Decoder.decode(text?.toByteArray(Charsets.UTF_8)))?.toString(Charsets.UTF_8)
}