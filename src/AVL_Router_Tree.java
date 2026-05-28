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
        NodeAVL novoNo = new NodeAVL(regra);

        //se tiver vazia
        if (raiz == null) {
            raiz = novoNo;
            return;
        }

        NodeAVL atual = raiz;
        NodeAVL pai = null;
        int comparacao = 0;

        //descendo: busca interativa
        while (atual != null) {
            pai = atual;
            comparacao = regra.compareTo(atual.dado);

            if (comparacao < 0) {
                atual = atual.esquerdo;
            } else if (comparacao > 0) {
                atual = atual.direito;
            } else {
                return; //regra duplicada, ignora
            }
        }

        //inserção: conecta o novo nó ao pai encontrado
        novoNo.pai = pai;
        if (comparacao < 0) {
            pai.esquerdo = novoNo;
        } else {
            pai.direito = novoNo;
        }

        //subida: sobe pelo ponteiro pai rebalanceando
        atual = pai;
        while (atual != null) {
            //atualiza a altura do nó atual
            atual.altura = 1 + Math.max(altura(atual.esquerdo), altura(atual.direito));

            //calcula o fator de balanceamento
            int fb = fatorBalanceamento(atual);
            NodeAVL novoTopo = null;

            //verifica os 4 casos e rotaciona se necessário
            // esquerda-esquerda LL
            if (fb > 1 && regra.compareTo(atual.esquerdo.dado) < 0) {
                novoTopo = rotacionarDireita(atual);
            }
            // direita-direita RR
            else if (fb < -1 && regra.compareTo(atual.direito.dado) > 0) {
                novoTopo = rotacionarEsquerda(atual);
            }
            // esquerda-direita LR
            else if (fb > 1 && regra.compareTo(atual.esquerdo.dado) > 0) {
                rotacionarEsquerda(atual.esquerdo);
                novoTopo = rotacionarDireita(atual);
            }
            // direita-esquerda RL
            else if (fb < -1 && regra.compareTo(atual.direito.dado) < 0) {
                rotacionarDireita(atual.direito);
                novoTopo = rotacionarEsquerda(atual);
            }

            //se aplicou rotação, a subarvore ja recuperou a altura original
            if (novoTopo != null) {
                break; //avl no max 1 rotação por inserção
            }

            //sobe para o próximo nível seguindo o ponteiro do pai
            atual = atual.pai;
        }

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

        //reconecta B ao avô
        if (B.pai == null) {
            raiz = B;
        } else if (B.pai.esquerdo == A) {
            B.pai.esquerdo = B;
        } else {
            B.pai.direito = B;
        }

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

        //reconecta B ao avô
        if (B.pai == null) {
            raiz = B;
        } else if (B.pai.esquerdo == A) {
            B.pai.esquerdo = B;
        } else {
            B.pai.direito = B;
        }

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
    public void remover(PacketRule pacoteAlvo) {
        //se for nula
        if (raiz == null) {
            return;
        }
        
        //descer ate encontrar o ná que vai remover
        NodeAVL atual = raiz;
        while (atual != null) {
            int comparacao = pacoteAlvo.compareTo(atual.dado);
            if (comparacao < 0) {
                atual = atual.esquerdo;
            } else if (comparacao > 0) {
                atual = atual.direito;
            } else {
                break; //encontrado
            }
        }
        
        if (atual == null) {
            return; //n encontrado
        }
        
        //remover o no
        NodeAVL inicioRebalanceamento;
        
        if (atual.esquerdo == null || atual.direito == null) {
            //caso folha ou 1 filho
            NodeAVL filho = (atual.esquerdo != null) ? atual.esquerdo : atual.direito;
            inicioRebalanceamento = atual.pai;
            transplante(atual, filho);
        } else {
            //caso 2 filhos: busca sucessor in-order
            NodeAVL sucessor = atual.direito;
            while (sucessor.esquerdo != null) {
                sucessor = sucessor.esquerdo;
            }
            
            inicioRebalanceamento = (sucessor.pai == atual) ? atual : sucessor.pai;
            
            //copia os dados e mantém o nó na arvore
            atual.dado = sucessor.dado;
            
            //remove o sucessor 
            transplante(sucessor, sucessor.direito);
        }
        
        //subir rebalanceando via o pai
        //pode rebalancear vario niveis
        NodeAVL node = inicioRebalanceamento;
        while (node != null) {
            node.altura = 1 + Math.max(altura(node.esquerdo), altura(node.direito));

            int fb = fatorBalanceamento(node);
            NodeAVL novoTopo = null;

            //LL
            if (fb > 1 && fatorBalanceamento(node.esquerdo) >= 0) {
                novoTopo = rotacionarDireita(node);
            }

            //LR
            else if (fb > 1 && fatorBalanceamento(node.esquerdo) < 0) {
                rotacionarEsquerda(node.esquerdo);
                novoTopo = rotacionarDireita(node);
            }

            //RR
            else if (fb < -1 && fatorBalanceamento(node.direito) <= 0) {
                novoTopo = rotacionarEsquerda(node);
            }

            //RL
            else if (fb < -1 && fatorBalanceamento(node.direito) > 0) {
                rotacionarDireita(node.direito);
                novoTopo = rotacionarEsquerda(node);
            }

            if (novoTopo != null) {
                node = novoTopo; //continua subindo a partir do novo topo
            }

            node = node.pai;
        }
    }

    public int getRotacoes() {
        return rotacoes;
    }

    public int tamanho() {
        if (raiz == null) {
            return 0;
        }

        int count = 0;
        NodeAVL[] pilha = new NodeAVL[1024];
        int topo = 0;
        pilha[topo++] = raiz;

        while (topo > 0) {
            NodeAVL node = pilha[--topo];
            count++;
            if (node.esquerdo != null) {
                pilha[topo++] = node.esquerdo;
            }
            if (node.direito  != null) {
                pilha[topo++] = node.direito;
            }
        }
        return count;
    }

    public NodeAVL getRaiz() {
        return raiz;
    }

    //substitui o nó alvo pelo substituto na árvore, atualizando ponteiros
    private void transplante(NodeAVL alvo, NodeAVL substituto) {
        if (alvo.pai == null) {
            raiz = substituto;
        } else if (alvo.pai.esquerdo == alvo) {
            alvo.pai.esquerdo = substituto;
        } else {
            alvo.pai.direito = substituto;
        }

        if (substituto != null) {
            substituto.pai = alvo.pai;
        }
    }
}
