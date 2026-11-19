// Estende EstrategiaPrevisao e projeta a tendência comparando dois períodos consecutivos.
package br.com.ubs;

import java.util.List;

// Pega as seis últimas leituras, divide em dois grupos de três 
// e calcula quanto a média mudou de um grupo para o outro. 
public class PrevisaoComparacao extends EstrategiaPrevisao {

	@Override
	public double prever(List<Integer> leituras) {
	    int total = leituras.size();
		int soma = 0;
		
		for (int i = total - 6; i < total - 3; i++) {
		    soma += leituras.get(i);
		}

		double mediaAnterior = soma / 3.0;
		soma = 0;

		for (int i = total - 3; i < total; i++) {
		    soma += leituras.get(i);
		}
        double mediaRecente = soma / 3.0;
		
		return mediaRecente + (mediaRecente - mediaAnterior);
	}
}