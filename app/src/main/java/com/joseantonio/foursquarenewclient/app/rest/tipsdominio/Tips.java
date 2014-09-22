
package com.joseantonio.foursquarenewclient.app.rest.tipsdominio;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Tips {

    @Expose
    private Integer count;
    @Expose
    private List<Item_> items = new ArrayList<Item_>();

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<Item_> getItems() {
        return items;
    }

    public void setItems(List<Item_> items) {
        this.items = items;
    }
}
