package generator;

import model.Registro;
import java.util.Random;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;

public class GeradorDados {
    
    public static Registro[] generateDataset(int quantidade, long semente) {
        Random aleatorio = new Random(semente);
        Registro[] conjuntoDados = new Registro[quantidade];
        
        for (int i = 0; i < quantidade; i++) {
            long codigo = Math.abs(aleatorio.nextLong()) % 1000000000L;
            conjuntoDados[i] = new Registro(String.valueOf(codigo));
        }
        
        return conjuntoDados;
    }
    
    public static void saveToCSV(Registro[] conjuntoDados, String nomeArquivo) {
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(nomeArquivo))) {
            escritor.write("codigo\n");
            for (Registro registro : conjuntoDados) {
                escritor.write(registro.getCodigo() + "\n");
            }
            System.out.println("Dataset salvo em: " + nomeArquivo);
        } catch (IOException e) {
            System.err.println("Erro ao salvar dataset: " + e.getMessage());
        }
    }
}