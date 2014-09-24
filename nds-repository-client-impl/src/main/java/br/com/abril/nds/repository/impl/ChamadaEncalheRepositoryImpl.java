package br.com.abril.nds.repository.impl;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.BandeirasDTO;
import br.com.abril.nds.dto.CapaDTO;
import br.com.abril.nds.dto.CotaEmissaoDTO;
import br.com.abril.nds.dto.CotaProdutoEmissaoCEDTO;
import br.com.abril.nds.dto.FornecedorDTO;
import br.com.abril.nds.dto.ProdutoEmissaoDTO;
import br.com.abril.nds.dto.filtro.FiltroEmissaoCE;
import br.com.abril.nds.dto.filtro.FiltroEmissaoCE.ColunaOrdenacao;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.planejamento.ChamadaEncalhe;
import br.com.abril.nds.model.planejamento.Lancamento;
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

	@SuppressWarnings("unchecked")
    public List<Long> obterIdsProdutoEdicaoNaMatrizRecolhimento(
			Date dataEncalhe, 
			List<Long> idsProdutoEdicao) {

		StringBuilder hql = new StringBuilder();

		hql.append(" select chamadaEncalhe.produtoEdicao.id from ChamadaEncalhe chamadaEncalhe 	");
		hql.append(" where chamadaEncalhe.dataRecolhimento = :dataEncalhe 			");
		hql.append(" and chamadaEncalhe.tipoChamadaEncalhe = :tipoChamadaEncalhe 	");
		hql.append(" and chamadaEncalhe.produtoEdicao.id in (:idsProdutoEdicao) 	");
		hql.append(" group by chamadaEncalhe.produtoEdicao.id ");

		Query query = this.getSession().createQuery(hql.toString());

		query.setParameter("tipoChamadaEncalhe", TipoChamadaEncalhe.MATRIZ_RECOLHIMENTO);
		query.setParameterList("idsProdutoEdicao", idsProdutoEdicao);
		query.setParameter("dataEncalhe", dataEncalhe);

		return (List<Long>) query.list();
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
	public List<ChamadaEncalhe> obterChamadasEncalhe(ProdutoEdicao produtoEdicao,
				 									 TipoChamadaEncalhe tipoChamadaEncalhe,
				 									 Boolean fechado) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select chamadaEncalhe ");
		hql.append(" from ChamadaEncalhe chamadaEncalhe ");
		hql.append(" join chamadaEncalhe.chamadaEncalheCotas chamadaEncalheCota ");
		hql.append(" where chamadaEncalhe.produtoEdicao = :produtoEdicao ");
		
		if (tipoChamadaEncalhe != null ) {
			hql.append(" and chamadaEncalhe.tipoChamadaEncalhe = :tipoChamadaEncalhe ");
		}
		
		if (fechado != null ) {
			hql.append(" and chamadaEncalheCota.fechado = :fechado ");
		}
		
		hql.append(" group by chamadaEncalhe.id");
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("produtoEdicao", produtoEdicao);
		
		if (tipoChamadaEncalhe != null) {
			query.setParameter("tipoChamadaEncalhe", tipoChamadaEncalhe);
		}
		
		if (fechado != null ) {
			query.setParameter("fechado", fechado);
		}
		
		return  query.list();
	}
	
	/**
	 * Obtém lista de ChamadaEncalhe de ProdutoEdicao dos lancamentos
	 * 
	 * @param idsLancamento
	 * @param fechado
	 * @return List<ChamadaEncalhe>
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ChamadaEncalhe> obterChamadasEncalheLancamentos(Set<Long> idsLancamento, Boolean fechado) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select chamadaEncalhe ");
		
		hql.append(" from ChamadaEncalhe chamadaEncalhe ");
		
		hql.append(" join chamadaEncalhe.chamadaEncalheCotas chamadaEncalheCota ");
		
		hql.append(" join chamadaEncalhe.produtoEdicao produtoEdicao ");
		
		hql.append(" where produtoEdicao.id IN (select pe.id from Lancamento l join l.produtoEdicao pe where l.id IN (:idsLancamento))");
		
		if (fechado != null ) {
			
			hql.append(" and chamadaEncalheCota.fechado = :fechado ");
		}
		
		hql.append(" group by chamadaEncalhe.id");
		
		Query query = this.getSession().createQuery(hql.toString());
		
		if (fechado != null ) {
			
			query.setParameter("fechado", fechado);
		}
		
		query.setParameterList("idsLancamento", idsLancamento);
		
		return  query.list();
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
		sql.append(" end as nomeCota, ");
		sql.append(" sum(chamadaenc0_.QTDE_PREVISTA) as qtdeExemplares, ");
		sql.append(" sum(chamadaenc0_.QTDE_PREVISTA * produtoedi5_.PRECO_VENDA) as vlrTotalCe ");
		
		gerarFromWhereSQL(filtro, sql, param);
		
		sql.append(" group by cota3_.ID  ");
		
		gerarOrdenacao(filtro, sql);		
		
		RowMapper cotaRowMapper = new RowMapper() {

			public Object mapRow(ResultSet rs, int arg1) throws SQLException {

				CotaEmissaoDTO cota = new CotaEmissaoDTO();
				cota.setNumCota(rs.getInt("numCota"));
				cota.setIdCota(rs.getLong("idCota"));
				cota.setNomeCota(rs.getString("nomeCota"));
				cota.setQtdeExemplares(BigInteger.valueOf(rs.getInt("qtdeExemplares")));
				cota.setVlrTotalCe(rs.getBigDecimal("vlrTotalCe"));
				return cota;
			}
		};

		List<CotaEmissaoDTO> lista = (List<CotaEmissaoDTO>) new JdbcTemplate(dataSource).query(sql.toString(), param.toArray(), cotaRowMapper);
		
		if(lista == null || lista.size() ==0)
			return null;
		
		/** FIXME
		 * for(CotaEmissaoDTO dto : lista){
		 *	setQtdExamplaresVlrTotalCe(filtro, dto, false);
		 * }
		 */
		
		
		
		return lista;
	}

	private void gerarFromWhereSQL(FiltroEmissaoCE filtro, StringBuilder sql, ArrayList<Object> param) {

		sql.append(" from ");
		sql.append(" CHAMADA_ENCALHE_COTA chamadaenc0_ ");
		sql.append(" inner join ");
		sql.append(" CHAMADA_ENCALHE chamadaenc2_ ");
		sql.append(" on chamadaenc0_.CHAMADA_ENCALHE_ID=chamadaenc2_.ID ");
		sql.append(" join PRODUTO_EDICAO produtoedi5_ on (chamadaenc2_.PRODUTO_EDICAO_ID=produtoedi5_.ID) ");
				
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
		sql.append(" where 1=1 ");
		sql.append(" and roteiro13_.tipo_roteiro <> 'ESPECIAL' "); 
        
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
			sql.append(" and roteiro13_.ID = ? ");
			param.add(filtro.getIdRoteiro());
		}
				
		if(filtro.getIdRota() != null) {
			sql.append(" and rota12_.ID = ? ");
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
		
		carregarParamFornecedores(filtro, sql, param);
		
		
	}

	/**
	 * Carrega os parâmetros fornecedores. 
	 * 
	 * @param filtro
	 * @param sql
	 * @param param
	 */
	private void carregarParamFornecedores(
			FiltroEmissaoCE filtro,
			StringBuilder sql, 
			ArrayList<Object> param) {
		
		if(filtro.getFornecedores() != null && !filtro.getFornecedores().isEmpty()) {
			
			sql.append(" and fornecedor8_.ID in ");

			sql.append(" ( ");

			int counter = 0;
			
			for(Long idFornecedor: filtro.getFornecedores()) {
				sql.append( (++counter == filtro.getFornecedores().size()) ?  " ? " : " ?, ");
				param.add(idFornecedor);
			}
			
			sql.append(" ) ");
			
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
		sql.append(" from CHAMADA_ENCALHE_COTA chamadaenc14_ ");
		sql.append(" inner join CHAMADA_ENCALHE chamadaenc15_ on chamadaenc14_.CHAMADA_ENCALHE_ID=chamadaenc15_.ID ");
		sql.append(" inner join PRODUTO_EDICAO produtoedi17_ on chamadaenc15_.PRODUTO_EDICAO_ID=produtoedi17_.ID ");
		
		if (filtro.getFornecedores() != null && !filtro.getFornecedores().isEmpty()){
		    
		    sql.append(" inner join PRODUTO prod on produtoedi17_.PRODUTO_ID = prod.ID ");
		    sql.append(" inner join PRODUTO_FORNECEDOR prod_fornec on prod.ID = prod_fornec.PRODUTO_ID ");
		}
		
		sql.append(" inner join COTA cota16_ on chamadaenc14_.COTA_ID=cota16_.ID ");
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
		
		if (filtro.getFornecedores() != null && !filtro.getFornecedores().isEmpty()){
		    
		    sql.append(" and prod_fornec.fornecedores_ID in ( ");
		    
		    for (int index = 0 ; index < filtro.getFornecedores().size() ; index++){
		        
		        sql.append((index > 0) ? ", ?" : " ? ");
		        param.add(filtro.getFornecedores().get(index));
		    }
		    
		    sql.append(")");
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

		hql.append(" from ChamadaEncalheCota chamEncCota ")
		   .append(" join chamEncCota.chamadaEncalhe  chamadaEncalhe ")
		   .append(" join chamEncCota.cota cota ")
		   .append(" join cota.pessoa pessoa ")
		   .append(" join chamadaEncalhe.produtoEdicao produtoEdicao ")
		   .append(" join produtoEdicao.produto produto ")
		   .append(" join produto.fornecedores fornecedores ")
		   .append(" join cota.pdvs pdv ")
		   .append(" join pdv.rotas rotaPdv ")
		   .append(" join rotaPdv.rota rota ")
		   .append(" join rota.roteiro roteiro ")
		   .append(" join roteiro.roteirizacao roteirizacao ")
		   .append(" join roteirizacao.box box ")
		   .append(" where cota.box.id = box.id ");
		
		if(filtro.getCotasOperacaoDiferenciada()!=null) {
			hql.append(" and cota.numeroCota in (:cotasOperacaoDiferenciada) ");
			param.put("cotasOperacaoDiferenciada", filtro.getCotasOperacaoDiferenciada());
		}
		
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
			hql.append(" and roteiro.id = :idRoteiro ");
			param.put("idRoteiro", filtro.getIdRoteiro());
		}
				
		if(filtro.getIdRota() != null) {
			hql.append(" and rota.id = :idRota ");
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
	public List<CotaEmissaoDTO> obterDadosEmissaoImpressaoChamadasEncalhe(FiltroEmissaoCE filtro) { 		

		HashMap<String, Object> param = new HashMap<String, Object>();
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select  						");
		
		hql.append("chamEncCota.id as idChamEncCota,	");
		hql.append("(select count(c.id) from GrupoCota gc join gc.cotas c where c.id=cota.id) as qtdGrupoCota, ");
		hql.append(" cota.numeroCota as numCota, 							");
		hql.append(" chamadaEncalhe.dataRecolhimento as dataRecolhimento, 	");
		hql.append(" cota.id as idCota, 										");
		hql.append(" case pessoa.class ");
		hql.append("       when 'F' then pessoa.nome ");
		hql.append("       when 'J' then pessoa.razaoSocial end  as nomeCota,");
		hql.append(" chamEncCota.qtdePrevista as qtdeExemplares, ");
		hql.append(" sum(chamEncCota.qtdePrevista * produtoEdicao.precoVenda) as vlrTotalCe, ");	
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
		
		setParameters(query, param);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(CotaEmissaoDTO.class));
		
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
		
		setParameters(query, param);
		
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

		hql.append("and _cota.id = cota.id ");
		
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
	public Map<Long, List<ProdutoEmissaoDTO>> obterProdutosEmissaoCE(FiltroEmissaoCE filtro) {
		
		HashMap<String, Object> param = new HashMap<String, Object>();
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select cota.id as idCota,                              ");
		hql.append("        produtoEdicao.codigoDeBarras as codigoBarras, 	");
		hql.append(" 	    produto.codigo as codigoProduto, 				");
		hql.append(" 	    produto.nome as nomeProduto, 					");
		hql.append(" 	    produtoEdicao.id as idProdutoEdicao, 			");
		hql.append(" 	    produtoEdicao.numeroEdicao as edicao, 			");

		hql.append(" 	    (movimentoCota.valoresAplicados.valorDesconto) as desconto, 	");
		hql.append("		produtoEdicao.precoVenda as precoVenda,    		");
		hql.append(" 	    periodoLancParcial.tipo as tipoRecolhimento, 		");
		hql.append(" 	    lancamentos.dataLancamentoDistribuidor as dataLancamento, ");
		hql.append("        chamadaEncalhe.dataRecolhimento as dataRecolhimento,           ");
		hql.append("        chamadaEncalhe.dataRecolhimento as dataRecolhimento,           ");
		hql.append("    	coalesce( movimentoCota.valoresAplicados.precoComDesconto, movimentoCota.valoresAplicados.precoVenda, 0 ) as precoComDesconto, ");	
		
		// case when count(conferenci23_.ID)>0 then 1 else 0 end as  col_15_0_,
		
		hql.append(" ( ");
		hql.append(" sum(movimentoCota.qtde) ");
		//hql.append(obterSubHqlQtdeReparte(filtro));
		hql.append(" ) as reparte,	");

		hql.append(" coalesce(count(conferenciaEncalhe.id), 0)  as conversaoQtdeDevolvida, ");
		//hql.append(hqlQtdeEncalhe.toString()).append(" as quantidadeDevolvida, ");
		
		
		hql.append(" case when count(conferenciaEncalhe.id) > 0 then true else false end as confereciaRealizada,");
		//hql.append(hqlConferenciaRealizada.toString()).append(" as confereciaRealizada, ");
				
		hql.append("		chamadaEncalhe.sequencia as sequencia, ");
		
		hql.append(" min(notaEnvio.numero) as numeroNotaEnvio ");
		
		gerarFromWhereProdutosCE(filtro, hql, param);
		
		hql.append(" group by chamadaEncalhe, cota.id ");
		
		hql.append(" order by chamadaEncalhe.dataRecolhimento, sequencia ");
		
		Query query =  getSession().createQuery(hql.toString());
		
		setParameters(query, param);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ProdutoEmissaoDTO.class));
		
		List<ProdutoEmissaoDTO> listaProdutoEmissaoCota = query.list();
		
		Map<Long, List<ProdutoEmissaoDTO>> mapProdutosEmissaoCota = new HashMap<>(); 
		
		for (ProdutoEmissaoDTO produtoEmissaoDTO : listaProdutoEmissaoCota) {
		    
		    List<ProdutoEmissaoDTO> listaProdutoEmissao =
	            mapProdutosEmissaoCota.get(produtoEmissaoDTO.getIdCota());
		    
		    if (listaProdutoEmissao == null) {
		        
		        listaProdutoEmissao = new ArrayList<ProdutoEmissaoDTO>();
		    }
		    
		    listaProdutoEmissao.add(produtoEmissaoDTO);
		    
		    mapProdutosEmissaoCota.put(produtoEmissaoDTO.getIdCota(), listaProdutoEmissao);
		}
		
		return mapProdutosEmissaoCota;
	}

	private void gerarFromWhereProdutosCE(FiltroEmissaoCE filtro, StringBuilder hql, HashMap<String, Object> param) {

		//TODO Ajuste alterações PARCIAIS
		
		hql.append(" from ChamadaEncalheCota chamEncCota 					                         ")
		   .append(" join chamEncCota.chamadaEncalhe chamadaEncalhe 		                         ")
		   .append(" left join chamEncCota.conferenciasEncalhe conferenciaEncalhe 			         ")
		   .append(" left join conferenciaEncalhe.controleConferenciaEncalheCota controleConfEncalhe ")
		   .append(" join chamEncCota.cota cota                                                      ")
		   .append(" join chamadaEncalhe.produtoEdicao produtoEdicao 		                         ")
		   .append(" join produtoEdicao.produto produto 					                         ")
		   .append(" join produto.fornecedores fornecedores 				                         ")
		   .append(" INNER join chamadaEncalhe.lancamentos lancamentos 			                     ")
		   .append(" left join lancamentos.periodoLancamentoParcial periodoLancParcial 	             ")
		   .append(" left join lancamentos.estudo estudo                                             ")
           
		   .append(" left join estudo.estudoCotas estudoCotas                                        ")
           .append(" left join estudoCotas.itemNotaEnvios itensNotaEnvio                             ")
           .append(" left join itensNotaEnvio.itemNotaEnvioPK.notaEnvio notaEnvio                    ")
		   
		   .append(" INNER JOIN lancamentos.movimentoEstoqueCotas  movimentoCota 	                 ")
		   .append(" INNER JOIN movimentoCota.tipoMovimento tipoMovimento                            ")
		   .append(" where (movimentoCota.id is null or movimentoCota.cota = cota)	                 ")
		   .append(" and (estudoCotas.id is null or estudoCotas.cota = cota)                         ")
		   .append(" and chamEncCota.postergado = :isPostergado                                      ");
		   // .append(" and chamEncCota.qtdePrevista>0  ")
	
		param.put("isPostergado", false);
		
		if(filtro.getDtRecolhimentoDe() != null) {
			
			hql.append(" and chamadaEncalhe.dataRecolhimento >=:dataDe ");
			param.put("dataDe", filtro.getDtRecolhimentoDe());
		}
		
		if(filtro.getDtRecolhimentoAte() != null) {
			hql.append(" and chamadaEncalhe.dataRecolhimento <=:dataAte ");
			param.put("dataAte", filtro.getDtRecolhimentoAte());
		}
		
		if (filtro.getFornecedores() != null && !filtro.getFornecedores().isEmpty()){
		    
		    hql.append(" and fornecedores.id in (:fornec) ");
		    param.put("fornec", filtro.getFornecedores());
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
			Intervalo<Date> intervalo, TipoChamadaEncalhe tipoChamadaEncalhe, Long fornecedor, PaginacaoVO paginacaoVO) {
	
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
			.append(" and chamadaEncalhe.dataRecolhimento <= :dataAte ");
		
		if (fornecedor != null){
			
			hql.append(" and fornecedores.id = :fornecedor ");
		}
		
		if(tipoChamadaEncalhe != null){
		    hql.append(" and chamadaEncalhe.tipoChamadaEncalhe = :tipoChamadaEncalhe");
		}
		
		hql.append(" group by chamadaEncalhe.id ");
		
		if (paginacaoVO != null)		
			hql.append(getOrderByobterBandeirasNoIntervalo(paginacaoVO)); 
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("dataDe", intervalo.getDe());
		query.setParameter("dataAte", intervalo.getAte());
		
		if (fornecedor != null){
			
			query.setParameter("fornecedor", fornecedor);
		}
		if(tipoChamadaEncalhe != null){
		    query.setParameter("tipoChamadaEncalhe", tipoChamadaEncalhe);
		}
		
		if (paginacaoVO != null && paginacaoVO.getPosicaoInicial() != null) { 
			
			query.setFirstResult(paginacaoVO.getPosicaoInicial());
			
			query.setMaxResults(paginacaoVO.getQtdResultadosPorPagina());
		}
		
		query.setResultTransformer(Transformers.aliasToBean(BandeirasDTO.class));
		
		return query.list();
	}
	
	@Override
    public Long countObterBandeirasNoIntervalo(Intervalo<Date> intervalo) {
        return countObterBandeirasNoIntervalo(intervalo, null, null);
    }

    @Override
	public Long countObterBandeirasNoIntervalo(Intervalo<Date> intervalo, TipoChamadaEncalhe tipoChamadaEncalhe, Long fornecedor) {
	
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
		if (fornecedor != null){
            
            hql.append(" and fornecedores.id = :fornecedor ");
        }
			
		if(tipoChamadaEncalhe != null){
            hql.append(" and chamadaEncalhe.tipoChamadaEncalhe = :tipoChamadaEncalhe");
        }
        
		Query query = this.getSession().createQuery(hql.toString());
		
		
		query.setParameter("dataDe", intervalo.getDe());
		query.setParameter("dataAte", intervalo.getAte());
		
		if (fornecedor != null){
            
            query.setParameter("fornecedor", fornecedor);
        }
		if(tipoChamadaEncalhe != null){
            query.setParameter("tipoChamadaEncalhe", tipoChamadaEncalhe);
        }
        
				
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
	public List<FornecedorDTO> obterDadosFornecedoresParaImpressaoBandeira(
			Intervalo<Date> intervalo, Long fornecedor) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select pessoaFornecedor.razaoSocial as razaoSocial, ")
			.append(" fornecedores.codigoInterface as codigoInterface, ")
			.append(" fornecedores.canalDistribuicao as canalDistribuicao, ")
			.append(" d.enderecoDistribuidor.endereco.cidade as praca ")
			
			.append(" from ChamadaEncalhe chamadaEncalhe, Distribuidor d ")
			.append(" join chamadaEncalhe.produtoEdicao produtoEdicao ")
			.append(" join produtoEdicao.produto produto ")
			.append(" join produto.fornecedores fornecedores ")
			.append(" join fornecedores.juridica pessoaFornecedor ")
			.append(" where chamadaEncalhe.dataRecolhimento >= :dataDe ")
			.append(" and chamadaEncalhe.dataRecolhimento <= :dataAte ");
		
		
		if (fornecedor != null){
            
            hql.append(" and fornecedores.id = :fornecedor ");
        }
		hql.append(" group by fornecedores.id ");
					
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("dataDe", intervalo.getDe());
		query.setParameter("dataAte", intervalo.getAte());
		
		if (fornecedor != null){
		    query.setParameter("fornecedor",fornecedor);
		}
		
		query.setResultTransformer(Transformers.aliasToBean(FornecedorDTO.class));
		
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

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override		
	public Set<Lancamento> obterLancamentos(Long idChamadaEncalhe) {

		String hql = " select lancamentos from ChamadaEncalhe chamadaEncalhe "
				+ " join chamadaEncalhe.lancamentos lancamentos"
				+ " where chamadaEncalhe.id= :idChamadaEncalhe ";
				
		Query query = super.getSession().createQuery(hql);
				
		query.setParameter("idChamadaEncalhe", idChamadaEncalhe);
		
		return new HashSet(query.list());
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CotaProdutoEmissaoCEDTO> obterDecomposicaoReparteSuplementarRedistribuicao(FiltroEmissaoCE filtro) {
		
		StringBuilder sql = new StringBuilder("")
		
			.append(" select c.numero_cota as numeroCota ")
			.append(" 	, c.id as idCota ")
			.append(" 	, mec.PRODUTO_EDICAO_ID as idProdutoEdicao ")
			.append(" 	, mec.DATA as dataMovimento")
			.append(" 	, nei.nota_envio_id as numeroNotaEnvio")
			.append(" 	, mec.QTDE as reparte ")
			.append(" from chamada_encalhe ce ")   
		    .append(" inner join chamada_encalhe_cota cec on cec.CHAMADA_ENCALHE_ID = ce.ID ")   
		    .append(" inner join cota c on c.id = cec.COTA_ID   ")
			.append(" inner join chamada_encalhe_lancamento cel on ce.id = cel.chamada_encalhe_id ")   
			.append(" inner join lancamento l on l.id = cel.lancamento_id    ")
			.append(" inner join produto_edicao pe on pe.id = l.PRODUTO_EDICAO_ID ")   
			.append(" inner join movimento_estoque_cota mec on mec.PRODUTO_EDICAO_ID = ce.PRODUTO_EDICAO_ID and mec.COTA_ID = cec.COTA_ID and mec.LANCAMENTO_ID = l.ID")   
			.append(" inner join tipo_movimento tm on tm.id = mec.TIPO_MOVIMENTO_ID   ")
			.append(" inner join estudo e on e.PRODUTO_EDICAO_ID = mec.PRODUTO_EDICAO_ID ")   
			.append(" inner join estudo_cota ec on ec.ESTUDO_ID = e.id and ec.COTA_ID = mec.COTA_ID ")   
			.append(" inner join nota_envio_item nei on nei.PRODUTO_EDICAO_ID = e.PRODUTO_EDICAO_ID and nei.ESTUDO_COTA_ID = ec.id ")   
			.append(" 	inner join ( ")
			.append(" 	select c.numero_cota, c.id, mec.PRODUTO_EDICAO_ID, min(data_aprovacao) as data_aprovacao ")
            .append(" 	from chamada_encalhe ce    ")
    		.append(" 	inner join chamada_encalhe_cota cec  on cec.CHAMADA_ENCALHE_ID = ce.ID  ")   
    		.append(" 	inner join cota c  on c.id = cec.COTA_ID      ")
    		.append(" 	left join chamada_encalhe_lancamento cel on ce.id = cel.chamada_encalhe_id ")     
    		.append(" 	inner join lancamento l on l.id = cel.lancamento_id      ")
    		.append(" 	inner join produto_edicao pe on pe.id = l.PRODUTO_EDICAO_ID ")     
    		.append(" 	INNER JOIN movimento_estoque_cota mec ON mec.PRODUTO_EDICAO_ID = ce.PRODUTO_EDICAO_ID and mec.COTA_ID = cec.COTA_ID ")   
			.append(" 	inner join tipo_movimento tm on tm.id = mec.TIPO_MOVIMENTO_ID ")
			.append(" 	where 1 = 1 ");
		
		if(filtro != null && filtro.getDtRecolhimentoDe() != null && filtro.getDtRecolhimentoAte() != null) {
			sql.append("and ce.DATA_RECOLHIMENTO between :recolhimentoDe and :recolhimentoAte ");
		}
		
		sql.append(" 	and mec.TIPO_MOVIMENTO_ID in (select id from tipo_movimento where GRUPO_MOVIMENTO_ESTOQUE in (:movimentoRecebimentoReparte, :movimentoCompraSuplementar)) ")
		   .append("   and mec.MOVIMENTO_ESTOQUE_COTA_FURO_ID IS NULL ")
		   .append(" 	group by mec.PRODUTO_EDICAO_ID, mec.COTA_ID ")
		   .append(" 	having count(0) > 1 ")
		   .append(" ) rs_sup on rs_sup.id = mec.cota_id and rs_sup.PRODUTO_EDICAO_ID = mec.PRODUTO_EDICAO_ID ") // and mec.DATA_APROVACAO <> rs_sup.DATA_APROVACAO ")
		   .append(" where 1 = 1 ");
		
		if(filtro != null && filtro.getDtRecolhimentoDe() != null && filtro.getDtRecolhimentoAte() != null) {
			sql.append("and ce.DATA_RECOLHIMENTO between :recolhimentoDe and :recolhimentoAte ");
		}
			
		 sql.append(" and mec.TIPO_MOVIMENTO_ID in (select id from tipo_movimento where GRUPO_MOVIMENTO_ESTOQUE in (:movimentoRecebimentoReparte, :movimentoCompraSuplementar)) ");
		 sql.append(" and mec.MOVIMENTO_ESTOQUE_COTA_FURO_ID IS NULL ");
		 sql.append(" group by c.numero_cota, idCota, idProdutoEdicao ")
		 .append(" 	having count(0) > 1 ")
		 
			.append(" union ")
			.append(" select c.numero_cota as numeroCota ")
			.append(" 	, c.id as idCota ")
			.append(" 	, mec.PRODUTO_EDICAO_ID as idProdutoEdicao ")
			.append(" 	, mec.DATA as dataMovimento ")
			.append(" 	, null as numeroNotaEnvio ")
			.append(" 	, sum(mec.QTDE) as reparte ")
			.append(" from chamada_encalhe ce   ")
		    .append(" inner join chamada_encalhe_cota cec on cec.CHAMADA_ENCALHE_ID = ce.ID ")  
			.append(" inner join chamada_encalhe_lancamento cel on ce.id = cel.chamada_encalhe_id   ")
			.append(" inner join lancamento l on l.id = cel.lancamento_id     ")
			.append(" inner join produto_edicao pe on pe.id = l.PRODUTO_EDICAO_ID ")   
			.append(" inner join cota c on c.id = cec.COTA_ID    ")
			.append(" inner join movimento_estoque_cota mec on mec.PRODUTO_EDICAO_ID = ce.PRODUTO_EDICAO_ID  and mec.COTA_ID = cec.COTA_ID ")  
			.append(" inner join tipo_movimento tm on tm.id = mec.TIPO_MOVIMENTO_ID   ")
			.append(" where 1 = 1 ");
		
		if(filtro != null && filtro.getDtRecolhimentoDe() != null && filtro.getDtRecolhimentoAte() != null) {
			sql.append("and ce.DATA_RECOLHIMENTO between :recolhimentoDe and :recolhimentoAte ");
		}
		
		sql	.append(" and mec.TIPO_MOVIMENTO_ID in (select id from tipo_movimento where GRUPO_MOVIMENTO_ESTOQUE in (:movimentoCompraSuplementar)) ")
			.append(" group by numeroCota, idCota, idProdutoEdicao, dataMovimento ")
		 
			// redistribuicao
			.append(" union ")
			.append(" SELECT c.numero_cota as numeroCota,                                                                                          ")
			.append(" c.id as idCota,                                                                                                              ")
			.append(" mec.PRODUTO_EDICAO_ID as idProdutoEdicao,                                                                                    ")
			.append(" mec.data as dataMovimento,                                                                                                   ")
			.append(" null as numeroNotaEnvio,                                                                                                     ")
			.append(" mec.QTDE as reparte                                                                                                          ")
			.append(" FROM chamada_encalhe ce                                                                                                      ")
			.append(" INNER JOIN chamada_encalhe_cota cec on cec.CHAMADA_ENCALHE_ID = ce.ID                                                        ")                      
			.append(" INNER join chamada_encalhe_lancamento cel on ce.id = cel.chamada_encalhe_id                                                  ")
			.append(" INNER JOIN cota c  on c.id = cec.COTA_ID                                                                                     ")                                      
			.append(" INNER JOIN produto_edicao pe  on pe.ID = ce.PRODUTO_EDICAO_ID                                                                ")                                 
			.append(" INNER JOIN lancamento l on l.PRODUTO_EDICAO_ID = pe.ID                                                                       ")  
			.append(" INNER join lancamento_parcial lp  on lp.produto_edicao_id = pe.ID                                                            ")
			.append(" INNER JOIN periodo_lancamento_parcial plr  on l.PERIODO_LANCAMENTO_PARCIAL_ID = plr.ID                                       ")                                                         
			.append(" INNER JOIN movimento_estoque_cota mec  ON mec.PRODUTO_EDICAO_ID = ce.PRODUTO_EDICAO_ID  and mec.COTA_ID = cec.COTA_ID        ")            
			.append(" and mec.LANCAMENTO_ID = l.ID and mec.LANCAMENTO_ID = cel.LANCAMENTO_ID                                                       ")
			.append(" inner join tipo_movimento tm  on tm.id = mec.TIPO_MOVIMENTO_ID                                                               ")
			.append(" where 1 = 1                                                                                                                  ")
			.append(" and l.NUMERO_LANCAMENTO > 1                                                                                                  ")
			.append(" and ce.DATA_RECOLHIMENTO between :recolhimentoDe and :recolhimentoAte                                                        ")
			
			// passar por parametro
			.append(" and l.status not in ('RECOLHIDO', 'FECHADO', 'EM_RECOLHIMENTO')                                                              ")
			.append(" and pe.parcial = true                                                                                                        ")
			.append(" group by numeroCota, idCota, idProdutoEdicao                                                                                 ")   
			/// .append(" having count(0) > 1                                                                                                      ")
			.append(" order by dataMovimento ");
		
		SQLQuery query = super.getSession().createSQLQuery(sql.toString());
		
		//query.setParameter("idChamadaEncalhe", idChamadaEncalhe);
		
		query.addScalar("numeroCota", StandardBasicTypes.LONG);
		query.addScalar("idCota", StandardBasicTypes.LONG);
		query.addScalar("idProdutoEdicao", StandardBasicTypes.LONG);
		query.addScalar("numeroNotaEnvio", StandardBasicTypes.LONG);
		query.addScalar("dataMovimento", StandardBasicTypes.DATE);
		query.addScalar("reparte", StandardBasicTypes.BIG_INTEGER);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(CotaProdutoEmissaoCEDTO.class));
		
		query.setParameter("movimentoRecebimentoReparte", GrupoMovimentoEstoque.RECEBIMENTO_REPARTE.name());
		query.setParameter("movimentoCompraSuplementar", GrupoMovimentoEstoque.COMPRA_SUPLEMENTAR.name());
		if(filtro != null && filtro.getDtRecolhimentoDe() != null && filtro.getDtRecolhimentoAte() != null) {
			query.setParameter("recolhimentoDe", filtro.getDtRecolhimentoDe());
			query.setParameter("recolhimentoAte", filtro.getDtRecolhimentoAte());
		}
		
		return new ArrayList<CotaProdutoEmissaoCEDTO>(query.list());
	}
	

	@Override
    public Date obterMaxDataRecolhimento(final TipoChamadaEncalhe tipoChamadaEncalhe){
	    final Criteria criteria = getSession().createCriteria(ChamadaEncalhe.class);
	    
	    criteria.add(Restrictions.eq("tipoChamadaEncalhe", tipoChamadaEncalhe));
	    criteria.setProjection(Projections.max("dataRecolhimento"));
	    
	    return (Date) criteria.uniqueResult();
	}

	/**
	 * Remove chamadas de Encalhe por lista de ID da chamada de encalhe
	 * 
	 * @param ids
	 */
	@Override
	public void removerChamadaEncalhePorIds(List<Long> ids) {
		
		
		String sqlCel  = "DELETE FROM CHAMADA_ENCALHE_LANCAMENTO WHERE CHAMADA_ENCALHE_ID in (:ids)" ;
			
		Query querySqlCel = getSession().createSQLQuery(sqlCel);
			
		querySqlCel.setParameterList("ids", ids);	
			
		querySqlCel.executeUpdate();
		
		
        String sqlCe  = "DELETE FROM CHAMADA_ENCALHE WHERE id in (:ids)" ;
		
		Query querySqlCe = getSession().createSQLQuery(sqlCe);
		
		querySqlCe.setParameterList("ids", ids);	
		
		querySqlCe.executeUpdate();
		
		
		getSession().flush();
		
	}
}
