package br.com.abril.nds.repository.impl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.StatelessSession;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.enums.TipoParametroSistema;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.TipoAtividade;
import br.com.abril.nds.model.fiscal.ParametroFTFGeracao;
import br.com.abril.nds.model.fiscal.nota.NotaFiscal;
import br.com.abril.nds.model.fiscal.nota.NotaFiscalReferenciada;
import br.com.abril.nds.model.ftf.FTFCommons;
import br.com.abril.nds.model.ftf.envio.FTFEnvTipoRegistro00;
import br.com.abril.nds.model.ftf.envio.FTFEnvTipoRegistro01;
import br.com.abril.nds.model.ftf.envio.FTFEnvTipoRegistro02;
import br.com.abril.nds.model.ftf.envio.FTFEnvTipoRegistro06;
import br.com.abril.nds.model.ftf.envio.FTFEnvTipoRegistro08;
import br.com.abril.nds.model.ftf.envio.FTFEnvTipoRegistro09;
import br.com.abril.nds.model.ftf.retorno.FTFRetTipoRegistro01;
import br.com.abril.nds.model.integracao.ParametroSistema;
import br.com.abril.nds.repository.AbstractRepository;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.repository.FTFRepository;
import br.com.abril.nds.repository.ParametroSistemaRepository;
import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.StringUtil;
import br.com.abril.nds.vo.ItemEncalheBandeiraVO;
import br.com.abril.nds.vo.NotaEncalheBandeiraVO;

@Repository
public class FTFRepositoryImpl extends AbstractRepository implements FTFRepository {

	private static long NF_VENDA_COTA = 28;
	
	private static long NF_REMESSA_COTA = 22;
			
	private static long NF_ENTRADA_POR_DEVOLUCAO = 26;
	
	private Distribuidor distribuidor;

	@Autowired
	HibernateTransactionManager transactionManager;

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Autowired
	private DistribuidorRepository distribuidorRepository;

	@Autowired
	private ParametroSistemaRepository parametroSistemaRepository;

	@Autowired
	private CotaRepository cotaRepository;

	@Override
	public FTFEnvTipoRegistro00 obterRegistroTipo00(long idNaturezaOperacao) {
		
		FTFEnvTipoRegistro00 reg00 = popularRegistro00(idNaturezaOperacao);
		
		if(reg00 == null) {
			
			throw new ValidacaoException(TipoMensagem.ERROR, "Problemas na configuração da Natureza de Operação para o FTF.");
		}
		
		reg00.setDataGeracao(DateUtil.formatarData(GregorianCalendar.getInstance().getTime(), Constantes.DATE_PATTERN_PT_BR));

		ParametroSistema ps = parametroSistemaRepository.buscarParametroPorTipoParametro(TipoParametroSistema.FTF_INDEX_FILENAME);

		Long sequence  = Long.parseLong(ps.getValor());
		sequence += 1l;
		ps.setValor(Long.toString(sequence));
		parametroSistemaRepository.alterar(ps);

		String pattern = "000000";
		DecimalFormat myFormatter = new DecimalFormat(pattern);
		long value = sequence;
		String output = myFormatter.format(value);

		String codigoDistribuidor = distribuidor.getCodigoDistribuidorDinap();
		if(StringUtil.isEmpty(codigoDistribuidor) || codigoDistribuidor.equals("0")) {
			codigoDistribuidor = distribuidor.getCodigoDistribuidorFC();
		}

		reg00.setNomeArquivo(String.format("NDS%s%s.PED", output, codigoDistribuidor));
		reg00.setNumSequencia(output);
		reg00.setNovoNomeArquivo(String.format("NDS%s%s.PED", output, codigoDistribuidor));
		return reg00;

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FTFEnvTipoRegistro01> obterResgistroTipo01(List<NotaFiscal> notas, long idNaturezaOperacao) {

		StringBuilder sqlBuilder = new StringBuilder();

		sqlBuilder.append(" select ");
		sqlBuilder.append(" '1' as tipoRegistro, ");

		sqlBuilder.append(" paramFtf.CODIGO_ESTABELECIMENTO_EMISSOR_GFF as codigoEstabelecimentoEmissor, ");
		sqlBuilder.append(" paramFtf.CODIGO_CENTRO_EMISSOR as codigoCentroEmissor, ");
		sqlBuilder.append(" paramFtf.CNPJ_EMISSOR as cnpjEmpresaEmissora, ");
		sqlBuilder.append(" paramFtf.ESTABELECIMENTO as codLocal, ");
		
		concatenarTipoPedidoBy(idNaturezaOperacao, sqlBuilder);

		sqlBuilder.append(" LPAD(cast(nfn.id as char), 8, '0') as numeroDocOrigem, ");
		sqlBuilder.append(" paramFtf.CODIGO_SOLICITANTE as codSolicitante,  "); 
		sqlBuilder.append(" '' as  nomeLocalEntregaNf, ");
		sqlBuilder.append(" DATE_FORMAT(nfn.DATA_EMISSAO,'%d/%m/%Y') as dataPedido, ");
		sqlBuilder.append(" nfn.DOCUMENTO_DESTINATARIO as cpfCnpjDestinatario, ");
		sqlBuilder.append(" '' as cpfCnpjEstabelecimentoEntrega, ");
		sqlBuilder.append(" '' as codDestinatarioSistemaOrigem, ");
		sqlBuilder.append(" COALESCE(cast(nfn.CONDICAO as char),'') as codCondicaoPagamento, ");
		sqlBuilder.append(" '' as numInscricaoEstadual, ");
		sqlBuilder.append(" '' as numDeclaracaoImportacao, ");
		sqlBuilder.append(" '' as valorDespesasAcessorias, ");
		sqlBuilder.append(" '' as valorVariacaoCambial, ");
		sqlBuilder.append(" cast(nfn.vl_total_frete as char) as valorFrete, ");
		sqlBuilder.append(" cast(nfn.vl_total_seguro as char) as valorSeguro, ");
		sqlBuilder.append(" cast(nfn.vl_total_outro as char) as valorOutrasDespesas, ");
		sqlBuilder.append(" '' as valorComplemento,  ");
		sqlBuilder.append(" '' as valorIcmsComplemento, ");
		sqlBuilder.append(" '' as valorIpiComplemento, ");
		sqlBuilder.append(" '' as imprimePercentualDesconto, ");
		sqlBuilder.append(" '' as codMotivoCancelamento, ");
		sqlBuilder.append(" '' as indicadorRefaturamento, ");
		sqlBuilder.append(" '' as codMoeda, ");
		sqlBuilder.append(" '' as dataCancelamento,  ");
		sqlBuilder.append(" '' as numSerieNfOrigem,  ");
		sqlBuilder.append(" '' as numNfOrigem,  ");
		sqlBuilder.append(" COALESCE(cast(nfn.documento_trans as char),'') as cpfCnpjTransportadora, ");
		sqlBuilder.append(" COALESCE(cast(nfn.placa_veiculo_trans as char),'') as numPlacaVeiculo, ");
		sqlBuilder.append(" '' as tipoEmbalagem, ");
		sqlBuilder.append(" '0' as quantidadeVolumes, ");
		sqlBuilder.append(" cast((case when sum(pe.peso) > 0 then sum(pe.peso) else '0' end) as char) as pesoLiquido, ");
		sqlBuilder.append(" cast((case when sum(pe.peso) > 0 then sum(pe.peso) else '0' end) as char) as pesoBruto, ");
		sqlBuilder.append(" '' as numContaBanco, ");
		sqlBuilder.append(" COALESCE(DATE_FORMAT(nfn.data_saida_entrada,'%d/%m/%Y'),'')  as dataSaida, ");
		sqlBuilder.append(" '' as formaPagamento, ");

		sqlBuilder.append(" '' as envioBoleto, ");
		sqlBuilder.append(" '' as formaFaturamento, ");
		sqlBuilder.append(" '' as percentualComissaoIntermediaio, ");
		sqlBuilder.append(" '' as pedidoInsercao, ");
		sqlBuilder.append(" '' as cpfCnpjIntermediario, ");
		sqlBuilder.append(" '' as codIntermediarioSistemaOrigem, ");
		sqlBuilder.append(" '' as valorBruto, ");
		sqlBuilder.append(" '' as numeroFaturaAssociada, ");

		sqlBuilder.append(" '' as debitoCartaoCredito, ");
		sqlBuilder.append(" '' as numCartaoCredito, ");
		sqlBuilder.append(" '' as numContratoPermuta, ");
		sqlBuilder.append(" '' as textoObservacoes, ");
		sqlBuilder.append(" '' as numFaturaCliente, ");
		sqlBuilder.append(" '' as valorDescontoComercial, ");
		sqlBuilder.append(" '' as codUnidadeOperacionalEmpresaEmissora, ");
		sqlBuilder.append(" '0' as indicadorResponsavelPeloFrete, ");
		sqlBuilder.append(" '1' as statusPedido, ");

		sqlBuilder.append(" '' as numPedidoCliente, ");
		sqlBuilder.append(" '' as codLinhaDistribuicao, "); 

		sqlBuilder.append(" '' as codResponsavelPeloFrete, ");
		sqlBuilder.append(" '' as numSimulacao, "); 

		sqlBuilder.append(" '' as tipoOperacao, "); //TODO: -- falta definição por parte da equipe de FTF 
		sqlBuilder.append(" '' as numAprovacao, ");
		sqlBuilder.append(" '' as situacaoTitulo,  ");//TODO: -- verificar equipe FTF
		sqlBuilder.append(" '' as codUnidadeOperacionalEmpresaDestinataria, ");
		sqlBuilder.append(" '' as percentualDescontoFidelidade, ");
		sqlBuilder.append(" '' as pracaEntrega, ");
		sqlBuilder.append(" '' as codAssinanteCdms, ");
		sqlBuilder.append(" '' as codProjetoSize3, ");
		sqlBuilder.append(" '' as numAssinatura, ");
		sqlBuilder.append(" '' as cpfCnpjEstabelecimentoColeta, ");
		sqlBuilder.append(" '' as cnpjTransportadoraSuframa, "); //TODO: -- verificar equipe de negocio 
		sqlBuilder.append(" COALESCE(nfn.UF_TRANS,'') as ufPlacaVeiculoTransportadora, ");
		sqlBuilder.append(" COALESCE(nfn.EMAIL_DESTINATARIO,'') as emailDestinatarioNota, ");
		sqlBuilder.append(" '' as codProjetoSize7, ");
		sqlBuilder.append(" '' as sequenciaAssinatura, ");
		sqlBuilder.append(" '' as codContrato, ");
		sqlBuilder.append(" '' as codLinhaContrato, ");
		sqlBuilder.append(" '' as codSistemaOrigem, ");
		sqlBuilder.append(" '' as mensagemFiscalImpressaNota, "); 
		sqlBuilder.append(" nfn.hora_saida_entrada as horaSaidaNotaMercadoria, ");
		sqlBuilder.append(" '' as aliquotaIssImpostoDevido, ");
		sqlBuilder.append(" '' as nomeMunicipioServicoPrestado, ");
		sqlBuilder.append(" '' as numNotaEmpenho, ");
		sqlBuilder.append(" '' as codViagem, ");
		sqlBuilder.append(" '' as codFaturaAssociada, ");
		sqlBuilder.append(" ''  as codigoCGLServicoPrestado, ");
		sqlBuilder.append(" nfp.TIPO_PESSOA as tipoPessoaDestinatario, ");
		sqlBuilder.append(" '' as modoPrestacaoServico ");
		sqlBuilder.append(" from nota_fiscal_novo nfn ");
		sqlBuilder.append(" inner join nota_fiscal_produto_servico nfps on nfps.NOTA_FISCAL_ID = nfn.ID ");
		sqlBuilder.append(" inner join produto_edicao pe on nfps.PRODUTO_EDICAO_ID = pe.ID ");
		sqlBuilder.append(" left join natureza_operacao no ON no.ID = nfn.NATUREZA_OPERACAO_ID ");
		sqlBuilder.append(" join parametros_ftf_geracao paramFtf ON no.ID = paramftf.NATUREZA_OPERACAO_ID ");
		sqlBuilder.append(" left join nota_fiscal_endereco endereco on endereco.ID = nfn.ENDERECO_ID_DESTINATARIO ");
		sqlBuilder.append(" left join nota_fiscal_pessoa nfp on nfp.ID = nfn.PESSOA_DESTINATARIO_ID_REFERENCIA ");

		sqlBuilder.append(" where 1 = 1 ");
		sqlBuilder.append(" and nfn.id in (:idsNotasFiscais) ");
		sqlBuilder.append(" and no.id = :idNaturezaOperacao ");
		sqlBuilder.append(" GROUP BY nfn.id, pe.id ");
		
		SQLQuery query = getSession().createSQLQuery(sqlBuilder.toString());

		query.setParameterList("idsNotasFiscais", obterIdsFrom(notas));
		query.setParameter("idNaturezaOperacao", idNaturezaOperacao);

		query.setResultTransformer(new AliasToBeanResultTransformer(FTFEnvTipoRegistro01.class));

		return query.list();
	}

	private void concatenarTipoPedidoBy(long idTipoNotaFiscal, StringBuilder sqlBuilder) {
		if (idTipoNotaFiscal == NF_REMESSA_COTA) {
			sqlBuilder.append(" '2' as tipoPedido, ");
		} else if (idTipoNotaFiscal == NF_ENTRADA_POR_DEVOLUCAO) {
			sqlBuilder.append(" '5' as tipoPedido, ");
		} else if (idTipoNotaFiscal == NF_VENDA_COTA){
			sqlBuilder.append(" '9' as tipoPedido, ");
		} else {
			sqlBuilder.append(" '2' as tipoPedido, ");
		}
	}

	private List<Long> obterIdsFrom(List<NotaFiscal> notas){
		
		List<Long> ids = new ArrayList<>();

		for (NotaFiscal nota : notas) {
			ids.add(nota.getId());
		}
		
		return ids;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FTFEnvTipoRegistro02> obterResgistroTipo02(Long idNF, long idTipoNotaFiscal) {

		StringBuilder sqlBuilder = new StringBuilder();

		sqlBuilder.append(" select ")
		.append(" '2' as tipoRegistro, ")
		.append(" paramFtf.CODIGO_ESTABELECIMENTO_EMISSOR_GFF as codigoEstabelecimentoEmissor, ")
		.append(" paramFtf.CNPJ_EMISSOR as cnpjEmpresaEmissora,  ")
		.append(" paramFtf.ESTABELECIMENTO as codLocal,  ");
		
		concatenarTipoPedidoBy(idTipoNotaFiscal, sqlBuilder);
		
		sqlBuilder.append(" LPAD(cast(nfn.id as char), 8, '0') as numeroDocOrigem, ")
		.append(" cast(nfps.SEQUENCIA as char) as numItemPedido, ")
		.append(" 'NDS' as codSistemaOrigemPedido, ")
		.append(" RPAD(cast(nfps.CODIGO_PRODUTO as char), 10, ' ') as codProdutoOuServicoSistemaOrigem, ")
		.append(" replace(cast(nfps.quantidade_comercial as char), '.', '') as qtdeProdutoOuServico, ")
		.append(" replace(cast(round(nfps.VALOR_UNITARIO_COMERCIAL, 8) as char), '.', '') as valorUnitario, ")
		.append(" '' as valorBrutoTabela, ")
		.append(" '' as valorPrecoSubstituicao, ")
		.append(" replace(cast(round(valor_desconto, 8) as char), '.', '') as percentualDescontoItem, ")
		.append(" '' as valorDescontoComercial, ")
		.append(" '1' as tipoUtilizacaoProduto, ")
		.append(" paramFtf.CODIGO_NATUREZA_OPERACAO_FTF as codTipoNaturezaOperacao, ")
		.append(" '' as textoObservacoes, ")
		.append(" CONCAT('0', pe.numero_edicao) as numEdicaoRevista, ")
		.append(" '' as dataCompetencia, ")
		.append(" LPAD(cast(nfps.CODIGO_BARRAS as char), 13, '0') as codBarrasProduto, ")
		.append(" '5' as indicadorProdutoServicoMaterial, ") //TODO: -- aguardando resposta equipe de negócio
		.append(" '0000100014' as codMaterialOuServicoCorporativo, ")
		.append(" paramFtf.CODIGO_NATUREZA_OPERACAO_FTF as novoCodigoTipoNaturezaOperacao, ")
		.append(" nfps.DESCRICAO_PRODUTO as descricaoProduto, ")
		.append(" '' as codEanProduto ")
		.append(" from nota_fiscal_produto_servico nfps ")
		.append(" inner join nota_fiscal_novo nfn ON nfn.id = nfps.NOTA_FISCAL_ID ")
		.append(" left join natureza_operacao no ON no.ID = nfn.NATUREZA_OPERACAO_ID ")
		.append(" inner join produto_edicao pe ON pe.id = nfps.PRODUTO_EDICAO_ID ")
		.append(" inner join nota_fiscal_pessoa nfp on nfp.ID = nfn.PESSOA_DESTINATARIO_ID_REFERENCIA ")
        .append(" inner join pessoa p on p.id = nfp.ID_PESSOA_ORIGINAL ")
        .append(" left outer join cota c on c.PESSOA_ID = p.id and c.id = nfn.COTA_ID ")
        .append(" join parametros_ftf_geracao paramFtf ON no.ID = paramftf.NATUREZA_OPERACAO_ID ")
		.append(" 		and (CASE WHEN c.CONTRIBUINTE_ICMS = true THEN ")
		.append(" 				(paramFtf.CNPJ_DESTINATARIO IS NULL OR paramFtf.CNPJ_DESTINATARIO = '') ") 
		.append(" 			ELSE ")
		.append(" 				(paramFtf.CNPJ_DESTINATARIO IS NOT NULL AND LENGTH(paramFtf.CNPJ_DESTINATARIO) > 1) ")
		.append(" 			END) ")
		.append(" where nfps.NOTA_FISCAL_ID = :idNF ")
		.append(" GROUP BY nfn.id ");
		
		SQLQuery query = getSession().createSQLQuery(sqlBuilder.toString());
		query.setParameter("idNF", idNF);

		query.setResultTransformer(new AliasToBeanResultTransformer(FTFEnvTipoRegistro02.class));

		return query.list();
	}

	@Override
	public FTFEnvTipoRegistro09 obterRegistroTipo09(long idNaturezaOperacao) {
		
		StringBuilder sb = new StringBuilder();

		sb.append(" select DISTINCT ")
		.append(" '9' as tipoRegistro, ")
		.append(" paramFtf.CODIGO_ESTABELECIMENTO_EMISSOR_GFF as codigoEstabelecimentoEmissor, ")
		.append(" paramFtf.CNPJ_EMISSOR as cnpjEmpresaEmissora,  ")
		.append(" paramFtf.ESTABELECIMENTO as codLocal,  ")
		.append(" '99' as tipoPedido, ")
		.append(" '99999999' as numeroDocOrigem ")
		.append(" from natureza_operacao no ")
		.append(" join parametros_ftf_geracao paramFtf ON no.ID = paramftf.NATUREZA_OPERACAO_ID ")
		.append(" where no.id = :idNaturezaOperacao ");

		SQLQuery query = getSession().createSQLQuery(sb.toString());
		query.setParameter("idNaturezaOperacao", idNaturezaOperacao);

		query.setResultTransformer(new AliasToBeanResultTransformer(FTFEnvTipoRegistro09.class));

		Object uniqueResult = query.uniqueResult();

		FTFEnvTipoRegistro09 reg09 = (FTFEnvTipoRegistro09) uniqueResult;
		
		return reg09;
	}

	private FTFEnvTipoRegistro00 popularRegistro00(long idNaturezaOperacao) {
		
		StringBuilder sb = new StringBuilder();
		
		sb.append(" select DISTINCT ")
		.append(" '0' as tipoRegistro, ")
		.append(" paramFtf.CODIGO_ESTABELECIMENTO_EMISSOR_GFF as codigoEstabelecimentoEmissor, ")
		.append(" paramFtf.CNPJ_EMISSOR as cnpjEmpresaEmissora,  ")
		.append(" paramFtf.ESTABELECIMENTO as codLocal,  ")
		.append(" '  ' as tipoPedido,  ")
		.append(" '00000000' as numeroDocOrigem ")
		.append(" from natureza_operacao no ")
		.append(" join parametros_ftf_geracao paramFtf ON no.ID = paramftf.NATUREZA_OPERACAO_ID ")
		.append(" where no.id = :idNaturezaOperacao ");

		SQLQuery query = getSession().createSQLQuery(sb.toString());
		
		query.setParameter("idNaturezaOperacao", idNaturezaOperacao);

		query.setResultTransformer(new AliasToBeanResultTransformer(FTFEnvTipoRegistro00.class));

		Object uniqueResult = query.uniqueResult();

		FTFEnvTipoRegistro00 reg00 = (FTFEnvTipoRegistro00) uniqueResult;
		return reg00;
	}

	private void setCommonsParameters(FTFCommons object) {

		Map<String, ParametroSistema> ps = parametroSistemaRepository.buscarParametroSistemaGeralMap();
		if (ps != null) {
			object.setCodigoEstabelecimentoEmissor(ps.get("FTF_CODIGO_ESTABELECIMENTO_EMISSOR").getValor());
			object.setCnpjEmpresaEmissora(ps.get("FTF_CNPJ_ESTABELECIMENTO_EMISSOR").getValor());
			object.setCodLocal(ps.get("FTF_CODIGO_LOCAL").getValor());
		}

	}

	
	@PostConstruct
	@SuppressWarnings("unchecked")
	public void getDistribuidor() {

		StatelessSession ss = transactionManager.getSessionFactory().openStatelessSession();

		String hql = "from Distribuidor";
		Query query = ss.createQuery(hql);

		List<Distribuidor> distribuidores = query.list();

		distribuidor = distribuidores.isEmpty() ? null : distribuidores.get(0);

	}

	@Override
	public void atualizarRetornoFTF(final List<FTFRetTipoRegistro01> list) {

		StringBuilder builder  = new StringBuilder("");
		builder.append("  UPDATE nota_fiscal_novo ") 
		.append(" SET ") 
		.append(" SERIE = :serieNFe, ")
		.append(" STATUS = :statusNFe, ")
		.append(" CHAVE_ACESSO = :chaveAcessoNFe, ")
		.append(" PROTOCOLO = :numeroProtocoloAutorizacao, ")
		.append(" NUMERO_NFE_RET_FTF = :numeroNFe ")
		.append(" WHERE id = :numeroDocumentoOrigem ");

		SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(list.toArray());
		namedParameterJdbcTemplate.batchUpdate(builder.toString(), batch);


	}
	
	
	

	public FTFEnvTipoRegistro06 obterRegistroTipo06(final NotaFiscalReferenciada nota) {

		FTFEnvTipoRegistro06 tipoRegistro06 = null;
		NotaFiscal notaFiscal = nota.getPk().getNotaFiscal();

		if (notaFiscal != null) {
			tipoRegistro06 = new FTFEnvTipoRegistro06();

			tipoRegistro06.setTipoRegistro("6");
			setCommonsParameters(tipoRegistro06);
			tipoRegistro06.setTipoPedido("2");
			tipoRegistro06.setNumeroDocOrigem(notaFiscal.getId().toString());
			tipoRegistro06.setNumDocumentoOrigemAssociado("");//TODO

		}


		return tipoRegistro06;
	}

	@SuppressWarnings("unchecked")
	@Override
	public FTFEnvTipoRegistro06 obterRegistroTipo06(final Long idNF) {

		StringBuilder sb = new StringBuilder();

		sb.append("select nfr from NotaFiscalReferenciada nfr ")
		.append(" left join nfr.pk ")
		.append(" left join nfr.pk.notaFiscal nf ")
		.append(" where nf.id = :idNF ");
		
		Query query = getSession().createQuery(sb.toString());
		query.setParameter("idNF", idNF);

		List<NotaFiscalReferenciada> notas = query.list();

		FTFEnvTipoRegistro06 tipoRegistro06 = null;
		
		if(notas != null && !notas.isEmpty()){

			for (NotaFiscalReferenciada nota : notas) {
				
				NotaFiscal notaFiscal = nota.getPk().getNotaFiscal();
				
				if (notaFiscal != null) {
					tipoRegistro06 = new FTFEnvTipoRegistro06();
					
					tipoRegistro06.setTipoRegistro("6");
					setCommonsParameters(tipoRegistro06);
					tipoRegistro06.setTipoPedido("2");
					tipoRegistro06.setNumeroDocOrigem(notaFiscal.getId().toString());
					tipoRegistro06.setNumDocumentoOrigemAssociado("");
					break;
				}
			}
			
		}
		return tipoRegistro06;
	}


	@Override
	public FTFEnvTipoRegistro08 obterRegistroTipo08(long idNotaFiscal) {
		StringBuilder sb = new StringBuilder();

		sb.append(" select DISTINCT ")
		.append(" '8' as tipoRegistro, ")
		.append(" paramFtf.CODIGO_ESTABELECIMENTO_EMISSOR_GFF as codigoEstabelecimentoEmissor, ")
		.append(" paramFtf.CNPJ_EMISSOR as cnpjEmpresaEmissora,  ")
		.append(" paramFtf.ESTABELECIMENTO as codLocal,  ");
		concatenarTipoPedidoBy(idNotaFiscal, sb);
		sb.append(" LPAD(nfn.id, 8, '0') as numeroDocOrigem, ")
		.append(" COALESCE(pessoa.NOME,'') as nomeDoCliente, ")
		.append(" nfn.DOCUMENTO_DESTINATARIO as cpfOuCnpj, ")
		.append(" endereco.LOGRADOURO as endereco, ")
		.append(" endereco.NUMERO as numeroEndereco, ")
		.append(" COALESCE(endereco.COMPLEMENTO,'') as  complementoEndereco, ")
		.append(" endereco.BAIRRO as bairro, ")
		.append(" endereco.CIDADE as cidade, ")
		.append(" cast(endereco.CODIGO_CIDADE_IBGE as char) as codigoIBGE, ")
		.append(" endereco.UF as siglaEstado, ")
		.append(" replace(endereco.CEP, '-', '') as cep, ")
		.append(" '55' as codigoPais, ")
		.append(" 'BRASIL' as nomePais, ")
		.append(" '' as codigoCGL, ")
		.append(" '55' as ddi, ")
		.append(" telefone.DDD as ddd, ")
		.append(" cast(telefone.NUMERO as char) as telefone, ")
		.append(" '' as complementoTelefone ")
		.append("  ")
		.append(" from nota_fiscal_novo nfn ")
		.append(" left join natureza_operacao no ON no.ID = nfn.NATUREZA_OPERACAO_ID ")
		.append(" join parametros_ftf_geracao paramFtf ON no.ID = paramftf.NATUREZA_OPERACAO_ID ")
		.append(" left join nota_fiscal_pessoa pessoa ON pessoa.ID = nfn.PESSOA_DESTINATARIO_ID_REFERENCIA ")
		.append(" left join nota_fiscal_endereco endereco ON endereco.ID = nfn.ENDERECO_ID_DESTINATARIO ")
		.append(" left join nota_fiscal_telefone telefone ON telefone.ID = nfn.TELEFONE_ID_DESTINATARIO ")
		.append(" where nfn.id = :idNF ");

		SQLQuery query = getSession().createSQLQuery(sb.toString());
		
		query.setParameter("idNF", idNotaFiscal);

		query.setResultTransformer(new AliasToBeanResultTransformer(FTFEnvTipoRegistro08.class));

		Object uniqueResult = query.uniqueResult();

		FTFEnvTipoRegistro08 reg08 = (FTFEnvTipoRegistro08) uniqueResult;
		
		return reg08;
	}
	
	@Override
	public ParametroFTFGeracao obterParametrosFTFGeracao(Long idNaturezaOperacao, String codigoCFOP, TipoAtividade tipoAtividade) {
		StringBuilder hql = new StringBuilder();
		
		hql.append("select param ")
		   .append(" from ParametroFTFGeracao param ")
		   .append(" where ")
		   .append(" param.naturezaOperacao.id = :idNaturezaOperacao")
		   .append(" param.cfop.codigo = :codigoCFOP")
		   .append(" param.tipoAtividade = :tipoAtividade");
		 
		   	
		Query query = super.getSession().createQuery(hql.toString());

		query.setParameter("codigoNaturezaOperacao", idNaturezaOperacao);
		query.setParameter("codigoCFOP", codigoCFOP);
		query.setParameter("tipoAtividade", tipoAtividade);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ParametroFTFGeracao.class));
		
		return (ParametroFTFGeracao) query.uniqueResult();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ParametroFTFGeracao> obterTodosParametrosGeracaoFTF() {
		StringBuilder hql = new StringBuilder();
		
		hql.append("select param ").append(" from ParametroFTFGeracao param ");		 
		   	
		Query query = super.getSession().createQuery(hql.toString());
		
		return query.list();
	}

	@Override
	public boolean verificarRegistroVenda(long idNaturezaOperacao) {
		
		StringBuilder sb = new StringBuilder();

		sb.append(" select DISTINCT ")
		.append(" no.NOTA_FISCAL_VENDA_CONSIGNADO as notaFiscalVendaConsignado ")
		.append(" from natureza_operacao no ")
		.append(" where no.id = :idNaturezaOperacao ");

		SQLQuery query = getSession().createSQLQuery(sb.toString());
		query.setParameter("idNaturezaOperacao", idNaturezaOperacao);
		
		return (boolean) query.uniqueResult();
	}
	
	Integer tipoNota = 1;
	Integer numNota = 123;
	Integer codVolume = 1;
	Integer qtdSacosPaletes = 2;
	String nomeDestinoEncalhe = "DEVOLUCAO AO EDITOR";
	
	
	
	@Override
	@Transactional(readOnly = true)
	public List<NotaEncalheBandeiraVO> obterNotasNaoEnviadas() {
		StringBuilder sql = new StringBuilder();
		
		  sql.append(" select " )
		   .append(" nota.id as notaId,")
		   .append("  109  as  tipoNota,")
		   .append(" numero_documento_fiscal as numNota, ")
		   .append("   1 as codVolume,")
		   .append("     nota.qtd_volume_pallet as qtdSacosPaletes ,")
		   .append("  nome_destinatario nomeDestinoEncalhe ")
		   .append(" 	  from nota_fiscal_novo nota  where natureza_operacao_id in ")
		   .append(" (select id from natureza_operacao  ")
		   .append(" 		where TIPO_EMITENTE = 'DISTRIBUIDOR' ")
		   .append(" 	and TIPO_DESTINATARIO = 'FORNECEDOR' ")
			.append(" 	and TIPO_OPERACAO = 'SAIDA' )")
			.append("	and (interface_devolucao_fornecedor is null or interface_devolucao_fornecedor = false )  ");
			              
		
		   	
		  SQLQuery query = getSession().createSQLQuery(sql.toString());
		   query.addScalar("notaId",StandardBasicTypes.INTEGER);
		   query.addScalar("tipoNota",StandardBasicTypes.INTEGER);
		   query.addScalar("numNota",StandardBasicTypes.INTEGER);
		   query.addScalar("codVolume",StandardBasicTypes.INTEGER);
		   query.addScalar("qtdSacosPaletes",StandardBasicTypes.INTEGER);
		   query.addScalar("nomeDestinoEncalhe",StandardBasicTypes.STRING);
		query.setResultTransformer(new AliasToBeanResultTransformer(NotaEncalheBandeiraVO.class));
		return query.list();
	}
	
	 @Override
	    @Transactional(readOnly = true)
	public List<ItemEncalheBandeiraVO> obterItensNotasNaoEnviadas(Integer notaId) {
		StringBuilder sql = new StringBuilder();
		
	   
		   sql.append("	select ")
		     .append(" pe.id as produtoEdicaoId , ")
		     .append(" p.codigo as  codPublicacao, ")
			 .append(" pe.numero_edicao as numEdicao,")
			 .append(" (select max(lan.data_rec_prevista) from lancamento lan where lan.produto_edicao_id = pe.id ) as dataRecolhimento , ")
			 .append(" item.QUANTIDADE_COMERCIAL as qtdExemplares,")
			 .append(" case when pe.pacote_padrao > 0 then pe.pacote_padrao else 1 end  as pacotePadrao,")
			// .append(" 1 as exemplaresPacote,")
			// .append(" 0 as qtdExemplaresIrregular, ")
			.append(" case when pe.pacote_padrao > 0 then item.QUANTIDADE_COMERCIAL div pe.pacote_padrao else item.QUANTIDADE_COMERCIAL end  as exemplaresPacote , ")
            .append(" case when  pe.pacote_padrao > 0 then  item.QUANTIDADE_COMERCIAL mod pe.pacote_padrao else  0 end  as qtdExemplaresIrregular, ")
			 .append(" 1 as indiceVolume ")
			 .append(" from nota_fiscal_produto_servico item ")
			 .append(" inner join produto_edicao pe on  item.produto_edicao_id = pe.id ")
			 .append(" inner join produto p on pe.produto_id = p.id ")
		    .append("  where item.nota_fiscal_id = :notaId and  item.QUANTIDADE_COMERCIAL > 0 ");
		   	
		   SQLQuery query = getSession().createSQLQuery(sql.toString());
		   query.setParameter("notaId",notaId);
		   query.addScalar("produtoEdicaoId",StandardBasicTypes.INTEGER);
		   query.addScalar("codPublicacao",StandardBasicTypes.INTEGER);
		   query.addScalar("numEdicao",StandardBasicTypes.INTEGER);
		   query.addScalar("dataRecolhimento",StandardBasicTypes.DATE);
		   query.addScalar("qtdExemplares",StandardBasicTypes.INTEGER);
		   query.addScalar("pacotePadrao",StandardBasicTypes.INTEGER);
		   query.addScalar("qtdExemplaresIrregular",StandardBasicTypes.INTEGER);
		   query.addScalar("indiceVolume",StandardBasicTypes.INTEGER);
		query.setResultTransformer(new AliasToBeanResultTransformer(ItemEncalheBandeiraVO.class));
		return query.list();
	}
	 
	 
	 @Override
	 @Transactional
	 public void atualizaFlagInterfaceNotasEnviadas(Integer notaId,boolean flag) {
			
			StringBuilder sql  = new StringBuilder("");
			sql.append("  UPDATE nota_fiscal_novo ") 
			.append(" SET ") 
			.append(" interface_devolucao_fornecedor = :flag ")
			.append(" WHERE id = :notaId ");

			SQLQuery query=getSession().createSQLQuery(sql.toString());
			query.setParameter("notaId",notaId);
			query.setParameter("flag",flag);
			
			query.executeUpdate();
		
	}
	 
	 @Override
	 @Transactional
		public void atualizarQtdVolumePallet(Integer numero,Integer serie, Integer qtd) {

			StringBuilder sql  = new StringBuilder("");
			sql.append("  UPDATE nota_fiscal_novo ") 
			.append(" SET ") 
			.append(" qtd_volume_pallet = :qtd ")
			.append(" WHERE NUMERO_DOCUMENTO_FISCAL = :numero and SERIE = :serie ");
			SQLQuery query=getSession().createSQLQuery(sql.toString());
			query.setParameter("qtd",qtd);
			query.setParameter("numero",numero);
			query.setParameter("serie",serie);
			query.executeUpdate();

		


		}


}

