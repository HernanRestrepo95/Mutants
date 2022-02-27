/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mutants.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
/**
 *
 * @author Hernan_Restrepo
 */
@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Mutant implements Serializable {
    
    @Id
    @GeneratedValue
    private Long id;
    
    @Column(name = "dna")
    @NotEmpty(message = "El campo dna debe contener un arreglo de ADN")   
    @NotNull(message = "El campo dna debe contener un arreglo de ADN")
    private String dna;
    
    @Column(name = "is_mutan")
    private boolean isMutant;
}
