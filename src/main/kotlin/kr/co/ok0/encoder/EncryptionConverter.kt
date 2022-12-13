package kr.co.ok0.encoder

interface EncryptionConverter {
  fun encode(text: String?): String?
  fun decode(text: String?): String?
}