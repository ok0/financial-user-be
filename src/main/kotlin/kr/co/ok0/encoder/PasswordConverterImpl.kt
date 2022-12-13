package kr.co.ok0.encoder

import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

class PasswordConverterImpl (
  private val configuration: PasswordConverterConfiguration
): PasswordConverter {
  private var cipherEncoder: Cipher = Cipher.getInstance(configuration.cipherTransformation)
  private var base64Encoder: Base64.Encoder = Base64.getMimeEncoder()

  private var cipherDecoder: Cipher = Cipher.getInstance(configuration.cipherTransformation)
  private var base64Decoder: Base64.Decoder = Base64.getMimeDecoder()

  init {
    val keyBytes = ByteArray(16)
    val oldKeyBytes = configuration.key.toByteArray(Charsets.UTF_8)
    oldKeyBytes.copyInto(keyBytes)
    cipherEncoder.init(Cipher.ENCRYPT_MODE, SecretKeySpec(keyBytes, configuration.keyAlgorithm))
  }

  override fun encode(text: String?): String?
    = base64Encoder.encodeToString(cipherEncoder.doFinal(text?.toByteArray(Charsets.UTF_8)))

  override fun decode(text: String?): String? {
    val decodeByte = cipherDecoder.doFinal(base64Decoder.decode(text?.toByteArray(Charsets.UTF_8)))
    return decodeByte?.toString(Charsets.UTF_8)
  }
}