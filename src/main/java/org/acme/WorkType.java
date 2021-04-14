package org.acme;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;

@Entity
public class WorkType extends PanacheEntity {
    public String workType;
}
