package org.example.springboot25.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalViewExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(org.example.springboot25.exceptions.GlobalViewExceptionHandler.class);

    @ExceptionHandler(BadRequestException.class)
    public ModelAndView handleBadRequest(BadRequestException ex) {
        logger.warn("Bad request: {}", ex.getMessage());
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("error", ex.getMessage());
        return mav;
    }

    @ExceptionHandler(ConflictException.class)
    public ModelAndView handleConflict(ConflictException ex) {
        logger.warn("Conflict detected: {}", ex.getMessage());
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("error", ex.getMessage());
        return mav;
    }

    @ExceptionHandler(BindException.class)
    public ModelAndView handleValidationErrors(BindException ex) {
        logger.warn("Validation error: {}", ex.getMessage());
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("error", "Please correct the errors in the form.");
        mav.addObject("validationErrors", ex.getBindingResult().getAllErrors());
        return mav;
    }


    @ExceptionHandler(Exception.class)
    public ModelAndView handleGeneric(Exception ex) {
        logger.error("Unexpected error occurred", ex);
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("error", "Unexpected error: " + ex.getMessage());
        return mav;
    }

    @ExceptionHandler(NotFoundException.class)
    public ModelAndView handleNotFoundException(NotFoundException ex) {
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("error", ex.getMessage());
        return mav;
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ModelAndView handleUserAlreadyExistsException(AlreadyExistsException ex) {
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("error", ex.getMessage());
        return mav;
    }
}
