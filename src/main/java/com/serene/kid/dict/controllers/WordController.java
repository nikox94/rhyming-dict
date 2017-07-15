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
import com.serene.kid.dict.model.RhymeResponse;
import com.serene.kid.dict.services.IWordService;

@Controller
public class WordController {

	private final IWordCache wordCache;

	@Autowired
	public WordController(final IWordService wordService) {
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
	
	@RequestMapping("/word/{query}")
	@ResponseBody
	public RhymeResponse frontEndQuery(@PathVariable String query) {
		if (query.length() < 2)
			throw new IllegalArgumentException("Search word is smaller then 2. No results can be returned.");
		
		RhymeResponse response = new RhymeResponse();

		StringBuilder twoLetterRhymes = new StringBuilder();
		getNMatchingChars(query, (byte) 2).stream().forEach(s -> twoLetterRhymes.append(s).append(", "));
		
		if (twoLetterRhymes.length() == 0) {
			return response;
		} else {
			response.setTwoLetterRhymes(
					twoLetterRhymes.subSequence(0, twoLetterRhymes.length() - 2).toString());
		}
		
		if (query.length() == 2) {
			return response;
		}
		
		// Here query.length > 2
		StringBuilder threeLetterRhymes = new StringBuilder();
		getNMatchingChars(query, (byte) 3).stream().forEach(s -> threeLetterRhymes.append(s).append(", "));
		
		if (threeLetterRhymes.length() == 0) {
			return response;
		} else {
			response.setThreeLetterRhymes(
					threeLetterRhymes.subSequence(0, threeLetterRhymes.length() - 2).toString());
		}
		
		return response;
	}
}
