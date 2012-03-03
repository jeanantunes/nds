package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.ResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.DetalheItemNotaFiscalDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaNotaFiscalDTO;
import br.com.abril.nds.model.fiscal.NotaFiscal;
import br.com.abril.nds.model.fiscal.NotaFiscalFornecedor;
import br.com.abril.nds.model.fiscal.StatusNotaFiscal;
import br.com.abril.nds.repository.NotaFiscalRepository;
import br.com.abril.nds.vo.PaginacaoVO;

@Repository
public class NotaFiscalRepositoryImpl extends AbstractRepository<NotaFiscal, Long> implements
		NotaFiscalRepository {

	public NotaFiscalRepositoryImpl() {
		super(NotaFiscal.class);
	}

	public Integer obterQuantidadeNotasFicaisCadastradas(FiltroConsultaNotaFiscalDTO filtroConsultaNotaFiscal) {

		String hql = getConsultaNotasFiscaisCadastradas(filtroConsultaNotaFiscal);

		hql = hql.replaceFirst("select notaFiscal", "select count(notaFiscal)");

		Query query = criarQueryComParametrosObterNotasFiscaisCadastradas(hql, filtroConsultaNotaFiscal);

		return ((Long) query.uniqueResult()).intValue();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<NotaFiscalFornecedor> obterNotasFiscaisCadastradas(FiltroConsultaNotaFiscalDTO filtroConsultaNotaFiscal) {

		String hql = getConsultaNotasFiscaisCadastradas(filtroConsultaNotaFiscal);
		
		Query query = criarQueryComParametrosObterNotasFiscaisCadastradas(hql, filtroConsultaNotaFiscal);
		
		query.setFirstResult(filtroConsultaNotaFiscal.getPaginacao().getPosicaoInicial());
		query.setMaxResults(filtroConsultaNotaFiscal.getPaginacao().getQtdResultadosPorPagina());

		return query.list();
	}

	private String getConsultaNotasFiscaisCadastradas(FiltroConsultaNotaFiscalDTO filtroConsultaNotaFiscal) { 

		StringBuilder hql = new StringBuilder();
		
		hql.append(" select notaFiscal from NotaFiscalFornecedor notaFiscal ")
		   .append(" join notaFiscal.fornecedor ")
		   .append(" join notaFiscal.tipoNotaFiscal ")
		   .append(" where notaFiscal.dataEmissao between :dataInicio and :dataFim ");
		
		if (filtroConsultaNotaFiscal.getIdTipoNotaFiscal() != null) {

			hql.append(" and notaFiscal.tipoNotaFiscal.id = :idTipoNotaFiscal ");
		}
		
		if (filtroConsultaNotaFiscal.getIdFornecedor() != null) {

		   hql.append(" and notaFiscal.fornecedor.id = :idFornecedor ");
		}

		if (filtroConsultaNotaFiscal.getIsNotaRecebida() != null && filtroConsultaNotaFiscal.getIsNotaRecebida()) {
			
			hql.append(" and notaFiscal.statusNotaFiscal = :statusNotaFiscal ");
		}
		
		PaginacaoVO paginacao = filtroConsultaNotaFiscal.getPaginacao();

		if (filtroConsultaNotaFiscal.getColunaOrdenacao() != null) {
				
			hql.append(" order by ");
			
			switch (filtroConsultaNotaFiscal.getColunaOrdenacao()) {
	
				case DATA_EMISSAO:
					hql.append(" notaFiscal.dataEmissao ");
					break;
				case DATA_EXPEDICAO:
					hql.append(" notaFiscal.dataExpedicao ");
					break;
				case FORNECEDOR:
					hql.append(" notaFiscal.fornecedor.juridica.razaoSocial ");
					break;
				case NOTA_RECEBIDA:
					hql.append(" notaFiscal.statusNotaFiscal ");
					break;
				case NUMERO_NOTA:
					hql.append(" notaFiscal.numero ");
					break;
				case TIPO_NOTA:
					hql.append(" notaFiscal.tipoNotaFiscal.descricao ");
					break;
				default:
					break;
			}
			
			if (paginacao.getOrdenacao() != null) {
				
				hql.append(paginacao.getOrdenacao().toString());
			}
		}		
		
		return hql.toString();
	}

	private Query criarQueryComParametrosObterNotasFiscaisCadastradas(String hql, FiltroConsultaNotaFiscalDTO filtroConsultaNotaFiscal) {

		Query query = getSession().createQuery(hql.toString());

		query.setParameter("dataInicio", filtroConsultaNotaFiscal.getPeriodo().getDataInicial());
		query.setParameter("dataFim", filtroConsultaNotaFiscal.getPeriodo().getDataFinal());
		
		if (filtroConsultaNotaFiscal.getIdTipoNotaFiscal() != null) {
			
			query.setParameter("idTipoNotaFiscal", filtroConsultaNotaFiscal.getIdTipoNotaFiscal());
		}

		if (filtroConsultaNotaFiscal.getIdFornecedor() != null) {
			
			query.setParameter("idFornecedor", filtroConsultaNotaFiscal.getIdFornecedor());
		}
		
		if (filtroConsultaNotaFiscal.getIsNotaRecebida() != null && 
				filtroConsultaNotaFiscal.getIsNotaRecebida()) {

			query.setParameter("statusNotaFiscal", StatusNotaFiscal.RECEBIDA);
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
				   + " from ItemNotaFiscal itemNotaFiscal "
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
	public void inserirNotaFiscal(NotaFiscal notaFiscal){
		
		if (notaFiscal == null) {
			throw new IllegalArgumentException("Erro inesperado. Nota Fiscal não definida.");
		}else{
			this.adicionar(notaFiscal);
		}
	}
	@Override	
	public NotaFiscal obterNotaFiscalPorNumero(String numero){
		String hql = "from NotaFiscal nf where nf.numero = :numero ";
		Query query = super.getSession().createQuery(hql);
		query.setParameter("numero", numero);
		return (NotaFiscal) query.uniqueResult();
	}
	/**
	 * Metodo para buscar nota com numero,serie, cnpj e chaveDeAcesso 
	 * @param filtroConsultaNotaFiscal
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<NotaFiscal> obterNotaFiscalPorNumeroSerieCnpj(FiltroConsultaNotaFiscalDTO filtroConsultaNotaFiscal){
		StringBuilder hql = new StringBuilder();
		
		hql.append("from NotaFiscal nf where nf.numero = :numero ");
		hql.append("and nf.serie = :serie ");
		hql.append("and nf.emitente.cnpj = :cnpj ");		
		if(filtroConsultaNotaFiscal.getChave() == null){
			hql.append("and nf.chaveAcesso is null ");	
		}else{
			hql.append("and nf.chaveAcesso = :chaveAcesso  ");	
		}
		Query query = super.getSession().createQuery(hql.toString());
		query.setParameter("numero", filtroConsultaNotaFiscal.getNumeroNota());
		query.setParameter("serie", filtroConsultaNotaFiscal.getSerie());
		query.setParameter("cnpj", filtroConsultaNotaFiscal.getCnpj());
		query.setParameter("chaveAcesso", filtroConsultaNotaFiscal.getChave());
		return query.list();
	}	
}