package br.com.abril.nds.repository.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.CotaQueNaoRecebeClassificacaoDTO;
import br.com.abril.nds.dto.CotaQueRecebeClassificacaoDTO;
import br.com.abril.nds.dto.ProdutoRecebidoDTO;
import br.com.abril.nds.dto.filtro.FiltroClassificacaoNaoRecebidaDTO;
import br.com.abril.nds.dto.filtro.FiltroDTO;
import br.com.abril.nds.model.distribuicao.ClassificacaoNaoRecebida;
import br.com.abril.nds.repository.ClassificacaoNaoRecebidaRepository;
import br.com.abril.nds.vo.PaginacaoVO;

@Repository
public class ClassificacaoNaoRecebidarRepositoryImpl extends AbstractRepositoryModel<ClassificacaoNaoRecebida, Long> implements ClassificacaoNaoRecebidaRepository {

	public ClassificacaoNaoRecebidarRepositoryImpl() {
		super(ClassificacaoNaoRecebida.class);
	}

	@Override
	public List<CotaQueRecebeClassificacaoDTO> obterCotasQueRecebemClassificacao(FiltroClassificacaoNaoRecebidaDTO filtro) {
		Map<String, Object> parameters = new HashMap<String, Object>();

		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT ");
		hql.append(" classificacaoNaoRecebida.id as idClassificacaoNaoRecebida, "); // ID ClassificacaoNaoRecebida
		hql.append(" cota.numeroCota as numeroCota, "); // NUMERO DA COTA
		hql.append(" coalesce(pessoa.nomeFantasia, pessoa.razaoSocial, pessoa.nome,'') as nomePessoa "); // NOME DA COTA
		
		hql.append(" FROM ClassificacaoNaoRecebida as classificacaoNaoRecebida ");
		hql.append(" INNER JOIN classificacaoNaoRecebida.tipoClassificacaoProduto as tipoClassificacaoProduto ");
		hql.append(" INNER JOIN classificacaoNaoRecebida.cota as cota ");
		hql.append(" INNER JOIN cota.pessoa as pessoa ");
		
		// O filtro sempre terá OU nomeCota OU codigoCota
		hql.append(" WHERE ");
		
		if (filtro.getIdTipoClassificacaoProduto() != null && !filtro.getIdTipoClassificacaoProduto().equals(0)) {
			hql.append(" tipoClassificacaoProduto.id = :tipoClassificacaoProduto ");
			parameters.put("tipoClassificacaoProduto", filtro.getIdTipoClassificacaoProduto());
		}
		
		Query query = getSession().createQuery(hql.toString());
		
		setParameters(query, parameters);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(CotaQueRecebeClassificacaoDTO.class));
		
		configurarPaginacao(filtro, query);
		
		return query.list();
	}
	
	@Override
	public List<CotaQueNaoRecebeClassificacaoDTO> obterCotasQueNaoRecebemClassificacao(FiltroClassificacaoNaoRecebidaDTO filtro) {
		
		return null;
	}
	
	private void configurarPaginacao(FiltroDTO filtro, Query query) {
		
		PaginacaoVO paginacao = filtro.getPaginacao();

		if (paginacao == null) 
			return;
		
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
	
	private void setParameters(Query query, Map<String, Object> parameters) {
		for (String key : parameters.keySet()) {
			query.setParameter(key, parameters.get(key));
		}
	}

}
