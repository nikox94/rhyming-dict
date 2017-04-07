package com.serene.kid.dict.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
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

		final StringBuilder allWords = new StringBuilder();

		lightweightWordList.getWordList().stream().forEach(s -> allWords.append(" ;; " + s.getWordText()));

		return allWords.toString();
	}

	@RequestMapping("/word/{word}/chars/{nChars}")
	@ResponseBody
	public List<String> getNMatchingChars(@PathVariable String word, @PathVariable byte nChars) {
		if (word.length() < nChars)
			throw new IllegalArgumentException("Search word is smaller then number of matched chars");
		return lightweightWordList.getWordList()
				.stream()
				.filter(s -> s.length() >= nChars)
				.filter(s -> s.isEqualSuffix(word, nChars))
				.map(w -> w.getWordText())
				.collect(Collectors.toList());
	}
}
