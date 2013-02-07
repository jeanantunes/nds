package br.com.abril.nds.process;

import br.com.abril.nds.model.Estudo;

public abstract class ProcessoAbstrato {

	protected Estudo estudo;

	protected ProcessoAbstrato() {

	}

	protected ProcessoAbstrato(Estudo estudo) {
		this.estudo = estudo;
	}

	/**
	 * Método utilizado apenas para dar sequencia no fluxo de execução do
	 * processo. Função semelhante a do método main. Implementa-lo para chamar o
	 * método calcular() do processo e eventuais subprocessos.
	 */
	protected abstract void executarProcesso() throws Exception;

	/**
	 * Get estudo.
	 * 
	 * @return
	 */
	public Estudo getEstudo() {
		return this.estudo;
	}

	public void executar() throws Exception {
		System.out.println("Input: "+ estudo);
		executarProcesso();
		System.out.println("Output: "+ estudo);
	}
}
