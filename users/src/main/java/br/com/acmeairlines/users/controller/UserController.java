package br.com.acmeairlines.users.controller;

import br.com.acmeairlines.users.model.UserModel;
import br.com.acmeairlines.users.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<Page<UserDataDTO>> getUsers(@PageableDefault(size = 10, sort = {"id"}) Pageable pages) {
        return ResponseEntity.ok(userService.findActiveUsers(pages));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserDataDTO> getUserByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userService.findByEmail(email));
    }

    @PutMapping("/update")
    @Transactional
    public ResponseEntity<UserDataDTO> updateUser(HttpServletRequest request, @RequestBody @Valid UserUpdateDTO data) {
        UserDataDTO user = userService.findByEmail(request.getRemoteUser());
        UserDataDTO newUser = userService.updateUser(data, user.id());

        if(newUser == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(newUser);
    }

}
