import java.util.List;
import java.util.Scanner;
public class teste {
    public static void main(String[] args) {
        Scanner sc= new Scanner(System.in);
        System.out.println("Digite quantos registros voce deseja inserir");
        int quantidade  = sc.nextInt();
        List<PacketRule> regras= new geradorPacketRule().criarNo(quantidade);
        System.out.println("Registros criados com sucesso");


        AVL_Router_Tree avl=new AVL_Router_Tree();
        RedBlack_Router_Tree rb=new RedBlack_Router_Tree();

        int valorOperação=Math.min(1_000,quantidade);


        //Inserção nas AVL.
        long inicio= System.nanoTime();
        for(PacketRule regra: regras){
            avl.inserir(regra);
        }
        long fim=System.nanoTime();
        long tempoInsercao= fim-inicio;

        //Inserção RBT.
        inicio= System.nanoTime();
        for(PacketRule regra: regras){
            rb.inserir(regra);
        }
        fim=System.nanoTime();
        long InsercaoRBT= fim-inicio;


        //busca da árvore.
        inicio=System.nanoTime();
        for(int i=0;i<valorOperação;i++){
            PacketRule regra= regras.get(i);
            avl.buscar(regra);
        }
         fim=System.nanoTime();
        long tempoBuscar= fim-inicio;

        //Busca RBT.
        inicio= System.nanoTime();
        for(int i=0;i<valorOperação;i++){
            rb.buscar(regras.get(i));
        }
        fim=System.nanoTime();
        long tempoBuscaRBT= fim-inicio;


        int excessao=(int)(quantidade*0.20);//regra dos 20%
        //remoção da AVL.
        inicio=System.nanoTime();
        for(int i=0;i<excessao;i++){
            PacketRule regra= regras.get(i);
            avl.remover(regra);
        }
        fim=System.nanoTime();
         long tempoRemover=fim-inicio;

         //Remoção RBT
        inicio=System.nanoTime();
        for(int i=0;i<excessao;i++){
            PacketRule regra= regras.get(i);
            rb.remover(regra);
        }
        fim=System.nanoTime();
        long remocaoRBT= fim-inicio;



        System.out.println("\nRESUMO COMPARATIVO:");
        System.out.println("Operação     | AVL (ns/op) | RBT (ns/op)  ");
        System.out.println("-------------|-------------|------------");
        System.out.printf("Inserção      %11d |  %d%n", tempoInsercao / quantidade, InsercaoRBT / quantidade);
        System.out.printf("Busca         %11d |  %d%n", tempoBuscar   / valorOperação, tempoBuscaRBT / quantidade);
        System.out.printf("Deleção       %11d |  %d%n", tempoRemover / excessao,  remocaoRBT / excessao);
    }
}
