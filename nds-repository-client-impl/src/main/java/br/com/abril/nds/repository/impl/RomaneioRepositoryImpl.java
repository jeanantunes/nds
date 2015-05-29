package br.com.abril.nds.repository.impl;

import java.util.Arrays;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.RomaneioDTO;
import br.com.abril.nds.dto.filtro.FiltroRomaneioDTO;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.TipoEstudoCota;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.RomaneioRepository;

@Repository
public class RomaneioRepositoryImpl extends AbstractRepositoryModel<Box, Long> implements RomaneioRepository {

	/** Quantidade máxima de produtos exibidos no relatório.*/
	private static final int QUANTIDADE_MAX_PRODUTOS_POR_RELATORIO = 6;
	
	public RomaneioRepositoryImpl() {
		super(Box.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RomaneioDTO> buscarRomaneios(FiltroRomaneioDTO filtro, boolean limitar) {
		
		Query query = this.createQueryBuscarRomaneio(filtro, true);
		query.setResultTransformer(new AliasToBeanResultTransformer(RomaneioDTO.class));
		
		// Realiza a paginação:
		if (limitar) {
			
			if (filtro.getPaginacao().getPosicaoInicial() != null) {
				query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
			}
			
			if (filtro.getPaginacao().getQtdResultadosPorPagina() != null) {
				query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
			}
		}
		
		return  query.list();
		//return  popularEndereco(query.list());
	}
	
	
	/**
	 * Gera a consulta que pesquisa os romaneios com os critérios definidos
	 * pelo usuário.
	 * 
	 * @param filtro
	 * @param ordenarConsulta
	 * 
	 * @return
	 */
	private Query createQueryBuscarRomaneio(FiltroRomaneioDTO filtro, boolean ordenarConsulta) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select ");
		hql.append("	pdvs_.ID as idPDV, "); 
		hql.append("	notaenvio_.NUMERO_COTA as numeroCota, ");
		hql.append("    notaenvio_.NOME_DESTINATARIO as nome, ");
		hql.append("    cota_.ID as idCota, ");
		hql.append("    notaenvio_.numero as numeroNotaEnvio, ");
		hql.append("    case  ");
		hql.append("         when roteiro_.TIPO_ROTEIRO<>'ESPECIAL' then box_.ID  ");
		hql.append("       else -1  ");
		hql.append("    end as idBox, ");
		hql.append("    case ");
		hql.append("         when roteiro_.TIPO_ROTEIRO<>'ESPECIAL' then box_.NOME ");
		hql.append("         else 'ESPECIAL' ");
		hql.append("    end as nomeBox,");
		hql.append("    roteiro_.ID as idRoteiro,");
		hql.append("    roteiro_.DESCRICAO_ROTEIRO as nomeRoteiro,");
		hql.append("    rota_.ID as idRota,");
		hql.append("    rota_.DESCRICAO_ROTA as nomeRota,");
		hql.append("    concat(endereco_.TIPO_LOGRADOURO,' ',endereco_.LOGRADOURO,', ',endereco_.NUMERO,', ',endereco_.BAIRRO,', ',endereco_.CIDADE,', ',endereco_.UF,' - ',endereco_.CEP) as endereco "); 
		
		hql.append(getSqlFromEWhereRomaneio(filtro));
		
		if (ordenarConsulta) {
			
			hql.append(getOrderBy(filtro, true));
		}
		
		Query query =  getSession().createSQLQuery(hql.toString())
				.addScalar("idPDV",StandardBasicTypes.LONG)
				.addScalar("numeroCota",StandardBasicTypes.INTEGER)
				.addScalar("nome",StandardBasicTypes.STRING)
				.addScalar("idCota",StandardBasicTypes.LONG)
				.addScalar("numeroNotaEnvio",StandardBasicTypes.LONG)
				.addScalar("idBox",StandardBasicTypes.LONG)
				.addScalar("nomeBox",StandardBasicTypes.STRING)
				.addScalar("idRoteiro",StandardBasicTypes.LONG)
				.addScalar("nomeRoteiro",StandardBasicTypes.STRING)
				.addScalar("idRota",StandardBasicTypes.LONG)
				.addScalar("nomeRota",StandardBasicTypes.STRING)
				.addScalar("endereco",StandardBasicTypes.STRING);
		this.setarParametrosRomaneio(filtro, query);
		
		return query;
	}
	
	private String getSqlFromEWhereRomaneio(FiltroRomaneioDTO filtro) {
		
		StringBuilder hql = new StringBuilder();

		hql.append(" from LANCAMENTO lancamento_                                                                   ");
		hql.append(" INNER join PRODUTO_EDICAO pe_ on lancamento_.PRODUTO_EDICAO_ID =  pe_.ID                      ");
		hql.append(" INNER join ESTUDO estudo_ on estudo_.ID = lancamento_.ESTUDO_ID                               ");
		hql.append(" INNER join ESTUDO_COTA estudo_cota_ on estudo_.ID = estudo_cota_.ESTUDO_ID                    ");
		hql.append(" INNER join COTA cota_ on cota_.ID=estudo_cota_.COTA_ID                                        ");
		hql.append(" left outer join BOX box_ on cota_.BOX_ID=box_.ID                                              ");
		hql.append(" INNER JOIN PDV pdvs_ on pdvs_.COTA_ID = cota_.ID                                             ");
		hql.append(" left outer join ESTUDO_PDV estpdv_ on pdvs_.ID = estpdv_.PDV_ID and cota_.ID = estpdv_.cota_id and estpdv_.ESTUDO_ID = estudo_.ID                               ");
		hql.append(" INNER JOIN ENDERECO_PDV epdv on epdv.PDV_ID = pdvs_.id                                        ");
		hql.append(" INNER JOIN ENDERECO endereco_ on epdv.ENDERECO_ID = endereco_.ID                              ");
		hql.append(" INNER JOIN ROTA_PDV rotas_ on pdvs_.ID=rotas_.PDV_ID                                          ");
		hql.append(" INNER JOIN ROTA rota_ on rotas_.ROTA_ID=rota_.ID                                              ");
		hql.append(" INNER join ROTEIRO roteiro_ on rota_.ROTEIRO_ID=roteiro_.ID                                   ");
		hql.append(" INNER JOIN NOTA_ENVIO notaenvio_ on notaenvio_.NUMERO_COTA=cota_.NUMERO_COTA                  ");
		hql.append(" INNER JOIN NOTA_ENVIO_ITEM itemNotaEnvio_ on notaenvio_.numero=itemNotaEnvio_.NOTA_ENVIO_ID   ");
		hql.append(" where ");
		hql.append(" cota_.NUMERO_COTA=notaenvio_.NUMERO_COTA ");
		hql.append(" and epdv.PRINCIPAL = :pontoPrincipal and pdvs_.ponto_principal = :pontoPrincipal ");
		hql.append(" and lancamento_.PRODUTO_EDICAO_ID=itemNotaEnvio_.PRODUTO_EDICAO_ID "); 
		hql.append(" and cota_.SITUACAO_CADASTRO <> :situacaoInativo ");
		hql.append(" and lancamento_.STATUS not in (:statusLancamento) ");
		hql.append(" and estudo_cota_.TIPO_ESTUDO != :juramentado ");
		
		if(filtro.getIdBox() == null) {
			hql.append(" and box_.ID is not null ");
			hql.append(" and roteiro_.TIPO_ROTEIRO <> 'ESPECIAL' ");
			
		} else {
			
			if(filtro.getIdBox() <= 0){
				hql.append(" and roteiro_.TIPO_ROTEIRO  = 'ESPECIAL' ");
			}else{
				hql.append(" and box_.ID = :idBox ");
				hql.append(" and roteiro_.TIPO_ROTEIRO  <> 'ESPECIAL' ");
			}
		}
			
		if(filtro.getIdRoteiro() != null){
			
			hql.append( " and roteiro_.id = :idRoteiro ");
		}
		
		if(filtro.getIdRota() != null){
			
			hql.append( " and rota_.id = :idRota ");
		}
		
		if(filtro.getData() != null){
			
			hql.append(" and lancamento_.DATA_LCTO_DISTRIBUIDOR = :data ");
		}
		
		if (filtro.getProdutos() != null && !filtro.getProdutos().isEmpty()){
			
			hql.append(" and lancamento_.PRODUTO_EDICAO_ID in (:produtos) ");
		}
		
		hql.append(" group by pdvs_.ID, box_.ID, notaenvio_.NUMERO, notaenvio_.NUMERO_COTA, concat(endereco_.TIPO_LOGRADOURO,' ',endereco_.LOGRADOURO,', ',endereco_.NUMERO,', ',endereco_.BAIRRO,', ',endereco_.CIDADE,', ',endereco_.UF,' - ',endereco_.CEP) ");

		return hql.toString();
	}
	
	/**
	 * Incluir uma virgula (separador) para os critérios de order by.<br>
	 * Caso não haja consulta HQL não incluirá nada, retornando o próprio
	 * parâmetro.
	 * 
	 * @param hql
	 * @return
	 */
	private StringBuilder addSeparadorOrderBy(StringBuilder hql) {
		if (hql != null && hql.length() > 0) {
			hql.append(", ");
		}
		
		return hql;
	}
	
	private String getOrderBy(FiltroRomaneioDTO filtro, boolean isImpressao) {
		
		if(filtro.getPaginacao() == null || filtro.getPaginacao().getSortColumn() == null) {
			
			return "";
		}
		
		StringBuilder hql = new StringBuilder();
		
		if ("numeroCota".equals(filtro.getPaginacao().getSortColumn())) {
			hql = addSeparadorOrderBy(hql);
			hql.append(" cota_.numero_cota ");
		} else if ("nome".equals(filtro.getPaginacao().getSortColumn())) {
			hql = addSeparadorOrderBy(hql);
			hql.append(" notaenvio_.NOME_DESTINATARIO ");
		} else if("numeroNotaEnvio".equals(filtro.getPaginacao().getSortColumn())) {
			hql = addSeparadorOrderBy(hql);
			hql.append(" notaenvio_.numero ");
		} else {
			hql.append(" box_.codigo asc, roteiro_.ordem asc, rotas_.ordem ");
		}
		
		if (hql.length() > 0 && filtro.getPaginacao().getOrdenacao() != null) {
			hql.append(filtro.getPaginacao().getOrdenacao().toString());
		}

		if (hql.length() > 0) {
			hql.insert(0, " order by ");
		}
		
		return hql.toString();
	}
	
	private void setarParametrosRomaneio(FiltroRomaneioDTO filtro, Query query) {
		
		query.setParameter("situacaoInativo", SituacaoCadastro.INATIVO.name());
		
		query.setParameter("pontoPrincipal", true);
		
		query.setParameter("juramentado", TipoEstudoCota.JURAMENTADO.name());
	
		query.setParameterList(
			"statusLancamento",Arrays.asList(StatusLancamento.PLANEJADO.name(), 
					StatusLancamento.CONFIRMADO.name(),
					StatusLancamento.FURO.name(),
					StatusLancamento.CANCELADO.name()));
		
		if(filtro.getIdBox() != null && filtro.getIdBox() > 0) {
			
			query.setParameter("idBox", filtro.getIdBox());
		}
		
		if(filtro.getIdRoteiro() != null){
			
			query.setParameter("idRoteiro", filtro.getIdRoteiro());
		}
		
		if(filtro.getIdRota() != null){
			
			query.setParameter("idRota", filtro.getIdRota());
		}
		
		if (filtro.getData() != null){
			
			query.setParameter("data", filtro.getData());
		}
		
		if (filtro.getProdutos() != null && !filtro.getProdutos().isEmpty()){
		
			query.setParameterList("produtos", filtro.getProdutos());
		}
	}

	/**
	 * Preenche os parâmetros para exibir os romaneios para exportação.
	 * 
	 * @param filtro
	 * @param query
	 * @param qtdProdutos
	 */
	private void setarParametrosRomaneioParaExportacao(FiltroRomaneioDTO filtro,
			Query query, int qtdProdutos) {
		
		this.setarParametrosRomaneio(filtro, query);
		
		// Cenário em que o usuário selecionou dois ou mais produtos:
		if (qtdProdutos > 1) {
			for (int index = 0; index < qtdProdutos; index++) {
				query.setParameter("idProdutoEdicao" + index, filtro.getProdutos().get(index));
			}
		}
	}
	
	@Override
	public Integer buscarTotal(FiltroRomaneioDTO filtro, boolean countCotas) {
		
		Number totalRegistros = null;
		
		StringBuilder hql = new StringBuilder();
			
		hql.append("select count( count.idPDV ) from ");
		hql.append(" (select pdvs_.ID as idPDV ");
		hql.append(getSqlFromEWhereRomaneio(filtro));
		hql.append(" ) as count ");
			
		Query query =  getSession().createSQLQuery(hql.toString());
		
		this.setarParametrosRomaneio(filtro, query);
			
		totalRegistros = (Number) query.uniqueResult();
		
		return totalRegistros.intValue();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<RomaneioDTO> buscarRomaneiosParaExportacao(FiltroRomaneioDTO filtro) {
		
		Query query = this.createQueryBuscarRomaneioParaExportacao(filtro);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(RomaneioDTO.class));
		
		return query.list();
	}

	/**
	 * Gera a consulta que pesquisa os romaneios com os critérios definidos
	 * pelo usuário.
	 * 
	 * @param filtro
	 * @param ordenarConsulta
	 * 
	 * @return
	 */
	private Query createQueryBuscarRomaneioParaExportacao(FiltroRomaneioDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select ");
		hql.append("	pdvs_.ID as idPDV, "); 
		hql.append("	notaenvio_.NUMERO_COTA as numeroCota, ");
		hql.append("    notaenvio_.NOME_DESTINATARIO as nome, ");
		hql.append("    cota_.ID as idCota, ");
		hql.append("    notaenvio_.numero as numeroNotaEnvio, ");
		hql.append("    case  ");
		hql.append("         when roteiro_.TIPO_ROTEIRO<>'ESPECIAL' then box_.ID  ");
		hql.append("       else -1  ");
		hql.append("    end as idBox, ");
		hql.append("    case ");
		hql.append("         when roteiro_.TIPO_ROTEIRO<>'ESPECIAL' then box_.NOME ");
		hql.append("         else 'ESPECIAL' ");
		hql.append("    end as nomeBox,");
		hql.append("    roteiro_.ID as idRoteiro,");
		hql.append("    roteiro_.DESCRICAO_ROTEIRO as nomeRoteiro,");
		hql.append("    rota_.ID as idRota,");
		hql.append("    rota_.DESCRICAO_ROTA as nomeRota,");
		hql.append("    concat(endereco_.TIPO_LOGRADOURO,' ',endereco_.LOGRADOURO,', ',endereco_.NUMERO,', ',endereco_.BAIRRO,', ',endereco_.CIDADE,', ',endereco_.UF,' - ',endereco_.CEP) as endereco"); 
		
		int qtdProdutos = 0;
		if (filtro.getProdutos() != null && !filtro.getProdutos().isEmpty()) {
		
			if (filtro.getProdutos().size() == 1) {
				// 
				hql.append(",round(estudo_cota_.REPARTE / pe_.PACOTE_PADRAO) as pacote ");
				hql.append(",mod(estudo_cota_.REPARTE, pe_.PACOTE_PADRAO) as quebra ");
				hql.append(",COALESCE(estpdv_.REPARTE, estudo_cota_.REPARTE) as reparteTotal ");
				
			} else {
				
				// Exibir a quantidade de reparte de 'n' produtos:
				qtdProdutos = Math.min(filtro.getProdutos().size(), QUANTIDADE_MAX_PRODUTOS_POR_RELATORIO);
				for (int index = 0; index < qtdProdutos; index++) {
					
					hql.append(",COALESCE((SELECT SUM(COALESCE(estudoPDV.REPARTE, estudoCota.REPARTE)) FROM estudo e  ");
					hql.append(" LEFT JOIN estudo_pdv estudoPDV                                           ");
					hql.append(" on estudoPDV.ESTUDO_ID = e.ID                                            ");
					hql.append(" LEFT join estudo_cota estudoCota                                         ");
					hql.append(" on estudoCota.estudo_id = e.ID                                           ");
					hql.append(" where e.PRODUTO_EDICAO_ID =:idProdutoEdicao").append(index);
					hql.append(" and estudoCota.COTA_ID = cota_.ID ");
					// hql.append(" and estudoPDV.COTA_ID  = cota_.ID ");
					// hql.append(" and estudoPDV.PDV_ID = rotas_.PDV_ID ");
					hql.append("),0) as qtdProduto").append(index);
					/*
					hql.append(",coalesce((select estudoPDVSub.REPARTE ");
					hql.append(" from estudo_pdv estudoPDVSub join estudo_gerado est on est.ID = estudoPDVSub.ESTUDO_ID ");
					hql.append(" where est.PRODUTO_EDICAO_ID =:idProdutoEdicao").append(index);
					hql.append(" and estudoPDVSub.COTA_ID = cota_.ID ");
					hql.append(" and estudoPDVSub.PDV_ID = rotas_.PDV_ID ");
					hql.append("),0) as qtdProduto").append(index);*/
				}
			}
		}
		
		hql.append(getSqlFromEWhereRomaneio(filtro));
		hql.append("order by nomeBox asc, roteiro_.ordem asc, rota_.ordem asc,rotas_.ordem  "); // para romaneio, classificacao fixa
		SQLQuery query =  getSession().createSQLQuery(hql.toString())
				.addScalar("idPDV",StandardBasicTypes.LONG)
				.addScalar("numeroCota",StandardBasicTypes.INTEGER)
				.addScalar("nome",StandardBasicTypes.STRING)
				.addScalar("idCota",StandardBasicTypes.LONG)
				.addScalar("numeroNotaEnvio",StandardBasicTypes.LONG)
				.addScalar("idBox",StandardBasicTypes.LONG)
				.addScalar("nomeBox",StandardBasicTypes.STRING)
				.addScalar("idRoteiro",StandardBasicTypes.LONG)
				.addScalar("nomeRoteiro",StandardBasicTypes.STRING)
				.addScalar("idRota",StandardBasicTypes.LONG)
				.addScalar("nomeRota",StandardBasicTypes.STRING)
				.addScalar("endereco",StandardBasicTypes.STRING);
		
		if(filtro.getProdutos() != null && !filtro.getProdutos().isEmpty()){
			
			if(filtro.getProdutos().size() == 1) {
				
				query.addScalar("pacote",StandardBasicTypes.BIG_INTEGER)
					 .addScalar("quebra",StandardBasicTypes.BIG_INTEGER)
					 .addScalar("reparteTotal",StandardBasicTypes.BIG_INTEGER);
			} else {
				
				qtdProdutos = Math.min(filtro.getProdutos().size(), QUANTIDADE_MAX_PRODUTOS_POR_RELATORIO);
				
				for (int index = 0; index < qtdProdutos; index++) {
					
					String nomeProduto  = "qtdProduto"+index;
					query.addScalar(nomeProduto,StandardBasicTypes.BIG_INTEGER);
				}
				
			}
		}
		
		this.setarParametrosRomaneioParaExportacao(filtro, query, qtdProdutos);
		
		return query;
	}	
}