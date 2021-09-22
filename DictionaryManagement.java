import java.io.*;
import java.util.Scanner;
import java.lang.String;

public class DictionaryManagement {
    Dictionary dictionary = new Dictionary();
    Word word = new Word();

    /**
     * them 1 word tu file vao list
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
     * tim nghia cua 1 tu
     * @param wordTarget tu can tim
     * @return nghia cua tu
     */
    public String dictionaryLookup(String wordTarget) {
        String wordExplain = "";
        for (int i = 0; i < dictionary.wordList.size(); i++) {
            if (wordTarget == dictionary.wordList.get(i).getWord_target()) {
                wordExplain = dictionary.wordList.get(i).getWord_explain();
                break;
            }
        }
        return wordExplain;
    }
}
