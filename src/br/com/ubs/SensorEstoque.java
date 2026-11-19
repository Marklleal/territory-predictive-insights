// Implementa Sensor e simula quantos kits de atendimento estão disponíveis.
package br.com.ubs;

public class SensorEstoque implements Sensor {

	@Override
	public int lerValor() {
		return 30;
	}

	@Override
	public boolean verificarFuncionamento() {
		return true;
	}

	@Override
	public String getTipo() {
		return "Estoque";
	}
    
}