/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.snehasish.blog.service;

/**
 *
 * @author snehasish
 */
public interface EmailService {

    void sendSimpleMailMessage(String from, String to, String subject, String name, String blogUsername, String blogPassword);

    void sendMimeMessageWithAttachment(String from, String to, String subject, String text, String filePath);
}
