package com.iremayvaz.model.dto.response;

import com.iremayvaz.model.enums.Concentration;
import com.iremayvaz.model.enums.Gender;
import com.iremayvaz.model.enums.Season;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor

public class CategoryDetailResponse {
    private Long id;
    private Gender gender;
    private Concentration concentrationName;
    private Season season;
    private String accord;
}
