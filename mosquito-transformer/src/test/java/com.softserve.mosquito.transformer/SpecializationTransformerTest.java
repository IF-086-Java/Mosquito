package com.softserve.mosquito.transformer;

import com.softserve.mosquito.dtos.SpecializationDto;
import com.softserve.mosquito.entities.Specialization;
import org.junit.Test;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class SpecializationTransformerTest {


    @Test
    public void toEntityList() {
        SpecializationDto specializationDto = new SpecializationDto();
        specializationDto.setId(1L);
        specializationDto.setTitle("JuniorJavaDeveloper");
        SpecializationDto specializationDto1 = new SpecializationDto();
        specializationDto1.setId(2L);
        specializationDto1.setTitle("MiddleJavaDeveloper");
        Set<SpecializationDto> specializationDtoSet = new HashSet<>();
        specializationDtoSet.add(specializationDto);
        specializationDtoSet.add(specializationDto1);
        Set<Specialization> specializations = SpecializationTransformer.toEntityList(specializationDtoSet);
        Iterator<SpecializationDto> iter = specializationDtoSet.iterator();
        Specialization first = specializations.stream().findFirst().get();
        assertEquals(specializationDto1.getTitle(),first.getTitle() );

    }

    @Test
    public void toDTOList() {
        Specialization specialization1 = new Specialization();
        specialization1.setId(1L);
        specialization1.setTitle("BigBoss");
        Specialization specialization2 = new Specialization();
        specialization2.setId(10L);
        specialization2.setTitle("Worker");
        Set<Specialization> specializations = new HashSet<>();
        specializations.add(specialization2);
        specializations.add(specialization1);

        Set<SpecializationDto> specializationDtos = SpecializationTransformer.toDTOList(specializations);
        SpecializationDto firstDto = specializationDtos.stream().findFirst().get();
        assertEquals(specialization2.getTitle(), firstDto.getTitle());
    }

    @Test
    public void toEntity() {
        SpecializationDto specializationDto = new SpecializationDto();
        specializationDto.setId(6L);
        specializationDto.setTitle("CEO");
        Specialization specialization = SpecializationTransformer.toEntity(specializationDto);
        assertEquals(specializationDto.getId(), specialization.getId());
        assertEquals(specializationDto.getTitle(), specialization.getTitle());
    }

    @Test
    public void toDTO() {
        Specialization specialization = new Specialization();
        specialization.setId(2L);
        specialization.setTitle("Director");
        SpecializationDto specializationDto = SpecializationTransformer.toDTO(specialization);
        assertEquals(specialization.getId(), specializationDto.getId());
        assertEquals(specialization.getTitle(), specializationDto.getTitle());
    }
    @Test
    public void toDTO_null() {
        Specialization specializationn = null;
        SpecializationDto specializationDto = SpecializationTransformer.toDTO(specializationn);
        assertNull(specializationDto);
    }
    @Test
    public void toEntity_null() {
        SpecializationDto specializationDto = null;
        Specialization specialization = SpecializationTransformer.toEntity(specializationDto);
        assertNull(specialization);
    }
}
