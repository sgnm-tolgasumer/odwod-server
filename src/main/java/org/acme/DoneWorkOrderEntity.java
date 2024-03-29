package org.acme;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name = "workorder_done")
public class DoneWorkOrderEntity extends PanacheEntity {
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
}
