package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.ResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.fiscal.ItemNotaFiscal;
import br.com.abril.nds.model.fiscal.NotaFiscal;
import br.com.abril.nds.model.fiscal.StatusNotaFiscal;
import br.com.abril.nds.repository.NotaFiscalRepository;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.estoque.DetalheNotaFiscalVO;
import br.com.abril.nds.vo.filtro.FiltroConsultaNotaFiscalDTO;

@Repository
public class NotaFiscalRepositoryImpl extends AbstractRepository<NotaFiscal, Long> implements
		NotaFiscalRepository {

	public NotaFiscalRepositoryImpl() {
		super(NotaFiscal.class);
	}

	/**
	 * Obtém uma lista de notas fiscais de acordo com os seguintes parâmetros:
	 * 
	 * Cnpj - da pessoa jurídica relativa a nota.
	 * Numero da nota.
	 * Série da nota.
	 * Chave - caso esta seja um nota fiscal eletrônica.
	 * 
	 * @param filtroConsultaNotaFiscal
	 * @return List<NotaFiscal>
	 */
	@SuppressWarnings("unchecked")
	public List<NotaFiscal> obterListaNotaFiscal(FiltroConsultaNotaFiscalDTO filtroConsultaNotaFiscal) {
			
		StringBuffer hql = new StringBuffer();
		
		hql.append("select n from NotaFiscal n ");
		
		hql.append(" where  ");

		hql.append(" n.numero = :numero and		");
		hql.append(" n.serie = :serie 	and		");
		hql.append(" n.emitente.cnpj = :cnpj 	");
		
		
		if( filtroConsultaNotaFiscal.getChave() == null || "".equals(filtroConsultaNotaFiscal.getChave()) ){
			hql.append(" and n.chaveAcesso is null ");
		}else{
			hql.append(" and n.chaveAcesso = :chaveAcesso ");			
		}
		
		Query query = criarQueryComParametrosObterNotasFiscaisCadastradas(hql.toString(), filtroConsultaNotaFiscal);
		
		query.setParameter("cnpj",filtroConsultaNotaFiscal.getCnpj() );
		query.setParameter("serie",filtroConsultaNotaFiscal.getSerie() );
		query.setParameter("numero",filtroConsultaNotaFiscal.getNumeroNota() );
		
		if( filtroConsultaNotaFiscal.getChave() != null || !"".equals(filtroConsultaNotaFiscal.getChave()) ){
			query.setParameter("chave", filtroConsultaNotaFiscal.getChave());
		}
		
		return query.list();
		
		
	}
	
	public Integer obterQuantidadeNotasFicaisCadastradas(FiltroConsultaNotaFiscalDTO filtroConsultaNotaFiscal) {

		String hql = getConsultaNotasFiscaisCadastradas(filtroConsultaNotaFiscal);

		hql = hql.replaceFirst("select notaFiscal", "select count(notaFiscal)");

		Query query = criarQueryComParametrosObterNotasFiscaisCadastradas(hql, filtroConsultaNotaFiscal);

		return ((Long) query.uniqueResult()).intValue();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<NotaFiscal> obterNotasFiscaisCadastradas(FiltroConsultaNotaFiscalDTO filtroConsultaNotaFiscal) {

		String hql = getConsultaNotasFiscaisCadastradas(filtroConsultaNotaFiscal);
		
		Query query = criarQueryComParametrosObterNotasFiscaisCadastradas(hql, filtroConsultaNotaFiscal);
		
		query.setFirstResult(filtroConsultaNotaFiscal.getPaginacao().getPosicaoInicial());
		query.setMaxResults(filtroConsultaNotaFiscal.getPaginacao().getQtdResultadosPorPagina());

		return query.list();
	}

	private String getConsultaNotasFiscaisCadastradas(FiltroConsultaNotaFiscalDTO filtroConsultaNotaFiscal) { 

		StringBuilder hql = new StringBuilder();
		
		hql.append(" select notaFiscal from NotaFiscal notaFiscal ");
		
		if (filtroConsultaNotaFiscal.getIdFornecedor() != null) {
			
			hql.append(", Fornecedor fornecedor ")
			   .append(" join notaFiscal.emitente ");
		}

		if (filtroConsultaNotaFiscal.getIdTipoNotaFiscal() != null) {
		
			hql.append(" join notaFiscal.tipoNotaFiscal ");
		}
		
		hql.append(" where notaFiscal.dataEmissao between :dataInicio and :dataFim ");
		
		if (filtroConsultaNotaFiscal.getIdTipoNotaFiscal() != null) {

			hql.append(" and notaFiscal.tipoNotaFiscal.id = :idTipoNotaFiscal ");
		}
		
		if (filtroConsultaNotaFiscal.getIdFornecedor() != null) {

		   hql.append(" and fornecedor.id = :idFornecedor ")
 		      .append(" and fornecedor.juridica.id = notaFiscal.emitente.id ");
		}

		if (filtroConsultaNotaFiscal.isNotaRecebida() != null) {
			
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
					hql.append(" notaFiscal.emitente.razaoSocial ");
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
		
		if (filtroConsultaNotaFiscal.isNotaRecebida() != null) {

			StatusNotaFiscal statusNotaFiscal = 
					filtroConsultaNotaFiscal.isNotaRecebida() ? 
							StatusNotaFiscal.RECEBIDA : StatusNotaFiscal.NAO_RECEBIDA; 

			query.setParameter("statusNotaFiscal", statusNotaFiscal);
		}

		return query;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<DetalheNotaFiscalVO> obterDetalhesNotaFical(Long idNotaFiscal) {

		String hql = 
				" select "
				   + " itemNotaFiscal.id as codigoItem, " 
				   + " itemNotaFiscal.produtoEdicao.produto.nome as nomeProduto, " 
				   + " itemNotaFiscal.produtoEdicao.numeroEdicao as numeroEdicao, "
				   + " itemNotaFiscal.qtde as quantidadeExemplares, " 
//				   + " ,null, null "
				   + " itemNotaFiscal.recebimentoFisico.diferenca.qtde as sobrasFaltas, " 
				   + " itemNotaFiscal.recebimentoFisico.diferenca.tipoDiferenca as tipoDiferenca " 
				   + " from ItemNotaFiscal itemNotaFiscal "
				   + " left join itemNotaFiscal.recebimentoFisico "
				   + " left join itemNotaFiscal.recebimentoFisico.diferenca ";
//				   + " left join itemNotaFiscal.produtoEdicao ";
//				   + " where itemNotaFiscal.notaFiscal.id = :idNotaFiscal ";

		ResultTransformer resultTransformer = new AliasToBeanResultTransformer(DetalheNotaFiscalVO.class); 

		Query query = getSession().createQuery(hql);

//		query.setResultTransformer(resultTransformer);
//		query.setParameter("idNotaFiscal", idNotaFiscal);

		List<ItemNotaFiscal> listaItemNotaFiscal = query.list();
		
		return query.list();
	}

	
	@Override	
	public void inserirNotaFiscal(NotaFiscal notaFiscal){
		this.adicionar(notaFiscal);
		
	}
	@Override	
	public NotaFiscal obterNotaFiscalPorNumero(String numero){
		String hql = "from NotaFiscal nf where nf.numero = :numero ";
		Query query = super.getSession().createQuery(hql);
		query.setParameter("numero", numero);
		return (NotaFiscal) query.uniqueResult();
	}
	
	
}
