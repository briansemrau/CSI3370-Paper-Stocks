package com.csi3370.paperstocks.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Portfolio.
 */
@Entity
@Table(name = "portfolio")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Portfolio implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "portfolio")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Share> shares = new HashSet<>();
    @ManyToOne
    @JsonIgnoreProperties("portfolios")
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Portfolio name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Share> getShares() {
        return shares;
    }

    public Portfolio shares(Set<Share> shares) {
        this.shares = shares;
        return this;
    }

    public Portfolio addShare(Share share) {
        this.shares.add(share);
        share.setPortfolio(this);
        return this;
    }

    public Portfolio removeShare(Share share) {
        this.shares.remove(share);
        share.setPortfolio(null);
        return this;
    }

    public void setShares(Set<Share> shares) {
        this.shares = shares;
    }

    public User getUser() {
        return user;
    }

    public Portfolio user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
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
        Portfolio portfolio = (Portfolio) o;
        if (portfolio.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), portfolio.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Portfolio{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
