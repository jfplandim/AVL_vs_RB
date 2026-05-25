import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class geradorPackegerule {
   private static final long seed =1l;
   private static final int enderecos=10_000;

   private static String gerarIP(Random random){
       return random.nextInt(256)+"." +
               random.nextInt(256)+"."+
               random.nextInt(256)+"."+
               random.nextInt(256);
   }

   public static List<PacketRule> criarNo(){
       Random random= new Random(seed);
       List<PacketRule> regras= new ArrayList<>(enderecos);

       for(int i=0;i<enderecos;i++){
           String ipOrigem= gerarIP(random);
           String ipDestino = gerarIP(random);
           int prioridade= random.nextInt(100)+1;

           regras.add(new PacketRule(i,ipOrigem,ipDestino,prioridade));
       }
       return regras;
   }
}
