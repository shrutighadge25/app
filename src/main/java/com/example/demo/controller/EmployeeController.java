package com.example.demo.controller;

import com.example.demo.model.Employee;
import com.example.demo.service.EmployeeService;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.HashMap;
import java.util.Map;



@Controller
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/")
    public String showLoginForm() {
        return "login"; // Show login page when accessing the root URL
    }

    @GetMapping("/register")
    public String showRegistrationForm() {
        return "register";
    }

    @PostMapping("/register")
    public String registerEmployee(@RequestParam String employeename,
                                   @RequestParam String username,
                                   @RequestParam String employeecontact,
                                   @RequestParam String department,
                                   @RequestParam String createpassword,
                                   @RequestParam String confirmpassword,
                                   Model model) {

        if (!createpassword.equals(confirmpassword)) {
            model.addAttribute("error", "Passwords do not match");
            return "register";
        }

        Employee employee = new Employee();
        employee.setEmployeename(employeename);
        employee.setUsername(username);
        employee.setEmployeecontact(employeecontact);
        employee.setDepartment(department);
        employee.setCreatepassword(createpassword);
        employee.setConfirmpassword(confirmpassword);

        try {
            employeeService.registerEmployee(employee);
        } catch (RuntimeException e) {
            model.addAttribute("error", "Username already exists.");
            return "register";
        }

        return "redirect:/success";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        Model model,
                        HttpSession session) {
        
        Employee employee = employeeService.findByUsername(username);
        if (employee == null) {
            model.addAttribute("error", "Username does not exist");
            return "login";
        }
        
        if (!employee.getCreatepassword().equals(password)) {
            model.addAttribute("error", "Incorrect password");
            return "login";
        }
        
        session.setAttribute("user", employee);
        return "redirect:/home";
    }
    

    @GetMapping("/home")
    public String homePage(HttpSession session) {
        if (session.getAttribute("user") == null) {
            return "redirect:/";
        }
        return "home";
    }

    @GetMapping("/success")
    public String success() {
        return "success";
    }

    @GetMapping("/forgot-password")
    public String forgotPassword() {
        // Handle forgot password logic here
        return "forgot-password";
    }
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Invalidate the session to log the user out
        return "redirect:/logout-success"; // Redirect to a page that shows the logout success message
    }
    
    @GetMapping("/logout-success")
    public String logoutSuccess(Model model) {
        model.addAttribute("message", "Logout Successfully"); // Add a message attribute
        return "logout-success"; // Name of your HTML template
    }
    
    @GetMapping("/api/userDetails")
    @ResponseBody
    public Map<String, String> getUserDetails(HttpSession session) {
        Employee user = (Employee) session.getAttribute("user");
        Map<String, String> result = new HashMap<>();
        if (user != null) {
            // Split employeename into first and last name if possible
            String[] names = user.getEmployeename().split(" ", 2);
            result.put("firstName", names.length > 0 ? names[0] : "");
            result.put("lastName", names.length > 1 ? names[1] : "");
        } else {
            result.put("firstName", "");
            result.put("lastName", "");
        }
        return result;
    }
    
}
