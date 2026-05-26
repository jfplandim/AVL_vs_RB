public class RedBlack_Router_Tree {
    private NodeRBT raiz;
    private NodeRBT nil;    //sentinela
    private int rotacoes;

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
}
