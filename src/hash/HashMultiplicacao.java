package hash;

import model.Registro;

public class HashMultiplicacao implements FuncaoHash {
    // Constante A sugerida por Knuth: (raiz5 - 1) / 2
    private static final double A = 0.6180339887;
    
    @Override
    public int hash(Registro registro, int tamanhoTabela) {
        long codigo = Long.parseLong(registro.getCodigo());
        double temp = codigo * A;
        temp = temp - Math.floor(temp); // Parte fracionaria
        return (int) Math.floor(tamanhoTabela * temp);
    }
    
    @Override
    public String getNome() {
        return "Multiplication (Multiplicacao)";
    }
}