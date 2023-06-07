package my.app.advertzone.model.user;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "accounts")
public class Account {

    @MongoId
    private ObjectId _id;

    @Field(name = "full_name")
    private String fullName;

    @Field(name = "email")
    private String accountEmail;

    @Field(name = "username")
    private String accountUsername;

    @Field(name = "password")
    private String accountPassword;

    @Field(name = "role")
    private AccountRole accountRole;

    @Field(name = "advert")
    private List<ObjectId> adverts;

    public Account() {
    }

    public Account(String firstName, String lastName, String accountEmail,
                   String accountUsername, String accountPassword) {
        this.fullName = firstName+" "+lastName;
        this.accountEmail = accountEmail;
        this.accountUsername = accountUsername;
        this.accountPassword = accountPassword;
        this.adverts = new ArrayList<>();
    }

    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String firstName, String lastName) {
        this.fullName = firstName + " " + lastName;
    }

    public String getAccountEmail() {
        return accountEmail;
    }

    public void setAccountEmail(String accountEmail) {
        this.accountEmail = accountEmail;
    }

    public String getAccountUsername() {
        return accountUsername;
    }

    public void setAccountUsername(String accountUsername) {
        this.accountUsername = accountUsername;
    }

    public String getAccountPassword() {
        return accountPassword;
    }

    public void setAccountPassword(String accountPassword) {
        this.accountPassword = accountPassword;
    }

    public AccountRole getAccountRole() {
        return accountRole;
    }

    public void setAccountRole(AccountRole accountRole) {
        this.accountRole = accountRole;
    }

    public List<ObjectId> getAdverts() {
        return adverts;
    }

    public void setAdverts(List<ObjectId> adverts) {
        this.adverts = adverts;
    }


    public void addAdvertisement(ObjectId advertId) {
        if(adverts == null) {
            adverts = new ArrayList<>();
        }
        adverts.add(advertId);
    }




    @Override
    public String toString() {
        return "Account{" +
                "_id=" + _id +
                ", fullName='" + fullName + '\'' +
                ", accountEmail='" + accountEmail + '\'' +
                ", accountUsername='" + accountUsername + '\'' +
                ", accountPassword='" + accountPassword + '\'' +
                ", accountRole=" + accountRole +
                ", adverts=" + adverts +
                '}';
    }
}