package hashtable;

import model.Registro;
import metrics.ColetorMetricas;

public interface TabelaHash {
    void inserir(Registro registro, ColetorMetricas metricas);
    boolean buscar(Registro registro);
    int getTamanho();
    String getNomeTecnica();
    void analisarEstrutura(ColetorMetricas metricas);
}