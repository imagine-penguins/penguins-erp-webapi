package com.knackitsolutions.crm.imaginepenguins.dbservice.entity;

import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.DashboardViewType;

import javax.persistence.*;

@Entity
@Table(name = "dashboards")
public class Dashboard {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "api")
    private String api;

    @Column(name = "type")
    private DashboardViewType type;

    @ManyToOne
    @JoinColumn(name = "privilege_id")
    private Privilege privilege;

    @Column(name = "default_location")
    private Integer defaultLocation;

    public Dashboard() {
    }

    public Dashboard(Long id, String name, String api, DashboardViewType type, Privilege privilege, Integer defaultLocation) {
        this.id = id;
        this.name = name;
        this.api = api;
        this.type = type;
        this.privilege = privilege;
        this.defaultLocation = defaultLocation;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public Privilege getPrivilege() {
        return privilege;
    }

    public void setPrivilege(Privilege privilege) {
        this.privilege = privilege;
    }

    public Integer getDefaultLocation() {
        return defaultLocation;
    }

    public void setDefaultLocation(Integer defaultLocation) {
        this.defaultLocation = defaultLocation;
    }

    public DashboardViewType getType() {
        return type;
    }

    public void setType(DashboardViewType type) {
        this.type = type;
    }
}
