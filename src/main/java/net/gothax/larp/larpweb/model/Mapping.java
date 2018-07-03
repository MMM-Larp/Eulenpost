package net.gothax.larp.larpweb.model;

import org.springframework.data.repository.cdi.Eager;

import javax.persistence.*;
import java.util.List;

@Entity
public class Mapping {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false)
    private Long id;

    @OneToOne
    private Entry sender;

    @Column
    @OneToMany(fetch = FetchType.EAGER)
    private List<Entry> receivers;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Entry getSender() {
        return sender;
    }

    public void setSender(Entry sender) {
        this.sender = sender;
    }

    public List<Entry> getReceivers() {
        return receivers;
    }

    public void setReceivers(List<Entry> receivers) {
        this.receivers = receivers;
    }
}
