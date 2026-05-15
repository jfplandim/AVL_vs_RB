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

        if (regra.getPrioridade() < node.dado.getPrioridade())
            node.esquerdo = inserir(node.esquerdo, regra);
        else if (regra.getPrioridade() > node.dado.getPrioridade())
            node.direito = inserir(node.direito, regra);
        else
            return node;

        //atualiza a altura
        node.altura = 1 + Math.max(altura(node.esquerdo), altura(node.direito));

        //calcula o fator balanceamento
        int fb = fatorBalanceamento(node);

        //rotações
        //esquerda-esquerda LL
        if (fb > 1 && regra.getPrioridade() < node.esquerdo.dado.getPrioridade())
            return rotacionarDireita(node);

        //direita direita RR
        if (fb < -1 && regra.getPrioridade() > node.direito.dado.getPrioridade())
            return rotacionarEsquerda(node);

        //esquerda-direita LR
        if (fb > 1 && regra.getPrioridade() > node.esquerdo.dado.getPrioridade()) {
            node.esquerdo = rotacionarEsquerda(node.esquerdo);
            return rotacionarDireita(node);
        }

        //direita-esquerda RL
        if (fb < -1 && regra.getPrioridade() < node.direito.dado.getPrioridade()) {
            node.direito = rotacionarDireita(node.direito);
            return rotacionarEsquerda(node);
        }

        //no balanceado
        return node;
    }

    private NodeAVL rotacionarDireita(NodeAVL A) {
        NodeAVL B = A.esquerdo;
        NodeAVL T2 = B.direito;

        //rearranjo dos ponteiros
        B.direito = A;
        A.esquerdo = T2;

        //atualiza as alturas
        A.altura = 1 + Math.max(altura(A.esquerdo), altura(A.direito));
        B.altura = 1 + Math.max(altura(B.esquerdo), altura(B.direito));

        rotacoes++;
        return B;
    }

    private NodeAVL rotacionarEsquerda(NodeAVL A) {
        NodeAVL B  = A.direito;
        NodeAVL T2 = B.esquerdo;

        //rearranjo de ponteiros
        B.esquerdo = A;
        A.direito  = T2;

        //atualiza alturas
        A.altura = 1 + Math.max(altura(A.esquerdo), altura(A.direito));
        B.altura = 1 + Math.max(altura(B.esquerdo), altura(B.direito));

        rotacoes++;
        return B;
    }

    public PacketRule buscar(int prioridade) {
        NodeAVL node = raiz;

        while (node != null && node.dado.getPrioridade() != prioridade) {
            if (prioridade < node.dado.getPrioridade())
                node = node.esquerdo;
            else
                node = node.direito;
        }

        return (node != null) ? node.dado : null;
    }

    //remoção
    //buscamos o menor valor da subárvore direita(in-order)
    private NodeAVL menorNo(NodeAVL node) {
        NodeAVL atual = node;
        while (atual.esquerdo != null)
            atual = atual.esquerdo;
        return atual;
    }

    public void remover(int prioridade) {
        raiz = remover(raiz, prioridade);
    }

    private NodeAVL remover(NodeAVL node, int prioridade) {
        //nó nao encontrado
        if (node == null)
            return null;
        
        //1. busca recursiva da bst
        if (prioridade < node.dado.getPrioridade())
            node.esquerdo = remover(node.esquerdo, prioridade);
        else if (prioridade > node.dado.getPrioridade())
            node.direito = remover(node.direito,  prioridade);
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
                node.direito = remover(node.direito, sucessor.dado.getPrioridade());
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

}
