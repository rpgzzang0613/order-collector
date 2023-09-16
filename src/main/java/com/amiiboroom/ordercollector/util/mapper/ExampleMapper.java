package com.amiiboroom.ordercollector.util.mapper;

import com.amiiboroom.ordercollector.dto.example.RequestExampleDTO;
import com.amiiboroom.ordercollector.dto.example.ResponseExampleDTO;
import com.amiiboroom.ordercollector.entity.Example;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * Entity <-> DTO 변환용 클래스
 * mybatis에서 쓰는 @Mapper 어노테이션과 다르므로 주의해서 import 해야함
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ExampleMapper {

    ExampleMapper exampleMapper = Mappers.getMapper(ExampleMapper.class);
    ResponseExampleDTO entityToDto(Example entity);
    Example dtoToEntity(RequestExampleDTO dto);

}
