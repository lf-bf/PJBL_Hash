package hashtable;

import model.Registro;
import hash.FuncaoHash;
import metrics.ColetorMetricas;

public class TabelaHashQuadratica implements TabelaHash {
    private Registro[] tabela;
    private boolean[] ocupado;
    private int tamanho;
    private FuncaoHash funcaoHash;
    
    public TabelaHashQuadratica(int tamanho, FuncaoHash funcaoHash) {
        this.tamanho = tamanho;
        this.tabela = new Registro[tamanho];
        this.ocupado = new boolean[tamanho];
        this.funcaoHash = funcaoHash;
    }
    
    @Override
    public void inserir(Registro registro, ColetorMetricas metricas) {
        int indice = funcaoHash.hash(registro, tamanho);
        int indiceOriginal = indice;
        int colisoes = 0;
        int i = 1;
        
        while (ocupado[indice]) {
            if (tabela[indice].equals(registro)) {
                metricas.adicionarColisao(colisoes);
                return;
            }
            colisoes++;
            
            // Calcular novo índice com proteção contra overflow
            // Usar Math.abs e garantir que o resultado seja positivo
            long deslocamento = (long) i * i;
            indice = (int) ((indiceOriginal + deslocamento) % tamanho);
            
            // Garantir que o índice seja sempre positivo
            if (indice < 0) {
                indice = (indice + tamanho) % tamanho;
            }
            
            i++;
            
            // Evitar loop infinito
            if (i > tamanho) {
                metricas.adicionarColisao(colisoes);
                return;
            }
        }
        
        tabela[indice] = registro;
        ocupado[indice] = true;
        metricas.adicionarColisao(colisoes);
    }
    
    @Override
    public boolean buscar(Registro registro) {
        int indice = funcaoHash.hash(registro, tamanho);
        int indiceOriginal = indice;
        int i = 1;
        
        while (ocupado[indice]) {
            if (tabela[indice].equals(registro)) {
                return true;
            }
            
            // Calcular novo índice com proteção contra overflow
            long deslocamento = (long) i * i;
            indice = (int) ((indiceOriginal + deslocamento) % tamanho);
            
            // Garantir que o índice seja sempre positivo
            if (indice < 0) {
                indice = (indice + tamanho) % tamanho;
            }
            
            i++;
            
            if (i > tamanho) {
                break;
            }
        }
        return false;
    }
    
    @Override
    public int getTamanho() {
        return tamanho;
    }
    
    @Override
    public String getNomeTecnica() {
        return "Rehashing Quadratico";
    }
    
    @Override
    public void analisarEstrutura(ColetorMetricas metricas) {
        metricas.setTresMaioresListas(new int[]{0, 0, 0});
        
        int ultimoOcupado = -1;
        int gapMinimo = Integer.MAX_VALUE;
        int gapMaximo = 0;
        long somaGaps = 0;
        int contagemGaps = 0;
        
        for (int i = 0; i < tamanho; i++) {
            if (ocupado[i]) {
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