package se.app.vocabulary.model;

public class Connection {

    private int word_id;
    private int vocabulary_id;

    public Connection(int word_id, int vocabulary_id) {
        this.word_id = word_id;
        this.vocabulary_id = vocabulary_id;
    }

    public int getWord_id() {
        return word_id;
    }

    public void setWord_id(int word_id) {
        this.word_id = word_id;
    }

    public int getVocabulary_id() {
        return vocabulary_id;
    }

    public void setVocabulary_id(int vocabulary_id) {
        this.vocabulary_id = vocabulary_id;
    }
}
