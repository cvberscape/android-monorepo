
package com.example.android2023ej.classes.weatherstation;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class OunasvaaraData {

    @SerializedName("d")
    @Expose
    private D d;
    @SerializedName("ts")
    @Expose
    private String ts;

    /**
     * No args constructor for use in serialization
     * 
     */
    public OunasvaaraData() {
    }

    /**
     * 
     * @param d
     * @param ts
     */
    public OunasvaaraData(D d, String ts) {
        super();
        this.d = d;
        this.ts = ts;
    }

    public D getD() {
        return d;
    }

    public void setD(D d) {
        this.d = d;
    }

    public OunasvaaraData withD(D d) {
        this.d = d;
        return this;
    }

    public String getTs() {
        return ts;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }

    public OunasvaaraData withTs(String ts) {
        this.ts = ts;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(OunasvaaraData.class.getName()).append('@').append(Double.toHexString(System.identityHashCode(this))).append('[');
        sb.append("d");
        sb.append('=');
        sb.append(((this.d == null)?"<null>":this.d));
        sb.append(',');
        sb.append("ts");
        sb.append('=');
        sb.append(((this.ts == null)?"<null>":this.ts));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
