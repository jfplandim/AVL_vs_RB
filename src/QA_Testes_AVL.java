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
        System.out.println("\n2. Testando buscas...");
        PacketRule achou = arvore.buscar(150); // Equivalente ao i=15
        PacketRule naoAchou = arvore.buscar(999);
        System.out.println("Busca 150: " + (achou != null ? "Encontrado" : "Falhou"));
        System.out.println("Busca 999: " + (naoAchou == null ? "Nao encontrado (certo)" : "Falhou"));

        // 3. Remocoes (Teste Unitario de Integridade)
        // OBS: Este não é o teste de carga do SRE (Integrante 2).
        // O objetivo aqui é apenas validar se o rebalanceamento na exclusão, feito pelo Integrante 1, está correto.
        System.out.println("\n3. Removendo 4 nos (20%) e revalidando estrutura...");
        arvore.remover(40);
        arvore.remover(80);
        arvore.remover(120);
        arvore.remover(160);

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
    }
}