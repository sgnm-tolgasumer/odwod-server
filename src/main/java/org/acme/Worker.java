package org.acme;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import javax.persistence.Entity;

@Entity
public class Worker extends PanacheEntity {
    public String userId;
    public String name;
    public String surname;
    public String telephone;
    public String mail;
    public String birthDate;
    public String addressCity;
    public String workableDistricts;
    public String jobTypes;

}
