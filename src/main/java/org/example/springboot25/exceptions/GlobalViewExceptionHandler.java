package org.example.springboot25.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalViewExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(org.example.springboot25.exceptions.GlobalViewExceptionHandler.class);

    @ExceptionHandler(BadRequestException.class)
    public String handleBadRequest(BadRequestException ex, Model model) {
        logger.warn("Bad request: {}", ex.getMessage());
        model.addAttribute("error", ex.getMessage());
        return "error";
    }

    @ExceptionHandler(ConflictException.class)
    public String handleConflict(ConflictException ex, Model model) {
        logger.warn("Conflict detected: {}", ex.getMessage());
        model.addAttribute("error", ex.getMessage());
        return "error";
    }

    @ExceptionHandler(BindException.class)
    public String handleValidationErrors(BindException ex, Model model) {
        logger.warn("Validation error: {}", ex.getMessage());
        model.addAttribute("error", "Please correct the errors in the form.");
        model.addAttribute("validationErrors", ex.getBindingResult().getAllErrors());
        return "error";
    }

    @ExceptionHandler(Exception.class)
    public String handleGeneric(Exception ex, Model model) {
        logger.error("Unexpected error occurred", ex);
        model.addAttribute("error", "Unexpected error: " + ex.getMessage());
        return "error";
    }

    @ExceptionHandler(NotFoundException.class)
    public ModelAndView handleNotFoundException(NotFoundException ex) {
        ModelAndView mav = new ModelAndView("error-page");
        mav.addObject("error", ex.getMessage());
        return mav;
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ModelAndView handleUserAlreadyExistsException(AlreadyExistsException ex) {
        ModelAndView mav = new ModelAndView("error-page");
        mav.addObject("error", ex.getMessage());
        return mav;
    }
}
