package com.smartnews.backend.mappers;

import com.smartnews.backend.dtos.SaveBasicNewsDto;
import com.smartnews.backend.dtos.SearchResultDto;
import com.smartnews.backend.entities.News;
import org.mapstruct.*;

import java.util.List;


@Mapper(componentModel = "spring")
public interface NewsMapper {
    @Mapping(source = "categoryId", target = "category.id")
    @Mapping(source = "headline", target = "headline")
    News toEntity(SaveBasicNewsDto request);

//    @Mapping(source = "sentimentID", target = "sentiment.id")
//    @Mapping(source = "categoryID", target = "category.id")
//    News toEntity(SearchResultDto searchDto);
//
//    @InheritInverseConfiguration(name = "toEntity")
//    List<SearchDto> toSearchDtoList(List<News> news);
//
//    @InheritInverseConfiguration(name = "toEntity")
//    SearchDto toSearchDto(News news);

}
