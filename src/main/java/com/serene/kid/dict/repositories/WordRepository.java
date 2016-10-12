package com.serene.kid.dict.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.serene.kid.dict.entities.Word;

public interface WordRepository extends JpaRepository<Word, Long> {

}
