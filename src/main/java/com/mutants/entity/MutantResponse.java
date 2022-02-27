/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mutants.entity;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
/**
 *
 * @author Hernan_Restrepo
 */

@Getter
@AllArgsConstructor
@Builder
public class MutantResponse implements Serializable {

    private int count_mutant_dna;
    private int count_human_dna;
    private double ratio;
}
