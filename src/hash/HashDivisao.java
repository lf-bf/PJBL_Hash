package hash;

import model.Registro;

public class HashDivisao implements FuncaoHash {
    
    @Override
    public int hash(Registro registro, int tamanhoTabela) {
        long codigo = Long.parseLong(registro.getCodigo());
        return (int) (codigo % tamanhoTabela);
    }
    
    @Override
    public String getNome() {
        return "Division (Resto da Divisao)";
    }
}