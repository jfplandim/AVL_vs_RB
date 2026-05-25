import javax.lang.model.element.PackageElement;
import java.util.ArrayList;
import java.util.List;

public class teste {
    public static void main(String[] args) {
        System.out.println("Gerando registros");
        List<PacketRule> regras= new geradorPackegerule().criarNo();
        System.out.println("Registros criados com sucesso");

        //Inserção nas árvores.
        AVL_Router_Tree avl=new AVL_Router_Tree();

        long inicio=System.nanoTime();
        for(PacketRule regra :regras){
            avl.inserir(regra);
        }
        long fim=System.nanoTime();
        long tempoInsercao=fim-inicio;//tempo resultante da busca.

        //Busca nas arvores.
        inicio=System.nanoTime();
        for(int i=0;i<1_000;i++){
            avl.buscar(i);//lista da maior até a menor prioridade.
        }
        fim=System.nanoTime();
        long tempoBuscar=fim-inicio;

        //IDs para deleção.
        inicio=System.nanoTime();
        for(int i=0;i<1_000;i++){
            avl.remover(i);
        }
        fim=System.nanoTime();
        long tempoRemover=fim-inicio;





        System.out.println("\nRESUMO COMPARATIVO:");
        System.out.println("Operação     | AVL (ns/op)");
        System.out.println("-------------|-------------|------------");
        System.out.printf("Inserção     %11d%n", tempoInsercao / 10_000);
        System.out.printf("Busca         %11d%n", tempoBuscar   / 10_000);
        System.out.printf("Deleção       %11d%n", tempoRemover / 10_000);
    }
}
