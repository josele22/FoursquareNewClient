
package com.joseantonio.foursquarenewclient.app.rest.Profiles;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;

import java.io.Serializable;

@Generated("org.jsonschema2pojo")
public class Notification implements Serializable {

    @Expose
    private String type;
    @Expose
    private Item item;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

}
