package com.sofactory.capitalcycles.cycletrip.VO;

/**
 * Created by LuisSebastian on 11/23/15.
 */
public class Feature {

    private String feature;
    private String value;

    public Feature(String feature, String value) {
        this.feature = feature;
        this.value = value;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
