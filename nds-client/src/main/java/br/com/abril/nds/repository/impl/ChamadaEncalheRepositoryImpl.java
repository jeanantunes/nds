package br.com.abril.nds.repository.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.Transformers;
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
import br.com.abril.nds.model.planejamento.ChamadaEncalhe;
import br.com.abril.nds.model.planejamento.TipoChamadaEncalhe;
import br.com.abril.nds.repository.ChamadaEncalheRepository;
import br.com.abril.nds.util.Intervalo;
import br.com.abril.nds.vo.PaginacaoVO;

@Repository
public class ChamadaEncalheRepositoryImpl extends AbstractRepositoryModel<ChamadaEncalhe,Long> implements ChamadaEncalheRepository{

	public ChamadaEncalheRepositoryImpl() {
		super(ChamadaEncalhe.class);
	}
	
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
	public List<ChamadaEncalhe> obterChamadasEncalhePor(Date dataOperacao, Long idCota) {
		
		try {
			
			return super.getSession().createCriteria(ChamadaEncalhe.class, "chamadaEncalhe")
					.createAlias("chamadaEncalhe.chamadaEncalheCotas", "chamadaEncalheCotas")
					.setFetchMode("chamadaEncalheCotas", FetchMode.JOIN)
					.setFetchMode("chamadaEncalhe.produtoEdicao", FetchMode.JOIN)
					.setFetchMode("chamadaEncalheCotas.cota", FetchMode.JOIN)
					.add(Restrictions.eq("chamadaEncalhe.dataRecolhimento", dataOperacao))
					.add(Restrictions.eq("chamadaEncalheCotas.cota.id", idCota)).list();
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
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
			
			hql.append(" select sum( _chamEncCota.qtdePrevista * produtoEdicao.precoVenda ) ");
			
		} else {
			
			hql.append(" select sum(_chamEncCota.qtdePrevista) 				");
		}
		
		hql.append(" from ChamadaEncalheCota _chamEncCota 					")
		.append(" join _chamEncCota.chamadaEncalhe  _chamadaEncalhe 	")
		.append(" join _chamEncCota.cota _cota 							")
		.append(" where _cota.id = cota.id ");

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

		HashMap<String, Object> param = new HashMap<String, Object>();
		
		StringBuilder hql = new StringBuilder();
				
		hql.append(" select cota.numeroCota as numCota, ");
		hql.append(" cota.id as idCota, ");
		hql.append(" case pessoa.class ");
		hql.append("      when 'F' then pessoa.nome ");
		hql.append("      when 'J' then pessoa.razaoSocial end  as nomeCota,");		
		hql.append("(").append(getSubHqlTotalQtdeValorPrevistaDaEmissaoCE(filtro, false)).append(" ) as qtdeExemplares, ");	
		hql.append("(").append(getSubHqlTotalQtdeValorPrevistaDaEmissaoCE(filtro, true)).append(" ) as vlrTotalCe ");	
		
		gerarFromWhere(filtro, hql, param);
		
		hql.append(" group by cota   ");
		
		gerarOrdenacao(filtro, hql);		
				
		Query query =  getSession().createQuery(hql.toString());
		
		for(String key : param.keySet()){
			
			if(param.get(key) instanceof List)
				query.setParameterList(key, (List) param.get(key));
			else					
				query.setParameter(key, param.get(key));
			
		}
		
		query.setResultTransformer(new AliasToBeanResultTransformer(
				CotaEmissaoDTO.class));
		
		return query.list();
		
	}
	
	private void gerarFromWhere(FiltroEmissaoCE filtro, StringBuilder hql, HashMap<String, Object> param) {

		hql.append(" from ChamadaEncalheCota chamEncCota ")
		   .append(" join chamEncCota.chamadaEncalhe  chamadaEncalhe ")
		   .append(" join chamEncCota.cota cota ")
		   .append(" join cota.pessoa pessoa ")
		   .append(" join chamadaEncalhe.produtoEdicao produtoEdicao ")
		   .append(" join produtoEdicao.produto produto ")
		   .append(" join produto.fornecedores fornecedores ")
		   .append(" JOIN cota.box box ")
		   .append(" JOIN cota.pdvs pdv ")
		   .append(" JOIN pdv.rotas rotaPdv ")
		   .append(" JOIN rotaPdv.rota rota ")
		   .append(" JOIN rota.roteiro roteiro ");
		   
		boolean contemWhere = false;
		
		
		if(filtro.getDtRecolhimentoDe() != null) {
			
			hql.append(((contemWhere)?" and ":" where ")+" chamadaEncalhe.dataRecolhimento >=:dataDe ");
			param.put("dataDe", filtro.getDtRecolhimentoDe());
			contemWhere = true;
		}
		
		if(filtro.getDtRecolhimentoAte() != null) {
			hql.append(((contemWhere)?" and ":" where ")+" chamadaEncalhe.dataRecolhimento <=:dataAte ");
			param.put("dataAte", filtro.getDtRecolhimentoAte());
			contemWhere = true;
		}
		
		if(filtro.getNumCotaDe() != null) {

			hql.append(((contemWhere)?"and":"where")+" cota.numeroCota >=:cotaDe ");
			param.put("cotaDe", filtro.getNumCotaDe());
			contemWhere = true;
		}
		
		if(filtro.getNumCotaAte() != null) {
			
			hql.append(((contemWhere)?"and":"where")+" cota.numeroCota <=:cotaAte ");
			param.put("cotaAte", filtro.getNumCotaAte());
			contemWhere = true;
		}
		
		if(filtro.getIdRoteiro() != null) {
			
			hql.append(((contemWhere)?"and":"where")+" roteiro.id <=:idRoteiro ");
			param.put("idRoteiro", filtro.getIdRoteiro());
			contemWhere = true;
		}
				
		if(filtro.getIdRota() != null) {
			
			hql.append(((contemWhere)?"and":"where")+" rota.id <=:idRota ");
			param.put("idRota", filtro.getIdRota());
			contemWhere = true;
		}
		
		if(filtro.getIdBoxDe() != null) {
			
			hql.append(((contemWhere)?"and":"where")+" box.codigo >=:codBox ");
			param.put("codBox", filtro.getIdBoxDe());
			contemWhere = true;
		}
		
		if(filtro.getIdBoxAte() != null) {
			
			hql.append(((contemWhere)?"and":"where")+" box.codigo <=:codBox");
			param.put("codBox", filtro.getIdBoxAte());
			contemWhere = true;
		}
		
		if(filtro.getFornecedores() != null && !filtro.getFornecedores().isEmpty()) {
			
			hql.append(((contemWhere)?"and":"where")+" fornecedores.id in (:listaFornecedores) ");
			param.put("listaFornecedores", filtro.getFornecedores());
			contemWhere = true;
		}
		

	}

	private void gerarOrdenacao(FiltroEmissaoCE filtro, StringBuilder hql) {
		
		String sortOrder = filtro.getOrdenacao();
		ColunaOrdenacao coluna = ColunaOrdenacao.getPorDescricao(filtro.getColunaOrdenacao());
		
		String nome = null;
		
		switch(coluna) {
			case COTA:
				nome = " numCota ";
				break;
			case NOME: 
				nome = " nomeCota ";
				break;
			case EXEMPLARES:
				nome = " qtdeExemplares ";
				break;
			case VALOR:
				nome = " vlrTotalCe ";
				break;
			default:
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
		
		hql.append(" select produtoEdicao.id as id ");
		
		hql.append(" from ChamadaEncalheCota chamEncCota ")
		   .append(" join chamEncCota.chamadaEncalhe  chamadaEncalhe ")
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

		
		HashMap<String, Object> param = new HashMap<String, Object>();
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select produtoEdicao.codigoDeBarras as codigoBarras, 	");
		hql.append(" 		lancamentos.sequenciaMatriz as sequencia, 		");
		hql.append(" 	    produto.codigo as codigoProduto, 				");
		hql.append(" 	    produto.nome as nomeProduto, 					");
		hql.append(" 	    produtoEdicao.id as idProdutoEdicao, 			");
		hql.append(" 	    produtoEdicao.numeroEdicao as edicao, 			");
		hql.append(" 	    ("+ this.obterSQLDesconto() +") as desconto, 	");
		hql.append(" 	    produtoEdicao.precoVenda as precoVenda, 		");
		hql.append(" 	    produtoEdicao.parcial as tipoRecolhimento, 		");
		hql.append(" 	    lancamentos.dataLancamentoDistribuidor as dataLancamento, ");
		hql.append(" 	    (produtoEdicao.precoVenda - (produtoEdicao.precoVenda * ("+ this.obterSQLDesconto() +") / 100)) as precoComDesconto, ");
		
		hql.append(" ( ");
		hql.append(obterSubHqlQtdeReparte(filtro));
		hql.append(" ) as reparte,	");
		
		hql.append(" 	    sum(movimentoCota.qtde) as quantidadeDevolvida, ");
		hql.append("		lancamentos.sequenciaMatriz as sequencia ");
		
		gerarFromWhereProdutosCE(filtro, hql, param, idCota);
		

		hql.append(" group by chamadaEncalhe ");
						
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

		hql.append(" from ChamadaEncalheCota chamEncCota ")
		   .append(" join chamEncCota.chamadaEncalhe  chamadaEncalhe ")
		   .append(" left join chamEncCota.conferenciasEncalhe confEnc ")
		   .append(" left join confEnc.movimentoEstoqueCota  movimentoCota ")
		   .append(" join chamEncCota.cota cota ")
		   .append(" join cota.pessoa pessoa ")
		   .append(" join chamadaEncalhe.produtoEdicao produtoEdicao ")
		   .append(" join produtoEdicao.produto produto ")
		   .append(" join produto.fornecedores fornecedores ")
		   .append(" join chamadaEncalhe.lancamentos lancamentos ")
		   .append(" where cota.id=:idCota ");
		
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
	
	private String obterSQLDesconto(){
		
		StringBuilder hql = new StringBuilder("coalesce ((select view.desconto ");
		hql.append(" from ViewDesconto view ")
		   .append(" where view.cotaId = cota.id ")
		   .append(" and view.produtoEdicaoId = produtoEdicao.id ")
		   .append(" and view.fornecedorId = fornecedores.id),0) ");
		
		return hql.toString();
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
			orderBy += " produto.descricao ";
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
}
