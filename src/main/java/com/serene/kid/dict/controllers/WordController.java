package com.serene.kid.dict.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.serene.kid.dict.repositories.WordRepository;

import redis.clients.jedis.Jedis;

@Controller
public class WordController {

	private final WordRepository wordRepository;

	@Autowired
	public WordController(final WordRepository wordRepository) {
		this.wordRepository = wordRepository;
	}

	@RequestMapping("/")
	public String getMainPage() {
		return "index";
	}

	@RequestMapping("/words")
	@ResponseBody
	public String getAllWords() {
		final Jedis jedis = new Jedis("localhost");

		long startTimeGet = System.nanoTime();
		jedis.keys("*");
		long endTimeGet = System.nanoTime();

		long startTimeStream = System.nanoTime();
		final String redisRet = jedis.keys("*").stream().map(s -> s.toString()).reduce("", (a, b) -> a + " ;; " + b);
		long endTimeStream = System.nanoTime();

		final long redisDurationGet = (endTimeGet - startTimeGet);  //divide by 1000000 to get milliseconds.
		final long redisDurationStream = (endTimeStream - startTimeStream);  //divide by 1000000 to get milliseconds.


		startTimeGet = System.nanoTime();
		wordRepository.findAll();
		endTimeGet = System.nanoTime();

		startTimeStream = System.nanoTime();
		final String dbRet = wordRepository.findAll().stream().map(s -> s.getWordText()).reduce("", (a, b) -> a + " ;; " + b);
		endTimeStream = System.nanoTime();

		final long dbDurationGet = (endTimeGet - startTimeGet);  //divide by 1000000 to get milliseconds.
		final long dbDurationStream = (endTimeStream - startTimeStream);  //divide by 1000000 to get milliseconds.


		System.out.println("Pure Redis get took: " + redisDurationGet*1E-6);
		System.out.println("Redis get and Java text manipulation took: " + redisDurationStream*1E-6);

		System.out.println("Pure Postgres get took: " + dbDurationGet*1E-6);
		System.out.println("Postgres get and Java text manipulation took: " + dbDurationStream*1E-6);

		jedis.close();

		return dbRet;

	}
}
