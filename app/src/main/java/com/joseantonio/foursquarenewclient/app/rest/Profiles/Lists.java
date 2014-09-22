
package com.joseantonio.foursquarenewclient.app.rest.Profiles;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;

@Generated("org.jsonschema2pojo")
public class Lists implements Serializable {

    @Expose
    private List<Group> groups = new ArrayList<Group>();

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

}
