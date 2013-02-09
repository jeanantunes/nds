package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.AjusteReparteDTO;
import br.com.abril.nds.dto.filtro.FiltroCotasRegiaoDTO;
import br.com.abril.nds.model.distribuicao.AjusteReparte;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.AjusteReparteRepository;
import br.com.abril.nds.vo.PaginacaoVO;

@Repository
public class AjusteReparteRepositoryImpl extends AbstractRepositoryModel<AjusteReparte, Long> implements AjusteReparteRepository {
	
	public AjusteReparteRepositoryImpl( ) {
		super(AjusteReparte.class);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<AjusteReparteDTO> buscarTodasCotas(AjusteReparteDTO dto) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT ");
		hql.append(" cota.numeroCota as numeroCota, ");
		hql.append(" cota.situacaoCadastro as status, ");
		hql.append(" coalesce(pessoa.nomeFantasia, pessoa.razaoSocial, pessoa.nome, '') as nomeCota, ");
		hql.append(" ajuste.formaAjuste as formaAjuste, ");
		hql.append(" ajuste.ajusteAplicado as ajusteAplicado, ");
		hql.append(" ajuste.dataInicio as dataInicio, ");
		hql.append(" ajuste.dataFim as dataFim, ");
		hql.append(" ajuste.motivo as motivoAjuste, ");
		hql.append(" usuario.nome as nomeUsuario, ");
		hql.append(" ajuste.dataAlteracao as dataAlteracao ");
		
		hql.append(" FROM AjusteReparte AS ajuste ");
		hql.append(" LEFT JOIN ajuste.cota as cota ");
		hql.append(" LEFT JOIN cota.pessoa as pessoa ");
		hql.append(" LEFT JOIN ajuste.usuario  as usuario ");
		

		Query query = super.getSession().createQuery(hql.toString());
		
		query.setResultTransformer(new AliasToBeanResultTransformer(
				AjusteReparteDTO.class));
		
		configurarPaginacao(dto, query);
		
		return query.list();
		
	}
	
	private void configurarPaginacao(AjusteReparteDTO dto, Query query) {

		PaginacaoVO paginacao = dto.getPaginacao();

		if (paginacao.getQtdResultadosTotal().equals(0)) {
			paginacao.setQtdResultadosTotal(query.list().size());
		}

		if(paginacao.getQtdResultadosPorPagina() != null) {
			query.setMaxResults(paginacao.getQtdResultadosPorPagina());
		}

		if (paginacao.getPosicaoInicial() != null) {
			query.setFirstResult(paginacao.getPosicaoInicial());
		}
	}
	
//	private void configurarPaginacao(AjusteReparteDTO ajuste, Query query) {
//
//		PaginacaoVO paginacao = ajuste.getPaginacao();
//
//		if (paginacao.getQtdResultadosTotal().equals(0)) {
//			paginacao.setQtdResultadosTotal(query.list().size());
//		}
//
//		if (paginacao.getQtdResultadosPorPagina() != null) {
//			query.setMaxResults(paginacao.getQtdResultadosPorPagina());
//		}
//
//		if (paginacao.getPosicaoInicial() != null) {
//			query.setFirstResult(paginacao.getPosicaoInicial());
//		}
//	}
	
}
