package com.example.SpringBatchTutorial.core.domain;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.cglib.transform.impl.AccessFieldTransformer;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String name;
    private int age;
    private String phone;

    private String email;

    @OneToMany(mappedBy = "user")
    private List<SendCoupon> userCouponList = new ArrayList<>();
}
