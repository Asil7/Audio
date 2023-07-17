package com.example.book.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;
import org.apache.poi.hssf.record.StyleRecord;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity(name = "channels")
public class Channel {

    public Channel(String name){
        this.name = name;
    }

    public Channel(String name, String nameKr, String link, Double price) {
        this.name = name;
        this.nameKr = nameKr;
        this.link = link;
        this.price = price;


    }

    @Id
    @GeneratedValue
    private Integer id;
    private String name;
    private String nameKr;
    private String link;
    private Double price;
    private String card = "9860012405287272";

    @Override
    public String toString() {
        return
              "\n" +  name + " - " +  price + " so'm\n";

    }
}
