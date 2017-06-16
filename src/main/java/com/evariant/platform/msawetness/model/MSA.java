package com.evariant.platform.msawetness.model;

import java.util.HashSet;
import java.util.Set;

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

public class MSA {

    private String name;

    private Set<CountyKey> listOfCounty = new HashSet<>();

    public MSA(String name) {
        super();
        this.name = name;
        this.listOfCounty = new HashSet<>();

    }

    public MSA(String name, Set<CountyKey> listOfCounty) {
        super();
        this.name = name;
        this.listOfCounty = listOfCounty;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<CountyKey> getListOfCounty() {
        return listOfCounty;
    }

    public void setListOfCounty(Set<CountyKey> listOfCounty) {
        this.listOfCounty = listOfCounty;
    }

    public void addCounty(CountyKey countyKey) {
        listOfCounty.add(countyKey);
    }

    public boolean containThisCountyKey(CountyKey countyKey) {
        return listOfCounty.contains(countyKey);
    }
}
