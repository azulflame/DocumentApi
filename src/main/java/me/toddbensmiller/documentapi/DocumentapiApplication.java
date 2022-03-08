package me.toddbensmiller.documentapi;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import me.toddbensmiller.documentapi.csvsaver.CsvSaver;
import me.toddbensmiller.documentapi.entity.Document;
import me.toddbensmiller.documentapi.entity.TextRelation;
import me.toddbensmiller.documentapi.entity.TitleRelation;
import me.toddbensmiller.documentapi.entity.Word;
import me.toddbensmiller.documentapi.service.DocumentService;
import me.toddbensmiller.documentapi.service.WordService;

@SpringBootApplication
public class DocumentapiApplication {

	private static Logger log = LoggerFactory.getLogger(DocumentapiApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(DocumentapiApplication.class, args);
	}

	@Bean
	@Autowired
	public CommandLineRunner loadData(DocumentService docService, WordService wordService) {
		return args -> {
			log.info("Starting document load");
			Map<String, Word> wordCache = new HashMap<String, Word>();
			CsvSaver<TitleRelation> titleRelationSaver = new CsvSaver<>("/home/todd/.documents/title_relations.csv",
					0L);
			CsvSaver<TextRelation> textRelationSaver = new CsvSaver<>("/home/todd/.documents/text_relations.csv", 0L);
			List<Document> docStorage = new ArrayList<Document>();
			ObjectMapper mapper = new ObjectMapper();
			FileReader inputStream = new FileReader("corpus.json");
			int index = 0;
			Long start = System.nanoTime();
			log.info("Loading documents");
			try (BufferedReader input = new BufferedReader(inputStream)) {
				// Rebuild fractured inputs, saving them as progress is made
				String title = "";
				String body = "";
				boolean finalRun = false;
				while (input.ready() || finalRun) {
					finalRun = false;
					Map<String, Object> parsed = mapper.readValue(input.readLine(),
							new TypeReference<Map<String, Object>>() {
							});
					if (title.equals((String) parsed.get("title"))) {
						body += " " + (String) parsed.get("text");
					} else {
						index++;
						if (body.length() > 0 && title.length() > 0) {
							Document d = new Document(title, body);
							d = docService.storeRealText(d, index);
							docStorage.add(d);
						}
						body = (String) parsed.get("text");
						title = (String) parsed.get("title");
						if (index % 50000 == 0) {
							parseDocuments(docStorage, wordCache, docService, wordService,
									textRelationSaver,
									titleRelationSaver);
							docStorage.clear();
						}
					}
				}
				if (title.length() > 0) {
					index++;
					Document d = new Document(title, body);
					d = docService.storeRealText(d, index);
					docStorage.add(d);
				}
			}
			parseDocuments(docStorage, wordCache, docService, wordService,
					textRelationSaver, titleRelationSaver);
			titleRelationSaver.save();
			textRelationSaver.save();
			log.info("Finished document load in " + ((double) ((System.nanoTime() -
					start) / 1000000) / 1000) + " s");
		};
	}

	private Word getFromCache(Map<String, Word> cache, String stem, Set<Word> newWords) {
		if (!cache.containsKey(stem)) {
			Word w = new Word(stem);
			cache.put(stem, w);
			newWords.add(w);
		}
		return cache.get(stem);
	}

	private void parseDocuments(List<Document> docs, Map<String, Word> wordCache,
			DocumentService docService,
			WordService wordService, CsvSaver<TextRelation> textRelationSaver,
			CsvSaver<TitleRelation> titleRelationSaver) {
		// save docs first
		List<Document> savedDocs = docService.saveAll(docs);
		Set<Word> newWords = new HashSet<>();
		Set<TitleRelation> titleRelations = new HashSet<>();
		Set<TextRelation> textRelations = new HashSet<>();
		// process words and relations from the document
		for (Document d : savedDocs) {
			Set<String> textStems = docService.getImperitiveStemsFromText(docService.getRealText(d));
			Set<String> titleStems = docService.getImperitiveStemsFromText(d.getTitle());
			for (String textString : textStems) {
				Word w = getFromCache(wordCache, textString, newWords);
				if (null != w) {
					// csvSaver.add(new TextRelation(w.getStem(), d.getId()));
					textRelations.add(new TextRelation(w.getStem(), d.getId()));
				}
			}
			for (String titleStem : titleStems) {
				Word w = getFromCache(wordCache, titleStem, newWords);
				if (null != w) {
					titleRelations.add(new TitleRelation(w.getStem(), d.getId()));
				}
			}
		}
		// save words
		List<Word> savedWords = wordService.saveAll(newWords);
		for (Word w : savedWords) {
			wordCache.put(w.getStem(), w);
		}
		for (TextRelation tr : textRelations) {
			textRelationSaver
					.add(new TextRelation(getFromCache(wordCache, tr.getStem(),
							newWords).getId(), tr.getDocId()));
		}
		for (TitleRelation tr : titleRelations) {
			titleRelationSaver
					.add(new TitleRelation(getFromCache(wordCache, tr.getStem(),
							newWords).getId(), tr.getDocId()));
		}
	}
}