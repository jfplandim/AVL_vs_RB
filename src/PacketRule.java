public class PacketRule implements  Comparable<PacketRule> {
    private int id;
    private String ipOrigem;
    private String ipDestino;
    private int prioridade;

    public PacketRule(int id, String ipOrigem, String ipDestino, int prioridade) {
        this.id = id;
        this.ipOrigem = ipOrigem;
        this.ipDestino = ipDestino;
        this.prioridade = prioridade;
    }

    public int getId() {
        return id;
    }

    public String getIpOrigem() {
        return ipOrigem;
    }

    public String getIpDestino() {
        return ipDestino;
    }

    public int getPrioridade() {
        return prioridade;
    }

    @Override
    public int compareTo(PacketRule other) {
        //regra 1: prioridade
        if (this.prioridade != other.prioridade) {
            return Integer.compare(this.prioridade, other.prioridade);
        }

        //regra 2: ID
        return Integer.compare(this.id, other.id);
    }


    @Override
    public String toString() {
        return "PacketRule{" +
                "id=" + id +
                ", ipOrigem='" + ipOrigem + '\'' +
                ", ipDestino='" + ipDestino + '\'' +
                ", prioridade=" + prioridade +
                '}';
    }
}
