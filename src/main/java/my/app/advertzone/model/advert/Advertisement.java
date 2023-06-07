package my.app.advertzone.model.advert;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection = "adverts")
public class Advertisement {

    @MongoId
    private ObjectId _id;


    @Field(name = "title")
    private String advertTitle;

    @Field(name = "description")
    private String advertDescription;

    @Field(name = "user")
    private ObjectId advertPostedBy;

    @Field(name = "status")
    private AdvertStatus advertStatus;

    @Field(name = "price")
    private String advertPrice;


    public Advertisement() {
    }

    public Advertisement(String advertTitle, String advertDescription, ObjectId advertPostedBy, Double advertPrice) {
        this.advertTitle = advertTitle;
        this.advertDescription = advertDescription;
        this.advertPostedBy = advertPostedBy;
        this.advertStatus = AdvertStatus.PENDING;

        if (advertPrice <= 0) {
            this.advertPrice = "Free";
        } else {
            this.advertPrice = String.valueOf(advertPrice);
        }
    }


    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public String getAdvertTitle() {
        return advertTitle;
    }

    public void setAdvertTitle(String advertTitle) {
        this.advertTitle = advertTitle;
    }

    public String getAdvertDescription() {
        return advertDescription;
    }

    public void setAdvertDescription(String advertDescription) {
        this.advertDescription = advertDescription;
    }

    public ObjectId getAdvertPostedBy() {
        return advertPostedBy;
    }

    public void setAdvertPostedBy(ObjectId advertPostedBy) {
        this.advertPostedBy = advertPostedBy;
    }

    public AdvertStatus getAdvertStatus() {
        return advertStatus;
    }

    public void setAdvertStatus(AdvertStatus advertStatus) {
        this.advertStatus = advertStatus;
    }

    public String getAdvertPrice() {
        return advertPrice;
    }

    public void setAdvertPrice(Double advertPrice) {
        if (advertPrice <= 0) {
            this.advertPrice = "free";
        } else {
            this.advertPrice = String.valueOf(advertPrice);
        }
    }
}