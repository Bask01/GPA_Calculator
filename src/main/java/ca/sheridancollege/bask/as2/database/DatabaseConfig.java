package ca.sheridancollege.bask.as2.database;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;


/**
 * Models a Database configuration class.
 * 
 * @author Kubra Bas
 *
 */

@Configuration
public class DatabaseConfig {
	

	/**
	 * Bean method will be stored in the IOCC
	 * @param dataSource object will be injected to the jdbc template
	 * 
	 */
	@Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }
	
}
