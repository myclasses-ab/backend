package com.classes.Backend.Domain.master;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name = "cities")
public class City {

    @Id
    @Column(name = "identifier", unique = true)
    private String identifier = UUID.randomUUID().toString();

    @Column(name = "name", length = 200)
    private String name;

    @Column(name = "state", length = 200)
    private String state;

    @Column(name = "state_code", length = 10)
    private String stateCode;

    @Column(name = "pincode_prefix", length = 10)
    private String pincodePrefix;

    @Column(name = "is_metro")
    private Boolean isMetro = false;

    @Column(name = "latitude", precision = 10, scale = 7)
    private BigDecimal latitude;

    @Column(name = "longitude", precision = 10, scale = 7)
    private BigDecimal longitude;
}
