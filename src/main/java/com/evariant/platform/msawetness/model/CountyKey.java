package com.evariant.platform.msawetness.model;

/**
 * Created by Peter Zhou on 06/12/2017.
 */

public class CountyKey {

    private String name;
    private String state;

    public CountyKey(String name, String state) {
        this.name = name;
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        CountyKey county = (CountyKey) o;

        if (this.name.equals(county.name) && this.state.equals(county.state)) {
            return true;
        }

        return false;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (state != null ? state.hashCode() : 0);
        return result;
    }

    public String getName() {
        return name;
    }

    public String getState() {
        return state;
    }

    @Override
    public String toString() {
        return "CountyKey{" + "name='" + name + '\'' + ", state='" + state + '\'' + '}';
    }

}
