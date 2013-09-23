DELETE FROM lancamento_diferenca_movimento_estoque_cota;
DELETE FROM nota_fiscal_produto_servico_movimento_estoque_cota;
DELETE FROM fechamento_encalhe_box;
DELETE FROM fechamento_encalhe;
DELETE FROM controle_fechamento_encalhe;
DELETE FROM conferencia_encalhe;
DELETE FROM cobranca_controle_conferencia_encalhe_cota;
DELETE FROM controle_conferencia_encalhe_cota;
DELETE FROM controle_conferencia_encalhe;
DELETE FROM conferencia_enc_parcial;
DELETE FROM conferencia_encalhe;
DELETE FROM chamada_encalhe_cota;
DELETE FROM chamada_encalhe_lancamento;
DELETE FROM chamada_encalhe;
DELETE FROM negociacao_cobranca_originaria;
DELETE FROM consolidado_mvto_financeiro_cota;
DELETE FROM historico_movto_financeiro_cota;
DELETE FROM venda_produto_movimento_financeiro;
DELETE FROM venda_produto_movimento_estoque;
DELETE FROM venda_produto;
DELETE FROM parcela_negociacao;
DELETE FROM lancamento_item_receb_fisico;
DELETE FROM rateio_diferenca;
UPDATE movimento_estoque SET item_rec_fisico_id = null;
UPDATE item_receb_fisico SET diferenca_id = null;

DELETE FROM atualizacao_estoque_gfs;
DELETE FROM diferenca;
DELETE FROM item_receb_fisico;
DELETE FROM lancamento_diferenca;
DELETE FROM movimento_estoque;
DELETE FROM estoque_produto;

DELETE FROM cota_ausente_movimento_estoque_cota WHERE movimento_estoque_cota_id IN (SELECT id FROM movimento_estoque_cota);
DELETE FROM movimento_estoque_cota;
DELETE FROM movimento_financeiro_cota;

DELETE FROM nota_envio_item;
DELETE FROM nota_envio;

DELETE FROM baixa_cobranca;
DELETE FROM cobranca;

CREATE TABLE ids (
    id INTEGER
);
INSERT INTO ids
SELECT id FROM divida WHERE divida_raiz_id IS NULL;

DELETE FROM historico_acumulo_divida WHERE divida_id IN (SELECT id FROM divida);
DELETE FROM divida WHERE divida_raiz_id IN (SELECT id FROM ids);
DELETE FROM divida;

DELETE FROM consolidado_financeiro_cota;
DELETE FROM estoque_produto_cota;

DROP TABLE ids;
