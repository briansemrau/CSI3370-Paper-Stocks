package com.csi3370.paperstocks.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A Transaction.
 */
@Entity
@Table(name = "transaction")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Transaction implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "ticker", nullable = false)
    private String ticker;

    @NotNull
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @NotNull
    @Column(name = "price_per_share", nullable = false)
    private Double pricePerShare;

    @NotNull
    @Column(name = "jhi_date", nullable = false)
    private Instant date;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("transactions")
    private Portfolio portfolio;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTicker() {
        return ticker;
    }

    public Transaction ticker(String ticker) {
        this.ticker = ticker;
        return this;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Transaction quantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPricePerShare() {
        return pricePerShare;
    }

    public Transaction pricePerShare(Double pricePerShare) {
        this.pricePerShare = pricePerShare;
        return this;
    }

    public void setPricePerShare(Double pricePerShare) {
        this.pricePerShare = pricePerShare;
    }

    public Instant getDate() {
        return date;
    }

    public Transaction date(Instant date) {
        this.date = date;
        return this;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public Portfolio getPortfolio() {
        return portfolio;
    }

    public Transaction portfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
        return this;
    }

    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Transaction transaction = (Transaction) o;
        if (transaction.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), transaction.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Transaction{" +
            "id=" + getId() +
            ", ticker='" + getTicker() + "'" +
            ", quantity=" + getQuantity() +
            ", pricePerShare=" + getPricePerShare() +
            ", date='" + getDate() + "'" +
            "}";
    }
}
