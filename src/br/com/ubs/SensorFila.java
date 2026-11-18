// Implementa Sensor e simula quantas pessoas aguardam atendimento.
package br.com.ubs;

public class SensorFila implements Sensor {

	@Override
	public int lerValor() {
		return 5;
	}

	@Override
	public boolean verificarFuncionamento() {
		return true;
	}

	@Override
	public String getTipe() {
		return "Fila";
	}
    
}