package com.softserve.mosquito.trellodtos;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import java.util.Arrays;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Board {

    private String name;
    private String id;
    private String descData;
    private String desc;

    private List[] listOfBoards;

    public Board() {
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

    public String getDescData() {
        return descData;
    }

    public void setDescData(String descData) {
        this.descData = descData;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List[] getListOfBoards() {
        return listOfBoards;
    }

    public void setListOfBoards(List[] listOfBoards) {
        this.listOfBoards = listOfBoards;
    }

    @Override
    public String toString() {
        return "Board{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", descData='" + descData + '\'' +
                ", desc='" + desc + '\'' +
                ", listOfBoards=" + Arrays.toString(listOfBoards) +
                '}';
    }
}
