package com.agh.librario.Book;

import com.agh.librario.Picture.Picture;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String author;

    private int length;

    private int stockCount;

    private String description;

    @Enumerated(EnumType.STRING)
    private Category category;

    @OneToOne
    private Picture cover_photo;

    public Book(){}

    public void setcover_photo(Picture cover_photo) {
        this.cover_photo = cover_photo;
    }

    public void incrementStockCount() {
        this.stockCount++;
    }

    public void decrementStockCount() {
        this.stockCount--;
    }
}
