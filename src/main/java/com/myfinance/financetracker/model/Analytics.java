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
@Table(name = "analytics")
public class Analytics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime analysisDate;
    private String analysisResult; // Результат анализа в виде JSON или текста
    private String recommendations; // Рекомендации по сокращению расходов

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Analytics() {
    }

    public Analytics(LocalDateTime analysisDate,
                     String analysisResult, String recommendations, User user) {
        this.analysisDate = analysisDate;
        this.analysisResult = analysisResult;
        this.recommendations = recommendations;
        this.user = user;
    }

    // Геттеры и сеттеры

    public Long getId() {
        return id;
    }

    public LocalDateTime getAnalysisDate() {
        return analysisDate;
    }

    public void setAnalysisDate(LocalDateTime analysisDate) {
        this.analysisDate = analysisDate;
    }

    public String getAnalysisResult() {
        return analysisResult;
    }

    public void setAnalysisResult(String analysisResult) {
        this.analysisResult = analysisResult;
    }

    public String getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(String recommendations) {
        this.recommendations = recommendations;
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
        if (!(o instanceof Analytics)) {
            return false;
        }
        Analytics analytics = (Analytics) o;
        return Objects.equals(getId(), analytics.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "Analytics{"
            +
            "id="
            + id
            +
            ", analysisDate="
            + analysisDate
            +
            ", analysisResult='"
            + analysisResult
            + '\''
            +
            ", recommendations='"
            + recommendations
            + '\''
            +
            '}';
    }
}