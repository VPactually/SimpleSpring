package com.vpactually.dto.labels;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LabelUpdateDTO {
    private JsonNullable<String> name;
}
