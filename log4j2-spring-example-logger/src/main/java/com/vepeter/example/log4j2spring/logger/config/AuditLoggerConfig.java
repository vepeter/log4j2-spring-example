package com.vepeter.example.log4j2spring.logger.config;

import static java.lang.Boolean.FALSE;

import java.sql.SQLException;
import java.sql.Timestamp;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.db.ColumnMapping;
import org.apache.logging.log4j.core.appender.db.jdbc.JdbcAppender;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.vepeter.example.log4j2spring.logger.model.AuditDataSourceConnectionSource;

@org.springframework.context.annotation.Configuration
@PropertySource("classpath:logger-config.properties")
public class AuditLoggerConfig {

    @Autowired
    private DataSource dataSource;

    @Value("${example.appender.name}")
    private String appenderName;

    @Value("${example.appender.bufferSize:0}")
    private Integer appenderBufferSize;

    @Value("${example.appender.tableName}")
    private String appendertableName;

    @PostConstruct
    public void initLogger() {
        final LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        final Configuration config = ctx.getConfiguration();

		ColumnMapping sourceColumnMapper = ColumnMapping.newBuilder().setConfiguration(config).setName("source")
				.setPattern("%c{1}").build();
		ColumnMapping nameColumnMapper = ColumnMapping.newBuilder().setConfiguration(config).setName("name")
				.setPattern("%message").build();
		ColumnMapping paramColumnMapper = ColumnMapping.newBuilder().setConfiguration(config).setName("param")
				.setPattern("%mdc{param}").build();
		ColumnMapping timestampColumnMapper = ColumnMapping.newBuilder().setConfiguration(config).setName("eventDate")
				.setType(Timestamp.class).build();
		Appender appender = JdbcAppender.newBuilder()
				.setBufferSize(appenderBufferSize)
				.setColumnMappings(sourceColumnMapper, nameColumnMapper, paramColumnMapper, timestampColumnMapper)
				.setConnectionSource(new AuditDataSourceConnectionSource(dataSource))
				.setTableName(appendertableName)
				.setName(appenderName)
				.setIgnoreExceptions(true)
				.setFilter(null)
				.build();
        appender.start();
        config.addAppender(appender);
        AppenderRef ref = AppenderRef.createAppenderRef(appenderName, null, null);
        AppenderRef[] appenderRefs = new AppenderRef[] { ref };
        LoggerConfig loggerConfig = LoggerConfig.createLogger(true, Level.INFO,
                "com.vepeter.example.log4j2spring.logger.audit", FALSE.toString(), appenderRefs, null, config, null);
        loggerConfig.addAppender(appender, null, null);
        config.addLogger("com.vepeter.example.log4j2spring.logger.audit", loggerConfig);
        ctx.updateLoggers();
    }

    @PostConstruct
    public void initDatabase() throws SQLException {
		jdbcTemplate().execute(String.format(
				"CREATE TABLE %s("
				+ "id INTEGER IDENTITY PRIMARY KEY, "
				+ "source VARCHAR(120) NOT NULL, "
				+ "name VARCHAR(120) NOT NULL, "
				+ "param VARCHAR(120) NOT NULL, "
				+ "eventDate TIMESTAMP NOT NULL)",
				appendertableName));
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
