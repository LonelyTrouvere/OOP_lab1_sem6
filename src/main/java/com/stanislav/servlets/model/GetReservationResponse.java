package com.stanislav.servlets.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetReservationResponse {
    private  String id;
    private  String bookId;
    private String bookName;
    private long date;
}
