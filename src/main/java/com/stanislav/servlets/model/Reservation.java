package com.stanislav.servlets.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {
    private String id;
    private String userId;
    private String bookId;
    private long date;
}
