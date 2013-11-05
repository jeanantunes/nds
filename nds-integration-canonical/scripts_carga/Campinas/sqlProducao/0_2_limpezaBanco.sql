SET FOREIGN_KEY_CHECKS = 1;

truncate nota_envio_item;
truncate nota_envio;
truncate fechamento_encalhe_box;
truncate fechamento_encalhe;
truncate controle_fechamento_encalhe;
truncate conferencia_encalhe;
truncate cobranca_controle_conferencia_encalhe_cota;
truncate controle_conferencia_encalhe_cota;
truncate controle_conferencia_encalhe;
truncate conferencia_enc_parcial;
truncate conferencia_encalhe;
truncate chamada_encalhe_cota;
truncate chamada_encalhe_lancamento;
truncate chamada_encalhe;
truncate negociacao_cobranca_originaria;

update movimento_financeiro_cota set baixa_cobranca_id = null;

truncate baixa_cobranca;
truncate cobranca;
update divida set DIVIDA_RAIZ_ID = null;

truncate historico_acumulo_divida;

truncate divida;
truncate consolidado_mvto_financeiro_cota;
truncate consolidado_financeiro_cota;
truncate historico_movto_financeiro_cota;
truncate venda_produto_movimento_financeiro;
truncate venda_produto_movimento_estoque;
truncate venda_produto;
truncate nota_fiscal_produto_servico_movimento_estoque_cota;
truncate lancamento_diferenca_movimento_estoque_cota;
truncate parcela_negociacao;
truncate cota_ausente_movimento_estoque_cota;
truncate movimento_estoque_cota;
truncate movimento_financeiro_cota;
truncate rateio_cota_ausente;
truncate cota_ausente;
truncate estoque_produto_cota;

truncate lancamento_item_receb_fisico;
truncate rateio_diferenca;
update movimento_estoque set item_rec_fisico_id = null;
update item_receb_fisico set diferenca_id = null;
truncate diferenca;
truncate item_receb_fisico;
truncate lancamento_diferenca;
truncate movimento_estoque;
truncate estoque_produto;

update nota_fiscal_entrada  set nota_fiscal_entrada.STATUS_NOTA_FISCAL = 'NAO_RECEBIDA'  
where nota_fiscal_entrada.ID IN (select recebimento_fisico.NOTA_FISCAL_ID from recebimento_fisico);

update lancamento  set  lancamento.`STATUS` = 'BALANCEADO'
where lancamento.PRODUTO_EDICAO_ID IN (
	select item_nota_fiscal_entrada.PRODUTO_EDICAO_ID from nota_fiscal_entrada 
	join recebimento_fisico on recebimento_fisico.NOTA_FISCAL_ID = nota_fiscal_entrada.ID 
	join item_nota_fiscal_entrada on item_nota_fiscal_entrada.NOTA_FISCAL_ID = nota_fiscal_entrada.ID
);

update diferenca set diferenca.itemRecebimentoFisico_ID = null, diferenca.LANCAMENTO_DIFERENCA_ID = null;
truncate lancamento_item_receb_fisico;
truncate lancamento_diferenca;
truncate movimento_estoque;
truncate item_receb_fisico;
truncate rateio_diferenca;
truncate diferenca;
truncate recebimento_fisico;
truncate movimento_estoque;

update lancamento set lancamento.EXPEDICAO_ID = null;
truncate expedicao;

truncate estoque_produto;
