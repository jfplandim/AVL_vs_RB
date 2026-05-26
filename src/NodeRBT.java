public class NodeRBT {
    PacketRule dado;
    NodeRBT esquerdo;
    NodeRBT direito;
    NodeRBT pai;
    boolean vermelho; //true = vermelho, false = preto

    public NodeRBT(PacketRule dado) {
        this.dado = dado;
        this.vermelho = true; //todo nó nasce vermelho
    }
}
