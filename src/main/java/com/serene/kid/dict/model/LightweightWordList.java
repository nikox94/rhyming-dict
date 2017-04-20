package com.serene.kid.dict.model;

import java.util.List;

import com.serene.kid.dict.entities.Word;
import com.serene.kid.dict.services.WordService;

import lombok.Data;

@Data
public class LightweightWordList implements IWordCache {

	private final List<Word> wordList;

	private final WordService wordService;

	public LightweightWordList(final List<Word> words) {
		wordList = words;
		wordService = null;
	}
}
