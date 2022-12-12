package kr.co.ok0.api.service.exception

import java.lang.RuntimeException

abstract class BusinessLogicException: RuntimeException {
  constructor() : super()
  constructor(message: String?) : super(message)
}