package com.myfinance.financetracker.model;

import java.util.Objects;

public class Category {
    private Long id;
    private String name;

    public Category(Long id, String name) {
        this.id = id;

        this.name = name;
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

    @Override
  public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Category)) {
            return false;
        }
        Category category = (Category) o;
        return Objects.equals(getId(), category.getId());
    }

    @Override
  public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
  public String toString() {
        return "Category [id=" + id + ", name=" + name + "]";
    }
}