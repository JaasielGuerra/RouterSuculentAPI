package com.guerra.RouterSuculentAPI.controller;

import com.google.gson.Gson;
import com.guerra.RouterSuculentAPI.dto.EtiquetaImagenDto;
import com.guerra.RouterSuculentAPI.dto.ResponseDataDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin
@Log4j2
@RestController
public class RouterRestController {

    private RestTemplate restTemplate;

    @Value("${url-api}")
    private String urlApi;

    @Autowired
    public RouterRestController(RestTemplate restTemplate) {

        this.restTemplate = restTemplate;
        log.info("RouterRestController init");
    }

    @GetMapping("/sintomas")
    @ResponseBody
    public ResponseEntity<ResponseDataDto> consultarSintomasBasico() {

        ResponseDataDto respuestaConsultaSintomas = new ResponseDataDto();

        log.info("consultando sintomas");

        try{

            ResponseEntity<ResponseDataDto> responseEntity = restTemplate.getForEntity(urlApi + "/sintomas", ResponseDataDto.class);
            respuestaConsultaSintomas = responseEntity.getBody();

            log.info("respuesta: {}", respuestaConsultaSintomas);

        } catch (HttpServerErrorException e) {
            log.error("Error al consultar sintomas", e);

            String responseBody = e.getResponseBodyAsString();
            ResponseDataDto responseDataDto = new Gson().fromJson(responseBody, ResponseDataDto.class);

            return ResponseEntity.internalServerError().body(responseDataDto);

        }

        return ResponseEntity.ok(respuestaConsultaSintomas);
    }


    @PostMapping("/registrar-suculenta")
    @ResponseBody
    public ResponseEntity<ResponseDataDto> registrarSuculenta(
            @ModelAttribute EtiquetaImagenDto etiqueta,
            @RequestParam("fotos") List<MultipartFile> fotos
    ) {

        ResponseDataDto respuestaRegistroSuculenta = new ResponseDataDto();

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("idSintoma", etiqueta.getIdSintoma());
        body.add("consejo", etiqueta.getConsejo());
        fotos.forEach(foto -> {
            body.add("fotos", foto.getResource());
        });

        // form-data
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        try{

            ResponseEntity<ResponseDataDto> responseEntity = restTemplate.postForEntity(urlApi + "/registrar-suculenta", requestEntity, ResponseDataDto.class);
            respuestaRegistroSuculenta = responseEntity.getBody();

            log.info("respuesta: {}", respuestaRegistroSuculenta);

        } catch (HttpClientErrorException e){

            log.error("Error al registrar suculenta", e);

            String responseBody = e.getResponseBodyAsString();
            ResponseDataDto responseDataDto = new Gson().fromJson(responseBody, ResponseDataDto.class);

            return ResponseEntity.badRequest().body(responseDataDto);

        } catch (HttpServerErrorException e){

            log.error("Error al registrar suculenta", e);

            String responseBody = e.getResponseBodyAsString();
            ResponseDataDto responseDataDto = new Gson().fromJson(responseBody, ResponseDataDto.class);

            return ResponseEntity.internalServerError().body(responseDataDto);

        }

        return ResponseEntity.ok(respuestaRegistroSuculenta);
    }

}
