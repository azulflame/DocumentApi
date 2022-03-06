package me.toddbensmiller.documentapi;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import me.toddbensmiller.documentapi.entity.Document;
import me.toddbensmiller.documentapi.entity.Word;
import me.toddbensmiller.documentapi.service.DocumentService;
import me.toddbensmiller.documentapi.service.WordService;

@SpringBootApplication
public class DocumentapiApplication {

	private static Logger log = LoggerFactory.getLogger(DocumentapiApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(DocumentapiApplication.class, args);
	}

	public void saveDocuments(Set<Document> documents, Set<Word> words, DocumentService documentService,
			WordService wordService) {
		log.info("Saving words");
		wordService.saveAll(words);
		log.info("Words saved");
		log.info("Saving documents");
		documentService.saveAll(documents);
		log.info("Saved documents");
	}

	// @Bean
	// @Autowired
	// public CommandLineRunner loadData(DocumentService docService, WordService
	// wordService) {
	// return args -> {
	// log.info("Starting document load");
	// Map<String, Word> wordCache = new HashMap<String, Word>();
	// Map<String, Word> newWordCache = new HashMap<String, Word>();
	// Set<Document> docStorage = new HashSet<Document>();
	// ObjectMapper mapper = new ObjectMapper();
	// FileReader inputStream = new FileReader("corpus.json");
	// int index = 0;
	// int line = 0;
	// try (BufferedReader input = new BufferedReader(inputStream)) {
	// // Rebuild fractured inputs, saving them as progress is made
	// String title = "";
	// String body = "";
	// while (input.ready()) {
	// line++;
	// Map<String, Object> parsed = mapper.readValue(input.readLine(),
	// new TypeReference<Map<String, Object>>() {
	// });
	// if (title.equals((String) parsed.get("title"))) {
	// body += " " + (String) parsed.get("text");
	// } else {
	// if (body.length() > 0 && title.length() > 0) {
	// Document d = new Document();
	// d.setText(body);
	// d.setTitle(title);
	// Set<String> textStems = docService.getImperitiveStemsFromText(d.getText());
	// Set<String> titleStems = docService.getImperitiveStemsFromText(d.getTitle());
	// Set<Word> textWords = textStems.stream().map(stem -> {
	// Word w = wordCache.getOrDefault(stem, null);
	// if (w == null) {
	// w = new Word(stem);
	// wordCache.put(stem, w);
	// newWordCache.put(stem, w);
	// }
	// return w;
	// }).collect(Collectors.toSet());
	// Set<Word> titleWords = titleStems.stream().map(stem -> {
	// Word w = wordCache.getOrDefault(stem, null);
	// if (w == null) {
	// w = new Word(stem);
	// wordCache.put(stem, w);
	// newWordCache.put(stem, w);
	// }
	// return w;
	// }).collect(Collectors.toSet());
	// d.setContainedInText(textWords);
	// d.setContainedInTitle(titleWords);
	// docStorage.add(d);
	// // log.info("Document " + (index++) + ": \"" + d.getTitle() + "\" (" + line +
	// ")
	// // loaded");
	// index++;
	// if (index % 30 == 0) {
	// log.info("Storing document batch " + index / 30);
	// saveDocuments(docStorage, Set.copyOf(newWordCache.values()), docService,
	// wordService);
	// newWordCache.clear();
	// docStorage.clear();
	// log.info("Finished storing document batch " + index / 30);
	// }
	// }
	// body = (String) parsed.get("text");
	// title = (String) parsed.get("title");
	// }
	// }
	// Document d = new Document();
	// d.setText(body);
	// d.setTitle(title);
	// Set<String> textStems = docService.getImperitiveStemsFromText(d.getText());
	// Set<String> titleStems = docService.getImperitiveStemsFromText(d.getTitle());
	// Set<Word> textWords = textStems.stream().filter(stem -> stem != null &&
	// !stem.equals(""))
	// .map(stem -> {
	// Word w = wordCache.getOrDefault(stem, null);
	// if (w == null) {
	// w = wordService.saveWord(new Word(stem));
	// wordCache.put(stem, w);
	// newWordCache.put(stem, w);
	// }
	// return w;
	// }).collect(Collectors.toSet());
	// Set<Word> titleWords = titleStems.stream().filter(stem -> stem != null &&
	// !stem.equals(""))
	// .map(stem -> {
	// Word w = wordCache.getOrDefault(stem, null);
	// if (w == null) {
	// w = new Word(stem);
	// wordCache.put(stem, w);
	// newWordCache.put(stem, w);
	// }
	// return w;
	// }).collect(Collectors.toSet());
	// d.setContainedInText(textWords);
	// d.setContainedInTitle(titleWords);
	// docStorage.add(d);

	// saveDocuments(docStorage, Set.copyOf(newWordCache.values()), docService,
	// wordService);
	// log.info("Document " + (index++) + ": \"" + d.getTitle() + "\" (" + line + ")
	// loaded");
	// }
	// log.info("Finished document load");
	// };
	// }
}
