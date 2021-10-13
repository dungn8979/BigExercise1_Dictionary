package Solution;

import Solution.Word;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import javax.speech.Engine;
import javax.speech.Central;
import javax.speech.synthesis.SynthesizerModeDesc;
import javax.speech.synthesis.Synthesizer;

/**
 * class quan li tu dien
 */
public class DictionaryManagement {
    List<Word> dictionary_list = new ArrayList<Word>();

    /**
     * để file ở ngoài cùng project.
     * @param path String
     * @return isSuccess
     * @throws FileNotFoundException
     */
    public boolean  insertFromFile(String path) throws FileNotFoundException {
        boolean success = true;
        try {
            File file = new File(path);
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
                dictionary_list.add(word);
                scanner1.close();
            }
            scanner.close();
        } catch (Exception exp) {
            success = false;
            System.out.println(exp.toString());
        }
        return success;
    }

    /**
     * xuat tu dien ra file path
     * @throws IOException
     */
    public boolean exportToFile(String path) throws IOException {
        boolean success = true;
        try {
            File file = new File(path);
            OutputStream outputStream = new FileOutputStream(file);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
            for (Word item: dictionary_list) {
                outputStreamWriter.write(item.getWord_target() + "\t" + item.getWord_explain());
                outputStreamWriter.write("\n");
            }
            outputStreamWriter.flush();
        } catch (Exception exp) {
            success = false;
            System.out.println(exp.toString());
        }
        return success;
    }

    /**
     * them tu vao dictionary
     * @param word
     */

    public void insertWordToDictionaries(Word word) {
        dictionary_list.add(word);
        Collections.sort(dictionary_list,Word::compareTo);
    }

    /**
     * tim kiem theo wordTargat
     * @param wordTarget
     * @return index
     */
    public int dictionaryLookup(String wordTarget) {
        int left = 0;
        int right = dictionary_list.size() - 1;
        while (left <= right) {
            int mid = (left + right) / 2;
            String temp = dictionary_list.get(mid).getWord_target();
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
        dictionary_list.remove(index);
    }

    /**
     * sua nghia cua tu
     * @param wordTarget
     * @param wordExplain
     */
    public void editWordExplain(String wordTarget, String wordExplain) {
        int index = dictionaryLookup(wordTarget);
        Word _word = new Word(wordTarget, wordExplain);
        dictionary_list.set(index, _word);
    }

    /**
     * show all
     */
    public void showWordList() {
        for (Word item: dictionary_list) {
            System.out.println(item.getWord_target()+" "+item.getWord_explain());
        }
    }

    /**
     * ham search nhe nhe
     * @param search
     * @return list tu search duoc
     */
    public List<Word> dictionarySearch(String search) {
        List<Word> matchElement = new ArrayList<Word>();
        for (Word item: dictionary.wordList) {
            int index = item.getWord_target().indexOf(search);
            if (index == 0) {
                matchElement.add(item);
            }
        }
        return matchElement;
    }

    /**
     * phat am.
     * @param s tu can doc
     */
    public void speakEnglish(String s) {
        try {
            System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
            Central.registerEngineCentral("com.sun.speech.freetts" + ".jsapi.FreeTTSEngineCentral");
            Synthesizer synthesizer = Central.createSynthesizer(new SynthesizerModeDesc(Locale.US));
            synthesizer.allocate();
            synthesizer.resume();
            synthesizer.speakPlainText(s, null);
            synthesizer.waitEngineState(Synthesizer.QUEUE_EMPTY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}