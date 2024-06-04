package com.vpactually.mappers;

import com.vpactually.dto.labels.LabelCreateDTO;
import com.vpactually.dto.labels.LabelDTO;
import com.vpactually.dto.labels.LabelUpdateDTO;
import com.vpactually.entities.Label;
import org.mapstruct.*;

@Mapper(
        uses = JsonNullableMapper.class,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class LabelMapper {

    public abstract Label map(LabelCreateDTO labelCreateDTO);

    public abstract LabelDTO map(Label label);

    public abstract void update(@MappingTarget Label label, LabelUpdateDTO labelUpdateDTO);
}
