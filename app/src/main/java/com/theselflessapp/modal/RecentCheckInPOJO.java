package com.theselflessapp.modal;

/**
 * Created by ourdesignz on 24/10/16.
 */

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
        "success",
        "message",
        "data"
})
public class RecentCheckInPOJO {

    @JsonProperty("success")
    private Boolean success;
    @JsonProperty("message")
    private String message;
    @JsonProperty("data")
    private List<List<Datum>> data = new ArrayList<List<Datum>>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * @return The success
     */
    @JsonProperty("success")
    public Boolean getSuccess() {
        return success;
    }

    /**
     * @param success The success
     */
    @JsonProperty("success")
    public void setSuccess(Boolean success) {
        this.success = success;
    }

    /**
     * @return The message
     */
    @JsonProperty("message")
    public String getMessage() {
        return message;
    }

    /**
     * @param message The message
     */
    @JsonProperty("message")
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return The data
     */
    @JsonProperty("data")
    public List<List<Datum>> getData() {
        return data;
    }

    /**
     * @param data The data
     */
    @JsonProperty("data")
    public void setData(List<List<Datum>> data) {
        this.data = data;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Generated("org.jsonschema2pojo")
    @JsonPropertyOrder({
            "id",
            "userid",
            "photo_name",
            "date_created",
            "date_updated",
            "tag_users",
            "timess",
            "checkin_time",
            "checkout_time"
    })
    public class Datum {

        @JsonProperty("id")
        private String id;
        @JsonProperty("userid")
        private String userid;
        @JsonProperty("photo_name")
        private String photo_name;
        @JsonProperty("date_created")
        private String date_created;
        @JsonProperty("date_updated")
        private String date_updated;
        @JsonProperty("tag_users")
        private String tag_users;
        @JsonProperty("timess")
        private String timess;
        @JsonProperty("checkin_time")
        private String checkin_time;
        @JsonProperty("checkout_time")
        private String checkout_time;
        @JsonIgnore
        private Map<String, Object> additionalProperties = new HashMap<String, Object>();

        /**
         * @return The id
         */
        @JsonProperty("id")
        public String getId() {
            return id;
        }

        /**
         * @param id The id
         */
        @JsonProperty("id")
        public void setId(String id) {
            this.id = id;
        }

        /**
         * @return The userid
         */
        @JsonProperty("userid")
        public String getUserid() {
            return userid;
        }

        /**
         * @param userid The userid
         */
        @JsonProperty("userid")
        public void setUserid(String userid) {
            this.userid = userid;
        }

        /**
         * @return The photoName
         */
        @JsonProperty("photo_name")
        public String getPhotoName() {
            return photo_name;
        }

        /**
         * @param photoName The photo_name
         */
        @JsonProperty("photo_name")
        public void setPhotoName(String photo_name) {
            this.photo_name = photo_name;
        }

        /**
         * @return The dateCreated
         */
        @JsonProperty("date_created")
        public String getDateCreated() {
            return date_created;
        }

        /**
         * @param dateCreated The date_created
         */
        @JsonProperty("date_created")
        public void setDateCreated(String date_created) {
            this.date_created = date_created;
        }

        /**
         * @return The dateUpdated
         */
        @JsonProperty("date_updated")
        public String getDateUpdated() {
            return date_updated;
        }

        /**
         * @param dateUpdated The date_updated
         */
        @JsonProperty("date_updated")
        public void setDateUpdated(String date_updated) {
            this.date_updated = date_updated;
        }

        /**
         * @return The tagUsers
         */
        @JsonProperty("tag_users")
        public String getTagUsers() {
            return tag_users;
        }

        /**
         * @param tagUsers The tag_users
         */
        @JsonProperty("tag_users")
        public void setTagUsers(String tag_users) {
            this.tag_users = tag_users;
        }

        /**
         * @return The timess
         */
        @JsonProperty("timess")
        public String getTimess() {
            return timess;
        }

        /**
         * @param timess The timess
         */
        @JsonProperty("timess")
        public void setTimess(String timess) {
            this.timess = timess;
        }

        /**
         * @return The checkinTime
         */
        @JsonProperty("checkin_time")
        public String getCheckinTime() {
            return checkin_time;
        }

        /**
         * @param checkinTime The checkin_time
         */
        @JsonProperty("checkin_time")
        public void setCheckinTime(String checkin_time) {
            this.checkin_time = checkin_time;
        }

        /**
         * @return The checkoutTime
         */
        @JsonProperty("checkout_time")
        public String getCheckoutTime() {
            return checkout_time;
        }

        /**
         * @param checkoutTime The checkout_time
         */
        @JsonProperty("checkout_time")
        public void setCheckoutTime(String checkout_time) {
            this.checkout_time = checkout_time;
        }

        @JsonAnyGetter
        public Map<String, Object> getAdditionalProperties() {
            return this.additionalProperties;
        }

        @JsonAnySetter
        public void setAdditionalProperty(String name, Object value) {
            this.additionalProperties.put(name, value);
        }

    }

}
