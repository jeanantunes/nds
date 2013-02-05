package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.RegiaoCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroCotasRegiaoDTO;
import br.com.abril.nds.model.distribuicao.RegistroCotaRegiao;
import br.com.abril.nds.repository.RegistroCotaRegiaoRepository;
import br.com.abril.nds.vo.PaginacaoVO;

@Repository
public class RegistroCotaRegiaoRepositoryImpl extends AbstractRepositoryModel<RegistroCotaRegiao, Long> implements RegistroCotaRegiaoRepository {

	public RegistroCotaRegiaoRepositoryImpl() {
		super(RegistroCotaRegiao.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RegiaoCotaDTO> carregarCotasRegiao(FiltroCotasRegiaoDTO filtro) {
		StringBuilder hql = new StringBuilder();
		
		hql.append("SELECT ");
		hql.append(" registroCotaRegiao.id as registroCotaRegiaoId, ");
		hql.append(" cota.numeroCota as numeroCota, ");
		hql.append(" coalesce(pessoa.nomeFantasia, pessoa.razaoSocial, pessoa.nome, '') as nomeCota,");
		hql.append(" tipoPontoPDV.descricao as tipoPDV, ");
		hql.append(" cota.situacaoCadastro as tipoStatus, ");
		hql.append(" endereco.bairro as bairro, ");
		hql.append(" endereco.cidade as cidade, ");
		hql.append(" usuario.nome as nomeUsuario, ");
		hql.append(" registroCotaRegiao.dataAlteracao as data, ");
		hql.append(" sum((estoqueProdutoCota.qtdeRecebida - estoqueProdutoCota.qtdeDevolvida) * produtoEdicao.precoVenda) as faturamento "); // FATURAMENTO

		hql.append(" FROM RegistroCotaRegiao AS registroCotaRegiao, EstoqueProdutoCota as estoqueProdutoCota ");
		hql.append(" LEFT JOIN registroCotaRegiao.cota as cota ");
		hql.append(" LEFT JOIN cota.enderecos as enderecoCota ");
		hql.append(" LEFT JOIN enderecoCota.endereco as endereco ");
		hql.append(" LEFT JOIN cota.pessoa as pessoa ");
		hql.append(" LEFT JOIN cota.pdvs as pdv ");
		hql.append(" LEFT JOIN pdv.segmentacao as segmentacao ");
		hql.append(" LEFT JOIN segmentacao.tipoPontoPDV as tipoPontoPDV ");
		hql.append(" LEFT JOIN registroCotaRegiao.usuario  as usuario ");
		hql.append(" LEFT JOIN registroCotaRegiao.regiao  as regiao ");
		hql.append(" LEFT JOIN estoqueProdutoCota.produtoEdicao as produtoEdicao ");

		hql.append(" WHERE registroCotaRegiao.cota.id = estoqueProdutoCota.cota.id AND ");
		hql.append(" registroCotaRegiao.regiao.id = :ID_REGIAO ");
		hql.append(" group by cota.id ");
		
		Query query = super.getSession().createQuery(hql.toString());
		
		query.setParameter("ID_REGIAO", filtro.getId());
		
		query.setResultTransformer(new AliasToBeanResultTransformer(
				RegiaoCotaDTO.class));
		
		configurarPaginacao(filtro, query);
		
		return query.list();
		
	}
	
	
		@SuppressWarnings("unchecked")
		@Override
		public List<RegiaoCotaDTO> buscarPorCEP(FiltroCotasRegiaoDTO filtro) {
			StringBuilder hql = new StringBuilder();
			
			hql.append("SELECT");
			hql.append(" cota.numeroCota as numeroCota, ");
			hql.append(" coalesce(pessoa.nomeFantasia, pessoa.razaoSocial, pessoa.nome, '') as nomeCota, ");
			hql.append(" cota.situacaoCadastro as tipoStatus ");
			
			hql.append(" FROM Cota AS cota ");
			
			hql.append(" JOIN cota.pdvs as pdv ");
			hql.append(" JOIN pdv.enderecos as enderecosPdv "); 
			hql.append(" JOIN enderecosPdv.endereco as enderecoPdv ");
			hql.append(" JOIN pdv.caracteristicas as caracteristicasPDV ");
			hql.append(" JOIN cota.pessoa as pessoa ");
			
			hql.append(" WHERE enderecoPdv.cep between :CEP_INICIAL and :CEP_FINAL ");
			hql.append(" and caracteristicasPDV.pontoPrincipal = true ");
			hql.append(" and cota.id not in (select reg.cota.id from RegistroCotaRegiao reg where reg.regiao.id = :ID_REGIAO) ");
			hql.append(" GROUP BY cota.numeroCota");
			
			
			Query query = super.getSession().createQuery(hql.toString());
			query.setParameter("CEP_INICIAL", filtro.getCepInicial()); 
			query.setParameter("CEP_FINAL", filtro.getCepFinal());
			query.setParameter("ID_REGIAO", filtro.getId());
			
			query.setResultTransformer(new AliasToBeanResultTransformer(RegiaoCotaDTO.class));
			
			configurarPaginacao(filtro, query);
			
			return query.list();
		}
		
		
		private void configurarPaginacao(FiltroCotasRegiaoDTO filtro, Query query) {

			PaginacaoVO paginacao = filtro.getPaginacao();

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
