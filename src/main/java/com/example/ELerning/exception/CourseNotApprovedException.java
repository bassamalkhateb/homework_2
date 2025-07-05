package com.example.ELerning.exception;

  public class CourseNotApprovedException extends RuntimeException {
        public CourseNotApprovedException(Long courseId) {
            super("الدورة غير معتمدة للتسجيل بالمعرف: " + courseId);
        }
    }
