package com.sismics.home.core.model.dbi;

import com.google.common.base.Objects;

import java.util.Date;

/**
 * User entity.
 * 
 * @author jtremeaux
 */
public class User {
    /**
     * User ID.
     */
    private String id;
    
    /**
     * Locale ID.
     */
    private String roleId;
    
    /**
     * User's username.
     */
    private String username;
    
    /**
     * User's password.
     */
    private String password;

    /**
     * Email address.
     */
    private String email;
    
    /**
     * True if the user hasn't dismissed the first connection screen.
     */
    private boolean firstConnection;

    /**
     * Creation date.
     */
    private Date createDate;
    
    /**
     * Deletion date.
     */
    private Date deleteDate;

    public User() {
    }

    public User(String id, String roleId, String username, String password, String email, boolean firstConnection, Date createDate, Date deleteDate) {
        this.id = id;
        this.roleId = roleId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstConnection = firstConnection;
        this.createDate = createDate;
        this.deleteDate = deleteDate;
    }

    /**
     * Getter of id.
     *
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * Setter of id.
     *
     * @param id id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Getter of roleId.
     *
     * @return roleId
     */
    public String getRoleId() {
        return roleId;
    }

    /**
     * Setter of roleId.
     *
     * @param roleId roleId
     */
    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    /**
     * Getter of username.
     *
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Setter of username.
     *
     * @param username username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Getter of password.
     *
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Setter of password.
     *
     * @param password password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Getter of email.
     *
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Setter of email.
     *
     * @param email email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Getter of firstConnection.
     *
     * @return firstConnection
     */
    public boolean isFirstConnection() {
        return firstConnection;
    }

    /**
     * Setter of firstConnection.
     *
     * @param firstConnection firstConnection
     */
    public void setFirstConnection(boolean firstConnection) {
        this.firstConnection = firstConnection;
    }

    /**
     * Getter of createDate.
     *
     * @return createDate
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * Setter of createDate.
     *
     * @param createDate createDate
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * Getter of deleteDate.
     *
     * @return deleteDate
     */
    public Date getDeleteDate() {
        return deleteDate;
    }

    /**
     * Setter of deleteDate.
     *
     * @param deleteDate deleteDate
     */
    public void setDeleteDate(Date deleteDate) {
        this.deleteDate = deleteDate;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("id", id)
                .add("username", username)
                .toString();
    }
}
