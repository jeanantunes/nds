	
		 SELECT 
		 <#if isCount == true>
		 	count(*)
		 <#else>
		 cast(:codEmpresa as char) codEmpresa, 
		 cast(:codFilial as char) codFilial, 
		 '180' AS naturezaEstoque, 
		 DATE_FORMAT(lc.data_lcto_distribuidor, '%d/%m/%Y') dataLancamento, 
		 p.codigo AS codMaterial, 
		 'ESS' AS tipoOperacao,  
		 replace(cast(round(infe.qtde,3) as char),'.',',') quantidade, 
		 'E' AS indLancamento,  
		 concat(DATE_FORMAT(CURRENT_DATE, '%Y%m'), :distribCodigo, 
				nfe.numero, p.CODIGO, pe.NUMERO_EDICAO)
				 AS numArquivamento,  
		 	CASE 
		  		WHEN nfe.TIPO_NF_ID =  2 or nfe.TIPO_NF_ID =  9 or nfe.TIPO_NF_ID =  10	THEN 'NFD'
		 		ELSE ''  
		 END AS tipoDocumento, 
		 cast(nfe.numero as char) numDocumento, 
		 nfe.serie AS serDocumento, 
		 '' AS numSequencialItem, 
		 '' AS numSerieMaterial, 
		 cast(CASE WHEN tnf.emitente='COTA' OR  tnf.destinatario ='COTA' 
		 	THEN 'CL' 
		    ELSE 'FO' 
		 END as char) categoriaPfPj, 
		 CASE 
		 WHEN pes.cnpj is not null THEN pes.cnpj 
		 WHEN pes.cpf is not null THEN pes.cpf 
		 ELSE '' 
		 END codigoPfPj,  
		 cast(1 as char) localizacao, 
		 replace(cast(round(infe.preco, 4) as char),'.',',') vlrUnitario, 
		 replace(cast(round(nfe.valor_bruto, 2) as char),'.',',') vlrTotal, 
		 replace(cast(round(infe.preco, 4) as char),'.',',') custoUnitario, 
		 replace(cast(round(nfe.valor_bruto, 2) as char),'.',',') custoTotal, 
		 '' AS contaEstoque, 
		 '' AS contraPartida, 
		 '' AS contratoServico, 
		 '@' AS centroCusto, 
		 cfop.codigo AS cfop, 
		 	CASE 
		  		WHEN infe.aliquota_ipi_produto is not null 
		 		THEN replace(cast(round(infe.aliquota_ipi_produto, 2) as char),'.',',') 
		 		ELSE ''  
		 END vlrIpi, 
		 '' AS pagLivroFiscal, 
		 '' AS numLote, 
		 '' AS docEstornado, 
		 '' AS itemEstornado, 
		 '' AS divisao, 
		 cast(YEAR(nfe.data_emissao) as char) anoDocumento, 
		 '' AS observacao, 
		 'NDS' AS openflex01, 
		 now() AS openflex02, 
		 '' AS openflex03, 
		 'NDS' AS openflex04, 
		 '' AS openflex05, 
		 '' AS openflex06, 
		 '' AS openflex07, 
		 '' AS openflex08 
		 </#if> 
		 FROM 
		     nota_fiscal_entrada AS nfe 
		 INNER JOIN 
		     item_nota_fiscal_entrada AS infe 
		         ON infe.nota_fiscal_id = nfe.id 
		 INNER JOIN  
		     produto_edicao AS pe 
		         ON pe.id = infe.produto_edicao_id 
		 INNER JOIN 
		     produto AS p 
		         ON pe.produto_id = p.id 
		 INNER JOIN 
		     lancamento AS lc 
		         ON pe.id = lc.produto_edicao_id 
		 INNER JOIN 
		     cfop 
		         ON cfop.id = nfe.cfop_id 
		 LEFT JOIN 
		     pessoa pes 
		         ON pes.id = nfe.pj_id 
		 INNER JOIN 
		     tipo_nota_fiscal tnf  
		         ON nfe.tipo_nf_id = tnf.id 
		 WHERE 
		 nfe.data_emissao BETWEEN :dataInicial AND :dataFinal 
		 AND TIPO_NF_ID IN (2,9,10) order by nfe.data_recebimento 