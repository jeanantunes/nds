package br.com.abril.nds.repository.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.ResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.DetalheItemNotaFiscalDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.NotaFiscalEntradaFornecedorDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaNotaFiscalDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaNotaFiscalDTO.ColunaOrdenacao;
import br.com.abril.nds.model.Origem;
import br.com.abril.nds.model.fiscal.NotaFiscalEntrada;
import br.com.abril.nds.model.fiscal.NotaFiscalEntradaFornecedor;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.NotaFiscalEntradaRepository;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.PeriodoVO;

@Repository
public class NotaFiscalEntradaRepositoryImpl extends AbstractRepositoryModel<NotaFiscalEntrada, Long> implements NotaFiscalEntradaRepository {

	public NotaFiscalEntradaRepositoryImpl() {
		super(NotaFiscalEntrada.class);
	}

	public Integer obterQuantidadeNotasFicaisCadastradas(FiltroConsultaNotaFiscalDTO filtroConsultaNotaFiscal) {

		String hql = getConsultaNotasFiscaisCadastradas(filtroConsultaNotaFiscal, true, false);

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

		String hql = getConsultaNotasFiscaisCadastradas(filtroConsultaNotaFiscal, false, false);
		
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

	@Override
	public List<NotaFiscalEntradaFornecedorDTO> obterNotasFiscaisCadastradasDTO(
			FiltroConsultaNotaFiscalDTO filtroConsultaNotaFiscal) {

		String hql = getConsultaNotasFiscaisCadastradas(filtroConsultaNotaFiscal, false, true);

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
	
	
	private String getConsultaNotasFiscaisCadastradas(FiltroConsultaNotaFiscalDTO filtroConsultaNotaFiscal, boolean isCount, boolean isDTO) { 

		StringBuilder hql = new StringBuilder();

		if (isDTO) {

			hql.append("select DISTINCT new ").append(NotaFiscalEntradaFornecedorDTO.class.getCanonicalName())
				.append(" (notaFiscal.id, ")
				.append("  notaFiscal.numero, ")
				.append("  notaFiscal.serie, ")
				.append("  notaFiscal.numeroNotaEnvio, ")
				.append("  notaFiscal.dataEmissao, ")
				.append("  notaFiscal.dataExpedicao, ")
				.append("  naturezaOperacao.descricao, ")
				.append("  notaFiscal.valorBruto as valorTotalNota, ")
				.append("  (notaFiscal.valorBruto - coalesce(notaFiscal.valorDesconto,0)) as valorTotalNotaComDesconto, ")
				.append("  notaFiscal.statusNotaFiscal, ")
				.append("  notaFiscal.dataRecebimento, ")
				.append("  notaFiscal.emitente.razaoSocial,")
				.append("  notaFiscal.chaveAcesso ) ");

		} else {
			if(isCount) {
				hql.append("select count( distinct notaFiscal.id )");
				
			}else {
				hql.append("select distinct(notaFiscal)");
				
			}
		}
		
		hql.append(" from NotaFiscalEntradaFornecedor notaFiscal ")

		   .append(" join notaFiscal.naturezaOperacao naturezaOperacao	")
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
			
			condicoes += " naturezaOperacao.id = :naturezaOperacaoId ";
		}

		if (filtroConsultaNotaFiscal.getIdFornecedor() != null) {

			condicoes += "".equals(condicoes) ? " where " : " and ";
			
			condicoes += " notaFiscal.emitente.id = ( select fncd.juridica.id from Fornecedor fncd where fncd.id = :idFornecedor ) ";
		}

		if (filtroConsultaNotaFiscal.getNotaRecebida() != null) {

			String condicaoNotaRecebida = null;
			
			switch(filtroConsultaNotaFiscal.getNotaRecebida()) {
				
				case SOMENTE_NOTAS_RECEBIDAS:
					
					condicaoNotaRecebida = " ( notaFiscal.dataRecebimento is not null and notaFiscal.statusNotaFiscal = 'RECEBIDA' ) ";
					
					break;
				case SOMENTE_NOTAS_NAO_RECEBIDAS:
					
					condicaoNotaRecebida = " ( notaFiscal.dataRecebimento is null and notaFiscal.statusNotaFiscal <> 'RECEBIDA' ) ";
					
					break;
				case NOTAS_NAO_RECEBIDAS_COM_NOTA_DE_ENVIO:
					
					condicaoNotaRecebida = " ((notaFiscal.numeroNotaEnvio is not null and notaFiscal.numeroNotaEnvio <> 0) and (notaFiscal.numero is null or notaFiscal.numero = 0)) ";
					
					break;
				default:
					break;
			}
			
			if (condicaoNotaRecebida != null) {

				condicoes += "".equals(condicoes) ? " where " : " and ";

				condicoes += condicaoNotaRecebida;
			}
		}

		hql.append(condicoes);

		PaginacaoVO paginacao = filtroConsultaNotaFiscal.getPaginacao();

		if (filtroConsultaNotaFiscal.getListaColunaOrdenacao() != null && 
				!filtroConsultaNotaFiscal.getListaColunaOrdenacao().isEmpty()) {

			hql.append(" order by ");
			
			String orderByColumn = "";
			
			for (ColunaOrdenacao colunaOrdenacao : filtroConsultaNotaFiscal.getListaColunaOrdenacao()) {

				switch (colunaOrdenacao) {
				
					case DATA_EMISSAO:
						orderByColumn += orderByColumn.equals("") ? "" : ",";
						orderByColumn += " notaFiscal.dataEmissao ";
						break;
					case SERIE:
						orderByColumn += orderByColumn.equals("") ? "" : ",";
						orderByColumn += " notaFiscal.serie ";
						break;
					case NUMERO_NOTA_ENVIO:
						orderByColumn += orderByColumn.equals("") ? "" : ",";
						orderByColumn += " notaFiscal.numeroNotaEnvio ";
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
						orderByColumn += " naturezaOperacao.descricao ";
						break;
					case VALOR:
						orderByColumn += orderByColumn.equals("") ? "" : ",";
						orderByColumn += " (notaFiscal.valorBruto) ";
						break;
					case VALOR_COM_DESCONTO:
						orderByColumn += orderByColumn.equals("") ? "" : ",";
						orderByColumn += " (notaFiscal.valorBruto - coalesce(notaFiscal.valorDesconto,0)) ";
						break;
					case CHAVE_ACESSO:
						orderByColumn += orderByColumn.equals("") ? "" : ",";
						orderByColumn += " notaFiscal.chaveAcesso ";
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
			
			query.setParameter("naturezaOperacaoId", filtroConsultaNotaFiscal.getIdTipoNotaFiscal());
		}
		
		if (filtroConsultaNotaFiscal.getIdFornecedor() != null) {
			
			query.setParameter("idFornecedor", filtroConsultaNotaFiscal.getIdFornecedor());
		}

		return query;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<DetalheItemNotaFiscalDTO> obterDetalhesNotaFical(Long idNotaFiscal, PaginacaoVO paginacao) {

		String hql = 
				" select "
				   + " itemNotaFiscal.id as codigoItem, "
				   + " itemNotaFiscal.produtoEdicao.produto.codigo as codigoProduto,"
				   + " itemNotaFiscal.produtoEdicao.produto.nome as nomeProduto, " 
				   + " itemNotaFiscal.produtoEdicao.numeroEdicao as numeroEdicao, "
				   + " itemNotaFiscal.preco as precoVenda, "
				   + " itemNotaFiscal.qtde as quantidadeExemplares, " 
				   
				   + " (itemNotaFiscal.preco * itemNotaFiscal.qtde) as valorTotal, "
				   
				   + " diferenca.qtde as sobrasFaltas, " 
				   + " diferenca.tipoDiferenca as tipoDiferenca, " 
				   + " itemNotaFiscal.desconto as desconto, " 
				   
				   + " (case when itemNotaFiscal.produtoEdicao.origem = :origemProdutoSemCadastro "
				   + " then true else false end "
				   + " ) as produtoSemCadastro "
				   
				   + " from ItemNotaFiscalEntrada itemNotaFiscal "
				   
				   + " left join itemNotaFiscal.recebimentoFisico.diferenca as diferenca "
				   + " where itemNotaFiscal.notaFiscal.id = :idNotaFiscal ";
				   
		if (paginacao != null) {
			hql += " order by " + paginacao.getSortColumn() + " " + paginacao.getSortOrder(); 
		}

		ResultTransformer resultTransformer = new AliasToBeanResultTransformer(DetalheItemNotaFiscalDTO.class); 

		Query query = getSession().createQuery(hql);

		query.setResultTransformer(resultTransformer);
		query.setParameter("idNotaFiscal", idNotaFiscal);
		
		//Trac 784
		query.setParameter("origemProdutoSemCadastro", Origem.PRODUTO_SEM_CADASTRO);
 
		return query.list();
	}

	@Override	
	public void inserirNotaFiscal(NotaFiscalEntrada notaFiscal){
		
			this.adicionar(notaFiscal);
	}
	
	/**
	 * Metodo para buscar nota com numero,serie, cnpj e chaveDeAcesso 
	 * @param filtroConsultaNotaFiscal
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<NotaFiscalEntrada> obterNotaFiscalEntrada(FiltroConsultaNotaFiscalDTO filtroConsultaNotaFiscal){
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
		
		if(filtroConsultaNotaFiscal.getNumeroNotaEnvio()!=null) {
			
			if(!indWhere) {
				hql.append(" where ");
				indWhere = true;
			}
			
			if(indAnd) {
				hql.append(" and ");
			}
			
			hql.append(" nf.numeroNotaEnvio = :numeroNotaEnvio");
			
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
			
			hql.append(" nf.emitente.cnpj = :cnpj ");	
			
			indAnd = true;
			
			
		}
			
		if(filtroConsultaNotaFiscal.getChave() != null) {
			
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

		if(filtroConsultaNotaFiscal.getNumeroNotaEnvio()!=null) {
			query.setParameter("numeroNotaEnvio", filtroConsultaNotaFiscal.getNumeroNotaEnvio());
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

	@Override
	public boolean existeNotaFiscalEntradaFornecedor(Long numeroNotaEnvio,
			Long idPessoaJuridica, Date dataEmissao) {
		
		StringBuilder hql = new StringBuilder("select count(n.id) ");
		hql.append(" from NotaFiscalEntrada n ")
		   .append(" where n.numeroNotaEnvio = :numeroNotaEnvio ")
		   .append(" and n.emitente.id = :idPessoaJuridica ")
		   .append(" and n.dataEmissao = :dataEmissao ");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("numeroNotaEnvio", numeroNotaEnvio);
		query.setParameter("idPessoaJuridica", idPessoaJuridica);
		query.setParameter("dataEmissao", dataEmissao);
		
		return (Long) query.uniqueResult() > 0;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Long> pesquisarItensNotaExpedidos(Long idNota) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select pe.id 						")
		   .append(" from Lancamento lancamento 		")
		   .append(" join lancamento.produtoEdicao pe 	")
		   .append(" join lancamento.recebimentos itensRecebimentos 		")
		   .append(" join itensRecebimentos.recebimentoFisico recebimento 	")
		   .append(" join recebimento.notaFiscal notaFiscal 				")		   
		   .append(" where notaFiscal.id = :idNota 							")
		   .append(" and lancamento.status not in (:status) 				")
		   .append(" group by pe.id 										");	

		StatusLancamento[] status = new StatusLancamento[]{StatusLancamento.PLANEJADO,StatusLancamento.CONFIRMADO,
				StatusLancamento.FURO,StatusLancamento.EM_BALANCEAMENTO, StatusLancamento.BALANCEADO,StatusLancamento.ESTUDO_FECHADO};
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("idNota", idNota);
		query.setParameterList("status", status);
		
		return query.list();
	}
	
}