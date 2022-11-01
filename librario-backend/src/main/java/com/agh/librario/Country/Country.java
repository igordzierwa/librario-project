package com.agh.librario.Country;

import com.agh.librario.Picture.Picture;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "countries")
public class Country {
    private @Id @GeneratedValue Long id;
    private String name;

    @OneToOne
    private Picture flag;




}
