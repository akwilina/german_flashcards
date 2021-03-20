package projects.germanflashcards.web.controller;

import com.zaxxer.hikari.metrics.MetricsTrackerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import projects.germanflashcards.exception.UserAlreadyExistsException;
import projects.germanflashcards.service.UserService;
import projects.germanflashcards.web.command.RegisterUserCommand;

import javax.validation.Valid;

@Controller @Slf4j @RequiredArgsConstructor
@RequestMapping("/register")
public class RegistrationController {

    private final UserService userService;


    @GetMapping
    public String getRegister(Model model){
        model.addAttribute(new RegisterUserCommand());
        return "register/form";
    }

    @PostMapping
    //do rejestracji potrzebujemy: username i password
    public String processRegister(@Valid RegisterUserCommand registerUserCommand, BindingResult bindingResult){

        log.debug("Data for user creation: {}", registerUserCommand);
        if(bindingResult.hasErrors()){
            log.debug("Wrong data: {}", bindingResult.getAllErrors());
            return "register/form";
        }

        try {
            Long id = userService.create(registerUserCommand);
            log.debug("Id of created user = {}", id);
            return "redirect:/login";
        } catch (UserAlreadyExistsException uaee){
            bindingResult.rejectValue("username", null, "User with that username already exist");
            return "register/form";
        } catch (RuntimeException re){
            bindingResult.rejectValue(null, null,"Exception occurred, please contact the admin");
            return "register/form";
        }
    }
}