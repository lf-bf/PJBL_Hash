package metrics;

public class ColetorMetricas {
    private long colisaoTotais;
    private long tempoInsercao;
    private long tempoBusca;
    private int[] tresMaioresListas;
    private int gapMinimo;
    private int gapMaximo;
    private double gapMedio;
    private int contagemElementos;
    
    public ColetorMetricas() {
        this.colisaoTotais = 0;
        this.tempoInsercao = 0;
        this.tempoBusca = 0;
        this.tresMaioresListas = new int[3];
        this.contagemElementos = 0;
    }
    
    public void adicionarColisao(int colisoes) {
        this.colisaoTotais += colisoes;
        this.contagemElementos++;
    }
    
    public void setTempoInsercao(long tempo) {
        this.tempoInsercao = tempo;
    }
    
    public void setTempoBusca(long tempo) {
        this.tempoBusca = tempo;
    }
    
    public void setTresMaioresListas(int[] listas) {
        this.tresMaioresListas = listas;
    }
    
    public void setGapMinimo(int gap) {
        this.gapMinimo = gap;
    }
    
    public void setGapMaximo(int gap) {
        this.gapMaximo = gap;
    }
    
    public void setGapMedio(double gap) {
        this.gapMedio = gap;
    }
    
    public long getColisaoTotais() {
        return colisaoTotais;
    }
    
    public double getColisaoMedia() {
        return contagemElementos > 0 ? (double) colisaoTotais / contagemElementos : 0;
    }
    
    public long getTempoInsercao() {
        return tempoInsercao;
    }
    
    public long getTempoBusca() {
        return tempoBusca;
    }
    
    public int[] getTresMaioresListas() {
        return tresMaioresListas;
    }
    
    public int getGapMinimo() {
        return gapMinimo;
    }
    
    public int getGapMaximo() {
        return gapMaximo;
    }
    
    public double getGapMedio() {
        return gapMedio;
    }
    
    @Override
    public String toString() {
        return String.format(
            "Colisoes Totais: %d\n" +
            "Colisoes Medias: %.4f\n" +
            "Tempo Insercao: %d ms\n" +
            "Tempo Busca: %d ms\n" +
            "3 Maiores Listas: [%d, %d, %d]\n" +
            "Gap Minimo: %d\n" +
            "Gap Maximo: %d\n" +
            "Gap Medio: %.2f",
            colisaoTotais,
            getColisaoMedia(),
            tempoInsercao,
            tempoBusca,
            tresMaioresListas[0], tresMaioresListas[1], tresMaioresListas[2],
            gapMinimo, gapMaximo, gapMedio
        );
    }
}