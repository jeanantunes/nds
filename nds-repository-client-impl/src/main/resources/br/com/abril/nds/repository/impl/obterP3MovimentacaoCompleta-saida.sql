		 SELECT 
		 <#if isCount??>
		 	count(*)
		 <#else>
		 cast(:codEmpresa as char) codEmpresa, 
		 cast(:codFilial as char) codFilial, 
		 '180' AS naturezaEstoque, 
		 DATE_FORMAT(lc.data_lcto_distribuidor, '%d/%m/%Y') dataLancamento, 
		 p.codigo AS codMaterial, 
		 'ESS' AS tipoOperacao, 
		 replace(cast(round(infs.qtde,3) AS char),'.',',') quantidade, 
		 'S' AS indLancamento, 
		 concat(DATE_FORMAT(CURRENT_DATE, '%Y%m'), :distribCodigo, 
			nfs.numero, p.CODIGO, pe.NUMERO_EDICAO)
			 AS numArquivamento, 
		 	CASE 
		  		WHEN nfs.TIPO_NF_ID =  3 or nfs.TIPO_NF_ID = 4 or nfs.TIPO_NF_ID =  11 or nfs.TIPO_NF_ID =  28 or nfs.TIPO_NF_ID =  29	
		  		THEN 'NFD' 
		 		ELSE ''  
		 END AS tipoDocumento, 
		 cast(nfs.numero as char) numDocumento, 
		 nfs.serie AS serDocumento, 
		 '' AS numSequencialItem, 
		 '' AS numSerieMaterial, 

		 cast(CASE WHEN natOp.TIPO_EMITENTE ='COTA' OR natOp.TIPO_DESTINATARIO ='COTA'
		 	THEN 'CL' 
		    ELSE 'FO' 
		 END as char) categoriaPfPj, 

		 CASE 
		 WHEN pes.cnpj is not null THEN pes.cnpj 
		 WHEN pes.cpf is not null THEN pes.cpf 
		 ELSE '' 
		 END codigoPfPj, 

		 cast(1 as char) localizacao, 

		 replace(cast(round(pe.preco_venda, 4) as char),'.',',') vlrUnitario, 
		 replace(cast(round((pe.preco_venda)*infs.qtde, 2) AS char),'.',',') vlrTotal, 
		 replace(cast(round(pe.preco_venda, 4) as char),'.',',') custoUnitario, 
		 replace(cast(round((pe.preco_venda)*infs.qtde, 2) AS char),'.',',') custoTotal, 

		 '' AS contaEstoque, 
		 '' AS contraPartida, 
		 '' AS contratoServico, 
		 '@' AS centroCusto, 
		 cfop.codigo AS cfop, 

		 	CASE 
		  		WHEN infs.aliquota_ipi_produto is not null 
		 		THEN replace(cast(round(infs.aliquota_ipi_produto, 2) as char),'.',',') 
		 		ELSE ''  
		 END vlrIpi, 

		 '' AS pagLivroFiscal, 
		 '' AS numLote, 
		 '' AS docEstornado, 
		 '' AS itemEstornado, 
		 '' AS divisao, 
		 cast(YEAR(nfs.data_emissao) as char) anoDocumento, 
		 '' AS observacao, 
		 'NDS' AS openflex01, 
		 cast(NOW() as char) AS openflex02, 
		 '' AS openflex03, 
		 'NDS' AS openflex04, 
		 '' AS openflex05, 
		 '' AS openflex06, 
		 '' AS openflex07, 
		 '' AS openflex08 

		  </#if> 
		  
		 FROM 
		     nota_fiscal_saida AS nfs 
		 INNER JOIN 
		     item_nota_fiscal_saida AS infs 
		         ON infs.nota_fiscal_id = nfs.id 
		 INNER JOIN  
		     produto_edicao AS pe 
		         ON infs.produto_edicao_id = pe.id  
		 INNER JOIN 
		     produto AS p 
		         ON pe.produto_id = p.id 
		 INNER JOIN 
		     lancamento AS lc 
		         ON pe.id = lc.produto_edicao_id 
		 INNER JOIN 
		     cfop   
		         ON nfs.cfop_id = cfop.id 
		 LEFT JOIN 
		     pessoa pes 
		         ON pes.id = nfs.pj_id 
		 INNER JOIN 
			 natureza_operacao natOp  
				 ON nfs.NATUREZA_OPERACAO = natOp.ID

		 WHERE 

		 nfs.data_emissao BETWEEN :dataInicial AND :dataFinal 

		 AND NATUREZA_OPERACAO NOT IN (3,11,28) order by infs.NOTA_FISCAL_ID 