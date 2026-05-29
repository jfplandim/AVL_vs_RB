public class AVL_Verificador {

    // Verifica se a propriedade de altura ta certa (1 + max(esq, dir))
    private static boolean verificarAlturas(NodeAVL node) {
        if (node == null) return true;

        int alturaEsq = altura(node.esquerdo);
        int alturaDir = altura(node.direito);
        int alturaEsperada = 1 + Math.max(alturaEsq, alturaDir);

        if (node.altura != alturaEsperada) {
            System.out.println("Erro de altura no nó " + node.dado.getPrioridade() +
                    " (Atual: " + node.altura + ", Esperada: " + alturaEsperada + ")");
            return false;
        }

        return verificarAlturas(node.esquerdo) && verificarAlturas(node.direito);
    }

    private static int altura(NodeAVL node) {
        if (node == null) return -1;
        return node.altura;
    }

    // Checa se a arvore atende a regra da BST
    // Usa compareTo para respeitar a regra prioridade + ID
    private static boolean verificarBST(NodeAVL node, PacketRule min, PacketRule max) {
        if (node == null) return true;

        if (min != null && node.dado.compareTo(min) <= 0) {
            System.out.println("Erro BST: no prioridade=" + node.dado.getPrioridade() +
                    " id=" + node.dado.getId() +
                    " deveria ser maior que prioridade=" + min.getPrioridade() +
                    " id=" + min.getId());
            return false;
        }
        if (max != null && node.dado.compareTo(max) >= 0) {
            System.out.println("Erro BST: no prioridade=" + node.dado.getPrioridade() +
                    " id=" + node.dado.getId() +
                    " deveria ser menor que prioridade=" + max.getPrioridade() +
                    " id=" + max.getId());
            return false;
        }

        return verificarBST(node.esquerdo, min, node.dado) &&
                verificarBST(node.direito, node.dado, max);
    }

    // O fator de balanceamento (FB) nao pode passar de 1 ou -1
    private static boolean verificarBalanceamento(NodeAVL node) {
        if (node == null) return true;

        int fb = altura(node.esquerdo) - altura(node.direito);

        if (fb > 1 || fb < -1) {
            System.out.println("Erro de balanceamento no nó " + node.dado.getPrioridade() +
                    " | FB: " + fb);
            return false;
        }

        return verificarBalanceamento(node.esquerdo) && verificarBalanceamento(node.direito);
    }

    // Metodo principal para validar a arvore toda
    public static boolean verificarAVL(AVL_Router_Tree arvore) {
        NodeAVL raiz = arvore.getRaiz();

        System.out.println("--- Teste de Validacao AVL ---");

        boolean bst     = verificarBST(raiz, null, null);
        boolean alturas = verificarAlturas(raiz);
        boolean balance = verificarBalanceamento(raiz);

        System.out.println("Status -> BST: " + bst + " | Alturas: " + alturas +
                " | Balanceamento: " + balance);

        return bst && alturas && balance;
    }

    public static void reportarRotacoes(AVL_Router_Tree arvore) {
        System.out.println("Total de rotacoes na arvore: " + arvore.getRotacoes());
    }
}