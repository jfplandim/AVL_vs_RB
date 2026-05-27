import java.util.*;


public class geradorPacketRule {
   private static final long seed =1l;

   private static String gerarIP(Random random){
       return random.nextInt(256)+"." +
               random.nextInt(256)+"."+
               random.nextInt(256)+"."+
               random.nextInt(256);
   }

   public static List<PacketRule> criarNo(int enderecos){
       Random random= new Random(seed);
       List<PacketRule> regras= new ArrayList<>(enderecos);

       for(int i=0;i<enderecos;i++){
           String ipOrigem= gerarIP(random);
           String ipDestino = gerarIP(random);
           int prioridade= random.nextInt(Integer.MAX_VALUE);

           regras.add(new PacketRule(i,ipOrigem,ipDestino,prioridade));
       }
       return regras;
   }


}
