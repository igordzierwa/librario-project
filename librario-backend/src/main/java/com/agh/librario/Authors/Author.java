package com.agh.librario.Authors;

import com.agh.librario.Picture.Picture;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "authors")
public class Author {
    private @Id @GeneratedValue Long id;
    private String name;
    private Integer year;

//    @OneToMany
//    private Country coutry;

    @OneToOne
    private Picture profile_photo;

    private String description;

    public Author() { }

    public Author(String name, Integer year, Picture profile_photo, String description) {
        this.name = name;
        this.year = year;
        this.profile_photo = profile_photo;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Picture getProfile_photo() {
        return profile_photo;
    }

    public void setProfile_photo(Picture profile_photo) {
        this.profile_photo = profile_photo;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
