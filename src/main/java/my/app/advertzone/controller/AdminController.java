package my.app.advertzone.controller;

import my.app.advertzone.dao.Database;
import my.app.advertzone.model.advert.Advertisement;
import my.app.advertzone.model.user.Account;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;



/**
 * this is the admin controller. every api in here can be accessed by all authenticated users
 * with ADMIN role
 */
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private Database dao;


    @PutMapping("/denyAdvert/{id}")
    public ResponseEntity<String> denyAdvertById(@PathVariable ObjectId id, Authentication authentication) {

        if (authentication.isAuthenticated()) {
            try {
                dao.denyAdvert(id);
                // todo add mailer to send to the adverter about the information
                return new ResponseEntity<>("advert denied by admins", HttpStatus.OK);
            } catch (AccessDeniedException error) {
                error.printStackTrace();
                return new ResponseEntity<>("You are not an admin!", HttpStatus.UNAUTHORIZED);
            }
        }
        return new ResponseEntity<>("not not logged in", HttpStatus.UNAUTHORIZED);
    }

    @PutMapping("/acceptAdvert/{id}")
    public ResponseEntity<String> acceptAdvertById(@PathVariable ObjectId id, Authentication authentication) {
        boolean isAut = authentication.isAuthenticated();
        if (isAut) {
            try {
                dao.acceptAdvert(id);
                // todo add mailer to send to the adverter about the information
                return new ResponseEntity<>("advert accepted by admins", HttpStatus.OK);
            } catch (AccessDeniedException error) {
                error.printStackTrace();
                return new ResponseEntity<>("You are not an admin!", HttpStatus.UNAUTHORIZED);
            }
        }
        return new ResponseEntity<>("not not logged in", HttpStatus.UNAUTHORIZED);
    }


    @PutMapping("/ban/{id}")
    public ResponseEntity<String> banUserById(@PathVariable ObjectId id, Authentication authentication) {
        boolean isAut = authentication.isAuthenticated();
        if (isAut) {
            try {
                dao.banUser(id);
                Account foundAccount = dao.findAccountById(id);
                return new ResponseEntity<>("User: " + foundAccount + " was successfully banned", HttpStatus.OK);
            } catch (AccessDeniedException error) {
                error.printStackTrace();
                return new ResponseEntity<>("access denied", HttpStatus.UNAUTHORIZED);
            }
        }
        return new ResponseEntity<>("something went wrong", HttpStatus.UNAUTHORIZED);
    }

    @PutMapping("/promote/{id}")
    public ResponseEntity<String> promoteUserById(@PathVariable ObjectId id, Authentication authentication) {
        boolean isAut = authentication.isAuthenticated();
        if (isAut) {
            try {
                dao.banUser(id);
                return new ResponseEntity<>("user was successfully promoted", HttpStatus.OK);
            } catch (AccessDeniedException error) {
                error.printStackTrace();
                return new ResponseEntity<>("access denied", HttpStatus.UNAUTHORIZED);
            }
        }
        return new ResponseEntity<>("something went wrong", HttpStatus.UNAUTHORIZED);
    }


    @GetMapping("/advertStats/{id}")
    public ResponseEntity<String> getUserAdvertStatsByUserId(@PathVariable ObjectId id,
                                                             Authentication authentication) {
        if (authentication.isAuthenticated()) {
            try {
                List<java.util.List<Advertisement>> advertStats = dao.getUserAdvertsStats(id);
                return new ResponseEntity<>("this is the advert stats for this user: \n" +
                        " " + advertStats, HttpStatus.OK);
            } catch (AccessDeniedException error) {
                error.printStackTrace();
                return new ResponseEntity<>("access denied", HttpStatus.UNAUTHORIZED);
            }
        }
        return new ResponseEntity<>("something went wrong!", HttpStatus.UNAUTHORIZED);
    }
}