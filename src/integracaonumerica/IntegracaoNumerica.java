/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package integracaonumerica;

/**
 *
 * @author ellia
 */
class IntegracaoNumerica {
    public static double funcao() {
        
    }
    
    public static double trapezio(double a, double b, int n) {
        double h = (b - a) / n; // Tamanho do subintervalo
        double soma = 0;

        for (int i = 0; i <= n; i++) {
            double x = a + i * h;
            soma += funcao(x);
        }

        return h * (soma - (funcao(a) + funcao(b)) / 2);
    }
    
     public static double simpson(double a, double b, int n) {
        double h = (b - a) / n; // Tamanho do subintervalo
        double soma = funcao(a) + funcao(b);

        for (int i = 1; i < n; i++) {
            double x = a + i * h;

            if (i % 2 == 0) {
                soma += 2 * funcao(x);
            } else {
                soma += 4 * funcao(x);
            }
        }

        return h * soma / 3;
    }
     
    public static double richardson(double a, double b, int n, int k) {
        double h1 = (b - a) / n; // Tamanho do passo inicial

        // Cálculo da primeira aproximação
        double I1 = trapezio(a, b, n);

        // Cálculo da segunda aproximação com tamanho de passo reduzido
        double I2 = trapezio(a, b, n * k);

        // Estimativa mais precisa
        return (Math.pow(k, 2) * I2 - I1) / (Math.pow(k, 2) - 1);
    }
}
