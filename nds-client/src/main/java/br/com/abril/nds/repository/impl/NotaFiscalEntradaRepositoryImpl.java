package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.ResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.DetalheItemNotaFiscalDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaNotaFiscalDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaNotaFiscalDTO.ColunaOrdenacao;
import br.com.abril.nds.model.fiscal.NotaFiscalEntrada;
import br.com.abril.nds.model.fiscal.NotaFiscalEntradaFornecedor;
import br.com.abril.nds.model.fiscal.StatusNotaFiscalEntrada;
import br.com.abril.nds.repository.NotaFiscalEntradaRepository;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.PeriodoVO;

@Repository
public class NotaFiscalEntradaRepositoryImpl extends AbstractRepositoryModel<NotaFiscalEntrada, Long> implements
		NotaFiscalEntradaRepository {

	public NotaFiscalEntradaRepositoryImpl() {
		super(NotaFiscalEntrada.class);
	}

	public Integer obterQuantidadeNotasFicaisCadastradas(FiltroConsultaNotaFiscalDTO filtroConsultaNotaFiscal) {

		String hql = getConsultaNotasFiscaisCadastradas(filtroConsultaNotaFiscal);

		hql = hql.replaceFirst("select notaFiscal", "select count(notaFiscal)");

		Query query = criarQueryComParametrosObterNotasFiscaisCadastradas(hql, filtroConsultaNotaFiscal);

		return ((Long) query.uniqueResult()).intValue();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<NotaFiscalEntradaFornecedor> obterNotasFiscaisCadastradas(FiltroConsultaNotaFiscalDTO filtroConsultaNotaFiscal) {

		String hql = getConsultaNotasFiscaisCadastradas(filtroConsultaNotaFiscal);
		
		Query query = criarQueryComParametrosObterNotasFiscaisCadastradas(hql, filtroConsultaNotaFiscal);
		
		if (filtroConsultaNotaFiscal.getPaginacao() != null) {
			
			if (filtroConsultaNotaFiscal.getPaginacao().getPosicaoInicial() != null) {
				
				query.setFirstResult(filtroConsultaNotaFiscal.getPaginacao().getPosicaoInicial());
			}
			
			if (filtroConsultaNotaFiscal.getPaginacao().getQtdResultadosPorPagina() != null) {
				
				query.setMaxResults(filtroConsultaNotaFiscal.getPaginacao().getQtdResultadosPorPagina());
			}
		}

		return query.list();
	}

	private String getConsultaNotasFiscaisCadastradas(FiltroConsultaNotaFiscalDTO filtroConsultaNotaFiscal) { 

		StringBuilder hql = new StringBuilder();

		hql.append(" select notaFiscal from NotaFiscalEntradaFornecedor notaFiscal ")
		   .append(" join notaFiscal.fornecedor ")
		   .append(" join notaFiscal.tipoNotaFiscal ");
		
		String condicoes = "";
		
		PeriodoVO periodo = filtroConsultaNotaFiscal.getPeriodo();
		
		if (periodo.getDataInicial() != null && periodo.getDataFinal() != null) {

			condicoes += "".equals(condicoes) ? " where " : " and ";
			
			condicoes += " notaFiscal.dataEmissao between :dataInicio and :dataFim ";
		}

		if (filtroConsultaNotaFiscal.getIdTipoNotaFiscal() != null) {
			
			condicoes += "".equals(condicoes) ? " where " : " and ";
			
			condicoes += " notaFiscal.tipoNotaFiscal.id = :idTipoNotaFiscal ";
		}

		if (filtroConsultaNotaFiscal.getIdFornecedor() != null) {

			condicoes += "".equals(condicoes) ? " where " : " and ";
			
			condicoes += " notaFiscal.fornecedor.id = :idFornecedor ";
		}

		if (filtroConsultaNotaFiscal.getIsNotaRecebida() != null) {

			String condicaoNotaRecebida = filtroConsultaNotaFiscal.getIsNotaRecebida() ? " = " : " != ";

			condicoes += "".equals(condicoes) ? " where " : " and ";
			
			condicoes += " notaFiscal.statusNotaFiscal " 
					  + condicaoNotaRecebida 
					  + " :statusNotaFiscal ";
		}

		hql.append(condicoes);

		PaginacaoVO paginacao = filtroConsultaNotaFiscal.getPaginacao();

		if (filtroConsultaNotaFiscal.getListaColunaOrdenacao() != null || 
				!filtroConsultaNotaFiscal.getListaColunaOrdenacao().isEmpty()) {

			hql.append(" order by ");
			
			String orderByColumn = "";
			
			for (ColunaOrdenacao colunaOrdenacao : filtroConsultaNotaFiscal.getListaColunaOrdenacao()) {

				switch (colunaOrdenacao) {
				
					case DATA_EMISSAO:
						orderByColumn += orderByColumn.equals("") ? "" : ",";
						orderByColumn += " notaFiscal.dataEmissao ";
						break;
					case DATA_EXPEDICAO:
						orderByColumn += orderByColumn.equals("") ? "" : ",";
						orderByColumn += " notaFiscal.dataExpedicao ";
						break;
					case FORNECEDOR:
						orderByColumn += orderByColumn.equals("") ? "" : ",";
						orderByColumn += " notaFiscal.fornecedor.juridica.razaoSocial ";
						break;
					case NOTA_RECEBIDA:
						orderByColumn += orderByColumn.equals("") ? "" : ",";
						orderByColumn += " notaFiscal.statusNotaFiscal ";
						break;
					case NUMERO_NOTA:
						orderByColumn += orderByColumn.equals("") ? "" : ",";
						orderByColumn += " notaFiscal.numero ";
						break;
					case TIPO_NOTA:
						orderByColumn += orderByColumn.equals("") ? "" : ",";
						orderByColumn += " notaFiscal.tipoNotaFiscal.descricao ";
						break;
					default:
						break;
				}
			}
			
			hql.append(orderByColumn);
			
			if (paginacao.getOrdenacao() != null) {
				
				hql.append(paginacao.getOrdenacao().toString());
			}
		}		
		
		return hql.toString();
	}

	private Query criarQueryComParametrosObterNotasFiscaisCadastradas(String hql, FiltroConsultaNotaFiscalDTO filtroConsultaNotaFiscal) {

		Query query = getSession().createQuery(hql.toString());
		
		PeriodoVO periodo = filtroConsultaNotaFiscal.getPeriodo();
		
		if (periodo.getDataInicial() != null && periodo.getDataFinal() != null) {
			
			query.setParameter("dataInicio", periodo.getDataInicial());
			
			query.setParameter("dataFim", periodo.getDataFinal());
		}
		
		if (filtroConsultaNotaFiscal.getIdTipoNotaFiscal() != null) {
			
			query.setParameter("idTipoNotaFiscal", filtroConsultaNotaFiscal.getIdTipoNotaFiscal());
		}

		if (filtroConsultaNotaFiscal.getIdFornecedor() != null) {
			
			query.setParameter("idFornecedor", filtroConsultaNotaFiscal.getIdFornecedor());
		}
		
		if (filtroConsultaNotaFiscal.getIsNotaRecebida() != null) {

			query.setParameter("statusNotaFiscal", StatusNotaFiscalEntrada.RECEBIDA);
		}

		return query;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<DetalheItemNotaFiscalDTO> obterDetalhesNotaFical(Long idNotaFiscal) {

		String hql = 
				" select "
				   + " itemNotaFiscal.id as codigoItem, "
				   + " itemNotaFiscal.produtoEdicao.produto.codigo as codigoProduto,"
				   + " itemNotaFiscal.produtoEdicao.produto.nome as nomeProduto, " 
				   + " itemNotaFiscal.produtoEdicao.numeroEdicao as numeroEdicao, "
				   + " itemNotaFiscal.produtoEdicao.precoVenda as precoVenda, "
				   + " itemNotaFiscal.qtde as quantidadeExemplares, " 
				   + " (itemNotaFiscal.qtde * itemNotaFiscal.produtoEdicao.precoVenda) as valorTotal, "
				   + " diferenca.qtde as sobrasFaltas, " 
				   + " diferenca.tipoDiferenca as tipoDiferenca " 
				   + " from ItemNotaFiscalEntrada itemNotaFiscal "
				   + " left join itemNotaFiscal.recebimentoFisico.diferenca as diferenca "
				   + " where itemNotaFiscal.notaFiscal.id = :idNotaFiscal "
				   + " order by itemNotaFiscal.id ";

		ResultTransformer resultTransformer = new AliasToBeanResultTransformer(DetalheItemNotaFiscalDTO.class); 

		Query query = getSession().createQuery(hql);

		query.setResultTransformer(resultTransformer);
		query.setParameter("idNotaFiscal", idNotaFiscal);

		return query.list();
	}

	@Override	
	public void inserirNotaFiscal(NotaFiscalEntrada notaFiscal){
		
		if (notaFiscal == null) {
			throw new IllegalArgumentException("Erro inesperado. Nota Fiscal não definida.");
		}else{
			this.adicionar(notaFiscal);
		}
	}
	@Override	
	public NotaFiscalEntrada obterNotaFiscalPorNumero(String numero){
		String hql = "from NotaFiscalEntrada nf where nf.numero = :numero ";
		Query query = super.getSession().createQuery(hql);
		query.setParameter("numero", numero);
		return (NotaFiscalEntrada) query.uniqueResult();
	}
	/**
	 * Metodo para buscar nota com numero,serie, cnpj e chaveDeAcesso 
	 * @param filtroConsultaNotaFiscal
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<NotaFiscalEntrada> obterNotaFiscalPorNumeroSerieCnpj(FiltroConsultaNotaFiscalDTO filtroConsultaNotaFiscal){
		StringBuilder hql = new StringBuilder();
		
		hql.append("from NotaFiscalEntrada nf where nf.numero = :numero ");		
		hql.append("and nf.serie = :serie ");
		if(filtroConsultaNotaFiscal.getNomeFornecedor() != null && !filtroConsultaNotaFiscal.getNomeFornecedor().equals("-1")){
			hql.append("and nf.emitente.cnpj = :cnpj ");	
		}
			
		if(filtroConsultaNotaFiscal.getChave() == null){
			hql.append("and nf.chaveAcesso is null ");	
		}else{
			hql.append("and nf.chaveAcesso = :chaveAcesso  ");	
		}
		Query query = super.getSession().createQuery(hql.toString());
		
		query.setParameter("numero", filtroConsultaNotaFiscal.getNumeroNota());
		query.setParameter("serie", filtroConsultaNotaFiscal.getSerie());
		if(filtroConsultaNotaFiscal.getNomeFornecedor() != null && !filtroConsultaNotaFiscal.getNomeFornecedor().equals("-1")){
			query.setParameter("cnpj", filtroConsultaNotaFiscal.getCnpj());
		}	
		
		if(filtroConsultaNotaFiscal.getChave() != null){
			query.setParameter("chaveAcesso", filtroConsultaNotaFiscal.getChave());
		}
		
		return query.list();
	}	
}