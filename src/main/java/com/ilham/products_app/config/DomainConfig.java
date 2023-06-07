package com.ilham.products_app.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
@EntityScan("com.ilham.products_app")
@EnableJpaRepositories("com.ilham.products_app")
@EnableTransactionManagement
public class DomainConfig {
}
