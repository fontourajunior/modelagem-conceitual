package br.com.cursomc.cursomodelagemconceitual.resources;

import br.com.cursomc.cursomodelagemconceitual.dto.EmailDTO;
import br.com.cursomc.cursomodelagemconceitual.security.JWTUtil;
import br.com.cursomc.cursomodelagemconceitual.security.UserSS;
import br.com.cursomc.cursomodelagemconceitual.services.AuthService;
import br.com.cursomc.cursomodelagemconceitual.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping(value = "/auth")
public class AuthResource {

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private AuthService authService;

    @PostMapping(value = "/refresh_token")
    public ResponseEntity<Void> refreshToken(HttpServletResponse response) {
        UserSS userAuthenticated = UserService.authenticated();
        String token = jwtUtil.generateToken(userAuthenticated.getUsername());
        response.addHeader("Authorization", "Bearer " + token);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/forgot")
    public ResponseEntity<Void> forgot(@Valid @RequestBody EmailDTO emailDTO) {
        authService.sendNewPassword(emailDTO.getEmail());
        return ResponseEntity.noContent().build();
    }

}
