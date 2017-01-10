package com.vepeter.example.log4j2spring.logger.config;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

import java.sql.SQLException;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.db.jdbc.ColumnConfig;
import org.apache.logging.log4j.core.appender.db.jdbc.JdbcAppender;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.vepeter.example.log4j2spring.logger.model.DataSourceConnectionSource;

@org.springframework.context.annotation.Configuration
public class AuditLoggerConfig {

    @Autowired
    private DataSource dataSource;

    @PostConstruct
    public void initLogger() {
        final LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        final Configuration config = ctx.getConfiguration();

        ColumnConfig nameColumn = ColumnConfig.createColumnConfig(config, "name", "%message", "", FALSE.toString(),
                FALSE.toString(), FALSE.toString());
        ColumnConfig timestampColumn = ColumnConfig.createColumnConfig(config, "eventDate", "", "", TRUE.toString(),
                TRUE.toString(), FALSE.toString());
        ColumnConfig[] columnConfigs = new ColumnConfig[] { nameColumn, timestampColumn };
        Appender appender = JdbcAppender.createAppender("auditJdbcAppender", TRUE.toString(), null,
                new DataSourceConnectionSource(dataSource), "0", "audit_event", columnConfigs);
        appender.start();
        config.addAppender(appender);
        AppenderRef ref = AppenderRef.createAppenderRef("auditJdbcAppender", null, null);
        AppenderRef[] appenderRefs = new AppenderRef[] { ref };
        LoggerConfig loggerConfig = LoggerConfig.createLogger(false, Level.INFO,
                "com.vepeter.example.log4j2spring.logger.audit", FALSE.toString(), appenderRefs, null, config, null);
        loggerConfig.addAppender(appender, null, null);
        config.addLogger("com.vepeter.example.log4j2spring.logger.audit", loggerConfig);
        ctx.updateLoggers();
    }

    @PostConstruct
    public void initDatabase() throws SQLException {
        jdbcTemplate().execute(
                "CREATE TABLE audit_event(id INTEGER IDENTITY PRIMARY KEY, name VARCHAR(120) NOT NULL, eventDate TIMESTAMP NOT NULL)");
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.hsqldb.jdbc.JDBCDriver");
        dataSource.setUrl("jdbc:hsqldb:mem:log4j2-spring");
        dataSource.setUsername("sa");
        dataSource.setPassword("");
        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }

}