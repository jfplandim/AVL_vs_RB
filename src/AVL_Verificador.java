public class AVL_Verificador {

    // Verifica se a propriedade de altura ta certa (1 + max(esq, dir))
    private static boolean verificarAlturas(NodeAVL node) {
        if (node == null) return true;

        int alturaEsq = altura(node.esquerdo);
        int alturaDir = altura(node.direito);
        int alturaEsperada = 1 + Math.max(alturaEsq, alturaDir);

        if (node.altura != alturaEsperada) {
            System.out.println("Erro de altura no nó " + node.dado.getPrioridade() + " (Atual: " + node.altura + ", Esperada: " + alturaEsperada + ")");
            return false;
        }

        return verificarAlturas(node.esquerdo) && verificarAlturas(node.direito);
    }

    private static int altura(NodeAVL node) {
        if (node == null) {
            return -1;
        }
        return node.altura;
    }

    // Checa se a arvore atende a regra da BST
    // Qualquer nó à esquerda deve ser menor e à direita maior
    private static boolean verificarBST(NodeAVL node, Integer min, Integer max) {
        if (node == null) return true;

        int prioridade = node.dado.getPrioridade();

        if (min != null && prioridade <= min) {
            System.out.println("Erro BST: prioridade " + prioridade + " deveria ser maior que " + min);
            return false;
        }
        if (max != null && prioridade >= max) {
            System.out.println("Erro BST: prioridade " + prioridade + " deveria ser menor que " + max);
            return false;
        }

        return verificarBST(node.esquerdo, min, prioridade) &&
                verificarBST(node.direito, prioridade, max);
    }
}