package com.serene.kid.dict.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Word {

	private long id;

	private String wordText = "";

	public Word(final String wordText) {
		this.wordText = wordText;
	}

}
