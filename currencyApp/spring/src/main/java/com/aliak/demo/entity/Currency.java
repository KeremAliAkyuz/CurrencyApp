package com.aliak.demo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "currencies")
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE currencies SET deleted = true WHERE id=?")
@FilterDef(name = "deletedCurrencyFilter", parameters = @ParamDef(name = "isDeleted", type = org.hibernate.type.descriptor.java.BooleanJavaType.class))
@Filter(name = "deletedCurrencyFilter", condition = "deleted = :isDeleted")
@Builder
public class Currency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "bank")
    private String bank;

    @Column(name = "currency")
    private String currency;

    @Column(name = "buy")
    private Double buy;

    @Column(name = "sell")
    private Double sell;

    @Column(name="date")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm")
    private LocalDateTime date;

    @Column(name = "deleted")
    private boolean deleted = Boolean.FALSE;

}
