package com.serene.kid.dict.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.serene.kid.dict.LightweightWordList;
@Controller
public class WordController {

	private final LightweightWordList lightweightWordList;

	public WordController(final LightweightWordList lightweightWordList) {
		this.lightweightWordList = lightweightWordList;
	}

	@RequestMapping("/")
	public String getMainPage() {
		return "index";
	}

	@RequestMapping("/words")
	@ResponseBody
	public String getAllWords() {

		final StringBuilder redisRet = new StringBuilder();

		lightweightWordList.getWordlist().stream().forEach(s -> redisRet.append(" ;; " + s.getWordText()));

		return redisRet.toString();

	}
}
