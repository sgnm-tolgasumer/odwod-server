package org.acme;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class WorkOrder {
    public String workOrderId;
    public String userId;
    public String title;
    public String description;
    public String type;
    public String telephone;
    public String addressCity;
    public String addressDistrict;
    public String openAddress;
    public String workerId;
    public int status;
    private Long id;


    public void setId(Long id) {
        this.id = id;
    }

    @Id
    public Long getId() {
        return id;
    }
}
