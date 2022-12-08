package net.newsportal.payload.request;

import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
public class UserRegisterRequest {
    @NotBlank
    @Size(max = 20)
    private String username;

    @NotBlank
    @Size(min = 2, max = 10)
    private String firstName;

    @NotBlank
    @Size(min = 2, max = 15)
    private String lastName;

    @NotBlank
    @Email
    @Size(max = 50)
    private String email;

    @NotBlank
    @Size(min = 6, max = 50)
    private String password;

    private String role;

    @Override
    public String toString() {
        return "request{" +
                "\n username='" + username + '\'' +
                "\n firstName='" + firstName + '\'' +
                "\n lastName='" + lastName + '\'' +
                "\n email='" + email + '\'' +
                "\n password='" + password + '\'' +
                "\n role='" + role + '\'' +
                "\n}";
    }
}
