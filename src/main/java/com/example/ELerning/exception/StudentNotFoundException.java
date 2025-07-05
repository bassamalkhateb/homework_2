package com.example.ELerning.exception;

public class StudentNotFoundException extends RuntimeException {

    public StudentNotFoundException(Long studentId) {
        super("الطالب غير موجود بالمعرف: " + studentId);
    }

}
