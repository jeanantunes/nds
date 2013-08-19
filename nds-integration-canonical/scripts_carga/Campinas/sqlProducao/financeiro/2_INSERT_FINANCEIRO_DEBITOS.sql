alter table movimento_financeiro_cota modify id bigint(20) AUTO_INCREMENT;

INSERT INTO movimento_financeiro_cota
(
APROVADO_AUTOMATICAMENTE,
DATA_APROVACAO,
MOTIVO,
STATUS,
DATA,
DATA_CRIACAO,
APROVADOR_ID,
TIPO_MOVIMENTO_ID,
USUARIO_ID,
LANCAMENTO_MANUAL,
COTA_ID,
OBSERVACAO, 
valor)

(select 
true,
data_real,
'CARGA',
'APROVADO',
data_real,
data_real,
null,
23,
1,
true,
cota_id,
concat('CARGA INICIAL - ', desc_debito),
valor_decimal

from mov_deb);
