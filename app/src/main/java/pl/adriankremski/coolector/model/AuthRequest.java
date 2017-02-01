package pl.adriankremski.coolector.model;

public class AuthRequest {
    private String email;
    private String password;
    private String provider;

    public AuthRequest(String email, String password, String provider) {
        this.email = email;
        this.password = password;
        this.provider = provider;
    }
}
