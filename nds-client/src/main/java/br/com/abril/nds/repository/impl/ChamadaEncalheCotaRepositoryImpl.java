package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ChamadaAntecipadaEncalheDTO;
import br.com.abril.nds.dto.filtro.FiltroChamadaAntecipadaEncalheDTO;
import br.com.abril.nds.model.planejamento.ChamadaEncalheCota;
import br.com.abril.nds.model.planejamento.TipoChamadaEncalhe;
import br.com.abril.nds.repository.ChamadaEncalheCotaRepository;

@Repository
public class ChamadaEncalheCotaRepositoryImpl extends
		AbstractRepositoryModel<ChamadaEncalheCota, Long> implements
		ChamadaEncalheCotaRepository {

	public ChamadaEncalheCotaRepositoryImpl() {
		super(ChamadaEncalheCota.class);
	}

	public BigDecimal obterReparteDaChamaEncalheCota(
			Integer numeroCota, Date dataOperacao, boolean indPesquisaCEFutura,
			boolean conferido, boolean postergado) {

		StringBuilder hql = new StringBuilder();

		hql.append(" select sum ( ");
		hql.append(" chamadaEncalheCota.qtdePrevista * (produtoEdicao.precoVenda - ");
		hql.append("(produtoEdicao.precoVenda * (" + this.getSubQueryDesconto() + " / 100 ))) ");
		hql.append(" ) ");
		
		hql.append(" from ChamadaEncalheCota chamadaEncalheCota ");
		hql.append(" join chamadaEncalheCota.chamadaEncalhe chamadaEncalhe ");
		hql.append(" join chamadaEncalhe.produtoEdicao produtoEdicao ");
		hql.append(" join chamadaEncalheCota.cota cota ");
		hql.append(" join produtoEdicao.produto produto ");
		hql.append(" join produto.fornecedores fornecedor ");
		
		hql.append(" where ");
		hql.append(" cota.numeroCota = :numeroCota ");
		hql.append(" and chamadaEncalheCota.fechado = :conferido ");
		hql.append(" and chamadaEncalheCota.postergado = :postergado ");

		if (indPesquisaCEFutura) {
			hql.append(" and chamadaEncalhe.dataRecolhimento >= :dataOperacao ");
		} else {
			hql.append(" and chamadaEncalhe.dataRecolhimento = :dataOperacao ");
		}

		Query query = this.getSession().createQuery(hql.toString());

		query.setParameter("numeroCota", numeroCota);

		query.setParameter("conferido", conferido);

		query.setParameter("postergado", postergado);

		query.setParameter("dataOperacao", dataOperacao);

		return (BigDecimal) query.uniqueResult();
	}

	public Long obterQtdListaChamaEncalheCota(Integer numeroCota,
			Date dataOperacao, Long idProdutoEdicao,
			boolean indPesquisaCEFutura, boolean conferido, boolean postergado) {

		StringBuilder hql = new StringBuilder();

		hql.append(" select count(chamadaEncalheCota.id) ");

		hql.append(" from ChamadaEncalheCota chamadaEncalheCota ");

		hql.append(" where ");

		hql.append(" chamadaEncalheCota.cota.numeroCota = :numeroCota ");

		hql.append(" and chamadaEncalheCota.fechado = :conferido ");

		hql.append(" and chamadaEncalheCota.postergado = :postergado ");

		if (indPesquisaCEFutura) {
			hql.append(" and chamadaEncalheCota.chamadaEncalhe.dataRecolhimento >= :dataOperacao ");
		} else {
			hql.append(" and chamadaEncalheCota.chamadaEncalhe.dataRecolhimento = :dataOperacao ");
		}

		if (idProdutoEdicao != null) {
			hql.append(" and chamadaEncalheCota.chamadaEncalhe.produtoEdicao.id = :idProdutoEdicao ");
		}

		Query query = this.getSession().createQuery(hql.toString());

		query.setParameter("numeroCota", numeroCota);

		query.setParameter("conferido", conferido);

		query.setParameter("dataOperacao", dataOperacao);

		query.setParameter("postergado", postergado);

		if (idProdutoEdicao != null) {
			query.setParameter("idProdutoEdicao", idProdutoEdicao);
		}

		Long qtde = (Long) query.uniqueResult();

		return (qtde == null) ? 0 : qtde;

	}

	@SuppressWarnings("unchecked")
	public List<ChamadaEncalheCota> obterListaChamaEncalheCota(
			Integer numeroCota, Date dataOperacao, Long idProdutoEdicao,
			boolean indPesquisaCEFutura, boolean conferido, boolean postergado) {

		StringBuilder hql = new StringBuilder();

		hql.append(" select chamadaEncalheCota ");

		hql.append(" from ChamadaEncalheCota chamadaEncalheCota ");

		hql.append(" where ");

		hql.append(" chamadaEncalheCota.cota.numeroCota = :numeroCota ");

		hql.append(" and chamadaEncalheCota.fechado = :conferido ");

		hql.append(" and chamadaEncalheCota.postergado = :postergado ");

		if (indPesquisaCEFutura) {
			hql.append(" and chamadaEncalheCota.chamadaEncalhe.dataRecolhimento >= :dataOperacao ");
		} else {
			hql.append(" and chamadaEncalheCota.chamadaEncalhe.dataRecolhimento = :dataOperacao ");
		}

		if (idProdutoEdicao != null) {
			hql.append(" and chamadaEncalheCota.chamadaEncalhe.produtoEdicao.id = :idProdutoEdicao ");
		}

		Query query = this.getSession().createQuery(hql.toString());

		query.setParameter("numeroCota", numeroCota);

		query.setParameter("conferido", conferido);

		query.setParameter("postergado", postergado);

		query.setParameter("dataOperacao", dataOperacao);

		if (idProdutoEdicao != null) {
			query.setParameter("idProdutoEdicao", idProdutoEdicao);
		}

		return query.list();

	}

	public ChamadaEncalheCota buscarPorChamadaEncalheECota(
			Long idChamadaEncalhe, Long idCota) {

		StringBuilder hql = new StringBuilder();

		hql.append(" select o from ChamadaEncalheCota o ")
				.append(" WHERE o.cota.id =:idCota ")
				.append(" AND o.chamadaEncalhe.id=:idChamadaEncalhe ");

		Query query = getSession().createQuery(hql.toString());
		query.setParameter("idChamadaEncalhe", idChamadaEncalhe);
		query.setParameter("idCota", idCota);

		return (ChamadaEncalheCota) query.uniqueResult();

	}

	@Override
	public BigDecimal obterQntExemplaresComProgramacaoAntecipadaEncalheCota(
			FiltroChamadaAntecipadaEncalheDTO filtro) {

		StringBuilder hql = new StringBuilder();

		/*
		 * Foi incluido a cláusula DISTINCT para evitar o cenário de mais de um 
		 * PDV associado a mesma cota.
		 */
		hql.append(" SELECT DISTINCT chamadaEncalheCota.qtdePrevista ");

		hql.append(getSqlFromEWhereCotasProgramadaParaAntecipacaoEncalhe(filtro));

		Query query = this.getSession().createQuery(hql.toString());

		HashMap<String, Object> param = getParametrosCotasProgramadaParaAntecipacaoEncalhe(filtro);

		for (String key : param.keySet()) {
			query.setParameter(key, param.get(key));
		}

		query.setMaxResults(1);

		return (BigDecimal) query.uniqueResult();
	}

	@Override
	public Long obterQntCotasProgramadaParaAntecipacoEncalhe(
			FiltroChamadaAntecipadaEncalheDTO filtro) {

		StringBuilder hql = new StringBuilder();

		/*
		 * Foi incluido a cláusula DISTINCT para evitar o cenário de mais de um 
		 * PDV associado a mesma cota.
		 */
		hql.append("SELECT DISTINCT count ( cota.id ) ");

		hql.append(getSqlFromEWhereCotasProgramadaParaAntecipacaoEncalhe(filtro));

		Query query = this.getSession().createQuery(hql.toString());

		HashMap<String, Object> param = getParametrosCotasProgramadaParaAntecipacaoEncalhe(filtro);

		for (String key : param.keySet()) {
			query.setParameter(key, param.get(key));
		}
		return (Long) query.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public List<ChamadaAntecipadaEncalheDTO> obterCotasProgramadaParaAntecipacoEncalhe(
			FiltroChamadaAntecipadaEncalheDTO filtro) {

		StringBuilder hql = new StringBuilder();

		/*
		 * Foi incluido a cláusula DISTINCT para evitar o cenário de mais de um 
		 * PDV associado a mesma cota.
		 */
		hql.append("SELECT DISTINCT new ")
				.append(ChamadaAntecipadaEncalheDTO.class.getCanonicalName())
				.append(" (box.codigo, ")
				.append(" box.nome, ")
				.append(" cota.numeroCota, ")
				.append(" chamadaEncalheCota.qtdePrevista,")
				.append(" case when (pessoa.nome is not null) then ( pessoa.nome )")
				.append(" when (pessoa.razaoSocial is not null) then ( pessoa.razaoSocial )")
				.append(" else null end ,").append(" chamadaEncalheCota.id ")
				.append(" ) ");

		hql.append(getSqlFromEWhereCotasProgramadaParaAntecipacaoEncalhe(filtro));

		hql.append(getOrderByCotasProgramadaParaAntecipacaoEncalhe(filtro));

		Query query = this.getSession().createQuery(hql.toString());

		HashMap<String, Object> param = getParametrosCotasProgramadaParaAntecipacaoEncalhe(filtro);

		for (String key : param.keySet()) {
			query.setParameter(key, param.get(key));
		}

		if (filtro.getPaginacao() != null) {

			if (filtro.getPaginacao().getPosicaoInicial() != null) {
				query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
			}

			if (filtro.getPaginacao().getQtdResultadosPorPagina() != null) {
				query.setMaxResults(filtro.getPaginacao()
						.getQtdResultadosPorPagina());
			}
		}

		return query.list();
	}

	private HashMap<String, Object> getParametrosCotasProgramadaParaAntecipacaoEncalhe(
			FiltroChamadaAntecipadaEncalheDTO filtro) {

		HashMap<String, Object> param = new HashMap<String, Object>();

		param.put("codigoProduto", filtro.getCodigoProduto());
		param.put("numeroEdicao", filtro.getNumeroEdicao());
		param.put("tipoChamadaEncalhe", TipoChamadaEncalhe.ANTECIPADA);
		param.put("dataOperacao", filtro.getDataOperacao());

		if (filtro.getNumeroCota() != null) {
			param.put("numeroCota", filtro.getNumeroCota());
		}

		if (filtro.getFornecedor() != null) {
			param.put("fornecedor", filtro.getFornecedor());
		}

		if (filtro.getBox() != null) {
			param.put("box", filtro.getBox());
		}

		if (filtro.getRota() != null) {
			param.put("rota", filtro.getRota());
		}

		if (filtro.getRoteiro() != null) {
			param.put("roteiro", filtro.getRoteiro());
		}

		if (filtro.getCodMunicipio() != null) {
			param.put("codigoCidadeIBGE", filtro.getCodMunicipio());
		}

		if (filtro.getCodTipoPontoPDV() != null) {
			param.put("codigoTipoPontoPDV", filtro.getCodTipoPontoPDV());
		}

		return param;
	}

	private Object getOrderByCotasProgramadaParaAntecipacaoEncalhe(
			FiltroChamadaAntecipadaEncalheDTO filtro) {

		if (filtro == null || filtro.getOrdenacaoColuna() == null) {
			return "";
		}

		StringBuilder hql = new StringBuilder();

		switch (filtro.getOrdenacaoColuna()) {
		case BOX:
			hql.append(" order by box.codigo ");
			break;
		case NOME_COTA:
			hql.append(" order by   ")
					.append(" case when (pessoa.nome is not null) then ( pessoa.nome )")
					.append(" when (pessoa.razaoSocial is not null) then ( pessoa.razaoSocial )")
					.append(" else null end ");
			break;
		case NUMERO_COTA:
			hql.append(" order by cota.numeroCota ");
			break;
		case QNT_EXEMPLARES:
			hql.append(" order by  chamadaEncalheCota.qtdePrevista ");
			break;
		default:
			hql.append(" order by  box.codigo ");
		}

		if (filtro.getPaginacao().getOrdenacao() != null) {
			hql.append(filtro.getPaginacao().getOrdenacao().toString());
		}

		return hql.toString();
	}

	private Object getSqlFromEWhereCotasProgramadaParaAntecipacaoEncalhe(FiltroChamadaAntecipadaEncalheDTO filtro) {
		
		StringBuilder hql = new StringBuilder();

		hql.append("  FROM ChamadaEncalheCota chamadaEncalheCota ")
			.append(" JOIN chamadaEncalheCota.chamadaEncalhe chamadaEncalhe ")
			.append(" JOIN chamadaEncalhe.produtoEdicao produtoEdicao ")
			.append(" JOIN produtoEdicao.produto produto ")
			.append(" JOIN chamadaEncalheCota.cota cota ")
			.append(" JOIN cota.pessoa pessoa ")
			.append(" JOIN cota.box box ")
			.append(" JOIN cota.pdvs pdv ")
			.append(" LEFT JOIN pdv.rotas rotaPdv  ")
			.append(" LEFT JOIN rotaPdv.rota rota  ")
			.append(" LEFT JOIN rota.roteiro roteiro ");

			
		if (filtro.getFornecedor() != null) {
			hql.append(" JOIN produto.fornecedores fornecedor ");
		}
		
		if(filtro.getCodMunicipio()!= null){
			hql.append(" JOIN pdv.enderecos enderecoPDV ")
				.append(" JOIN enderecoPDV.endereco endereco ");
		}
			
		hql.append(" WHERE ")
			.append(" chamadaEncalhe.tipoChamadaEncalhe=:tipoChamadaEncalhe")
			.append(" AND produto.codigo =:codigoProduto ")
			.append(" AND produtoEdicao.numeroEdicao =:numeroEdicao ")
			.append(" AND chamadaEncalheCota.fechado = false ")
			.append(" AND chamadaEncalheCota.postergado = false ")
			.append(" AND pdv.caracteristicas.pontoPrincipal = true ")
			.append(" AND chamadaEncalhe.dataRecolhimento > :dataOperacao ");
		
		if (filtro.getNumeroCota() != null) {

			hql.append(" AND cota.numeroCota =:numeroCota ");
		}

		if (filtro.getFornecedor() != null) {

			hql.append(" AND fornecedor.id =:fornecedor ");
		}

		if (filtro.getBox() != null) {

			hql.append(" AND box.id =:box ");
		}
		
		if(filtro.getRota()!= null){
			hql.append(" AND rota.id =:rota ");
		}
		
		if(filtro.getRoteiro()!= null ){
			hql.append(" AND roteiro.id =:roteiro ");
		}
		
		if(filtro.getCodMunicipio()!= null){
			hql.append(" AND endereco.codigoCidadeIBGE =:codigoCidadeIBGE ");
		}
		
		if(filtro.getCodTipoPontoPDV()!= null){
			hql.append(" AND pdv.segmentacao.tipoPontoPDV.codigo =:codigoTipoPontoPDV ");
		}
		
		return hql;
	}

	@Override
	public Long obterQntChamadaEncalheCota(Long idChamadaEncalhe) {

		String hql = " select count(chamadaEncalheCota.id) from ChamadaEncalheCota chamadaEncalheCota where chamadaEncalheCota.chamadaEncalhe.id=:idChamada ";

		Query query = getSession().createQuery(hql);

		query.setParameter("idChamada", idChamadaEncalhe);

		return (Long) query.uniqueResult();
	}
	
	/**
	 * Retorna String referente a uma subquery que obtém o percentual de desconto
	 * para determinado produtoEdicao a partir de idCota e idFornecedor. 
	 * 
	 * @return String
	 */
	private String getSubQueryDesconto() {
		
		StringBuilder hql = new StringBuilder("coalesce ((select view.desconto");
		hql.append(" from ViewDesconto view ")
		   .append(" where view.cotaId = cota.id ")
		   .append(" and view.produtoEdicaoId = produtoEdicao.id ")
		   .append(" and view.fornecedorId = fornecedor.id),0) ");
		
		return hql.toString();
		
	}
}
