package com.ironhack.bankingsystem.utils;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.ironhack.bankingsystem.controllers.dtos.*;
import org.springframework.security.crypto.bcrypt.*;
import org.springframework.security.crypto.password.*;

import java.math.*;
import java.time.*;
import java.time.temporal.*;
import java.util.*;

public class PwdUtils {
    public static void main(String[] args) throws JsonProcessingException {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


        System.out.println(passwordEncoder.encode("1234"));


    }
}
