package com.guerra.RouterSuculentAPI.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class EtiquetaImagenDto implements Serializable {

    private String idSintoma;
    private String consejo;
}