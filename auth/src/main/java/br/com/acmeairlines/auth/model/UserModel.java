package br.com.acmeairlines.auth.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class UserModel implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String email;
    private String cpf;
    private String name;
    private String password;
    private String phone;
    @JsonManagedReference
    private AddressModel address;
    private Set<RoleModel> roles = new HashSet<>();

    public UserModel(Long id, String email, String cpf, String name, String password, String phone, AddressModel address, Set<RoleModel> roles) {
        this.id = id;
        this.email = email;
        this.cpf = cpf;
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.address = address;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public AddressModel getAddress() {
        return address;
    }

    public void setAddress(AddressModel address) {
        this.address = address;
    }

    public Set<RoleModel> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleModel> roles) {
        this.roles = roles;
    }
}
