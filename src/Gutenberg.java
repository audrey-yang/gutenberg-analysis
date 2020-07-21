import java.io.*;
import java.util.*;

public class Gutenberg {

    /* Whole text words and frequencies */
    List<String> text;
    Map<String, Integer> freqs;

    /* Words and frequencies by chapter */
    List<List<String>> chapterText;
    List<HashMap<String, Integer>> chapterFreqs;
    String filePath;

    /**
    * Constructor that takes in a file path and read the file in to pre-process the text
    * and records the words and their frequencies
    * */
    public Gutenberg(String filePath) {
        this.filePath = filePath;
        text = new LinkedList<>();
        chapterText = new LinkedList<>();
        freqs = new LinkedHashMap<>();
        chapterFreqs = new LinkedList<>();

        /* Reading in file */
        if (filePath == null) {
            throw new IllegalArgumentException("File path can't be null!");
        }

        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(filePath));
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("Uh oh! File not found.");
        }

        String line;
        List<String> currChapter = null;
        HashMap<String, Integer> currChapterFreqs = null;

        try {
            while ((line = br.readLine()) != null) {
                for (String word : line.split(" ")) {
                    if (word.equals("Chapter")) {
                        if (currChapter != null) {
                            chapterText.add(currChapter);
                            chapterFreqs.add(currChapterFreqs);
                        }
                        currChapter = new LinkedList<>();
                        currChapterFreqs = new HashMap<>();
                        break;
                    }

                    if (currChapter != null) {
                        word = cleanWord(word);
                        if (word.isBlank()) {
                            break;
                        }
                        if (word.contains(" ")) {
                            String word1 = word.substring(0, word.indexOf(" "));

                            if (!word1.equals("")) {
                                text.add(word1);
                                freqs.put(word1, freqs.getOrDefault(word1, 0) + 1);
                                currChapter.add(word1);
                                currChapterFreqs.put(word1, currChapterFreqs.getOrDefault(word1, 0) + 1);
                            }
                            word = word.substring(word.indexOf(" ") + 1);
                        }

                        if (!word.equals("")) {
                            text.add(word);
                            freqs.put(word, freqs.getOrDefault(word, 0) + 1);
                            currChapter.add(word);
                            currChapterFreqs.put(word, currChapterFreqs.getOrDefault(word, 0) + 1);
                        }
                    }
                }
            }

            if (currChapter != null) {
                chapterText.add(currChapter);
                chapterFreqs.add(currChapterFreqs);
            }

        } catch (IOException e) {
            throw new IllegalArgumentException("Exception! " + e.toString());
        }
    }

    /**
     * Gets total number of words in the text
     *
    * @return number of words
    */
    public int getTotalNumberOfWords() {
        return text.size();
    }

    /**
     * Gets total number of unique words in the text
     *
     * @return number of unique words
     */
    public int getTotalUniqueWords() {
        return freqs.size();
    }

    /**
     * Gets the 20 most frequently occurring words in the text
     *
     * @return list of 20 most frequent words
     */
    public List<Map.Entry<String, Integer>> get20MostFrequentWords() {
        HashMap<String, Integer> sortedFreqs = sortedHMDescending(freqs);

        List<Map.Entry<String, Integer>> res = new LinkedList<>();
        Iterator it = sortedFreqs.entrySet().iterator();
        for (int i = 0; i < 20; i++) {
            if (!it.hasNext()) {
                break;
            }
            res.add((Map.Entry<String, Integer>) it.next());
        }
        return res;
    }

    /**
     * Gets the 20 most frequently occurring words in the text that aren't the
     * most commonly used English words
     *
     * @return list of 20 most frequently occurring "interesting" words
     */
    public List<Map.Entry<String, Integer>> get20MostInterestingFrequentWords() {
        HashSet<String> mostFrequent = mostFrequentWords();
        HashMap<String, Integer> sortedFreqs = sortedHMDescending(freqs);

        List<Map.Entry<String, Integer>> res = new LinkedList<>();
        Iterator it = sortedFreqs.entrySet().iterator();
        int i = 0;
        while (i < 20) {
            if (!it.hasNext()) {
                break;
            }

            Map.Entry<String, Integer> nxt = (Map.Entry<String, Integer>) it.next();
            if (!mostFrequent.contains(nxt.getKey())) {
                res.add(nxt);
                i++;
            }
        }

        return res;
    }


    /**
     * Gets the least frequently used words
     *
     * @return list of least frequently occurring words
     */
    public List<Map.Entry<String, Integer>> get20LeastFrequentWords() {
        HashMap<String, Integer> sortedFreqs = sortedHMAscending(freqs);

        List<Map.Entry<String, Integer>> res = new LinkedList<>();
        Iterator it = sortedFreqs.entrySet().iterator();
        for (int i = 0; i < 20; i++) {
            if (!it.hasNext()) {
                break;
            }
            res.add((Map.Entry<String, Integer>) it.next());
        }
        return res;
    }

    /**
     * Gets the chapter-by-chapter progression of word frequency
     *
     * @param word  the word whose frequencies to get, cannot be null or empty
     * @return the list of frequencies by chapter
     */
    public List<Integer> getFrequencyOfWord(String word) {
        if (word == null || word.isBlank()) {
            throw new IllegalArgumentException();
        }

        Integer[] progression = new Integer[chapterFreqs.size()];
        for (int i = 0; i <  chapterFreqs.size(); i++) {
            progression[i] = chapterFreqs.get(i).getOrDefault(word, 0);
        }

        return Arrays.asList(progression);
    }

    /**
     * Gets the chapter in which a specific quote appears
     *
     * @param quote  the quote to search, cannot be null or empty
     * @return the chapter number of the chapter with the quote;
     *         chapter is -1 if the quote is not in the book
     */
    public int getChapterQuoteAppears(String quote) {
        if (quote == null || quote.isBlank()) {
            throw new IllegalArgumentException();
        }

        List<String> quoteWords = new LinkedList<>();
        for (String word : quote.split(" ")) {
            word = cleanWord(word);
            if (!word.isBlank()) {
                quoteWords.add(word);
            }
        }

        if (quoteWords.size() != 0) {
            for (int i = 0; i < chapterFreqs.size(); i++) {

                if (!chapterFreqs.get(i).containsKey(quoteWords.get(0))) {
                    continue;
                }


                List<String> chapter = chapterText.get(i);

                int ind = chapter.indexOf(quoteWords.get(0));

                while (ind != -1 && ind < chapter.size() - 1) {
                    for (int j = 0; j < quoteWords.size(); j++) {
                        if (!chapter.get(ind).
                                equals(quoteWords.get(j))) {
                            break;
                        }

                        ind++;

                        if (j == quoteWords.size() - 1) {
                            return i + 1;
                        }
                    }

                    if (ind >= chapter.size() - 1) {
                        break;
                    }
                    chapter = chapter.subList(ind + 1, chapter.size());
                    ind = chapter.indexOf(quoteWords.get(0));
                }
            }
        }

        return -1;
    }

    /**
     * Generates a sentence using a Markov Chain
     *
     * @return generated sentence
     */
    public String generateSentence() {
        MarkovChain m = new MarkovChain(filePath);
        return m.createSentence();
    }

    /*
     * Helper method that removes punctuation
     * */
    static String cleanWord(String word) {
        return word.toLowerCase().
                replaceAll("[.,;:!?\\\\\\/\\–\\\"\\'\\_()]", "").
                replaceAll("--", " ").strip();
    }

    /*
     * Helper method that sorts a hashmap in descending order by value
     * */
    static HashMap<String, Integer> sortedHMDescending(Map<String, Integer> hm) {
        List<Map.Entry<String, Integer>> list =
                new LinkedList<>(hm.entrySet());

        //Sort list
        Collections.sort(list, Comparator.comparing(Map.Entry::getValue, (a, b) -> b - a));

        //Convert list to linked hashmap
        HashMap<String, Integer> res = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> e : list) {
            res.put(e.getKey(), e.getValue());
        }
        return res;
    }

    /*
     * Helper method that sorts a hashmap in ascending order by value
     * */
    static HashMap<String, Integer> sortedHMAscending(Map<String, Integer> hm) {
        List<Map.Entry<String, Integer>> list =
                new LinkedList<>(hm.entrySet());

        //Sort list
        Collections.sort(list, Comparator.comparing(Map.Entry::getValue));

        //Convert list to linked hashmap
        HashMap<String, Integer> res = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> e : list) {
            res.put(e.getKey(), e.getValue());
        }
        return res;
    }

    /*
     * Helper method that reads in the 1000 most frequent words file
     * and returns a set of those words
     */
    static private HashSet<String> mostFrequentWords() {
        HashSet<String> hs = new HashSet<>();
        int i = 0;

        /* Reading in file */
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader("files/1000.txt"));
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("Uh oh! File not found.");
        }

        String line;

        try {
            while ((line = br.readLine()) != null && i < 100) {
                hs.add(line);
                i++;
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Exception! " + e.toString());
        }

        return hs;
    }

}