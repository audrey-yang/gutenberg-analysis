import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class MarkovChain {

    HashMap<String, List<String>> bigrams;

    public MarkovChain(String filePath) {

        bigrams = new HashMap<>();
        String text = "";

        String line;
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(filePath));
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("Uh oh! File not found.");
        }

        try {
            while ((line = br.readLine()) != null) {
                text += line.replaceAll("\"", "").toLowerCase() + " ";
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Exception! " + e.toString());
        }



        String[] words = text.split(" ");

        for (int i = 0; i < words.length - 1; i++) {
            if (words[i].isBlank() || words[i].equals("Chapter") ||
                    Character.isDigit(words[i].charAt(0)) || words[i + 1].isBlank() ||
                    words[i + 1].equals("Chapter") || Character.isDigit(words[i + 1].charAt(0))) {
                continue;
            }
            List<String> lst = bigrams.getOrDefault(words[i].toLowerCase(), new LinkedList<>());
            lst.add(words[i + 1]);
            bigrams.put(words[i], lst);
        }

    }

    public String createSentence() {
        String punctuations = ".?!";
        String word = new ArrayList<>(bigrams.keySet()).get((int) (Math.random() * bigrams.size()));
        String sentence = "";
        int i = 0;

        while (!punctuations.contains(word.charAt(word.length() - 1) + "") && i < 20) {
            sentence += word + " ";
            List<String> next = bigrams.get(word);

            if (next == null || next.size() == 0) {
                break;
            }
            word = next.get((int) (Math.random() * next.size()));
            i++;
        }

        sentence += word;

        return sentence;
    }

}
