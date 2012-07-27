package br.com.abril.nds.repository.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.CotaEmissaoDTO;
import br.com.abril.nds.dto.ProdutoEmissaoDTO;
import br.com.abril.nds.dto.filtro.FiltroEmissaoCE;
import br.com.abril.nds.dto.filtro.FiltroEmissaoCE.ColunaOrdenacao;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.planejamento.ChamadaEncalhe;
import br.com.abril.nds.model.planejamento.TipoChamadaEncalhe;
import br.com.abril.nds.repository.ChamadaEncalheRepository;

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
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<CotaEmissaoDTO> obterDadosEmissaoChamadasEncalhe(
			FiltroEmissaoCE filtro) { 		

		HashMap<String, Object> param = new HashMap<String, Object>();
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select cota.numeroCota as numCota, ");
		hql.append(" 		cota.id as idCota, ");
		hql.append(" 		pessoa.nome as nomeCota, ");
		hql.append("		sum(chamEncCota.qtdePrevista) as qtdeExemplares, ");	
		hql.append("		sum(chamEncCota.qtdePrevista * produtoEdicao.precoVenda) as vlrTotalCe ");
		
				
		gerarFromWhere(filtro, hql, param);
		

		hql.append(" group by cota ");
		
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

		hql.append(" from ChamadaEncalheCota chamEncCota, Roteirizacao roterizacao ")
		   .append(" join chamEncCota.chamadaEncalhe  chamadaEncalhe ")
		   .append(" left join chamEncCota.conferenciasEncalhe confEnc ")
		   .append(" left join confEnc.movimentoEstoqueCota  movimentoCota ")
		   .append(" join chamEncCota.cota cota ")
		   .append(" join cota.pessoa pessoa ")
		   .append(" join chamadaEncalhe.produtoEdicao produtoEdicao ")
		   .append(" join produtoEdicao.produto produto ")
		   .append(" join produto.fornecedores fornecedores ")
		   
		   .append(" join roterizacao.rota rota ")
		   .append(" join roterizacao.pdv pdv ")
		   .append(" join pdv.cota cotaPdv ")
		   .append(" join rota.roteiro roteiro ")
		   .append(" join roteiro.box box ")
		   .append(" where cotaPdv.id=cota.id ");
		
		
		
		if(filtro.getDtRecolhimentoDe() != null) {
			
			hql.append(" and chamadaEncalhe.dataRecolhimento >=:dataDe ");
			param.put("dataDe", filtro.getDtRecolhimentoDe());
		}
		
		if(filtro.getDtRecolhimentoAte() != null) {
			hql.append(" and chamadaEncalhe.dataRecolhimento <=:dataAte ");
			param.put("dataAte", filtro.getDtRecolhimentoAte());
		}
		
		if(filtro.getNumCotaDe() != null) {

			hql.append(" and cota.numeroCota >=:cotaDe ");
			param.put("cotaDe", filtro.getNumCotaDe());
		}
		
		if(filtro.getNumCotaAte() != null) {
			
			hql.append(" and cota.numeroCota <=:cotaAte ");
			param.put("cotaAte", filtro.getNumCotaAte());
		}
		
		if(filtro.getIdRoteiro() != null) {
			
			hql.append(" and roteiro.id <=:idRoteiro ");
			param.put("idRoteiro", filtro.getIdRoteiro());
		}
				
		if(filtro.getIdRota() != null) {
			
			hql.append(" and rota.id <=:idRota ");
			param.put("idRota", filtro.getIdRota());
		}
		
		if(filtro.getIdBoxDe() != null) {
			
			hql.append(" and box.codigo >=:codBox ");
			param.put("codBox", filtro.getIdBoxDe());
		}
		
		if(filtro.getIdBoxAte() != null) {
			
			hql.append(" and box.codigo <=:codBox");
			param.put("codBox", filtro.getIdBoxAte());
		}
		
		if(filtro.getFornecedores() != null && !filtro.getFornecedores().isEmpty()) {
			
			hql.append(" and fornecedores.id in (:listaFornecedores) ");
			param.put("listaFornecedores", filtro.getFornecedores());
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

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<CotaEmissaoDTO> obterDadosEmissaoImpressaoChamadasEncalhe(
			FiltroEmissaoCE filtro) { 		

		HashMap<String, Object> param = new HashMap<String, Object>();
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select chamEncCota.id as idChamEncCota, ");
		hql.append(" 		cota.numeroCota as numCota, ");
		hql.append(" 		chamadaEncalhe.dataRecolhimento as dataRecolhimento, ");
		hql.append(" 		cota.id as idCota, ");
		hql.append(" 		pessoa.nome as nomeCota, ");
		hql.append("		sum(chamEncCota.qtdePrevista) as qtdeExemplares, ");	
		hql.append("		sum(chamEncCota.qtdePrevista * produtoEdicao.precoVenda) as vlrTotalCe, ");
		hql.append(" 		box.codigo as box, ");
		hql.append(" 		rota.codigoRota as codigoRota, ");
		hql.append(" 		rota.descricaoRota as nomeRota ");
		
		gerarFromWhere(filtro, hql, param);
		

		hql.append(" group by cota ");
		
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

	@SuppressWarnings("unchecked")
	@Override
	public List<ProdutoEmissaoDTO> obterProdutosEmissaoCE(
			FiltroEmissaoCE filtro, Long idCota) {

		
		HashMap<String, Object> param = new HashMap<String, Object>();
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select produtoEdicao.codigoDeBarras as codigoBarras, ");
		hql.append(" 		lancamentos.sequenciaMatriz as sequencia, ");
		hql.append(" 	    produto.codigo as codigoProduto, ");
		hql.append(" 	    produto.nome as nomeProduto, ");
		hql.append(" 	    produtoEdicao.id as idProdutoEdicao, ");
		hql.append(" 	    produtoEdicao.numeroEdicao as edicao, ");
		hql.append(" 	    produtoEdicao.desconto as desconto, ");
		hql.append(" 	    produtoEdicao.precoVenda as precoVenda, ");
		hql.append(" 	    produtoEdicao.parcial as tipoRecolhimento, ");
		hql.append(" 	    lancamentos.dataLancamentoDistribuidor as dataLancamento, ");
		hql.append(" 	    (produtoEdicao.precoVenda - produtoEdicao.desconto) as precoComDesconto, ");
		hql.append(" 	    lancamentos.reparte as reparte, ");
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
}
