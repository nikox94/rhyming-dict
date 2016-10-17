package com.serene.kid.dict.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.serene.kid.dict.annotations.UsesJedis;

import redis.clients.jedis.Jedis;

@Controller
public class WordController {

	@RequestMapping("/")
	public String getMainPage() {
		return "index";
	}

	@RequestMapping("/words")
	@ResponseBody
	@UsesJedis
	public String getAllWords() {
		final Jedis jedis = new Jedis("localhost");

		final StringBuilder redisRet = new StringBuilder();

		jedis.smembers("words").stream().forEach(s -> redisRet.append(" ;; " + s));

		jedis.close();

		return redisRet.toString();

	}
}
