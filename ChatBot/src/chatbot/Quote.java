/*
 * To store quote API's JSON data 
 */
package chatbot;

/**
 *
 * @author David
 */
public class Quote {

    private String quoteText;
    private String senderLink;
    private String senderName;
    private String quoteLink;
    private String quoteAuthor;

    public String getQuoteText() {
        return quoteText;
    }

    public String getSenderLink() {
        return senderLink;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getQuoteLink() {
        return quoteLink;
    }

    public String getQuoteAuthor() {
        return quoteAuthor;
    }
}
