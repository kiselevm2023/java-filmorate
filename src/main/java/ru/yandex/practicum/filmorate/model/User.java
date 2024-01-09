package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
@Builder
public class User {

    private int id;
    @NotNull
    @NotBlank
    @Email(message = "Email must contain the @ symbol")
    private String email;
    @NotNull
    @NotBlank
    private String login;
    private String name;
    @PastOrPresent
    private LocalDate birthday;
    //private Set<Integer> friends = new HashSet<>();

    /* public HashSet<Integer> getFriends() {
        return new HashSet<>(friends);
    }

    public void setFriends(int id) {
        friends.add(id);
    }

    public void deleteFriend(int id) {
        friends.remove(id);
    }  */
}