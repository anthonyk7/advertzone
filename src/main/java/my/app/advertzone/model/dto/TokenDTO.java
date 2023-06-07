package my.app.advertzone.model.dto;

public class TokenDTO {

    private String token;

    public TokenDTO(String token) {
        this.token = token;
    }

    public TokenDTO() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "TokenDTO{" +
                "token='" + token + '\'' +
                '}';
    }
}