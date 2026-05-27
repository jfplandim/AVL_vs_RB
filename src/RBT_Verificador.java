public class RBT_Verificador {

    // Propriedade 2: a raiz sempre tem que ser preta
    private static boolean verificarRaizPreta(NodeRBT raiz) {
        if (raiz == null || !raiz.vermelho) return true;

        System.out.println("Erro P2: a raiz vermelha");
        return false;
    }

    // Propriedade 4: no vermelho nao pode ter filho vermelho
    private static boolean verificarVermelhoSemFilhoVermelho(NodeRBT no) {
        if (no == null || no.dado == null) return true;

        if (no.vermelho) {
            if (no.esquerdo != null && no.esquerdo.vermelho) {
                System.out.println("Erro P4: o no " + no.dado.getPrioridade() + " tem filho esquerdo vermelho");
                return false;
            }
            if (no.direito != null && no.direito.vermelho) {
                System.out.println("Erro P4: o no " + no.dado.getPrioridade() + " tem filho direito vermelho");
                return false;
            }
        }

        return verificarVermelhoSemFilhoVermelho(no.esquerdo) && verificarVermelhoSemFilhoVermelho(no.direito);
    }
}