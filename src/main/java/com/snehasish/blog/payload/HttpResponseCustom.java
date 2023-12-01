/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.snehasish.blog.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Map;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

/**
 *
 * @author snehasish
 */
@Data
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class HttpResponseCustom {

    private String timeStamp;
    private Integer statusCode;
    private HttpStatus status;
    private String message;
    private String DeveloperMessage;
    private String path;
    private String requestMethod;
    private Map<?, ?> payload;
}
