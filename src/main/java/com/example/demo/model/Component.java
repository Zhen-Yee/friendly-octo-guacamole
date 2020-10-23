package com.example.demo.model;

import java.io.Serializable;

public class Component implements Serializable {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
