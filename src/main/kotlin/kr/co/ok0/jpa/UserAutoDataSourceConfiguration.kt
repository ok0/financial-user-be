package kr.co.ok0.jpa

import com.zaxxer.hikari.HikariDataSource
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import org.springframework.transaction.annotation.EnableTransactionManagement
import javax.sql.DataSource

@ConditionalOnProperty(prefix = "datasource.user.slave", name = ["jdbc-url"])
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
  basePackages = ["kr.co.ok0.api.repository"],
  entityManagerFactoryRef = "userEntityManagerFactory",
  transactionManagerRef = "userTransactionManager"
)
class UserAutoDataSourceConfiguration(
  private val japProperties: JpaProperties
) {
  @Bean
  @ConfigurationProperties(prefix = "datasource.user.slave")
  fun userSlaveDataSource(): DataSource {
    return DataSourceBuilder
      .create()
      .type(HikariDataSource::class.java)
      .build()
  }

  @Bean
  @ConfigurationProperties(prefix = "datasource.user.master")
  fun userMasterDataSource(): DataSource {
    return DataSourceBuilder
      .create()
      .type(HikariDataSource::class.java)
      .build()
  }

  @Bean
  fun userRoutingDataSource() = MasterSlaveRoutingDataSource().apply {
    setTargetDataSources(
      hashMapOf<Any, Any>(
        "master" to userMasterDataSource(),
        "slave" to userSlaveDataSource()
      )
    )
    setDefaultTargetDataSource(userMasterDataSource())
  }

  @Bean
  fun userLazyDataSource() = LazyConnectionDataSourceProxy(userRoutingDataSource())

  @Bean("userEntityManagerFactory")
  fun userEntityManagerFactory(): LocalContainerEntityManagerFactoryBean {
    return LocalContainerEntityManagerFactoryBean().apply {
      dataSource = userLazyDataSource()
      setPackagesToScan("kr.co.ok0.api.repository.entity")
      jpaVendorAdapter = HibernateJpaVendorAdapter().apply {
        setShowSql(japProperties.isShowSql)
        setGenerateDdl(japProperties.isGenerateDdl)
        setJpaPropertyMap(japProperties.properties)
      }
      afterPropertiesSet()
    }
  }

  @Bean
  fun userTransactionManager(): JpaTransactionManager {
    return JpaTransactionManager(userEntityManagerFactory().`object`!!)
  }
}