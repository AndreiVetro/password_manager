package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class WordUtil
{
    private List<String> englishWords = new ArrayList<>();
    private List<String> romanianWords = new ArrayList<>();

    public WordUtil()
    {
        try
        {
            loadEnglishWordsIntoMemory();
            loadRomanianWordsIntoMemory();

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void loadEnglishWordsIntoMemory() throws IOException
    {
        loadWordsIntoMemory("src/main/resources/words/englishWords.txt", englishWords);
    }

    private void loadRomanianWordsIntoMemory() throws IOException
    {
        loadWordsIntoMemory("src/main/resources/words/romanianWords.txt", romanianWords);
    }

    private void loadWordsIntoMemory(String path, List<String> wordList) throws IOException
    {
        File input = new File(path);

        BufferedReader bufferedReader = new BufferedReader(new FileReader(input));

        String line;
        while ((line = bufferedReader.readLine()) != null)
        {
            wordList.add(line);
        }
    }

    public List<String> getRandomEnglishWords(int count)
    {
        return getRandomWords(count, englishWords);
    }

    public List<String> getRandomRomanianWords(int count)
    {
        return getRandomWords(count, romanianWords);
    }

    private List<String> getRandomWords(int count, List<String> wordList)
    {
        List<String> resultList = new ArrayList<>();

        ThreadLocalRandom.current().ints(0, wordList.size()).distinct()
                .limit(count).forEach(random -> resultList.add(wordList.get(random)));

        return resultList;
    }
}
