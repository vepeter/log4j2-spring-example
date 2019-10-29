package com.vepeter.example.log4j2spring.logger.model;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.logging.log4j.core.appender.db.jdbc.AbstractConnectionSource;

public class AuditDataSourceConnectionSource extends AbstractConnectionSource {
    
    private DataSource dataSource;

    public AuditDataSourceConnectionSource(DataSource dataSource) {
        super();
        this.dataSource = dataSource;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

}
