package com.mutants.service;

import com.mutants.repository.MutantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.mutants.entity.Mutant;
import com.mutants.entity.MutantRequest;
import com.mutants.entity.MutantResponse;

import static java.lang.Math.min;
import static java.lang.Math.max;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MutantsServiceImpl implements MutantsService {

    private final MutantRepository mutantsRepository;

    private static final String ALLOW_VALUES = "ATCG";
    private static final int OCURRENCE_MUTANT = 4;
    private static final int IS_MUTANT = 2;

    @Override
    public MutantResponse getStats() {

        //Obtiene la lista de ADN verificados junto con sus tipos
        List<Mutant> lstMutants = mutantsRepository.findAll();

        //Cuenta la cantidad de ADNs correspondientes a Mutantes
        double countMutant = lstMutants.stream().filter(mutant -> mutant.isMutant()).count();
        //Cuenta la cantidad de ADNs correspondientes a Humanos
        double countHuman = lstMutants.stream().filter(mutant -> !mutant.isMutant()).count();

        //Calcula el ratio, se realiza una validacion ternaria para evitar errores al dividir por cero
        double ratio = (countHuman > 0.0) ? (countMutant / countHuman) : 0.0;

        //Realiza redondeo por si hay demasiados decimales
        ratio = ratio * 100;
        ratio = Math.round(ratio);
        ratio = ratio / 100;

        MutantResponse mutantResponse = MutantResponse.builder().count_mutant_dna((int) countMutant).count_human_dna((int) countHuman).ratio(ratio).build();

        return mutantResponse;
    }

    @Override
    public boolean validateMutant(MutantRequest mutantRequest) throws Exception {

        //Obtiene el arreglo de ADN
        String[] arrayDna = mutantRequest.getDna();

        //Valida si en el arreglo existen 6 posiciones
        if (arrayDna.length != 6) {
            throw new Exception("El arreglo debe ser de 6 posiciones");
        }

        //Calcula tamaño de la matriz
        int n = arrayDna.length;
        int m = arrayDna[0].length();

        int countOcurrences = 0;

        //Suma las ocurrencias donde se encuentra la secuencia con mas de cuatro letras de forma horizontal y vertical 
        countOcurrences += validateHorVer(arrayDna, n, m);
        //Suma las ocurrencias donde se encuentra la secuencia con mas de cuatro letras de forma oblicua
        countOcurrences += validateObl(arrayDna, n, m);
        //Suma las ocurrencias donde se encuentra la secuencia con mas de cuatro letras de forma inversa
        countOcurrences += validateOblInv(arrayDna, n, m);

        //Es mutante si hay dos o mas ocurrencias
        boolean isMutant = countOcurrences >= IS_MUTANT;

        //Guarda el registro si no existe otro ADN igual en base de datos
        String dna = String.join(",", mutantRequest.getDna());
        if (mutantsRepository.findByDna(dna) == null) {
            Mutant mutant = Mutant.builder().dna(dna).isMutant(isMutant).build();
            mutantsRepository.save(mutant);
        }

        //Retorna si es mutante o no
        return isMutant;
    }

    //Metodo que valida si los caracteres ingresados corresponden a (A,T,C,G)
    private void isValidChar(char charAt) throws Exception {
        if (ALLOW_VALUES.indexOf(charAt) < 0) {
            throw new Exception(charAt + " no es un caracter valido");
        }
    }

    //Metodo que cuenta la cantidad de caracteres en secuencia en una palabra, si existe secuencia devuelve 1
    private int countCharacters(String dna) {

        char[] arrayChar = dna.toCharArray();
        Character charFind = arrayChar[0];

        int count = 1;

        //Recorrido hasta el final de la palabra o hasta encontrar ya una coincidencia
        for (int i = 0; i < arrayChar.length && count < 4; i++) {
            char character = arrayChar[i];

            //Si son los mismos caracteres suma al contador
            if (charFind == character) {
                count++;
            } //Dee lo contrario reinicia el contador en 1 y asigna el nuevo caracter
            else {
                charFind = character;
                count = 1;
            }
        }

        return count >= OCURRENCE_MUTANT ? 1 : 0;
    }

    //Metodo que obtiene las palabras que se forman de manera horizontal y vertical, anexo a esto devuelve la cantidad de veces que hayan coincidencias
    private int validateHorVer(String[] arrayDna, int n, int m) throws Exception {
        int countOcurrences = 0;

        //Recorrido de forma vertical y horizontal
        for (int i = 0; i < n; i++) {
            String dna = arrayDna[i];
            if (dna.length() != 6) {
                throw new Exception("Cada cadena de texto debe contener 6 caracteres");
            }

            StringBuilder sbDnaHor = new StringBuilder();
            StringBuilder sbDnaVert = new StringBuilder();

            for (int j = 0; j < m; j++) {
                //Concatena y valida cada caracter
                char characterA = arrayDna[i].toCharArray()[j];
                isValidChar(characterA);
                sbDnaHor.append(characterA);

                char characterB = arrayDna[j].toCharArray()[i];
                isValidChar(characterB);
                sbDnaVert.append(characterB);
            }

            //Obtiene cada palabra
            String dnaHor = sbDnaHor.toString();
            String dnaVert = sbDnaVert.toString();

            //Si la palabra formada tiene mas de 4 caracteres, valida y cuenta
            if (dnaHor.length() >= OCURRENCE_MUTANT) {
                countOcurrences += countCharacters(dnaHor);
            }
            if (dnaVert.length() >= OCURRENCE_MUTANT) {
                countOcurrences += countCharacters(dnaVert);
            }
        }

        return countOcurrences;
    }

    //Metodo que obtiene las palabras que se forman de oblicua, anexo a esto devuelve la cantidad de veces que hayan coincidencias
    private int validateObl(String[] arrayDna, int n, int m) throws Exception {
        int countOcurrences = 0;
        
        //Recorrido de forma oblicua
        for (int i = 0; i <= m + n - 2; i++) {
            StringBuilder sbDnaObl = new StringBuilder();

            for (int x = 0; x <= i && (x < n); x++) {
                //Si el indice es menor al tamaño
                if (i - x < m) {
                    //Concatena y valida cada caracter
                    char character = arrayDna[x].toCharArray()[i - x];
                    isValidChar(character);

                    sbDnaObl.append(character);
                }
            }
            //Obtiene la palabra
            String dnaObl = sbDnaObl.toString();
            
            //Si la palabra formada tiene mas de 4 caracteres, valida y cuenta
            if (dnaObl.length() >= OCURRENCE_MUTANT) {
                countOcurrences += countCharacters(dnaObl);
            }
        }
        return countOcurrences;
    }

    //Metodo que obtiene las palabras que se forman de oblicua inversa, anexo a esto devuelve la cantidad de veces que hayan coincidencias
    private int validateOblInv(String[] arrayDna, int n, int m) throws Exception {
        int countOcurrences = 0;
        
        //Recorrido de forma oblicua inversa
        for (int i = 1 - m; i < n; i++) {
            StringBuilder sbDnaOblInv = new StringBuilder();

            for (int x = -min(0, i), y = max(0, i); x < m && y < n; x++, y++) {
                //Concatena y valida cada caracter
                char character = arrayDna[y].toCharArray()[x];
                isValidChar(character);

                sbDnaOblInv.append(character);
            }

            //Obtiene la palabra
            String dnaOblInv = sbDnaOblInv.toString();
            
            //Si la palabra formada tiene mas de 4 caracteres, valida y cuenta
            if (dnaOblInv.length() >= OCURRENCE_MUTANT) {
                countOcurrences += countCharacters(dnaOblInv);
            }
        }
        return countOcurrences;
    }
}
