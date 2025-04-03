package org.example.springboot25.exceptions;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalViewExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ModelAndView handleNotFoundException(NotFoundException ex) {
        ModelAndView mav = new ModelAndView("error-page");
        mav.addObject("error", ex.getMessage());
        return mav;
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ModelAndView handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        ModelAndView mav = new ModelAndView("error-page");
        mav.addObject("error", ex.getMessage());
        return mav;
    }
}
