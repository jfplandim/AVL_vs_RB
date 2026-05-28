public class NodeAVL {
    PacketRule dado;
    NodeAVL esquerdo;
    NodeAVL direito;
    NodeAVL pai;
    int altura;

    public NodeAVL(PacketRule dado) {
        this.dado = dado;
        this.altura = 0;
        this.esquerdo = null;
        this.direito = null;
        this.pai = null;
    }
}