package br.com.abril.nds.process;

import br.com.abril.nds.model.GenericDTO;

public abstract class ProcessoAbstrato {

    protected GenericDTO<?> genericDTO;

    protected ProcessoAbstrato() {

    }

    protected ProcessoAbstrato(GenericDTO<?> genericDTO) {
	this.genericDTO = genericDTO;
    }

    /**
     * Método utilizado apenas para dar sequencia no fluxo de execução do
     * processo. Função semelhante a do método main. Implementa-lo para chamar o
     * método calcular() do processo e eventuais subprocessos.
     */
    protected abstract void executarProcesso() throws Exception;

    public GenericDTO<?> getGenericDTO() {
	return genericDTO;
    }

    public void executar() throws Exception {
	executarProcesso();
    }
}
