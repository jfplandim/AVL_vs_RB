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
    }
}