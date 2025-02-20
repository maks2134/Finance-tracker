package com.myfinance.financetracker.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Budget {
	private Long id;
	private String name;
	private Double limit;
	private List<Category> categories = new ArrayList<>();

	public Budget(Long id, String name, Double limit) {
		this.id = id;
		this.name = name;
		this.limit = limit;
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

	public Double getLimit() {
		return limit;
	}

	public void setLimit(Double limit) {
		this.limit = limit;
	}

	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Budget)) return false;
		Budget budget = (Budget) o;
		return Objects.equals(getId(), budget.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId());
	}

	@Override
	public String toString() {
		return "Budget{" +
			"id=" + id +
			", name='" + name + '\'' +
			", limit=" + limit +
			'}';
	}
}