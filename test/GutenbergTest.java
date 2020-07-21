import java.util.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class GutenbergTest {

    Gutenberg g1 = new Gutenberg("files/test.txt");
    Gutenberg g2 = new Gutenberg("files/test2.txt");

    @Test(expected = IllegalArgumentException.class)
    public void testNullFilePath() {
        Gutenberg g = new Gutenberg(null);
        assertEquals(0, g.getTotalNumberOfWords());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFileNotFound() {
        Gutenberg g = new Gutenberg("../files/files/files.txt");
        assertEquals(0, g.getTotalNumberOfWords());
    }

    @Test
    public void testTextMatches() {
        List<String> lst = Arrays.asList("what", "who", "where", "are",
                "you", "what", "how", "what", "happened");
        assertEquals(lst, g1.text);
        assertEquals(9, g1.getTotalNumberOfWords());
    }

    @Test
    public void testFreqsMatches() {
        Map<String, Integer> freqs = new HashMap<>();
        freqs.put("what", 3);
        freqs.put("who", 1);
        freqs.put("where", 1);
        freqs.put("are", 1);
        freqs.put("you", 1);
        freqs.put("how", 1);
        freqs.put("happened", 1);
        assertEquals(freqs, g1.freqs);
        assertEquals(7, g1.getTotalUniqueWords());
    }

    @Test
    public void testChapterTextMatches() {
        List<String> lst1 = Arrays.asList("what", "who", "where", "are", "you", "what");
        List<String> lst2 = Arrays.asList("how", "what", "happened");
        assertEquals(lst1, g1.chapterText.get(0));
        assertEquals(lst2, g1.chapterText.get(1));
    }

    @Test
    public void testChapterFreqsMatches() {
        Map<String, Integer> freqs1 = new HashMap<>();
        freqs1.put("what", 2);
        freqs1.put("who", 1);
        freqs1.put("where", 1);
        freqs1.put("are", 1);
        freqs1.put("you", 1);

        Map<String, Integer> freqs2 = new HashMap<>();
        freqs2.put("what", 1);
        freqs2.put("how", 1);
        freqs2.put("happened", 1);

        assertEquals(freqs1, g1.chapterFreqs.get(0));
        assertEquals(freqs2, g1.chapterFreqs.get(1));
    }

    @Test
    public void testGetFreqOfWord() {
        Integer[] expected = {2, 1};
        assertEquals(Arrays.asList(expected), g1.getFrequencyOfWord("what"));
    }

    @Test
    public void testGetMostCommonWordsUnder20() {
        List<String> expected = Arrays.asList("what", "who", "where",
                "are", "you", "how", "happened");

        Iterator<Map.Entry<String, Integer>> it = g1.get20MostFrequentWords().iterator();
        int i = 0;
        while (it.hasNext()) {
            assertEquals(expected.get(i), it.next().getKey());
            i++;
        }

    }

    @Test
    public void testGetMostInterestingCommonWordsUnder20() {
        List<String> expected = Arrays.asList("where", "happened");
        Iterator<Map.Entry<String, Integer>> it = g1.get20MostInterestingFrequentWords().iterator();
        int i = 0;
        while (it.hasNext()) {
            assertEquals(expected.get(i), it.next().getKey());
            i++;
        }
    }

    @Test
    public void testGetLeastCommonWordsUnder20() {
        List<String> expected = Arrays.asList("who", "where", "are", "you",
                "how", "happened", "what");
        Iterator<Map.Entry<String, Integer>> it = g1.get20LeastFrequentWords().iterator();
        int i = 0;
        while (it.hasNext()) {
            assertEquals(expected.get(i), it.next().getKey());
            i++;
        }
    }

    @Test
    public void testGetMostCommonWordsOver20() {
        List<String> expected = Arrays.asList("what", "i", "you", "today", "went",
                "outside", "who", "where", "are", "how", "happened", "well", "did",
                "do", "oh", "my", "word", "words", "it", "is");
        Iterator<Map.Entry<String, Integer>> it = g2.get20MostFrequentWords().iterator();
        int i = 0;
        while (it.hasNext()) {
            assertEquals(expected.get(i), it.next().getKey());
            i++;
        }
        assertEquals(20, g2.get20MostFrequentWords().size());
    }

    @Test
    public void testGetMostInterestingCommonWordsOver20() {
        List<String> expected = Arrays.asList("today", "went", "outside", "where",
                "happened", "well", "oh", "words", "july", "saw", "birb");
        Iterator<Map.Entry<String, Integer>> it = g2.get20MostInterestingFrequentWords().iterator();
        int i = 0;
        while (it.hasNext()) {
            assertEquals(expected.get(i), it.next().getKey());
            i++;
        }
    }

    @Test
    public void testGetLeastCommonWordsOver20() {
        List<String> expected = Arrays.asList("who", "where", "are", "how", "happened", "well", "did", "do",
                "oh", "my", "word", "words", "it", "is", "july", "and",
                "saw", "a", "birb", "you");
        Iterator<Map.Entry<String, Integer>> it = g2.get20LeastFrequentWords().iterator();
        int i = 0;
        while (it.hasNext()) {
            assertEquals(expected.get(i), it.next().getKey());
            i++;
        }
        assertEquals(20, g2.get20LeastFrequentWords().size());
    }

    @Test
    public void testProgression1() {
        List<Integer> expected1 = Arrays.asList(2, 1);
        assertEquals(expected1, g1.getFrequencyOfWord("what"));

        List<Integer> expected2 = Arrays.asList(2, 1, 1, 0, 0);
        assertEquals(expected2, g2.getFrequencyOfWord("what"));
    }

    @Test
    public void testProgression2() {
        List<Integer> expected1 = Arrays.asList(0, 0);
        assertEquals(expected1, g1.getFrequencyOfWord("outside"));

        List<Integer> expected2 = Arrays.asList(0, 0, 1, 0, 1);
        assertEquals(expected2, g2.getFrequencyOfWord("outside"));
    }

    @Test
    public void testQuoteGetChapter() {
        assertEquals(1, g1.getChapterQuoteAppears("what?"));
        assertEquals(1, g1.getChapterQuoteAppears("where are you?"));
        assertEquals(2, g1.getChapterQuoteAppears("how?!?!?!?!"));
        assertEquals(2, g1.getChapterQuoteAppears("what happened?"));
    }

    @Test
    public void testQuoteGetChapterWordOverlap() {
        assertEquals(5, g2.getChapterQuoteAppears("today i went outside"));
    }

}