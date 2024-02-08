package com.assignment.caffe.persistence.config

import io.kotest.core.annotation.AutoScan
import io.kotest.core.listeners.AfterProjectListener
import io.kotest.core.listeners.BeforeProjectListener
import org.testcontainers.containers.MySQLContainer

/**
 * Kotest Extension 기반 Test 전용 Mysql Container 실행
 *
 * 참고: [Kotest > Extensions](https://kotest.io/docs/framework/extensions/extensions-introduction.html)
 */
@AutoScan
object DbExtension : BeforeProjectListener, AfterProjectListener {
    private val mysql = MySQLContainer("mysql:5.7.34")
        .withDatabaseName("caffe")
        .withInitScript("db/init/init.sql")

    override suspend fun beforeProject() {
        mysql.start()
        System.setProperty("spring.datasource.url", mysql.jdbcUrl)
        System.setProperty("spring.datasource.username", mysql.username)
        System.setProperty("spring.datasource.password", mysql.password)
    }
}