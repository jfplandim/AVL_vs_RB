public class AVL_Router_Tree {
    private NodeAVL raiz;
    private int rotacoes;

    public AVL_Router_Tree() {
        this.raiz = null;
        this.rotacoes = 0;
    }

    //retorna a altura do nó, -1 se for nula
    private int altura(NodeAVL node) {
        return (node == null) ? -1 : node.altura;
    }

    //calcula o fator de balanço do nó
    private int fatorBalanceamento(NodeAVL node) {
        return (node == null) ? 0 : altura(node.esquerdo) - altura(node.direito);
    }

    public void inserir(PacketRule regra) {
        raiz = inserir(raiz, regra);
    }

    private NodeAVL inserir(NodeAVL node, PacketRule regra) {
        if (node == null) {
            return new NodeAVL(regra);
        }

        //alterando para o uso da regra Prioridade + ID
        int comparacao = regra.compareTo(node.dado);

        if (comparacao < 0)
            node.esquerdo = inserir(node.esquerdo, regra);
        else if (comparacao > 0)
            node.direito = inserir(node.direito, regra);
        else
            return node; //ignora caso seja uma regra 100% duplicada


        //atualiza a altura
        node.altura = 1 + Math.max(altura(node.esquerdo), altura(node.direito));

        //calcula o fator balanceamento
        int fb = fatorBalanceamento(node);

        //rotações
        //esquerda-esquerda LL
        // fb > 1 garante que node.esquerdo != null (altura esquerda >= 2)
        if (fb > 1 && regra.compareTo(node.esquerdo.dado) < 0)
            return rotacionarDireita(node);

        //direita direita RR
        // fb < -1 garante que node.direito != null (altura direita >= 2)
        if (fb < -1 && regra.compareTo(node.direito.dado) > 0)
            return rotacionarEsquerda(node);

        //esquerda-direita LR
        // fb > 1 garante que node.esquerdo != null
        if (fb > 1 && regra.compareTo(node.esquerdo.dado) > 0) {
            node.esquerdo = rotacionarEsquerda(node.esquerdo);
            return rotacionarDireita(node);
        }

        //direita-esquerda RL
        // fb < -1 garante que node.direito != null
        if (fb < -1 && regra.compareTo(node.direito.dado) < 0) {
            node.direito = rotacionarDireita(node.direito);
            return rotacionarEsquerda(node);
        }

        //no balanceado
        return node;
    }

    private NodeAVL rotacionarDireita(NodeAVL A) {
        NodeAVL B = A.esquerdo;
        NodeAVL T2 = B.direito;

        //B herda o paid e A
        B.pai = A.pai;

        //rearranjo de B e A
        B.direito = A;
        A.pai = B;

        //rearranjo de A e T2
        A.esquerdo = T2;
        if (T2 != null) {
            T2.pai = A; //se T2 existir, o novo pai dele é A
        }

        //atualiza as alturas
        A.altura = 1 + Math.max(altura(A.esquerdo), altura(A.direito));
        B.altura = 1 + Math.max(altura(B.esquerdo), altura(B.direito));

        rotacoes++;
        return B;
    }

    private NodeAVL rotacionarEsquerda(NodeAVL A) {
        NodeAVL B  = A.direito;
        NodeAVL T2 = B.esquerdo;

        //B herda o pai de A
        B.pai = A.pai;

        //Rearranjo de B e A
        B.esquerdo = A;
        A.pai = B;

        //Rearranjo de A e T2
        A.direito = T2;
        if (T2 != null) {
            T2.pai = A; //se T2 existir, o novo pai dele é A
        }

        //atualiza alturas
        A.altura = 1 + Math.max(altura(A.esquerdo), altura(A.direito));
        B.altura = 1 + Math.max(altura(B.esquerdo), altura(B.direito));

        rotacoes++;
        return B;
    }

    public PacketRule buscar(PacketRule pacoteAlvo) {
        NodeAVL node = raiz;

        // Enquanto não chegar em uma folha vazia
        while (node != null) {

            //delega a comparação para a classe PacketRule
            int comparacao = pacoteAlvo.compareTo(node.dado);

            if (comparacao == 0) {
                //encontrou a regra exata (mesma prioridade e mesmo ID)
                return node.dado;
            } else if (comparacao < 0) {
                //o pacote alvo é menor, desce para a esquerda
                node = node.esquerdo;
            } else {
                //o pacote alvo é maior, desce para a direita
                node = node.direito;
            }
        }

        //se o laço terminar, o pacote não está na árvore
        return null;
    }

    //remoção
    //buscamos o menor valor da subárvore direita(in-order)
    private NodeAVL menorNo(NodeAVL node) {
        NodeAVL atual = node;
        while (atual.esquerdo != null)
            atual = atual.esquerdo;
        return atual;
    }

    public void remover(PacketRule pacoteAlvo) {
        raiz = remover(raiz, pacoteAlvo);
    }

    private NodeAVL remover(NodeAVL node, PacketRule pacoteAlvo) {
        //nó nao encontrado
        if (node == null)
            return null;

        //uso do compareTo
        int comparacao = pacoteAlvo.compareTo(node.dado);

        //1. busca recursiva da bst
        if (comparacao < 0)
            node.esquerdo = remover(node.esquerdo, pacoteAlvo);
        else if (comparacao > 0)
            node.direito = remover(node.direito, pacoteAlvo);
        else {
            //apos encontrar o nó
            if (node.esquerdo == null || node.direito == null) {
                NodeAVL temp = (node.esquerdo != null) ? node.esquerdo : node.direito;

                //caso 1: folha
                if (temp == null)
                    node = null;
                //caso 2: um filho
                else
                    node = temp;
            } else {
                //caso 3: 2 filhos - busca o sucessor in order
                NodeAVL sucessor = menorNo(node.direito);

                //copia o valor do sucessor para o nó atual
                node.dado = sucessor.dado;

                //remove o sucessor da subárvore direita
                node.direito = remover(node.direito, sucessor.dado);
            }

        }

        //se a árvore tinha só 1 nó e foi removido
        if (node == null)
            return null;

        //2. atualiza a altura
        node.altura = 1 + Math.max(altura(node.esquerdo), altura(node.direito));

        //3. calcula fator de balanceamento
        int fb = fatorBalanceamento(node);

        //4. rotações

        //LL
        if (fb > 1 && fatorBalanceamento(node.esquerdo) >= 0)
            return rotacionarDireita(node);

        //RR
        if (fb < -1 && fatorBalanceamento(node.direito) <= 0)
            return rotacionarEsquerda(node);

        //LR
        if (fb > 1 && fatorBalanceamento(node.esquerdo) < 0) {
            node.esquerdo = rotacionarEsquerda(node.esquerdo);
            return  rotacionarDireita(node);
        }

        //RL
        if (fb < -1 && fatorBalanceamento(node.direito) > 0) {
            node.direito = rotacionarDireita(node.direito);
            return rotacionarEsquerda(node);
        }

        return node;

    }

    public int getRotacoes() {
        return rotacoes;
    }

    public int tamanho() {
        return tamanho(raiz);
    }

    private int tamanho(NodeAVL node) {
        if (node == null)
            return 0;
        return 1 + tamanho(node.esquerdo) + tamanho(node.direito);
    }

    public NodeAVL getRaiz() {
        return raiz;
    }
}
