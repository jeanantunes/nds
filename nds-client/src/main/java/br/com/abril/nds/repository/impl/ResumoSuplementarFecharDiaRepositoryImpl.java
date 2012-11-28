package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.SuplementarFecharDiaDTO;
import br.com.abril.nds.dto.VendaFechamentoDiaDTO;
import br.com.abril.nds.model.cadastro.FormaComercializacao;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.TipoVendaEncalhe;
import br.com.abril.nds.repository.ResumoSuplementarFecharDiaRepository;

@Repository
public class ResumoSuplementarFecharDiaRepositoryImpl extends AbstractRepository implements
		ResumoSuplementarFecharDiaRepository {

	@Override
	public BigDecimal obterValorEstoqueLogico(Date dataOperacao) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append("SELECT ");
				hql.append("( ");
				hql.append(" coalesce( (SELECT me2.qtde from MovimentoEstoque as me2 ");       
				hql.append(" JOIN me2.tipoMovimento as tm2 ");       
				hql.append(" WHERE tm2.grupoMovimentoEstoque = :entradaSuplementarEnvioReparte");
				hql.append(" AND me2.data = :dataOperacao), 0)");
			hql.append(" + ");
				hql.append(" coalesce( (SELECT me2.qtde from MovimentoEstoque as me2");       
				hql.append(" JOIN me2.tipoMovimento as tm2 ");       
				hql.append(" WHERE tm2.grupoMovimentoEstoque = :suplementarCotaAusente");
				hql.append(" AND me2.data = :dataOperacao),0) ");
			hql.append(" - ");							
				hql.append(" coalesce( (SELECT me2.qtde from MovimentoEstoque as me2");       
				hql.append(" JOIN me2.tipoMovimento as tm2 ");       
				hql.append(" WHERE tm2.grupoMovimentoEstoque = :reparteCotaAusente");
				hql.append(" AND me2.data = :dataOperacao),0)");
				hql.append(" ) ");
		hql.append("  * pe.precoVenda");
		hql.append(" FROM MovimentoEstoque as me ");
		hql.append(" JOIN me.produtoEdicao as pe ");		
		hql.append(" WHERE me.data = :dataOperacao");
		hql.append(" GROUP BY me.data");
		
		Query query = super.getSession().createQuery(hql.toString());
		
		query.setParameter("dataOperacao", dataOperacao);
		
		query.setParameter("entradaSuplementarEnvioReparte", GrupoMovimentoEstoque.ENTRADA_SUPLEMENTAR_ENVIO_REPARTE);
		query.setParameter("suplementarCotaAusente", GrupoMovimentoEstoque.SUPLEMENTAR_COTA_AUSENTE);
		query.setParameter("reparteCotaAusente", GrupoMovimentoEstoque.REPARTE_COTA_AUSENTE);
		
		BigDecimal total =  (BigDecimal) query.uniqueResult();
		
		return total != null ? total : BigDecimal.ZERO ;
	}

	@Override
	public BigDecimal obterValorTransferencia(Date dataOperacao) {
		
		StringBuilder hql = new StringBuilder();
		
			hql.append("SELECT ");
			hql.append("( ");
				hql.append(" coalesce( (SELECT me2.qtde from MovimentoEstoque as me2 ");       
				hql.append(" JOIN me2.tipoMovimento as tm2 ");       
				hql.append(" WHERE tm2.grupoMovimentoEstoque = :transferenciaEntradaSuplementar");
				hql.append(" AND me2.data = :dataOperacao), 0)");
			hql.append(" - ");
				hql.append(" coalesce( (SELECT me2.qtde from MovimentoEstoque as me2");       
				hql.append(" JOIN me2.tipoMovimento as tm2 ");       
				hql.append(" WHERE tm2.grupoMovimentoEstoque = :suplementarCotaAusente");
				hql.append(" AND me2.data = :dataOperacao),0) ");			
			hql.append(" ) ");
			hql.append("  * pe.precoVenda");
			hql.append(" FROM MovimentoEstoque as me ");
			hql.append(" JOIN me.produtoEdicao as pe ");		
			hql.append(" WHERE me.data = :dataOperacao");
			hql.append(" GROUP BY me.data");
		

		Query query = super.getSession().createQuery(hql.toString());
		
		query.setParameter("dataOperacao", dataOperacao);
		
		query.setParameter("transferenciaEntradaSuplementar", GrupoMovimentoEstoque.TRANSFERENCIA_ENTRADA_SUPLEMENTAR );
		query.setParameter("suplementarCotaAusente", GrupoMovimentoEstoque.TRANSFERENCIA_SAIDA_SUPLEMENTAR);
		
		BigDecimal total =  (BigDecimal) query.uniqueResult();
		
		return total != null ? total : BigDecimal.ZERO ;
	}

	@Override
	public BigDecimal obterValorVenda(Date dataOperacao) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT   ");
		hql.append(" SUM(ve.qntProduto * pe.precoVenda)");
		hql.append(" FROM VendaProduto as ve ");		 
		hql.append(" JOIN ve.produtoEdicao as pe ");					
		hql.append(" JOIN pe.produto as p ");					
		hql.append(" WHERE ve.dataVenda = :dataOperacao ");
		hql.append(" AND ve.tipoComercializacaoVenda = :tipoComercializacaoVenda ");
		
		hql.append(" AND ve.tipoVenda = :suplementar");

		Query query = super.getSession().createQuery(hql.toString());
		
		query.setParameter("dataOperacao", dataOperacao);
		
		query.setParameter("suplementar", TipoVendaEncalhe.SUPLEMENTAR);
		
		query.setParameter("tipoComercializacaoVenda", FormaComercializacao.CONTA_FIRME);	
		
		BigDecimal total =  (BigDecimal) query.uniqueResult();
		
		return total != null ? total : BigDecimal.ZERO ;
	}

	@Override
	public BigDecimal obterValorFisico(Date dataOperacao) {
		StringBuilder hql = new StringBuilder();
		
		hql.append("SELECT SUM(ep.qtdeSuplementar) ");
		hql.append("FROM EstoqueProduto as ep ");
	
		Query query = super.getSession().createQuery(hql.toString());		
		
		BigInteger total =  (BigInteger) query.uniqueResult();
		BigDecimal totalFormatado = new BigDecimal(total);
		
		return totalFormatado != null ? totalFormatado : BigDecimal.ZERO ;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<VendaFechamentoDiaDTO> obterVendasSuplementar(Date dataOperacao) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT p.codigo as codigo,  ");
		hql.append(" p.nome as nomeProduto, ");
		hql.append(" pe.numeroEdicao as numeroEdicao, ");
		hql.append(" ve.qntProduto as qtde, ");
		hql.append(" (ve.qntProduto * pe.precoVenda) as valor, ");
		hql.append(" ve.dataVenda as dataRecolhimento ");
		
		hql.append(" FROM VendaProduto as ve ");		 
		hql.append(" JOIN ve.produtoEdicao as pe ");					
		hql.append(" JOIN pe.produto as p ");					
		hql.append(" WHERE ve.dataVenda = :dataOperacao ");
		hql.append(" AND ve.tipoComercializacaoVenda = :tipoComercializacaoVenda ");
		
		hql.append(" AND ve.tipoVenda = :suplementar");

		Query query = super.getSession().createQuery(hql.toString());
		
		query.setParameter("dataOperacao", dataOperacao);
		
		query.setParameter("suplementar", TipoVendaEncalhe.SUPLEMENTAR);
		
		query.setParameter("tipoComercializacaoVenda", FormaComercializacao.CONTA_FIRME);		
		
		query.setResultTransformer(new AliasToBeanResultTransformer(
				VendaFechamentoDiaDTO.class));
		
		return query.list();
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SuplementarFecharDiaDTO> obterDadosGridSuplementar() {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT p.codigo as codigo,  ");
		hql.append(" p.nome as nomeProduto, ");
		hql.append(" pe.numeroEdicao as numeroEdicao, ");
		hql.append(" pe.precoVenda as precoVenda ");								
		hql.append(" FROM EstoqueProduto as ep ");				 
		hql.append(" JOIN ep.produtoEdicao as pe ");
		hql.append(" JOIN pe.produto as p ");
		hql.append(" WHERE ep.qtdeSuplementar is not null");				

		Query query = super.getSession().createQuery(hql.toString());	
		
		
		query.setResultTransformer(new AliasToBeanResultTransformer(
				SuplementarFecharDiaDTO.class));
		
		return query.list();
	}

}
