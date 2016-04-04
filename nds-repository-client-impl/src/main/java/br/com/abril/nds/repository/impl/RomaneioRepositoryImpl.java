package br.com.abril.nds.repository.impl;

import java.util.Arrays;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ArquivoRotDTO;
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
		hql.append("    box_.ID as idBox, ");
		hql.append("    box_.NOME as nomeBox,");
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
		hql.append(" INNER JOIN PRODUTO_EDICAO pe_ on lancamento_.PRODUTO_EDICAO_ID =  pe_.ID                      ");
		hql.append(" INNER JOIN ESTUDO estudo_ on estudo_.ID = lancamento_.ESTUDO_ID                               ");
		hql.append(" INNER JOIN ESTUDO_COTA estudo_cota_ on estudo_.ID = estudo_cota_.ESTUDO_ID                    ");
		hql.append(" INNER JOIN COTA cota_ on cota_.ID=estudo_cota_.COTA_ID                                        ");
		hql.append(" INNER JOIN PDV pdvs_ on pdvs_.COTA_ID = cota_.ID                                             ");
		hql.append(" LEFT OUTER JOIN ESTUDO_PDV estpdv_ on pdvs_.ID = estpdv_.PDV_ID and cota_.ID = estpdv_.cota_id and estpdv_.ESTUDO_ID = estudo_.ID                               ");
		hql.append(" INNER JOIN ENDERECO_PDV epdv on epdv.PDV_ID = pdvs_.id                                        ");
		hql.append(" INNER JOIN ENDERECO endereco_ on epdv.ENDERECO_ID = endereco_.ID                              ");
		hql.append(" INNER JOIN ROTA_PDV rotas_ on pdvs_.ID=rotas_.PDV_ID                                          ");
		hql.append(" INNER JOIN ROTA rota_ on rotas_.ROTA_ID=rota_.ID                                              ");
		hql.append(" INNER JOIN ROTEIRO roteiro_ on rota_.ROTEIRO_ID=roteiro_.ID                                   ");
		hql.append(" INNER join ROTEIRIZACAO roteirizacao_ on roteirizacao_.ID=roteiro_.roteirizacao_ID            ");
		hql.append(" INNER JOIN BOX box_ on roteirizacao_.BOX_ID=box_.ID                                           ");
		hql.append(" INNER JOIN NOTA_ENVIO notaenvio_ on notaenvio_.NUMERO_COTA=cota_.NUMERO_COTA                  ");
		hql.append(" INNER JOIN NOTA_ENVIO_ITEM itemNotaEnvio_ on notaenvio_.numero=itemNotaEnvio_.NOTA_ENVIO_ID   and itemNotaEnvio_.ESTUDO_COTA_ID = estudo_cota_.id ");
		hql.append(" where ");
		hql.append(" cota_.NUMERO_COTA=notaenvio_.NUMERO_COTA ");
		hql.append(" and epdv.PRINCIPAL = :pontoPrincipal ");
	//	hql.append(" and ( (estpdv_.PDV_ID is not null and  estpdv_.reparte > 0 ) or  pdvs_.ponto_principal = true) ");
		hql.append(" and (( estpdv_.PDV_ID is not null and estpdv_.reparte > 0 )  or ( estpdv_.PDV_ID is  null and pdvs_.ponto_principal = true)) ");
		hql.append(" and lancamento_.PRODUTO_EDICAO_ID=itemNotaEnvio_.PRODUTO_EDICAO_ID "); 
		hql.append(" and cota_.SITUACAO_CADASTRO <> :situacaoInativo ");
		hql.append(" and lancamento_.STATUS not in (:statusLancamento) ");
		hql.append(" and estudo_cota_.TIPO_ESTUDO <> :juramentado ");
		
		if(filtro.getIdBox() == null) {
			
			hql.append(" and box_.ID is not null ");
			hql.append(" and roteiro_.TIPO_ROTEIRO <> 'ESPECIAL' ");
		} else {
			hql.append(" and ( case when (:idBox in (select id from box where tipo_box <> 'ESPECIAL')) then box_.ID = :idBox and roteiro_.TIPO_ROTEIRO <> 'ESPECIAL' and pdvs_.ponto_principal = :pontoPrincipal ");
			hql.append(" else roteiro_.TIPO_ROTEIRO = 'ESPECIAL' end ) ");
		}
			
		if(filtro.getIdRoteiro() != null){
			
			hql.append( " and roteiro_.id in (:idRoteiro) ");
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
			
			query.setParameterList("idRoteiro", filtro.getIdRoteiro());
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
	private void setarParametrosRomaneioParaExportacao(FiltroRomaneioDTO filtro, Query query, int qtdProdutos) {
		
		this.setarParametrosRomaneio(filtro, query);
		
		// Cenário em que o usuário selecionou dois ou mais produtos:
		if (filtro != null && filtro.getProdutos() != null && filtro.getProdutos().size() == 1) {
			query.setParameter("idProdutoEdicao_lc", filtro.getProdutos().get(0));
			query.setParameter("data_distribuicao_lc", filtro.getData());
		} else if (qtdProdutos > 1) {
			for (int index = 0; index < qtdProdutos; index++) {
				query.setParameter("idProdutoEdicao" + index, filtro.getProdutos().get(index));
				query.setParameter("idProdutoEdicao_lc" + index, filtro.getProdutos().get(index));
				query.setParameter("data_distribuicao_lc" + index, filtro.getData());
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
		hql.append("    box_.ID as idBox, ");
		hql.append("    box_.CODIGO as codigoBox, ");
		hql.append("    box_.NOME as nomeBox,");
		hql.append("    roteiro_.ID as idRoteiro,");
		hql.append("    roteiro_.DESCRICAO_ROTEIRO as nomeRoteiro,");
		hql.append("    rota_.ID as idRota,");
		hql.append("    rota_.DESCRICAO_ROTA as nomeRota,");
		hql.append("    concat(endereco_.TIPO_LOGRADOURO,' ',endereco_.LOGRADOURO,', ',endereco_.NUMERO,', ',endereco_.BAIRRO,', ',endereco_.CIDADE,', ',endereco_.UF) as endereco"); 
		
		int qtdProdutos = 0;
		if (filtro.getProdutos() != null && !filtro.getProdutos().isEmpty()) {
		
			if (filtro.getProdutos().size() == 1) {
				// 
				hql.append(",round(estudo_cota_.REPARTE / pe_.PACOTE_PADRAO) as pacote ");
				hql.append(",mod(estudo_cota_.REPARTE, pe_.PACOTE_PADRAO) as quebra ");
				hql.append(",COALESCE((SELECT SUM(COALESCE(estudoPDV.REPARTE, estudoCota.REPARTE)) FROM estudo e  ");
				hql.append(" inner join estudo_cota estudoCota on estudoCota.estudo_id = e.ID         ");
	            hql.append(" LEFT join estudo_pdv estudoPDV on estudoPDV.ESTUDO_ID = e.ID and estudoPDV.cota_id = estudoCota.cota_id ");
				hql.append(" left join lancamento lc on lc.PRODUTO_EDICAO_ID = :idProdutoEdicao_lc");
				hql.append(" and lc.DATA_LCTO_DISTRIBUIDOR = :data_distribuicao_lc");
				hql.append(" where e.PRODUTO_EDICAO_ID = :idProdutoEdicao_lc");
				hql.append(" and estudoCota.COTA_ID = cota_.ID ");
				hql.append(" and (( estudoPDV.PDV_ID is not null and estudoPDV.reparte > 0 )  or ( estudoPDV.PDV_ID is  null  and pdvs_.ponto_principal = true)) ");
				hql.append(" and e.lancamento_id = lc.id ");
				if(filtro.getIdBox() != null) {
					hql.append(" and ( case when (:idBox in (select id from box where tipo_box <> 'ESPECIAL')) then 1 = 1 ");
					hql.append(" else estudoPDV.PDV_ID = rotas_.PDV_ID or estudoPDV.PDV_ID is null end ) ");
				}
				hql.append("),0) as reparteTotal");
				
			} else {
				
				// Exibir a quantidade de reparte de 'n' produtos:
				qtdProdutos = Math.min(filtro.getProdutos().size(), QUANTIDADE_MAX_PRODUTOS_POR_RELATORIO);
				for (int index = 0; index < qtdProdutos; index++) {
					
					hql.append(",COALESCE((SELECT SUM(COALESCE(estudoPDV.REPARTE, estudoCota.REPARTE)) FROM estudo e  ");
					hql.append(" inner join estudo_cota estudoCota on estudoCota.estudo_id = e.ID         ");
		            hql.append(" LEFT join estudo_pdv estudoPDV on estudoPDV.ESTUDO_ID = e.ID and estudoPDV.cota_id = estudoCota.cota_id ");
					hql.append(" left join lancamento lc on lc.PRODUTO_EDICAO_ID = :idProdutoEdicao_lc").append(index);
					hql.append(" and lc.DATA_LCTO_DISTRIBUIDOR = :data_distribuicao_lc").append(index);
					hql.append(" where e.PRODUTO_EDICAO_ID =:idProdutoEdicao").append(index);
					hql.append(" and estudoCota.COTA_ID = cota_.ID ");
					hql.append(" and (( estudoPDV.PDV_ID is not null and estudoPDV.reparte > 0 )  or ( estudoPDV.PDV_ID is  null and pdvs_.ponto_principal = true )) ");
					
					hql.append(" and e.lancamento_id = lc.id ");
					if(filtro.getIdBox() != null) {
						hql.append(" and ( case when (:idBox in (select id from box where tipo_box <> 'ESPECIAL')) then 1 = 1 ");
						hql.append(" else estudoPDV.PDV_ID = rotas_.PDV_ID or (estudoPDV.PDV_ID is null and  pdvs_.ponto_principal = true ) end ) ");
					}
					hql.append("),0) as qtdProduto").append(index);
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
				.addScalar("codigoBox",StandardBasicTypes.LONG)
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

	@Override
	@SuppressWarnings("unchecked")
	public List<ArquivoRotDTO> obterInformacoesParaArquivoRot(FiltroRomaneioDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append("select lpad(e.ordem,6,0) as roteiroOrdem, ");
		hql.append("rpad(e.descricao_roteiro,30,' ') as roteiroDescricao, ");
		hql.append(" lpad(d.ordem,3,0) as rotaOrdem, ");
		hql.append("case when (e.descricao_roteiro = 'VEJA-PLANTAO COMPRADOR')  then 'ROTA CD PLANTAO               ' ");
		hql.append("else ");
		hql.append("rpad(d.descricao_rota,30,' ') ");
		hql.append("end as rota, "); 
		hql.append("6389563 as codigoDistribuidor, ");
		hql.append("'04/04/2016' as dataOperacao, ");
		hql.append("lpad(numero_cota,5,0) as numeroCota, ");
		hql.append("rpad(l.nome,35,' ') as nome,  ");
		hql.append("rpad(coalesce(n.TIPO_LOGRADOURO,5,' '),5,' ') as tipoLogradouro, ");
		hql.append("rpad(n.logradouro,30,' ') as logradouro, ");
		hql.append("rpad(n.numero,5,' ') as numero,");
		hql.append("rpad(coalesce(n.COMPLEMENTO,' '),20,' ') as complemento, ");
		hql.append("rpad(n.bairro,30,' ') as bairro, ");
		hql.append("rpad(n.cidade,30,' ') as cidade, "); 
		hql.append("lpad(k.codigo,8,0) as codigoProduto, ");
		hql.append("rpad(j.nome_comercial,30,' ') as nomeComercial, ");
		hql.append("lpad(j.numero_edicao,4,0) as edicao,  ");
		hql.append("lpad(truncate(i.QTDE_efetiva,0),10,0) as qtdeEfetiva, ");
		hql.append("lpad(truncate((i.QTDE_efetiva / j.pacote_padrao),0),10,0) as divisaoQtdeEfetivaPacotePadrao, ");
		hql.append("lpad(truncate(mod(i.QTDE_efetiva, j.pacote_padrao),0),10,0) as modQtdeEfetivaPacotePadrao "); 
		hql.append("from cota a, pdv b, rota_pdv c, rota d, roteiro e, roteirizacao f, box g, estudo h, estudo_cota i, produto_edicao j, produto k, ");
		hql.append("pessoa l, endereco_pdv m, endereco n ");
		hql.append("where a.id = b.cota_id ");
		hql.append("and g.id = f.box_id ");
		hql.append("and e.roteirizacao_id = f.id ");
		hql.append("and d.roteiro_id = e.id ");
		hql.append("and c.pdv_id = b.id  ");
		hql.append("and d.id = c.rota_id ");
		hql.append("and i.cota_id = a.id ");
		hql.append("and n.id = m.endereco_id ");
		hql.append("and m.pdv_id = b.id ");
		hql.append("and e.TIPO_ROTEIRO = 'ESPECIAL' ");
		hql.append("and h.id = i.estudo_id ");
		hql.append("and k.id = j.produto_id ");
		hql.append("and l.id = a.pessoa_id ");
		// hql.append(" and DESCRICAO_ROTEIRO in ('VEJA-GRAFICA', 'VEJA-PLANTAO COMPRADOR') ");
		if(filtro.getIdRoteiro() != null && !filtro.getIdRoteiro().isEmpty()) {			
			hql.append(" and e.id in (:idRoteiro) ");		
		}
		hql.append(" and b.ponto_principal = true ");
		hql.append(" and h.produto_edicao_id = j.id ");
		
		hql.append(" and h.produto_edicao_id = j.id ");
		hql.append(" and h.data_lancamento = :dataLancamento ");
		hql.append(" and j.id in (:produtoEdicao) ");
		
		hql.append(" and b.id not in (select pdv_id from estudo_pdv where estudo_id = e.id) ");
		hql.append(" union all ");
		hql.append(" select lpad(e.ordem,6,0) as roteiroOrdem, ");
		hql.append(" rpad(e.descricao_roteiro,30,' ') as roteiroDescricao, ");
		hql.append(" lpad(d.ordem,3,0) as rotaOrdem, ");
		hql.append("  ");
		hql.append(" case when (e.descricao_roteiro = 'VEJA-PLANTAO COMPRADOR') "); 
		hql.append(" then 'ROTA CD PLANTAO               ' ");
		hql.append(" else ");
		hql.append(" rpad(d.descricao_rota,30,' ') ");
		hql.append(" end as rota, ");
		hql.append("  ");
		hql.append(" 6389563 as 'codigoDistribuidor', ");
		hql.append(" '04/04/2016' as dataOperacao, "); 
		hql.append(" lpad(numero_cota,5,0) as numeroCota, ");
		hql.append(" rpad(l.nome,35,' ') as nome, ");
		hql.append(" rpad(coalesce(n.TIPO_LOGRADOURO,5,' '),5,' ') as tipoLogradouro, ");
		hql.append(" rpad(n.logradouro,30,' ') as logradouro, ");
		hql.append("        rpad(n.numero,5,' ') as numero, ");
		hql.append(" rpad(coalesce(n.COMPLEMENTO,' '),20,' ') as complemento, ");
		hql.append(" rpad(n.bairro,30,' ') as bairro, ");
		hql.append(" rpad(n.cidade,30,' ') as cidade, ");
		hql.append("        lpad(k.codigo,8,0) as codigoProduto, ");
		hql.append(" rpad(j.nome_comercial,30,' ') as nomeComercial, ");
		hql.append(" lpad(j.numero_edicao,4,0) as edicao, ");
		hql.append(" lpad(truncate(EP.REPARTE,0),10,0) as qtdeEfetiva, ");
		hql.append(" 	   lpad(truncate((EP.REPARTE / j.pacote_padrao),0),10,0) as divisaoQtdeEfetivaPacotePadrao, ");
		hql.append(" 	   lpad(truncate(mod(EP.REPARTE, j.pacote_padrao),0),10,0) as modQtdeEfetivaPacotePadrao ");
		hql.append(" from cota a, pdv b, rota_pdv c, rota d, roteiro e, roteirizacao f, box g, estudo h, estudo_cota i, produto_edicao j, produto k, ");
		hql.append(" 	pessoa l, endereco_pdv m, endereco n, estudo_pdv ep ");
		hql.append(" where a.id = b.cota_id  ");
		hql.append(" and g.id = f.box_id ");
		hql.append(" and e.roteirizacao_id = f.id ");
		hql.append(" and ep.estudo_id = h.id ");
		hql.append(" and ep.pdv_id = b.id ");
		hql.append(" and d.roteiro_id = e.id ");
		hql.append(" and c.pdv_id = b.id  ");
		hql.append(" and d.id = c.rota_id ");
		hql.append(" and i.cota_id = a.id ");
		hql.append(" and n.id = m.endereco_id ");
		hql.append(" and m.pdv_id = b.id ");
		hql.append(" and e.TIPO_ROTEIRO = 'ESPECIAL' ");
		hql.append(" and h.id = i.estudo_id ");
		hql.append(" and k.id = j.produto_id ");
		hql.append(" and l.id = a.pessoa_id ");
		
		if(filtro.getIdRoteiro() != null && !filtro.getIdRoteiro().isEmpty()) {			
			hql.append(" and e.id in (:idRoteiro) ");		
		}
		
		hql.append(" and h.produto_edicao_id = j.id ");
		hql.append(" and h.data_lancamento = :dataLancamento ");
		
		if(filtro.getProdutos() != null && !filtro.getProdutos().isEmpty()) {
			hql.append(" and j.id in (:produtoEdicao) ");
		}
		
		hql.append(" AND EP.ESTUDO_ID = e.id ");
		hql.append(" order by roteiroOrdem, roteiroDescricao, rotaOrdem, rota, numeroCota, nome ");
		
		final SQLQuery query =  getSession().createSQLQuery(hql.toString());
		query.addScalar("roteiroOrdem", StandardBasicTypes.STRING);
        query.addScalar("roteiroDescricao", StandardBasicTypes.STRING);
        query.addScalar("rotaOrdem", StandardBasicTypes.STRING);
        query.addScalar("rota", StandardBasicTypes.STRING);
        query.addScalar("codigoDistribuidor", StandardBasicTypes.STRING);
        query.addScalar("dataOperacao", StandardBasicTypes.STRING);
        query.addScalar("numeroCota", StandardBasicTypes.STRING);
        query.addScalar("nome", StandardBasicTypes.STRING);
        query.addScalar("tipoLogradouro", StandardBasicTypes.STRING);
        query.addScalar("logradouro", StandardBasicTypes.STRING);
        query.addScalar("numero", StandardBasicTypes.STRING);
        query.addScalar("complemento", StandardBasicTypes.STRING);
        query.addScalar("bairro", StandardBasicTypes.STRING);
        query.addScalar("cidade", StandardBasicTypes.STRING);
        query.addScalar("codigoProduto", StandardBasicTypes.STRING);
        query.addScalar("nomeComercial", StandardBasicTypes.STRING);
        query.addScalar("edicao", StandardBasicTypes.STRING);
        query.addScalar("qtdeEfetiva", StandardBasicTypes.STRING);
        query.addScalar("divisaoQtdeEfetivaPacotePadrao", StandardBasicTypes.STRING);
        query.addScalar("modQtdeEfetivaPacotePadrao", StandardBasicTypes.STRING);
        
        if(filtro.getIdRoteiro() != null && !filtro.getIdRoteiro().isEmpty()) {
        	query.setParameterList("idRoteiro", filtro.getIdRoteiro());
		}
        
        query.setParameter("dataLancamento", filtro.getData());
        
        if(filtro.getProdutos() != null && !filtro.getProdutos().isEmpty()) {
        	query.setParameterList("produtoEdicao", filtro.getProdutos());
        }
        
        query.setResultTransformer(new AliasToBeanResultTransformer(ArquivoRotDTO.class));
		
		return query.list();
	}	
}