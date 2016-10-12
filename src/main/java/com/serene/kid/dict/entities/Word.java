package com.serene.kid.dict.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "wrd_word")
@Data
@NoArgsConstructor
public class Word {

	@Id
	@GeneratedValue
	@Column(name = "wrd_id")
	private long id;

	@Column(name = "wrd_word_text")
	private String wordText = "";

	public Word(final String wordText) {
		this.wordText = wordText;
	}

}
