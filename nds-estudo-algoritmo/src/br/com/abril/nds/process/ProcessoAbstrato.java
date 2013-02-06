package br.com.abril.nds.process;

import br.com.abril.nds.model.Estudo;

public abstract class ProcessoAbstrato {

    protected Estudo estudo;
    
    public Estudo executar(Estudo estudo) throws Exception {
    	System.out.println("In�cio do Processo: "+ this.getClass().getName() +"\nEntrada:\n"+ estudo);
        this.estudo = estudo;
        executarProcesso();
        System.out.println("Fim do Processo: "+ this.getClass().getName() +"\nResultados:\n"+ estudo);
        return this.estudo;
    }
    
    /**
     * M�todo utilizado apenas para dar sequ�ncia no fluxo de execu��o do processo. Fun��o semelhante � do m�todo main.
     * Implement�-lo para chamar o m�todo calcular() do processo e eventuais subprocessos.
     * @throws Exception 
     */
    protected abstract void executarProcesso() throws Exception;
    
    /**
     * M�todo utilizado para efetivamente realizar os c�lculos do processo.
     * @throws Exception 
     */
    protected abstract void calcular() throws Exception;
}
