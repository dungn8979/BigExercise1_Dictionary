import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import javax.speech.AudioException;
import javax.speech.Engine;
import javax.speech.Central;
import javax.speech.EngineException;
import javax.speech.synthesis.SynthesizerModeDesc;
import javax.speech.synthesis.Synthesizer;
import java.util.Locale;

public class DictionaryManagement {
    Dictionary dictionary = new Dictionary();
    SQLutils sqLutils = new SQLutils();
    public void importFromSQL() {
        sqLutils.getConnection(dictionary.wordList);
    }
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

    public void exportToFile() throws IOException {
        File file = new File("directory.txt");
        OutputStream outputStream = new FileOutputStream(file);
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
        for (Word item: dictionary.wordList) {
            outputStreamWriter.write(item.getWord_target() + "\t" + item.getWord_explain());
            outputStreamWriter.write("\n");
        }
        outputStreamWriter.flush();
    }

    public void insertWordToDictionaries(Word word) {
        dictionary.wordList.add(word);
        Collections.sort(dictionary.wordList,Word::compareTo);
    }

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

    public void removeWord(String wordTarget) {
        int index = dictionaryLookup(wordTarget);
        dictionary.wordList.remove(index);
    }

    public void editWordExplain(String wordTarget, String wordExplain) {
        int index = dictionaryLookup(wordTarget);
        Word _word = new Word(wordTarget, wordExplain);
        dictionary.wordList.set(index, _word);
    }

    public List<Word> dictionarySearch(String search) {
        List<Word> matchElement = dictionary.wordList.stream()
                .filter(str -> str.getWord_target().contains(search))
                .collect(Collectors.toList());
        return matchElement;
    }

    public void speakEnglish(String s) {
        try {
            System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
            Central.registerEngineCentral("com.sun.speech.freetts" + ".jsapi.FreeTTSEngineCentral");
            Synthesizer synthesizer = Central.createSynthesizer(new SynthesizerModeDesc(Locale.US));
            synthesizer.allocate();
            synthesizer.resume();
            synthesizer.speakPlainText(s, null);
            synthesizer.waitEngineState(Synthesizer.QUEUE_EMPTY);
            synthesizer.deallocate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
