package com.serene.kid.dict.model;

import java.util.List;

import com.serene.kid.dict.entities.Word;

/**
 * Caching layer for Word objects
 * @author nikola
 *
 */
public interface IWordCache {

	List<Word> getWordList();
}
