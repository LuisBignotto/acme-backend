package br.com.acmeairlines.auth.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import java.io.Serializable;

public class AddressModel implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String street;
    private String neighborhood;
    private String zipcode;
    private String number;
    private String complement;
    private String city;
    private String state;
    @JsonBackReference
    private UserModel user;

    public AddressModel(Long id, String street, String neighborhood, String zipcode, String number, String complement, String city, String state, UserModel user) {
        this.id = id;
        this.street = street;
        this.neighborhood = neighborhood;
        this.zipcode = zipcode;
        this.number = number;
        this.complement = complement;
        this.city = city;
        this.state = state;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getComplement() {
        return complement;
    }

    public void setComplement(String complement) {
        this.complement = complement;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }
}
