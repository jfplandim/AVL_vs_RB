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
    // Propriedade 5: os caminhos ate a folha tem que ter a mesma qtd de nos pretos
    private static boolean verificarAlturaPreta(NodeRBT raiz) {
        int alt = calcularAlturaPreta(raiz);
        if (alt == -1) {
            System.out.println("Erro P5: altura preta diferente entre os caminhos");
            return false;
        }
        System.out.println("Altura preta da arvore: " + alt);
        return true;
    }

    private static int calcularAlturaPreta(NodeRBT no) {
        if (no == null || no.dado == null) return 1;

        int altEsq = calcularAlturaPreta(no.esquerdo);
        int altDir = calcularAlturaPreta(no.direito);

        if (altEsq == -1 || altDir == -1) return -1;

        if (altEsq != altDir) {
            System.out.println("Erro P5: no " + no.dado.getPrioridade() + " | esq=" + altEsq + " | dir=" + altDir);
            return -1;
        }

        // se for vermelho soma 0, se for preto soma 1
        return altEsq + (no.vermelho ? 0 : 1);
    }
}