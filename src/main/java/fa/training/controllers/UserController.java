package fa.training.controllers;

import fa.training.entities.User;
import fa.training.enums.StatusUser;
import fa.training.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String listUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "users/user-list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("user", new User());
        return "users/user-create";
    }

    @PostMapping
    public String createUser(@Valid @ModelAttribute("user") User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "users/user-create";
        }
        userService.createUser(user);
        return "redirect:/users";
    }

    @GetMapping("/detail/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        User user = userService.getUserById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        model.addAttribute("user", user);
        return "users/user-detail";
    }

    @PostMapping("/toggleStatus/{id}")
    public String toggleStatus(@PathVariable Long id) {
        User user = userService.getUserById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));

        if (user.getStatus() == StatusUser.Active) {
            user.setStatus(StatusUser.Inactive);
        } else {
            user.setStatus(StatusUser.Active);
        }

        userService.updateUser(user);

        return "redirect:/users/detail/" + id;
    }

}
