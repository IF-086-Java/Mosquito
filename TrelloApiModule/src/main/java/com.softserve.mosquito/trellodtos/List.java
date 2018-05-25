package com.softserve.mosquito.trellodtos;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import java.util.Arrays;

@JsonIgnoreProperties(ignoreUnknown = true)
public class List {

    private String name;
    private String id;
    private Card[] listOfCards;

    public List() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Card[] getListOfCards() {
        return listOfCards;
    }

    public void setListOfCards(Card[] listOfCards) {
        this.listOfCards = listOfCards;
    }

    @Override
    public String toString() {
        return "List{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", listOfCards=" + Arrays.toString(listOfCards) +
                '}';
    }
}
