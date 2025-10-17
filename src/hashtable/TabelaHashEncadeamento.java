package hashtable;

import model.Registro;
import hash.FuncaoHash;
import metrics.ColetorMetricas;

public class TabelaHashEncadeamento implements TabelaHash {
    private No[] tabela;
    private int tamanho;
    private FuncaoHash funcaoHash;
    
    public TabelaHashEncadeamento(int tamanho, FuncaoHash funcaoHash) {
        this.tamanho = tamanho;
        this.tabela = new No[tamanho];
        this.funcaoHash = funcaoHash;
    }
    
    @Override
    public void inserir(Registro registro, ColetorMetricas metricas) {
        int indice = funcaoHash.hash(registro, tamanho);
        int colisoes = 0;
        No atual = tabela[indice];
        
        while (atual != null) {
            colisoes++;
            if (atual.getRegistro().equals(registro)) {
                metricas.adicionarColisao(colisoes);
                return;
            }
            atual = atual.getProximo();
        }
        
        No novoNo = new No(registro);
        novoNo.setProximo(tabela[indice]);
        tabela[indice] = novoNo;
        metricas.adicionarColisao(colisoes);
    }
    
    @Override
    public boolean buscar(Registro registro) {
        int indice = funcaoHash.hash(registro, tamanho);
        No atual = tabela[indice];
        
        while (atual != null) {
            if (atual.getRegistro().equals(registro)) {
                return true;
            }
            atual = atual.getProximo();
        }
        return false;
    }
    
    @Override
    public int getTamanho() {
        return tamanho;
    }
    
    @Override
    public String getNomeTecnica() {
        return "Encadeamento (Chaining)";
    }
    
    @Override
    public void analisarEstrutura(ColetorMetricas metricas) {
        int[] tamanhosListas = new int[tamanho];
        
        for (int i = 0; i < tamanho; i++) {
            int tamanhoLista = 0;
            No atual = tabela[i];
            while (atual != null) {
                tamanhoLista++;
                atual = atual.getProximo();
            }
            tamanhosListas[i] = tamanhoLista;
        }
        
        for (int i = 0; i < tamanho - 1; i++) {
            for (int j = 0; j < tamanho - i - 1; j++) {
                if (tamanhosListas[j] < tamanhosListas[j + 1]) {
                    int temp = tamanhosListas[j];
                    tamanhosListas[j] = tamanhosListas[j + 1];
                    tamanhosListas[j + 1] = temp;
                }
            }
        }
        
        int[] top3 = new int[3];
        for (int i = 0; i < 3 && i < tamanho; i++) {
            top3[i] = tamanhosListas[i];
        }
        metricas.setTresMaioresListas(top3);
        
        int ultimoOcupado = -1;
        int gapMinimo = Integer.MAX_VALUE;
        int gapMaximo = 0;
        long somaGaps = 0;
        int contagemGaps = 0;
        
        for (int i = 0; i < tamanho; i++) {
            if (tabela[i] != null) {
                if (ultimoOcupado != -1) {
                    int gap = i - ultimoOcupado - 1;
                    gapMinimo = Math.min(gapMinimo, gap);
                    gapMaximo = Math.max(gapMaximo, gap);
                    somaGaps += gap;
                    contagemGaps++;
                }
                ultimoOcupado = i;
            }
        }
        
        if (contagemGaps > 0) {
            metricas.setGapMinimo(gapMinimo);
            metricas.setGapMaximo(gapMaximo);
            metricas.setGapMedio(somaGaps / (double) contagemGaps);
        } else {
            metricas.setGapMinimo(0);
            metricas.setGapMaximo(0);
            metricas.setGapMedio(0);
        }
    }
}