UPDATE nota_fiscal_entrada 
SET nota_fiscal_entrada.STATUS_NOTA_FISCAL = 'NAO_RECEBIDA' and DATA_RECEBIMENTO=null
WHERE nota_fiscal_entrada.ID IN (SELECT recebimento_fisico.NOTA_FISCAL_ID FROM recebimento_fisico);

UPDATE lancamento  SET  lancamento.`STATUS` = 'BALANCEADO'
WHERE lancamento.PRODUTO_EDICAO_ID IN (
	SELECT item_nota_fiscal_entrada.PRODUTO_EDICAO_ID FROM nota_fiscal_entrada 
	JOIN recebimento_fisico ON recebimento_fisico.NOTA_FISCAL_ID = nota_fiscal_entrada.ID 
	JOIN item_nota_fiscal_entrada ON item_nota_fiscal_entrada.NOTA_FISCAL_ID = nota_fiscal_entrada.ID
);

UPDATE diferenca SET diferenca.itemRecebimentoFisico_ID = NULL, diferenca.LANCAMENTO_DIFERENCA_ID = NULL;
							
DELETE FROM lancamento_item_receb_fisico;
DELETE FROM lancamento_diferenca;
DELETE FROM movimento_estoque;
DELETE FROM item_receb_fisico;
DELETE FROM rateio_diferenca;
DELETE FROM diferenca;
DELETE FROM recebimento_fisico;
DELETE FROM movimento_estoque;

UPDATE lancamento SET lancamento.EXPEDICAO_ID = null;
DELETE FROM expedicao;

UPDATE lancamento SET status = 'CONFIRMADO';

UPDATE lancamento SET status = 'FECHADO' WHERE data_rec_prevista < sysdate();

UPDATE cota SET id_fiador = NULL;
DELETE FROM CAUCAO_LIQUIDA;
DELETE FROM IMOVEL;
DELETE FROM cota_garantia;
DELETE FROM endereco_fiador;
DELETE FROM telefone_fiador;
DELETE FROM garantia;
DELETE FROM fiador_socio;
DELETE FROM fiador;

DELETE FROM cota_fornecedor WHERE fornecedor_id IN (
    SELECT f.id 
    FROM fornecedor f 
    INNER JOIN pessoa p ON p.id = f.juridica_id 
    WHERE p.nome_fantasia <> 'Dinap' 
    AND p.nome_fantasia <> 'FC' 
    AND p.nome_fantasia <> 'Treelog'
);
DELETE FROM forma_cobranca_fornecedor WHERE fornecedor_id IN (
    SELECT f.id 
    FROM fornecedor f 
    INNER JOIN pessoa p ON p.id = f.juridica_id 
    WHERE p.nome_fantasia <> 'Dinap' 
    AND p.nome_fantasia <> 'FC' 
    AND p.nome_fantasia <> 'Treelog'
);
DELETE FROM endereco_fornecedor WHERE fornecedor_id IN (
    SELECT f.id 
    FROM fornecedor f 
    INNER JOIN pessoa p ON p.id = f.juridica_id 
    WHERE p.nome_fantasia <> 'Dinap' 
    AND p.nome_fantasia <> 'FC' 
    AND p.nome_fantasia <> 'Treelog'
);
DELETE FROM telefone_fornecedor WHERE fornecedor_id IN (
    SELECT f.id 
    FROM fornecedor f 
    INNER JOIN pessoa p ON p.id = f.juridica_id 
    WHERE p.nome_fantasia <> 'Dinap' 
    AND p.nome_fantasia <> 'FC' 
    AND p.nome_fantasia <> 'Treelog'
);
DELETE FROM produto_fornecedor WHERE fornecedores_id IN (
    SELECT f.id 
    FROM fornecedor f 
    INNER JOIN pessoa p ON p.id = f.juridica_id 
    WHERE p.nome_fantasia <> 'Dinap' 
    AND p.nome_fantasia <> 'FC' 
    AND p.nome_fantasia <> 'Treelog'
);

update fornecedor set desconto_id = null;
delete from desconto;

delete from controle_baixa_bancaria;

select * from forma_cobranca_fo
;

delete from forma_cobranca
;
delete from banco
;

/*
select * from desconto_cota;
select * from desconto_produto_cota;
select * from desconto_cota_fornecedor;
select * from distribuidor;
select * from desconto_distribuidor;
select * from desconto_distribuidor_fornecedor;
select * from fornecedor;
select * from pessoa;

select * from COTA_FORNECEDOR;

select * from cota where numero_cota = 2;
*/

DELETE FROM desconto_cota_produto_excessoes ;

DELETE FROM historico_desconto_cota_produto_excessoes;

DELETE FROM HISTORICO_DESCONTO_PRODUTOS;

delete FROM HISTORICO_DESCONTO_PRODUTO_EDICOES;

delete from desconto_lancamento_cota;
delete from DESCONTO_PROXIMOS_LANCAMENTOS;

delete from COTA_FORNECEDOR;

DELETE FROM forma_cobranca WHERE parametro_cobranca_cota_id IN (
    SELECT pcc.id 
    FROM parametro_cobranca_cota pcc WHERE fornecedor_id IN (
        SELECT f.id 
        FROM fornecedor f 
        INNER JOIN pessoa p ON p.id = f.juridica_id 
        WHERE p.nome_fantasia <> 'Dinap' 
        AND p.nome_fantasia <> 'FC' 
        AND p.nome_fantasia <> 'Treelog'
    )
);
DELETE FROM parametro_cobranca_cota WHERE fornecedor_id IN (
    SELECT f.id 
    FROM fornecedor f 
    INNER JOIN pessoa p ON p.id = f.juridica_id 
    WHERE p.nome_fantasia <> 'Dinap' 
    AND p.nome_fantasia <> 'FC' 
    AND p.nome_fantasia <> 'Treelog'
);
DELETE FROM item_nota_fiscal_entrada WHERE nota_fiscal_id IN (
    SELECT id FROM nota_fiscal_entrada WHERE fornecedor_id IN (
        SELECT f.id 
        FROM fornecedor f 
        INNER JOIN pessoa p ON p.id = f.juridica_id 
        WHERE p.nome_fantasia <> 'Dinap' 
        AND p.nome_fantasia <> 'FC' 
        AND p.nome_fantasia <> 'Treelog'
    )
);

delete from item_nota_fiscal_entrada where NOTA_FISCAL_ID in (
	select id from nota_fiscal_entrada where origem = 'MANUAL'
)
;

delete FROM nota_fiscal_entrada where origem = 'MANUAL'
;

DELETE FROM distribuicao_fornecedor;
DELETE FROM historico_descontos_fornecedores;
DELETE f.* FROM fornecedor f INNER JOIN pessoa p ON p.id = f.juridica_id WHERE p.nome_fantasia <> 'Dinap' AND p.nome_fantasia <> 'FC' AND p.nome_fantasia <> 'Treelog';

DELETE FROM endereco_transportador;
DELETE FROM telefone_transportador;
DELETE FROM motorista;
DELETE FROM veiculo;
DELETE FROM transportador;

DELETE FROM telefone_entregador;
DELETE from endereco_entregador;
DELETE FROM entregador;
DELETE FROM rota_pdv;
DELETE FROM rota;
DELETE FROM roteiro;
DELETE FROM roteirizacao;

UPDATE usuario SET box_id = null;
UPDATE cota SET box_id = null;
DELETE FROM historico_situacao_cota;
INSERT historico_situacao_cota (DATA_EDICAO, TIPO_EDICAO, DATA_INICIO_VALIDADE, DESCRICAO, MOTIVO, NOVA_SITUACAO, SITUACAO_ANTERIOR, USUARIO_ID, COTA_ID) 
(
	SELECT NOW(), "INCLUSAO", NOW(), "INTERFACE", "OUTROS", SITUACAO_CADASTRO, SITUACAO_CADASTRO, 1, ID FROM cota 
);
DELETE FROM box;
