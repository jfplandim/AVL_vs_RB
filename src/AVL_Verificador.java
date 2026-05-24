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
}