package my.app.advertzone.service;

import my.app.advertzone.model.user.Account;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final MongoTemplate dao;

    public CustomUserDetailsService(MongoTemplate dao) {
        this.dao = dao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = dao.findOne(Query.query(Criteria.where("username").is(username)), Account.class);
        if (account == null) {
            throw new UsernameNotFoundException("User not found with username or email: " + username);
        }

        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(account.getAccountRole().name()));

        return new org.springframework.security.core.userdetails.User
                (
                        account.getAccountUsername(),
                        account.getAccountPassword(),
                        authorities
                );
    }
}
