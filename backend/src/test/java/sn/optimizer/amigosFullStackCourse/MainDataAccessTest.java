package sn.optimizer.amigosFullStackCourse;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;

@Testcontainers
public abstract class MainDataAccessTest {

    @Container
    protected static final PostgreSQLContainer<?> psqlContainer=new PostgreSQLContainer<>("postgres")
            .withDatabaseName("customer-database-test")
            .withUsername("test-username")
            .withPassword("test-password");

    @BeforeAll
    protected static void flywayDoMigration(){
        Flyway flyway=Flyway.configure()
                .dataSource(getDataSource())
                .load();
        flyway.migrate();
    }

    protected static DataSource getDataSource(){
        return DataSourceBuilder
                .create()
                .driverClassName(psqlContainer.getDriverClassName())
                .url(psqlContainer.getJdbcUrl())
                .username(psqlContainer.getUsername())
                .password(psqlContainer.getPassword())
                .build();
    }

    @DynamicPropertySource
    protected static void dynamicSourceProperty(DynamicPropertyRegistry registry){
        registry.add("spring.datasource.url", psqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", psqlContainer::getUsername);
        registry.add("spring.datasource.password", psqlContainer::getPassword);
    }
}
