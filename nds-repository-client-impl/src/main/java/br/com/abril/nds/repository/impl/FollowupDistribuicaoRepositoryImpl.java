package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ConsultaFollowupDistribuicaoDTO;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.FollowupDistribuicaoRepository;
import br.com.abril.nds.vo.PaginacaoVO;

@Repository
public class FollowupDistribuicaoRepositoryImpl extends AbstractRepositoryModel<ConsultaFollowupDistribuicaoDTO,Long> implements FollowupDistribuicaoRepository{

	public FollowupDistribuicaoRepositoryImpl() {
		super(ConsultaFollowupDistribuicaoDTO.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ConsultaFollowupDistribuicaoDTO> obterCotas(ConsultaFollowupDistribuicaoDTO dto) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT ");
		hql.append(" cota.numeroCota as numeroCota, ");
		hql.append(" coalesce(pessoa.nomeFantasia, pessoa.razaoSocial, pessoa.nome, '') as nomeJornaleiro, ");
//		hql.append(" MENSAGEM, ");
		hql.append(" ABS(datediff(ajuste.dataFim, (select dist.dataOperacao from Distribuidor dist))) as qtdDiasRestantes ");

		hql.append(" from AjusteReparte as ajuste ");
		
		hql.append(" JOIN ajuste.cota as cota ");
		hql.append(" LEFT JOIN cota.pessoa as pessoa ");
		
		hql.append(" where datediff(ajuste.dataFim,(select dist.dataOperacao from Distribuidor dist))<=5 ");
		
		hql.append(" order by cota.numeroCota, nomeJornaleiro");
		
		Query query =  getSession().createQuery(hql.toString());		
		
		if(dto != null){
			configurarPaginacao(dto, query);
		}
		query.setResultTransformer(new AliasToBeanResultTransformer(ConsultaFollowupDistribuicaoDTO.class));
		
		return query.list();
		
	}

	private void configurarPaginacao(ConsultaFollowupDistribuicaoDTO dto, Query query) {

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

}
