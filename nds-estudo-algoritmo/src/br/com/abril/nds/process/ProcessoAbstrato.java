package br.com.abril.nds.process;

import br.com.abril.nds.model.Estudo;

public abstract class ProcessoAbstrato {

    protected Estudo estudo;
    
    public Estudo executar(Estudo estudo) throws Exception {
    	System.out.println("Inicio do Processo: "+ this.getClass().getName() +"\nEntrada:\n"+ estudo);
        this.estudo = estudo;
        executarProcesso();
        System.out.println("Fim do Processo: "+ this.getClass().getName() +"\nResultados:\n"+ estudo);
        return this.estudo;
    }
    
    /**
     * Método utilizado apenas para dar sequencia no fluxo de execução do processo. Função semelhante a do método main.
     * Implementa-lo para chamar o método calcular() do processo e eventuais subprocessos.
     */
    protected abstract void executarProcesso() throws Exception;
    
    /**
     * Método utilizado para efetivamente realizar os cálculos do processo.
     * @throws Exception 
     */
    protected abstract void executar() throws Exception;
}
