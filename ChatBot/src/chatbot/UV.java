/*
 * UV object
 */
package chatbot;

/**
 *
 * @author David
 */
public class UV {

    private Api_info api_info;

    private Items[] items;

    public Api_info getApi_info() {
        return api_info;
    }

    public void setApi_info(Api_info api_info) {
        this.api_info = api_info;
    }

    public Items[] getItems() {
        return items;
    }

    public void setItems(Items[] items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "[api_info = " + api_info + ", items = " + items + "]";
    }
}
