package com.vepeter.example.log4j2spring.web.rest;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.vepeter.example.log4j2spring.logger.model.AuditEvent;

@Component
@Produces("application/json")
public class EventResource {

	private JdbcTemplate jdbcTemplate;

	@Autowired
	public EventResource(JdbcTemplate jdbcTemplate) {
		super();
		this.jdbcTemplate = jdbcTemplate;
	}

	@GET
	public List<AuditEvent> events() {
		return jdbcTemplate.query("select source, name, param, eventDate from audit_event",
				new RowMapper<AuditEvent>() {

					@Override
					public AuditEvent mapRow(ResultSet rs, int rowNum) throws SQLException {
						return new AuditEvent(rs.getString("source"), rs.getString("name"), rs.getString("param"),
								rs.getTimestamp("eventDate").toLocalDateTime());
					}

				});
	}

}
