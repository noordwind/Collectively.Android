package pl.adriankremski.coolector.model;

public class SignUpRequest {
    private String name;
    private String email;
    private String password;

    public SignUpRequest(String username, String email, String password) {
        this.name = username;
        this.email = email;
        this.password = password;
    }

    public String getUsername(){
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
