package com.yusuf.lims.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BookRent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinTable(
            joinColumns={@JoinColumn(name="BOOK_RENT_ID", referencedColumnName="ID")},
            inverseJoinColumns={@JoinColumn(name="BOOK_ID", referencedColumnName="ID")})
    private List<Book> books = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinTable(
            joinColumns={@JoinColumn(name="BOOK_RENT_ID", referencedColumnName="ID")},
            inverseJoinColumns={@JoinColumn(name="USER_ID", referencedColumnName="ID")})
    private List<User> users = new ArrayList<>();

    @Column(nullable=false)
    private Date rent_date;

    @Column(nullable=false)
    private Date return_date;
}
