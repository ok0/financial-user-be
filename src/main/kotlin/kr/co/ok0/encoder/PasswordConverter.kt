package kr.co.ok0.encoder

interface PasswordConverter {
  fun encode(text: String?): String?
  fun decode(text: String?): String?
}