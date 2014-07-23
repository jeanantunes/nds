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

DELETE FROM acumulo_divida;

delete from negociacao_mov_finan;

DELETE FROM movimento_financeiro_cota;

DELETE FROM baixa_cobranca;

delete from boleto_email;

DELETE FROM cobranca;
DELETE FROM divida;

DELETE FROM consolidado_financeiro_cota;

DELETE FROM estoque_produto_cota;

UPDATE lancamento SET lancamento.EXPEDICAO_ID = null;
DELETE FROM expedicao;

UPDATE lancamento SET status = 'CONFIRMADO';

UPDATE lancamento SET status = 'FECHADO' WHERE data_rec_prevista < sysdate();

delete from desconto;

delete from forma_cobranca_fornecedor;

delete from concentracao_cobranca_cota;

delete from politica_cobranca;

delete from formacobranca_diasdomes;

delete from negociacao;

delete from forma_cobranca;

update fornecedor set banco_id = null where banco_id is not null;

delete from banco;

DELETE f.* FROM fornecedor f INNER JOIN pessoa p ON p.id = f.juridica_id WHERE p.nome_fantasia <> 'Dinap' AND p.nome_fantasia <> 'FC' AND p.nome_fantasia <> 'Treelog';

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
