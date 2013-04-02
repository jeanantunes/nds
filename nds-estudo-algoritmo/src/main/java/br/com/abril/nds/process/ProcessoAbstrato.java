package br.com.abril.nds.process;

import org.springframework.stereotype.Component;

import br.com.abril.nds.model.estudo.EstudoTransient;

@Component
public abstract class ProcessoAbstrato {

	public abstract void executar(EstudoTransient estudo) throws Exception;
}
