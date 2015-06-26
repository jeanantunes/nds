package br.com.abril.nds.service.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.RegistroEdicoesFechadasVO;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.repository.EdicoesFechadasRepository;
import br.com.abril.nds.service.EdicoesFechadasService;
import br.com.abril.nds.service.ExtratoEdicaoService;

/**
 * Classe de implementação de serviços referentes a entidade
 * @author infoA2
 */
@Service
public class EdicoesFechadasServiceImpl implements EdicoesFechadasService {

	@Autowired
	private EdicoesFechadasRepository edicoesFechadasRepository;
	
	@Autowired
	private ExtratoEdicaoService extratoEdicaoService;
	
	/* (non-Javadoc)
	 * @see br.com.abril.nds.service.EdicoesFechadasService#obterResultadoEdicoesFechadas(java.util.Date, java.util.Date, java.lang.String)
	 */
	@Override
	@Transactional(readOnly = true)
	public List<RegistroEdicoesFechadasVO> obterResultadoEdicoesFechadas(Date dataDe, Date dateAte, Long idFornecedor) {	
		
		List<GrupoMovimentoEstoque> movimentosExcluidos = this.extratoEdicaoService.obterGruposMovimentoEstoqueExtratoEdicao();
		
		List<String> nameMovimentos = tratarNameMovimentos(movimentosExcluidos);
		
		return edicoesFechadasRepository.obterResultadoEdicoesFechadas(dataDe, 
				                                                       dateAte, 
				                                                       idFornecedor, 
				                                                       null, 
				                                                       null, 
				                                                       null, 
				                                                       null,
				                                                       nameMovimentos,
				                                                       StatusAprovacao.APROVADO);
		
	}
	

	/* (non-Javadoc)
	 * @see br.com.abril.nds.service.EdicoesFechadasService#obterTotalResultadoEdicoesFechadas(java.util.Date, java.util.Date, java.lang.String)
	 */
	@Override
	@Transactional(readOnly = true)
	public BigInteger obterTotalResultadoEdicoesFechadas(
			Date dataDe, Date dateAte, Long idFornecedor) {
		
		List<GrupoMovimentoEstoque> movimentosExcluidos = this.extratoEdicaoService.obterGruposMovimentoEstoqueExtratoEdicao();
		
		List<String> nameMovimentos = tratarNameMovimentos(movimentosExcluidos);
		
		return edicoesFechadasRepository.obterResultadoTotalEdicoesFechadas(dataDe, 
				                                                            dateAte, 
				                                                            idFornecedor,
				                                                            nameMovimentos,
						                                                    StatusAprovacao.APROVADO);
		
	}


	/**
	 * @param dataDe
	 * @param dateAte
	 * @param idFornecedor
	 * @param sortorder
	 * @param sortname
	 * @param firstResult
	 * @param maxResults
	 * @return
	 * @see br.com.abril.nds.repository.EdicoesFechadasRepository#obterResultadoEdicoesFechadas(java.util.Date, java.util.Date, Long, java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	@Transactional(readOnly = true)
	public List<RegistroEdicoesFechadasVO> obterResultadoEdicoesFechadas(
			Date dataDe, Date dateAte, Long idFornecedor,
			String sortorder, String sortname, Integer firstResult,
			Integer maxResults) {
		
		List<GrupoMovimentoEstoque> movimentosExcluidos = this.extratoEdicaoService.obterGruposMovimentoEstoqueExtratoEdicao();
		
		List<String> nameMovimentos = tratarNameMovimentos(movimentosExcluidos);
		
		return edicoesFechadasRepository.obterResultadoEdicoesFechadas(dataDe,
				                                                       dateAte, 
				                                                       idFornecedor, 
				                                                       sortorder, 
				                                                       sortname, 
				                                                       firstResult,
				                                                       maxResults,
				                                                       nameMovimentos,
					                                                   StatusAprovacao.APROVADO);
	}
	
	private List<String> tratarNameMovimentos (List<GrupoMovimentoEstoque> movimentos){
		
		List<String> nameMovimentos = new ArrayList<>();
		
		for (GrupoMovimentoEstoque movEstoque : movimentos) {
			nameMovimentos.add(movEstoque.name());
		}
		
		return nameMovimentos;
	}

	@Override
	@Transactional(readOnly=true)
	public Long countResultadoEdicoesFechadas(	Date dataDe, Date dateAte, Long idFornecedor) {
		
		List<GrupoMovimentoEstoque> movimentosExcluidos = this.extratoEdicaoService.obterGruposMovimentoEstoqueExtratoEdicao();
		
		List<String> nameMovimentos = tratarNameMovimentos(movimentosExcluidos);
		
		return edicoesFechadasRepository.countResultadoEdicoesFechadas(dataDe,
				                                                       dateAte, 
				                                                       idFornecedor,
				                                                       nameMovimentos,
					                                                   StatusAprovacao.APROVADO);
	}
	
	
}
