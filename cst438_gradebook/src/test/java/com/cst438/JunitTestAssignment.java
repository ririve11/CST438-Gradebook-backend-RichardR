package com.cst438;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Optional;
import java.sql.Date;

import com.cst438.controllers.AssignmentController;
import com.cst438.domain.Assignment;
import com.cst438.domain.AssignmentRepository;
import com.cst438.domain.Course;
import com.cst438.domain.CourseRepository;
import com.cst438.domain.Enrollment;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = { AssignmentController.class })
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest
public class JunitTestAssignment {
    static final String URL = "http://localhost:8080";
    public static final int TEST_COURSE_ID = 40442;
    public static final String TEST_STUDENT_EMAIL = "test@csumb.edu";
    public static final String TEST_STUDENT_NAME = "test";
    public static final String TEST_INSTRUCTOR_EMAIL = "dwisneski@csumb.edu";
    public static final int TEST_YEAR = 2021;
    public static final String TEST_SEMESTER = "Fall";

    @MockBean
    AssignmentRepository assignmentRepository;

    @MockBean
    CourseRepository courseRepository; // must have this to keep Spring test happy

    @Autowired
    private MockMvc mvc;

    @Test
    public void AddAssignment() throws Exception {

        MockHttpServletResponse response;

        Course course = new Course();
        course.setCourse_id(TEST_COURSE_ID);
        course.setSemester(TEST_SEMESTER);
        course.setYear(TEST_YEAR);
        course.setInstructor(TEST_INSTRUCTOR_EMAIL);
        course.setAssignments(new java.util.ArrayList<Assignment>());

        Assignment assignment = new Assignment();
        assignment.setId(1);
        assignment.setName("Assignment 1");
        assignment.setDueDate(new Date(System.currentTimeMillis()));

        given(courseRepository.findById(TEST_COURSE_ID)).willReturn(Optional.of(course));
        given(assignmentRepository.save(any(Assignment.class))).willReturn(assignment);

        response = mvc.perform(MockMvcRequestBuilders.post("/assignment/{courseId}", TEST_COURSE_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(assignment)))
                .andReturn().getResponse();

        assertEquals(200, response.getStatus());
        verify(assignmentRepository, times(1)).save(any(Assignment.class));
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static <T> T fromJsonString(String str, Class<T> valueType) {
        try {
            return new ObjectMapper().readValue(str, valueType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

