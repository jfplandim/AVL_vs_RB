# SDN-Scale: AVL vs Red-Black Tree

> Projeto acadêmico da disciplina **Estrutura de Dados II** — iCEV  
> Professor: Ricardo Sekeff

Comparação empírica entre as estruturas **AVL Router Tree** e **RedBlack Router Tree** aplicadas ao gerenciamento de regras de firewall em um Load Balancer simulado, com medições de desempenho em nanossegundos.

---

## 👥 Equipe

| Integrante | GitHub | Papel |
|---|---|---|
| **José Francisco Paes Landim Sobrinho** | [@jfplandim](https://github.com/jfplandim) | Lead Software Engineer — Implementação das árvores |
| **Gustavo Nunes da Silva Pereira** | [@Gustavo19-code](https://github.com/Gustavo19-code) | DevOps & SRE — Testes de carga e coleta de dados |
| **João Guilherme Aragão Malta** | [@JoaoMalta27](https://github.com/JoaoMalta27) | QA & Analytics — Verificadores de invariantes e análise |

---

## 📋 Sobre o Projeto

O sistema atual de roteamento sofre de latência excessiva quando novas regras de firewall são inseridas sequencialmente. O objetivo é comparar AVL e Red-Black Tree para decidir qual estrutura oferece menor latência em escala de nanossegundos em um ambiente SDN (*Software-Defined Networking*).

### Estrutura de Dados Principal

Cada nó das árvores gerencia objetos `PacketRule` com os seguintes atributos:

```java
class PacketRule implements Comparable<PacketRule> {
    int id;
    String ipOrigem;
    String ipDestino;
    int prioridade;
}
```

A ordenação é feita por **prioridade**, usando o **ID** como critério de desempate.

---

## 🏗️ Arquitetura

Ambas as árvores foram projetadas com:

- **Arquitetura 100% iterativa** — elimina o sobrecusto de chamadas recursivas
- **Ponteiro pai em cada nó** — permite subida eficiente para rebalanceamento
- **Sentinela `T.nil`** (apenas RBT) — blinda a navegação em folhas contra erros de referência
- **Contador de rotações** — campo `int rotacoes` incrementado a cada rotação para fins de auditoria

---

## 📁 Estrutura do Repositório

```
AVL_vs_RB/
├── src/
│   ├── AVL_Router_Tree.java        # Implementação da Árvore AVL
│   ├── RedBlack_Router_Tree.java   # Implementação da Red-Black Tree
│   ├── NodeAVL.java                # Nó da Árvore AVL (com ponteiro pai e altura)
│   ├── NodeRBT.java                # Nó da Red-Black Tree (com cor e sentinela T.nil)
│   ├── PacketRule.java             # Modelo de regra de firewall
│   ├── geradorPacketRule.java      # Gerador de dados com seed fixa
│   ├── AVL_Verificador.java        # Verificador de invariantes da AVL
│   ├── RBT_Verificador.java        # Verificador de propriedades da RBT
│   ├── QA_Testes_AVL.java          # Testes unitários da AVL (QA)
│   ├── QA_Testes_RBT.java          # Testes unitários da RBT (QA)
│   └── teste.java                  # Arquivo de testes gerais
├── out/
│   └── production/AVL_vs_RB/       # Bytecode compilado (.class)
│       ├── AVL_Router_Tree.class
│       ├── RedBlack_Router_Tree.class
│       ├── NodeAVL.class
│       ├── NodeRBT.class
│       ├── PacketRule.class
│       ├── geradorPacketRule.class
│       ├── AVL_Verificador.class
│       ├── RBT_Verificador.class
│       ├── QA_Testes_AVL.class
│       ├── QA_Testes_RBT.class
│       ├── SRE_StressTest.class
│       └── teste.class
├── .gitignore
├── CODEOWNERS                      # JoaoMalta27 como revisor obrigatório
└── README.md
```

---

## ⚙️ Como Executar

### Pré-requisitos

- Java 11 ou superior
- IDE recomendada: IntelliJ IDEA

### Compilar e rodar os testes de carga

```bash
# Compilar todos os arquivos
javac src/*.java

# Executar o stress test completo
java teste.java
```

O teste usará **seed = 1** por padrão, garantindo reprodutibilidade total dos resultados.

---

## 📊 Resultados (seed = 1)

| Registros | Ins. AVL | Ins. RBT | Busca AVL | Busca RBT | Del. AVL | Del. RBT | Rot. AVL | Rot. RBT |
|----------:|:--------:|:--------:|:---------:|:---------:|:--------:|:--------:|:--------:|:--------:|
| 1.000 | 5162 | **1873** | 1428 | **970** | 4886 | **1658** | 774 | **664** |
| 10.000 | 517 | **379** | 455 | **291** | 1195 | **664** | 7819 | **6666** |
| 50.000 | 1062 | **899** | 673 | **536** | **1019** | 1465 | 39137 | **33276** |
| 100.000 | 1020 | **961** | 695 | **450** | 1605 | **1312** | 78250 | **66436** |
| 1.000.000 | 1640 | **1427** | 608 | **375** | **1426** | 1862 | 787061 | **668870** |

> Valores em **ns/op** (nanossegundos por operação). Negrito indica a estrutura vencedora.

### Conclusão dos Testes

A **Red-Black Tree** foi recomendada como motor de busca para o Load Balancer por:

- ✅ **13% mais rápida** na inserção (1.427 vs 1.640 ns/op)
- ✅ **38% mais rápida** na busca (375 vs 608 ns/op)
- ✅ **15% menos rotações** acumuladas (668.870 vs 787.061)
- ⚠️ AVL superior apenas na remoção — operação menos frequente no cenário proposto

---

## 🔍 Dinâmica de Desenvolvimento

### Code Review Obrigatório

O Integrante 1 **não pode** fazer merge na `main` sem aprovação do **João Malta (QA)** via Pull Request. Os verificadores de invariantes devem passar antes de qualquer merge.

### Branch Protection

- Branch protegida: `main` `develop`
- Aprovação obrigatória: **JoaoMalta27** (CODEOWNERS)
- Testes de invariante devem passar antes do merge

### Verificadores de Invariantes

```
AVL_Verificador:
  ✔ Validade BST (ordenação correta)
  ✔ Consistência de alturas
  ✔ Fator de Balanceamento |FB| ≤ 1

RBT_Verificador:
  ✔ Propriedades 1-5 da Red-Black Tree
  ✔ Altura preta uniforme em todos os caminhos
```

---

## 📚 Referências

- CORMEN, T. H. et al. *Algoritmos: Teoria e Prática*. 3ª ed. Elsevier, 2012.
- SEDGEWICK, R.; WAYNE, K. *Algorithms*. 4th ed. Addison-Wesley, 2011.
- ZIVIANI, N. *Projeto de Algoritmos*. 4ª ed. Pioneira, 1999.
- SEKEFF, R. *Notas de Aula: Estrutura de Dados II*. iCEV, 2025.
