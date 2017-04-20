package com.serene.kid.dict.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.serene.kid.dict.model.IWordCache;
import com.serene.kid.dict.model.LightweightWordList;
import com.serene.kid.dict.services.IWordService;

@Controller
public class WordController {

	private final IWordService wordService;
	private final IWordCache wordCache;

	@Autowired
	public WordController(final IWordService wordService) {
		this.wordService = wordService;
		wordCache = new LightweightWordList(wordService.getWords());
	}

	@RequestMapping("/")
	public String getMainPage() {
		return "index";
	}

	@RequestMapping("/words")
	@ResponseBody
	public List<String> getAllWords() {

		return wordCache.getWordList().stream().map(s -> s.getWordText()).collect(Collectors.toList());
	}

	@RequestMapping("/word/{word}/chars/{nChars}")
	@ResponseBody
	public List<String> getNMatchingChars(@PathVariable String word, @PathVariable byte nChars) {
		if (word.length() < nChars)
			throw new IllegalArgumentException("Search word is smaller then number of matched chars");
		return wordCache.getWordList()
				.stream()
				.filter(s -> s.length() >= nChars)
				.filter(s -> s.isEqualSuffix(word, nChars))
				.map(w -> w.getWordText())
				.collect(Collectors.toList());
	}
}
