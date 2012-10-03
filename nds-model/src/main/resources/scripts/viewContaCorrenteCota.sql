DROP VIEW IF EXISTS viewContaCorrenteCota;
CREATE OR
REPLACE VIEW VIEW_CONTA_CORRENTE_COTA AS (

SELECT 
		 consolidado_financeiro_cota.ID AS ID, 
		 consolidado_financeiro_cota.cota_ID AS COTA_ID, 
		 cota.NUMERO_COTA AS NUMERO_COTA, 
		 consolidado_financeiro_cota.CONSIGNADO AS CONSIGNADO, 
		 consolidado_financeiro_cota.DT_CONSOLIDADO AS DT_CONSOLIDADO, 
		 consolidado_financeiro_cota.DEBITO_CREDITO AS DEBITO_CREDITO, 
		 consolidado_financeiro_cota.ENCALHE AS ENCALHE, 
		 consolidado_financeiro_cota.ENCARGOS AS ENCARGOS, 
		 consolidado_financeiro_cota.NUMERO_ATRASADOS AS NUMERO_ATRASADOS, 
		 consolidado_financeiro_cota.PENDENTE AS PENDENTE, 
		 consolidado_financeiro_cota.TOTAL AS TOTAL, 
		 consolidado_financeiro_cota.VALOR_POSTERGADO AS VALOR_POSTERGADO, 
		 consolidado_financeiro_cota.VENDA_ENCALHE AS VENDA_ENCALHE, 
		 'CONSOLIDADO' AS TIPO, 
		 (
			SELECT CASE WHEN divida.DATA IS NOT NULL THEN divida.DATA ELSE acumulada.data END AS data_raiz
			FROM divida acumulada
			LEFT JOIN DIVIDA divida ON divida.id = acumulada.divida_raiz_id
			WHERE acumulada.consolidado_id = consolidado_financeiro_cota.id
		)AS DT_RAIZ_CONSOLIDADO,
		 
		 NULL as DT_RAIZ_PENDENTE,
		
		coalesce((select sum(baixa_cobranca.VALOR_PAGO)
			from baixa_cobranca INNER JOIN cobranca ON cobranca.ID = baixa_cobranca.COBRANCA_ID   
			INNER JOIN divida ON divida.ID = cobranca.DIVIDA_ID
			where baixa_cobranca.STATUS NOT IN ('NAO_PAGO_DIVERGENCIA_VALOR',
														'NAO_PAGO_DIVERGENCIA_DATA',
														'NAO_PAGO_BAIXA_JA_REALIZADA',
														'NAO_PAGO_POSTERGADO')
			and cota.ID = cobranca.COTA_ID
			and divida.CONSOLIDADO_ID = consolidado_financeiro_cota.ID
			and consolidado_financeiro_cota.ID
		),0) AS VALOR_PAGO,
		
	   TOTAL - coalesce((select sum(baixa_cobranca.VALOR_PAGO)
			from baixa_cobranca INNER JOIN cobranca ON cobranca.ID = baixa_cobranca.COBRANCA_ID   
			INNER JOIN divida ON divida.ID = cobranca.DIVIDA_ID
			where baixa_cobranca.STATUS NOT IN ('NAO_PAGO_DIVERGENCIA_VALOR',
														'NAO_PAGO_DIVERGENCIA_DATA',
														'NAO_PAGO_BAIXA_JA_REALIZADA',
														'NAO_PAGO_POSTERGADO')
			and cota.ID = cobranca.COTA_ID
			and divida.CONSOLIDADO_ID = consolidado_financeiro_cota.ID
			and consolidado_financeiro_cota.ID
		),0) AS SALDO
		
FROM consolidado_financeiro_cota
INNER JOIN cota ON cota.id=consolidado_financeiro_cota.cota_ID

);