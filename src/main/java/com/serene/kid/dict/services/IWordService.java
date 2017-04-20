package com.serene.kid.dict.services;

import java.util.List;

import com.serene.kid.dict.entities.Word;

public interface IWordService {

	/**
	 * Get available words from service
	 * @return Word list of valid dictionary words
	 */
	List<Word> getWords();
}
