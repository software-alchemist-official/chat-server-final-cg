package main.model;

import lombok.*;

import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@EqualsAndHashCode
public class Message {

    private int id;
    private int userId;
    private String content;
    private Timestamp timestamp;

}
