package hash;

import model.Registro;

public class HashMeioQuadrado implements FuncaoHash {
    
    @Override
    public int hash(Registro registro, int tamanhoTabela) {
        long codigo = Long.parseLong(registro.getCodigo());
        long quadrado = codigo * codigo;
        String quadradoStr = String.valueOf(quadrado);
        int comprimento = quadradoStr.length();
        
        int meio = comprimento / 2;
        int digitosPegar = Math.min(6, comprimento);
        int inicio = Math.max(0, meio - digitosPegar / 2);
        int fim = Math.min(comprimento, inicio + digitosPegar);
        
        String digitosMeio = quadradoStr.substring(inicio, fim);
        long valorHash = Long.parseLong(digitosMeio);
        
        return (int) (valorHash % tamanhoTabela);
    }
    
    @Override
    public String getNome() {
        return "Mid-Square (Meio do Quadrado)";
    }
}