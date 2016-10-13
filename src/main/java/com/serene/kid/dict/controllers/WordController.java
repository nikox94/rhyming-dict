package com.serene.kid.dict.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.serene.kid.dict.repositories.WordRepository;

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
		return wordRepository.findAll().stream().map(s -> s.toString()).reduce("", (a, b) -> a + " ;; " + b);
	}
}
