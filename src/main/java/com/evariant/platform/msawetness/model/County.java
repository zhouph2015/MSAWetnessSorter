package com.evariant.platform.msawetness.model;

/**
 * Created by Peter Zhou on 06/12/2017.
 */

public class County {

    private CountyKey countyKey;
    private int population;
    private String msa;

    public County(CountyKey countyKey, int population, String msa2) {
        this.countyKey = countyKey;
        this.population = population;
        this.msa = msa2;
    }

    public CountyKey getCountyKey() {
        return countyKey;
    }

    public boolean belongMsa() {
        return !msa.isEmpty();
    }

    public int getPopulation() {
        return population;
    }

    public String getMsa() {
        return msa;
    }

}
