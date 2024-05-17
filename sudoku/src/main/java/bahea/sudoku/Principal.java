package bahea.sudoku;

import bahea.sudoku.model.Grid;
import bahea.sudoku.model.NewBoard;
import bahea.sudoku.model.SudokuNewBoardResponse;
import bahea.sudoku.service.ConsumoApi;
import bahea.sudoku.service.ConverteDados;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Principal {
    private ConsumoApi cs;
    private ConverteDados cv;
    private final String endereco = "https://sudoku-api.vercel.app/api/dosuku";
    private static int[][] matrix;
    private static int[][] solucao;
    private String dificuldade;
    private Scanner sc;
    private static List<Integer> linhas;
    private static List<Integer> colunas;

    public void jogo() {
        linhas = new ArrayList<>();
        colunas = new ArrayList<>();
        SudokuNewBoardResponse sudoku;
        cs = new ConsumoApi();
        cv = new ConverteDados();
        sc = new Scanner(System.in);
        Integer op = 1;
        Integer ganhou = 0;
        String json = cs.obterDados(endereco);
        int x = 0;

        try {
            sudoku = cv.obterDados(json, SudokuNewBoardResponse.class);

            if (sudoku != null && sudoku.getNewboard() != null && sudoku.getNewboard().getGrids() != null && !sudoku.getNewboard().getGrids().isEmpty()) {
                matrix = sudoku.getNewboard().getGrids().get(0).getValue();
                //  System.out.println("Tabuleiro Inicial:");
                //    print(matrix);
                // System.out.println("Solução:");
                solucao = sudoku.getNewboard().getGrids().get(0).getSolution();
                dificuldade = sudoku.getNewboard().getGrids().get(0).getDifficulty();

            } else {
                System.out.println("Erro: O tabuleiro ou a lista de grids está vazia.");
            }
        } catch (RuntimeException e) {
            System.out.println("Erro ao processar os dados do Sudoku: " + e.getMessage());
            System.out.println("Resposta da API: " + json);
        }
        System.out.println("Seja Bem vindo Ao Sudoko");

        while (ganhou == 0) {
            if (op == 1) {
                printMatriz((matrix));
                System.out.println();
                System.out.println("Digite O VALOR de 1 a 9");
                int chute = sc.nextInt();
                System.out.println("Digite Linha O VALOR de 1 a 9");
                int linha = sc.nextInt() - 1;
                System.out.println("Digte a coluna de 1 a 9");
                int coluna = sc.nextInt() - 1;

                if (checkingValores(chute, linha, coluna)) {
                    jogar(chute, linha, coluna);
                }
                if (verficaSeGanhou()) {
                    break;
                }
            }


            else if (op == 2) {
               if(linhas.size()>0){
                    voltarjogada();
                }else{
                   System.out.println("Nenhuma jogada disponivel para voltar");

               }

            }
            if (verficaSeGanhou()) {
                ganhou = 1;
            }
            System.out.println("1-Efetuar nova Jogada");
            System.out.println("2-Voltar  Jogada");
            op = sc.nextInt();

        }

    }
        public static void printMatriz ( int m[][]){

            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {


                    System.out.print(m[i][j] + " ");
                    if (j == 2 || j == 5) {

                        System.out.print("| ");
                    }
                }
                if (i == 2 || i == 5) {
                    System.out.println();
                    System.out.print("---------------------");
                }
                System.out.println();
            }


        }

    private boolean checkingValores(int chute, int linha, int coluna ) {
        if(linha<0 || linha>9){

            System.out.println("Valor da linha incorreto");
            return false;
        }
        if(coluna<0 || coluna>9){
            System.out.println("Valor da coluna incorreto");
            return false;
        }
        if(chute<0 || chute>9){
            System.out.println("Valor do chute incorreto");
            return false;
        }
        return true;

    }
    public static void jogar(int chute, int linha, int coluna) {
        if (verficaSeEstaVazio(linha, coluna)) {
            if (!verficaAcerto(chute, linha, coluna)) {

                return;
            }
            matrix[linha][coluna] = chute;
            linhas.add(linha);
            colunas.add(coluna);
            System.out.println("Bela jogada");
            printMatriz(matrix);

        }

    }
    public static void voltarjogada( ) {
        Scanner sc= new Scanner(System.in);
        int x =1;
        while(x ==1 ) {
            if (linhas.size() > 0) {
                System.out.println("Digite 1  para voltar uma jogada e 2 para voltar a jogar");
                x = sc.nextInt();
                int lastRow = linhas.size() - 1;
                int lastColumn = colunas.size() - 1;
                matrix[linhas.get(lastRow)][colunas.get(lastColumn)] = 0;
                linhas.remove(lastRow);
                colunas.remove(lastColumn);
                printMatriz(matrix);
            } else {
                System.out.println("Nenhum jogada disponivel para voltar");
                break;
            }
        }
    }


    public static boolean verficaAcerto(int chute, int i, int j) {
        int c=0;
        if (!verficaColunaLinha(chute,i,j)) {
            c++;
        }
        if(!verificaMesmoEscopo(i,j,chute)){
            c++;
        }
       return  c==0;
    }
    public static boolean verficaColunaLinha(int chute,int linha,int coluna) {

        for (int j = 0; j < 9; j++) {
            int valor = matrix[linha][j];
            if (valor == chute) {
                System.out.println("ERROU!!: A linha  " + (linha+1)+ " ja possui o  numero "+ chute);
                return false;
            }
        }
        for (int j = 0; j < 9; j++) {
            int valor = matrix[j][coluna];
            if (valor == chute) {
                System.out.println("ERROU!!: A coluna  " + (coluna+1)+ " ja possui o numero "+ chute);
                return false;
            }
        }
        return true;
    }


    public static boolean verficaSeEstaVazio(int l, int c) {
        if (matrix[l][c] == 0) {
            return true;
        }
        //vida--
        System.out.println("Tente novamente: Posição Invalida");

        return false;
    }


    public static boolean verificaMesmoEscopo(int l, int c, int chute) {
        if (l >= 0 && l <= 2 && c >= 0 && c <= 2) {
            // (0,0) até (2,2)
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (matrix[i][j] == chute) {
                        System.out.println("ERROU!!: Ja existe o numero " + chute + " nesse escopo");
                        return false;
                    }
                }
            }
        } else if (l >= 0 && l <= 2 && c >= 3 && c <= 5) {
            // (0,3) até (2,5)
            for (int i = 0; i < 3; i++) {
                for (int j = 3; j < 6; j++) {
                    if (matrix[i][j] == chute) {
                        System.out.println("ERROU!!: Ja existe o numero " + chute + " nesse escopo");
                        return false;
                    }
                }
            }
        } else if (l >= 0 && l <= 2 && c >= 6 && c <= 8) {
            // (0,6) até (2,8)
            for (int i = 0; i < 3; i++) {
                for (int j = 6; j < 9; j++) {
                    if (matrix[i][j] == chute) {
                        System.out.println("ERROU!!: Ja existe o numero " + chute + " nesse escopo");
                        return false;
                    }
                }
            }
        } else if (l >= 3 && l <= 5 && c >= 0 && c <= 2) {
            // (3,0) até (5,2)
            for (int i = 3; i < 6; i++) {
                for (int j = 0; j < 3; j++) {
                    if (matrix[i][j] == chute) {
                        System.out.println("ERROU!!: Ja existe o numero " + chute + " nesse escopo");
                        return false;
                    }
                }
            }
        } else if (l >= 3 && l <= 5 && c >= 3 && c <= 5) {
            // (3,3) até (5,5)
            for (int i = 3; i < 6; i++) {
                for (int j = 3; j < 6; j++) {
                    if (matrix[i][j] == chute) {
                        System.out.println("ERROU!!: Ja existe o numero " + chute + " nesse escopo");
                        return false;
                    }
                }
            }
        } else if (l >= 3 && l <= 5 && c >= 6 && c <= 8) {
            // (3,6) até (5,8)
            for (int i = 3; i < 6; i++) {
                for (int j = 6; j < 9; j++) {
                    if (matrix[i][j] == chute) {
                        System.out.println("ERROU!!: Ja existe o numero " + chute + " nesse escopo");
                        return false;

                    }
                }
            }
        } else if (l >= 6 && l <= 8 && c >= 0 && c <= 2) {
            // (6,0) até (8,2)
            for (int i = 6; i < 9; i++) {
                for (int j = 0; j < 3; j++) {
                    if (matrix[i][j] == chute) {
                        System.out.println("ERROU!!: Ja existe o numero " + chute + " nesse escopo");
                        return false;
                    }
                }
            }
        } else if (l >= 6 && l <= 8 && c >= 3 && c <= 5) {
            // (6,3) até (8,5)
            for (int i = 6; i < 9; i++) {
                for (int j = 3; j < 6; j++) {
                    if (matrix[i][j] == chute) {
                        System.out.println("ERROU!!: Ja existe o numero " + chute + " nesse escopo");
                        return false;
                    }
                }
            }
        } else if (l >= 6 && l <= 8 && c >= 6 && c <= 8) {
            // (6,6) até (8,8)
            for (int i = 6; i < 9; i++) {
                for (int j = 6; j < 9; j++) {
                    if (matrix[i][j] == chute) {
                        System.out.println("ERROU!!: Ja existe o numero " + chute + " nesse escopo");
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public static boolean verficaSeGanhou() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (matrix[i][j] == 0) {
                    return false;
                }
            }
        }
        System.out.println("*****************************");
        System.out.println("PARABÉNS VC GANHOU!!!!!!!!!!");
        System.out.println("*****************************");
        return true;
    }

    public static boolean verficaSeEstaCheia() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (matrix[i][j] == 0) {
                    return false;
                }
            }

        }
        return  true;
    }
}


