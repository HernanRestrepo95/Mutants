package com.mutants;

import com.mutants.controller.MutantsController;
import com.mutants.entity.Mutant;
import com.mutants.entity.MutantRequest;
import com.mutants.entity.MutantResponse;
import com.mutants.repository.MutantRepository;
import com.mutants.service.MutantsService;
import com.mutants.service.MutantsServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.junit.Rule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import static org.mockito.Mockito.mock;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@SpringBootTest
public class MutantMockTest {

    @Autowired
    private MutantRepository mutantRepository;

    private MutantsService mutantsService;

    private MutantsController mutantsController;

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @BeforeEach
    public void setup() {
        mutantsService = new MutantsServiceImpl(mutantRepository);
        mutantsController = new MutantsController(mutantsService);
    }

    @Test
    public void validateMutant() {
        BindingResult result = mock(BindingResult.class);

        String[] dnaMut = new String[]{"AAAAAA", "AAAAAA", "AAAAAA", "AAAAAA", "AAAAAA", "AAAAAA"};
        MutantRequest mutantRequest = MutantRequest.builder().dna(dnaMut).build();

        boolean isMutant = mutantsController.validateMutant(mutantRequest, result).getBody();
        Assertions.assertThat(isMutant).isEqualTo(true);

        dnaMut = new String[]{"CTGCGA", "CAGTGC", "TTATGT", "AGAAGG", "ACCCTA", "TCACTG"};
        mutantRequest = MutantRequest.builder().dna(dnaMut).build();
        HttpStatus httpStatus = mutantsController.validateMutant(mutantRequest, result).getStatusCode();
        Assertions.assertThat(httpStatus).isEqualTo(HttpStatus.FORBIDDEN);

        try {
            String[] dnaExc = new String[]{"ATGCGA,CAGTGC,TTATGT,AGAAGG,CCCCTA,TCACTG"};
            mutantRequest = MutantRequest.builder().dna(dnaExc).build();
            mutantsController.validateMutant(mutantRequest, result);
        } catch (ResponseStatusException e) {
            Assertions.assertThat(e.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        try {
            String[] dnaExc2 = new String[]{"TGCGA", "CAGTGC", "TTATGT", "AGAAGG", "ACCCTA", "TCACTG"};
            mutantRequest = MutantRequest.builder().dna(dnaExc2).build();
            mutantsController.validateMutant(mutantRequest, result);
        } catch (ResponseStatusException e) {
            Assertions.assertThat(e.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        try {
            String[] dnaExc3 = new String[]{"ZTGCGA", "CAGTGC", "TTATGT", "AGAAGG", "ACCCTA", "TCACTG"};
            mutantRequest = MutantRequest.builder().dna(dnaExc3).build();
            mutantsController.validateMutant(mutantRequest, result);
        } catch (ResponseStatusException e) {
            Assertions.assertThat(e.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        try {
            mutantsController.validateMutant(null, result);
        } catch (ResponseStatusException e) {
            Assertions.assertThat(e.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
        }
    }

    @Test
    public void getStats() {

        Long nextVal = mutantRepository.getNextVal();
        String dnaMut = "ATGCGA,CAGTGC,TTATGT,AGAAGG,CCCCTA,TCACTG";
        Mutant mutan = Mutant.builder().id(nextVal).dna(dnaMut).isMutant(true).build();

        Mutant found = mutantRepository.findByDna(dnaMut);
        if (found == null) {
            mutantRepository.save(mutan);
            nextVal = mutantRepository.getNextVal();
        }

        String dnaHum = "CTGCGA,CAGTGC,TTATGT,AGAAGG,ACCCTA,TCACTG";
        mutan = Mutant.builder().id(nextVal).dna(dnaHum).isMutant(true).build();

        found = mutantRepository.findByDna(dnaHum);
        if (found == null) {
            mutantRepository.save(mutan);
        }

        ResponseEntity<MutantResponse> mutantResponseEntity = mutantsController.getStats();
        MutantResponse mutantResponse = mutantResponseEntity.getBody();

        Assertions.assertThat(mutantResponse.getCount_human_dna()).isGreaterThan(0);
        Assertions.assertThat(mutantResponse.getCount_mutant_dna()).isGreaterThan(0);

        mutantsController.getStats();
    }

}
