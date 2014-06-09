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
VALOR)

(select 
true,
data_real,
'CARGA',
'APROVADO',
data_real,
data_real,
null,
22,
1,
true,
cota_id,
concat('CARGA INICIAL - ', desc_credito),
valor_decimal

from mov_cred);


select * from tipo_movimento where id = 21;