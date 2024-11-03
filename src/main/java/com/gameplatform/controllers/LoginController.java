package com.gameplatform.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;

public class LoginController {
    
    @FXML
    private TextField usernameField;
    
    @FXML
    private PasswordField passwordField;
    
    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        // 实现登录逻辑
    }
    
    @FXML
    private void handleSignUp() {
        // 实现跳转到注册页面的逻辑
    }
} 