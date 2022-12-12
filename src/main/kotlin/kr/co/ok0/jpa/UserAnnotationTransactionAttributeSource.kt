package kr.co.ok0.jpa

import org.springframework.transaction.annotation.AnnotationTransactionAttributeSource
import org.springframework.transaction.interceptor.DefaultTransactionAttribute
import org.springframework.transaction.interceptor.TransactionAttribute
import java.lang.reflect.Method

class UserAnnotationTransactionAttributeSource: AnnotationTransactionAttributeSource() {
  override fun computeTransactionAttribute(method: Method, targetClass: Class<*>?): TransactionAttribute? {
    return super.computeTransactionAttribute(method, targetClass)?.let {
      if (it is DefaultTransactionAttribute
        && targetClass?.name?.endsWith("ServiceImpl") == true
        && it.qualifier?.isEmpty() == true
      ) {
        if (targetClass.packageName.startsWith("kr.co.ok0.data.rdb")) {
          it.qualifier = "userTransactionManager"
        }
      }

      return it
    }
  }
}