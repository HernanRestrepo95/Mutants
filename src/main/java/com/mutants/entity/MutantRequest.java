/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mutants.entity;

import java.io.Serializable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
/**
 *
 * @author Hernan_Restrepo
 */
@Data
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MutantRequest implements Serializable {
    
    @NotEmpty(message = "El campo dna debe contener un arreglo de ADN")   
    @NotNull(message = "El campo dna debe contener un arreglo de ADN")
    private String[] dna;
}
