DROP VIEW IF EXISTS viewContaCorrenteCota;
CREATE OR
REPLACE VIEW VIEW_CONTA_CORRENTE_COTA AS (
SELECT consolidado_financeiro_cota.ID AS ID, consolidado_financeiro_cota.cota_ID AS COTA_ID, cota.NUMERO_COTA AS NUMERO_COTA, consolidado_financeiro_cota.CONSIGNADO AS CONSIGNADO, consolidado_financeiro_cota.DT_CONSOLIDADO AS DT_CONSOLIDADO, consolidado_financeiro_cota.DEBITO_CREDITO AS DEBITO_CREDITO, consolidado_financeiro_cota.ENCALHE AS ENCALHE, consolidado_financeiro_cota.ENCARGOS AS ENCARGOS, consolidado_financeiro_cota.NUMERO_ATRASADOS AS NUMERO_ATRASADOS, consolidado_financeiro_cota.PENDENTE AS PENDENTE, consolidado_financeiro_cota.TOTAL AS TOTAL, consolidado_financeiro_cota.VALOR_POSTERGADO AS VALOR_POSTERGADO, consolidado_financeiro_cota.VENDA_ENCALHE AS VENDA_ENCALHE, 'CONSOLIDADO' AS TIPO, (
SELECT CASE WHEN divida.DATA IS NOT NULL THEN divida.DATA ELSE acumulada.data END AS data_raiz
FROM divida acumulada
LEFT JOIN DIVIDA divida ON divida.id = acumulada.divida_raiz_id
WHERE acumulada.consolidado_id = consolidado_financeiro_cota.id) AS DT_RAIZ_CONSOLIDADO,
NULL as DT_RAIZ_PENDENTE
FROM consolidado_financeiro_cota
INNER JOIN cota ON cota.id=consolidado_financeiro_cota.cota_ID) UNION (
SELECT 0 AS ID, cota.id AS COTA_ID, cota.NUMERO_COTA AS NUMERO_COTA, NULL AS CONSIGNADO, cobranca.DT_EMISSAO AS DT_CONSOLIDADO, NULL AS DEBITO_CREDITO, NULL AS ENCALHE, NULL AS ENCARGOS, NULL AS NUMERO_ATRASADOS, cobranca.VALOR AS PENDENTE, NULL AS TOTAL, NULL VALOR_POSTERGADO, NULL AS VENDA_ENCALHE, 'COBRANCA' AS TIPO, NULL AS DT_RAIZ_CONSOLIDADO,
(SELECT CASE WHEN divida.DATA IS NOT NULL THEN divida.DATA ELSE acumulada.data END AS data_raiz
FROM divida acumulada
LEFT JOIN DIVIDA divida ON divida.id = acumulada.divida_raiz_id
WHERE acumulada.id = cobranca.divida_id) AS DT_RAIZ_PENDENTE
FROM cobranca
INNER JOIN cota ON cota.id=cobranca.cota_ID
WHERE cobranca.status_cobranca = 'NAO_PAGO' AND cobranca.DT_VENCIMENTO > (
SELECT MAX(consolidado_financeiro_cota.DT_CONSOLIDADO)
FROM consolidado_financeiro_cota))