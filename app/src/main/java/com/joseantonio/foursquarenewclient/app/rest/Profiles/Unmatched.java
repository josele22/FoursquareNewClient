
package com.joseantonio.foursquarenewclient.app.rest.Profiles;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;

@Generated("org.jsonschema2pojo")
public class Unmatched implements Serializable {

    @Expose
    private List<Object> email = new ArrayList<Object>();
    @Expose
    private List<Object> name = new ArrayList<Object>();

    public List<Object> getEmail() {
        return email;
    }

    public void setEmail(List<Object> email) {
        this.email = email;
    }

    public List<Object> getName() {
        return name;
    }

    public void setName(List<Object> name) {
        this.name = name;
    }

}
