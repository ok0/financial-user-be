package kr.co.ok0.jpa

import org.springframework.context.ApplicationContext
import org.springframework.data.domain.AuditorAware
import org.springframework.stereotype.Component
import java.util.*

@Component
class UserAuditorAware(val applicationContext: ApplicationContext): AuditorAware<String> {
  override fun getCurrentAuditor(): Optional<String> =
    Optional.ofNullable(this.applicationContext.id)
}