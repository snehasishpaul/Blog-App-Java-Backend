/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.snehasish.blog.util;

import com.snehasish.blog.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author snehasish
 */
public class EmailUtil {

    public static String getText(String name, String blogUsername, String blogPassword) {
        String text = "Hello " + name + "\n\nNew Account has been created with this EMAIL. \n\nYour Username: " + blogUsername + "\nYour Password: " + blogPassword + "\n\nThanks,\nBlog App Dev.";

        return text;

    }
}
