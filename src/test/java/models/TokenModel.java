package models;

import lombok.Data;

@Data
public class TokenModel {
    private String token;
    private String expires;
    private String status;
    private String result;
}
