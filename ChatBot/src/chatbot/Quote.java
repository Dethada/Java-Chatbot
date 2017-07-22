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

    public void setQuoteText(String quoteText) {
        this.quoteText = quoteText;
    }

    public String getSenderLink() {
        return senderLink;
    }

    public void setSenderLink(String senderLink) {
        this.senderLink = senderLink;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getQuoteLink() {
        return quoteLink;
    }

    public void setQuoteLink(String quoteLink) {
        this.quoteLink = quoteLink;
    }

    public String getQuoteAuthor() {
        return quoteAuthor;
    }

    public void setQuoteAuthor(String quoteAuthor) {
        this.quoteAuthor = quoteAuthor;
    }

    @Override
    public String toString() {
        return "[quoteText = " + quoteText + ", senderLink = " + senderLink + ", senderName = " + senderName + ", quoteLink = " + quoteLink + ", quoteAuthor = " + quoteAuthor + "]";
    }
}
