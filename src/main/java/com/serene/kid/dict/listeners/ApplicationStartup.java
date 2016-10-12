package com.serene.kid.dict.listeners;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.serene.kid.dict.repositories.WordRepository;
import com.serene.kid.dict.services.WordService;

@Component
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {

	private final WordRepository wordRepository;

	private final WordService wordService;

	public ApplicationStartup(final WordRepository wordRepository, final WordService wordService) {
		this.wordRepository = wordRepository;
		this.wordService = wordService;
	}

	@Override
	public void onApplicationEvent(final ApplicationReadyEvent event) {

		wordRepository.save(wordService.getWordsFromApache());
	}

}
