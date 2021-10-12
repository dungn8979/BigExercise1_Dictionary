package Solution;

public class Word {
    private String word_target;
    private String word_explain;

    public Word() {
    }

    public Word(String word_target, String word_explain) {
        this.word_target = word_target;
        this.word_explain = word_explain;
    }

    public String getWord_target() {
        return word_target;
    }

    public void setWord_target(String word_target) {
        this.word_target = word_target;
    }

    public String getWord_explain() {
        return word_explain;
    }

    public void setWord_explain(String word_explain) {
        this.word_explain = word_explain;
    }

    /**
     * ham so sanh tu hien tai voi 1 tu khac dc nhap vao
     * @param word tu can so sanh
     * @return 0 neu 2 tu giong, 1 neu tu them vao dung sau, -1 neu tu them vao dung truoc
     */

    public int compareTo(Word word) {
      return this.getWord_target().compareTo(word.word_target);
    }
}
