package br.com.abril.nds.process;

import br.com.abril.nds.model.Estudo;

public abstract class ProcessoAbstrato {

	protected Estudo estudo;
	
	protected ProcessoAbstrato(){
		
	}
	
	protected ProcessoAbstrato (Estudo estudo) {
		this.estudo = estudo;
	}
    
    public void executar() {
        this.calcular();
        executarProcesso();
    }
    
    /**
     * Método utilizado apenas para dar sequência no fluxo de execução do processo. Função semelhante à do método main.
     * Implementá-lo para chamar o método calcular() do processo e eventuais subprocessos.
     */
    protected abstract void executarProcesso();
    
    /**
     * Método utilizado para efetivamente realizar os cálculos do processo.
     */
    protected abstract void calcular();
    
    public Estudo getEstudo() {
    	return this.estudo;
    }

}
