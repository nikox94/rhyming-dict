package com.serene.kid.dict.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.springframework.stereotype.Service;

import com.github.axet.wget.WGet;
import com.serene.kid.dict.entities.Word;

import lombok.extern.java.Log;

@Service
@Log
public class WordService {

	final String APACHE_DICT_URL = "http://downloads.sourceforge.net/project/aoo-extensions/744/8/dictionaries-bg.oxt";
	final Path APACHE_DICT_TARGET_FILE = FileSystems.getDefault()
			.getPath(System.getProperty("java.io.tmpdir"), "dictionaries-bg.oxt");

	private File downloadApacheDictionaryFile() {
		try {
			log.log(Level.INFO, "Starting download of dictionary at " + APACHE_DICT_URL);
			final URL url = new URL(APACHE_DICT_URL);
			log.log(Level.INFO, "Target file for download is " + APACHE_DICT_TARGET_FILE);
			final File target = new File(APACHE_DICT_TARGET_FILE.toString());
			final WGet w = new WGet(url, target);
			// single threaded download. Will return here only when
			// download completes or error.
			w.download();
			log.log(Level.INFO, "Dictionary download from APACHE finished successfully.");
			return target;
		} catch (final MalformedURLException e) {
			log.log(Level.WARNING, "Imame problem s URL-to Houston.");
			e.printStackTrace();
		} catch (final RuntimeException e) {
			e.printStackTrace();
		}

		return null;
	}

	public List<Word> getWordsFromApache() {

		File apacheDictZip = new File(APACHE_DICT_TARGET_FILE.toString());

		if (!apacheDictZip.exists()) {
			apacheDictZip = downloadApacheDictionaryFile(); // new File(APACHE_DICT_TARGET_FILE);
		}

		final List<Word> result = new ArrayList<>();

		try {
			final ZipFile zipFile = new ZipFile(apacheDictZip);
			final ZipEntry dicFile = zipFile.stream().filter(s -> s.getName().equals("spell/bg_BG.dic")).findFirst().get();
			// TODO: include aff file ending parsing
			// final ZipEntry affFile = zipFile.stream().filter(s -> s.getName().equals("spell/bg_BG.aff")).findFirst().get();

			final InputStream dicStream = zipFile.getInputStream(dicFile);
			final BufferedReader reader = new BufferedReader(new InputStreamReader(dicStream, "CP1251"));

			// Ignore first line. It contains the number of words in the file.
			reader.readLine();
			reader.lines().filter(s -> !s.contains("/")).forEach(s -> {
				result.add(new Word(s));
					});

			zipFile.close();

		} catch (final ZipException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		}

		return result;
	}

}
