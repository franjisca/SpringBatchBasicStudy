package com.example.SpringBatchTutorial.core.domain;


import jakarta.persistence.*;
import jdk.jfr.Timestamp;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Date;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Getter
public class SendCoupon {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long c_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private User user;

    @CreationTimestamp
    private Date issudate;

    @Column(columnDefinition = "boolean default false")
    private boolean isUsed;

    public SendCoupon(User user) {
        this.user = user;
    }
}
