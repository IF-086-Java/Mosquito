package com.softserve.mosquito.trellocontrollers;

import javax.ws.rs.core.Response;

import com.softserve.mosquito.trellodtos.Board;
import com.softserve.mosquito.trellodtos.Card;
import com.softserve.mosquito.trellodtos.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

public class BoardController {

    public Board[] getAllBoards(){
        Board[] boards= null;
        String urlGetAllBoards = "https://api.trello.com/1/members/if086softserve/boards?key=9097df69617e33b2dd4d9fe573570eac&token=b30aab2aa99bd68be5f45032ce63c5568363b2bac0a5c6088743270acdb02493";

        try {

            ResteasyClient client = new ResteasyClientBuilder().build();
            ResteasyWebTarget target = client.target(urlGetAllBoards);

            Response response = target.request().get();

            String responseAsString = response.readEntity(String.class);

            ObjectMapper mapper = new ObjectMapper();

            boards = mapper.readValue(responseAsString, Board[].class);

        } catch (Exception e) {
            System.err.println(e);
        }

        return boards;
    }

    public List[] getListByBoard(String idBoard){
        List[] lists = null;
        String urlGetListOfBoard = "https://api.trello.com/1/boards/"+idBoard+"/lists?cards=open&card_fields=name&fields=name&key=9097df69617e33b2dd4d9fe573570eac&token=b30aab2aa99bd68be5f45032ce63c5568363b2bac0a5c6088743270acdb02493";

        try {

            ResteasyClient client = new ResteasyClientBuilder().build();
            ResteasyWebTarget target = client.target(urlGetListOfBoard);

            Response response = target.request().get();

            String responseAsString = response.readEntity(String.class);

            ObjectMapper mapper = new ObjectMapper();

            lists = mapper.readValue(responseAsString, List[].class);

        } catch (Exception e) {
            System.err.println(e);
        }

        return lists;
    }

    public Card[] getCardsByList(String idList){
        Card[] cards = null;
        String urlGetCardsByList= "https://api.trello.com/1/lists/"+idList+"/cards?key=9097df69617e33b2dd4d9fe573570eac&token=b30aab2aa99bd68be5f45032ce63c5568363b2bac0a5c6088743270acdb02493";

        try {

            ResteasyClient client = new ResteasyClientBuilder().build();
            ResteasyWebTarget target = client.target(urlGetCardsByList);

            Response response = target.request().get();

            String responseAsString = response.readEntity(String.class);

            ObjectMapper mapper = new ObjectMapper();

            cards = mapper.readValue(responseAsString, Card[].class);

        } catch (Exception e) {
            System.err.println(e);
        }

        return cards;


    }

}
