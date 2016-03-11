package br.com.abril.nds.repository.impl;

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.P3DTO;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.repository.AbstractRepository;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.repository.P3Repository;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@Repository
public class P3RepositoryImpl extends AbstractRepository implements
		P3Repository {

	@Autowired
	private DistribuidorRepository distribuidoRepo;

	@Override
	public Boolean isRegimeEspecial() {

		StringBuilder sql = new StringBuilder();

		sql.append(" SELECT POSSUI_REGIME_ESPECIAL_DISPENSA_INTERNA FROM distribuidor ");
		
		SQLQuery query = this.getSession().createSQLQuery(sql.toString());
		
		query.addScalar("POSSUI_REGIME_ESPECIAL_DISPENSA_INTERNA", StandardBasicTypes.BOOLEAN);
		
		return (Boolean) query.uniqueResult();
		
//		javax.persistence.Query query = this.getSession().createSQLQuery(sql.toString());

		 
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<P3DTO> obterP3SemRegimeEspecial_Entrada(Date dataInicial,
			Date dataFinal) {

		StringBuilder sql = new StringBuilder();

		Distribuidor dist = distribuidoRepo.obter();

		sql.append(" SELECT ");
		sql.append(" cast(:codEmpresa as char) codEmpresa, ");
		sql.append(" cast(:codFilial as char) codFilial, ");
		sql.append(" '00180' AS naturezaEstoque, ");
		sql.append(" DATE_FORMAT(lc.data_lcto_distribuidor, '%d/%m/%Y') dataLancamento, ");
		sql.append(" p.codigo AS codMaterial, ");
		sql.append(" '  ESE' AS tipoOperacao, ");
		sql.append(" replace(cast(round(infe.qtde,3) as char),'.',',') quantidade, ");
		sql.append(" 'E' AS indLancamento, ");
		sql.append(" concat(DATE_FORMAT(CURRENT_DATE, '%Y%m'), distrib.codigo, "
				+ " nfe.numero, p.CODIGO, pe.NUMERO_EDICAO)"
				+ " AS numArquivamento, ");

		sql.append(" 	CASE ");
		sql.append("  		WHEN nfe.chave_acesso is not null ");
		sql.append(" 		THEN 'NFE' ");
		sql.append(" 		ELSE ''  ");
		sql.append(" END AS tipoDocumento, ");

		sql.append(" cast(nfe.numero as char) numDocumento, ");
		sql.append(" nfe.serie AS serDocumento, ");
		sql.append(" '' AS numSequencialItem, ");
		sql.append(" '' AS numSerieMaterial, ");

		sql.append(" cast(CASE WHEN no.TIPO_EMITENTE ='COTA' OR no.TIPO_DESTINATARIO ='COTA' ");
		sql.append(" 	THEN 'CL' ");
		sql.append("    ELSE 'FO' ");
		sql.append(" END as char) categoriaPfPj, ");

		sql.append(" CASE ");
		sql.append(" WHEN pes.cnpj is not null THEN pes.cnpj ");
		sql.append(" WHEN pes.cpf is not null THEN pes.cpf ");
		sql.append(" ELSE '' ");
		sql.append(" END codigoPfPj, ");

		sql.append(" cast(1 as char) localizacao, ");

		sql.append(" replace(cast(round(infe.preco, 4) as char),'.',',') vlrUnitario, ");
		sql.append(" replace(cast(round(nfe.valor_bruto, 2) as char),'.',',') vlrTotal, ");
		sql.append(" replace(cast(round(infe.preco, 4) as char),'.',',') custoUnitario, ");
		sql.append(" replace(cast(round(nfe.valor_bruto, 2) as char),'.',',') custoTotal, ");

		sql.append(" '' AS contaEstoque, ");
		sql.append(" '' AS contraPartida, ");
		sql.append(" '' AS contratoServico, ");
		sql.append(" '' AS centroCusto, ");
		sql.append(" cfop.codigo AS cfop, ");

		sql.append(" 	CASE ");
		sql.append("  		WHEN infe.aliquota_ipi_produto is not null ");
		sql.append(" 		THEN replace(cast(round(infe.aliquota_ipi_produto, 2) as char),'.',',') ");
		sql.append(" 		ELSE ''  ");
		sql.append(" END vlrIpi, ");

		sql.append(" '' AS pagLivroFiscal, ");
		sql.append(" '' AS numLote, ");
		sql.append(" '' AS docEstornado, ");
		sql.append(" '' AS itemEstornado, ");
		sql.append(" '' AS divisao, ");
		sql.append(" cast(YEAR(nfe.data_emissao) as char) anoDocumento, ");
		sql.append(" '' AS observacao, ");
		sql.append(" 'NDS' AS openflex01, ");
		sql.append(" cast(NOW() as char) AS openflex02, ");
		sql.append(" concat(p.CODIGO, pe.NUMERO_EDICAO) AS openflex03, ");
		sql.append(" 'NDS' AS openflex04, ");
		sql.append(" '' AS openflex05, ");
		sql.append(" '' AS openflex06, ");
		sql.append(" '' AS openflex07, ");
		sql.append(" '' AS openflex08 ");

		sql.append(" FROM distribuidor AS distrib, ");
		sql.append("     nota_fiscal_entrada AS nfe ");
		sql.append(" INNER JOIN ");
		sql.append("     item_nota_fiscal_entrada AS infe ");
		sql.append("         ON infe.nota_fiscal_id = nfe.id ");
		sql.append(" INNER JOIN  ");
		sql.append("     produto_edicao AS pe ");
		sql.append("         ON pe.id = infe.produto_edicao_id ");
		sql.append(" INNER JOIN ");
		sql.append("     produto AS p ");
		sql.append("         ON pe.produto_id = p.id ");
		sql.append(" INNER JOIN ");
		sql.append("     lancamento AS lc ");
		sql.append("         ON pe.id = lc.produto_edicao_id ");
		sql.append(" INNER JOIN ");
		sql.append("     cfop ");
		sql.append("         ON cfop.id = nfe.cfop_id ");
		sql.append(" LEFT JOIN ");
		sql.append("     pessoa pes ");
		sql.append("         ON pes.id = nfe.pj_id ");
		sql.append(" INNER JOIN ");
		sql.append("     natureza_operacao no  ");
		sql.append("         ON nfe.NATUREZA_OPERACAO_ID = no.ID ");
		
		sql.append(" WHERE ");

		sql.append(" nfe.data_emissao BETWEEN :dataInicial AND :dataFinal ");

		sql.append(" AND NATUREZA_OPERACAO_ID NOT IN (2,3,9,11,28) order by nfe.data_recebimento ");
		
		Query query = this.getSession().createSQLQuery(sql.toString());

		query.setParameter("dataInicial", dataInicial);
		query.setParameter("dataFinal", dataFinal);
		query.setParameter("codEmpresa", dist.getJuridica().getCnpj()
				.substring(0, dist.getJuridica().getCnpj().indexOf("/"))
				.replace(".", ""));
		query.setParameter("codFilial", dist.getCodigo().toString());

		query.setResultTransformer(new AliasToBeanResultTransformer(P3DTO.class));

		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<P3DTO> obterP3SemRegimeEspecial_Saida(Date dataInicial,
			Date dataFinal) {
		StringBuilder sql = new StringBuilder();

		Distribuidor dist = distribuidoRepo.obter();

		sql.append(" SELECT ");
		sql.append(" cast(:codEmpresa as char) codEmpresa, ");
		sql.append(" cast(:codFilial as char) codFilial, ");
		sql.append(" '00180' AS naturezaEstoque, ");
		sql.append(" DATE_FORMAT(lc.data_lcto_distribuidor, '%d/%m/%Y') dataLancamento, ");
		sql.append(" p.codigo AS codMaterial, ");
		sql.append(" 'BAIXA' AS tipoOperacao, ");
		sql.append(" replace(cast(round(infs.qtde,3) AS char),'.',',') quantidade, ");
		sql.append(" '  ESS' AS indLancamento, ");
		sql.append(" concat(DATE_FORMAT(CURRENT_DATE, '%Y%m'), distrib.codigo, nfs.numero"
				+ ", p.CODIGO, pe.NUMERO_EDICAO)"
				+ " AS numArquivamento, ");
		
		
		sql.append(" 	CASE ");
		sql.append("  		WHEN nfs.chave_acesso is not null ");
		sql.append(" 		THEN 'NFE' ");
		sql.append(" 		ELSE ''  ");
		sql.append(" END AS tipoDocumento, ");
		sql.append(" cast(nfs.numero as char) numDocumento, ");
		sql.append(" nfs.serie AS serDocumento, ");
		sql.append(" '' AS numSequencialItem, ");
		sql.append(" '' AS numSerieMaterial, ");

		sql.append(" cast(CASE WHEN no.TIPO_EMITENTE ='COTA' OR no.TIPO_DESTINATARIO ='COTA' ");
		sql.append(" 	THEN 'CL' ");
		sql.append("    ELSE 'FO' ");
		sql.append(" END as char) categoriaPfPj, ");

		sql.append(" CASE ");
		sql.append(" WHEN pes.cnpj is not null THEN pes.cnpj ");
		sql.append(" WHEN pes.cpf is not null THEN pes.cpf ");
		sql.append(" ELSE '' ");
		sql.append(" END codigoPfPj, ");

		sql.append(" cast(1 as char) localizacao, ");

		sql.append(" replace(cast(round(pe.preco_venda, 4) as char),'.',',') vlrUnitario, ");
		sql.append(" replace(cast(round((pe.preco_venda)*infs.qtde, 2) AS char),'.',',') vlrTotal, ");
		sql.append(" replace(cast(round(pe.preco_venda, 4) as char),'.',',') custoUnitario, ");
		sql.append(" replace(cast(round((pe.preco_venda)*infs.qtde, 2) AS char),'.',',') custoTotal, ");

		sql.append(" '' AS contaEstoque, ");
		sql.append(" '' AS contraPartida, ");
		sql.append(" '' AS contratoServico, ");
		sql.append(" '' AS centroCusto, ");
		sql.append(" cfop.codigo AS cfop, ");

		sql.append(" 	CASE ");
		sql.append("  		WHEN infs.aliquota_ipi_produto is not null ");
		sql.append(" 		THEN replace(cast(round(infs.aliquota_ipi_produto, 2) as char),'.',',') ");
		sql.append(" 		ELSE ''  ");
		sql.append(" END vlrIpi, ");

		sql.append(" '' AS pagLivroFiscal, ");
		sql.append(" '' AS numLote, ");
		sql.append(" '' AS docEstornado, ");
		sql.append(" '' AS itemEstornado, ");
		sql.append(" '' AS divisao, ");
		sql.append(" cast(YEAR(nfs.data_emissao) as char) anoDocumento, ");
		sql.append(" '' AS observacao, ");
		sql.append(" 'NDS' AS openflex01, ");
		sql.append(" cast(NOW() as char) AS openflex02, ");
		sql.append(" concat(p.CODIGO, pe.NUMERO_EDICAO ) AS openflex03, ");
		sql.append(" 'NDS' AS openflex04, ");
		sql.append(" '' AS openflex05, ");
		sql.append(" '' AS openflex06, ");
		sql.append(" '' AS openflex07, ");
		sql.append(" '' AS openflex08 ");

		sql.append(" FROM distribuidor AS distrib, ");
		sql.append("     nota_fiscal_saida AS nfs ");
		sql.append(" INNER JOIN ");
		sql.append("     item_nota_fiscal_saida AS infs ");
		sql.append("         ON infs.nota_fiscal_id = nfs.id ");
		sql.append(" INNER JOIN  ");
		sql.append("     produto_edicao AS pe ");
		sql.append("         ON infs.produto_edicao_id = pe.id  ");
		sql.append(" INNER JOIN ");
		sql.append("     produto AS p ");
		sql.append("         ON pe.produto_id = p.id ");
		sql.append(" INNER JOIN ");
		sql.append("     lancamento AS lc ");
		sql.append("         ON pe.id = lc.produto_edicao_id ");
		sql.append(" INNER JOIN ");
		sql.append("     cfop   ");
		sql.append("         ON nfs.cfop_id = cfop.id ");
		sql.append(" LEFT JOIN ");
		sql.append("     pessoa pes ");
		sql.append("         ON pes.id = nfs.pj_id ");
		sql.append(" INNER JOIN ");
		sql.append("     natureza_operacao no  ");
		sql.append("         ON nfs.NATUREZA_OPERACAO = no.ID ");
		
		sql.append(" WHERE ");

		sql.append(" nfs.data_emissao BETWEEN :dataInicial AND :dataFinal ");

		sql.append(" AND nfs.TIPO_NF_ID NOT IN (2,3,4,9,10,11,28,29) order by infs.NOTA_FISCAL_ID ");
		
//		TODO confirmar na homologação, qual amarração estará correta (NATUREZA_OPERACAO_ID || TIPO_NF_ID)
		//		sql.append(" AND NATUREZA_OPERACAO_ID NOT IN (2,3,9,11,28) order by infs.NOTA_FISCAL_ID ");

		Query query = this.getSession().createSQLQuery(sql.toString());

		query.setParameter("dataInicial", dataInicial);
		query.setParameter("dataFinal", dataFinal);
		query.setParameter("codEmpresa", dist.getJuridica().getCnpj()
				.substring(0, dist.getJuridica().getCnpj().indexOf("/"))
				.replace(".", ""));
		query.setParameter("codFilial", dist.getCodigo().toString());

		query.setResultTransformer(new AliasToBeanResultTransformer(P3DTO.class));

		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<P3DTO> obterP3ComRegimeEspecial_NotaEnvio(Date dataInicial,
			Date dataFinal) {

		StringBuilder sql = new StringBuilder();

		Distribuidor dist = distribuidoRepo.obter();

		sql.append(" SELECT ");
		sql.append(" cast(:codEmpresa as char) codEmpresa, ");
		sql.append(" cast(:codFilial as char) codFilial, ");
		sql.append(" '00180' AS naturezaEstoque, ");
		sql.append(" DATE_FORMAT(lc.data_lcto_distribuidor, '%d/%m/%Y') dataLancamento, ");
		sql.append(" p.codigo AS codMaterial, ");
		sql.append(" '  ESS' AS tipoOperacao, ");

		sql.append(" 	replace(cast(round(CASE ");
		sql.append("  		WHEN infs.qtde is not null ");
		sql.append(" 		THEN infs.qtde  ");
		sql.append(" 		ELSE 0  ");
		sql.append(" END, 3) AS char),'.',',') quantidade, ");

		sql.append(" 'S' AS indLancamento, ");
		sql.append(" concat(DATE_FORMAT(CURRENT_DATE, '%Y%m'), distrib.codigo, "
				+ " ne.numero, p.CODIGO, pe.NUMERO_EDICAO)"
				+ " AS numArquivamento, ");
		sql.append(" 	CASE ");
		sql.append("  		WHEN ne.chave_acesso is not null ");
		sql.append(" 		THEN 'NFE' ");
		sql.append(" 		ELSE ''  ");
		sql.append(" END AS tipoDocumento, ");
		sql.append(" cast(ne.numero as char) numDocumento, ");

		sql.append(" 	CASE ");
		sql.append("  		WHEN nfs.serie is not null ");
		sql.append(" 		THEN nfs.serie  ");
		sql.append(" 		ELSE ''  ");
		sql.append(" END AS serDocumento, ");

		sql.append(" cast(nei.sequencia AS char) numSequencialItem, ");
		sql.append(" '' AS numSerieMaterial, ");
		sql.append(" cast(CASE WHEN no.TIPO_EMITENTE ='COTA' OR no.TIPO_DESTINATARIO ='COTA' ");
		sql.append(" 	THEN 'CL' ");
		sql.append("    ELSE 'FO' ");
		sql.append(" END as char) categoriaPfPj, ");

		sql.append(" CASE ");
		sql.append(" WHEN pes.cnpj is not null THEN pes.cnpj ");
		sql.append(" WHEN pes.cpf is not null THEN pes.cpf ");
		sql.append(" ELSE '' ");
		sql.append(" END codigoPfPj, ");

		sql.append(" cast(1 as char) localizacao, ");

		sql.append(" replace(cast(round(nei.preco_capa, 4) as char),'.',',') vlrUnitario, ");

		sql.append(" 	replace(cast(round(CASE ");
		sql.append("  		WHEN pe.preco_venda is not null && infs.qtde is not null THEN (pe.preco_venda)*infs.qtde ");
		sql.append(" 		ELSE 0 ");
		sql.append(" END, 2) AS char),'.',',') vlrTotal, ");

		sql.append(" replace(cast(round(nei.preco_capa, 4) as char),'.',',') custoUnitario, ");

		sql.append(" 	replace(cast(round(CASE ");
		sql.append("  		WHEN pe.preco_venda is not null && infs.qtde is not null THEN (pe.preco_venda)*infs.qtde ");
		sql.append(" 		ELSE '' ");
		sql.append(" END, 2) AS char),'.',',') custoTotal, ");

		sql.append(" '' AS contaEstoque, ");
		sql.append(" '' AS contraPartida, ");
		sql.append(" '' AS contratoServico, ");
		sql.append(" '' AS centroCusto, ");

		sql.append(" 	CASE ");
		sql.append("  		WHEN cfop.codigo is not null ");
		sql.append(" 		THEN cfop.codigo  ");
		sql.append(" 		ELSE ''  ");
		sql.append(" END AS cfop, ");

		sql.append(" 	CASE ");
		sql.append("  		WHEN infs.aliquota_ipi_produto is not null ");
		sql.append(" 		THEN replace(cast(round(infs.aliquota_ipi_produto, 2) as char),'.',',') ");
		sql.append(" 		ELSE ''  ");
		sql.append(" END vlrIpi, ");

		sql.append(" '' AS pagLivroFiscal, ");
		sql.append(" '' AS numLote, ");
		sql.append(" '' AS docEstornado, ");
		sql.append(" '' AS itemEstornado, ");
		sql.append(" '' AS divisao, ");
		sql.append(" cast(YEAR(ne.dataEmissao) as char) anoDocumento, ");
		sql.append(" '' AS observacao, ");
		sql.append(" 'NDS' AS openflex01, ");
		sql.append(" cast(NOW() as char) AS openflex02, ");
		sql.append(" concat(p.CODIGO, pe.NUMERO_EDICAO) AS openflex03, ");
		sql.append(" 'NDS' AS openflex04, ");
		sql.append(" '' AS openflex05, ");
		sql.append(" '' AS openflex06, ");
		sql.append(" '' AS openflex07, ");
		sql.append(" '' AS openflex08 ");

		sql.append(" FROM distribuidor AS distrib, ");
		sql.append("     nota_envio AS ne ");
		sql.append(" INNER JOIN ");
		sql.append("     nota_envio_item AS nei ");
		sql.append("         ON nei.nota_envio_id = ne.numero ");
		sql.append(" INNER JOIN  ");
		sql.append("     produto_edicao AS pe ");
		sql.append("         ON pe.id = nei.produto_edicao_id ");
		sql.append(" INNER JOIN ");
		sql.append("     produto AS p ");
		sql.append("         ON pe.produto_id = p.id ");
		sql.append(" INNER JOIN ");
		sql.append("     lancamento AS lc ");
		sql.append("         ON pe.id = lc.produto_edicao_id ");
		sql.append(" LEFT JOIN ");
		sql.append("     item_nota_fiscal_saida infs  ");
		sql.append("         ON infs.produto_edicao_id = pe.id ");
		sql.append(" LEFT JOIN ");
		sql.append("     nota_fiscal_saida nfs ");
		sql.append("         ON infs.nota_fiscal_id = nfs.id ");
		sql.append(" LEFT JOIN ");
		sql.append("     natureza_operacao no  ");
		sql.append("         ON nfs.TIPO_NF_ID = no.ID ");
		sql.append(" INNER JOIN ");
		sql.append("     pessoa pes ");
		sql.append("         ON ne.pessoa_destinatario_id_referencia = pes.id ");
		sql.append(" LEFT JOIN ");
		sql.append("     cfop  ");
		sql.append("         ON nfs.cfop_id = cfop.id ");

		sql.append(" WHERE ");

		sql.append(" ne.dataEmissao BETWEEN :dataInicial AND :dataFinal order by nei.nota_envio_id ");

		Query query = this.getSession().createSQLQuery(sql.toString());

		query.setParameter("dataInicial", dataInicial);
		query.setParameter("dataFinal", dataFinal);
		query.setParameter("codEmpresa", dist.getJuridica().getCnpj()
				.substring(0, dist.getJuridica().getCnpj().indexOf("/"))
				.replace(".", ""));
		query.setParameter("codFilial", dist.getCodigo().toString());

		query.setResultTransformer(new AliasToBeanResultTransformer(P3DTO.class));

		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<P3DTO> obterP3ComRegimeEspecial_NotaFiscalNovo(
			Date dataInicial, Date dataFinal) {

		StringBuilder sql = new StringBuilder();

		Distribuidor dist = distribuidoRepo.obter();

		sql.append(" SELECT ");
		sql.append(" cast(:codEmpresa as char) codEmpresa, ");
		sql.append(" cast(:codFilial as char) codFilial, ");
		sql.append(" '00180' AS naturezaEstoque, ");
		sql.append(" DATE_FORMAT(lc.data_lcto_distribuidor, '%d/%m/%Y') dataLancamento, ");
		sql.append(" p.codigo AS codMaterial, ");

		sql.append(" 	CASE ");
		sql.append("  		WHEN no.TIPO_OPERACAO='ENTRADA'");
		sql.append(" 		THEN '  ESE' ");
		sql.append(" 		ELSE '  ESS'  ");
		sql.append(" END AS tipoOperacao, ");

		sql.append(" 	replace(cast(round(CASE ");
		sql.append("  		WHEN infe.QTDE is not null ");
		sql.append(" 		THEN infe.QTDE  ");
		sql.append(" 		ELSE 0  ");
		sql.append(" END, 3) AS char),'.',',') quantidade, ");

		sql.append(" 	cast(CASE ");
		sql.append("  		WHEN no.TIPO_OPERACAO='ENTRADA'");
		sql.append(" 		THEN 'E' ");
		sql.append(" 		ELSE 'S'  ");
		sql.append(" END AS char) indLancamento, ");

		sql.append(" concat(DATE_FORMAT(CURRENT_DATE, '%Y%m'), distrib.codigo, "
				+ " nfn.numero_documento_fiscal, p.CODIGO, pe.NUMERO_EDICAO)"
				+ " AS numArquivamento, ");

		sql.append(" 	CASE ");
		sql.append("  		WHEN nfn.chave_acesso is not null ");
		sql.append(" 		THEN 'NFE' ");
		sql.append(" 		ELSE ''  ");
		sql.append(" END AS tipoDocumento, ");

		sql.append(" cast(nfn.numero_documento_fiscal as char) numDocumento, ");
		sql.append(" cast(nfn.serie as char) serDocumento, ");
		sql.append(" '' AS numSequencialItem, ");
		sql.append(" '' AS numSerieMaterial, ");

		sql.append(" cast(CASE WHEN no.TIPO_EMITENTE ='COTA' OR no.TIPO_DESTINATARIO ='COTA' ");
		sql.append(" 	THEN 'CL' ");
		sql.append("    ELSE 'FO' ");
		sql.append(" END as char) categoriaPfPj, ");

		sql.append(" CASE ");
		sql.append(" WHEN pes.cnpj is not null THEN pes.cnpj ");
		sql.append(" WHEN pes.cpf is not null THEN pes.cpf ");
		sql.append(" ELSE '' ");
		sql.append(" END codigoPfPj, ");

		sql.append(" cast(1 as char) localizacao, ");

		sql.append(" 	replace(cast(round(CASE ");
		sql.append("  		WHEN no.TIPO_OPERACAO='ENTRADA' && infe.preco is not null && infe.desconto is not null THEN (infe.preco - infe.desconto) ");
		sql.append(" 		WHEN no.TIPO_OPERACAO='SAIDA' && pe.preco_venda is not null THEN (pe.preco_venda) ");
		sql.append(" 		ELSE 0  ");
		sql.append(" END, 4) AS char),'.',',') vlrUnitario, ");

		sql.append(" 	replace(cast(round(CASE ");
		sql.append("  		WHEN no.TIPO_OPERACAO='ENTRADA' && infe.preco is not null && infe.desconto is not null && infe.qtde is not null THEN (infe.preco - infe.desconto)*infe.qtde ");
		sql.append(" 		WHEN no.TIPO_OPERACAO='SAIDA' && pe.preco_venda is not null && infs.qtde is not null THEN (pe.preco_venda)*infs.qtde ");
		sql.append(" 		ELSE 0 ");
		sql.append(" END, 2) AS char),'.',',') vlrTotal, ");

		sql.append(" 	replace(cast(round(CASE ");
		sql.append("  		WHEN no.TIPO_OPERACAO='ENTRADA' && infe.preco is not null && infe.desconto is not null THEN (infe.preco - infe.desconto) ");
		sql.append(" 		WHEN no.TIPO_OPERACAO='SAIDA' && pe.preco_venda is not null THEN (pe.preco_venda) ");
		sql.append(" 		ELSE ''  ");
		sql.append(" END, 4) AS char),'.',',') custoUnitario, ");

		sql.append(" 	replace(cast(round(CASE ");
		sql.append("  		WHEN no.TIPO_OPERACAO='ENTRADA' && infe.preco is not null && infe.desconto is not null && infe.qtde is not null THEN (infe.preco - infe.desconto)*infe.qtde ");
		sql.append(" 		WHEN no.TIPO_OPERACAO='SAIDA' && pe.preco_venda is not null && infs.qtde is not null THEN (pe.preco_venda)*infs.qtde ");
		sql.append(" 		ELSE '' ");
		sql.append(" END, 2) AS char),'.',',') custoTotal, ");

		sql.append(" '' AS contaEstoque, ");
		sql.append(" '' AS contraPartida, ");
		sql.append(" '' AS contratoServico, ");
		sql.append(" '' AS centroCusto, ");
		sql.append(" cfop.codigo AS cfop, ");

		sql.append(" 	replace(cast(round(CASE ");
		sql.append("  		WHEN no.TIPO_OPERACAO='ENTRADA' && infe.aliquota_ipi_produto is not null THEN infe.aliquota_ipi_produto ");
		sql.append(" 		WHEN no.TIPO_OPERACAO='SAIDA' && infs.aliquota_ipi_produto is not null THEN infs.aliquota_ipi_produto ");
		sql.append(" 		ELSE '' ");
		sql.append(" END, 2) AS char),'.',',') vlrIpi, ");

		sql.append(" '' AS pagLivroFiscal, ");
		sql.append(" '' AS numLote, ");
		sql.append(" '' AS docEstornado, ");
		sql.append(" '' AS itemEstornado, ");
		sql.append(" '' AS divisao, ");
		sql.append(" cast(YEAR(nfn.data_emissao) as char) anoDocumento, ");
		sql.append(" '' AS observacao, ");
		sql.append(" 'NDS' AS openflex01, ");
		sql.append(" cast(NOW() as char) AS openflex02, ");
		sql.append(" concat(p.CODIGO, pe.NUMERO_EDICAO ) AS openflex03, ");
		sql.append(" 'NDS' AS openflex04, ");
		sql.append(" '' AS openflex05, ");
		sql.append(" '' AS openflex06, ");
		sql.append(" '' AS openflex07, ");
		sql.append(" '' AS openflex08 ");

		sql.append(" FROM distribuidor AS distrib, ");
		sql.append("     nota_fiscal_novo nfn ");
		sql.append(" INNER JOIN ");
		sql.append("     nota_fiscal_produto_servico nfps ");
		sql.append("         ON nfps.NOTA_FISCAL_ID = nfn.id ");
		sql.append(" INNER JOIN  ");
		sql.append("     produto_edicao AS pe ");
		sql.append("         ON nfps.produto_edicao_id = pe.id ");
		sql.append(" INNER JOIN ");
		sql.append("     produto AS p ");
		sql.append("         ON pe.produto_id = p.id ");
		sql.append(" INNER JOIN ");
		sql.append("     lancamento AS lc ");
		sql.append("         ON  lc.produto_edicao_id = pe.id ");
		sql.append(" LEFT JOIN ");
		sql.append("     pessoa pes  ");
		sql.append("         ON nfn.pessoa_destinatario_id_referencia = pes.id ");
		sql.append(" LEFT JOIN ");
		sql.append("     natureza_operacao no ");
		sql.append("         ON nfn.NATUREZA_OPERACAO_ID = no.ID ");
		sql.append(" LEFT JOIN ");
		sql.append("     cfop  ");
		sql.append("         ON no.CFOP_ESTADO = cfop.ID ");
		sql.append(" LEFT JOIN ");
		sql.append("     item_nota_fiscal_entrada infe ");
		sql.append("         ON infe.produto_edicao_id = pe.id ");
		sql.append(" LEFT JOIN ");
		sql.append("     nota_fiscal_entrada nfe ");
		sql.append("         ON infe.NOTA_FISCAL_ID = nfe.ID ");
		sql.append(" LEFT JOIN ");
		sql.append("     nota_fiscal_saida nfs ");
		sql.append("         ON nfs.NATUREZA_OPERACAO = no.id ");
		sql.append(" LEFT JOIN ");
		sql.append("      item_nota_fiscal_saida infs ");
		sql.append("         ON infs.produto_edicao_id = pe.id ");

		sql.append(" WHERE ");

		sql.append(" nfn.data_emissao BETWEEN :dataInicial AND :dataFinal ");
		sql.append(" AND nfn.NATUREZA_OPERACAO_ID NOT IN (2,3,9,11,28) order by nfps.nota_fiscal_id ");

		Query query = this.getSession().createSQLQuery(sql.toString());

		query.setParameter("dataInicial", dataInicial);
		query.setParameter("dataFinal", dataFinal);
		query.setParameter("codEmpresa", dist.getJuridica().getCnpj()
				.substring(0, dist.getJuridica().getCnpj().indexOf("/"))
				.replace(".", ""));
		query.setParameter("codFilial", dist.getCodigo().toString());

		query.setResultTransformer(new AliasToBeanResultTransformer(P3DTO.class));

		return query.list();
	}

	@Override
	public Integer count_obterP3SemRegimeEspecial_Entrada(Date dataInicial,
			Date dataFinal) {

		StringBuilder sql = new StringBuilder();

		sql.append(" SELECT ");

		sql.append(" count(*) ");

		sql.append(" FROM ");
		sql.append("     nota_fiscal_entrada AS nfe ");
		sql.append(" INNER JOIN ");
		sql.append("     item_nota_fiscal_entrada AS infe ");
		sql.append("         ON infe.nota_fiscal_id = nfe.id ");
		sql.append(" INNER JOIN  ");
		sql.append("     produto_edicao AS pe ");
		sql.append("         ON pe.id = infe.produto_edicao_id ");
		sql.append(" INNER JOIN ");
		sql.append("     produto AS p ");
		sql.append("         ON pe.produto_id = p.id ");
		sql.append(" INNER JOIN ");
		sql.append("     lancamento AS lc ");
		sql.append("         ON pe.id = lc.produto_edicao_id ");
		sql.append(" INNER JOIN ");
		sql.append("     cfop ");
		sql.append("         ON cfop.id = nfe.cfop_id ");
		sql.append(" LEFT JOIN ");
		sql.append("     pessoa pes ");
		sql.append("         ON pes.id = nfe.pj_id ");
		sql.append(" INNER JOIN ");
		sql.append("     natureza_operacao no  ");
		sql.append("         ON nfe.NATUREZA_OPERACAO_ID = no.ID ");
		
		sql.append(" WHERE ");

		sql.append(" nfe.data_emissao BETWEEN :dataInicial AND :dataFinal ");

		sql.append(" AND NATUREZA_OPERACAO_ID NOT IN (2,3,9,11,28) order by nfe.data_recebimento ");

		Query query = this.getSession().createSQLQuery(sql.toString());

		query.setParameter("dataInicial", dataInicial);
		query.setParameter("dataFinal", dataFinal);

		Object uniqueResult = query.uniqueResult();

		return ((BigInteger) uniqueResult).intValue();
	}

	@Override
	public Integer count_obterP3SemRegimeEspecial_Saida(Date dataInicial,
			Date dataFinal) {

		StringBuilder sql = new StringBuilder();

		sql.append(" SELECT ");

		sql.append(" count(*) ");

		sql.append(" FROM ");
		sql.append("     nota_fiscal_saida AS nfs ");
		sql.append(" INNER JOIN ");
		sql.append("     item_nota_fiscal_saida AS infs ");
		sql.append("         ON infs.nota_fiscal_id = nfs.id ");
		sql.append(" INNER JOIN  ");
		sql.append("     produto_edicao AS pe ");
		sql.append("         ON infs.produto_edicao_id = pe.id  ");
		sql.append(" INNER JOIN ");
		sql.append("     produto AS p ");
		sql.append("         ON pe.produto_id = p.id ");
		sql.append(" INNER JOIN ");
		sql.append("     lancamento AS lc ");
		sql.append("         ON pe.id = lc.produto_edicao_id ");
		sql.append(" INNER JOIN ");
		sql.append("     cfop   ");
		sql.append("         ON nfs.cfop_id = cfop.id ");
		sql.append(" LEFT JOIN ");
		sql.append("     pessoa pes ");
		sql.append("         ON pes.id = nfs.pj_id ");

		sql.append(" WHERE ");

		sql.append(" nfs.data_emissao BETWEEN :dataInicial AND :dataFinal ");

		sql.append(" AND nfs.TIPO_NF_ID NOT IN (2,3,4,9,10,11,28,29) order by infs.NOTA_FISCAL_ID ");

		Query query = this.getSession().createSQLQuery(sql.toString());

		query.setParameter("dataInicial", dataInicial);
		query.setParameter("dataFinal", dataFinal);

		Object uniqueResult = query.uniqueResult();

		return ((BigInteger) uniqueResult).intValue();
	}

	@Override
	public Integer count_obterP3ComRegimeEspecial_NotaEnvio(Date dataInicial,
			Date dataFinal) {

		StringBuilder sql = new StringBuilder();

		sql.append(" SELECT ");

		sql.append(" count(*) ");

		sql.append(" FROM ");
		sql.append("     nota_envio AS ne ");
		sql.append(" INNER JOIN ");
		sql.append("     nota_envio_item AS nei ");
		sql.append("         ON nei.nota_envio_id = ne.numero ");
		sql.append(" INNER JOIN  ");
		sql.append("     produto_edicao AS pe ");
		sql.append("         ON pe.id = nei.produto_edicao_id ");
		sql.append(" INNER JOIN ");
		sql.append("     produto AS p ");
		sql.append("         ON pe.produto_id = p.id ");
		sql.append(" INNER JOIN ");
		sql.append("     lancamento AS lc ");
		sql.append("         ON pe.id = lc.produto_edicao_id ");
		sql.append(" LEFT JOIN ");
		sql.append("     item_nota_fiscal_saida infs  ");
		sql.append("         ON infs.produto_edicao_id = pe.id ");
		sql.append(" LEFT JOIN ");
		sql.append("     nota_fiscal_saida nfs ");
		sql.append("         ON infs.nota_fiscal_id = nfs.id ");
		sql.append(" INNER JOIN ");
		sql.append("     pessoa pes ");
		sql.append("         ON ne.pessoa_destinatario_id_referencia = pes.id ");
		sql.append(" LEFT JOIN ");
		sql.append("     cfop  ");
		sql.append("         ON nfs.cfop_id = cfop.id ");

		sql.append(" WHERE ");

		sql.append(" ne.dataEmissao BETWEEN :dataInicial AND :dataFinal order by nei.nota_envio_id ");

		Query query = this.getSession().createSQLQuery(sql.toString());

		query.setParameter("dataInicial", dataInicial);
		query.setParameter("dataFinal", dataFinal);

		Object uniqueResult = query.uniqueResult();

		return ((BigInteger) uniqueResult).intValue();
	}

	@Override
	public Integer count_obterP3ComRegimeEspecial_NotaFiscalNovo(
			Date dataInicial, Date dataFinal) {

		StringBuilder sql = new StringBuilder();

		sql.append(" SELECT ");

		sql.append(" count(*) ");

		sql.append(" FROM ");
		sql.append("     nota_fiscal_novo nfn ");
		sql.append(" INNER JOIN ");
		sql.append("     nota_fiscal_produto_servico nfps ");
		sql.append("         ON nfps.NOTA_FISCAL_ID = nfn.id ");
		sql.append(" INNER JOIN  ");
		sql.append("     produto_edicao AS pe ");
		sql.append("         ON nfps.produto_edicao_id = pe.id ");
		sql.append(" INNER JOIN ");
		sql.append("     produto AS p ");
		sql.append("         ON pe.produto_id = p.id ");
		sql.append(" INNER JOIN ");
		sql.append("     lancamento AS lc ");
		sql.append("         ON  lc.produto_edicao_id = pe.id ");
		sql.append(" LEFT JOIN ");
		sql.append("     pessoa pes  ");
		sql.append("         ON nfn.pessoa_destinatario_id_referencia = pes.id ");
		sql.append(" LEFT JOIN ");
		sql.append("     natureza_operacao no ");
		sql.append("         ON nfn.NATUREZA_OPERACAO_ID = no.ID ");
		sql.append(" LEFT JOIN ");
		sql.append("     cfop  ");
		sql.append("         ON no.CFOP_ESTADO = cfop.ID ");
		sql.append(" LEFT JOIN ");
		sql.append("     item_nota_fiscal_entrada infe ");
		sql.append("         ON infe.produto_edicao_id = pe.id ");
		sql.append(" LEFT JOIN ");
		sql.append("     nota_fiscal_entrada nfe ");
		sql.append("         ON infe.NOTA_FISCAL_ID = nfe.ID ");
		sql.append(" LEFT JOIN ");
		sql.append("     nota_fiscal_saida nfs ");
		sql.append("         ON nfs.NATUREZA_OPERACAO = no.id ");
		sql.append(" LEFT JOIN ");
		sql.append("      item_nota_fiscal_saida infs ");
		sql.append("         ON infs.produto_edicao_id = pe.id ");

		sql.append(" WHERE ");

		sql.append(" nfn.data_emissao BETWEEN :dataInicial AND :dataFinal ");
		sql.append(" AND nfn.NATUREZA_OPERACAO_ID NOT IN (2,3,9,11,28) order by nfps.nota_fiscal_id ");

		Query query = this.getSession().createSQLQuery(sql.toString());

		query.setParameter("dataInicial", dataInicial);
		query.setParameter("dataFinal", dataFinal);

		Object uniqueResult = query.uniqueResult();

		return ((BigInteger) uniqueResult).intValue();
	}

	public List<P3DTO> obterP3MovimentacaoCompleta(Date dataInicial, Date dataFinal) {


		Distribuidor dist = distribuidoRepo.obter();

		String[] sqls = {"obterP3MovimentacaoCompleta","obterP3MovimentacaoCompleta-saida"};
		
		List<P3DTO> result = new ArrayList<P3DTO>();
		for (String sqlFile : sqls) {
			String templateQuery = getTemplateQuery(sqlFile, null);
			Query query = this.getSession().createSQLQuery(templateQuery);
			
			query.setParameter("dataInicial", dataInicial);
			query.setParameter("dataFinal", dataFinal);
			query.setParameter("codEmpresa", dist.getJuridica().getCnpj()
					.substring(0, dist.getJuridica().getCnpj().indexOf("/"))
					.replace(".", ""));
			query.setParameter("distribCodigo", dist.getCodigo());
			query.setParameter("codFilial", dist.getCodigo().toString());
			
			query.setResultTransformer(new AliasToBeanResultTransformer(P3DTO.class));
			result.addAll(query.list());
			
		}

		return result;
	}

	@Override
	public Integer count_obterP3MovimentacaoCompleta(Date dataInicial,Date dataFinal) {

		String[] sqls = {"obterP3MovimentacaoCompleta","obterP3MovimentacaoCompleta-saida"};

		int count = 0;
		for (String sqlFile : sqls) {
			HashMap p = new HashMap();
			p.put("isCount", Boolean.TRUE);
			String templateQuery = getTemplateQuery(sqlFile,p );
			Query query = this.getSession().createSQLQuery(templateQuery);
			
			query.setParameter("dataInicial", dataInicial);
			query.setParameter("dataFinal", dataFinal);
			
			Object uniqueResult = query.uniqueResult();
			count += ((BigInteger) uniqueResult).intValue();
		}

		return count;
	}
	
	private String getTemplateQuery(String qname, Map datamodel) {

		String templateName = qname+".sql";

		ClassTemplateLoader ctl = new ClassTemplateLoader(getClass(), "");

		Configuration cfg = new Configuration();
		cfg.setTemplateUpdateDelay(0);
		cfg.setTemplateLoader(ctl);

		Template tpl = null;
		StringWriter sw = new StringWriter();

		try {
			tpl = cfg.getTemplate(templateName);
			tpl.process(datamodel, sw);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		}
		// OutputStreamWriter output = new OutputStreamWriter(System.out);
		return sw.toString();

	}
}
