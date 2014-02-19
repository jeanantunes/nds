package br.com.abril.nds.repository.impl;

import java.util.List;

import javax.sql.DataSource;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.CotaExemplaresDTO;
import br.com.abril.nds.dto.FornecedorExemplaresDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.filtro.FiltroViewNotaFiscalDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.estoque.EstoqueProduto;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.fiscal.TipoDestinatario;
import br.com.abril.nds.model.fiscal.nfe.NotaFiscalNds;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.NotaFiscalNdsRepository;
import br.com.abril.nds.repository.NotaFiscalRepository;

@Repository
public class NotaFiscalNdsRepositoryImpl extends AbstractRepositoryModel<NotaFiscalNds, Long> implements NotaFiscalNdsRepository {

	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private NotaFiscalRepository notaFiscalRepository;
	
	public NotaFiscalNdsRepositoryImpl() {
		super(NotaFiscalNds.class);
	}	
	

	@Override
	@SuppressWarnings("unchecked")
	public List<CotaExemplaresDTO> consultaCotaExemplaresSumarizados(FiltroViewNotaFiscalDTO filtro) {
		
		// OBTER COTA EXEMPLARES SUMARIZADOS
		StringBuilder hql = new StringBuilder("SELECT ");
		hql.append(" mec.cota.id as idCota, ");
		hql.append(" mec.cota.numeroCota as numeroCota, ");
		hql.append(" coalesce(pessoa.nomeFantasia, pessoa.razaoSocial, pessoa.nome,'') as nomeCota,");
		hql.append(" SUM(mec.qtde) as exemplares, ");
		hql.append(" SUM(mec.valoresAplicados.precoVenda * mec.qtde) as total, "); 
		hql.append(" SUM(mec.valoresAplicados.precoComDesconto * mec.qtde) as totalDesconto"); 	
		
		Query query = queryConsultaNfeParameters(queryConsultaNfe(filtro, hql, false, false, false), filtro);
				
		if(filtro.getPaginacaoVO()!=null) {
			if(filtro.getPaginacaoVO().getPosicaoInicial()!=null) {
				query.setFirstResult(filtro.getPaginacaoVO().getPosicaoInicial());
			}

			if(filtro.getPaginacaoVO().getQtdResultadosPorPagina()!=null) {
				query.setMaxResults(filtro.getPaginacaoVO().getQtdResultadosPorPagina());
			}
		}
		
		query.setResultTransformer(new AliasToBeanResultTransformer(CotaExemplaresDTO.class));
		
		return query.list();
	}
	
	@Override
	public Long consultaCotaExemplaresSumarizadosQtd(FiltroViewNotaFiscalDTO filtro) {
		
		// OBTER COTA EXEMPLARES SUMARIZADOS
		StringBuilder hql = new StringBuilder("SELECT ");
		hql.append(" COUNT(mec.cota.id) ");
		Query query = queryConsultaNfeParameters(queryConsultaNfe(filtro, hql, true, true, false), filtro);
		
		return (long) query.list().size();
	}
	
	/**
	 * Obter conjunto de cotas
	 * @param FiltroViewNotaFiscalDTO
	 * @return List ids
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Cota> obterConjuntoCotasNotafiscal(FiltroViewNotaFiscalDTO filtro) {
		
		// OBTER ID DE TODAS AS COTAS DA TELA
		StringBuilder hql = new StringBuilder("SELECT ");
		hql.append(" mec.cota ");
		Query query = queryConsultaNfeParameters(queryConsultaNfe(filtro, hql, false, false, false), filtro);
		
		return query.list();
	}
	
	/**
	 * Obter os itens da nota com base nos movimentos de estoque cota
	 * 
	 * @param filtro
	 * @return
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<MovimentoEstoqueCota> obterMovimentosEstoqueCota(FiltroViewNotaFiscalDTO filtro) {
	
		// ITENS DA NOTA FISCAL
		StringBuilder hql = new StringBuilder("SELECT mec");
		Query query = queryConsultaNfeParameters(queryConsultaNfe(filtro, hql, false, false, true), filtro);
		
		return query.list();
	}
	
	private StringBuilder queryConsultaNfe(FiltroViewNotaFiscalDTO filtro, StringBuilder hql, boolean isCount, boolean isPagination, boolean isGroup) {
		
		hql.append(" FROM MovimentoEstoqueCota mec ")
		.append(" JOIN mec.tipoMovimento tipoMovimento ")
		.append(" JOIN mec.lancamento lancamento ")
		.append(" JOIN mec.cota cota ")
		.append(" JOIN cota.pessoa pessoa ")
		.append(" LEFT JOIN cota.box box ")
		.append(" LEFT JOIN box.roteirizacao roteirizacao ")
		.append(" LEFT JOIN roteirizacao.roteiros roteiro ")
		.append(" LEFT JOIN roteiro.rotas rota ")
		.append(" JOIN mec.produtoEdicao produtoEdicao")
		.append(" JOIN produtoEdicao.produto produto ")
		.append(" JOIN produto.fornecedores fornecedor")
		.append(" WHERE mec.data BETWEEN :dataInicial AND :dataFinal ")
		.append(" AND mec.movimentoEstoqueCotaEstorno is null ")
		.append(" AND mec.movimentoEstoqueCotaFuro is null ")
		.append(" AND mec.notaFiscalEmitida = false ");
		
		// Tipo de Nota:		
		if(filtro.getIdNaturezaOperacao() != null) {
			hql.append(" AND mec.tipoMovimento.id in (SELECT tm.id ");
			hql.append("FROM NaturezaOperacao no ");
			hql.append("JOIN no.tipoMovimento tm ");
			hql.append("WHERE no.id in(:tipoNota)) ");
		}
		
		// Data Emissão:	...		
		if(filtro.getDataEmissao() != null) {
			hql.append(" ");
		}
		
		// Cota:		
		if(filtro.getIdCota() != null) {
			hql.append(" AND cota.id = :cotaId ");
		}
		
		// Intervalo de Cota:
		if(filtro.getIntervalorCotaInicial() != null && filtro.getIntervalorCotaFinal() != null) {
			hql.append(" AND cota.numeroCota BETWEEN :numeroCotaInicial AND :numeroCotaFinal ");
		}
		
		// Roteiro:
		if(filtro.getIdRoteiro() != null) {
			hql.append(" AND roteiro.id = :roteiroId ");
		}
		
		// Rota:		
		if(filtro.getIdRota() != null) {
			hql.append(" AND rota.id = :rotaId ");
		}
		
		// Cota de:	 Até   
		if(filtro.getIntervaloBoxInicial() != null && filtro.getIntervaloBoxFinal() != null) {
			hql.append(" AND box.codigo between :codigoBoxInicial AND :codigoBoxFinal ");
		}
		
		if(filtro.getListIdFornecedor() != null) {
			hql.append(" AND fornecedor.id in (:fornecedor) ");
		}
		
		if(!isGroup){
			hql.append(" GROUP BY mec.cota.numeroCota ");
		}
		
		if(!isCount && !isPagination){
			if(filtro.getPaginacaoVO()!=null && filtro.getPaginacaoVO().getSortOrder() != null && filtro.getPaginacaoVO().getSortColumn() != null) {
				hql.append(" ORDER BY  ").append(filtro.getPaginacaoVO().getSortColumn()).append(" ").append(filtro.getPaginacaoVO().getSortOrder());
			}
		}
		
		return hql;
	}
	
	public Query queryConsultaNfeParameters(StringBuilder hql, FiltroViewNotaFiscalDTO filtro) {
		

		// Realizar a consulta e converter ao objeto cota exemplares.
		Query query = this.getSession().createQuery(hql.toString());		
		
		// Data Movimento:	...  Até   ...
		if (filtro.getDataInicial() != null && filtro.getDataFinal() != null) {
			query.setParameter("dataInicial", filtro.getDataInicial());
			query.setParameter("dataFinal", filtro.getDataFinal());
		}
		
		// tipo da nota fiscal		
		if(filtro.getIdNaturezaOperacao() != null && filtro.getIdNaturezaOperacao().longValue() > 0) {
			query.setParameter("tipoNota", filtro.getIdNaturezaOperacao());
		}
		
		// forncedor id		
		if(filtro.getListIdFornecedor() !=null && !filtro.getListIdFornecedor().isEmpty()) {
			query.setParameterList("fornecedor", filtro.getListIdFornecedor());
		}
		// Data Emissão:	...		
		/*if(filtro.getDataEmissao() != null) {
			
		}*/
		
		if(filtro.getIdCota() != null) {
			query.setParameter("cotaId", filtro.getIdCota());
		}
		
		if(filtro.getIntervalorCotaInicial() != null && filtro.getIntervalorCotaFinal() != null) {
			query.setParameter("numeroCotaInicial", filtro.getIntervalorCotaInicial());
			query.setParameter("numeroCotaFinal", filtro.getIntervalorCotaFinal());
		}
		
		// Roteiro:
		if(filtro.getIdRoteiro() != null) {
			query.setParameter("roteiroId", filtro.getIdRoteiro());
		}
		
		// Rota:		
		if(filtro.getIdRota() != null) {
			query.setParameter("rotaId", filtro.getIdRota());
		}
		
		// Cota de:	 Até   
		if(filtro.getIntervaloBoxInicial() != null && filtro.getIntervaloBoxFinal() != null) {
			query.setParameter("codigoBoxInicial", filtro.getIntervaloBoxInicial());
			query.setParameter("codigoBoxFinal", filtro.getIntervaloBoxFinal());
		}
		
		if(filtro.getListIdFornecedor() != null) {
			query.setParameterList("fornecedor", filtro.getListIdFornecedor());
		}
		
		return query;	
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ItemDTO<Long, String>> obterNaturezasOperacoesPorTipoDestinatario(TipoDestinatario tipoDestinatario) {
		
		StringBuilder sql = new StringBuilder("")
			.append("SELECT id as `key`, descricao as value ") 
			.append("FROM natureza_operacao no ")
			.append("WHERE no.TIPO_ATIVIDADE = (select TIPO_ATIVIDADE from distribuidor) ");
		
		if(tipoDestinatario != null) {
			sql.append("AND no.TIPO_DESTINATARIO = :tipoDestinatario ");
		}

		SQLQuery sqlQuery = getSession().createSQLQuery(sql.toString());
		
		if(tipoDestinatario != null) {
			sqlQuery.setParameter("tipoDestinatario", tipoDestinatario.name());
		}
		
		sqlQuery.setResultTransformer(new AliasToBeanResultTransformer(ItemDTO.class));

		return sqlQuery.list();
		
	}

	@Override
	@SuppressWarnings("unchecked")	
	public List<EstoqueProduto> obterConjuntoFornecedorNotafiscal(FiltroViewNotaFiscalDTO filtro) {

		StringBuilder hql = new StringBuilder("select ")
		.append(" estoqueProduto")
		.append(" from EstoqueProduto estoqueProduto ")
		.append(" JOIN estoqueProduto.produtoEdicao produtoEdicao")
		.append(" JOIN produtoEdicao.produto produto ")
		.append(" JOIN produto.fornecedores fornecedor ")
		.append(" JOIN fornecedor.juridica pj ")
		.append(" where fornecedor.id in (:idFornecedor) ");
		
		hql.append("GROUP BY fornecedor.id");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameterList("idFornecedor", filtro.getListIdFornecedor());
		
		return query.list();
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<FornecedorExemplaresDTO> consultaFornecedorExemplarSumarizado(FiltroViewNotaFiscalDTO filtro) {
		
		StringBuilder hql = new StringBuilder("select ")
		.append(" fornecedor.id AS idFornecedor, ")
		.append(" fornecedor.codigoInterface as numeroFornecedor, ")
		.append(" pj.razaoSocial as nomeFornecedor, ")
		.append(" SUM(estoqueProduto.qtdeDevolucaoEncalhe) as exemplares, ")
		.append(" SUM(estoqueProduto.qtde) as total, ")
		.append(" SUM(estoqueProduto.qtde) as totalDesconto ")
		.append(" from EstoqueProduto as estoqueProduto ")
		.append(" JOIN estoqueProduto.produtoEdicao as produtoEdicao")
		.append(" JOIN produtoEdicao.produto as produto ")
		.append(" JOIN produto.fornecedores as fornecedor ")
		.append(" JOIN fornecedor.juridica as pj ")
		.append(" where fornecedor.id in (:idFornecedor) ");
		
		hql.append("GROUP BY fornecedor.id");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameterList("idFornecedor", filtro.getListIdFornecedor());
		
		query.setResultTransformer(new AliasToBeanResultTransformer(FornecedorExemplaresDTO.class));
		
		return query.list();
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<EstoqueProduto> obterEstoques(FiltroViewNotaFiscalDTO filtro) {
		StringBuilder hql = new StringBuilder("select estoqueProduto ");
		
		Query query =  queryConsultaNfeEstoqueParameters(queryConsultaNfeEstoque(filtro, hql, true, true, true), filtro);
		
		hql.append("GROUP BY fornecedor.id");
		
		return query.list();
	}
	
	
	@Override
	public Long consultaFornecedorExemplaresSumarizadosQtd(FiltroViewNotaFiscalDTO filtro) {
		
		// OBTER COTA EXEMPLARES SUMARIZADOS
		StringBuilder hql = new StringBuilder("SELECT ");
		hql.append(" COUNT(fornecedor.id) ");
		Query query = queryConsultaNfeEstoqueParameters(queryConsultaNfeEstoque(filtro, hql, true, true, false), filtro);
		
		return (long) query.list().size();
	}
	
	public Query queryConsultaNfeEstoqueParameters(StringBuilder hql, FiltroViewNotaFiscalDTO filtro) {

		Query query = this.getSession().createQuery(hql.toString());		
		
		query.setParameterList("idFornecedor", filtro.getListIdFornecedor());
		
		return query;
	}
	
	public StringBuilder queryConsultaNfeEstoque(FiltroViewNotaFiscalDTO filtro, StringBuilder hql, boolean isCount, boolean isPagination, boolean isGroup){
		
		hql.append(" from EstoqueProduto as estoqueProduto ")
		.append(" JOIN estoqueProduto.produtoEdicao as produtoEdicao")
		.append(" JOIN produtoEdicao.produto as produto ")
		.append(" JOIN produto.fornecedores as fornecedor ")
		.append(" JOIN fornecedor.juridica pj ")
		.append(" where fornecedor.id in (:idFornecedor) ");
		
		return hql;
	}
}
