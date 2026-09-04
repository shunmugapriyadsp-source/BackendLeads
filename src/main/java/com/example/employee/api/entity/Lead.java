package com.example.employee.api.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "lead_list")
@Data
public class Lead {

    @Id
    private String id;

    private String branchCode;
    private String chanelType;
    private String cityCode;
    private String conversationId;
    private LocalDateTime createdAt;
    private String customeId;
    private String email;
    private String firstName;
    private String lastName;
    private String mobileNumber;
    private String pincode;
    private String productCode;
    private String productName;


}