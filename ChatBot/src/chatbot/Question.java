/*
 * Question object
 */
package chatbot;

import java.io.Serializable;

/**
 *
 * @author David
 */
public class Question implements Serializable {

    private String question;
    private String[] answers;

    public Question(String q, String a) {
        this.question = q;
        this.answers = a.split(", ");
    }

    public void setQuestion(String q) {
        this.question = q;
    }

    public String getQuestion() {
        return question;
    }

    public void setAnswer(String a) {
        this.answers = a.split(", ");
    }

    public String[] getAnswer() {
        return answers;
    }
}
