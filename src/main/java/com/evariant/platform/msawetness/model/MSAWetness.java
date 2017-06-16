package com.evariant.platform.msawetness.model;
/**
 * In the United States, a metropolitan statistical area (MSA) is a geographical
 * region with a relatively
 * high population density at its core and close economic ties throughout the
 * area. It may have one or
 * many counties.
 */

/**
 * Created by Peter Zhou on 06/12/2017.
 */

public class MSAWetness implements Comparable<MSAWetness> {

    private String MSAname;
    private Double wetness;

    public MSAWetness(String msa, Double wetness) {
        this.MSAname = msa;
        this.wetness = wetness;
    }

    @Override
    public int compareTo(MSAWetness o) {
        return this.wetness.compareTo(o.wetness);
    }

    public String getMsa() {
        return MSAname;
    }

    public void setMsa(String msa) {
        this.MSAname = msa;
    }

    public Double getWetness() {
        return wetness;
    }

    public void setWetness(Double wetness) {
        this.wetness = wetness;
    }

    @Override
    public String toString() {
        return "MSAWetness{" + "MSA='" + MSAname + '\'' + ", Wetness=" + wetness + '}';
    }
}