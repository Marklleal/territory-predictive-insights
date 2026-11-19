// Implementa Sensor e simula quantos atendimentos foram realizados na última hora.
package br.com.ubs;

public class SensorAtendimentos implements Sensor {

	@Override
	public int lerValor() {
		return 12;
	}

	@Override
	public boolean verificarFuncionamento() {
		return true;
	}

	@Override
	public String getTipo() {
		return "Atendimentos";
	}
    
}