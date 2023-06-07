package my.app.advertzone.controller;

import my.app.advertzone.dao.Database;
import my.app.advertzone.model.advert.AdvertStatus;
import my.app.advertzone.model.advert.Advertisement;
import my.app.advertzone.model.dto.AdvertDTO;
import my.app.advertzone.model.user.Account;
import my.app.advertzone.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/advert")
@CrossOrigin
public class AdvertisementController {

    @Autowired
    private Database dao;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtProvider jwtProvider;


    @PostMapping("/upload")
    public ResponseEntity<String> uploadAdvert
            (@RequestBody AdvertDTO advertDTO,
             Authentication authentication) {

        if (authentication.isAuthenticated()) {
            String username = authentication.getName();
            Optional<Account> account = dao.findAccountByUsername(username);

            if (account.isPresent()) {
                Account foundAccount = dao.findAccountById(account.get().get_id());

                Advertisement advertisement = new Advertisement();

                advertisement.setAdvertTitle(advertDTO.getTitle());
                advertisement.setAdvertDescription(advertDTO.getDescription());
                advertisement.setAdvertPrice(advertDTO.getPrice());
                advertisement.setAdvertPostedBy(foundAccount.get_id());
                advertisement.setAdvertStatus(AdvertStatus.PENDING);
                dao.saveAdvert(advertisement);
            }
            return new ResponseEntity<>("advert was successfully saved", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("user is not logged in", HttpStatus.UNAUTHORIZED);
        }
    }
}