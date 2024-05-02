package br.com.acmeairlines.auth.model;

import java.io.Serializable;

public class RoleModel implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String roleName;

    public RoleModel(Long id, String roleName) {
        this.id = id;
        this.roleName = roleName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
