package com.myfinance.financetracker.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "budgets")
public class Budget {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// Название бюджета
	private String name;

	// Изначальный лимит бюджета (не изменяется)
	@Column(name = "limit_amount")
	private Double limitAmount;

	// Сумма расходов, накопленная по бюджету.
	// При создании новой транзакции это значение будет увеличиваться.
	private Double spent = 0.0;

	// Связь многие-ко-многим с категориями
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
	@JoinTable(name = "budget_category",
		joinColumns = @JoinColumn(name = "budget_id"),
		inverseJoinColumns = @JoinColumn(name = "category_id"))
	private List<Category> categories = new ArrayList<>();

	// Связь один ко многим с транзакциями
	@OneToMany(mappedBy = "budget", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Transaction> transactions = new ArrayList<>();

	public Budget() {
	}

	public Budget(String name, Double limitAmount) {
		this.name = name;
		this.limitAmount = limitAmount;
	}

	// Вычисляем остаток бюджета (лимит - уже потрачено)
	public Double getRemaining() {
		return limitAmount - spent;
	}

	// Геттеры и сеттеры

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getLimitAmount() {
		return limitAmount;
	}

	public void setLimitAmount(Double limitAmount) {
		this.limitAmount = limitAmount;
	}

	public Double getSpent() {
		return spent;
	}

	public void setSpent(Double spent) {
		this.spent = spent;
	}

	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}

	public List<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
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
			", limitAmount=" + limitAmount +
			", spent=" + spent +
			'}';
	}
}