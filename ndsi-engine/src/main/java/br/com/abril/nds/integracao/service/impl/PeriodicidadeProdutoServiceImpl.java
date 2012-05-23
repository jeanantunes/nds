package br.com.abril.nds.integracao.service.impl;

import org.springframework.stereotype.Service;

import br.com.abril.nds.integracao.service.PeriodicidadeProdutoService;
import br.com.abril.nds.model.cadastro.PeriodicidadeProduto;

@Service
public class PeriodicidadeProdutoServiceImpl implements PeriodicidadeProdutoService{

	@Override
	public PeriodicidadeProduto getPeriodicidadeProdutoAsArchive(Integer periodicidade) {
		if(periodicidade <= 7)
			return PeriodicidadeProduto.SEMANAL;
		else if(periodicidade <= 15)
			return PeriodicidadeProduto.QUINZENAL;
		else if(periodicidade <= 30)
			return PeriodicidadeProduto.MENSAL;
		/*else if (periodicidade <= 60)
			return PeriodicidadeProduto.BIMESTRAL;*/
		else if(periodicidade <= 90)
			return PeriodicidadeProduto.TRIMESTRAL;
		else if(periodicidade <= 180)
			return PeriodicidadeProduto.SEMESTRAL;
		else
			return PeriodicidadeProduto.ANUAL;
	}

}
