package spell;

import java.io.IOException;
import java.util.*;
import java.io.FileReader;

public class SpellCorrector implements ISpellCorrector
{
    private Trie dictionary;
    public SpellCorrector() {dictionary = new Trie();}

    @Override
    public void useDictionary(String dictionaryFileName) throws IOException
    {
        FileReader in = new FileReader(dictionaryFileName);
        Scanner scan = new Scanner(in);
        String word;
        while (scan.hasNext())
        {
            word = scan.next().toLowerCase();
            dictionary.add(word);
        }
        scan.close();
    }

    @Override
    public String suggestSimilarWord(String inputWord)
    {
        ArrayList<String> wordList1 = new ArrayList<>();
        ArrayList<String> wordList2 = new ArrayList<>();

        ArrayList<String> possibleWords = new ArrayList<>();
        inputWord = inputWord.toLowerCase();
        if (dictionary.find(inputWord) != null)// word is correct
        {
            return inputWord;
        }
        else//word is incorrect
        {
            alteration(inputWord, wordList1);
            insertion(inputWord, wordList1);
            deletion(inputWord, wordList1);
            transposition(inputWord, wordList1);
            //alteration(inputWord, wordList1);

            String d1Word;
            String possibleWord = null;
            for (int i = 0; i < wordList1.size(); i++)
            {
                d1Word = wordList1.get(i);
                if (dictionary.find(d1Word) != null)
                {
                    possibleWord = d1Word;
                    possibleWords.add(possibleWord);
                }
            }
            if (possibleWord == null)
            {
                String d2Word;
                for (int i = 0; i < wordList1.size(); i++)
                {
                    d1Word = wordList1.get(i);
                    wordList2.clear();
                    distance2(d1Word, wordList2);
                    for (int j = 0; j < wordList2.size(); j++)
                    {
                        d2Word = wordList2.get(j);
                        if (dictionary.find(d2Word) != null)
                        {
                            possibleWord = d2Word;
                            possibleWords.add(possibleWord);
                        }
                    }
                }
            }
            if (possibleWords.size() != 0)
            {
                possibleWord = pickCommon(possibleWords);
                return possibleWord;
            }
            return null;
        }

        //return null;
    }

    public void distance2(String word, ArrayList<String> wL)
    {
        insertion(word, wL);
        deletion(word, wL);
        transposition(word, wL);
        alteration(word, wL);
    }

    public String pickCommon(ArrayList<String> wL)
    {
        int highestCount = 0;
        String suggestedWord = null;
        for (int i = 0; i<wL.size(); i++)
        {
            if (dictionary.find(wL.get(i)).getValue() > highestCount)
            {
                highestCount = dictionary.find(wL.get(i)).getValue();
                suggestedWord = wL.get(i);
            }
            else if (dictionary.find(wL.get(i)).getValue() == highestCount)
            {
                highestCount = dictionary.find(wL.get(i)).getValue();
                int compare = suggestedWord.compareTo(wL.get(i));
                if (compare > 0)
                {
                    suggestedWord = wL.get(i);
                }
            }

        }
        return suggestedWord;
    }

    public void deletion(String word, ArrayList<String> wL)
    {
        for (int i = 0; i <= word.length() -1; i++)
        {
            StringBuilder newWord = new StringBuilder(word);
            newWord.deleteCharAt(i);
            wL.add(newWord.toString());
        }
    }

    public void transposition(String word, ArrayList<String> wL)
    {
        for (int i = 0; i < word.length(); i++)
        {
            StringBuilder newWord = new StringBuilder(word);
            int j = i+1;
            if (j < word.length())//<=?
            {
                newWord.setCharAt(i, word.charAt(j));
                newWord.setCharAt(j, word.charAt(i));
            }
            wL.add(newWord.toString());
        }
    }

    public void alteration(String word, ArrayList<String> wL)
    {
        for (int i = 0; i < word.length(); i++)
        {
            for (int j = 0; j < 26; j++)
            {
                StringBuilder newWord = new StringBuilder(word);
                newWord.setCharAt(i, (char) ('a' + j));//?? unsure
                wL.add(newWord.toString());
            }
        }
    }

    public void insertion(String word, ArrayList<String> wL)
    {
        for (int i = 0; i <= word.length(); i++)
        {
            for (int j = 0; j < 26; j++)
            {
                StringBuilder newWord = new StringBuilder(word);
                newWord.insert(i, (char) ('a' + j));
                wL.add(newWord.toString());
            }
        }
    }
}
