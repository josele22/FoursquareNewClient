
package com.joseantonio.foursquarenewclient.app.rest.Profiles;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;

@Generated("org.jsonschema2pojo")
public class Response implements Serializable {

    @Expose
    private List<Result> results = new ArrayList<Result>();
    @Expose
    private Unmatched unmatched;

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    public Unmatched getUnmatched() {
        return unmatched;
    }

    public void setUnmatched(Unmatched unmatched) {
        this.unmatched = unmatched;
    }

}
