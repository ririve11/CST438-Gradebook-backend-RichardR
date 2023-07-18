package com.cst438.controllers;

import java.sql.Date;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.cst438.domain.Assignment;
import com.cst438.domain.AssignmentGradeRepository;
import com.cst438.domain.AssignmentRepository;
import com.cst438.domain.Course;
import com.cst438.domain.CourseRepository;

@RestController
@CrossOrigin(origins = {"http://localhost:3000","http://localhost:3001"})
@RequestMapping("/assignments")
public class AssignmentController {

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private CourseRepository courseRepository;
    
    @Autowired
    AssignmentGradeRepository assignmentGradeRepository;

    @PostMapping("/new")
    @Transactional
    public Assignment createAssignment(@RequestParam("courseId") int courseId,
                                       @RequestParam("name") String name,
                                       @RequestParam("dueDate") String date) {
        Course course = courseRepository.findById(courseId).orElse(null);
        if (course != null) {
            Assignment assignment = new Assignment();
            assignment.setDueDate(Date.valueOf(date));
            assignment.setName(name);
            assignment.setCourse(course);
            assignment.setNeedsGrading(1);
            return assignmentRepository.save(assignment);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't change course ID " + courseId);
        }
    }

    @PutMapping("/update/{assignmentId}")
    @Transactional
    public Assignment changeAssignmentName(@PathVariable("assignmentId") int assignmentId,
                                           @RequestParam("name") String name) {
        Assignment assignment = assignmentRepository.findById(assignmentId).orElse(null);
        if (assignment != null) {
            assignment.setName(name);
            return assignmentRepository.save(assignment);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't change assignment ID " + assignmentId);
        }
    }

    @DeleteMapping("/delete/{assignmentId}")
    @Transactional
    public void deleteAssignment(@PathVariable("assignmentId") int assignmentId) {
        Assignment assignment = assignmentRepository.findById(assignmentId).orElse(null);
        if (assignment != null) {
            String email = "dwisneski@csumb.edu";
            if (email.equals("dwisneski@csumb.edu")) {
                assignmentRepository.delete(assignment);
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't delete");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Your assignment ID is invalid: " + assignmentId);
        }
    }
}

