package com.myfinance.financetracker.model;

import java.util.Objects;

public class Transaction {

  private Long id;
  private Double amount;
  private String date;
  private String description;

  public Transaction() {
  }

  public Transaction(final Long id, final Double amount, final String date, final String description) {
    this.id = id;
    this.amount = amount;
    this.date = date;
    this.description = description;
  }

  public Long getId() {
    return id;
  }

  public void setId(final Long id) {
    this.id = id;
  }

  public Double getAmount() {
    return amount;
  }

  public void setAmount(final Double amount) {
    this.amount = amount;
  }

  public String getDate() {
    return date;
  }

  public void setDate(final String date) {
    this.date = date;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(final String description) {
    this.description = description;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, date);
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Transaction)) {
      return false;
    }
    Transaction other = (Transaction) obj;
    return Objects.equals(id, other.id) && Objects.equals(date, other.date);
  }

  @Override
  public String toString() {
    return "Transaction [id=" + id + ", amount=" + amount + ", date=" + date + ", description=" + description + "]";
  }
}