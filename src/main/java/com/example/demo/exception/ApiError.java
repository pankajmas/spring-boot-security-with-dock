package com.example.demo.exception;


import java.util.Date;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public final class ApiError {

    private int statucode;
    private Date tDate;
    private HttpStatus error;
    private String errormsg;
    private String path;


    public ApiError(int statucode, Date tDate, HttpStatus error, String errormsg, String path) {
        super();
        this.statucode = statucode;
        this.tDate = tDate;
        this.error = error;
        this.errormsg = errormsg;
        this.path = path;
    }


}
