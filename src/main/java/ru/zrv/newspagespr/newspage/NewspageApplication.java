package ru.zrv.newspagespr.newspage;

import lombok.SneakyThrows;
import org.flywaydb.core.Flyway;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NewspageApplication {

	@SneakyThrows
	public static void main(String[] args) {


		Flyway flyway = Flyway.
				configure().
				dataSource(
						"jdbc:postgresql://localhost:5432/newspagespr?useUnicode=true&serverTimezone=UTC",
						"postgres",
						"root"
				).load();

		flyway.migrate();

		SpringApplication.run(NewspageApplication.class, args);
	}
}
