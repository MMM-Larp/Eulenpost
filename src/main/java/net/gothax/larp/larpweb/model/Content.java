package net.gothax.larp.larpweb.model;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Content {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false)
    private long id;

    @Column
    private String description;

    @Column
    private boolean allowSignUp;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isAllowSignUp() {
        return allowSignUp;
    }

    public void setAllowSignUp(boolean allowSignUp) {
        this.allowSignUp = allowSignUp;
    }
}
