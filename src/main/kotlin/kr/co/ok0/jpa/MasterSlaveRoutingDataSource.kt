package kr.co.ok0.jpa

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource
import org.springframework.transaction.support.TransactionSynchronizationManager

class MasterSlaveRoutingDataSource: AbstractRoutingDataSource() {
  override fun determineCurrentLookupKey(): Any =
    if (TransactionSynchronizationManager.isCurrentTransactionReadOnly()) "slave" else "master"
}