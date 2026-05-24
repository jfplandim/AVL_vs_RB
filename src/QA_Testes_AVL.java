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
    }
}