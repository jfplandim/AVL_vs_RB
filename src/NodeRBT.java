public class NodeRBT {
    PacketRule dado;
    NodeRBT esquerdo;
    NodeRBT direito;
    NodeRBT pai;
    boolean vermelho; //true = vermelho, false = preto

    public NodeRBT(PacketRule dado, NodeRBT esquerdo, NodeRBT direito, NodeRBT pai, boolean vermelho) {
        this.dado = dado;
        this.esquerdo = null;
        this.direito = null;
        this.pai = null;
        this.vermelho = true; //todo no nasce vermelho
    }
}
