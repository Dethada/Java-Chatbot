/*
 * Api_info object (Within UV)
 */
package chatbot;

/**
 *
 * @author David
 */
public class Api_info {

    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "UV [status = " + status + "]";
    }
}
