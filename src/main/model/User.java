package main.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@EqualsAndHashCode
public class User {
    private int id;
    private String username;
    private String password;
    private String email;
}
