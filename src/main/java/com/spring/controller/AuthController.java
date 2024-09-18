package com.spring.controller;

import com.spring.entities.*;
import com.spring.model.UsersDTO;
import com.spring.repository.UsersRepository;
import com.spring.service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Controller
public class AuthController {
    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsersService usersService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CarOwnerService carOwnerService;

    @Autowired
    private PasswordResetService passwordResetService;

    @Autowired
    private EmailService emailService;

    @GetMapping("/check-email")
    public ResponseEntity<Boolean> checkEmailExists(@RequestParam String email) {
        boolean emailExists = usersService.emailExists(email);
        return ResponseEntity.ok(emailExists);
    }

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("users", new Users());
        return "auth/auth-page";
    }

    @PostMapping("/login-check")
    public String loginSubmit(@Valid @ModelAttribute("users") Users users,
                              BindingResult result,
                              Model model) {
        if (result.hasErrors()) {
            return "auth/auth-page";
        }

        Users existingUser = usersService.findByEmail(users.getEmail());
        if (existingUser == null || !usersService.checkPassword(users.getPassword(), existingUser.getPassword())) {
            model.addAttribute("loginError", "Email or password incorrect");
            return "auth/auth-page";
        }
        return "redirect:/home";
    }

    @PostMapping("/auth")
    public String registerForm(@Valid @ModelAttribute UsersDTO usersDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "Validation errors occurred");
            return "auth/auth-page";
        }
        if (!usersDTO.getPassword().equals(usersDTO.getConfirmPassword())) {
            model.addAttribute("error", "Passwords do not match");
            return "auth/auth-page";
        }
        try {
            usersService.registerUser(usersDTO);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "An error occurred while saving data");
            return "auth/auth-page";
        }
        return "redirect:/login";
    }

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "auth/forgot-password";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam("email") String userEmail, Model model) {
        System.out.println("Received email: " + userEmail);

        try {
            Users users = usersService.findByEmail(userEmail);
            if (users == null) {
                System.out.println("No user found with email: " + userEmail);
            }

            String token = UUID.randomUUID().toString();
            passwordResetService.createPasswordResetTokenForUser(users, token);

            String resetUrl = "http://localhost:8080/reset-password?token=" + token;
            emailService.sendPasswordResetEmail(userEmail, resetUrl);

            model.addAttribute("message", "We've sent a password reset link to your email");
        } catch (Exception e) {
            model.addAttribute("error", "Your email was not found");
            e.printStackTrace();
        }
        return "auth/forgot-password";
    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam("token") String token, Model model) {
        PasswordResetToken resetToken = passwordResetService.findByToken(token);
        if (resetToken == null || resetToken.isExpired()) {
            model.addAttribute("error", "Token is invalid or expired");
            return "auth/reset-password";
        }

        model.addAttribute("token", token);
        return "auth/reset-password";
    }

    @PostMapping("/reset-password")
    public String handleResetPassword(@RequestParam("token") String token,
                                      @RequestParam("password") String password,
                                      @RequestParam("confirmPassword") String confirmPassword,
                                      Model model) {
        PasswordResetToken resetToken = passwordResetService.findByToken(token);
        if (resetToken == null || resetToken.isExpired()) {
            model.addAttribute("error", "Token is invalid or expired");
            return "auth/reset-password";
        }

        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Confirmation password does not match");
            model.addAttribute("token", token);
            return "auth/reset-password";
        }

        if (!isValidPassword(password)) {
            model.addAttribute("error", "Password must contain at least one letter, one number and at least 7 characters");
            model.addAttribute("token", token);
            return "auth/reset-password";
        }

        Users users = resetToken.getUsers();
        usersService.updatePassword(users, password);

        passwordResetService.deleteToken(resetToken);

        model.addAttribute("message", "Your password has been updated successfully. You can log in now");
        return "auth/auth-page";
    }

    private boolean isValidPassword(String password) {
        if (password.length() < 7) return false;
        boolean hasLetter = false;
        boolean hasDigit = false;
        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) hasLetter = true;
            if (Character.isDigit(c)) hasDigit = true;
            if (hasLetter && hasDigit) return true;
        }
        return false;
    }
}
