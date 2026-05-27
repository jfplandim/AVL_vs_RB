public class RBT_Verificador {

    // Propriedade 2: a raiz sempre tem que ser preta
    private static boolean verificarRaizPreta(NodeRBT raiz) {
        if (raiz == null || !raiz.vermelho) return true;

        System.out.println("Erro P2: a raiz vermelha");
        return false;
    }
}