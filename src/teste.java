import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
public class teste {
    public static void main(String[] args) {
        Scanner sc= new Scanner(System.in);
        System.out.println("Digite quantos registros voce deseja inserir");
        int quantidade  = sc.nextInt();
        List<PacketRule> regras= new geradorPackegerule().criarNo(quantidade);
        System.out.println("Registros criados com sucesso");


        AVL_Router_Tree avl=new AVL_Router_Tree();

        int valorOperação=Math.min(1_000,quantidade);


        //Inserção nas árvores.
        Long inicio= System.nanoTime();
        for(PacketRule regra: regras){
            avl.inserir(regra);
        }
        Long fim=System.nanoTime();
        Long tempoInsercao= fim-inicio;


        //coleta as prioridades dos elementos na árvore.
        List<Integer> prioridades = new ArrayList<>();
        for( PacketRule regra: regras) {
            prioridades.add(regra.getPrioridade());

        }

        //busca da árvore.
        inicio=System.nanoTime();
        for(int i=0;i<1_000;i++){
            avl.buscar(prioridades.get(i));
        }
         fim=System.nanoTime();
        Long tempoBuscar= fim-inicio;

        //remoção da árvore.
        inicio=System.nanoTime();
        for(int i=0;i<1_000;i++){
            avl.remover(prioridades.get(i));
        }
        fim=System.nanoTime();
         Long tempoRemover=fim-inicio;



        System.out.println("\nRESUMO COMPARATIVO:");
        System.out.println("Operação     | AVL (ns/op)");
        System.out.println("-------------|-------------|------------");
        System.out.printf("Inserção     %11d%n", tempoInsercao / quantidade);
        System.out.printf("Busca         %11d%n", tempoBuscar   / valorOperação);
        System.out.printf("Deleção       %11d%n", tempoRemover / valorOperação);
    }
}
