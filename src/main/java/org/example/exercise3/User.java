package org.example.exercise3;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {
    private int userId;
    private int id;
    private String title;
    private Boolean completed;

    public boolean isCompleted() {
        return completed;
    }
}