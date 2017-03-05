package com.serene.kid.dict;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.serene.kid.dict.entities.Word;
import com.serene.kid.dict.services.WordService;

import lombok.Data;

@Component
@Data
public class LightweightWordList {

	private final List<Word> wordList;

	private final WordService wordService;

	@Autowired
	public LightweightWordList(final WordService wordService) {
		this.wordService = wordService;
		wordList = wordService.getWords();
	}

	public LightweightWordList(final List<Word> words) {
		wordList = words;
		wordService = null;
	}
}
