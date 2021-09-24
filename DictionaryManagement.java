import java.io.*;
import java.util.Collection;
import java.util.Collections;
import java.util.Scanner;
import java.lang.String;

public class DictionaryManagement {
    Dictionary dictionary = new Dictionary();
    Word word = new Word();

    /**
     * them word tu file vao list
     */

    public void insertFromFile() {
        try {
            File file = new File("dictionaries.txt");
            Scanner scanner = new Scanner(file);
            String temp;
            String targetTemp;
            String explainTemp;
            int i = 0;
            while (scanner.hasNextLine()) {
                temp = scanner.nextLine();
                //xac dinh index cua ki tu tab
                i = temp.indexOf(9);
                //copy wordTarget
                targetTemp = temp.substring(0, i-1);
                //copy wordExplain
                explainTemp = temp.substring(i + 1);
                word.setWord_target(targetTemp);
                word.setWord_explain(explainTemp);
                dictionary.wordList.add(word);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * ghi list dictionary vao file
     * @throws IOException .
     */

    public void dictionaryExportToFile() throws IOException {
        File file = new File("directories.txt");
        OutputStream outputStream = new FileOutputStream(file);
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
        for (Word item: dictionary.wordList) {
            outputStreamWriter.write(item.getWord_target() + 9 + item.getWord_explain());
            outputStreamWriter.write("\n");
        }
        outputStreamWriter.flush();
    }

    /**
     * ham them 1 tu vao dictionary va sap xep lai dictionary.
     * @param word tu can them vao
     */
    public void insertWordToDictionaries(Word word) {
        dictionary.wordList.add(word);
        Collections.sort(dictionary.wordList,Word::compareTo);
    }


    /**
     * tim nghia cua 1 tu su dung tim kiem nhi phan
     * @param wordTarget tu can tim
     * @return nghia cua tu
     */
    public int dictionaryLookup(String wordTarget) {
        int left = 0;
        int right = dictionary.wordList.size();
        while (left <= right) {
            int mid = (left + right) / 2;
            String temp = dictionary.wordList.get(mid).getWord_target();
            if (wordTarget == temp) {
                return mid;
            } else {
                if (wordTarget.compareTo(temp) > 0) {
                    left = mid +1;
                } else {
                    right = mid -1;
                }
            }
        }
        return -1;
    }

    /**
     * xoa 1 tu khoi dictionaries
     * @param wordTarget tu can xoa
     */
    public void removeWord(String wordTarget) {
        int index = dictionaryLookup(wordTarget);
        final Word remove = dictionary.wordList.remove(index);
    }

    /**
     * ham de sua nghia cua 1 tu
     * @param wordTarget tu can sua nghia
     * @param wordExplain nghia moi
     */
    public void editWordExplain(String wordTarget, String wordExplain) {
        int index = dictionaryLookup(wordTarget);
        Word _word = new Word(wordTarget, wordExplain);
        dictionary.wordList.set(index, _word);
    }
}
