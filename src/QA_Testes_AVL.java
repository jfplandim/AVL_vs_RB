    public class QA_Testes_AVL {
        public static void main(String[] args) {
            System.out.println("--- Testes de Auditoria AVL ---");

            AVL_Router_Tree arvore = new AVL_Router_Tree();

            // 1. Insercao sequencial (Pior caso - Forca o desbalanceamento)
            System.out.println("\n1. Inserindo 20 pacotes...");
            for (int i = 1; i <= 20; i++) {
                arvore.inserir(new PacketRule(i, "192.168.1." + i, "10.0.1." + i, i * 10));
            }

            boolean validacao1 = AVL_Verificador.verificarAVL(arvore);
            System.out.println("Status pos-insercao: " + (validacao1 ? "OK" : "ERRO"));
            AVL_Verificador.reportarRotacoes(arvore);

            // 2. Buscas
            // Apos alteracao do Integrante 1, buscar recebe PacketRule
            // O ID deve bater com o que foi inserido (i=15 para prioridade 150)
            System.out.println("\n2. Testando buscas...");
            PacketRule achou = arvore.buscar(new PacketRule(15, "", "", 150));
            PacketRule naoAchou = arvore.buscar(new PacketRule(0, "", "", 999));
            System.out.println("Busca 150: " + (achou != null ? "Encontrado" : "Falhou"));
            System.out.println("Busca 999: " + (naoAchou == null ? "Nao encontrado (certo)" : "Falhou"));

            // 3. Remocoes (Teste Unitario de Integridade)
            // OBS: Este nao e o teste de carga do SRE (Integrante 2).
            // O objetivo aqui e apenas validar se o rebalanceamento na exclusao esta correto.
            // Apos alteracao do Integrante 1, remover recebe PacketRule
            // O ID deve bater com o que foi inserido
            System.out.println("\n3. Removendo 4 nos (20%) e revalidando estrutura...");
            arvore.remover(new PacketRule(4,  "", "", 40));
            arvore.remover(new PacketRule(8,  "", "", 80));
            arvore.remover(new PacketRule(12, "", "", 120));
            arvore.remover(new PacketRule(16, "", "", 160));

            boolean validacao2 = AVL_Verificador.verificarAVL(arvore);
            System.out.println("Status pos-remocao: " + (validacao2 ? "OK" : "ERRO"));
            AVL_Verificador.reportarRotacoes(arvore);

            // 4. Teste de Quebra (Rotacoes Duplas LR e RL)
            System.out.println("\n4. Teste de estresse: Rotacoes Duplas");
            AVL_Router_Tree arvoreCritica = new AVL_Router_Tree();

            // Forca LR
            arvoreCritica.inserir(new PacketRule(3, "IP", "IP", 30));
            arvoreCritica.inserir(new PacketRule(1, "IP", "IP", 10));
            arvoreCritica.inserir(new PacketRule(2, "IP", "IP", 20));

            // Forca RL
            arvoreCritica.inserir(new PacketRule(5, "IP", "IP", 50));
            arvoreCritica.inserir(new PacketRule(7, "IP", "IP", 70));
            arvoreCritica.inserir(new PacketRule(6, "IP", "IP", 60));

            boolean validacao3 = AVL_Verificador.verificarAVL(arvoreCritica);
            System.out.println("Status rotacoes duplas: " + (validacao3 ? "OK" : "ERRO"));

            // 5. Teste de prioridades iguais
            // Apos Comparable, dois pacotes com mesma prioridade mas IDs diferentes
            // devem ser inseridos normalmente
            System.out.println("\n5. Teste de prioridades iguais...");
            AVL_Router_Tree arvorePrioridade = new AVL_Router_Tree();
            arvorePrioridade.inserir(new PacketRule(1, "192.168.0.1", "10.0.0.1", 50));
            arvorePrioridade.inserir(new PacketRule(2, "192.168.0.2", "10.0.0.2", 50));

            int tamanho = arvorePrioridade.tamanho();
            System.out.println("Tamanho esperado: 2 | Tamanho real: " + tamanho);
            System.out.println("Status prioridades iguais: " + (tamanho == 2 ? "OK" : "ERRO - pacote perdido"));

            boolean validacao4 = AVL_Verificador.verificarAVL(arvorePrioridade);
            System.out.println("Status pos-insercao prioridades iguais: " + (validacao4 ? "OK" : "ERRO"));
        }
    }