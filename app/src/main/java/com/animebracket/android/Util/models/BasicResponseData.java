package com.animebracket.android.Util.models;

import java.io.Serializable;

/**
 * Created by Noah Pederson on 1/28/2015.
 */
public class BasicResponseData implements Serializable{
    //This should have any parameter sent in a POST to the server

    //For nominate
    private int bracketId;
    private String nomineeName;
    private String nomineeSource;
    private String image;
    private boolean verified;


    public int getBracketId() {
        return bracketId;
    }

    public void setBracketId(int bracketId) {
        this.bracketId = bracketId;
    }

    public String getNomineeName() {
        return nomineeName;
    }

    public void setNomineeName(String nomineeName) {
        this.nomineeName = nomineeName;
    }

    public String getNomineeSource() {
        return nomineeSource;
    }

    public void setNomineeSource(String nomineeSource) {
        this.nomineeSource = nomineeSource;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }
}
