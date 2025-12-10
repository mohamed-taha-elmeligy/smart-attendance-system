package com.emts.smart_attendance_system.dataset;

import com.emts.smart_attendance_system.entities.Course;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

/**
 * *******************************************************************
 * File: CourseSet.java
 * Package: com.emts.smart_attendance_system.dataset
 * Project: eMTS Smart Attendance System
 * © ٢٠٢٥ Mohamed Taha Elmeligy - eMTS (e Modern Tech Solutions)
 * This file is part of the eMTS Smart Attendance System.
 * Created on: 06/12/2025
 * Port Number: 8083
 * *******************************************************************
 */

@Component
public class CourseSet {

    public Flux<Course> courseSet(){
        return Flux.just(
                Course.builder()
                        .code("CS101")
                        .name("Introduction to Computer Science")
                        .description("Basic concepts of computer science and programming")
                        .build(),
                Course.builder()
                        .code("CS102")
                        .name("Data Structures")
                        .description("Study of arrays, linked lists, stacks, queues, and trees")
                        .build(),
                Course.builder()
                        .code("CS103")
                        .name("Algorithms")
                        .description("Algorithm design and analysis techniques")
                        .build(),
                Course.builder()
                        .code("CS104")
                        .name("Database Systems")
                        .description("SQL and relational database design fundamentals")
                        .build(),
                Course.builder()
                        .code("CS105")
                        .name("Web Development")
                        .description("HTML, CSS, JavaScript and web application development")
                        .build(),
                Course.builder()
                        .code("CS106")
                        .name("Object-Oriented Programming")
                        .description("OOP concepts including inheritance, polymorphism, and encapsulation")
                        .build(),
                Course.builder()
                        .code("CS107")
                        .name("Operating Systems")
                        .description("Process management, memory management, and file systems")
                        .build(),
                Course.builder()
                        .code("CS108")
                        .name("Computer Networks")
                        .description("Network protocols, TCP/IP, and network security basics")
                        .build(),
                Course.builder()
                        .code("CS109")
                        .name("Software Engineering")
                        .description("Software development lifecycle and design patterns")
                        .build(),
                Course.builder()
                        .code("CS110")
                        .name("Machine Learning")
                        .description("Supervised and unsupervised learning algorithms")
                        .build(),
                Course.builder()
                        .code("CS111")
                        .name("Artificial Intelligence")
                        .description("AI concepts, search algorithms, and knowledge representation")
                        .build(),
                Course.builder()
                        .code("CS112")
                        .name("Cybersecurity")
                        .description("Security principles, encryption, and threat analysis")
                        .build(),
                Course.builder()
                        .code("CS113")
                        .name("Cloud Computing")
                        .description("Cloud services, virtualization, and distributed systems")
                        .build(),
                Course.builder()
                        .code("CS114")
                        .name("Mobile App Development")
                        .description("Development of mobile applications for iOS and Android")
                        .build(),
                Course.builder()
                        .code("CS115")
                        .name("Compiler Design")
                        .description("Lexical analysis, parsing, and code generation")
                        .build(),
                Course.builder()
                        .code("CS116")
                        .name("Graphics and Visualization")
                        .description("Computer graphics, rendering, and data visualization")
                        .build(),
                Course.builder()
                        .code("CS117")
                        .name("Natural Language Processing")
                        .description("Text processing, sentiment analysis, and language models")
                        .build(),
                Course.builder()
                        .code("CS118")
                        .name("DevOps and Continuous Integration")
                        .description("CI/CD pipelines, containerization, and infrastructure automation")
                        .build(),
                Course.builder()
                        .code("CS119")
                        .name("Blockchain Technology")
                        .description("Cryptocurrency, smart contracts, and distributed ledgers")
                        .build(),
                Course.builder()
                        .code("CS120")
                        .name("Advanced Web Technologies")
                        .description("REST APIs, microservices, and modern web frameworks")
                        .build()
        );
    }
}