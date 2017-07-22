/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatbot;

import java.util.Arrays;

/**
 *
 * @author David
 */
public class Items {

    private String timestamp;

    private Index[] index;

    private String update_timestamp;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Index[] getIndex() {
        return index;
    }

    public void setIndex(Index[] index) {
        this.index = index;
    }

    public String getUpdate_timestamp() {
        return update_timestamp;
    }

    public void setUpdate_timestamp(String update_timestamp) {
        this.update_timestamp = update_timestamp;
    }

    @Override
    public String toString() {
        return Arrays.toString(index);
    }
}
