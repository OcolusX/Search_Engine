package searchengine.dto.indexing;

import org.apache.lucene.morphology.LuceneMorphology;
import org.apache.lucene.morphology.russian.RussianLuceneMorphology;

import java.io.IOException;
import java.util.*;

public class HtmlLemmaFinder {

    private final LuceneMorphology luceneMorphology;
    private static final String WORD_TYPE_REGEX = "\\W\\w&&[^а-яА-Я\\s]";
    private static final String HTML_TAG_REGEX = "<.+?>";
    private static final String[] particlesNames = new String[]{"МЕЖД", "ПРЕДЛ", "СОЮЗ"};

    public static HtmlLemmaFinder getInstance() throws IOException {
        LuceneMorphology morphology = new RussianLuceneMorphology();
        return new HtmlLemmaFinder(morphology);
    }

    private HtmlLemmaFinder(LuceneMorphology luceneMorphology) {
        this.luceneMorphology = luceneMorphology;
    }
    private HtmlLemmaFinder() {
        throw new RuntimeException("Disallow construct");
    }

    public String clearHtmlTags(String html) {
        return html.replaceAll(HTML_TAG_REGEX, "");
    }

    public Map<String, Integer> collectLemmas(String text) {
        String[] words = getRussianWords(text);
        HashMap<String, Integer> lemmas = new HashMap<>();

        for(String word : words) {
            if(word.isBlank()) {
                continue;
            }

            List<String> wordBaseForms = luceneMorphology.getMorphInfo(word);
            if(containsParticle(wordBaseForms)) {
                continue;
            }

            List<String> normalForms = luceneMorphology.getNormalForms(word);
            if(normalForms.isEmpty()) {
                continue;
            }
            String normalWord = normalForms.get(0);
            if(lemmas.containsKey(normalWord)) {
                lemmas.put(normalWord, lemmas.get(normalWord) + 1);
            } else {
                lemmas.put(normalWord, 1);
            }
        }
        return lemmas;
    }

    public Set<String> getLemmasSet(String text) {
        String[] textArray = getRussianWords(text);
        Set<String> lemmasSet = new HashSet<>();
        for(String word : textArray) {
            if(!word.isEmpty() && isCorrectWordForm(word)) {
                List<String> wordBaseForms = luceneMorphology.getMorphInfo(word);
                if(containsParticle(wordBaseForms)){
                    continue;
                }
                lemmasSet.addAll(luceneMorphology.getNormalForms(word));
            }
        }
        return lemmasSet;
    }

    private String[] getRussianWords(String text) {
        return text.toLowerCase(Locale.ROOT)
                .replaceAll("([^а-я\\s])", " ")
                .trim().split("\\s+");
    }

    private boolean containsParticle(List<String> wordBaseForms) {
        for(String wordBaseForm : wordBaseForms) {
            for(String particleName : particlesNames) {
                if(wordBaseForm.toUpperCase().contains(particleName)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isCorrectWordForm(String word) {
        List<String> wordInfo = luceneMorphology.getMorphInfo(word);
        for(String morphInfo : wordInfo) {
            if(morphInfo.matches(WORD_TYPE_REGEX)) {
                return false;
            }
        }
        return true;
    }
}
