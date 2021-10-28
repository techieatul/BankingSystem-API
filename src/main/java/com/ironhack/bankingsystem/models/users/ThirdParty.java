package com.ironhack.bankingsystem.models.users;

import javax.persistence.*;

@Entity
@Table(name = "third_party")
public class ThirdParty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String hashedKey;

    public ThirdParty(String name, String hashedKey) {
        this.name = name;
        this.hashedKey = hashedKey;
    }

    public ThirdParty() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long thirdPartyId) {
        this.id = thirdPartyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHashedKey() {
        return hashedKey;
    }

    public void setHashedKey(String hashKey) {
        this.hashedKey = hashKey;
    }
}
