package org.acme;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;

@Entity
public class District extends PanacheEntity {
    public String districtName;


    public District(String districtName) {
        this.districtName = districtName;
    }

    public District() {

    }
}
