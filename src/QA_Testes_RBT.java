public class QA_Testes_RBT {
    public static void main(String[] args) {
        System.out.println("--- Bateria de Testes RBT ---");

        RedBlack_Router_Tree arvore = new RedBlack_Router_Tree();

        // Teste de Insercao (vendo se o rebalanceamento no pior caso funciona)
        System.out.println("\n-> Inserindo 20 pacotes em sequencia");
        for (int i = 1; i <= 20; i++) {
            arvore.inserir(new PacketRule(i, "192.168.1." + i, "10.0.1." + i, i * 10));
        }
        boolean passouIns = RBT_Verificador.verificarRBT(arvore);
        System.out.println("Insercao OK? " + passouIns);
        RBT_Verificador.reportarRotacoes(arvore);

        // Teste de Busca
        System.out.println("\n-> Testando buscas");
        NodeRBT achou = arvore.buscar(new PacketRule(15, "", "", 150));
        NodeRBT nAchou = arvore.buscar(new PacketRule(0, "", "", 999));
        System.out.println("Achou o 150? " + (achou.dado != null));
        System.out.println("Falhou no 999 (como esperado)? " + (nAchou.dado == null));

        // Teste de Remocao
        // O objetivo aqui e so ver se a arvore nao quebra as regras depois de apagar
        System.out.println("\n-> Removendo 4 nos e revalidando");
        arvore.remover(new PacketRule(4, "", "", 40));
        arvore.remover(new PacketRule(8, "", "", 80));
        arvore.remover(new PacketRule(12, "", "", 120));
        arvore.remover(new PacketRule(16, "", "", 160));

        boolean passouRem = RBT_Verificador.verificarRBT(arvore);
        System.out.println("Remocao OK? " + passouRem);
        RBT_Verificador.reportarRotacoes(arvore);
        // Teste de prioridades iguais (tem que aceitar e nao substituir)
        System.out.println("\n-> Teste Prioridade Igual");
        RedBlack_Router_Tree arvPrio = new RedBlack_Router_Tree();
        arvPrio.inserir(new PacketRule(1, "IP1", "IP2", 50));
        arvPrio.inserir(new PacketRule(2, "IP3", "IP4", 50));

        System.out.println("Tamanho (esperado 2): " + arvPrio.tamanho());
        System.out.println("Estrutura ta certa? " + RBT_Verificador.verificarRBT(arvPrio));

        // Teste forcando recoloracao
        System.out.println("\n-> Teste Recoloracao");
        RedBlack_Router_Tree arvRec = new RedBlack_Router_Tree();
        for (int i = 1; i <= 5; i++) {
            arvRec.inserir(new PacketRule(i, "IP", "IP", i * 10));
        }
        System.out.println("Estrutura pos recoloracao OK? " + RBT_Verificador.verificarRBT(arvRec));
        RBT_Verificador.reportarRotacoes(arvRec);
    }
}