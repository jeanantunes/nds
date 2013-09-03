package br.com.abril.nds.repository.impl;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.sql.DataSource;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.BandeirasDTO;
import br.com.abril.nds.dto.CapaDTO;
import br.com.abril.nds.dto.CotaEmissaoDTO;
import br.com.abril.nds.dto.FornecedoresBandeiraDTO;
import br.com.abril.nds.dto.ProdutoEmissaoDTO;
import br.com.abril.nds.dto.filtro.FiltroEmissaoCE;
import br.com.abril.nds.dto.filtro.FiltroEmissaoCE.ColunaOrdenacao;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.planejamento.ChamadaEncalhe;
import br.com.abril.nds.model.planejamento.TipoChamadaEncalhe;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.ChamadaEncalheRepository;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.Intervalo;
import br.com.abril.nds.vo.PaginacaoVO;

@Repository
public class ChamadaEncalheRepositoryImpl extends AbstractRepositoryModel<ChamadaEncalhe,Long> implements ChamadaEncalheRepository{

	public ChamadaEncalheRepositoryImpl() {
		super(ChamadaEncalhe.class);
	}
	
	@Autowired
	private DataSource dataSource;
	
	public ChamadaEncalhe obterPorNumeroEdicaoEDataRecolhimento(ProdutoEdicao produtoEdicao,
																Date dataRecolhimento,
																TipoChamadaEncalhe tipoChamadaEncalhe) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select chamadaEncalhe from ChamadaEncalhe chamadaEncalhe ")
			.append(" where chamadaEncalhe.dataRecolhimento = :dataRecolhimento ")
			.append(" and chamadaEncalhe.tipoChamadaEncalhe = :tipoChamadaEncalhe ")
			.append(" and chamadaEncalhe.produtoEdicao = :produtoEdicao ");
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("tipoChamadaEncalhe", tipoChamadaEncalhe);
		query.setParameter("produtoEdicao", produtoEdicao);
		query.setParameter("dataRecolhimento", dataRecolhimento);
		
		query.setMaxResults(1);
		
		return (ChamadaEncalhe) query.uniqueResult();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ChamadaEncalhe> obterChamadasEncalhePor(Date dataOperacao, Long cotaID) {
		
			StringBuilder hql = new StringBuilder();
			
			hql.append(" select ce from ChamadaEncalhe ce ");
			hql.append(" join ce.chamadaEncalheCotas cec ");
			hql.append(" where cec.cota.id = :cotaID ");
			hql.append(" and ce.dataRecolhimento = :dataRecolhimento ");
			
			Query query = this.getSession().createQuery(hql.toString());
			
			query.setParameter("cotaID", cotaID);
			query.setParameter("dataRecolhimento", dataOperacao);
			
			return query.list();
	}
	
	public ChamadaEncalhe obterPorNumeroEdicaoEMaiorDataRecolhimento(ProdutoEdicao produtoEdicao,
																	 TipoChamadaEncalhe tipoChamadaEncalhe) {

		StringBuilder hql = new StringBuilder();
		
		hql.append(" select chamadaEncalhe from ChamadaEncalhe chamadaEncalhe ")
		.append(" where chamadaEncalhe.dataRecolhimento = (select max(chm.dataRecolhimento) from ChamadaEncalhe chm ) ")
		.append(" and chamadaEncalhe.tipoChamadaEncalhe = :tipoChamadaEncalhe ")
		.append(" and chamadaEncalhe.produtoEdicao = :produtoEdicao ");
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("tipoChamadaEncalhe", tipoChamadaEncalhe);
		query.setParameter("produtoEdicao", produtoEdicao);
		
		query.setMaxResults(1);
		
		return (ChamadaEncalhe) query.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	public List<ChamadaEncalhe> obterChamadaEncalhePorProdutoEdicao(ProdutoEdicao produtoEdicao,
			 												  TipoChamadaEncalhe tipoChamadaEncalhe) {

		StringBuilder hql = new StringBuilder();
		
		hql.append(" select chamadaEncalhe from ChamadaEncalhe chamadaEncalhe ")
		.append(" where  ")
		.append(" chamadaEncalhe.tipoChamadaEncalhe = :tipoChamadaEncalhe ")
		.append(" and chamadaEncalhe.produtoEdicao = :produtoEdicao ");
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("tipoChamadaEncalhe", tipoChamadaEncalhe);
		query.setParameter("produtoEdicao", produtoEdicao);
		
		return  query.list();
	}

	/**
	 * SubHql que obtém a quantidade total prevista da chamada de encalhe ou  
	 * o valor total da chamada de encalhe calculado pela somatória das qntidades 
	 * previstas multiplicada pelo preco de venda do produto edição.
	 * 
	 * @param filtro
	 * @param indValorTotalProdutos
	 * 
	 * @return String
	 */
	private String getSubHqlTotalQtdeValorPrevistaDaEmissaoCE(FiltroEmissaoCE filtro, boolean indValorTotalProdutos) {
		
		StringBuffer hql = new StringBuffer();
		
		if(indValorTotalProdutos) {
			
			hql.append(" select sum( _chamEncCota.qtdePrevista * _produtoEdicao.precoVenda ) ");
			
		} else {
			
			hql.append(" select sum(_chamEncCota.qtdePrevista) 				");
		}
		
		hql.append(" from ChamadaEncalheCota _chamEncCota 					")
		.append(" join _chamEncCota.chamadaEncalhe  _chamadaEncalhe 	")
		.append(" join _chamEncCota.cota _cota 							")
		.append(" join _chamadaEncalhe.produtoEdicao _produtoEdicao")
		.append(" where _cota.id = cota.id ")
		.append(" and _chamEncCota.postergado = :isPostergado ");

		if(filtro.getDtRecolhimentoDe() != null) {
			
			hql.append(" and _chamadaEncalhe.dataRecolhimento >=:dataDe ");
		}
		
		if(filtro.getDtRecolhimentoAte() != null) {
			hql.append(" and _chamadaEncalhe.dataRecolhimento <=:dataAte ");
		}
		
		return hql.toString();
		
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<CotaEmissaoDTO> obterDadosEmissaoChamadasEncalhe(
			FiltroEmissaoCE filtro) { 		

		ArrayList<Object> param = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder();

		sql.append(" select ");
		sql.append(" cota3_.NUMERO_COTA as numCota, ");
		sql.append(" cota3_.ID as idCota, ");
		sql.append(" case pessoa4_.TIPO  ");
		sql.append("      when 'F' then pessoa4_.NOME "); 
		sql.append(" 	  when 'J' then pessoa4_.RAZAO_SOCIAL  ");
		sql.append(" end as nomeCota ");
		
		gerarFromWhereSQL(filtro, sql, param);
		
		sql.append(" group by cota3_.ID  ");
		
		gerarOrdenacao(filtro, sql);		
		
		RowMapper cotaRowMapper = new RowMapper() {

			public Object mapRow(ResultSet rs, int arg1) throws SQLException {

				CotaEmissaoDTO cota = new CotaEmissaoDTO();
				cota.setNumCota(rs.getInt("numCota"));
				cota.setIdCota(rs.getLong("idCota"));
				cota.setNomeCota(rs.getString("nomeCota"));
				
				return cota;
			}
		};

		List<CotaEmissaoDTO> lista = (List<CotaEmissaoDTO>) new JdbcTemplate(dataSource).query(sql.toString(), param.toArray(), 
			cotaRowMapper);
		
		if(lista == null || lista.size() ==0)
			return null;
		
		for(CotaEmissaoDTO dto : lista){
			
			setQtdExamplaresVlrTotalCe(filtro, dto, false);
		}
		
		return lista;
	}

	private void gerarFromWhereSQL(FiltroEmissaoCE filtro, StringBuilder sql, ArrayList<Object> param) {

		sql.append(" from ");
		sql.append(" CHAMADA_ENCALHE_COTA chamadaenc0_ ");
		sql.append(" inner join ");
		sql.append(" CHAMADA_ENCALHE chamadaenc2_ ");
		sql.append(" on chamadaenc0_.CHAMADA_ENCALHE_ID=chamadaenc2_.ID ");
		
		sql.append(" left join CHAMADA_ENCALHE_LANCAMENTO cel on (cel.CHAMADA_ENCALHE_ID=chamadaenc2_.ID) ");
		sql.append(" join LANCAMENTO l on (l.ID=cel.LANCAMENTO_ID) ");
		sql.append(" join PRODUTO_EDICAO produtoedi5_ on (l.PRODUTO_EDICAO_ID=produtoedi5_.ID) ");
				
		sql.append(" inner join ");
		sql.append(" PRODUTO produto6_ ");
		sql.append(" on produtoedi5_.PRODUTO_ID=produto6_.ID ");
		sql.append(" inner join ");
		sql.append(" PRODUTO_FORNECEDOR fornecedor7_ ");
		sql.append(" on produto6_.ID=fornecedor7_.PRODUTO_ID ");
		sql.append(" inner join ");
		sql.append(" FORNECEDOR fornecedor8_ ");
		sql.append(" on fornecedor7_.fornecedores_ID=fornecedor8_.ID ");
		sql.append(" inner join ");
		sql.append(" COTA cota3_ ");
		sql.append(" on chamadaenc0_.COTA_ID=cota3_.ID ");
		sql.append(" inner join ");
		sql.append(" PESSOA pessoa4_ ");
		sql.append(" on cota3_.PESSOA_ID=pessoa4_.ID ");
		sql.append(" inner join ");
		sql.append(" BOX box9_ ");
		sql.append(" on cota3_.BOX_ID=box9_.ID ");
		sql.append(" inner join ");
		sql.append(" PDV pdvs10_ ");
		sql.append(" on cota3_.ID=pdvs10_.COTA_ID ");
		sql.append(" inner join ");
		sql.append(" ROTA_PDV rotas11_ ");
		sql.append(" on pdvs10_.ID=rotas11_.PDV_ID ");
		sql.append(" inner join ");
		sql.append(" ROTA rota12_ ");
		sql.append(" on rotas11_.ROTA_ID=rota12_.ID ");
		sql.append(" inner join ");
		sql.append(" ROTEIRO roteiro13_ ");
		sql.append(" on rota12_.ROTEIRO_ID=roteiro13_.ID ");
		sql.append(" where ");
		sql.append(" cel.CHAMADA_ENCALHE_ID=chamadaenc2_.ID "); 
        
		setParamsFilterSqlPostergado(filtro, sql, param);
	}
	
	private void setParamsFilterSql(FiltroEmissaoCE filtro, StringBuilder sql, ArrayList<Object> param) {
		if(filtro.getDtRecolhimentoDe() != null) {
			sql.append(" and chamadaenc2_.DATA_RECOLHIMENTO >= ? ");
			param.add(filtro.getDtRecolhimentoDe());
		}
		
		if(filtro.getDtRecolhimentoAte() != null) {
			sql.append(" and chamadaenc2_.DATA_RECOLHIMENTO <= ? ");
			param.add(filtro.getDtRecolhimentoAte());
		}
		
		if(filtro.getNumCotaDe() != null) {
			sql.append(" and cota3_.numero_cota >= ? ");
			param.add(filtro.getNumCotaDe());
		}
		
		if(filtro.getNumCotaAte() != null) {
			sql.append(" and cota3_.numero_cota <= ?");
			param.add(filtro.getNumCotaAte());
		}
		
		if(filtro.getIdRoteiro() != null) {
			sql.append(" and roteiro13_.ID <= ? ");
			param.add(filtro.getIdRoteiro());
		}
				
		if(filtro.getIdRota() != null) {
			sql.append(" and rota12_.ID <= ? ");
			param.add(filtro.getIdRota());
		}
		
		if(filtro.getCodigoBoxDe() != null) {
			sql.append(" and box9_.codigo >= ? ");
			param.add(filtro.getCodigoBoxDe());
		}
		
		if(filtro.getCodigoBoxAte() != null) {
			sql.append(" and box9_.codigo <= ? ");
			param.add(filtro.getCodigoBoxAte());
		}
		
		if(filtro.getFornecedores() != null && !filtro.getFornecedores().isEmpty()) {
			sql.append(" and fornecedor8_.ID in (?) ");
			param.add(filtro.getFornecedores());
		}
	}
	
	private void setParamsFilterSqlPostergado(FiltroEmissaoCE filtro, StringBuilder sql, ArrayList<Object> param) {
		setParamsFilterSql(filtro, sql, param);

		sql.append(" and chamadaenc0_.POSTERGADO = ? "); 
		param.add(false);
	}
	
	private void setQtdExamplaresVlrTotalCe(FiltroEmissaoCE filtro, CotaEmissaoDTO dto, boolean postergado) {
		CotaEmissaoDTO ret = getTotalQtdeValorPrevistaDaEmissaoCE(filtro, dto.getIdCota(), postergado);
		if(ret != null){

			if(ret.getQtdeExemplares() != null && ret.getQtdeExemplares() > -1)
				dto.setQtdeExemplares(ret.getQtdeExemplares() == null ? new BigInteger("0") : new BigInteger(ret.getQtdeExemplares().toString()));
			
			if(ret.getVlrTotalCe() != null && !"".equals(ret.getVlrTotalCe()))
				dto.setVlrTotalCe(CurrencyUtil.converterValor(ret.getVlrTotalCe()));
		}
	}

	private CotaEmissaoDTO getTotalQtdeValorPrevistaDaEmissaoCE(FiltroEmissaoCE filtro, Long idCota, boolean postergado) {
		
		StringBuilder sql = new StringBuilder();
		sql.append(" select ");
		sql.append(" sum(chamadaenc14_.QTDE_PREVISTA) as qtdeExemplares, ");
		sql.append(" sum(chamadaenc14_.QTDE_PREVISTA*produtoedi17_.PRECO_VENDA) as vlrTotalCe ");
		sql.append(" from ");
		sql.append(" CHAMADA_ENCALHE_COTA chamadaenc14_ ");
		sql.append(" inner join ");
		sql.append(" CHAMADA_ENCALHE chamadaenc15_ ");
		sql.append(" on chamadaenc14_.CHAMADA_ENCALHE_ID=chamadaenc15_.ID ");
		sql.append(" inner join ");
		sql.append(" PRODUTO_EDICAO produtoedi17_ ");
		sql.append(" on chamadaenc15_.PRODUTO_EDICAO_ID=produtoedi17_.ID ");
		sql.append(" inner join ");
		sql.append(" COTA cota16_ ");
		sql.append(" on chamadaenc14_.COTA_ID=cota16_.ID ");
		sql.append(" where ");
		sql.append(" cota16_.ID= ? ");
		sql.append(" and chamadaenc14_.POSTERGADO= ? ");
		
		ArrayList<Object> param = new ArrayList<Object>();
		
		param.add(idCota);
		param.add(postergado);
		
		if(filtro.getDtRecolhimentoDe() != null) {
			sql.append(" and chamadaenc15_.DATA_RECOLHIMENTO >= ? ");
			param.add(filtro.getDtRecolhimentoDe());
		}
		
		if(filtro.getDtRecolhimentoAte() != null) {
			sql.append(" and chamadaenc15_.DATA_RECOLHIMENTO <= ? ");
			param.add(filtro.getDtRecolhimentoAte());
		}
		
		@SuppressWarnings("rawtypes")
		RowMapper cotaRowMapper = new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				CotaEmissaoDTO cota = new CotaEmissaoDTO();
				cota.setQtdeExemplares(rs.getBigDecimal("qtdeExemplares") == null ? new BigInteger("0") : rs.getBigDecimal("qtdeExemplares").toBigInteger());
				cota.setVlrTotalCe(rs.getBigDecimal("vlrTotalCe"));
				return cota;
			}
		};

		@SuppressWarnings("unchecked")
		List<CotaEmissaoDTO> lista = (List<CotaEmissaoDTO>) new JdbcTemplate(dataSource).query(sql.toString(), param.toArray(), cotaRowMapper);

		if(lista != null && lista.size() > 0)
			return lista.get(0);
		
		return null;
	}

	

	private void gerarFromWhere(FiltroEmissaoCE filtro, StringBuilder hql, HashMap<String, Object> param) {

		hql.append(" from ChamadaEncalheCota chamEncCota, Box box ")
		   .append(" join chamEncCota.chamadaEncalhe  chamadaEncalhe ")
		   .append(" join chamEncCota.cota cota ")
		   .append(" join cota.pessoa pessoa ")
		   .append(" join chamadaEncalhe.produtoEdicao produtoEdicao ")
		   .append(" join produtoEdicao.produto produto ")
		   .append(" join produto.fornecedores fornecedores ")
		   .append(" join cota.box box ")
		   .append(" join cota.pdvs pdv ")
		   .append(" join pdv.rotas rotaPdv ")
		   .append(" join rotaPdv.rota rota ")
		   .append(" join rota.roteiro roteiro ")
		   .append(" where cota.box.id = box.id ");
		
		if(filtro.getDtRecolhimentoDe() != null) {
			hql.append(" and chamadaEncalhe.dataRecolhimento >= :dataDe ");
			param.put("dataDe", filtro.getDtRecolhimentoDe());
		}
		
		if(filtro.getDtRecolhimentoAte() != null) {
			hql.append(" and chamadaEncalhe.dataRecolhimento <= :dataAte ");
			param.put("dataAte", filtro.getDtRecolhimentoAte());
		}
		
		if(filtro.getNumCotaDe() != null) {
			hql.append(" and cota.numeroCota >= :cotaDe ");
			param.put("cotaDe", filtro.getNumCotaDe());
		}
		
		if(filtro.getNumCotaAte() != null) {
			hql.append(" and cota.numeroCota <= :cotaAte ");
			param.put("cotaAte", filtro.getNumCotaAte());
		}
		
		if(filtro.getIdRoteiro() != null) {
			hql.append(" and roteiro.id <= :idRoteiro ");
			param.put("idRoteiro", filtro.getIdRoteiro());
		}
				
		if(filtro.getIdRota() != null) {
			hql.append(" and rota.id <= :idRota ");
			param.put("idRota", filtro.getIdRota());
		}
		
		if(filtro.getCodigoBoxDe() != null) {
			hql.append(" and box.codigo >= :codBoxDe ");
			param.put("codBoxDe", filtro.getCodigoBoxDe());
		}
		
		if(filtro.getCodigoBoxAte() != null) {
			hql.append(" and box.codigo <= :codBoxAte");
			param.put("codBoxAte", filtro.getCodigoBoxAte());
		}
		
		if(filtro.getFornecedores() != null && !filtro.getFornecedores().isEmpty()) {
			hql.append(" and fornecedores.id in (:listaFornecedores) ");
			param.put("listaFornecedores", filtro.getFornecedores());
		}
		
		hql.append(" and chamEncCota.postergado = :isPostergado "); 
		param.put("isPostergado", false);
	}
	
	private void gerarOrdenacao(FiltroEmissaoCE filtro, StringBuilder hql) {
		
		String sortOrder = filtro.getOrdenacao();
		ColunaOrdenacao coluna = ColunaOrdenacao.getPorDescricao(filtro.getColunaOrdenacao());
		
		String nome = null;
		
		switch(coluna) {
			case NOME: 
				nome = " nomeCota ";
				break;
			default:
				nome = " numCota ";
				break;
		}
		
		hql.append( " order by " + nome + sortOrder + " ");
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CotaEmissaoDTO> obterDadosEmissaoImpressaoChamadasEncalhe(
			FiltroEmissaoCE filtro) { 		

		HashMap<String, Object> param = new HashMap<String, Object>();
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select  						");
		
		hql.append("chamEncCota.id as idChamEncCota,	");
		
		hql.append(" cota.numeroCota as numCota, 							");
		hql.append(" chamadaEncalhe.dataRecolhimento as dataRecolhimento, 	");
		hql.append(" cota.id as idCota, 										");
		hql.append(" case pessoa.class ");
		hql.append("       when 'F' then pessoa.nome ");
		hql.append("       when 'J' then pessoa.razaoSocial end  as nomeCota,");
		hql.append("(").append(getSubHqlTotalQtdeValorPrevistaDaEmissaoCE(filtro, false)).append(" ) as qtdeExemplares, ");	
		hql.append("(").append(getSubHqlTotalQtdeValorPrevistaDaEmissaoCE(filtro, true)).append(" ) as vlrTotalCe, ");	
		hql.append(" box.codigo as box, 						");
		hql.append(" box.nome as nomeBox, 						");
		hql.append(" cast (rota.id as string) as codigoRota, ");
		hql.append(" rota.descricaoRota as nomeRota, 		");
		hql.append(" cast (roteiro.id as string) as codigoRoteiro, ");
		hql.append(" roteiro.descricaoRoteiro as nomeRoteiro 	  ");
		
		gerarFromWhere(filtro, hql, param);
		
		hql.append(" group by cota ");
		
		hql.append( " order by box.codigo, roteiro.ordem, rota.ordem, rotaPdv.ordem ");
				
		Query query =  getSession().createQuery(hql.toString());
		
		for(String key : param.keySet()){
			
			if(param.get(key) instanceof List)
				query.setParameterList(key, (List<Fornecedor>) param.get(key));
			else					
				query.setParameter(key, param.get(key));
			
		}
		
		query.setResultTransformer(new AliasToBeanResultTransformer(
				CotaEmissaoDTO.class));
		
		return query.list();
		
	}

	@SuppressWarnings("unchecked")
	public List<CapaDTO> obterIdsCapasChamadaEncalhe(Date dataDe, Date dataAte) {
		
		HashMap<String, Object> param = new HashMap<String, Object>();
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select produtoEdicao.id as id, chamadaEncalhe.sequencia as sequenciaMatriz");
		
		hql.append(" from ChamadaEncalhe chamadaEncalhe ")
		   .append(" join chamadaEncalhe.produtoEdicao produtoEdicao ");		

		
		
		if(dataDe != null) {

			hql.append(param.isEmpty()?" where ":" and ");			
			hql.append(" chamadaEncalhe.dataRecolhimento >=:dataDe ");
			param.put("dataDe", dataDe);
		}
		
		if(dataAte != null) {

			hql.append(param.isEmpty()?" where ":" and ");
			hql.append(" chamadaEncalhe.dataRecolhimento <=:dataAte ");
			param.put("dataAte", dataAte);
		}
				
		hql.append(" order by chamadaEncalhe.sequencia ");
		
		Query query =  getSession().createQuery(hql.toString());
		
		for(String key : param.keySet()){
			query.setParameter(key, param.get(key));			
		}
		
		query.setResultTransformer(new AliasToBeanResultTransformer(
				CapaDTO.class));
		
		return query.list();
	}
	
	private String obterSubHqlQtdeReparte(FiltroEmissaoCE filtro) {
		
	StringBuffer hql = new StringBuffer();
		
		hql.append(" select sum(_chamEncCota.qtdePrevista) 				")		
		.append(" from ChamadaEncalheCota _chamEncCota 					")
		.append(" join _chamEncCota.chamadaEncalhe  _chamadaEncalhe 	")
		.append(" join _chamEncCota.cota _cota 							")
		.append(" where _chamadaEncalhe.id = chamadaEncalhe.id ");

		hql.append("and _cota.id =:idCota ");
		
		if(filtro.getDtRecolhimentoDe() != null) {
			
			hql.append(" and _chamadaEncalhe.dataRecolhimento >=:dataDe ");
		}
		
		if(filtro.getDtRecolhimentoAte() != null) {
			hql.append(" and _chamadaEncalhe.dataRecolhimento <=:dataAte ");
		}
		
		return hql.toString();		
		
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ProdutoEmissaoDTO> obterProdutosEmissaoCE(
			FiltroEmissaoCE filtro, Long idCota) {

		
		StringBuffer hqlQtdeEncalhe = new StringBuffer();
		hqlQtdeEncalhe.append(" ( select sum(conf.qtde) 				");
		hqlQtdeEncalhe.append(" from ConferenciaEncalhe conf 			");
		hqlQtdeEncalhe.append("	inner join conf.chamadaEncalheCota cec  ");
		hqlQtdeEncalhe.append("	where cec.id = chamEncCota.id )			");
		
		HashMap<String, Object> param = new HashMap<String, Object>();
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select produtoEdicao.codigoDeBarras as codigoBarras, 	");
		hql.append(" 	    produto.codigo as codigoProduto, 				");
		hql.append(" 	    produto.nome as nomeProduto, 					");
		hql.append(" 	    produtoEdicao.id as idProdutoEdicao, 			");
		hql.append(" 	    produtoEdicao.numeroEdicao as edicao, 			");
		hql.append(" 	    coalesce(movimentoCota.valoresAplicados.valorDesconto, 0) as desconto, ");
		hql.append("		coalesce(movimentoCota.valoresAplicados.precoVenda, produtoEdicao.precoVenda, 0)  as precoVenda,    		");
		hql.append(" 	    produtoEdicao.parcial as tipoRecolhimento, 		");
		hql.append(" 	    lancamentos.dataLancamentoDistribuidor as dataLancamento, ");
		hql.append("    	coalesce( movimentoCota.valoresAplicados.precoComDesconto, movimentoCota.valoresAplicados.precoVenda, 0 ) as precoComDesconto, ");
		
		hql.append(" ( ");
		hql.append(obterSubHqlQtdeReparte(filtro));
		hql.append(" ) as reparte,	");
		
		hql.append(hqlQtdeEncalhe.toString()).append(" as quantidadeDevolvida, ");
		hql.append("		chamadaEncalhe.sequencia as sequencia ");
				
		gerarFromWhereProdutosCE(filtro, hql, param, idCota);
		
		hql.append(" group by chamadaEncalhe ");
		
		hql.append(" order by chamadaEncalhe.dataRecolhimento, sequencia ");
		
		Query query =  getSession().createQuery(hql.toString());
		
		for(String key : param.keySet()){
			query.setParameter(key, param.get(key));			
		}
		
		query.setResultTransformer(new AliasToBeanResultTransformer(
				ProdutoEmissaoDTO.class));
		
		return query.list();
	}

	private void gerarFromWhereProdutosCE(FiltroEmissaoCE filtro, StringBuilder hql, HashMap<String, Object> param, 
			Long idCota) {

		hql.append(" from ChamadaEncalheCota chamEncCota 					")
		   .append(" join chamEncCota.chamadaEncalhe  chamadaEncalhe 		")
		   .append(" join chamEncCota.cota cota 							")
		   .append(" join cota.pessoa pessoa 								")
		   .append(" join chamadaEncalhe.produtoEdicao produtoEdicao 		")
		   .append(" join produtoEdicao.produto produto 					")
		   .append(" join produto.fornecedores fornecedores 				")
		   .append(" left join chamadaEncalhe.lancamentos lancamentos 			")
		   .append(" left join lancamentos.movimentoEstoqueCotas  movimentoCota 	")
		   .append(" left join movimentoCota.tipoMovimento tipoMovimento         ")
		   .append(" left join lancamentos.periodoLancamentoParcial  periodoLancamentoParcial ")
		   .append(" left join movimentoCota.cota cotaMov ")
		   .append(" where cota.id=:idCota 									")
		   .append(" and produtoEdicao.id = produtoEdicao.id  	")
		   .append(" and chamEncCota.qtdePrevista>0  ");
		
		param.put("idCota", idCota);
		
		if(filtro.getDtRecolhimentoDe() != null) {
			
			hql.append(" and chamadaEncalhe.dataRecolhimento >=:dataDe ");
			param.put("dataDe", filtro.getDtRecolhimentoDe());
		}
		
		if(filtro.getDtRecolhimentoAte() != null) {
			hql.append(" and chamadaEncalhe.dataRecolhimento <=:dataAte ");
			param.put("dataAte", filtro.getDtRecolhimentoAte());
		}
	}

	@Override
	public Date obterProximaDataEncalhe(Date base) {
		StringBuilder sb = new StringBuilder();
		sb.append("select o.dataRecolhimento from ChamadaEncalhe o where o.dataRecolhimento > :dataBase order by o.dataRecolhimento");
		
		Query query = getSession().createQuery(sb.toString());
		query.setParameter("dataBase", base);
		
		@SuppressWarnings("rawtypes")
		List result = query.list();
		
		if(result.size() == 0)
			return null;
		
		return (Date) result.get(0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BandeirasDTO> obterBandeirasNoIntervalo(
			Intervalo<Date> intervalo, PaginacaoVO paginacaoVO) {
	
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select produto.codigo as codProduto, ")
			.append(" produto.nome as nomeProduto, ")
			.append(" produtoEdicao.numeroEdicao as edProduto, ")
			.append(" produtoEdicao.pacotePadrao as pctPadrao, ")
			.append(" sum(chamadaEncalheCotas.qtdePrevista) as qtde, ")
			.append(" pessoaFornecedor.razaoSocial as destino, ")
			.append(" chamadaEncalhe.dataRecolhimento as data ")
			.append(" from ChamadaEncalhe chamadaEncalhe ")
			.append(" join chamadaEncalhe.produtoEdicao produtoEdicao ")
			.append(" join produtoEdicao.produto produto ")
			.append(" left join chamadaEncalhe.chamadaEncalheCotas chamadaEncalheCotas ")
			.append(" join produto.fornecedores fornecedores ")
			.append(" join fornecedores.juridica pessoaFornecedor ")
			.append(" where chamadaEncalhe.dataRecolhimento >= :dataDe ")
			.append(" and chamadaEncalhe.dataRecolhimento <= :dataAte ")
			.append(" group by chamadaEncalhe.id ");
		
		if (paginacaoVO != null)		
			hql.append(getOrderByobterBandeirasNoIntervalo(paginacaoVO)); 
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("dataDe", intervalo.getDe());
		query.setParameter("dataAte", intervalo.getAte());
		
		if (paginacaoVO != null && paginacaoVO.getPosicaoInicial() != null) { 
			
			query.setFirstResult(paginacaoVO.getPosicaoInicial());
			
			query.setMaxResults(paginacaoVO.getQtdResultadosPorPagina());
		}
		
		query.setResultTransformer(Transformers.aliasToBean(BandeirasDTO.class));
		
		return query.list();
	}
	
	@Override
	public Long countObterBandeirasNoIntervalo(Intervalo<Date> intervalo) {
	
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select count(distinct chamadaEncalhe.id) ")
			.append(" from ChamadaEncalhe chamadaEncalhe ")
			.append(" join chamadaEncalhe.produtoEdicao produtoEdicao ")
			.append(" join produtoEdicao.produto produto ")
			.append(" left join chamadaEncalhe.chamadaEncalheCotas chamadaEncalheCotas ")
			.append(" join produto.fornecedores fornecedores ")
			.append(" join fornecedores.juridica pessoaFornecedor ")
			.append(" where chamadaEncalhe.dataRecolhimento >= :dataDe ")
			.append(" and chamadaEncalhe.dataRecolhimento <= :dataAte ");
				
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("dataDe", intervalo.getDe());
		query.setParameter("dataAte", intervalo.getAte());
				
		return (Long) query.uniqueResult();
	}
	
	private String getOrderByobterBandeirasNoIntervalo(PaginacaoVO paginacaoVO) {


		String coluna = paginacaoVO.getSortColumn();
		
		if(coluna == null || coluna.isEmpty())
			return "";
		
		String orderBy = " order by ";
						
		if ("codProduto".equals(coluna))
			orderBy += " produto.codigo ";
		else if("nomeProduto".equals(coluna))
			orderBy += " produto.nome ";
		else if("edProduto".equals(coluna))
			orderBy += " produtoEdicao.numeroEdicao ";
		else if("pctPadrao".equals(coluna))
			orderBy += " produtoEdicao.pacotePadrao ";
		
		orderBy += paginacaoVO.getSortOrder();
		
		return orderBy;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<FornecedoresBandeiraDTO> obterDadosFornecedoresParaImpressaoBandeira(
			Intervalo<Date> intervalo) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select pessoaFornecedor.razaoSocial as nome, ")
			.append(" fornecedores.codigoInterface as codigoInterface ")
			
			.append(" from ChamadaEncalhe chamadaEncalhe ")
			.append(" join chamadaEncalhe.produtoEdicao produtoEdicao ")
			.append(" join produtoEdicao.produto produto ")
			.append(" join produto.fornecedores fornecedores ")
			.append(" join fornecedores.juridica pessoaFornecedor ")
			.append(" where chamadaEncalhe.dataRecolhimento >= :dataDe ")
			.append(" and chamadaEncalhe.dataRecolhimento <= :dataAte ")
			.append(" group by fornecedores.id ");
					
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("dataDe", intervalo.getDe());
		query.setParameter("dataAte", intervalo.getAte());
		
		query.setResultTransformer(Transformers.aliasToBean(FornecedoresBandeiraDTO.class));
		
		return query.list();
	}
	
	@Override
	public Integer obterMaiorSequenciaPorDia(Date dataRecolhimento) {
		
		String hql = " select max(chamadaEncalhe.sequencia) from ChamadaEncalhe chamadaEncalhe "
			+ " where chamadaEncalhe.dataRecolhimento = :dataRecolhimento ";
				
		Query query = super.getSession().createQuery(hql);
		
		query.setParameter("dataRecolhimento", dataRecolhimento);
		
		Integer maiorSequencia = (Integer) query.uniqueResult();
		
		if (maiorSequencia == null) {
			
			maiorSequencia = 0;
		}
		
		return maiorSequencia;
	}
	
}
