package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.CotaDTO;
import br.com.abril.nds.dto.filtro.FiltroCotaDTO;
import br.com.abril.nds.model.ClassificacaoCotaDistribuidorEnum;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.DistribuidorClassificacaoCota;
import br.com.abril.nds.model.distribuicao.RankingFaturamento;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.DistribuidorClassificacaoCotaRepository;
import br.com.abril.nds.service.ClassificacaoCotaService;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.RankingFaturamentoService;

@Service
public class ClassificacaoCotaServiceImpl implements ClassificacaoCotaService {
    
	@Autowired
	CotaService cotaService; 
	
	
	@Autowired
	DistribuidorClassificacaoCotaRepository distribuidorClassificacaoCotaRepository;
	
	@Autowired
	RankingFaturamentoService rankingFaturamentoService;
	
	@Autowired
	CotaRepository cotaRepository;
	
	
	@Override
	@Transactional
	public void executeReclassificacaoCota() {

		FiltroCotaDTO filtro= new FiltroCotaDTO();
		
	  List<CotaDTO> cotas=	  cotaService.obterCotas(filtro);
	  	
	  List<DistribuidorClassificacaoCota>  distribuidorClassificacaoCotas=  distribuidorClassificacaoCotaRepository.buscarTodos();
	  
	  for(CotaDTO cotaDTO: cotas){
		  Cota cota = new Cota();
		  cota.setId(cotaDTO.getIdCota());
		  String tipoClassificacao = new String();
		  // BUSCA O ULTIMO FATURAMENTO GERARDO DA COTA PELA ROTINA executeJobGerarRankingFaturamento
		  //FIXME erro na query
		  List<RankingFaturamento> rf = rankingFaturamentoService.buscarPorCota(cota);
		  if(!rf.isEmpty()){
			  BigDecimal valorFaturamento = rf.get(0).getFaturamento();
			  
			  for(DistribuidorClassificacaoCota dCC: distribuidorClassificacaoCotas ){
				  if(dCC.getValorDe().compareTo(valorFaturamento)==-1 && 
					 dCC.getValorAte().compareTo(valorFaturamento)==1)
				  
				  {
					  tipoClassificacao = dCC.getCodigoClassificacaoCota().toString(); 
					 break;
				  }
			  }
			  
			  if(  tipoClassificacao !=null && tipoClassificacao.isEmpty()){
				  cota =cotaRepository.buscarCotaPorID(cota.getId());
				  int quantidadePDV =cota.getPdvs().size();
				  if( tipoClassificacao.equals(ClassificacaoCotaDistribuidorEnum.A) && quantidadePDV>1){
					  tipoClassificacao = ClassificacaoCotaDistribuidorEnum.AA.toString();
				  }
				  else if (tipoClassificacao.equals(ClassificacaoCotaDistribuidorEnum.A) && quantidadePDV==1){
					  tipoClassificacao = ClassificacaoCotaDistribuidorEnum.A.toString();
				  }
				  //TODO ALMIR - VERIFICAR ONDE VAI COLOCAR.
				  cotaRepository.alterar(cota);
			  }
		  }
	  }

	}

}
