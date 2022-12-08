package net.newsportal.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserDetailsResponse {

    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String role;
}
