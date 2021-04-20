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


    @Override
    public String toString() {
        return "Worker{" +
                "userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", telephone='" + telephone + '\'' +
                ", mail='" + mail + '\'' +
                ", birthDate='" + birthDate + '\'' +
                ", addressCity='" + addressCity + '\'' +
                ", workableDistricts='" + workableDistricts + '\'' +
                ", jobTypes='" + jobTypes + '\'' +
                '}';
    }
}
