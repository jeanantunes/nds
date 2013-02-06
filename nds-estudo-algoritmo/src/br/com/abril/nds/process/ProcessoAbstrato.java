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
     * M�todo utilizado apenas para dar sequ�ncia no fluxo de execu��o do processo. Fun��o semelhante � do m�todo main.
     * Implement�-lo para chamar o m�todo calcular() do processo e eventuais subprocessos.
     */
    protected abstract void executarProcesso();
    
    /**
     * M�todo utilizado para efetivamente realizar os c�lculos do processo.
     */
    protected abstract void calcular();
    
    public Estudo getEstudo() {
    	return this.estudo;
    }

}
