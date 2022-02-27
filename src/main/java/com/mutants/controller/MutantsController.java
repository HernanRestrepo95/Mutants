package com.mutants.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.mutants.entity.MutantRequest;
import com.mutants.entity.MutantResponse;
import com.mutants.service.MutantsService;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class MutantsController {

    @Autowired
    private MutantsService mutantsService;

    @GetMapping(value = "/stats")
    public ResponseEntity<MutantResponse> getStats() {
        return ResponseEntity.ok(mutantsService.getStats());
    }

    @PostMapping(value = "/mutant")
    public ResponseEntity<Boolean> validateMutant(@Valid @RequestBody MutantRequest mutant, BindingResult result) {
        try {
            //Valida si el objeto de ingreso no es nulo y tiene sus propiedades completas
            if (mutant == null || result.hasErrors()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, this.formatMessage(result));
            }
            boolean blMutant = mutantsService.validateMutant(mutant);
            
            if (blMutant) {
                return ResponseEntity.ok(blMutant);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    private String formatMessage(BindingResult result) {
        List<Map<String, String>> errors = result.getFieldErrors().stream().map(err -> {
            Map<String, String> error = new HashMap<>();
            error.put(err.getField(), err.getDefaultMessage());
            return error;

        }).collect(Collectors.toList());
        ErrorMessage errorMessage = ErrorMessage.builder().code("01").messages(errors).build();
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "";
        try {
            jsonString = mapper.writeValueAsString(errorMessage);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return jsonString;
    }
}
