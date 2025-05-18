package models;

import lombok.Data;

@Data
public class LoginResponseModel {
    private String token;
    private String userId;
    private String expires;
    private String username;
    private String created_date;
    private String password;
    private Boolean isActive;
}
