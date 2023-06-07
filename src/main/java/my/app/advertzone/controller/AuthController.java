package my.app.advertzone.controller;

import my.app.advertzone.dao.Database;
import my.app.advertzone.model.dto.LoginDTO;
import my.app.advertzone.model.dto.RegisterDTO;
import my.app.advertzone.model.user.Account;
import my.app.advertzone.model.user.AccountRole;
import my.app.advertzone.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    // for hashing password
    @Autowired
    private PasswordEncoder encoder;

    // the database repository
    @Autowired
    private Database dao;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private AuthenticationManager authenticationManager;



    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterDTO registerDTO) {

        final boolean isUsernameTaken = dao.findAccountByUsername(registerDTO.getUsername()).isPresent();
        final boolean isEmailTaken = dao.findAccountByEmail(registerDTO.getEmail()).isPresent();
        final boolean isPasswordMatch = registerDTO.getPassword().matches(registerDTO.getConfirmPassword());

        if (isUsernameTaken) {
            return new ResponseEntity<>("username taken!", HttpStatus.BAD_REQUEST);
        } else if (isEmailTaken) {
            return new ResponseEntity<>("email taken!", HttpStatus.BAD_REQUEST);
        } else if (!isPasswordMatch) {
            return new ResponseEntity<>("password does not match each other", HttpStatus.BAD_REQUEST);
        }

        Account account = new Account();
        // sets fullName
        account.setFullName(registerDTO.getFirstName(), registerDTO.getLastName());
        // sets email
        account.setAccountEmail(registerDTO.getEmail().toLowerCase());
        // sets username
        account.setAccountUsername(registerDTO.getUsername().toLowerCase());
        // sets password and hashes password
        account.setAccountPassword(encoder.encode(registerDTO.getPassword()));
        // sets role
        account.setAccountRole(AccountRole.NORMAL_USER);
        dao.saveAccount(account);

        return new ResponseEntity<>("task complete! account created! check your email for verification code!"
                , HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {

        try {

            Optional<Account> optionalAccount = dao.findAccountByUsername(loginDTO.getUsername());
            Account foundAccount = optionalAccount.get();

            if (foundAccount.getAccountRole().equals(AccountRole.BANNED)) {
                return new ResponseEntity<>("You are banned", HttpStatus.UNAUTHORIZED);
            }

            Authentication auth = authenticationManager.authenticate
                    (
                            new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword())
                    );

            final boolean userIsAuthenticated = auth.isAuthenticated();

            if (userIsAuthenticated) {
                String generatedJwtToken = jwtProvider.generate(auth);
                return new ResponseEntity<>("You have successfully logged in\n" + " Your role is: \n"+
                        "JWT TOKEN: "+generatedJwtToken+ " "
                        + foundAccount.getAccountRole(), HttpStatus.OK);
            }
        } catch (AuthenticationException error) {
            System.out.println("Exception: ");
            error.printStackTrace();
            return new ResponseEntity<>("Wrong username or password!", HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>("Something went wrong!", HttpStatus.UNAUTHORIZED);
    }


    // todo add logout

}

