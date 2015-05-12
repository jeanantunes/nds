package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.RegiaoCotaDTO;
import br.com.abril.nds.dto.RegiaoNMaiores_CotaDTO;
import br.com.abril.nds.dto.RegiaoNMaiores_ProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroCotasRegiaoDTO;
import br.com.abril.nds.dto.filtro.FiltroDTO;
import br.com.abril.nds.dto.filtro.FiltroRegiaoNMaioresProdDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.distribuicao.Regiao;
import br.com.abril.nds.model.distribuicao.RegistroCotaRegiao;
import br.com.abril.nds.model.estoque.OperacaoEstoque;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.RegistroCotaRegiaoRepository;
import br.com.abril.nds.util.Intervalo;
import br.com.abril.nds.vo.PaginacaoVO;

@Repository
public class RegistroCotaRegiaoRepositoryImpl extends AbstractRepositoryModel<RegistroCotaRegiao, Long> implements RegistroCotaRegiaoRepository {

	@Autowired
	private CotaRepository cotaRepository;
	
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
		hql.append(" cota.id as cotaId, ");
		hql.append(" coalesce(pessoa.nomeFantasia, pessoa.razaoSocial, pessoa.nome, '') as nomeCota,");
		hql.append(" tipoPontoPDV.descricao as tipoPDV, ");
		hql.append(" cota.situacaoCadastro as tipoStatus, ");
		hql.append(" endereco.bairro as bairro, ");
		hql.append(" endereco.cidade as cidade, ");
		hql.append(" usuario.nome as nomeUsuario, ");
		hql.append(" TIME(registroCotaRegiao.dataAlteracao) as hora, ");
		hql.append(" registroCotaRegiao.dataAlteracao as data ");

		hql.append(" FROM RegistroCotaRegiao AS registroCotaRegiao ");
		hql.append(" LEFT JOIN registroCotaRegiao.cota as cota ");
		hql.append(" LEFT JOIN cota.pessoa as pessoa ");
		hql.append(" LEFT JOIN cota.pdvs as pdv ");
		hql.append(" LEFT JOIN pdv.enderecos as endPdv ");
		hql.append(" LEFT JOIN endPdv.endereco as endereco ");
		hql.append(" LEFT JOIN pdv.segmentacao as segmentacao ");
		hql.append(" LEFT JOIN segmentacao.tipoPontoPDV as tipoPontoPDV ");
		hql.append(" LEFT JOIN registroCotaRegiao.usuario  as usuario ");
		hql.append(" LEFT JOIN registroCotaRegiao.regiao  as regiao ");

		hql.append(" WHERE ");
		hql.append(" registroCotaRegiao.regiao.id = :ID_REGIAO ");
		hql.append(" group by cota.id ");
		
		hql.append(" order by ");

		if (filtro.getPaginacao() != null) {
		
        	if((filtro.getPaginacao().getSortColumn().equalsIgnoreCase("FATURAMENTO")) || (filtro.getPaginacao().getSortColumn().equalsIgnoreCase("ACAO"))){
        		hql.append(" cota.numeroCota ");
        	}else{
        		hql.append(filtro.getPaginacao().getSortColumn());
        	}

        	hql.append(" "+filtro.getPaginacao().getSortOrder());
			
		} else {
			hql.append(" cota.numeroCota ");
		}
		
		Query query = super.getSession().createQuery(hql.toString());
		
		query.setParameter("ID_REGIAO", filtro.getId());
		
		query.setResultTransformer(new AliasToBeanResultTransformer(RegiaoCotaDTO.class));
		
		configurarPaginacao(filtro, query);
		return query.list();
		
	}
	
	@Override
	public BigDecimal calcularFaturamentoCota(Long cotaID, Intervalo<Date> intervalo) {
		
	    String sql = "select sum(" +
	    		"case when (tp.OPERACAO_ESTOQUE = :opEstEnt) then mec.qtde else (mec.qtde*-1) end "+
	    		" * pe.PRECO_VENDA  "+
	    		" ) "+
	    		" from movimento_estoque_cota mec " +
                " join tipo_movimento tp on tp.id = mec.tipo_movimento_id " +
                " join cota c on c.id = mec.cota_id " +
                " join produto_edicao pe on pe.id = mec.produto_edicao_id " +
                " join lancamento lanc on lanc.produto_edicao_id = pe.id " +
                " where c.id = :idCota " +
                " and lanc.DATA_REC_DISTRIB between :de and :ate " +
                " and lanc.status in (:statusLancamento) ";
		
		SQLQuery query = this.getSession().createSQLQuery(sql.toString());
		
		query.setParameter("opEstEnt", OperacaoEstoque.ENTRADA.name());
		query.setParameter("idCota", cotaID);
		query.setParameter("de", intervalo.getDe());
		query.setParameter("ate", intervalo.getAte());
		query.setParameterList("statusLancamento", 
                Arrays.asList(
                        StatusLancamento.EM_RECOLHIMENTO.name(), 
                        StatusLancamento.RECOLHIDO.name(), 
                        StatusLancamento.FECHADO.name()));
		
		return (BigDecimal)query.uniqueResult();
		
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
				hql.append(" produto.codigoICD AS codigo_icd, ");
				hql.append(" produto.nome AS nomeProduto, ");
				hql.append(" prodEdicao.tipoClassificacaoProduto.descricao AS descricaoClassificacao, ");
				hql.append(" lancam.dataLancamentoDistribuidor AS dataLcto, ");
				hql.append(" lancam.dataRecolhimentoDistribuidor AS dataRcto, ");
				hql.append(" lancam.status AS status, ");

				hql.append("  round((SELECT sum(estqProdCota.qtdeRecebida) ");
				hql.append("          FROM EstoqueProdutoCota estqProdCota ");
				hql.append("          WHERE estqProdCota.produtoEdicao = prodEdicao.id),0) as reparte, ");
				
				hql.append(" (CASE   ");
				hql.append("  	WHEN lancam.status='FECHADO' OR lancam.status='RECOLHIDO' ");
				hql.append("  THEN ");
				hql.append("  	  round((SELECT sum(estqProdCota.qtdeRecebida - estqProdCota.qtdeDevolvida) ");
				hql.append("          FROM EstoqueProdutoCota estqProdCota ");
				hql.append("          WHERE estqProdCota.produtoEdicao = prodEdicao.id),0) ");
				hql.append("  ELSE ");
				hql.append("      null ");
				hql.append("  END) as venda ");
				
				hql.append(" FROM Lancamento AS lancam ");
				
				hql.append(" left join lancam.produtoEdicao AS prodEdicao ");
				hql.append(" left join prodEdicao.produto AS produto ");
				
				hql.append(" WHERE 1 = 1");
				
				hql.append(" AND produto.codigoICD = :COD_PRODUTO ");
				hql.append(" AND lancam.dataLancamentoDistribuidor <> (select max(l.dataLancamentoDistribuidor) from Lancamento l) ");

				hql.append(this.getSqlWhereBuscarProdutos(filtro));
				
				hql.append(" ORDER BY numeroEdicao desc ");
				
				Query query = super.getSession().createQuery(hql.toString());
				
				query.setParameter("COD_PRODUTO", filtro.getCodigoProduto());

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
				hql.append(" AND prodEdicao.tipoClassificacaoProduto.id = :ID_CLASSIFICACAO ");
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
			sql.append(" epc.cota_id as cota_id, ");
			sql.append(" cotas.numero_cota as numCota, ");
			sql.append(" cotas.situacao_cadastro as situacao, ");
			sql.append(" coalesce(pessoaJoin.nome_fantasia, pessoaJoin.razao_social, pessoaJoin.nome, '') as nomeCota, ");
			
			sql.append(" epc.qtde_recebida - epc.qtde_devolvida as venda_edicoes ");
			
			sql.append(" from estoque_produto_cota EPC ");
			sql.append(" join produto_edicao PE ON pe.ID = EPC.PRODUTO_EDICAO_ID ");
			sql.append(" join produto P ON p.ID = PE.PRODUTO_ID ");
			sql.append(" join cota cotas ON EPC.COTA_ID = cotas.ID ");
			sql.append(" join pessoa pessoaJoin ON cotas.pessoa_ID = pessoaJoin.ID ");
			
			sql.append(" where EPC.PRODUTO_EDICAO_ID in (").append(StringUtils.join(idsProdEdicaoParaMontagemRanking,",")).append(")");
			
			sql.append(" order by venda_edicoes desc ");
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
		
		@Override
		public Long adicionar(RegistroCotaRegiao regiaoCotaRegiao) {
			
			if (regiaoCotaRegiao == null || regiaoCotaRegiao.getCota() == null) {
				
				return null;
			}
			
			Cota cota = cotaRepository.buscarPorId(regiaoCotaRegiao.getCota().getId());
			
			if (cota != null && !isCotaRegiaoCadastrada(regiaoCotaRegiao.getRegiao(), cota)) {
				
				return super.adicionar(regiaoCotaRegiao);
			}
			
			return null;
		}
		
		public boolean isCotaRegiaoCadastrada(Regiao regiao, Cota cota) {
			
			StringBuilder sql = new StringBuilder();
			
			sql.append(" from RegistroCotaRegiao");
			sql.append(" where  regiao.id = :idRegiao");
			sql.append(" and    cota.id   = :idCota");
			
			Query query = getSession().createQuery(sql.toString());
			
			query.setParameter("idRegiao", regiao.getId());
			query.setParameter("idCota",   cota.getId());
			
			return !query.list().isEmpty(); 
		}
		
		
		@SuppressWarnings("unchecked")
		@Override
		public List<RegistroCotaRegiao> obterRegistroCotaReagiaPorRegiao(Regiao regiao) {
			
			StringBuilder sql = new StringBuilder();
			
			sql.append(" from RegistroCotaRegiao");
			sql.append(" where  regiao.id = :idRegiao");
			
			Query query = getSession().createQuery(sql.toString());
			
			query.setParameter("idRegiao", regiao.getId());
			
			return query.list();
		}
		
		@Override
		public void removerRegistroCotaReagiaPorRegiao(Regiao regiao) {
			
			StringBuilder sql = new StringBuilder();
			
			sql.append(" delete from RegistroCotaRegiao");
			sql.append(" where  regiao.id = :idRegiao");
			
			Query query = getSession().createQuery(sql.toString());
			
			query.setParameter("idRegiao", regiao.getId());
			
			query.executeUpdate();
		}

	}
