// Estende EstrategiaPrevisao e calcula a média das últimas N leituras.
package br.com.ubs;

import java.util.List;

public class PrevisaoMediaMovel extends EstrategiaPrevisao {

	@Override
	public double prever(List<Integer> leituras) {
		// acumulador
		int soma = 0;

		// considera apenas as últimas 3 medições
		for (int i = leituras.size() - 3; i < leituras.size(); i++) {
		    soma += leituras.get(i);
		}
		
		return soma / 3;
	}
}