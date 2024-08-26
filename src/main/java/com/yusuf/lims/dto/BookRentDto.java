package com.yusuf.lims.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookRentDto {

    private Long id;
    private Long bookId;
    private String bookName;
    private Long userId;
    private String userName;
    private Date rentDate;
    private Date returnDate;
}

