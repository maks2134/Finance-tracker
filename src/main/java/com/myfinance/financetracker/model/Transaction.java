package com.myfinance.financetracker.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount;

    // Можно хранить дату как строку или использовать java.time.LocalDate (рекомендуется)
    private String date;

    private String description;

    // Связь многие ко одному с бюджетом
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "budget_id")
    private Budget budget;

    public Transaction() {
    }

    public Transaction(Double amount, String date, String description, Budget budget) {
        this.amount = amount;
        this.date = date;
        this.description = description;
        this.budget = budget;
    }

    // Геттеры и сеттеры

    public Long getId() {
        return id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Budget getBudget() {
        return budget;
    }

    public void setBudget(Budget budget) {
        this.budget = budget;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Transaction)) {
            return false;
        }
        Transaction that = (Transaction) o;
        return Objects.equals(getId(), that.getId())
            &&
            Objects.equals(getDate(), that.getDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getDate());
    }

    @Override
    public String toString() {
        return "Transaction{"
            +
            "id="
            + id
            +
            ", amount="
            + amount
            +
            ", date='"
            + date
            + '\''
            +
            ", description='"
            + description
            + '\''
            +
            '}';
    }
}