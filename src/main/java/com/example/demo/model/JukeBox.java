package com.example.demo.model;

import java.io.Serializable;
import java.util.List;

public class JukeBox implements Serializable {

    private String id;
    private String model;
    private List<Component> components;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<Component> getComponents() {
        return components;
    }

    public void setComponents(List<Component> components) {
        this.components = components;
    }
}
