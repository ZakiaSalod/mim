package org.jhipster.mim.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import org.jhipster.mim.domain.enumeration.Province;

/**
 * A Event.
 */
@Entity
@Table(name = "event")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Event implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "posted_by", nullable = false)
    private String postedBy;

    @NotNull
    @Column(name = "event_name", nullable = false)
    private String eventName;

    @Lob
    @Column(name = "brief_description")
    private String briefDescription;

    @NotNull
    @Column(name = "event_date", nullable = false)
    private Instant eventDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "province")
    private Province province;

    @NotNull
    @Column(name = "region", nullable = false)
    private String region;

    @Lob
    @Column(name = "address")
    private String address;

    @Column(name = "event_website")
    private String eventWebsite;

    @ManyToOne
    @JsonIgnoreProperties("")
    private User user;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "event_category",
               joinColumns = @JoinColumn(name = "events_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "categories_id", referencedColumnName = "id"))
    private Set<Category> categories = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public Event postedBy(String postedBy) {
        this.postedBy = postedBy;
        return this;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }

    public String getEventName() {
        return eventName;
    }

    public Event eventName(String eventName) {
        this.eventName = eventName;
        return this;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getBriefDescription() {
        return briefDescription;
    }

    public Event briefDescription(String briefDescription) {
        this.briefDescription = briefDescription;
        return this;
    }

    public void setBriefDescription(String briefDescription) {
        this.briefDescription = briefDescription;
    }

    public Instant getEventDate() {
        return eventDate;
    }

    public Event eventDate(Instant eventDate) {
        this.eventDate = eventDate;
        return this;
    }

    public void setEventDate(Instant eventDate) {
        this.eventDate = eventDate;
    }

    public Province getProvince() {
        return province;
    }

    public Event province(Province province) {
        this.province = province;
        return this;
    }

    public void setProvince(Province province) {
        this.province = province;
    }

    public String getRegion() {
        return region;
    }

    public Event region(String region) {
        this.region = region;
        return this;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getAddress() {
        return address;
    }

    public Event address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEventWebsite() {
        return eventWebsite;
    }

    public Event eventWebsite(String eventWebsite) {
        this.eventWebsite = eventWebsite;
        return this;
    }

    public void setEventWebsite(String eventWebsite) {
        this.eventWebsite = eventWebsite;
    }

    public User getUser() {
        return user;
    }

    public Event user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public Event categories(Set<Category> categories) {
        this.categories = categories;
        return this;
    }

    public Event addCategory(Category category) {
        this.categories.add(category);
        category.getEvents().add(this);
        return this;
    }

    public Event removeCategory(Category category) {
        this.categories.remove(category);
        category.getEvents().remove(this);
        return this;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
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
        Event event = (Event) o;
        if (event.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), event.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Event{" +
            "id=" + getId() +
            ", postedBy='" + getPostedBy() + "'" +
            ", eventName='" + getEventName() + "'" +
            ", briefDescription='" + getBriefDescription() + "'" +
            ", eventDate='" + getEventDate() + "'" +
            ", province='" + getProvince() + "'" +
            ", region='" + getRegion() + "'" +
            ", address='" + getAddress() + "'" +
            ", eventWebsite='" + getEventWebsite() + "'" +
            "}";
    }
}
