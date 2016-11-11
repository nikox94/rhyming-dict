package com.serene.kid.dict;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class DictApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void contextLoads() {
	}

	@Test
	public void checkIfCorrectlyParsedWords() {

		final String body = this.restTemplate.getForObject("/words", String.class);
		//assertThat(body).contains(" демодулирам ");
		assertThat(body).contains(" демодулира ");
		assertThat(body).contains(" демодулирахме ");
		assertThat(body).contains(" демодулиращ ");
		assertThat(body).contains(" демодулиращият ");
		assertThat(body).doesNotContain(" демодулираейки ");
		assertThat(body).doesNotContain(" демодулираим ");

		//assertThat(body).contains(" демон ");
		//assertThat(body).contains(" демона ");
		//assertThat(body).contains(" демони ");
		//assertThat(body).contains(" демоните ");
		//assertThat(body).contains(" демонът ");


		//assertThat(body).contains(" ящен ");
		assertThat(body).contains(" ящна ");
		assertThat(body).doesNotContain(" ящеите ");

		assertThat(body).contains(" Абърдийн ");
		assertThat(body).doesNotContain("78233");
	}
}
