//package com.wenda.model;
//
//public class User {
//    private String name;
//    private int id;
//    private String headUrl;
//    private String password;
//    private String salt;
//    private int status;//账户状态0正常1封禁
//
//    public int getStatus() {
//        return status;
//    }
//
//    public void setStatus(int status) {
//        this.status = status;
//    }
//    public User(){}
//    public User(String name){
//        this.name=name;
//        this.salt="";
//        this.password="";
//        this.headUrl="";
//        status=0;
//    }
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    public String getHeadUrl() {
//        return headUrl;
//    }
//
//    public void setHeadUrl(String headUrl) {
//        this.headUrl = headUrl;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
//    public String getSalt() {
//        return salt;
//    }
//
//    public void setSalt(String salt) {
//        this.salt = salt;
//    }
//}
package com.wenda.model;

public class User {
    private String name;
    private int id;
    private String headUrl;
    private String password;
    private String salt;
    private int status;//账户状态0正常，1封禁

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User() {
    }

    public User(String name) {
        this.name = name;
        this.headUrl = "";
        this.password = "";
        this.salt = "";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
}
