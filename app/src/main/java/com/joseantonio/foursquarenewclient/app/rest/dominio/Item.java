
package com.joseantonio.foursquarenewclient.app.rest.dominio;

import com.google.gson.annotations.Expose;
import javax.annotation.Generated;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Generated("org.jsonschema2pojo")
public class Item implements Serializable{

    @Expose
    private Reasons reasons;
    @Expose
    private Venue venue;
    @Expose
    private List<Tip> tips = new ArrayList<Tip>();
    @Expose
    private String referralId;

    public Reasons getReasons() {
        return reasons;
    }

    public void setReasons(Reasons reasons) {
        this.reasons = reasons;
    }

    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    public List<Tip> getTips() {
        return tips;
    }

    public void setTips(List<Tip> tips) {
        this.tips = tips;
    }

    public String getReferralId() {
        return referralId;
    }

    public void setReferralId(String referralId) {
        this.referralId = referralId;
    }

}
