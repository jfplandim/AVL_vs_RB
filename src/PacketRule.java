public class PacketRule {
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
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof PacketRule)) return false;
        PacketRule other = (PacketRule) obj;
        return this.id == other.id && this.prioridade == other.prioridade;
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
