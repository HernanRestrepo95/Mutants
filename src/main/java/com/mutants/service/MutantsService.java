package com.mutants.service;

import com.mutants.entity.MutantRequest;
import com.mutants.entity.MutantResponse;


public interface MutantsService {
    public MutantResponse getStats();
    public boolean validateMutant(MutantRequest mutant) throws Exception;
}
