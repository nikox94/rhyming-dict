package com.serene.kid.dict.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.serene.kid.dict.repositories.WordRepository;

@RestController
public class WordController {

	private final WordRepository wordRepository;

	@Autowired
	public WordController(final WordRepository wordRepository) {
		this.wordRepository = wordRepository;
	}

	@RequestMapping("/words")
	public String getAllWords() {
		return wordRepository.findAll().stream().map(s -> s.toString()).reduce("", (a, b) -> a + " ;; " + b);
	}
}
