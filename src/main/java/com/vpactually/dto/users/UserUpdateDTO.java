package com.vpactually.dto.users;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateDTO {
    private JsonNullable<String> name;
    private JsonNullable<String> email;

    public UserUpdateDTO(String name, String email) {
        this.name = JsonNullable.of(name);
        this.email = JsonNullable.of(email);
    }
}
