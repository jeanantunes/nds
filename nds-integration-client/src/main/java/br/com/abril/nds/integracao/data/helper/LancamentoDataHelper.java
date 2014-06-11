package br.com.abril.nds.integracao.data.helper;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.model.cadastro.OperacaoDistribuidor;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.repository.AbstractRepository;
import br.com.abril.nds.service.CalendarioService;
import br.com.abril.nds.service.LancamentoService;
import br.com.abril.nds.service.integracao.DistribuidorService;



public class LancamentoDataHelper extends AbstractRepository {

	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private LancamentoService lancamentoService;
	
	@Autowired
	private CalendarioService calendarioService;

	private final Date distribuidorOperacao;

	private  Date dataLancamentoDistribuidor;

	public LancamentoDataHelper() {
		
		distribuidorOperacao = distribuidorService.obterDataOperacaoDistribuidor();
		
	}
	

	
public Date getDataLancamentoDistribuidorHelper(Lancamento lancamento) throws Exception{
		
	  if(!calendarioService.isDiaOperante(lancamento.getDataLancamentoDistribuidor(), new Long(1), OperacaoDistribuidor.DISTRIBUICAO)
		|| distribuidorOperacao.before(lancamento.getDataLancamentoDistribuidor())){
		  
		  do {
			  dataLancamentoDistribuidor  = calendarioService.obterProximaDataDiaUtil(lancamento.getDataLancamentoDistribuidor());
		  } while (!calendarioService.isDiaOperante(dataLancamentoDistribuidor, new Long(1), OperacaoDistribuidor.DISTRIBUICAO));

	  }
    
	  return dataLancamentoDistribuidor;
	}

}
