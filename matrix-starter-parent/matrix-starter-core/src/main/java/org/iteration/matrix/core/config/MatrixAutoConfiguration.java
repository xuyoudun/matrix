package org.iteration.matrix.core.config;

import org.iteration.matrix.core.cqrs.CqrsPersistenceJpaSupport;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan("org.iteration.matrix.core")
@EnableJpaRepositories(basePackages = {"**.adapter.repository.persistence"}, repositoryBaseClass = CqrsPersistenceJpaSupport.class)
public class MatrixAutoConfiguration {

}
