// Interface para ler um valor, verificar o funcionamento e informar o tipo do sensor.
package br.com.ubs;

public interface Sensor {
    int lerValor();
    boolean verificarFuncionamento();
    String getTipo();
}