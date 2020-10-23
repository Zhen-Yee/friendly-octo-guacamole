package com.example.demo.model;

import java.io.Serializable;
import java.util.List;

public class Setting implements Serializable {

    private String id;
    private List<String> requires;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getRequires() {
        return requires;
    }

    public void setRequires(List<String> requires) {
        this.requires = requires;
    }
}
