package com.example.upark.models;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {
    private String id;
    private String fullName;
    private String userName;
    private Date birthAt;
    private String email;
    private String phoneNumber;
    private char gender;
    private String password;
}
