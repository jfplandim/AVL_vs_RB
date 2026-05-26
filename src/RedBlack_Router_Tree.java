public class RedBlack_Router_Tree {
    private NodeRBT raiz;
    private NodeRBT nil;    //sentinela
    private int rotacoes;
    private int tamanhoDaArvore;

    public RedBlack_Router_Tree() {
        this.rotacoes = 0;
        this.nil = new NodeRBT(null);   //dado do sentinela é null
        this.nil.vermelho = false;            // sentinela é sempre preto

        //ponteiros da sentinale apontam para ela
        this.nil.esquerdo = this.nil;
        this.nil.direito = this.nil;
        this.nil.pai = this.nil;

        //arvore nasce vazia, logo a raiz aponta para a sentinela
        this.raiz = this.nil;
        this.tamanhoDaArvore = 0;
    }

    private void rotacionarEsquerda(NodeRBT x) {
        NodeRBT y = x.direito; //define y como filho a direita de x
        x.direito = y.esquerdo;

        //transforma a subárvore esquerda de x na subarvore direita de x
        if (y.esquerdo != this.nil) {
            y.esquerdo.pai = x;
        }

        //liga o pai de x a y
        y.pai = x.pai;
        if (x.pai == this.nil) {    //se x era a raiz (pai é sentinela)
            this.raiz =y;           //y passa a ser a nova raiz
        } else if (x == x.pai.esquerdo) {
            x.pai.esquerdo = y;
        } else {
            x.pai.direito = y;
        }

        //coloca x a esquerda de y
        y.esquerdo = x;
        x.pai = y;

        rotacoes++;
    }

    private void rotacionarDireita(NodeRBT x){
        NodeRBT y = x.esquerdo;     //define y como filho a esquerda de x
        x.esquerdo = y.direito;

        //transforma a subárvore direita de y na subárvore esquerda de x
        if (y.direito != this.nil) {
            y.direito.pai = x;
        }

        //liga o pai de x a y
        y.pai = x.pai;
        if (x.pai == this.nil) {    //se x era a raiz (pai é sentinela)
            this.raiz = y;          //y passa a ser a nova raiz
        } else if (x == x.pai.direito) {
            x.pai.direito = y;
        } else {
            x.pai.esquerdo = y;
        }

        //coloca x a direita de y
        y.direito = x;
        x.pai =y;

        rotacoes++;
    }

    public void inserir(PacketRule novoDado) {
        //instacia o nono nó z
        NodeRBT z = new NodeRBT(novoDado);
        z.esquerdo = this.nil;
        z.direito = this.nil;
        z.vermelho = true;  //todo novo nó é vermelho

        NodeRBT y = this.nil;
        NodeRBT x = this.raiz;

        //lógica padrão da bst
        while (x != this.nil) {
            y =x;
            //delega a decisão para o (prioridade + ID) do compareTo
            int comparacao = novoDado.compareTo(x.dado);

            if (comparacao < 0) {
                x = x.esquerdo;
            } else if (comparacao > 0) {
                x = x.direito;
            } else {
                //empate absoluto (msm prioridade e msm id, msm sendo impossivel pois o id é unico)
                return;
            }
        }

        //conecta o pai de z
        z.pai = y;
        if (y == this.nil) {
            this.raiz = z;  //arvore vazia
        } else if (novoDado.compareTo(y.dado) < 0) {
            y.esquerdo = z;
        } else {
            y.direito = z;
        }

        //corrige violaçãoes das rbt
        inserirFixup(z);

        //atualização do tamanho da arvore
        this.tamanhoDaArvore++;
    }

    private void inserirFixup(NodeRBT z) {
        //enquanto o pai for vermelho, existe violação
        while (z.pai.vermelho) {

            //verifica se o pai de z é o filho á esquerda do avô
            if (z.pai == z.pai.pai.esquerdo) {
                NodeRBT y = z.pai.pai.direito;  //y é o tio de z

                if (y.vermelho) {
                    //caso 1: o tio é vermelho -> recoloração
                    z.pai.vermelho = false;     //pai fica preto
                    y.vermelho = false;         //tio fica preto
                    z.pai.pai.vermelho = true;  //avô fica vermelho
                    z = z.pai.pai;              //sobe a verificação para o avô
                } else {
                    //caso 2: o tio é preto e z um filho a direita -> triangulo
                    if (z == z.pai.direito) {
                        z = z.pai;
                        rotacionarEsquerda(z);  //alinha os nós para aplicar caso 3
                    }
                    //caso 3: o tio é preto e z é um filho a esquerda -> reta
                    z.pai.vermelho = false;     //pai fica preto
                    z.pai.pai.vermelho = true;  //avo fica vermelho
                    rotacionarDireita(z.pai.pai); //rotação no avo para balancear
                }
            }

            //simetria: o pai de z é o filho á direita do avô
            else {
                NodeRBT y = z.pai.pai.esquerdo; //y é o tio de z

                if (y.vermelho) {
                    //caso 1: simétrico
                    z.pai.vermelho = false;
                    y.vermelho = false;
                    z.pai.pai.vermelho = true;
                    z = z.pai.pai;
                } else {
                    //caso 2
                    if (z == z.pai.esquerdo) {
                        z = z.pai;
                        rotacionarDireita(z);
                    }
                    //caso 3
                    z.pai.vermelho = false;
                    z.pai.pai.vermelho = true;
                    rotacionarEsquerda(z.pai.pai);
                }
            }
        }
        //garante a raiz seja sempre preta
        this.raiz.vermelho = false;
    }

    public NodeRBT buscar(PacketRule pacoteAlvo) {
        NodeRBT x = this.raiz;      //começa a busca pela raiz

        //enquanto nao atingit a sentinela
        while (x != this.nil) {
            //usa a logica prioridade + id
            int comparacao = pacoteAlvo.compareTo(x.dado);

            if (comparacao == 0) {
                return x;   //sucesso na busca
            } else if (comparacao < 0) {
                x = x.esquerdo;     //o alvo é menor, desce para a esquerda
            } else {
                x = x.direito;      //o alvo é maior, desce para a direita
            }
        }

        //se o laço terminar e x for igual a this.nil, a busca falha
        return this.nil;    //retorna this.nil indicando que não existe
    }

    //substitui a arvore de u por v
    private void transplant(NodeRBT u, NodeRBT v) {
        if (u.pai == this.nil) {
            this.raiz = v;
        } else if (u == u.pai.esquerdo) {
            u.pai.esquerdo = v;
        } else {
            u.pai.direito = v;
        }
        //ligação do pai
        v.pai = u.pai;
    }

    //encontra o no com menor chave (mais a esquerda) em uma subarvore
    private NodeRBT minimo(NodeRBT no) {
        while (no.esquerdo != this.nil) {
            no = no.esquerdo;
        }
        return no;
    }

    public void remover(PacketRule pacoteAlvo) {
        //encontra o nó a ser removido usando a função busca
        NodeRBT z = buscar(pacoteAlvo);
        if (z == null || z == this.nil) {
            return; //a regra n existe no roteador
        }

        NodeRBT y = z;
        NodeRBT x;
        boolean yCorOriginal = y.vermelho;  //lembrar a cor de quem vai sair

        //caso 1 e 2: z tem zero ou apenas um filho
        if (z.esquerdo == this.nil) {
            x = z.direito;
            transplant(z, z.direito);
        } else if (z.direito == this.nil) {
            x = z.esquerdo;
            transplant(z, z.esquerdo);
        }

        //caso 3: z tem dois filhos
        else {
            y = minimo(z.direito);  //busca o sucessor na arvore direita
            yCorOriginal = y.vermelho;
            x = y.direito;

            if (y.pai == z) {
                x.pai = y;  //x aponta para y (msm se x for sentina)
            } else {
                transplant(y, y.direito);
                y.direito = z.direito;
                y.direito.pai = y;
            }

            transplant(z, y);
            y.esquerdo = z.esquerdo;
            y.esquerdo.pai = y;
            y.vermelho = z.vermelho;    //y herda a cor de z
        }

        //se a cor do nó que foi removido era preta, as regras foram violadas
        if (!yCorOriginal) {
            removerFixup(x);
        }

        //atualizar o tamanho da arvore
        this.tamanhoDaArvore--;
    }

    private void removerFixup(NodeRBT x) {
        //enquanto o nó for a raiz e for preto
        while (x != this.raiz && x.vermelho == false) {

            if (x == x.pai.direito) {
                NodeRBT w = x.pai.direito; // w é o irmao de x

                //caso 1: o irmao de x é vermelho
                if (w.vermelho) {
                    w.vermelho = false;
                    x.pai.vermelho = true;
                    rotacionarEsquerda(x.pai);
                    w = x.pai.direito;
                }

                //caso 2: o irmao de x é preto e os filhos do irmao sao pretos
                if (w.esquerdo.vermelho == false && w.direito.vermelho == false) {
                    w.vermelho = true;  //retira o preto extra de x e w
                    x = x.pai;          //sobe o preto extra para o pai
                } else {
                    //caso 3: o irmao é preto, o filho esquerdo do irmao é vermelho e direito é preto
                    if (w.direito.vermelho == false) {
                        w.esquerdo.vermelho = false;
                        w.vermelho = true;
                        rotacionarDireita(w);
                        w = x.pai.direito;
                    }

                    //caso 4: o irmao é preto e o filho direito do irmao é vermelho
                    w.vermelho = x.pai.vermelho;
                    x.pai.vermelho = false;
                    w.direito.vermelho = false;
                    rotacionarEsquerda(x.pai);
                    x = this.raiz;              //encerra o laço
                }
            }
            //simetria (x é o filho a direita)
            else {
                NodeRBT w = x.pai.esquerdo;

                //caso 1
                if (w.vermelho) {
                    w.vermelho = false;
                    x.pai.vermelho = true;
                    rotacionarDireita(x.pai);
                    w = x.pai.esquerdo;
                }

                //caso 2
                if (w.direito.vermelho == false && w.esquerdo.vermelho == false) {
                    w.vermelho = true;
                    x = x.pai;
                } else {
                    //caso 3
                    if (w.esquerdo.vermelho == false) {
                        w.direito.vermelho = false;
                        w.vermelho = true;
                        rotacionarEsquerda(w);
                        w = x.pai.esquerdo;
                    }

                    //caso 4
                    w.vermelho = x.pai.vermelho;
                    x.pai.vermelho = false;
                    w.esquerdo.vermelho = false;
                    rotacionarDireita(x.pai);
                    x = this.raiz;
                }
            }
        }
        //garante que o no compensatorio (ou raiz) seja preto
        x.vermelho = false;
    }

    public int getRotacoes() {
        return rotacoes;
    }

    public NodeRBT getRaiz() {
        return this.raiz;
    }

    public int tamanho() {
        return this.tamanhoDaArvore;
    }
}
