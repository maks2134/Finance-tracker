package com.myfinance.financetracker.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "subscriptions")
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String subscriptionType; // Например, "MONTHLY", "YEARLY"
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String status; // Например, "ACTIVE", "CANCELLED", "EXPIRED"

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Subscription() {
    }

    public Subscription(String subscriptionType, LocalDateTime startDate,
                        LocalDateTime endDate, String status, User user) {
        this.subscriptionType = subscriptionType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.user = user;
    }

    // Геттеры и сеттеры

    public Long getId() {
        return id;
    }

    public String getSubscriptionType() {
        return subscriptionType;
    }

    public void setSubscriptionType(String subscriptionType) {
        this.subscriptionType = subscriptionType;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Subscription)) {
            return false;
        }
        Subscription that = (Subscription) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "Subscription{"
            +
            "id="
            + id
            +
            ", subscriptionType='"
            + subscriptionType
            + '\''
            +
            ", startDate="
            + startDate
            +
            ", endDate="
            + endDate
            +
            ", status='"
            + status
            + '\''
            +
            '}';
    }
}