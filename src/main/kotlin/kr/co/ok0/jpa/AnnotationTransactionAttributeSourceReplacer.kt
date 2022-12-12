package kr.co.ok0.jpa

import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor
import org.springframework.core.PriorityOrdered
import org.springframework.stereotype.Component
import org.springframework.transaction.interceptor.TransactionAttributeSource

@Component
class AnnotationTransactionAttributeSourceReplacer:
  InstantiationAwareBeanPostProcessor, PriorityOrdered {
  override fun postProcessBeforeInstantiation(beanClass: Class<*>, beanName: String): Any? {
    return if (beanName == "transactionAttributeSource"
      && TransactionAttributeSource::class.java.isAssignableFrom(beanClass)) {
      UserAnnotationTransactionAttributeSource()
    } else null
  }

  override fun getOrder() = 0
}