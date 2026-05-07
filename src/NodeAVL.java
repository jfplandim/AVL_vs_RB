public class NodeAVL {
    PacketRule dado;
    NodeAVL esquerdo;
    NodeAVL direito;
    int altura;

    public NodeAVL(PacketRule dado) {
        this.dado = dado;
        this.altura = 0;
    }
}