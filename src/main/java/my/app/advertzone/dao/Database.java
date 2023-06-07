package my.app.advertzone.dao;

import my.app.advertzone.model.advert.AdvertStatus;
import my.app.advertzone.model.advert.Advertisement;
import my.app.advertzone.model.user.Account;
import my.app.advertzone.model.user.AccountRole;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class Database {

    @Autowired
    private MongoTemplate mongoTemplate;


    public void saveAccount(Account account) {
        mongoTemplate.save(account);
    }


    public Optional<Account> findAccountByUsername(String username) {
        return Optional.ofNullable(mongoTemplate.findOne(
                Query.query
                        (Criteria.where("username").is(username)), Account.class));
    }


    public Optional<Account> findAccountByEmail(String email) {
        return Optional.ofNullable(mongoTemplate.findOne(
                Query.query
                        (Criteria.where("email").is(email)), Account.class));
    }


    public Account findAccountById(ObjectId id) {
        return mongoTemplate.findById(id, Account.class);
    }


    public Advertisement findAdvertById(ObjectId id) {
        return mongoTemplate.findById(id, Advertisement.class);
    }


    public List<Account> getAllUsers() {
        return mongoTemplate.findAll(Account.class);
    }


    public List<Advertisement> getAllAdverts() {
        return mongoTemplate.findAll(Advertisement.class);
    }


    public void saveAdvert(Advertisement advertisement) {
        Advertisement savedAdvert = mongoTemplate.save(advertisement);
        Account foundAccount = findAccountById(savedAdvert.getAdvertPostedBy());
        foundAccount.addAdvertisement(savedAdvert.get_id());
        mongoTemplate.save(foundAccount);
    }


    public boolean acceptAdvert(ObjectId id) {
        Advertisement foundAdvert = findAdvertById(id);

        if(foundAdvert.getAdvertStatus().equals(AdvertStatus.ACCEPTED)) {
            System.out.println("already accepted");
            return false;
        } else {
            foundAdvert.setAdvertStatus(AdvertStatus.ACCEPTED);
            mongoTemplate.save(foundAdvert);
            return true;
        }
    }


    public void denyAdvert(ObjectId id) {
        Advertisement foundAdvert = findAdvertById(id);
        foundAdvert.setAdvertStatus(AdvertStatus.DENIED);
        mongoTemplate.save(foundAdvert);
    }


    public void banUser(ObjectId id) {
        Account foundAccount = findAccountById(id);
        foundAccount.setAccountRole(AccountRole.BANNED);
        mongoTemplate.save(foundAccount);
    }


    public void promoteUser(ObjectId id) {
        Account foundAccount = findAccountById(id);
        foundAccount.setAccountRole(AccountRole.ADMIN);
        mongoTemplate.save(foundAccount);
    }


    public void demoteOrUnbanUser(ObjectId id) {
        Account foundAccount = findAccountById(id);
        foundAccount.setAccountRole(AccountRole.NORMAL_USER);
        mongoTemplate.save(foundAccount);
    }


    public List<List<Advertisement>> getUserAdvertsStats(ObjectId id) {
        Account foundAccount = findAccountById(id);

        if (foundAccount == null) {
            return null;
        } else {
            List<Advertisement> allAdverts = getAllAdverts();
            List<Advertisement> pendingAdverts = new ArrayList<>();
            List<Advertisement> acceptedAdverts = new ArrayList<>();
            List<Advertisement> deniedAdverts = new ArrayList<>();

            for (Advertisement a : allAdverts) {
                if (a.getAdvertStatus().equals(AdvertStatus.PENDING)) {
                    pendingAdverts.add(a);
                } else if (a.getAdvertStatus().equals(AdvertStatus.ACCEPTED)) {
                    acceptedAdverts.add(a);
                } else if (a.getAdvertStatus().equals(AdvertStatus.DENIED)) {
                    deniedAdverts.add(a);
                }
            }

            List<List<Advertisement>> result = new ArrayList<>();
            result.add(pendingAdverts);
            result.add(acceptedAdverts);
            result.add(deniedAdverts);

            return result;
        }
    }


    public void removeAdvert(ObjectId advertId) {
        Advertisement foundAdvert = findAdvertById(advertId);
        Account foundAccount = findAccountById(foundAdvert.getAdvertPostedBy());

        if (foundAccount != null) {
            mongoTemplate.remove(foundAdvert);
            foundAccount.getAdverts().remove(foundAdvert.get_id());
            mongoTemplate.save(foundAccount);
        }
    }


}