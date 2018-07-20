package com.softserve.mosquito.transformer;

import com.softserve.mosquito.dtos.TrelloInfoDto;
import com.softserve.mosquito.entities.TrelloInfo;

import java.util.ArrayList;
import java.util.List;

public class TrelloInfoTransformer {

    private TrelloInfoTransformer() {
        throw new IllegalStateException("Utility class");
    }

    public static TrelloInfoDto toDto(TrelloInfo trelloInfo) {
        if (trelloInfo == null) {
            return null;
        } else
            return new TrelloInfoDto(
                    trelloInfo.getId(),
                    trelloInfo.getUser().getId(),
                    trelloInfo.getUserTrelloName(),
                    trelloInfo.getUserTrelloKey(),
                    trelloInfo.getUserTrelloToken());
    }

    public static List<TrelloInfoDto> toDto(List<TrelloInfo> trelloInfos) {
        if (trelloInfos == null) return new ArrayList<>();
        else {
            List<TrelloInfoDto> trelloInfoDtos = new ArrayList<>();
            for (TrelloInfo info : trelloInfos) {
                trelloInfoDtos.add(toDto(info));
            }
            return trelloInfoDtos;
        }
    }
}
