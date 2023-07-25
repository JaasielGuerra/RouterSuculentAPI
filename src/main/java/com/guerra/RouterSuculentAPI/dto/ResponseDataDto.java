package com.guerra.RouterSuculentAPI.dto;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ResponseDataDto implements Serializable {

    private String message;
    private Object data;
    private List<String> errors;
}