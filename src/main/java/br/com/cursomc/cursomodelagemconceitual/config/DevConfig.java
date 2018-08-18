package br.com.cursomc.cursomodelagemconceitual.config;

import br.com.cursomc.cursomodelagemconceitual.services.DBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.text.ParseException;

@Configuration
@Profile("dev")
public class DevConfig {

    @Autowired
    private DBService dbService;

    @Value("${spring.jpa.properties.hibernate.format_sql}")
    private String strategy;

    @Bean boolean instantiateDatabase() throws ParseException {

        if (!"create".equals(strategy)) {
            return false;
        }

        dbService.instantiateTestDatabase();
        return true;
    }

}
