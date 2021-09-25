import java.io.*;
import java.util.Collections;
import java.util.Objects;
import java.util.Scanner;

/**
 * class quan li tu dien
 */
public class DictionaryManagement {
    Dictionary dictionary = new Dictionary();

    /**
     * nhap tu dien tu file
     * @throws FileNotFoundException
     */

    public void  insertFromFile() throws FileNotFoundException {
        File file = new File("dictionary.txt");
        Scanner scanner = new Scanner(file);
        String temp;
        String targetTemp;
        String explainTemp;

        while (scanner.hasNextLine()) {
            temp = scanner.nextLine();
            Scanner scanner1 = new Scanner(temp).useDelimiter("\t");
            targetTemp = scanner1.next();
            explainTemp = scanner1.next();
            Word word = new Word(targetTemp,explainTemp);
            dictionary.wordList.add(word);
            scanner1.close();
        }
        scanner.close();
    }

    /**
     * xuat tu dien ra file
     * @throws IOException
     */

    public void exportToFile() throws IOException {
        File file = new File("directories.txt");
        OutputStream outputStream = new FileOutputStream(file);
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
        for (Word item: dictionary.wordList) {
            outputStreamWriter.write(item.getWord_target() + "\t" + item.getWord_explain());
            outputStreamWriter.write("\n");
        }
        outputStreamWriter.flush();
    }

    /**
     * them tu vao dictionary
     * @param word
     */

    public void insertWordToDictionaries(Word word) {
        dictionary.wordList.add(word);
        Collections.sort(dictionary.wordList,Word::compareTo);
    }

    /**
     * tim kiem theo wordTargat
     * @param wordTarget
     * @return index
     */
    public int dictionaryLookup(String wordTarget) {
        int left = 0;
        int right = dictionary.wordList.size() - 1;
        while (left <= right) {
            int mid = (left + right) / 2;
            String temp = dictionary.wordList.get(mid).getWord_target();
            if (Objects.equals(wordTarget, temp)) {
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
     * xoa tu khoi dictionary
     * @param wordTarget
     */
    public void removeWord(String wordTarget) {
        int index = dictionaryLookup(wordTarget);
        dictionary.wordList.remove(index);
    }

    /**
     * sua nghia cua tu
     * @param wordTarget
     * @param wordExplain
     */
    public void editWordExplain(String wordTarget, String wordExplain) {
        int index = dictionaryLookup(wordTarget);
        Word _word = new Word(wordTarget, wordExplain);
        dictionary.wordList.set(index, _word);
    }

    /**
     * show all
     */
    public void showWordList() {
        for (Word item: dictionary.wordList) {
            System.out.println(item.getWord_target()+" "+item.getWord_explain());
        }
    }
}
