/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mutants.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mutants.entity.Mutant;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author Hernan_Restrepo
 */
@Repository
public interface MutantRepository extends JpaRepository<Mutant, Long> {

    Mutant findByDna(String dna);
    
    @Query(value = "SELECT next_val FROM mutants.hibernate_sequence", nativeQuery = true)
    Long getNextVal();
}
