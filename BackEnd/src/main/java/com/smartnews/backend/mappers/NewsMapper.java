package com.smartnews.backend.mappers;

import com.smartnews.backend.dtos.SaveBasicNewsDto;
import com.smartnews.backend.entities.News;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NewsMapper {
    @Mapping(source = "categoryId", target = "category.id")
    @Mapping(source = "headline",target = "headline")
    News toEntity(SaveBasicNewsDto request);

}
