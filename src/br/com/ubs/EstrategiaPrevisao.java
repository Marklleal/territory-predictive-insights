// Classe abstrata que define o método prever() e os atributos comuns às estratégias.
package br.com.ubs;

import java.util.List;

public abstract class EstrategiaPrevisao {
    public abstract double prever(List<Integer> leituras);
}
