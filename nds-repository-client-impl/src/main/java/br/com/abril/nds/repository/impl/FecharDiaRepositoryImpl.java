package br.com.abril.nds.repository.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.client.vo.EstoqueFecharDiaVO;
import br.com.abril.nds.dto.ValidacaoConfirmacaoDeExpedicaoFecharDiaDTO;
import br.com.abril.nds.dto.ValidacaoGeracaoCobrancaFecharDiaDTO;
import br.com.abril.nds.dto.ValidacaoLancamentoFaltaESobraFecharDiaDTO;
import br.com.abril.nds.dto.ValidacaoRecebimentoFisicoFecharDiaDTO;
import br.com.abril.nds.dto.fechamentodiario.ResumoEstoqueDTO;
import br.com.abril.nds.model.StatusCobranca;
import br.com.abril.nds.model.StatusConfirmacao;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.FormaCobranca;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.financeiro.GrupoMovimentoFinaceiro;
import br.com.abril.nds.model.fiscal.StatusRecebimento;
import br.com.abril.nds.model.movimentacao.Movimento;
import br.com.abril.nds.model.movimentacao.TipoMovimento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.repository.AbstractRepository;
import br.com.abril.nds.repository.FecharDiaRepository;

@Repository
public class FecharDiaRepositoryImpl extends AbstractRepository implements FecharDiaRepository {

	@Override
	public boolean existeCobrancaParaFecharDia(Date diaDeOperaoMenosUm) {
		StringBuilder hql = new StringBuilder();
		hql.append(" select count(c.id) from Cobranca c where ");		
		hql.append(" c.statusCobranca = :statusCobranca");		
		hql.append(" and c.dataVencimento = :diaDeOperaoMenosUm ");
		
		Query query = super.getSession().createQuery(hql.toString());
		
		query.setParameter("statusCobranca", StatusCobranca.NAO_PAGO);
		
		query.setParameter("diaDeOperaoMenosUm", diaDeOperaoMenosUm);
		
		return (long)query.uniqueResult() == 0L ? false : true;
	}

	@Override
	public boolean existeNotaFiscalSemRecebimentoFisico(Date dataOperacaoDistribuidor) {

		StringBuilder hql = new StringBuilder();

		hql.append(" SELECT notaFiscal from NotaFiscalEntradaFornecedor notaFiscal ");		
		hql.append(" WHERE notaFiscal.statusRecebimento = :statusRecebimentoNF  ");		
		
		Query query = super.getSession().createQuery(hql.toString());
		
		query.setParameter("statusRecebimentoNF", StatusRecebimento.SALVO);
		
		return query.list().isEmpty() ? true : false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ValidacaoRecebimentoFisicoFecharDiaDTO> obterNotaFiscalComRecebimentoFisicoNaoConfirmado(Date dataOperacaoDistribuidor) {
		StringBuilder hql = new StringBuilder();

		hql.append(" select numero as numeroNotaFiscal from NotaFiscalEntradaFornecedor notaFiscal ");		
		hql.append(" WHERE notaFiscal.statusRecebimento = :statusRecebimentoNF  ");
		hql.append(" GROUP BY numero");
		
		Query query = super.getSession().createQuery(hql.toString());
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ValidacaoRecebimentoFisicoFecharDiaDTO.class));
		
		query.setParameter("statusRecebimentoNF", StatusRecebimento.SALVO);
		
		return query.list();
	}

	@Override
	public Boolean existeConfirmacaoDeExpedicao(Date dataOperacaoDistribuidor) {
		StringBuilder jpql = new StringBuilder();

		jpql.append(" SELECT CASE WHEN COUNT(lancamento) > 0 THEN true ELSE false END ");	
		jpql.append(" FROM Lancamento lancamento ");
		jpql.append(" WHERE lancamento.dataLancamentoDistribuidor = :dataOperacao ")
		    .append("   AND lancamento.status NOT IN (:status) ");
		
		Query query = getSession().createQuery(jpql.toString());
		
		List<StatusLancamento> listaLancamentos = new ArrayList<StatusLancamento>();
		
		listaLancamentos.add(StatusLancamento.EXPEDIDO);
		listaLancamentos.add(StatusLancamento.CANCELADO);
		
		query.setParameterList("status", listaLancamentos);
		query.setParameter("dataOperacao", dataOperacaoDistribuidor);
		
		return (Boolean) query.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ValidacaoConfirmacaoDeExpedicaoFecharDiaDTO> obterConfirmacaoDeExpedicao(Date dataOperacaoDistribuidor) {
		
		StringBuilder jpql = new StringBuilder();
		
		jpql.append(" SELECT produto.codigo AS codigo,");
		jpql.append(" produto.nome AS nomeProduto,");
		jpql.append(" pe.numeroEdicao AS edicao ");
		
		jpql.append(" FROM Lancamento AS lancamento ");
		jpql.append(" JOIN lancamento.produtoEdicao AS pe ");
		jpql.append(" left JOIN lancamento.estudo  estudo ");
		jpql.append(" JOIN pe.produto AS produto ");
		jpql.append(" join produto.fornecedores fornecedor ");
		jpql.append(" WHERE  lancamento.dataLancamentoDistribuidor = :dataOperacaoDistribuidor ");
		
		// jpql.append(" and estudo.status = :statusEstudo ");
				
		jpql.append(" AND  lancamento.status=:status ");	
		jpql.append(" GROUP BY produto.codigo, produto.nome, pe.numeroEdicao ");
		
		Query query = super.getSession().createQuery(jpql.toString());
		
		List<StatusLancamento> listaLancamentos = new ArrayList<StatusLancamento>();
		listaLancamentos.add(StatusLancamento.BALANCEADO);
		

		//query.setParameter("statusEstudo", StatusLancamento.ESTUDO_FECHADO);
		
		query.setParameterList("status", listaLancamentos);
		query.setParameter("dataOperacaoDistribuidor", dataOperacaoDistribuidor);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ValidacaoConfirmacaoDeExpedicaoFecharDiaDTO.class));
		
		return query.list();
		 
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ValidacaoLancamentoFaltaESobraFecharDiaDTO> existeLancamentoFaltasESobrasPendentes(Date dataOperacaoDistribuidor) {		 
		StringBuilder jpql = new StringBuilder();

		jpql.append("SELECT produto.codigo AS codigo,");
		jpql.append(" produto.nome AS nomeProduto,");
		jpql.append(" pe.numeroEdicao AS edicao, ");
		jpql.append(" diferenca.tipoDiferenca AS inconsistencia ");
		
		jpql.append(" FROM Diferenca as diferenca ");
		jpql.append(" JOIN diferenca.produtoEdicao as pe ");
		jpql.append(" JOIN pe.produto as produto ");
		jpql.append(" WHERE diferenca.statusConfirmacao = :statusConfirmacao ")
		    .append(" AND  diferenca.dataMovimento = :dataOperacaoDistribuidor ");
		jpql.append(" GROUP BY produto.codigo, produto.nomeComercial, pe.numeroEdicao, diferenca.tipoDiferenca ");
		
		Query query = getSession().createQuery(jpql.toString());
		
		query.setParameter("statusConfirmacao", StatusConfirmacao.PENDENTE);
		query.setParameter("dataOperacaoDistribuidor", dataOperacaoDistribuidor);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ValidacaoLancamentoFaltaESobraFecharDiaDTO.class));
		
		return query.list();
	}

	@Override
	public boolean existePendenciasDeAprovacao(Date dataOperacao, StatusAprovacao statusAprovacao, 
			List<TipoMovimento> tiposMovimentoVerificaAprovacao) {
		
		StringBuilder jpql = new StringBuilder("select count(movimento.id) ");
		jpql.append(" FROM Movimento movimento ");
		jpql.append(" WHERE  movimento.dataCriacao = :dataOperacao ");
		jpql.append(" AND  movimento.status = :statusAprovacao");
		
		if (tiposMovimentoVerificaAprovacao != null &&
				!tiposMovimentoVerificaAprovacao.isEmpty()){
			
			jpql.append(" AND movimento.tipoMovimento in (:tiposMovimentoVerificaAprovacao) ");
		}
		
		Query query = getSession().createQuery(jpql.toString());
		
		query.setParameter("dataOperacao", dataOperacao);
		query.setParameter("statusAprovacao", statusAprovacao);
		
		if (tiposMovimentoVerificaAprovacao != null &&
				!tiposMovimentoVerificaAprovacao.isEmpty()){
			
			query.setParameterList("tiposMovimentoVerificaAprovacao", tiposMovimentoVerificaAprovacao);
		}
		
		return (Long)query.uniqueResult() > 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ValidacaoGeracaoCobrancaFecharDiaDTO> obterFormasDeCobranca() {
		StringBuilder jpql = new StringBuilder();
		
		jpql.append(" SELECT fc.tipoFormaCobranca as tipoFormaCobranca, ");
		jpql.append(" fc.id as formaCobrancaId ");
		jpql.append(" FROM PoliticaCobranca pc ");
		jpql.append(" JOIN pc.formaCobranca as fc ");
		jpql.append(" WHERE pc.ativo = true ");
				
		Query query = getSession().createQuery(jpql.toString());
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ValidacaoGeracaoCobrancaFecharDiaDTO.class));
		
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ValidacaoGeracaoCobrancaFecharDiaDTO> obterDiasDaConcentracao(FormaCobranca formaCobranca){
			 
			StringBuilder hql = new StringBuilder();
			
			hql.append("Select ccc.codigoDiaSemana as diaDoMes ");
			
			hql.append("FROM ConcentracaoCobrancaCota as ccc ");
			hql.append("JOIN ccc.formaCobranca as fc ");
			hql.append("WHERE fc.id = :idFormaCobranca");
			
			Query query = getSession().createQuery(hql.toString());
			
			query.setParameter("idFormaCobranca", formaCobranca.getId());
			
			query.setResultTransformer(new AliasToBeanResultTransformer(ValidacaoGeracaoCobrancaFecharDiaDTO.class));
			
			return query.list();		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Movimento> obterMovimentosPorStatusData(List<GrupoMovimentoEstoque> gruposMovimentoEstoque,
														List<GrupoMovimentoFinaceiro> gruposMovimentoFinanceiro,
														Date dataMovimento, StatusAprovacao statusAprovacao) {

		StringBuilder hql = new StringBuilder();

		hql.append(" SELECT movimento ");

		hql.append(" FROM Movimento movimento ");
		hql.append(" WHERE movimento.data = :dataMovimento ");
		hql.append("  AND  movimento.status = :statusAprovacao ");
		
		String restricaoGrupos = "";		

		if (gruposMovimentoEstoque != null) {
		
			restricaoGrupos = " movimento.tipoMovimento.grupoMovimentoEstoque IN (:gruposMovimentoEstoque) ";
		}
		
		if (gruposMovimentoFinanceiro != null) {
		
			restricaoGrupos += restricaoGrupos.isEmpty() ? " " : " OR ";
			
			restricaoGrupos += " movimento.tipoMovimento.grupoMovimentoFinaceiro IN (:gruposMovimentoFinanceiro) "; 
		}

		if (!restricaoGrupos.isEmpty()) {

			hql.append(" AND ( ");
			hql.append(restricaoGrupos);
			hql.append(" ) ");
		}
		
		Query query = getSession().createQuery(hql.toString());

		query.setParameter("dataMovimento", dataMovimento);
		query.setParameter("statusAprovacao", statusAprovacao);
		
		if (gruposMovimentoEstoque != null) {
		
			query.setParameterList("gruposMovimentoEstoque", gruposMovimentoEstoque);
		}
		
		if (gruposMovimentoFinanceiro != null) {
		
			query.setParameterList("gruposMovimentoFinanceiro", gruposMovimentoFinanceiro);
		}

		return query.list();
	}

	@Override
	public ResumoEstoqueDTO obterResumoEstoque(Date dataOperacaoDistribuidor) {
		
		ResumoEstoqueDTO resumoDTO = new ResumoEstoqueDTO();
		
		ResumoEstoqueDTO.ResumoEstoqueExemplar exemplar = resumoDTO.new ResumoEstoqueExemplar();
		
		ResumoEstoqueDTO.ResumoEstoqueProduto produto = resumoDTO.new ResumoEstoqueProduto();
		
		ResumoEstoqueDTO.ValorResumoEstoque venda = resumoDTO.new ValorResumoEstoque();
		
		StringBuilder hql = new StringBuilder();
		
		hql.append("SELECT ");
		hql.append("COALESCE( COUNT(ep.produtoEdicao.id) ,0)  as produto, ");
		hql.append("COALESCE(SUM(ep.qtde),0) as exemplar, ");
		hql.append("COALESCE(SUM(ep.qtde * pe.precoVenda),0) as venda ");		
		hql.append("FROM EstoqueProduto as ep ");
		hql.append("JOIN ep.produtoEdicao as pe ");
		hql.append("WHERE ep.qtde IS NOT NULL");
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setResultTransformer(new AliasToBeanResultTransformer(EstoqueFecharDiaVO.class));
		
		EstoqueFecharDiaVO vo = (EstoqueFecharDiaVO) query.uniqueResult();
		
		exemplar.setQuantidadeLancamento(vo.getExemplar().intValue());
		
		produto.setQuantidadeLancamento(vo.getProduto().intValue());
		
		venda.setValorLancamento(vo.getVenda());
		
		//FIM DO LANÇAMENTO
		
		hql = new StringBuilder();
		
		hql.append("SELECT ");
		hql.append("COALESCE(COUNT(ep.produtoEdicao.id),0) as produto, ");
		hql.append("COALESCE(SUM(ep.qtde),0) as exemplar, ");
		hql.append("COALESCE(SUM(ep.qtde * pe.precoVenda),0) as venda ");		
		hql.append("FROM EstoqueProdutoCotaJuramentado as ep ");
		hql.append("JOIN ep.produtoEdicao as pe ");
		hql.append("WHERE ep.data = :dataOperacaoDistribuidor");		
		
		query = getSession().createQuery(hql.toString());
		
		query.setParameter("dataOperacaoDistribuidor", dataOperacaoDistribuidor);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(EstoqueFecharDiaVO.class));
		
		vo = (EstoqueFecharDiaVO) query.uniqueResult();		
		
		exemplar.setQuantidadeJuramentado(vo.getExemplar().intValue());
		
		produto.setQuantidadeJuramentado(vo.getProduto().intValue());
		
		venda.setValorJuramentado(vo.getVenda());
		
		//FIM DO JURAMENTADO
		
		hql = new StringBuilder();
		
		hql.append("SELECT ");
		hql.append("COALESCE( COUNT(ep.produtoEdicao.id),0) as produto, ");
		hql.append("COALESCE(SUM(ep.qtdeSuplementar),0) as exemplar, ");
		hql.append("COALESCE(SUM(ep.qtdeSuplementar * pe.precoVenda),0) as venda ");		
		hql.append("FROM EstoqueProduto as ep ");
		hql.append("JOIN ep.produtoEdicao as pe ");
		hql.append("WHERE ep.qtdeSuplementar IS NOT NULL");
		
		query = getSession().createQuery(hql.toString());		
		
		query.setResultTransformer(new AliasToBeanResultTransformer(EstoqueFecharDiaVO.class));
		
		vo = (EstoqueFecharDiaVO) query.uniqueResult();		
		
		exemplar.setQuantidadeSuplementar(vo.getExemplar().intValue());
		
		produto.setQuantidadeSuplementar(vo.getProduto().intValue());
		
		venda.setValorSuplementar(vo.getVenda());
		
		//FIM DO SUPLEMENTAR
		
		hql = new StringBuilder();
		
		hql.append("SELECT ");
		hql.append("COALESCE(COUNT(ep.produtoEdicao.id),0) as produto, ");
		hql.append("COALESCE(SUM(ep.qtdeDevolucaoEncalhe),0) as exemplar, ");
		hql.append("COALESCE(SUM(ep.qtdeDevolucaoEncalhe * pe.precoVenda),0) as venda ");		
		hql.append("FROM EstoqueProduto as ep ");
		hql.append("JOIN ep.produtoEdicao as pe ");
		hql.append("WHERE ep.qtdeDevolucaoEncalhe IS NOT NULL");
		
		
		query = getSession().createQuery(hql.toString());		
		
		query.setResultTransformer(new AliasToBeanResultTransformer(EstoqueFecharDiaVO.class));
		
		vo = (EstoqueFecharDiaVO) query.uniqueResult();		
		
		exemplar.setQuantidadeRecolhimento(vo.getExemplar().intValue());
		
		produto.setQuantidadeRecolhimento(vo.getProduto().intValue());
		
		venda.setValorRecolhimento(vo.getVenda());
		
		//FIM DO RECOLHIMENTO
		
		hql = new StringBuilder();
		
		hql.append("SELECT ");
		hql.append("COALESCE(COUNT(ep.produtoEdicao.id),0) as produto, ");
		hql.append("COALESCE(SUM(ep.qtdeDanificado),0) as exemplar, ");
		hql.append("COALESCE(SUM(ep.qtdeDanificado * pe.precoVenda),0) as venda ");		
		hql.append("FROM EstoqueProduto as ep ");
		hql.append("JOIN ep.produtoEdicao as pe ");
		hql.append("WHERE ep.qtdeDanificado IS NOT NULL");
		
		query = getSession().createQuery(hql.toString());		
		
		query.setResultTransformer(new AliasToBeanResultTransformer(EstoqueFecharDiaVO.class));
		
		vo = (EstoqueFecharDiaVO) query.uniqueResult();		
		
		exemplar.setQuantidadeDanificados(vo.getExemplar().intValue());
		
		produto.setQuantidadeDanificados(vo.getProduto().intValue());
		
		venda.setValorDanificados(vo.getVenda());
		
		//FIM DO DANIFICADO
		
		resumoDTO.setResumEstoqueExemplar(exemplar);
		resumoDTO.setResumoEstoqueProduto(produto);
		resumoDTO.setValorResumoEstoque(venda);
		
		
		return resumoDTO;	
	}
}
