package br.com.abril.nds.repository.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.RegiaoCotaDTO;
import br.com.abril.nds.dto.RegiaoNMaiores_CotaDTO;
import br.com.abril.nds.dto.RegiaoNMaiores_ProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroCotasRegiaoDTO;
import br.com.abril.nds.dto.filtro.FiltroDTO;
import br.com.abril.nds.dto.filtro.FiltroRegiaoNMaioresProdDTO;
import br.com.abril.nds.model.distribuicao.RegistroCotaRegiao;
import br.com.abril.nds.repository.AbstractRepositoryModel;
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
		hql.append(" order by cota.numeroCota ");
		
		Query query = super.getSession().createQuery(hql.toString());
		
		query.setParameter("ID_REGIAO", filtro.getId());
		
		query.setResultTransformer(new AliasToBeanResultTransformer(RegiaoCotaDTO.class));
		
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
		
		
		private void configurarPaginacao(FiltroDTO filtro, Query query) {

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

		@SuppressWarnings("unchecked")
		@Override
		public List<Integer> buscarNumeroCotasPorIdRegiao(Long idRegiao) {
				
				StringBuilder hql = new StringBuilder();
				
				hql.append(" SELECT ");
				hql.append(" cota.numeroCota ");
				hql.append(" FROM RegistroCotaRegiao AS registroCotaRegiao ");
				hql.append(" LEFT JOIN registroCotaRegiao.cota as cota ");
				hql.append(" LEFT JOIN registroCotaRegiao.regiao as regiao ");
				hql.append(" WHERE regiao.id = :id_regiao ");

				Query query = super.getSession().createQuery(hql.toString());
				
				query.setParameter("id_regiao", idRegiao);
				
				return (List<Integer>)query.list();
			}

		@SuppressWarnings("unchecked")
		@Override
		public List<RegiaoNMaiores_ProdutoDTO> buscarProdutos(FiltroRegiaoNMaioresProdDTO filtro) {

				StringBuilder hql = new StringBuilder();
				
				hql.append(" SELECT ");
				
				hql.append(" prodEdicao.numeroEdicao AS numeroEdicao, ");
				hql.append(" produto.codigo AS codProduto, ");
				hql.append(" produto.nome AS nomeProduto, ");
				hql.append(" produto.tipoClassificacaoProduto.descricao AS descricaoClassificacao, ");
				hql.append(" lancam.dataLancamentoPrevista AS dataLcto, ");
				hql.append(" lancam.status AS status ");
				
				hql.append(" FROM Lancamento AS lancam ");
				
				hql.append(" left join lancam.produtoEdicao AS prodEdicao ");
				hql.append(" left join prodEdicao.produto AS produto ");
				
				hql.append(" WHERE produto.codigo = :COD_PRODUTO ");
				hql.append(" AND produto.nome = :NOME_PRODUTO ");
				hql.append(this.getSqlWhereBuscarProdutos(filtro));
				
				hql.append(" ORDER BY numeroEdicao desc ");
				
				Query query = super.getSession().createQuery(hql.toString());
				
				query.setParameter("COD_PRODUTO", filtro.getCodigoProduto());
				query.setParameter("NOME_PRODUTO", filtro.getNome());
				this.paramsDinamicosBuscarProdutos(query, filtro);
				
				query.setResultTransformer(new AliasToBeanResultTransformer(RegiaoNMaiores_ProdutoDTO.class));
				
				if (filtro != null){
					configurarPaginacao(filtro, query);
				}
				
				return query.list();
			}
			
		private String paramsDinamicosBuscarProdutos (Query query, FiltroRegiaoNMaioresProdDTO filtro) {
			
			StringBuilder hql = new StringBuilder();
			
			if(filtro.getIdTipoClassificacaoProduto() !=null && filtro.getIdTipoClassificacaoProduto() > 0){
				query.setParameter("ID_CLASSIFICACAO", filtro.getIdTipoClassificacaoProduto());
			}
			
			return hql.toString();
		}
		
		private String getSqlWhereBuscarProdutos (FiltroRegiaoNMaioresProdDTO filtro) {
			
			StringBuilder hql = new StringBuilder();
			
			if(filtro.getIdTipoClassificacaoProduto() !=null && filtro.getIdTipoClassificacaoProduto() > 0){
				hql.append(" AND produto.tipoClassificacaoProduto.id = :ID_CLASSIFICACAO ");
			}

			return hql.toString();
		}

		@SuppressWarnings("unchecked")
		@Override
		public List<RegiaoNMaiores_CotaDTO> rankingCotas(List<String> idsProdEdicaoParaMontagemRanking, Integer limite) {
			
			StringBuilder sql = new StringBuilder();
			
			sql.append(" select ");
			sql.append(" cota_id as cotaId, numCota as numeroCota, situacao as status, nomeCota as nomePessoa, ");
			sql.append(" sum(venda_edicoes) as vdMedia ");
			
			sql.append(" from ");
			
			sql.append(" ( ");
			
			sql.append(" select ");
			sql.append(" mec.cota_id as cota_id, ");
			sql.append(" cotas.numero_cota as numCota, ");
			sql.append(" cotas.situacao_cadastro as situacao, ");
			sql.append(" coalesce(pessoaJoin.nome_fantasia, pessoaJoin.razao_social, pessoaJoin.nome, '') as nomeCota, ");
			
			sql.append(" case when mec.tipo_movimento_id=21 then (mec.qtde) ");
			sql.append(" 	when mec.tipo_movimento_id=26 then (mec.qtde*-1) ");
			sql.append(" 	else 0 end as venda_edicoes ");
			
			sql.append(" from movimento_estoque_cota mec ");
			sql.append(" join estoque_produto_cota EPC ON EPC.ID = mec.ESTOQUE_PROD_COTA_ID ");
			sql.append(" join produto_edicao PE ON pe.ID = EPC.PRODUTO_EDICAO_ID ");
			sql.append(" join produto P ON p.ID = PE.PRODUTO_ID ");
			sql.append(" join cota cotas ON EPC.COTA_ID = cotas.ID ");
			sql.append(" join pessoa pessoaJoin ON cotas.pessoa_ID = pessoaJoin.ID ");
			
			sql.append(" where tipo_movimento_id in (21,26) ");
			sql.append(" and EPC.PRODUTO_EDICAO_ID in (").append(StringUtils.join(idsProdEdicaoParaMontagemRanking,",")).append(")");
			
			sql.append(" order by mec.cota_id ");
			sql.append(" ) as q1 ");
			
			sql.append(" group by COTA_ID ");
			sql.append(" order by vdMedia DESC ");
			sql.append(" LIMIT :limitePesquisa ");
			
			SQLQuery query = this.getSession().createSQLQuery(sql.toString());
			
			query.setParameter("limitePesquisa", limite);
			
			query.setResultTransformer(new AliasToBeanResultTransformer(RegiaoNMaiores_CotaDTO.class));
			
			return query.list();
			
		}

		@Override
		@SuppressWarnings("unchecked")
		public List<String> idProdEdicaoParaMontagemDoRanking(String codigoProduto, String numeroEdicao) {
			
			StringBuilder sql = new StringBuilder();
			
			sql.append(" select ");
			sql.append(" prodEdic.ID ");

			sql.append(" from ");
			
			sql.append(" produto_edicao prodEdic ");
			
			sql.append(" inner join produto ");
			sql.append(" ON prodEdic.PRODUTO_ID = produto.ID ");
			sql.append(" where prodEdic.NUMERO_EDICAO in (:numEdicao) ");
			sql.append(" and produto.CODIGO in (:codProduto) ");
			
			SQLQuery query = this.getSession().createSQLQuery(sql.toString());
			
			query.setParameter("codProduto", codigoProduto);
			query.setParameter("numEdicao", numeroEdicao);
			
			return (List<String>)query.list();
		}

		@SuppressWarnings("unchecked")
		@Override
		public List<RegiaoNMaiores_CotaDTO> filtroRanking(Integer numCota) {
			
			StringBuilder sql = new StringBuilder();
			
			sql.append(" select ");
			sql.append(" cotas.numero_cota as numeroCota, ");
			sql.append(" coalesce(pessoaJoin.nome_fantasia, pessoaJoin.razao_social, pessoaJoin.nome, '') as nomePessoa, ");
			sql.append(" cotas.situacao_cadastro as status ");

			sql.append(" from ");
			
			sql.append(" cota cotas ");
			
			sql.append(" inner join pessoa pessoaJoin ");
			sql.append(" ON cotas.pessoa_ID = pessoaJoin.ID ");
			
			sql.append(" where cotas.NUMERO_COTA in (:numCota) ");
			
			SQLQuery query = this.getSession().createSQLQuery(sql.toString());
			
			query.setParameter("numCota", numCota);
			
			query.setResultTransformer(new AliasToBeanResultTransformer(RegiaoNMaiores_CotaDTO.class));
			
			return query.list();
		}
}
