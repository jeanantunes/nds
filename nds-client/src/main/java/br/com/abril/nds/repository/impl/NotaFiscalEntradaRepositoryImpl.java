package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.ResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.DetalheItemNotaFiscalDTO;
import br.com.abril.nds.dto.ItemDTO;
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

		String hql = getConsultaNotasFiscaisCadastradas(filtroConsultaNotaFiscal, true);

		Query query = criarQueryComParametrosObterNotasFiscaisCadastradas(hql, filtroConsultaNotaFiscal);

		return ((Long) query.uniqueResult()).intValue();
	}

	/**
	 * Obtém lista de razão social do fornecedores dos itens associados 
	 * as notas fiscais de entrada passadas por parâmetro. 
	 * 
	 * @param listaIdNotaFiscal
	 * 
	 * @return List<ItemDTO<Long, String>>
	 */
	@Override
	public List<ItemDTO<Long, String>> obterListaFornecedorNotaFiscal(List<Long> listaIdNotaFiscal){
		
		StringBuilder hql = new StringBuilder();

		hql.append(" select	")
		
		.append(" notaFiscal.id as key, ")
		.append(" f.juridica.razaoSocial as value ")
		
		.append(" from NotaFiscalEntradaFornecedor notaFiscal ")
		.append(" join notaFiscal.tipoNotaFiscal 	")
		.append(" join notaFiscal.itens i 			")
		.append(" join i.produtoEdicao pe			")
		.append(" join pe.produto p					")
		.append(" join p.fornecedores f				")
		
		.append(" where notaFiscal.id in (:listaIdNotaFiscal) ")
		
		.append(" group by notaFiscal.id, f.juridica.id, f.juridica.razaoSocial ");
		
		Query query = getSession().createQuery(hql.toString()).setResultTransformer(new AliasToBeanResultTransformer(ItemDTO.class));
		
		query.setParameterList("listaIdNotaFiscal", listaIdNotaFiscal);
		
		return query.list();
		
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<NotaFiscalEntradaFornecedor> obterNotasFiscaisCadastradas(FiltroConsultaNotaFiscalDTO filtroConsultaNotaFiscal) {

		String hql = getConsultaNotasFiscaisCadastradas(filtroConsultaNotaFiscal, false);
		
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

	private String getConsultaNotasFiscaisCadastradas(FiltroConsultaNotaFiscalDTO filtroConsultaNotaFiscal, boolean isCount) { 

		StringBuilder hql = new StringBuilder();

		if(isCount) {
			hql.append("select count( distinct notaFiscal.id )");
			
		}else {
			hql.append("select distinct(notaFiscal)");
			
		}
		
		   
		hql.append(" from NotaFiscalEntradaFornecedor notaFiscal ")

		   .append(" join notaFiscal.tipoNotaFiscal 	")
		   .append(" join notaFiscal.itens i 			")
		   .append(" join i.produtoEdicao pe			")
		   .append(" join pe.produto p					")
		   .append(" join p.fornecedores f				");
		
		String condicoes = "";
		
		PeriodoVO periodo = filtroConsultaNotaFiscal.getPeriodo();
		
		if (periodo.getDataInicial() != null && periodo.getDataFinal() != null) {

			condicoes += "".equals(condicoes) ? " where " : " and ";
			
			condicoes += " notaFiscal.dataEmissao between :dataInicio and :dataFim ";
		}

		if (filtroConsultaNotaFiscal.getIdTipoNotaFiscal() != null) {
			
			condicoes += "".equals(condicoes) ? " where " : " and ";
			
			condicoes += " notaFiscal.tipoNotaFiscal.id = :idTipoNotaFiscal ";
		} else {
			
			if (filtroConsultaNotaFiscal.getIdDistribuidor() != null) {
				
				condicoes += "".equals(condicoes) ? " where " : " and ";
			
				condicoes += " notaFiscal.tipoNotaFiscal.tipoAtividade = ";
				
				condicoes += " ( select distribuidor.tipoAtividade from Distribuidor distribuidor where distribuidor.id = :idDistribuidor ) ";
			}
		}

		if (filtroConsultaNotaFiscal.getIdFornecedor() != null) {

			condicoes += "".equals(condicoes) ? " where " : " and ";
			
			condicoes += " f.id = :idFornecedor ";
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
						orderByColumn += " f.juridica.razaoSocial ";
						break;
					case NOTA_RECEBIDA:
						orderByColumn += orderByColumn.equals("") ? "" : ",";
						orderByColumn += " notaFiscal.dataRecebimento ";
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
						orderByColumn += orderByColumn.equals("") ? "" : ",";
						orderByColumn += " notaFiscal.dataEmissao ";
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
		} else {
		
			if (filtroConsultaNotaFiscal.getIdDistribuidor() != null) {
				query.setParameter("idDistribuidor", filtroConsultaNotaFiscal.getIdDistribuidor());
			}
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
				   + " itemNotaFiscal.preco as precoVenda, "
				   + " itemNotaFiscal.qtde as quantidadeExemplares, " 
				   + " (itemNotaFiscal.qtde * itemNotaFiscal.preco) - (itemNotaFiscal.qtde * itemNotaFiscal.preco * itemNotaFiscal.desconto) as valorTotal, "
				   + " diferenca.qtde as sobrasFaltas, " 
				   + " diferenca.tipoDiferenca as tipoDiferenca, " 
				   + " itemNotaFiscal.desconto as desconto " 
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
		
			this.adicionar(notaFiscal);
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
		
		hql.append(" from NotaFiscalEntrada nf ");		
		
		boolean indAnd = false;
		
		boolean indWhere = false;
		
		if(filtroConsultaNotaFiscal.getNumeroNota()!=null) {
			
			if(!indWhere) {
				hql.append(" where ");
				indWhere = true;
			}
			
			hql.append(" nf.numero = :numero");
			
			indAnd = true;
			
		}
		
		if(filtroConsultaNotaFiscal.getSerie()!=null){
			
			if(!indWhere) {
				hql.append(" where ");
				indWhere = true;
			}
			
			if(indAnd) {
				hql.append(" and ");
			}
			
			hql.append(" nf.serie = :serie ");
			
			indAnd = true;
			
		}
		
		if(filtroConsultaNotaFiscal.getNomeFornecedor() != null && !filtroConsultaNotaFiscal.getNomeFornecedor().equals("-1")){
			
			if(!indWhere) {
				hql.append(" where ");
				indWhere = true;
			}
			
			if(indAnd) {
				hql.append(" and ");
			}
			
			hql.append(" nf.fornecedor.juridica.cnpj = :cnpj ");	
			
			indAnd = true;
			
			
		}
			
		if(filtroConsultaNotaFiscal.getChave() == null) {
			
			if(!indWhere) {
				hql.append(" where ");
				indWhere = true;
			}
			
			if(indAnd) {
				hql.append(" and ");
			}
			
			hql.append(" nf.chaveAcesso is null ");
			
			indAnd = true;
			
		} else {
			
			if(!indWhere) {
				hql.append(" where ");
				indWhere = true;
			}
			
			if(indAnd) {
				hql.append(" and ");
			}
			
			hql.append(" nf.chaveAcesso = :chaveAcesso  ");	
		}
		
		Query query = super.getSession().createQuery(hql.toString());
		
		if(filtroConsultaNotaFiscal.getNumeroNota()!=null) {
			query.setParameter("numero", filtroConsultaNotaFiscal.getNumeroNota());
		}
		
		if(filtroConsultaNotaFiscal.getSerie()!=null) {
			query.setParameter("serie", filtroConsultaNotaFiscal.getSerie());
		}
		
		if(filtroConsultaNotaFiscal.getNomeFornecedor() != null && !filtroConsultaNotaFiscal.getNomeFornecedor().equals("-1")){
			query.setParameter("cnpj", filtroConsultaNotaFiscal.getCnpj());
		}	
		
		if(filtroConsultaNotaFiscal.getChave() != null){
			query.setParameter("chaveAcesso", filtroConsultaNotaFiscal.getChave());
		}
		
		return query.list();
	}
	
}