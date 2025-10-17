package hash;

import model.Registro;

public interface FuncaoHash {
    int hash(Registro registro, int tamanhoTabela);
    String getNome();
}