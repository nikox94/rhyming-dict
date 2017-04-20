package com.serene.kid.dict.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.axet.wget.WGet;
import com.serene.kid.dict.entities.Word;

import lombok.extern.java.Log;

@Service
@Log
public class WordService implements IWordService {

	@Value("${urls.apache.dict}")
	String APACHE_DICT_URL;
	
	final Path APACHE_DICT_TARGET_FILE = FileSystems.getDefault().getPath(
			System.getProperty("java.io.tmpdir"),
			"dictionaries-bg.oxt");
	
	private File downloadApacheDictionaryFile() {
		try {
			log.log(Level.INFO, "Starting download of dictionary at " + APACHE_DICT_URL);
			final URL url = new URL(APACHE_DICT_URL);
			log.log(Level.INFO, "Target file for download is " + APACHE_DICT_TARGET_FILE);
			final File target = new File(APACHE_DICT_TARGET_FILE.toString());
			final WGet wget = new WGet(url, target);
			// Single threaded download. Will return here only when
			// download completes or error.
			wget.download();
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

	public List<Word> getWords() {

		File apacheDictZip = new File(APACHE_DICT_TARGET_FILE.toString());

		if (!apacheDictZip.exists()) {
			apacheDictZip = downloadApacheDictionaryFile(); // new
															// File(APACHE_DICT_TARGET_FILE);
		}

		final List<Word> result = new ArrayList<>();

		try {
			final ZipFile zipFile = new ZipFile(apacheDictZip);
			final ZipEntry dicFile = zipFile.stream().filter(s -> s.getName().equals("spell/bg_BG.dic")).findFirst()
					.get();
			final ZipEntry affFile = zipFile.stream().filter(s -> s.getName().equals("spell/bg_BG.aff")).findFirst()
					.get(); // Include aff file for ending parsing

			final InputStream dicStream = zipFile.getInputStream(dicFile);
			final InputStream affStream = zipFile.getInputStream(affFile);
			final BufferedReader reader = new BufferedReader(new InputStreamReader(dicStream, "CP1251"));

			final String dicRegex = "^(?<word>\\S+)/(?<affix>\\w)$";
			final Pattern dicPattern = Pattern.compile(dicRegex);
			final Map<String, List<AffixRule>> affixRules = parseAffixFile(affStream);

			// Ignore first line. It contains the number of words in the file.
			reader.readLine();
			reader.lines().forEach(s -> {
				if (s.contains("/")) {
					final Matcher matcher = dicPattern.matcher(s);
					if (matcher.matches()) {
						final String word = matcher.group("word");
						final String affix = matcher.group("affix");
						final List<AffixRule> rules = affixRules.get(affix);
						for (final AffixRule rule : rules) {
							if (rule.remove == "0")
								result.add(new Word(word + rule.ending));
							else if (rule.remove
									.equals(word.substring(word.length() - rule.remove.length(), word.length()))) {
								result.add(new Word(word.substring(0, word.length() - rule.remove.length())
										+ (rule.ending.equals("0") ? "" : rule.ending)));
							}
						}
					}
				} else {
					result.add(new Word(s));
				}
			});

			zipFile.close();

		} catch (final ZipException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		}

		return result;
	}

	private Map<String, List<AffixRule>> parseAffixFile(final InputStream affStream) {
		final HashMap<String, List<AffixRule>> result = new HashMap<>();
		final String affRegex = "^(?<type>\\b\\S+\\b)\\s+(?<name>\\b\\S+\\b)\\s+(?<remove>\\b\\S+\\b)\\s+(?<ending>\\S+)\\s+(?<regex>\\S+)\\s*$";
		final Pattern affPattern = Pattern.compile(affRegex);

		try {
			final BufferedReader reader = new BufferedReader(new InputStreamReader(affStream, "CP1251"));
			reader.lines().forEach(s -> {
				final Matcher matcher = affPattern.matcher(s);
				if (matcher.matches()) {
					if (!result.containsKey(matcher.group("name"))) {
						result.put(matcher.group("name"), new ArrayList<AffixRule>());
					}
					result.get(matcher.group("name")).add(matcherToAffixRule(matcher));
				}
			});

		} catch (final UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return result;
	}

	private AffixRule matcherToAffixRule(final Matcher matcher) {
		final AffixRule result = new AffixRule();
		result.type = matcher.group("type");
		result.name = matcher.group("name");
		result.remove = matcher.group("remove");
		result.ending = matcher.group("ending");
		result.regex = matcher.group("regex");
		return result;
	}

	private class AffixRule {
		public String type;
		public String name;
		public String remove;
		public String ending;
		public String regex;
	}

}
