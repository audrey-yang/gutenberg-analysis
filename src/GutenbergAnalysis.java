public class GutenbergAnalysis {

    public static void main(String[] args) {
        Gutenberg g = new Gutenberg("files/hound.txt");

        System.out.println("The Hound of the Baskervilles by Arthur Conan Doyle");
        System.out.println("Total number of words: " + g.getTotalNumberOfWords());
        System.out.println("Total number of unique words: " + g.getTotalUniqueWords());
        System.out.println("Most frequent words: " + g.get20MostFrequentWords());
        System.out.println("Most frequent interesting words: " + g.get20MostInterestingFrequentWords());
        System.out.println("Least frequent words: " + g.get20LeastFrequentWords());
        System.out.println("Frequency of the word \"baskerville\": " + g.getFrequencyOfWord("baskerville"));
        System.out.println("Chapter of the quote \"There is nothing more stimulating than a case where everything goes against you\": "
                + g.getChapterQuoteAppears("There is nothing more stimulating than a case where everything goes against you"));
        System.out.println("Generated sentence: " + g.generateSentence());
    }

}
