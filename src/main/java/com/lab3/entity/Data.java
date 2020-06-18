package com.lab3.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "data")
public class Data implements Serializable {
    public static final String PATIENT_TYPE = "patient";
    public static final String NURSE_TYPE = "nurse";
    public static final String DOCTOR_TYPE = "doctor";

    public static final int ONLY_DOC_TREATMENT = 2;
    public static final String[] TREATMENTS = new String[]{"Procedures", "Drugs", "Operation"};

    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    private Long id;
    @Column(name = "type")
    private String type;
    @Column(name = "issick")
    private Boolean isSick;
    @Column(name = "sickness")
    private String sickness;
    @Column(name = "treatment")
    private String treatment;
    @Column(name = "sicknesshistory")
    private String sicknessHistory;

    @OneToOne
    private User user;
}
