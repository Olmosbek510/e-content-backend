package com.inha.os.econtentbackend.dto.request;

import com.inha.os.econtentbackend.entity.interfaces.Actions;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActionRequestDto {
    private Actions action;
}
