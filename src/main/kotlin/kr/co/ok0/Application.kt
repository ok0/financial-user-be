package kr.co.ok0

import kr.co.ok0.api.repository.UserDetailRepository
import kr.co.ok0.api.repository.UserRepository
import kr.co.ok0.api.repository.entity.UserDetailJpaEntity
import kr.co.ok0.api.repository.entity.UserJpaEntity
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.event.EventListener
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.stereotype.Component
import java.time.Instant
import javax.persistence.Column
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.OneToOne

@SpringBootApplication
@EnableJpaAuditing
@ComponentScan("kr.co.ok0")
class Application

fun main(args: Array<String>) {
  runApplication<Application>(*args)
}