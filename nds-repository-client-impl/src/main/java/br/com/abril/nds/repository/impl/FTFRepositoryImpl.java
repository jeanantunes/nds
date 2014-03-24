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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.enums.TipoParametroSistema;
import br.com.abril.nds.model.cadastro.Distribuidor;
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

@Repository
public class FTFRepositoryImpl extends AbstractRepository implements FTFRepository {

	private static long NF_VENDA_COTA = 8;
	private static long NF_DEVOLUCAO_FORNECEDOR = 7;

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
	public FTFEnvTipoRegistro00 obterRegistroTipo00(long idTipoNotaFiscal) {

		FTFEnvTipoRegistro00 reg00 = new FTFEnvTipoRegistro00();
		reg00.setTipoRegistro("0");
		setCommonsParameters(reg00);
		reg00.setNumeroDocOrigem("00000000");
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
		if(StringUtil.isEmpty(codigoDistribuidor)) {
			codigoDistribuidor = distribuidor.getCodigoDistribuidorFC();
		}

		reg00.setNomeArquivo(String.format("NDS%s%s.PED", output, codigoDistribuidor));
		reg00.setNumSequencia(output);

		return reg00;

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FTFEnvTipoRegistro01> obterResgistroTipo01(List<NotaFiscal> notas, long idTipoNotaFiscal) {

		StringBuilder sqlBuilder = new StringBuilder();

		sqlBuilder.append(" select DISTINCT ");
		sqlBuilder.append(" '1' as tipoRegistro, ");

		setParametersFromDistribuidor(sqlBuilder);

		concatenarTipoPedidoBy(idTipoNotaFiscal, sqlBuilder);

		sqlBuilder.append(" cast(nfn.id as char) as numeroDocOrigem, ");
		sqlBuilder.append(" 'NDS' as codSolicitante,  "); 
		sqlBuilder.append(" endereco.LOGRADOURO as  nomeLocalEntregaNf, ");
		sqlBuilder.append(" DATE_FORMAT(nfn.DATA_EMISSAO,'%d/%m/%Y') as dataPedido, ");
		sqlBuilder.append(" 'NDS' as codCentroEmissor, ");
		sqlBuilder.append(" nfn.DOCUMENTO_DESTINATARIO as cpfCnpjDestinatario, ");
		sqlBuilder.append(" nfn.DOCUMENTO_DESTINATARIO as cpfCnpjEstabelecimentoEntrega, ");

		adicionarCodDestinatarioSistemaOrigem(idTipoNotaFiscal, sqlBuilder);

		sqlBuilder.append(" COALESCE(cast(nfn.CONDICAO as char),'') as codCondicaoPagamento, ");
		sqlBuilder.append(" nfn.IE_EMITENTE as numInscricaoEstadual, ");
		sqlBuilder.append(" nfn.IE_EMITENTE as numDeclaracaoImportacao, ");
		sqlBuilder.append(" '' as valorDespesasAcessorias, ");
		sqlBuilder.append(" '' as valorVariacaoCambial, ");
		sqlBuilder.append(" cast(nfn.vl_total_frete as char) as valorFrete, ");
		sqlBuilder.append(" cast(nfn.vl_total_seguro as char) as valorSeguro, ");
		sqlBuilder.append(" cast(nfn.vl_total_outro as char) as valorOutrasDespesas, ");
		sqlBuilder.append(" '' as valorComplemento,  ");
		sqlBuilder.append(" '' as valorIcmsComplemento, ");
		sqlBuilder.append(" '' as valorIpiComplemento, ");
		sqlBuilder.append(" '' as imprimePercentualDesconto, ");
		sqlBuilder.append(" COALESCE(cast(nfn.motivo as char),'') as codMotivoCancelamento, ");
		sqlBuilder.append(" '' as indicadorRefaturamento, ");
		sqlBuilder.append(" 'REAL' as codMoeda, ");
		sqlBuilder.append(" '' as dataCancelamento,  ");
		sqlBuilder.append(" '' as numSerieNfOrigem,  ");
		sqlBuilder.append(" '' as numNfOrigem,  ");
		sqlBuilder.append(" COALESCE(cast(nfn.documento_trans as char),'') as cpfCnpjTransportadora, ");
		sqlBuilder.append(" COALESCE(cast(nfn.placa_veiculo_trans as char),'') as numPlacaVeiculo, ");
		sqlBuilder.append(" '' as tipoEmbalagem, ");
		sqlBuilder.append(" '' as quantidadeVolumes, ");
		sqlBuilder.append(" cast(COALESCE((select sum(produto_edicao.peso) from nota_fiscal_produto_servico nfps  ");
		sqlBuilder.append(" left join produto_edicao on nfps.PRODUTO_EDICAO_ID = produto_edicao.PRODUTO_EDICAO_ID ");
		sqlBuilder.append(" where nfps.NOTA_FISCAL_ID = nfn.ID ),0) as char) as pesoLiquido, ");
		sqlBuilder.append(" '' as pesoBruto, ");
		sqlBuilder.append(" '' as numContaBanco, ");
		sqlBuilder.append(" COALESCE(DATE_FORMAT(nfn.data_saida_entrada,'%d/%m/%Y'),'')  as dataSaida, ");
		sqlBuilder.append(" cast(nfn.indicador_forma_pagamento as char) as formaPagamento, ");

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
		sqlBuilder.append(" 'NDS' as codUnidadeOperacionalEmpresaEmissora, ");
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
		sqlBuilder.append(" '' as codPessoaDetalheEmissorNota, ");
		sqlBuilder.append(" '' as codPessoaDetalheDestinatarioNota ");
		sqlBuilder.append(" from nota_fiscal_novo nfn ");
		sqlBuilder.append(" left join natureza_operacao no ON no.ID = nfn.NATUREZA_OPERACAO_ID ");
		sqlBuilder.append(" left join nota_fiscal_endereco endereco on endereco.ID = nfn.ENDERECO_ID_DESTINATARIO ");

		adicionarJoinCota(idTipoNotaFiscal, sqlBuilder);

		sqlBuilder.append(" where nfn.id in (:idsNotaFiscal) ");
		sqlBuilder.append(" and no.id  = :idTipoNotaFiscal ");
		SQLQuery query = getSession().createSQLQuery(sqlBuilder.toString());

		query.setParameterList("idsNotaFiscal", obterIdsFrom(notas));
		query.setParameter("idTipoNotaFiscal", idTipoNotaFiscal);

		adicionarDistribuidorParametro(idTipoNotaFiscal, query);

		query.setResultTransformer(new AliasToBeanResultTransformer(FTFEnvTipoRegistro01.class));

		query.setMaxResults(2);

		return query.list();
	}

	private void adicionarDistribuidorParametro(long idTipoNotaFiscal,
			SQLQuery query) {
		if (idTipoNotaFiscal == NF_DEVOLUCAO_FORNECEDOR) {
			query.setParameter("codDestinatario", distribuidor.getCodigo());
		}
	}

	private void adicionarCodDestinatarioSistemaOrigem(long idTipoNotaFiscal,
			StringBuilder sqlBuilder) {
		if (idTipoNotaFiscal == NF_DEVOLUCAO_FORNECEDOR) {
			sqlBuilder.append(" COALESCE(cast(:codDestinatario as char),'') as codDestinatarioSistemaOrigem, ");
		}else if (idTipoNotaFiscal == NF_VENDA_COTA) {
			sqlBuilder.append(" COALESCE(cast(co.numero_cota as char),'') as codDestinatarioSistemaOrigem, ");
		}
	}

	private void adicionarJoinCota(long idTipoNotaFiscal,
			StringBuilder sqlBuilder) {
		if (idTipoNotaFiscal == NF_VENDA_COTA) {
			sqlBuilder.append(" left join cota co on co.pessoa_id = nfn.PESSOA_DESTINATARIO_ID_REFERENCIA ");
		}
	}

	private void concatenarTipoPedidoBy(long idTipoNotaFiscal, StringBuilder sqlBuilder) {
		if (idTipoNotaFiscal == NF_VENDA_COTA) {
			sqlBuilder.append(" '2' as tipoPedido, ");
		}else{
			sqlBuilder.append(" '5' as tipoPedido, ");
		}
	}

	private void setParametersFromDistribuidor(StringBuilder sqlBuilder) {

		Map<String, ParametroSistema> ps = parametroSistemaRepository.buscarParametroSistemaGeralMap();
		if (ps != null) {
			sqlBuilder.append(String.format(" '%s' as codEstabelecimentoEmissor,  ", ps.get("FTF_CODIGO_ESTABELECIMENTO_EMISSOR").getValor()));
			sqlBuilder.append(String.format(" '%s' as cnpjEstabelecimentoEmissor,  ", ps.get("FTF_CNPJ_ESTABELECIMENTO_EMISSOR").getValor()));
			sqlBuilder.append(String.format(" '%s' as codLocal,  ", ps.get("FTF_CODIGO_LOCAL").getValor()));
		}
	}

	private List<Long> obterIdsFrom(List<NotaFiscal> notas){
		int length = notas.size();
		List<Long> ids = new ArrayList<>();

		for (int i = 0; i < length; i++) {
			ids.add(notas.get(i).getId());
		}
		return ids;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FTFEnvTipoRegistro02> obterResgistroTipo02(Long idNF, long idTipoNotaFiscal) {

		StringBuilder sqlBuilder = new StringBuilder();

		sqlBuilder.append(" select ")
		.append("  '2' as tipoRegistro, ");
		setParametersFromDistribuidor(sqlBuilder);

		concatenarTipoPedidoBy(idTipoNotaFiscal, sqlBuilder);
		sqlBuilder.append(" LPAD(nfn.NUMERO_DOCUMENTO_FISCAL, 8, '0') as numeroDocOrigem, ")
		.append("  cast(nfps.SEQUENCIA as char) as numItemPedido, ")
		.append("  '' as codSistemaOrigemPedido,  ") //TODO: -- Deve ser cadastrado no FTF, ver com a Edna.
		.append("  nfps.CODIGO_PRODUTO as codProdutoOuServicoSistemaOrigem, ")
		.append("  cast(nfps.quantidade_comercial as char) as qtdeProdutoOuServico, ")
		.append("  replace(cast(nfps.VALOR_UNITARIO_COMERCIAL as char),'.','') as valorUnitario, ")
		.append("  '' as valorBrutoTabela, ")
		.append("  '' as valorPrecoSubstituicao, ")
		.append("  replace(cast(round(valor_desconto,2) as char),'.','') as percentualDescontoItem, ")
		.append("  '' as valorDescontoComercial, ")
		.append("  '' as tipoUtilizacaoProduto, ")
		.append("  cast(RIGHT(nfps.CFOP, 3) as char) as codTipoNaturecaOperacao, ")
		.append("  '' as textoObservacoes, ")
		.append("  '' as numEdicaoRevista, ")
		.append("  '' as dataCompetencia, ")
		.append("  cast(nfps.CODIGO_BARRAS as char) as codBarrasProduto, ")
		.append("  '' as indicadorProdutoServicoMaterial, ") //TODO: -- aguardando resposta equipe de negócio
		.append("  '' as codMaterialOuServicoCorporativo, ")
		.append("  '' as novoCodigoTipoNaturezaOperacao, ")
		.append("  nfps.DESCRICAO_PRODUTO as descricaoProduto, ")
		.append("  '' as codEanProduto ")
		.append(" from nota_fiscal_produto_servico nfps ")
		.append(" inner join nota_fiscal_novo nfn ON nfn.id = nfps.NOTA_FISCAL_ID ")
		.append(" left join natureza_operacao no ON no.ID = nfn.NATUREZA_OPERACAO_ID ")
		.append(" where nfps.NOTA_FISCAL_ID = :idNF ");

		SQLQuery query = getSession().createSQLQuery(sqlBuilder.toString());
		query.setParameter("idNF", idNF);

		query.setResultTransformer(new AliasToBeanResultTransformer(FTFEnvTipoRegistro02.class));

		List<FTFEnvTipoRegistro02> list = query.list();
		return list;
	}

	@Override
	public FTFEnvTipoRegistro09 obterRegistroTipo09(long idTipoNotaFiscal) {

		FTFEnvTipoRegistro09 reg09 = new FTFEnvTipoRegistro09();
		reg09.setTipoRegistro("9");
		reg09.setTipoPedido("99");

		setCommonsParameters(reg09);

		reg09.setNumeroDocOrigem("99999999");

		return reg09;
	}

	private void setCommonsParameters(FTFCommons object) {

		Map<String, ParametroSistema> ps = parametroSistemaRepository.buscarParametroSistemaGeralMap();
		if (ps != null) {
			object.setCodEstabelecimentoEmissor(ps.get("FTF_CODIGO_ESTABELECIMENTO_EMISSOR").getValor());
			object.setCnpjEstabelecimentoEmissor(ps.get("FTF_CNPJ_ESTABELECIMENTO_EMISSOR").getValor());
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

	@Override
	public FTFEnvTipoRegistro06 obterRegistroTipo06(final Long idNF) {

		StringBuilder sb = new StringBuilder();

		sb.append("select nfr from NotaFiscalReferenciada nfr ")
		.append(" left join nfr.pk ")
		.append(" left join nfr.pk.notaFiscal nf ")
		.append(" where nf.id = :idNF ");


		Query query = getSession().createQuery(sb.toString());
		query.setParameter("idNF", idNF);

		NotaFiscalReferenciada nota = (NotaFiscalReferenciada) query.uniqueResult();

		FTFEnvTipoRegistro06 tipoRegistro06 = null;
		if(nota!=null){

			NotaFiscal notaFiscal = nota.getPk().getNotaFiscal();

			if (notaFiscal != null) {
				tipoRegistro06 = new FTFEnvTipoRegistro06();

				tipoRegistro06.setTipoRegistro("6");
				setCommonsParameters(tipoRegistro06);
				tipoRegistro06.setTipoPedido("2");
				tipoRegistro06.setNumeroDocOrigem(notaFiscal.getId().toString());
				tipoRegistro06.setNumDocumentoOrigemAssociado("");//TODO

			}

		}


		return tipoRegistro06;

	}


	@Override
	public FTFEnvTipoRegistro08 obterRegistroTipo08(long idTipoNotaFiscal) {
		StringBuilder sb = new StringBuilder();

		sb.append(" select  ")
		.append(" nfn.DOCUMENTO_EMITENTE as cnpjEstabelecimentoEmissor, ")
		.append(" '' as codLocal, ")
		.append(" cast(nfn.TIPO_EMISSAO as char) as tipoPedido, ")
		.append(" '' as numeroDocOrigem, ")
		.append(" COALESCE(pessoa.NOME,'') as nomeDoCliente, ")
		.append(" '' as cpfOuCnpj, ")
		.append(" endereco.LOGRADOURO as endereco, ")
		.append(" endereco.NUMERO as numeroEndereco, ")
		.append(" COALESCE(endereco.COMPLEMENTO,'') as  complementoEndereco, ")
		.append(" endereco.BAIRRO as bairro, ")
		.append(" endereco.CIDADE as cidade, ")
		.append(" cast(endereco.CODIGO_CIDADE_IBGE as char) as codigoIBGE, ")
		.append(" endereco.UF as siglaEstado, ")
		.append(" endereco.CEP as cep, ")
		.append(" '' as codigoPais, ")
		.append(" '' as nomePais, ")
		.append(" '' as codigoCGL, ")
		.append(" '' as ddi, ")
		.append(" telefone.DDD as ddd, ")
		.append(" cast(telefone.NUMERO as char) as telefone, ")
		.append(" '' as complementoTelefone ")
		.append("  ")
		.append(" from nota_fiscal_novo nfn ")
		.append(" left join nota_fiscal_pessoa pessoa ON pessoa.ID = nfn.PESSOA_DESTINATARIO_ID_REFERENCIA ")
		.append(" left join nota_fiscal_endereco endereco ON endereco.ID = nfn.ENDERECO_ID_DESTINATARIO ")
		.append(" left join nota_fiscal_telefone telefone ON telefone.ID = nfn.TELEFONE_ID_DESTINATARIO ")
		.append(" where nfn.id = :idNF ");

		SQLQuery query = getSession().createSQLQuery(sb.toString());
		query.setParameter("idNF", idTipoNotaFiscal);

		query.setResultTransformer(new AliasToBeanResultTransformer(FTFEnvTipoRegistro08.class));

		Object uniqueResult = query.uniqueResult();

		FTFEnvTipoRegistro08 reg08 = (FTFEnvTipoRegistro08) uniqueResult;
		setCommonsParameters(reg08);
		
		return reg08;

	}
}

