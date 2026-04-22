package teclado;

import java.util.Scanner;

public class Teclado {
    private static final Scanner teclado = new Scanner(System.in);

    private Teclado() {}

    public static String inserirTexto(String exemplo, String mensagemDeErro){
        String texto = null;

        for(;;){
            System.out.print(exemplo);
            texto = teclado.nextLine();

            if (texto != null && !texto.isBlank()) return texto;
            
            System.err.println(mensagemDeErro);
        }
    }
}