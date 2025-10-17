import model.Registro;
import hash.*;
import hashtable.*;
import metrics.ColetorMetricas;
import generator.GeradorDados;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final int TAMANHO_DATASET_PADRAO = 10000;
    private static final int[] TAMANHOS_TABELA = {1000, 10000, 100000};

    public static void main(String[] args) {
        if (args.length < 1) {
            imprimirUso();
            return;
        }

        String nomeDataset = args[0];
        int tamanhoDataset = TAMANHO_DATASET_PADRAO;

        // Se segundo parâmetro foi fornecido, usa ele como tamanho do dataset
        if (args.length >= 2) {
            try {
                tamanhoDataset = Integer.parseInt(args[1]);
                if (tamanhoDataset <= 0) {
                    System.err.println("Erro: Tamanho do dataset deve ser maior que 0!");
                    imprimirUso();
                    return;
                }
            } catch (NumberFormatException e) {
                System.err.println("Erro: Tamanho do dataset invalido: " + args[1]);
                imprimirUso();
                return;
            }
        }

        long semente = nomeDataset.hashCode();

        System.out.println("==============================================");
        System.out.println("   ANALISE DE TABELAS HASH - PJBL");
        System.out.println("==============================================");
        System.out.println("Dataset: " + nomeDataset);
        System.out.println("Tamanho do Dataset: " + tamanhoDataset);
        System.out.println("Tamanhos da Tabela: 1000, 10000, 100000");
        System.out.println("Seed: " + semente);
        System.out.println("==============================================\n");

        String nomeArquivoDataset = "../data/" + nomeDataset + ".csv";
        Registro[] conjuntoDados = carregarDataset(nomeArquivoDataset);

        if (conjuntoDados == null) {
            System.out.println("Dataset nao encontrado. Gerando novo dataset...");
            conjuntoDados = GeradorDados.generateDataset(tamanhoDataset, semente);
            GeradorDados.saveToCSV(conjuntoDados, nomeArquivoDataset);
            System.out.println("Dataset gerado: " + nomeArquivoDataset + "\n");
        } else {
            System.out.println("Dataset carregado: " + nomeArquivoDataset);
            System.out.println("Total de registros: " + conjuntoDados.length + "\n");
            // Atualiza tamanhoDataset com o tamanho real do arquivo carregado
            tamanhoDataset = conjuntoDados.length;
        }

        FuncaoHash[] funcoesHash = { new HashDivisao(), new HashMultiplicacao(), new HashMeioQuadrado() };
        String nomeArquivoSaida = "../results/analise_" + nomeDataset + ".csv";

        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(nomeArquivoSaida))) {
            escritor.write("Dataset,Tamanho_Dataset,Tamanho_Tabela,Funcao_Hash,Tecnica,Colisoes_Totais,Colisoes_Medias,Tempo_Insercao_ms,Tempo_Busca_ms,Lista1,Lista2,Lista3,Gap_Min,Gap_Max,Gap_Medio\n");

            // Itera sobre cada tamanho de tabela
            for (int tamanhoTabela : TAMANHOS_TABELA) {
                System.out.println("\n==============================================");
                System.out.println("TESTANDO TAMANHO DE TABELA: " + tamanhoTabela);
                System.out.println("==============================================\n");

                // Para cada tamanho, testa todas as funções hash
                for (FuncaoHash funcaoHash : funcoesHash) {
                    System.out.println("Funcao Hash: " + funcaoHash.getNome());
                    System.out.println("-------------------------------------------");

                    analisarTabelaHash(new TabelaHashEncadeamento(tamanhoTabela, funcaoHash),
                            conjuntoDados, nomeDataset, tamanhoDataset, tamanhoTabela, funcaoHash.getNome(), escritor);

                    analisarTabelaHash(new TabelaHashLinear(tamanhoTabela, funcaoHash),
                            conjuntoDados, nomeDataset, tamanhoDataset, tamanhoTabela, funcaoHash.getNome(), escritor);

                    analisarTabelaHash(new TabelaHashQuadratica(tamanhoTabela, funcaoHash),
                            conjuntoDados, nomeDataset, tamanhoDataset, tamanhoTabela, funcaoHash.getNome(), escritor);

                    System.out.println();
                }
            }

            System.out.println("==============================================");
            System.out.println("   ANALISE CONCLUIDA COM SUCESSO!");
            System.out.println("   Relatorio: " + nomeArquivoSaida);
            System.out.println("   Total de testes: " + (TAMANHOS_TABELA.length * funcoesHash.length * 3));
            System.out.println("==============================================");
        } catch (IOException e) {
            System.err.println("Erro ao gerar relatorio: " + e.getMessage());
        }
    }

    private static void imprimirUso() {
        System.out.println("==============================================");
        System.out.println("   ANALISE DE TABELAS HASH - PJBL");
        System.out.println("==============================================\n");
        System.out.println("Uso: java Principal <nome_dataset> [tamanho_dataset]\n");
        System.out.println("Parametros:");
        System.out.println("  <nome_dataset>     - Nome do dataset (ex: dataset1, dataset2, teste_a)");
        System.out.println("  [tamanho_dataset]  - Tamanho do dataset (opcional, padrao: " + TAMANHO_DATASET_PADRAO + ")\n");
        System.out.println("Exemplos:");
        System.out.println("  java Principal dataset1           # Usa tamanho padrao (10000)");
        System.out.println("  java Principal dataset2 50000     # Dataset com 50000 elementos");
        System.out.println("  java Principal teste_a 100000     # Dataset com 100000 elementos\n");
        System.out.println("Configuracao:");
        System.out.println("  Tamanho do Dataset Padrao: " + TAMANHO_DATASET_PADRAO);
        System.out.println("  Tamanhos da Tabela: 1000, 10000, 100000\n");
        System.out.println("Para paralelizar, execute multiplas instancias:");
        System.out.println("  java Principal dataset1 10000 &");
        System.out.println("  java Principal dataset2 50000 &");
        System.out.println("  java Principal dataset3 100000 &");
        System.out.println("==============================================");
    }

    private static Registro[] carregarDataset(String nomeArquivo) {
        List<Registro> registros = new ArrayList<>();
        try (BufferedReader leitor = new BufferedReader(new FileReader(nomeArquivo))) {
            String linha = leitor.readLine();
            while ((linha = leitor.readLine()) != null) {
                linha = linha.trim();
                if (!linha.isEmpty()) {
                    registros.add(new Registro(linha));
                }
            }
            return registros.toArray(new Registro[0]);
        } catch (IOException e) {
            return null;
        }
    }

    private static void analisarTabelaHash(TabelaHash tabela, Registro[] conjuntoDados,
                                         String nomeDataset, int tamanhoDataset, int tamanhoTabela,
                                         String nomeFuncaoHash, BufferedWriter escritor) {
        ColetorMetricas metricas = new ColetorMetricas();

        long inicioInsercao = System.currentTimeMillis();
        for (Registro registro : conjuntoDados) {
            tabela.inserir(registro, metricas);
        }
        long fimInsercao = System.currentTimeMillis();
        metricas.setTempoInsercao(fimInsercao - inicioInsercao);

        long inicioBusca = System.currentTimeMillis();
        for (int i = 0; i < Math.min(1000, conjuntoDados.length); i++) {
            tabela.buscar(conjuntoDados[i]);
        }
        long fimBusca = System.currentTimeMillis();
        metricas.setTempoBusca(fimBusca - inicioBusca);

        tabela.analisarEstrutura(metricas);

        System.out.printf("  %-25s | Colisoes: %8d (Media: %.4f) | Tempo: %6d ms\n",
                tabela.getNomeTecnica(),
                metricas.getColisaoTotais(),
                metricas.getColisaoMedia(),
                metricas.getTempoInsercao());

        try {
            int[] top3 = metricas.getTresMaioresListas();
            escritor.write(String.format("%s,%d,%d,%s,%s,%d,%.4f,%d,%d,%d,%d,%d,%d,%d,%.2f\n",
                    nomeDataset, tamanhoDataset, tamanhoTabela, nomeFuncaoHash, tabela.getNomeTecnica(),
                    metricas.getColisaoTotais(), metricas.getColisaoMedia(),
                    metricas.getTempoInsercao(), metricas.getTempoBusca(),
                    top3[0], top3[1], top3[2],
                    metricas.getGapMinimo(), metricas.getGapMaximo(), metricas.getGapMedio()));
        } catch (IOException e) {
            System.err.println("Erro ao escrever no relatorio: " + e.getMessage());
        }
    }
}