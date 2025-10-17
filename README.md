# Análise Comparativa de Tabelas Hash

Este projeto implementa e analisa o desempenho de diferentes técnicas de tratamento de colisões em tabelas hash, utilizando três funções hash distintas e três métodos de resolução de colisões. O sistema foi desenvolvido em Java para análise acadêmica de estruturas de dados.

## Visão Geral

O projeto permite a comparação sistemática de diferentes configurações de tabelas hash através de:

- **3 técnicas de tratamento de colisões**
- **3 funções hash diferentes**
- **3 tamanhos de tabela configuráveis**
- **Geração automática de datasets**
- **Métricas detalhadas de desempenho**
- **Relatórios em formato CSV**

## Técnicas de Tratamento de Colisões

### 1. Encadeamento (Chaining)
- Utiliza listas encadeadas para armazenar múltiplos elementos na mesma posição
- Implementado através da classe `HashTableChaining`
- Ideal para altas taxas de ocupação

### 2. Rehashing Linear (Linear Probing)
- Busca sequencial por próxima posição disponível
- Implementado através da classe `HashTableLinearProbing`
- Propenso ao clustering primário

### 3. Rehashing Quadrático (Quadratic Probing)
- Busca quadrática por posição livre usando incrementos quadráticos
- Implementado através da classe `HashTableQuadraticProbing`
- Reduz clustering primário comparado ao linear

## Funções Hash Implementadas

### 1. Division Hash (Resto da Divisão)
```
h(k) = k mod m
```
- Implementada na classe `DivisionHash`
- Simples e eficiente
- Sensível à escolha do tamanho da tabela

### 2. Multiplication Hash (Multiplicação)
```
h(k) = floor(m * (k * A mod 1))
```
onde A ≈ 0.618 (razão áurea - 1)
- Implementada na classe `MultiplicationHash`
- Menos sensível ao tamanho da tabela
- Boa distribuição uniforme

### 3. Mid-Square Hash (Meio do Quadrado)
- Utiliza dígitos centrais de k²
- Implementada na classe `MidSquareHash`
- Boa para chaves com padrões regulares

## Arquitetura do Sistema

```
src/
├── Main.java                    # Classe principal e orquestração dos testes
├── model/
│   └── Registro.java           # Modelo de dados (códigos de 9 dígitos)
├── hash/
│   ├── HashFunction.java       # Interface para funções hash
│   ├── DivisionHash.java       # Implementação Division
│   ├── MultiplicationHash.java # Implementação Multiplication
│   └── MidSquareHash.java      # Implementação Mid-Square
├── hashtable/
│   ├── HashTable.java          # Interface para tabelas hash
│   ├── HashTableChaining.java  # Implementação Encadeamento
│   ├── HashTableLinearProbing.java    # Implementação Linear
│   ├── HashTableQuadraticProbing.java # Implementação Quadrática
│   └── Node.java              # Nó para listas encadeadas
├── metrics/
│   └── MetricsCollector.java   # Coleta e armazenamento de métricas
└── generator/
    └── DataGenerator.java      # Geração de datasets sintéticos
```

## Configuração dos Testes

### Tamanhos de Tabela
- 1.000 posições
- 10.000 posições  
- 100.000 posições

### Datasets
- Tamanho padrão: 10.000 registros
- Tamanho configurável via parâmetro
- Registros: códigos numéricos de 9 dígitos
- Seed determinística baseada no nome do dataset

### Parâmetros de Teste
- Total de combinações: 27 testes por dataset (3 tabelas × 3 funções × 3 técnicas)
- Busca: 1.000 consultas por configuração
- Reprodutibilidade garantida através de seeds fixas

## Compilação e Execução

### Compilar o Projeto
```bash
# Criar diretório para classes compiladas
mkdir -p bin

# Compilar todas as classes
javac -d bin src/**/*.java src/*.java
```

### Executar Análise
```bash
# Sintaxe básica
java -cp bin Main <nome_dataset> [tamanho_dataset]

# Exemplos
java -cp bin Main dataset_10k                    # Dataset com 10.000 registros
java -cp bin Main dataset_100k 100000           # Dataset com 100.000 registros
java -cp bin Main experimento_1 50000           # Dataset personalizado
```

### Execução Paralela
Para acelerar análises de múltiplos datasets:
```bash
java -cp bin Main dataset1 10000 &
java -cp bin Main dataset2 50000 &
java -cp bin Main dataset3 100000 &
wait
```

## Métricas Coletadas

### Métricas de Inserção
- **Tempo total de inserção** (milissegundos)
- **Número total de colisões**
- **Colisões médias por inserção**

### Métricas de Busca
- **Tempo total de busca** (milissegundos)
- **Eficiência das consultas**

### Análise Estrutural
- **Três maiores listas/clusters** (tamanhos)
- **Gap mínimo** entre elementos
- **Gap máximo** entre elementos  
- **Gap médio** entre elementos

## Estrutura de Dados de Saída

### Diretório `data/`
- Contém datasets gerados em formato CSV
- Arquivo `dataset_nome.csv` com códigos sequenciais
- Reutilização automática de datasets existentes

### Diretório `results/`
- Arquivo `analise_nome_dataset.csv` com métricas completas
- Formato CSV para análise posterior em planilhas/ferramentas estatísticas

### Formato do Relatório CSV
```csv
Dataset,Tamanho_Dataset,Tamanho_Tabela,Funcao_Hash,Tecnica,Colisoes_Totais,Colisoes_Medias,Tempo_Insercao_ms,Tempo_Busca_ms,Lista1,Lista2,Lista3,Gap_Min,Gap_Max,Gap_Medio
```

## Requisitos do Sistema

### Ambiente de Desenvolvimento
- **Java**: Versão 8 ou superior
- **Memória**: Mínimo 2GB RAM (recomendado 4GB para datasets grandes)
- **Espaço**: 100MB para dados e resultados

### Dependências
- Apenas bibliotecas padrão do Java (sem dependências externas)
- Compatível com qualquer JVM padrão

## Casos de Uso

### 1. Análise Comparativa Básica
Comparar desempenho das diferentes técnicas com dataset padrão:
```bash
java -cp bin Main experimento_basico
```

### 2. Análise de Escalabilidade
Testar comportamento com diferentes tamanhos de dataset:
```bash
java -cp bin Main small_dataset 1000
java -cp bin Main medium_dataset 10000  
java -cp bin Main large_dataset 100000
```

### 3. Estudos Reprodutíveis
Usar nomes determinísticos para garantir reprodutibilidade:
```bash
java -cp bin Main seed_42_test
java -cp bin Main seed_42_test  # Produzirá resultados idênticos
```

## Considerações de Performance

### Tempo de Execução Estimado
- **Dataset 10K**: ~1-2 minutos
- **Dataset 100K**: ~5-10 minutos
- **Dataset 1M**: ~30-60 minutos

### Otimizações Implementadas
- Geração de dados com seed fixa para reprodutibilidade
- Reutilização de datasets existentes
- Métricas calculadas durante inserção para eficiência
- Análise de gaps otimizada para diferentes técnicas

## Limitações e Considerações

1. **Memória**: Datasets muito grandes podem exigir ajuste de heap da JVM
2. **Precisão temporal**: Medições em milissegundos podem ter baixa precisão para operações rápidas
3. **Distribuição de dados**: Dados gerados pseudo-aleatoriamente podem não refletir padrões reais
4. **Tamanho da tabela**: Funções hash podem ter desempenhos diferentes com tamanhos específicos

## Interpretação dos Resultados

### Indicadores de Boa Performance
- **Baixo número de colisões** indica boa distribuição
- **Tempo de inserção reduzido** mostra eficiência operacional
- **Gaps uniformes** (para probing) indicam boa distribuição espacial
- **Listas equilibradas** (para chaining) mostram distribuição uniforme

### Análise Comparativa
- Compare métricas entre diferentes funções hash
- Observe impacto do tamanho da tabela nas colisões
- Analise trade-offs entre tempo e espaço
- Identifique técnicas mais adequadas para diferentes cenários

## Desenvolvimento e Extensibilidade

### Adicionar Nova Função Hash
1. Implementar interface `HashFunction`
2. Adicionar à lista em `Main.java`
3. Seguir padrão de nomenclatura

### Adicionar Nova Técnica de Colisão
1. Implementar interface `HashTable`
2. Integrar com `MetricsCollector`
3. Adicionar aos testes em `Main.java`

### Personalizar Métricas
- Modificar `MetricsCollector` para novas métricas
- Atualizar formato CSV de saída
- Implementar análises estatísticas adicionais

## Contribuição

Este projeto foi desenvolvido para fins acadêmicos como parte do estudo de estruturas de dados. Contribuições são bem-vindas para:

- Otimizações de performance
- Novas funções hash
- Métricas adicionais
- Melhorias na documentação
- Casos de teste abrangentes

## Licença

Projeto desenvolvido para uso acadêmico e educacional.