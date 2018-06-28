package net.gothax.larp.larpweb.model;

import javax.persistence.*;

@Entity
public class Mapping {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false)
    private Long id;

    @Column
    private Entry sender;

    @Column
    private Entry receiverOne;

    @Column
    private Entry receiverTwo;

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

    public Entry getReceiverOne() {
        return receiverOne;
    }

    public void setReceiverOne(Entry receiverOne) {
        this.receiverOne = receiverOne;
    }

    public Entry getReceiverTwo() {
        return receiverTwo;
    }

    public void setReceiverTwo(Entry receiverTwo) {
        this.receiverTwo = receiverTwo;
    }
}
