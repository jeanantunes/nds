--
-- VARIAVEIS DO SISTEMA
--

SET max_heap_table_size = 18446744073709551615;
SET tmp_table_size = 18446744073709551615;

--
-- VARIAVEIS INICIALIZACAO DO NDS
--
update distribuidor set data_operacao = date(sysdate());

select 'Data de Operação: ',data_operacao from distribuidor; -- Log 

insert into seq_generator (sequence_next_hi_value,sequence_name) values (1,'Movimento');

-- ################################################################################################
-- ######################################   TABELAS DE CARGA    ###################################
-- ################################################################################################

-- ######
-- PRODIN    
-- ######

-- CARGA_PRODIN_DSF
DROP TABLE IF EXISTS CARGA_PRODIN_DSF;

create table CARGA_PRODIN_DSF (
codDistribuidor VARCHAR(7), 
dataGeracaoArquivo VARCHAR(8), 
tipoDesconto VARCHAR(2),
percentualDesconto VARCHAR(7),
percentualPrestacao_servico VARCHAR(7),
dataInicioDesconto VARCHAR(8),
fornecedorId VARCHAR(1),
KEY tipoDesconto (tipoDesconto)) 
ENGINE=MEMORY
;

LOAD DATA INFILE '/opt/rollout/load_files/00000001.dsf' INTO TABLE CARGA_PRODIN_DSF
(@linha) SET 
codDistribuidor=SUBSTR(@linha,1,7),  
dataGeracaoArquivo=SUBSTR(@linha,8,8),
tipoDesconto=SUBSTR(@linha,35,2),
percentualDesconto=SUBSTR(@linha,37,7),
percentualPrestacao_servico=SUBSTR(@linha,44,7),
dataInicioDesconto=SUBSTR(@linha,51,8),
fornecedorId = 1;

LOAD DATA INFILE '/opt/rollout/load_files/00000002.dsf' INTO TABLE CARGA_PRODIN_DSF
(@linha) SET 
codDistribuidor=SUBSTR(@linha,1,7),  
dataGeracaoArquivo=SUBSTR(@linha,8,8),
tipoDesconto=SUBSTR(@linha,35,2),
percentualDesconto=SUBSTR(@linha,37,7),
percentualPrestacao_servico=SUBSTR(@linha,44,7),
dataInicioDesconto=SUBSTR(@linha,51,8),
fornecedorId = 2; -- FIXME trocar para 2

select 'CARGA_PRODIN_DSF:',count(*) from CARGA_PRODIN_DSF; -- Log 




--  CARGA_PRODIN_EDI
DROP TABLE IF EXISTS CARGA_PRODIN_EDI;

create table CARGA_PRODIN_EDI (
codDistribuidor VARCHAR(7), 		
tipoOperacao VARCHAR(1), 		
codigoEditor VARCHAR(7), 			
nomeEditor VARCHAR(35), 			
tipoLogradouroEditor VARCHAR(5),		
logradouroEditor VARCHAR(30), 			
numeroEditor VARCHAR(5), 		
complementoEditor VARCHAR(10), 			
cidadeEditor VARCHAR(30), 		
ufEditor VARCHAR(2), 				
cepEditor VARCHAR(8), 				
tipoLogradouroEntrega VARCHAR(5), 		
logradouroEntrega VARCHAR(20), 			
numeroEntrega VARCHAR(5), 		
complementoEntrega VARCHAR(20), 		
cidadeEntrega VARCHAR(30), 			
ufEntrega VARCHAR(2), 				
cepEntrega VARCHAR(8), 				
dddContato VARCHAR(4), 			
telefoneContato VARCHAR(8), 			
dddEditor VARCHAR(4), 			
telefoneEditor VARCHAR(8),		
dddFax VARCHAR(4), 			
telefoneFax VARCHAR(8), 			
inscricaoMunicipal VARCHAR(12), 		
tipoPessoa VARCHAR(1), 				
nomeContato VARCHAR(30), 			
status VARCHAR(1), 			    
cpf VARCHAR(12), 				
rg VARCHAR(15), 				
orgaoEmissor VARCHAR(10), 			
ufOrgaoEmissor VARCHAR(2), 			
cnpj VARCHAR(14), 				
inscricaoEstadual VARCHAR(20), 			
bairroEditor VARCHAR(5), 		
bairroEntrega VARCHAR(5),
KEY codigoEditor (codigoEditor)) 
ENGINE=MEMORY
;

LOAD DATA INFILE '/opt/rollout/load_files/00000001.edi' INTO TABLE CARGA_PRODIN_EDI CHARACTER SET UTF8
(@linha) SET 
codDistribuidor=SUBSTR(@linha,1,7),
tipoOperacao=SUBSTR(@linha,26,1),
codigoEditor=SUBSTR(@linha,28,7),	
nomeEditor=TRIM(SUBSTR(@linha,35,35)),	
tipoLogradouroEditor=SUBSTR(@linha,73,5),	
logradouroEditor=TRIM(SUBSTR(@linha,78,30)),
numeroEditor=SUBSTR(@linha,108,5),	
complementoEditor=SUBSTR(@linha,113,10),	
cidadeEditor=TRIM(SUBSTR(@linha,133, 30)),
ufEditor=SUBSTR(@linha,163,2),
cepEditor=SUBSTR(@linha,165,8),
tipoLogradouroEntrega=SUBSTR(@linha,173,5),	
logradouroEntrega=SUBSTR(@linha,178,20),	
numeroEntrega=SUBSTR(@linha,208,5),
complementoEntrega=SUBSTR(@linha,213,20),	
cidadeEntrega=TRIM(SUBSTR(@linha,233,30)),
ufEntrega=SUBSTR(@linha,263,2),
cepEntrega=SUBSTR(@linha,265,8),	
dddContato=SUBSTR(@linha,273,4),	
telefoneContato=SUBSTR(@linha,277,8),
dddEditor=SUBSTR(@linha,285,4),
telefoneEditor=SUBSTR(@linha,289,8),	
dddFax=SUBSTR(@linha,297,4),
telefoneFax=SUBSTR(@linha,301,8),
inscricaoMunicipal=TRIM(SUBSTR(@linha,315,12)),
tipoPessoa=SUBSTR(@linha,327,1),
nomeContato=TRIM(SUBSTR(@linha,328,30)),
status=SUBSTR(@linha,361,1),
cpf=SUBSTR(@linha,367,12),
rg=SUBSTR(@linha,379,15),
orgaoEmissor=SUBSTR(@linha,394,10),
ufOrgaoEmissor=SUBSTR(@linha,404,2),
cnpj=SUBSTR(@linha,406,14),
inscricaoEstadual=TRIM(SUBSTR(@linha,420,20)),
bairroEditor=SUBSTR(@linha,440,5),
bairroEntrega=SUBSTR(@linha,445,5);

LOAD DATA INFILE '/opt/rollout/load_files/00000002.edi' INTO TABLE CARGA_PRODIN_EDI CHARACTER SET UTF8
(@linha) SET 
codDistribuidor=SUBSTR(@linha,1,7),
tipoOperacao=SUBSTR(@linha,26,1),
codigoEditor=SUBSTR(@linha,28,7),	
nomeEditor=TRIM(SUBSTR(@linha,35,35)),	
tipoLogradouroEditor=SUBSTR(@linha,73,5),	
logradouroEditor=TRIM(SUBSTR(@linha,78,30)),
numeroEditor=SUBSTR(@linha,108,5),	
complementoEditor=SUBSTR(@linha,113,10),	
cidadeEditor=SUBSTR(@linha,133, 30),
ufEditor=SUBSTR(@linha,163,2),
cepEditor=SUBSTR(@linha,165,8),
tipoLogradouroEntrega=SUBSTR(@linha,173,5),	
logradouroEntrega=SUBSTR(@linha,178,20),	
numeroEntrega=SUBSTR(@linha,208,5),
complementoEntrega=SUBSTR(@linha,213,20),	
cidadeEntrega=SUBSTR(@linha,233,30),
ufEntrega=SUBSTR(@linha,263,2),
cepEntrega=TRIM(SUBSTR(@linha,265,8)),	
dddContato=SUBSTR(@linha,273,4),	
telefoneContato=SUBSTR(@linha,277,8),
dddEditor=SUBSTR(@linha,285,4),
telefoneEditor=SUBSTR(@linha,289,8),	
dddFax=SUBSTR(@linha,297,4),
telefoneFax=SUBSTR(@linha,301,8),
inscricaoMunicipal=SUBSTR(@linha,315,12),
tipoPessoa=SUBSTR(@linha,327,1),
nomeContato=SUBSTR(@linha,328,30),
status=SUBSTR(@linha,361,1),
cpf=SUBSTR(@linha,367,12),
rg=SUBSTR(@linha,379,15),
orgaoEmissor=SUBSTR(@linha,394,10),
ufOrgaoEmissor=SUBSTR(@linha,404,2),
cnpj=SUBSTR(@linha,406,14),
inscricaoEstadual=TRIM(SUBSTR(@linha,420,20)),
bairroEditor=SUBSTR(@linha,440,5),
bairroEntrega=SUBSTR(@linha,445,5);

select 'CARGA_PRODIN_EDI:',count(*) from CARGA_PRODIN_EDI; -- Log 




--  CARGA_PRODIN_PUB
DROP TABLE IF EXISTS CARGA_PRODIN_PUB;

create table CARGA_PRODIN_PUB (
codigoDistribuidor VARCHAR(7) ,
dataGeracaoArquivo VARCHAR(8),
codContexto VARCHAR(2),
codigoFornecedor VARCHAR(7),
codigoProduto VARCHAR(8),
nome VARCHAR(30),
peb VARCHAR(3),
pacotePadrao VARCHAR(5),
condicaoTransmissaoHistograma VARCHAR(1),
porcentagemAbrangencia VARCHAR(3),
lancamentoImediato VARCHAR(1),
formaComercializacao VARCHAR(3),
formaPagamento VARCHAR(2),
condicaoPagamentoAntecipado VARCHAR(1),
condicaoPermissaoAlteracao VARCHAR(1),
percetualCobrancaAntecipada VARCHAR(7),
diasCobrancaAntecipada VARCHAR(3),
slogan VARCHAR(50),
comprimento VARCHAR(5),
largura VARCHAR(5),
espessura VARCHAR(5),
peso VARCHAR(5),
status VARCHAR(1),
dataDesativacao VARCHAR(8),
criterio1SelecaoCota VARCHAR(2),
argumento1Criterio1 VARCHAR(3),
argumento2Criterio1 VARCHAR(3),
argumento3Criterio1 VARCHAR(3),
tipoCombinacao1 VARCHAR(2),
criterio2SelecaoCota VARCHAR(2),
argumento1Criterio2 VARCHAR(3),
argumento2Criterio2 VARCHAR(3),
argumento3Criterio2 VARCHAR(3),
tipoCombinacao2 VARCHAR(2),
criterio3SelecaoCota VARCHAR(2),
argumento1Criterio3 VARCHAR(3),
argumento2Criterio3 VARCHAR(3),
argumento3Criterio3 VARCHAR(3),
tributacaoFiscal VARCHAR(1),
situacaoTributaria VARCHAR(1),
tipoRecolhimento VARCHAR(1),
periodicidade VARCHAR(3),
categoria VARCHAR(3),
colecao VARCHAR(3),
algoritmo VARCHAR(2),
codigoHistograma VARCHAR(3),
tipoHistograma VARCHAR(1),
contextoEditor VARCHAR(1),
codigoEditor VARCHAR(7),
tipoDesconto VARCHAR(2),
percentualLimiteCotasFixadas VARCHAR(3),
percentualLimiteFixacaoReparte VARCHAR(3),
sentidoAbrangencia VARCHAR(1),
grupoEditorial VARCHAR(3),
subGrupoEditorial VARCHAR(3),
codigoICD VARCHAR(6),
segmento VARCHAR(30),
fornecedorId VARCHAR(1),
KEY codigoProduto (codigoProduto)) 
ENGINE=MEMORY
;

LOAD DATA INFILE '/opt/rollout/load_files/00000001.pub' INTO TABLE CARGA_PRODIN_PUB CHARACTER SET UTF8
(@linha) SET 
codigoDistribuidor=SUBSTR(@linha,1,7),
dataGeracaoArquivo=SUBSTR(@linha,8,8),
codContexto=SUBSTR(@linha,26,2),
codigoFornecedor=SUBSTR(@linha,28,7),
codigoProduto=SUBSTR(@linha,35,8), 
nome=TRIM(SUBSTR(@linha,43,30)),
peb=SUBSTR(@linha,73,3), 
pacotePadrao=SUBSTR(@linha,76,5), 
condicaoTransmissaoHistograma=SUBSTR(@linha,81,1), 
porcentagemAbrangencia=SUBSTR(@linha,82,3), 
lancamentoImediato=SUBSTR(@linha,85,1), 
formaComercializacao=SUBSTR(@linha,86,3), 
formaPagamento=SUBSTR(@linha,89,2), 
condicaoPagamentoAntecipado=SUBSTR(@linha,91,1), 
condicaoPermissaoAlteracao=SUBSTR(@linha,92,1), 
percetualCobrancaAntecipada=SUBSTR(@linha,93,7), 
diasCobrancaAntecipada=SUBSTR(@linha,100,3), 
slogan=SUBSTR(@linha,103,50), 
comprimento=SUBSTR(@linha,153,5), 
largura=SUBSTR(@linha,158,5), 
espessura=SUBSTR(@linha,163,5), 
peso=SUBSTR(@linha,168,5), 
status=SUBSTR(@linha,173,1), 
dataDesativacao=SUBSTR(@linha,174,8), 
criterio1SelecaoCota=SUBSTR(@linha,182,2), 
argumento1Criterio1=SUBSTR(@linha,184,3), 
argumento2Criterio1=SUBSTR(@linha,187,3), 
argumento3Criterio1=SUBSTR(@linha,190,3), 
tipoCombinacao1=SUBSTR(@linha,193,2), 
criterio2SelecaoCota=SUBSTR(@linha,195,2), 
argumento1Criterio2=SUBSTR(@linha,197,3), 
argumento2Criterio2=SUBSTR(@linha,200,3), 
argumento3Criterio2=SUBSTR(@linha,203,3), 
tipoCombinacao2=SUBSTR(@linha,206,2), 
criterio3SelecaoCota=SUBSTR(@linha,208,2), 
argumento1Criterio3=SUBSTR(@linha,210,3), 
argumento2Criterio3=SUBSTR(@linha,213,3), 
argumento3Criterio3=SUBSTR(@linha,216,3), 
tributacaoFiscal=SUBSTR(@linha,219,1), 
situacaoTributaria=SUBSTR(@linha,220,1), 
tipoRecolhimento=SUBSTR(@linha,221,1), 
periodicidade=SUBSTR(@linha,222,3), 
categoria=SUBSTR(@linha,225,3), 
colecao=SUBSTR(@linha,228,3), 
algoritmo=SUBSTR(@linha,231,2),
codigoHistograma=SUBSTR(@linha,233,3),
tipoHistograma=SUBSTR(@linha,236,1),
contextoEditor=SUBSTR(@linha,237,1),
codigoEditor=SUBSTR(@linha,238,7),
tipoDesconto=SUBSTR(@linha,245,2),
percentualLimiteCotasFixadas=SUBSTR(@linha,247,3),
percentualLimiteFixacaoReparte=SUBSTR(@linha,250,3),
sentidoAbrangencia=SUBSTR(@linha,253,1),
grupoEditorial=SUBSTR(@linha,254,3),
subGrupoEditorial=SUBSTR(@linha,257,3),
codigoICD=SUBSTR(@linha,260,6),
segmento=SUBSTR(@linha,266,30),
fornecedorId=1;

LOAD DATA INFILE '/opt/rollout/load_files/00000002.pub' INTO TABLE CARGA_PRODIN_PUB CHARACTER SET UTF8
(@linha) SET 
codigoDistribuidor=SUBSTR(@linha,1,7),
dataGeracaoArquivo=SUBSTR(@linha,8,8),
codContexto=SUBSTR(@linha,26,2),
codigoFornecedor=SUBSTR(@linha,28,7),
codigoProduto=SUBSTR(@linha,35,8), 
nome=TRIM(SUBSTR(@linha,43,30)),
peb=SUBSTR(@linha,73,3), 
pacotePadrao=SUBSTR(@linha,76,5), 
condicaoTransmissaoHistograma=SUBSTR(@linha,81,1), 
porcentagemAbrangencia=SUBSTR(@linha,82,3), 
lancamentoImediato=SUBSTR(@linha,85,1), 
formaComercializacao=SUBSTR(@linha,86,3), 
formaPagamento=SUBSTR(@linha,89,2), 
condicaoPagamentoAntecipado=SUBSTR(@linha,91,1), 
condicaoPermissaoAlteracao=SUBSTR(@linha,92,1), 
percetualCobrancaAntecipada=SUBSTR(@linha,93,7), 
diasCobrancaAntecipada=SUBSTR(@linha,100,3), 
slogan=SUBSTR(@linha,103,50), 
comprimento=SUBSTR(@linha,153,5), 
largura=SUBSTR(@linha,158,5), 
espessura=SUBSTR(@linha,163,5), 
peso=SUBSTR(@linha,168,5), 
status=SUBSTR(@linha,173,1), 
dataDesativacao=SUBSTR(@linha,174,8), 
criterio1SelecaoCota=SUBSTR(@linha,182,2), 
argumento1Criterio1=SUBSTR(@linha,184,3), 
argumento2Criterio1=SUBSTR(@linha,187,3), 
argumento3Criterio1=SUBSTR(@linha,190,3), 
tipoCombinacao1=SUBSTR(@linha,193,2), 
criterio2SelecaoCota=SUBSTR(@linha,195,2), 
argumento1Criterio2=SUBSTR(@linha,197,3), 
argumento2Criterio2=SUBSTR(@linha,200,3), 
argumento3Criterio2=SUBSTR(@linha,203,3), 
tipoCombinacao2=SUBSTR(@linha,206,2), 
criterio3SelecaoCota=SUBSTR(@linha,208,2), 
argumento1Criterio3=SUBSTR(@linha,210,3), 
argumento2Criterio3=SUBSTR(@linha,213,3), 
argumento3Criterio3=SUBSTR(@linha,216,3), 
tributacaoFiscal=SUBSTR(@linha,219,1), 
situacaoTributaria=SUBSTR(@linha,220,1), 
tipoRecolhimento=SUBSTR(@linha,221,1), 
periodicidade=SUBSTR(@linha,222,3), 
categoria=SUBSTR(@linha,225,3), 
colecao=SUBSTR(@linha,228,3), 
algoritmo=SUBSTR(@linha,231,2),
codigoHistograma=SUBSTR(@linha,233,3),
tipoHistograma=SUBSTR(@linha,236,1),
contextoEditor=SUBSTR(@linha,237,1),
codigoEditor=SUBSTR(@linha,238,7),
tipoDesconto=SUBSTR(@linha,245,2),
percentualLimiteCotasFixadas=SUBSTR(@linha,247,3),
percentualLimiteFixacaoReparte=SUBSTR(@linha,250,3),
sentidoAbrangencia=SUBSTR(@linha,253,1),
grupoEditorial=SUBSTR(@linha,254,3),
subGrupoEditorial=SUBSTR(@linha,257,3),
codigoICD=SUBSTR(@linha,260,6),
segmento=SUBSTR(@linha,266,30),
fornecedorId=2; 

update CARGA_PRODIN_PUB 
set codigoICD = substring(codigoProduto,1,6) where codigoICD is null;

select 'CARGA_PRODIN_PUB:',count(*) from CARGA_PRODIN_PUB; -- Log 




--  CARGA_PRODIN_LAN
DROP TABLE IF EXISTS CARGA_PRODIN_LAN;

create table CARGA_PRODIN_LAN (
codigoDistribuidor VARCHAR(7),
dataGeracaoArquivo VARCHAR(8),
codContexto VARCHAR(1),
codigoFornecedorProduto VARCHAR(7),
codigoProduto VARCHAR(8),
codigoEdicao VARCHAR(4),
lancamento VARCHAR(2),
fase VARCHAR(1),
dataLancamento VARCHAR(8),
tipoLancamento VARCHAR(3),
tipoProduto VARCHAR(1),
repartePrevisto VARCHAR(8),
porcentagemAbrangencia VARCHAR(3),
percetualCobrancaAntecipada VARCHAR(3),
condCotasAtuais VARCHAR(1),
tipoBaseRegiao VARCHAR(3),
tipoHistorico VARCHAR(1),
condBasePacotePadrao VARCHAR(1),
precoPrevisto VARCHAR(10),
repartePromocional VARCHAR(8),
condDistribuicaoFases VARCHAR(1),
KEY codigoProdutoEdicao (codigoProduto,codigoEdicao)) 
ENGINE=MEMORY
;

LOAD DATA INFILE '/opt/rollout/load_files/00000001.lan' INTO TABLE CARGA_PRODIN_LAN CHARACTER SET UTF8
(@linha) SET 
codigoDistribuidor=SUBSTR(@linha,1,7),
dataGeracaoArquivo=SUBSTR(@linha,8,8),
codContexto=SUBSTR(@linha,26,1),
codigoFornecedorProduto=SUBSTR(@linha,27,7),
codigoProduto=SUBSTR(@linha,34,8),
codigoEdicao=SUBSTR(@linha,42,4),
lancamento=SUBSTR(@linha,46,2),
fase=SUBSTR(@linha,48,1),
dataLancamento=SUBSTR(@linha,50,8),
tipoLancamento=SUBSTR(@linha,58,3),
tipoProduto=SUBSTR(@linha,61,1),
repartePrevisto=SUBSTR(@linha,62,8),
porcentagemAbrangencia=SUBSTR(@linha,70,3),
percetualCobrancaAntecipada=SUBSTR(@linha,73,3),
condCotasAtuais=SUBSTR(@linha,76,1),
tipoBaseRegiao=SUBSTR(@linha,77,3),
tipoHistorico=SUBSTR(@linha,80,1),
condBasePacotePadrao=SUBSTR(@linha,81,1),
precoPrevisto=SUBSTR(@linha,82,10),
repartePromocional=SUBSTR(@linha,92,8),
condDistribuicaoFases=SUBSTR(@linha,100,1);

LOAD DATA INFILE '/opt/rollout/load_files/00000002.lan' INTO TABLE CARGA_PRODIN_LAN CHARACTER SET UTF8
(@linha) SET 
codigoDistribuidor=SUBSTR(@linha,1,7),
dataGeracaoArquivo=SUBSTR(@linha,8,8),
codContexto=SUBSTR(@linha,26,1),
codigoFornecedorProduto=SUBSTR(@linha,27,7),
codigoProduto=SUBSTR(@linha,34,8),
codigoEdicao=SUBSTR(@linha,42,4),
lancamento=SUBSTR(@linha,46,2),
fase=SUBSTR(@linha,48,1),
dataLancamento=SUBSTR(@linha,50,8),
tipoLancamento=SUBSTR(@linha,58,3),
tipoProduto=SUBSTR(@linha,61,1),
repartePrevisto=SUBSTR(@linha,62,8),
porcentagemAbrangencia=SUBSTR(@linha,70,3),
percetualCobrancaAntecipada=SUBSTR(@linha,73,3),
condCotasAtuais=SUBSTR(@linha,76,1),
tipoBaseRegiao=SUBSTR(@linha,77,3),
tipoHistorico=SUBSTR(@linha,80,1),
condBasePacotePadrao=SUBSTR(@linha,81,1),
precoPrevisto=SUBSTR(@linha,82,10),
repartePromocional=SUBSTR(@linha,92,8),
condDistribuicaoFases=SUBSTR(@linha,100,1);

select 'CARGA_PRODIN_LAN:',count(*) from CARGA_PRODIN_LAN; -- Log




--  CARGA_PRODIN_REC
DROP TABLE IF EXISTS CARGA_PRODIN_REC;

create table CARGA_PRODIN_REC (
codDistrib VARCHAR(7),
dataGeracaoArquivo VARCHAR(8),
codigoProduto VARCHAR(8),
codigoEdicao VARCHAR(4),
dataRecolhimento VARCHAR(8),
KEY codigoProdutoEdicao (codigoProduto,codigoEdicao)) 
ENGINE=MEMORY
;

LOAD DATA INFILE '/opt/rollout/load_files/00000001.rec' INTO TABLE CARGA_PRODIN_REC
(@linha) SET 
codDistrib=SUBSTR(@linha,1,7),
dataGeracaoArquivo=SUBSTR(@linha,8,8),
codigoProduto=SUBSTR(@linha,34,8),
codigoEdicao=SUBSTR(@linha,42,4),
dataRecolhimento=SUBSTR(@linha,46,8);

LOAD DATA INFILE '/opt/rollout/load_files/00000002.rec' INTO TABLE CARGA_PRODIN_REC
(@linha) SET 
codDistrib=SUBSTR(@linha,1,7),
dataGeracaoArquivo=SUBSTR(@linha,8,8),
codigoProduto=SUBSTR(@linha,34,8),
codigoEdicao=SUBSTR(@linha,42,4),
dataRecolhimento=SUBSTR(@linha,46,8);

select 'CARGA_PRODIN_REC:',count(*) from CARGA_PRODIN_REC; -- Log





--  CARGA_PRODIN_CHC
DROP TABLE IF EXISTS CARGA_PRODIN_CHC;

create table CARGA_PRODIN_CHC (
codDistrib VARCHAR(7),
dataGeracaoArquivo VARCHAR(8),
contextoProd VARCHAR(1),
codForncProd VARCHAR(7),
codigoProduto VARCHAR(8),
codigoEdicao VARCHAR(4),
chamadaCapa VARCHAR(15),
KEY codigoProdutoEdicao (codigoProduto,codigoEdicao)) 
ENGINE=MEMORY
;

LOAD DATA INFILE '/opt/rollout/load_files/00000001.chc' INTO TABLE CARGA_PRODIN_CHC CHARACTER SET UTF8
(@linha) SET 
codDistrib=SUBSTR(@linha,1,7),
dataGeracaoArquivo=SUBSTR(@linha,8,8),
contextoProd=SUBSTR(@linha,26,1),
codForncProd=SUBSTR(@linha,27,7),
codigoProduto=SUBSTR(@linha,34,8),
codigoEdicao=SUBSTR(@linha,42,4),
chamadaCapa=SUBSTR(@linha,46,15);

LOAD DATA INFILE '/opt/rollout/load_files/00000002.chc' INTO TABLE CARGA_PRODIN_CHC CHARACTER SET UTF8
(@linha) SET 
codDistrib=SUBSTR(@linha,1,7),
dataGeracaoArquivo=SUBSTR(@linha,8,8),
contextoProd=SUBSTR(@linha,26,1),
codForncProd=SUBSTR(@linha,27,7),
codigoProduto=SUBSTR(@linha,34,8),
codigoEdicao=SUBSTR(@linha,42,4),
chamadaCapa=SUBSTR(@linha,46,15);

select 'CARGA_PRODIN_CHC:',count(*) from CARGA_PRODIN_CHC; -- Log





--  CARGA_PRODIN_CDB
DROP TABLE IF EXISTS CARGA_PRODIN_CDB;

create table CARGA_PRODIN_CDB (
codDistribuidor VARCHAR(7),
dataGeracaoArquivo VARCHAR(8),
contextoProduto VARCHAR(1),
codFornecedorProduto VARCHAR(4),
codigoProduto VARCHAR(8),
codigoEdicao VARCHAR(4),
codigoBarras VARCHAR(18),
KEY codigoProdutoEdicao (codigoProduto,codigoEdicao)) 
ENGINE=MEMORY
;

LOAD DATA INFILE '/opt/rollout/load_files/00000001.cdb' INTO TABLE CARGA_PRODIN_CDB
(@linha) SET 
codDistribuidor=SUBSTR(@linha,1,7),
dataGeracaoArquivo=SUBSTR(@linha,8,8),
contextoProduto=SUBSTR(@linha,26,1),
codFornecedorProduto=SUBSTR(@linha,27,4),
codigoProduto=SUBSTR(@linha,34,8),
codigoEdicao=SUBSTR(@linha,42,4),
codigoBarras=SUBSTR(@linha,46,18);

LOAD DATA INFILE '/opt/rollout/load_files/00000002.cdb' INTO TABLE CARGA_PRODIN_CDB
(@linha) SET 
codDistribuidor=SUBSTR(@linha,1,7),
dataGeracaoArquivo=SUBSTR(@linha,8,8),
contextoProduto=SUBSTR(@linha,26,1),
codFornecedorProduto=SUBSTR(@linha,27,4),
codigoProduto=SUBSTR(@linha,34,8),
codigoEdicao=SUBSTR(@linha,42,4),
codigoBarras=SUBSTR(@linha,46,18);

select 'CARGA_PRODIN_CDB:',count(*) from CARGA_PRODIN_CDB; -- Log


-- #########
-- ## MDC ##
-- #########


--  CARGA_MDC_PRODUTO
DROP TABLE IF EXISTS CARGA_MDC_PRODUTO;

create table CARGA_MDC_PRODUTO (
codigoProduto VARCHAR(10),
nome VARCHAR(20),
numeroEdicoes VARCHAR(2),
periodicidade VARCHAR(3),
categoria VARCHAR(2),
status VARCHAR(1),
codigoEditor VARCHAR(7),
cobrancaAntecipada VARCHAR(6),
condTransmiteHistograma VARCHAR(1),
pacotePadrao VARCHAR(5),
condPublicacaoEspecial VARCHAR(1),
condProdutoAVista VARCHAR(1),
contextoFornecedorProduto VARCHAR(1),
codigoFornecedorPublic VARCHAR(7),
desconto VARCHAR(8),
origem VARCHAR(1),
cromos VARCHAR(1),
nomeComercial VARCHAR(45),
KEY codigoProduto (codigoProduto)) 
ENGINE=MEMORY
;

LOAD DATA INFILE '/opt/rollout/load_files/PRODUTO.NEW' INTO TABLE CARGA_MDC_PRODUTO CHARACTER SET UTF8
(@linha) SET 
codigoProduto=SUBSTR(@linha,1,8),
nome=SUBSTR(@linha,9,20),
numeroEdicoes=SUBSTR(@linha,29,2),
periodicidade=SUBSTR(@linha,31,3),
categoria=SUBSTR(@linha,34,2),
status=SUBSTR(@linha,36,1),
codigoEditor=SUBSTR(@linha,37,7),
cobrancaAntecipada=SUBSTR(@linha,44,6),
condTransmiteHistograma=SUBSTR(@linha,50,1),
pacotePadrao=SUBSTR(@linha,51,5),
condPublicacaoEspecial=SUBSTR(@linha,56,1),
condProdutoAVista=SUBSTR(@linha,57,1),
contextoFornecedorProduto=SUBSTR(@linha,58,1),
codigoFornecedorPublic=SUBSTR(@linha,59,7),
desconto=SUBSTR(@linha,66,8),
origem=SUBSTR(@linha,74,1),
cromos=SUBSTR(@linha,75,1),
nomeComercial=SUBSTR(@linha,76,45);

select 'CARGA_MDC_PRODUTO:',count(*) from CARGA_MDC_PRODUTO; -- Log





--  CARGA_MDC_COTA
DROP TABLE IF EXISTS CARGA_MDC_COTA;


create table CARGA_MDC_COTA (
codigoCota VARCHAR(5),
nomeJornaleiro VARCHAR(30),
qtdeCotas VARCHAR(4),
endereco VARCHAR(40),
codBairro VARCHAR(5),
municipio VARCHAR(25),
siglaUF VARCHAR(2),
cep VARCHAR(8),
ddd VARCHAR(4),
telefone VARCHAR(8),
situacaoCota VARCHAR(1),
condPrazoPagamento VARCHAR(1),
codBox VARCHAR(3),
codTipoBox VARCHAR(1),
repartePDV VARCHAR(1),
codCapataz VARCHAR(5),
cpf VARCHAR(14),
cnpj VARCHAR(14),
tipoPessoa VARCHAR(1),
numLogradouro VARCHAR(6),
codCidadeIbge VARCHAR(7),
inscrEstadual VARCHAR(20),
inscrMunicipal VARCHAR(15),
cotaDP VARCHAR(5),
cotaFC VARCHAR(5),
situacao_anterior VARCHAR (1),
data_alteracao_cota VARCHAR (12),
tipo_logradouro VARCHAR (12),
complemento VARCHAR (30),
bairro VARCHAR (60),
KEY codigoCota (codigoCota)) 
ENGINE=MEMORY
;



LOAD DATA INFILE '/opt/rollout/load_files/COTA.NEW' INTO TABLE CARGA_MDC_COTA CHARACTER SET UTF8
(@linha) SET 
codigoCota=SUBSTR(@linha,221,5), -- 1,4
nomeJornaleiro=trim(SUBSTR(@linha,5,30)),
qtdeCotas=SUBSTR(@linha,35,4),
endereco=trim(SUBSTR(@linha,39,40)),
codBairro=SUBSTR(@linha,79,5),
municipio=trim(SUBSTR(@linha,84,20)),
siglaUF=SUBSTR(@linha,104,2),
cep=SUBSTR(@linha,106,8),
ddd=SUBSTR(@linha,114,4),
telefone=SUBSTR(@linha,118,7),
situacaoCota=SUBSTR(@linha,125,1),
condPrazoPagamento=SUBSTR(@linha,126,1),
codBox=SUBSTR(@linha,127,3),
codTipoBox=SUBSTR(@linha,130,1),
repartePDV=SUBSTR(@linha,131,1),
codCapataz=SUBSTR(@linha,132,5),
cpf=SUBSTR(@linha,137,11),
cnpj=SUBSTR(@linha,148,14),
tipoPessoa=SUBSTR(@linha,162,1),
numLogradouro=SUBSTR(@linha,163,6),
codCidadeIbge=SUBSTR(@linha,169,7),
inscrEstadual=SUBSTR(@linha,176,20),
inscrMunicipal=SUBSTR(@linha,196,15),
cotaDP=SUBSTR(@linha,216,5),
cotaFC=SUBSTR(@linha,221,5),
situacao_anterior=SUBSTR(@linha,234,1),
data_alteracao_cota=SUBSTR(@linha,235,10),
tipo_logradouro=SUBSTR(@linha,245,30),
complemento=trim(SUBSTR(@linha,245,60)),
bairro=trim(SUBSTR(@linha,305,60));


update CARGA_MDC_COTA set cpf = null where tipoPessoa ='J';

update CARGA_MDC_COTA set cnpj = null where tipoPessoa ='F';
update carga_mdc_cota set inscrEstadual = null where tipoPessoa ='F';

create temporary table cota_par_cob(
	idCota bigint(20),
	idParCob bigint(20)
);

insert into cota_par_cob
select id idParCob, cota_id idCota from parametro_cobranca_cota;

update cota c
inner join cota_par_cob cpc on cpc.idCota = c.id
set c.parametro_cobranca_id = idParCob;



-- Normalizacao de endereços (verificar os que faltam) e os tipos logradouro


update CARGA_MDC_COTA set tipo_logradouro = 'TRAVESSA'  where upper(substr(endereco,1,8)) in ('TRAVESSA');
update CARGA_MDC_COTA set tipo_logradouro = 'PASSAGEM'  where upper(substr(endereco,1,8)) in ('PASSAGEM');

update CARGA_MDC_COTA set endereco = trim(substring( endereco,8)) 
where upper(substring(endereco,1,8)) in ('TRAVESSA','PASSAGEM');

update CARGA_MDC_COTA set tipo_logradouro = 'ALAMEDA'  where upper(substr(endereco,1,7)) in ('ALAMEDA');
update CARGA_MDC_COTA set tipo_logradouro = 'AVENIDA'  where upper(substr(endereco,1,7)) in ('AVENIDA');
update CARGA_MDC_COTA set tipo_logradouro = 'RODOVIA'  where upper(substr(endereco,1,7)) in ('RODOVIA');
update CARGA_MDC_COTA set tipo_logradouro = 'VIADUTO'  where upper(substr(endereco,1,7)) in ('VIADUTO');
update CARGA_MDC_COTA set tipo_logradouro = 'ESTRADA'  where upper(substr(endereco,1,7)) in ('ESTRADA');
update CARGA_MDC_COTA set tipo_logradouro = 'ESTAÇÃO'  where upper(substr(endereco,1,7)) in ('ESTACAO','ESTAÇÃO');

update CARGA_MDC_COTA set endereco = trim(substring(endereco,7)) 
where upper(substring(endereco,1,7)) in ('ALAMEDA','AVENIDA','RODOVIA','VIADUTO','ESTRADA','ESTACAO','ESTAÇÃO');

update CARGA_MDC_COTA set tipo_logradouro = 'VIELA'    where upper(substr(endereco,1,5)) in ('VIELA');
update CARGA_MDC_COTA set tipo_logradouro = 'ESTRADA'  where upper(substr(endereco,1,5)) in ('ESTR ','ESTR.');
update CARGA_MDC_COTA set tipo_logradouro = 'TRAVESSA' where upper(substr(endereco,1,5)) in ('TRAV ','TRAV.');
update CARGA_MDC_COTA set tipo_logradouro = 'LARGO'    where upper(substr(endereco,1,5)) in ('LARGO');
update CARGA_MDC_COTA set tipo_logradouro = 'PRAÇA'    where upper(substr(endereco,1,5)) in ('PRACA','PRAÇA');

update CARGA_MDC_COTA set endereco = trim(substring(endereco,5)) 
where upper(substring(endereco,1,5)) in ('LARGO','PRACA','PRAÇA','TRAV ','TRAV.','ESTR ','ESTR.','VIELA');

update CARGA_MDC_COTA set tipo_logradouro = 'AVENIDA'  where upper(substr(endereco,1,4)) in ('AV. '																																																																																																																																																																																								);
update CARGA_MDC_COTA set tipo_logradouro = 'ALAMENDA'  where upper(substr(endereco,1,4)) in ('ALM ','ALM.');
update CARGA_MDC_COTA set tipo_logradouro = 'ESTRADA'  where upper(substr(endereco,1,4)) in ('EST ','EST.');
update CARGA_MDC_COTA set tipo_logradouro = 'TRAVESSA'  where upper(substr(endereco,1,4)) in ('TRV ','TRV.');

update CARGA_MDC_COTA set endereco = trim(substring(endereco,4)) 
where upper(substring(endereco,1,4)) in ('EST ','EST.','TRV ','TRV.','ALM ','ALM.','AV. ');


-- 3 caracteres
update CARGA_MDC_COTA set tipo_logradouro = 'RUA'
where upper(substr(endereco,1,3)) in ('R. ','R..','RUA','RU ');

update CARGA_MDC_COTA set tipo_logradouro = 'AVENIDA'
where upper(substr(endereco,1,3)) in ('AV.','A. ','A..','AV ','AV:');

update CARGA_MDC_COTA set tipo_logradouro = 'PRAÇA'
where upper(substr(endereco,1,3)) in ('PC.','PCA','P..','PÇA','PC ','PÇ ','PR ');

update CARGA_MDC_COTA set tipo_logradouro = 'ESTRADA'
where upper(substr(endereco,1,3)) in ('ES.','ES ');

update CARGA_MDC_COTA set tipo_logradouro = 'LARGO'
where upper(substr(endereco,1,3)) in ('LR.','LR ');

update CARGA_MDC_COTA set tipo_logradouro = 'RODOVIA'
where upper(substr(endereco,1,3)) in ('RO.','RO ','ROD');

update CARGA_MDC_COTA set tipo_logradouro = 'ALAMEDA'
where upper(substr(endereco,1,3)) in ('AL.','AL ');

update CARGA_MDC_COTA set tipo_logradouro = 'TRAVESSA'
where upper(substr(endereco,1,3)) in ('TR.','TV.','TR ','TV ');

update CARGA_MDC_COTA set tipo_logradouro = 'VILA'
where upper(substr(endereco,1,3)) in ('VL.','VL ');

update CARGA_MDC_COTA set endereco = trim(substring( endereco,3)) 
where upper(substring( endereco,1,3)) in (
'R. ','R..','RUA','RU ','PR '
'AV.','A. ','A..','AV ','AV:'
'PC.','PCA','P..','PÇA','PC ','PÇ ',
'ES.','ES ',
'LR.','LR ',
'RO.','RO ','ROD'
'AL.','AL ',
'TR.','TV.','TR ','TV ','VL.','VL ');

update CARGA_MDC_COTA set tipo_logradouro = 'AVENIDA'  where upper(substr(endereco,1,2)) in ('A.','A:','A ');
update CARGA_MDC_COTA set tipo_logradouro = 'RUA'      where upper(substr(endereco,1,2)) in ('R.','R:','R ','E ');
update CARGA_MDC_COTA set tipo_logradouro = 'VIADUTO'  where upper(substr(endereco,1,2)) in ('VD');
update CARGA_MDC_COTA set tipo_logradouro = 'VILA'  where upper(substr(endereco,1,2)) in ('V ','V.');

update CARGA_MDC_COTA set endereco = trim(substring(endereco,2)) 
where upper(substring( endereco,1,2)) in (
'A.','A:','R.','R:','R ','VD','A ','E ','V ','V.'
);

update CARGA_MDC_COTA set endereco = trim(substring( endereco,2))
where substring(endereco,1,1) in (' ','.',':','-','_',',');

update CARGA_MDC_COTA set tipo_logradouro = 'RUA' where tipo_logradouro is null or trim(tipo_logradouro) ='';

update CARGA_MDC_COTA set municipio = 'SÃO BERNARDO DO CAMPO'
where municipio = 'SAO BERNARDO DO CAMP';

update CARGA_MDC_COTA set municipio = 'FERRAZ DE VASCONCELOS'
where municipio = 'FERRAZ DE VASCONCELO';

update CARGA_MDC_COTA set municipio = 'VARGEM GRANDE PAULISTA'
where municipio = 'VARGEM GRANDE PAULIS';


update CARGA_MDC_COTA 
set 
endereco = trim(substring(endereco,1,locate(',',endereco)-1)) -- ,
-- complemento = trim(substring(endereco,locate(',',endereco)+1,locate(' ',endereco))) 
where endereco like '%,%';

																																																																																																																																																																																																																																																																																																	

-- update CARGA_MDC_COTA set ddd = '11';

select 'CARGA_MDC_COTA:',count(*) from CARGA_MDC_COTA; -- Log




--  CARGA_MDC_COTA_TEL
DROP TABLE IF EXISTS CARGA_MDC_COTA_TEL;

create table CARGA_MDC_COTA_TEL (
codigoCota VARCHAR(5),
codigoDP VARCHAR(5),
codigoFC VARCHAR(5),
ddd VARCHAR(4),
telefone VARCHAR(8),
KEY codigoCota (codigoCota)) 
ENGINE=MEMORY
;

LOAD DATA INFILE '/opt/rollout/load_files/COTA_TEL.NEW' INTO TABLE CARGA_MDC_COTA_TEL CHARACTER SET UTF8
(@linha) SET 
codigoCota=SUBSTR(@linha,11,5),
codigoDP=SUBSTR(@linha,6,5),
codigoFC=SUBSTR(@linha,11,5),
ddd=SUBSTR(@linha,16,4),
telefone=SUBSTR(@linha,20,8);

-- update CARGA_MDC_COTA_TEL set ddd = '11';


UPDATE CARGA_MDC_COTA c,CARGA_MDC_COTA_TEL t 
set c.telefone = t.telefone
where c.codigoCota = t.codigoCota;

UPDATE CARGA_MDC_COTA c,CARGA_MDC_COTA_TEL t 
set c.ddd = t.ddd
where c.codigoCota = t.codigoCota;



--  CARGA_MDC_BANCA
DROP TABLE IF EXISTS CARGA_MDC_BANCA;

create table CARGA_MDC_BANCA (
codigoCota VARCHAR(5),
endereco VARCHAR(40),
codBairro VARCHAR(5),
nomeMunicipio VARCHAR(25),
siglaUF VARCHAR(2),
cep VARCHAR(8),
ddd VARCHAR(4),
telefone VARCHAR(8),
tipoPontoVenda VARCHAR(2),
pontoReferencia VARCHAR(40),
cotaDP VARCHAR(5),
cotaFC VARCHAR(5),
numLogradouro VARCHAR (30),
tipo_logradouro VARCHAR (12),
complemento VARCHAR (30),
bairro VARCHAR (60),
linha bigint(20) NOT NULL AUTO_INCREMENT,
PRIMARY KEY (linha),
KEY codigoCota (codigoCota)) 
ENGINE=MEMORY
;


LOAD DATA INFILE '/opt/rollout/load_files/BANCA.NEW' INTO TABLE CARGA_MDC_BANCA CHARACTER SET UTF8
(@linha) SET 
codigoCota=SUBSTR(@linha,143,5),
endereco=trim(SUBSTR(@linha,5,40)),
codBairro=SUBSTR(@linha,45,5),
nomeMunicipio=trim(SUBSTR(@linha,50,20)),
siglaUF=SUBSTR(@linha,70,2),
cep=SUBSTR(@linha,72,8),
ddd=SUBSTR(@linha,80,4),
telefone=SUBSTR(@linha,148,8),
tipoPontoVenda=SUBSTR(@linha,91,2),
pontoReferencia=SUBSTR(@linha,93,40),
cotaDP=SUBSTR(@linha,138,5),
cotaFC=SUBSTR(@linha,143,5),
bairro=trim(SUBSTR(@linha,216,60)),
complemento=trim(SUBSTR(@linha,156,60));

-- update CARGA_MDC_BANCA set ddd = '11';

select 'CARGA_MDC_BANCA:',count(*) from CARGA_MDC_BANCA; -- Log


--  CARGA_MDC_BANCA_TEL
DROP TABLE IF EXISTS CARGA_MDC_BANCA_TEL;

create table CARGA_MDC_BANCA_TEL (
codigoCota VARCHAR(5),
codigoDP VARCHAR(5),
codigoFC VARCHAR(5),
ddd VARCHAR(4),
telefone VARCHAR(8),
KEY codigoCota (codigoCota)) 
ENGINE=MEMORY
;

LOAD DATA INFILE '/opt/rollout/load_files/BANCA_TEL.NEW' INTO TABLE CARGA_MDC_BANCA_TEL CHARACTER SET UTF8
(@linha) SET 
-- codigoCota=SUBSTR(@linha,1,5),
codigoCota=SUBSTR(@linha,11,5),
codigoDP=SUBSTR(@linha,5,5),
codigoFC=SUBSTR(@linha,11,5),
ddd=SUBSTR(@linha,1,4),
telefone=SUBSTR(@linha,20,8);

-- update CARGA_MDC_BANCA_TEL set ddd = '11';

select 'CARGA_MDC_BANCA_TEL:',count(*) from CARGA_MDC_BANCA_TEL; -- Log

UPDATE CARGA_MDC_BANCA b,CARGA_MDC_BANCA_TEL t 
set b.telefone = t.telefone
where b.codigoCota = t.codigoCota;

UPDATE CARGA_MDC_BANCA b,CARGA_MDC_BANCA_TEL t 
set b.ddd = t.ddd
where b.codigoCota = t.codigoCota;




--  CARGA_MDC_MATRIZ
DROP TABLE IF EXISTS CARGA_MDC_MATRIZ;

create table CARGA_MDC_MATRIZ (
dataMovimento VARCHAR(8),
codigoProduto VARCHAR(8),
lancamento VARCHAR(4),
codigoEdicao VARCHAR(4),
dataLancamentoRecolhimento VARCHAR(8),
condRelancamento VARCHAR(1),
condImprimeBoleto VARCHAR(1),
condEncalheRetido VARCHAR(1),
flagAtual VARCHAR(1),
tipoRecolhimento VARCHAR(1),
condCobrancaTotal VARCHAR(1),
condProdutoEspecial VARCHAR(1),
peso VARCHAR(5),
codigoBarras VARCHAR(18),
codigoBarrasCorporativo VARCHAR(18),
codigoEdicaoOrigem VARCHAR(4),
slogan VARCHAR(50),
PEB VARCHAR(3),
pacotePadrao VARCHAR(8),
formaComercializacao VARCHAR(3),
tipoDesconto VARCHAR(2),
percentualDesconto VARCHAR(7),
periodicidade VARCHAR(3),
tributacaoFiscal VARCHAR(1),
KEY codigoProdutoEdicao (codigoProduto,codigoEdicao)) 
ENGINE=MEMORY
;

LOAD DATA INFILE '/opt/rollout/load_files/MATRIZ.NEW' INTO TABLE CARGA_MDC_MATRIZ CHARACTER SET UTF8
(@linha) SET 
dataMovimento=SUBSTR(@linha,1,8),
codigoProduto=SUBSTR(@linha,9,8),
lancamento=SUBSTR(@linha,17,4),
codigoEdicao=SUBSTR(@linha,21,4),
dataLancamentoRecolhimento=SUBSTR(@linha,25,8),
condRelancamento=SUBSTR(@linha,33,1),
condImprimeBoleto=SUBSTR(@linha,34,1),
condEncalheRetido=SUBSTR(@linha,35,1),
flagAtual=SUBSTR(@linha,36,1),
tipoRecolhimento=SUBSTR(@linha,37,1),
condCobrancaTotal=SUBSTR(@linha,38,1),
condProdutoEspecial=SUBSTR(@linha,39,1),
peso=SUBSTR(@linha,40,5),
codigoBarras=SUBSTR(@linha,45,18),
codigoBarrasCorporativo=SUBSTR(@linha,63,18),
codigoEdicaoOrigem=SUBSTR(@linha,81,4),
slogan=SUBSTR(@linha,85,50),
PEB=SUBSTR(@linha,135,3),
pacotePadrao=SUBSTR(@linha,138,8),
formaComercializacao=SUBSTR(@linha,146,3),
tipoDesconto=SUBSTR(@linha,149,2),
percentualDesconto=SUBSTR(@linha,151,7),
periodicidade=SUBSTR(@linha,158,3),
tributacaoFiscal=SUBSTR(@linha,161,1);

select 'CARGA_MDC_MATRIZ:',count(*) from CARGA_MDC_MATRIZ; -- Log

--  CARGA_MDC_PRECO
DROP TABLE IF EXISTS CARGA_MDC_PRECO;

create table CARGA_MDC_PRECO (
codigoProduto VARCHAR(8),
codigoEdicao VARCHAR(4),
preco VARCHAR(10),
condRecolhimentoFinal VARCHAR(1),
KEY codigoProdutoEdicao (codigoProduto,codigoEdicao)) 
ENGINE=MEMORY
;

LOAD DATA INFILE '/opt/rollout/load_files/PRECO.NEW' INTO TABLE CARGA_MDC_PRECO
(@linha) SET 
codigoProduto=SUBSTR(@linha,1,8),
codigoEdicao=SUBSTR(@linha,9,4),
preco=SUBSTR(@linha,13,10),
condRecolhimentoFinal=SUBSTR(@linha,23,1);

select 'CARGA_MDC_PRECO:',count(*) from CARGA_MDC_PRECO; -- Log

-- #########################################################################################################################################
-- ####################################################  DESCONTO  #########################################################################
-- #########################################################################################################################################

--  CARGA_PRODIN_DSF > DESCONTO_LOGISTICA.
insert into desconto_logistica (
DATA_INICIO_VIGENCIA,
DESCRICAO,
PERCENTUAL_DESCONTO,
PERCENTUAL_PRESTACAO_SERVICO,
TIPO_DESCONTO,
FORNECEDOR_ID)
SELECT distinct
DATE_SUB(str_to_date(dataGeracaoArquivo,'%Y%m%d'), INTERVAL 10 DAY ),
concat(tipoDesconto,' ',fornecedorId), -- NULL,
CAST(CONCAT(SUBSTRING(percentualDesconto,1,3),'.',SUBSTRING(percentualDesconto,4,4)) as DECIMAL(7,4)),
CAST(CONCAT(SUBSTRING(percentualPrestacao_servico,1,3),'.',SUBSTRING(percentualPrestacao_servico,4,4)) as DECIMAL(7,4)),
tipoDesconto,fornecedorId
FROM CARGA_PRODIN_DSF
group by tipoDesconto,fornecedorId
order by fornecedorId,tipoDesconto;

select 'DESCONTO_LOGISTICA:',count(*) from DESCONTO_LOGISTICA; -- Log

-- #######################################################################################################################################
-- ######################################################  EDITOR  #######################################################################
-- #######################################################################################################################################


-- FIXME * Remover ASSIM QUE CORRIGIREM CNPJ/INSCRICAO ESTADUAL
-- update CARGA_PRODIN_EDI 
-- set cnpj = codigoEditor
-- where trim(LEADING '0' FROM cnpj) ='' ;

-- Editor (Fornecedor Terceiro)
INSERT INTO CARGA_PRODIN_EDI VALUES
('8300428','I', '0008000','ROTEIROBR EDITORA','RUA','JOSE BENEDITO DUARTE','140','(12) 3014 7992','JACAREI','SP','12307-200','','','','','','','','','','','','','','','J','','S','','','','','11791785000184','392252849118',0,0); 


-- pessoa FIXME * corrigir abaixo ASSIM QUE CORRIGIREM CNPJ/INSCRICAO ESTADUAL
insert into pessoa (TIPO,CNPJ,INSC_ESTADUAL,RAZAO_SOCIAL,SOCIO_PRINCIPAL)
select max('J') ,cnpj,max(inscricaoEstadual),trim(LEADING ' ' from nomeEditor),0
from CARGA_PRODIN_EDI 
where cnpj not in ('28322873000130','03555225000100','61438248000123','11791785000184') -- FIXME
group by cnpj;

select 'EDITOR - PESSOA:',count(*) from PESSOA; -- Log

-- editor
insert into editor (ATIVO,CODIGO,NOME_CONTATO,ORIGEM_INTERFACE,JURIDICA_ID)
select IF(status ='S',1,0),CAST(codigoEditor AS UNSIGNED),
trim(nomeContato),null,p.id
from CARGA_PRODIN_EDI c,pessoa p
where p.cnpj = c.cnpj;

select 'EDITOR:',count(*) from EDITOR; -- Log

-- endereco (Editor) FIXME * Ajustar queries de endereco* abaixo
insert into endereco (BAIRRO,CEP,CIDADE,CODIGO_BAIRRO,CODIGO_CIDADE_IBGE,CODIGO_UF,
COMPLEMENTO,LOGRADOURO,NUMERO,TIPO_LOGRADOURO,UF,PESSOA_ID)
select 
IF(bairroEditor <> '00000','',bairroEditor),
cepEditor,
cidadeEditor,
null,
1,
11, -- FIXME corrigir ufEditor,
complementoEditor,
trim(logradouroEditor),
numeroEditor,
tipoLogradouroEditor,
ufEditor,
(select edi.juridica_id from editor edi where edi.codigo = codigoEditor limit 1) -- FIXME Remover o limit 1 assim que corrigir
from CARGA_PRODIN_EDI
group by cnpj;

select 'EDITOR - ENDERECO (EDITOR):',count(*) from ENDERECO; -- Log

-- endereco (Entrega)
insert into endereco (BAIRRO,CEP,CIDADE,CODIGO_BAIRRO,CODIGO_CIDADE_IBGE,CODIGO_UF,
COMPLEMENTO,LOGRADOURO,NUMERO,TIPO_LOGRADOURO,UF,PESSOA_ID)
select 
IF(bairroEntrega <> '00000','',bairroEntrega),
cepEntrega,
cidadeEntrega,
null,
2,
11,-- FIXME corrigir ufEntrega,
complementoEntrega,
trim(logradouroEntrega),
numeroEntrega,
tipoLogradouroEntrega,
ufEntrega,
(select edi.juridica_id from editor edi where edi.codigo = codigoEditor limit 1) -- FIXME Remover o limit 1 assim que corrigir
from CARGA_PRODIN_EDI
group by cnpj;

select 'EDITOR - ENDERECO (EDITOR+LOCAL_ENTREGA):',count(*) from ENDERECO; -- Log

-- endereco_editor (COMERCIAL)

insert into endereco_editor (PRINCIPAL,TIPO_ENDERECO,ENDERECO_ID,EDITOR_ID)
select * from (
select 
0,
'COMERCIAL',
(select ed.id from editor edi,endereco ed where edi.codigo = codigoEditor and edi.juridica_id = ed.pessoa_id and ed.CODIGO_CIDADE_IBGE = 1 limit 1) as endereco_id, -- FIXME Remover o limit 1 assim que corrigir
(select edi.id from editor edi where edi.codigo = codigoEditor limit 1) as editor_id -- FIXME Remover o limit 1 assim que corrigir
from CARGA_PRODIN_EDI
group by cnpj)a where a.endereco_id is not null;

select 'EDITOR - ENDERECO_EDITOR (COMERCIAL):',count(*) from ENDERECO_EDITOR; -- Log

-- endereco_editor (LOCAL_ENTREGA)
insert into endereco_editor (PRINCIPAL,TIPO_ENDERECO,ENDERECO_ID,EDITOR_ID)
select * from (
select 
0,
'LOCAL_ENTREGA',
(select ed.id from editor edi,endereco ed where edi.codigo = codigoEditor and edi.juridica_id = ed.pessoa_id and ed.CODIGO_CIDADE_IBGE = 2 limit 1)  as endereco_id,
(select edi.id from editor edi where edi.codigo = codigoEditor limit 1) as editor_id -- FIXME Remover o limit 1 assim que corrigir
from CARGA_PRODIN_EDI
group by cnpj)a where a.endereco_id is not null;

select 'EDITOR - ENDERECO_EDITOR (COMERCIAL+LOCAL_ENTREGA):',count(*) from ENDERECO_EDITOR; -- Log

-- telefone (CONTATO)
insert into telefone (DDD,NUMERO,RAMAL,PESSOA_ID)
select distinct cmc.dddContato,cmc.telefoneContato,1,pes.id 
from CARGA_PRODIN_EDI cmc,pessoa pes
where cmc.cnpj = pes.cnpj
group by pes.cnpj;

select 'EDITOR - TELEFONE (CONTATO):',count(*) from TELEFONE; -- Log

-- telefone (COMERCIAL)
insert into telefone (DDD,NUMERO,RAMAL,PESSOA_ID)
select distinct cmc.dddEditor,cmc.telefoneEditor,2,pes.id 
from CARGA_PRODIN_EDI cmc,pessoa pes
where cmc.cnpj = pes.cnpj
group by pes.cnpj;

select 'EDITOR - TELEFONE (CONTATO+COMERCIAL):',count(*) from TELEFONE; -- Log

-- telefone (FAX)
insert into telefone (DDD,NUMERO,RAMAL,PESSOA_ID)
select distinct cmc.dddFax,cmc.telefoneFax,3,pes.id 
from CARGA_PRODIN_EDI cmc,pessoa pes
where cmc.cnpj = pes.cnpj
group by pes.cnpj;

select 'EDITOR - TELEFONE (CONTATO+COMERCIAL+FAX):',count(*) from TELEFONE; -- Log

-- telefone_editor
insert into telefone_editor (PRINCIPAL,TIPO_TELEFONE,TELEFONE_ID,EDITOR_ID)
select  
IF (tel.ramal =2,1,0),
CASE
WHEN tel.ramal = 1 then 'CONTATO'
WHEN tel.ramal = 2 then 'COMERCIAL'
WHEN tel.ramal = 3 then 'FAX'
ELSE 'OUTRO'
END ,
tel.id,
edi.id
from CARGA_PRODIN_EDI cmc,pessoa pes,editor edi,telefone tel
where cmc.codigoEditor = edi.codigo
and pes.id = edi.juridica_id
and edi.juridica_id = tel.pessoa_id
group by pes.cnpj;

select 'EDITOR - TELEFONE_EDITOR:',count(*) from TELEFONE_EDITOR; -- Log

-- ########################################################################################################################################
-- ####################################################  PRODUTO  #########################################################################
-- ########################################################################################################################################


-- CARGA_PRODIN_PUB
insert into produto (DATA_CRIACAO,ATIVO,CODIGO,COD_CONTEXTO,DATA_DESATIVACAO,
desconto,COMPRIMENTO,ESPESSURA,LARGURA,fase,FORMA_COMERCIALIZACAO,LANCAMENTO_IMEDIATO,
NOME,NOME_COMERCIAL,numeroLancamento,ORIGEM,PACOTE_PADRAO,PEB,
PERCENTUAl_ABRANGENCIA,PERC_LIMITE_COTA_FIXACAO,PERC_LIMITE_REPARTE_FIXACAO,
PERIODICIDADE,PESO,CLASSE_SOCIAL,FAIXA_ETARIA,FORMATO_PRODUTO,SEXO,
TEMA_PRINCIPAL,TEMA_SECUNDARIO,SITUACAO_TRIBUTARIA,SLOGAN,TRIBUTACAO_FISCAL,
ALGORITMO_ID,DESCONTO_LOGISTICA_ID,EDITOR_ID,TIPO_PRODUTO_ID,DESCRICAO_DESCONTO, 
TIPO_SEGMENTO_PRODUTO_ID,DESCONTO_ID,codigo_icd,GRUPO_EDITORIAL,geracao_automatica,
FORMA_FISICA,SUB_GRUPO_EDITORIAL)
select distinct
IF(dataGeracaoArquivo = '00000000',sysdate(),str_to_date(dataGeracaoArquivo,'%Y%m%d')),
1, -- IF(status ='A',1,0), 
codigoProduto,
CAST(codContexto AS UNSIGNED),
IF(dataDesativacao = '00000000',null,str_to_date(dataDesativacao,'%Y%m%d')),
NULL, -- CAST(tipoDesconto AS UNSIGNED),
IF(comprimento = '00000',null,CAST(comprimento AS DECIMAL(6,2))),
IF(espessura = '00000',null,CAST(espessura AS DECIMAL(6,2))),
IF(largura = '00000',null,CAST(largura AS DECIMAL(6,2))),
NULL,
IF(formaComercializacao ='CON','CONSIGNADO','CONTA_FIRME'),
NULL,
trim(nome),
trim(nome),
NULL,
'INTERFACE',
CAST(pacotePadrao AS UNSIGNED),
CAST(peb AS UNSIGNED),
CAST(porcentagemAbrangencia AS UNSIGNED),
CAST(percentualLimiteCotasFixadas AS UNSIGNED),
CAST(percentualLimiteFixacaoReparte AS UNSIGNED),
CASE
WHEN periodicidade = 1 then 'DIARIO'
WHEN periodicidade = 2 then 'SEMANAL'
WHEN periodicidade = 3 then 'QUINZENAL'
WHEN periodicidade = 4 then 'MENSAL'
WHEN periodicidade = 5 then 'BIMESTRAL'
WHEN periodicidade = 6 then 'TRIMESTRAL'
WHEN periodicidade = 7 then 'SEMESTRAL'
WHEN periodicidade = 8 then 'ANUAL'
WHEN periodicidade = 9 then 'INDEFINIDO'
WHEN periodicidade = 10 then 'EXTRA'
ELSE 'INDEFINIDO'
END ,
CAST(peso AS UNSIGNED),
NULL,
NULL,
NULL,
NULL,
NULL,
NULL,
NULL, -- CAST(tributacaoFiscal AS UNSIGNED),
slogan,
CASE
WHEN tributacaoFiscal = 0 then 'TRIBUTADO'
WHEN tributacaoFiscal = 2 then 'ISENTO'
WHEN tributacaoFiscal = 3 then 'OUTROS'
ELSE 'INDEFINIDO'
END ,
NULL, -- (select id from algoritmo tz where codigo =  tz.algoritmo),
(select id from desconto_logistica tz where tipo_desconto =  tz.tipo_desconto and fornecedorId = tz.fornecedor_id limit 1), 
(select id from editor tz where codigoEditor =  tz.codigo limit 1), -- FIXME Remover o limit 1 assim que corrigir
IF((select id from tipo_produto tz where CAST(tz.codigo AS UNSIGNED) =  CAST(categoria AS UNSIGNED)) is NULL,16,(select id from tipo_produto tz where CAST(tz.codigo AS UNSIGNED) =  CAST(categoria AS UNSIGNED))) ,
NULL,
IF((select id from tipo_segmento_produto tz where trim(tz.descricao) = trim(segmento) limit 1)=null,9,(select id from tipo_segmento_produto tz where trim(tz.descricao) = trim(segmento) limit 1)), 
NULL,
codigoICD, 
grupoEditorial, 
0, -- IF(lancamentoImediato ='N',0,1),
NULL, 
subGrupoEditorial 
from CARGA_PRODIN_PUB group by codigoProduto;




select 'PRODUTO - (PUB)',count(*) from PRODUTO; -- Log

-- FIXME -- SOMENTE PARA SAO JOSE
-- 

/*
update carga_mdc_produto
set codigoEditor = '0008000', codigoProduto = concat('10',codigoProduto)
where codigoFornecedorPublic <> '9999999';


update carga_mdc_matriz
set codigoProduto = concat('10',codigoProduto)
where concat('10',codigoProduto) in (select codigoProduto from carga_mdc_produto where codigoFornecedorPublic <> '9999999');
*/

-- MDC PRODUTO
insert into produto (DATA_CRIACAO,ATIVO,CODIGO,COD_CONTEXTO,DATA_DESATIVACAO,
desconto,COMPRIMENTO,ESPESSURA,LARGURA,fase,FORMA_COMERCIALIZACAO,LANCAMENTO_IMEDIATO,
NOME,NOME_COMERCIAL,numeroLancamento,ORIGEM,PACOTE_PADRAO,PEB,
PERCENTUAl_ABRANGENCIA,PERC_LIMITE_COTA_FIXACAO,PERC_LIMITE_REPARTE_FIXACAO,
PERIODICIDADE,PESO,CLASSE_SOCIAL,FAIXA_ETARIA,FORMATO_PRODUTO,SEXO,
TEMA_PRINCIPAL,TEMA_SECUNDARIO,SITUACAO_TRIBUTARIA,SLOGAN,TRIBUTACAO_FISCAL,
ALGORITMO_ID,DESCONTO_LOGISTICA_ID,EDITOR_ID,TIPO_PRODUTO_ID,DESCRICAO_DESCONTO, 
TIPO_SEGMENTO_PRODUTO_ID,DESCONTO_ID,codigo_icd,GRUPO_EDITORIAL,geracao_automatica,
FORMA_FISICA,SUB_GRUPO_EDITORIAL)
select 
sysdate(), -- IF(dataGeracaoArquivo = '00000000',null,str_to_date(dataGeracaoArquivo,'%Y%m%d')),
1, -- IF(status ='A',1,0), 
codigoProduto,
NULL, -- CAST(codContexto AS UNSIGNED),
NULL, -- IF(dataDesativacao = '00000000',null,str_to_date(dataDesativacao,'%Y%m%d')),
NULL, -- CAST(tipoDesconto AS UNSIGNED),
NULL, -- IF(comprimento = '00000',null,CAST(comprimento AS DECIMAL(6,2))),
NULL, -- IF(espessura = '00000',null,CAST(espessura AS DECIMAL(6,2))),
NULL, -- IF(largura = '00000',null,CAST(largura AS DECIMAL(6,2))),
NULL,
'CONSIGNADO', -- IF(formaComercializacao ='CON','CONSIGNADO','CONTA_FIRME'),
NULL,
IF(nome = '',trim(nomeComercial),trim(nome)),
IF(nomeComercial = '',trim(nome),trim(nomeComercial)),
NULL,
'MANUAL',
CAST(pacotePadrao AS UNSIGNED),
31, -- CAST(peb AS UNSIGNED),
NULL, -- CAST(porcentagemAbrangencia AS UNSIGNED),
NULL, -- CAST(percentualLimiteCotasFixadas AS UNSIGNED),
NULL, -- CAST(percentualLimiteFixacaoReparte AS UNSIGNED),
CASE
WHEN periodicidade = 1 then 'DIARIO'
WHEN periodicidade = 2 then 'SEMANAL'
WHEN periodicidade = 3 then 'QUINZENAL'
WHEN periodicidade = 4 then 'MENSAL'
WHEN periodicidade = 5 then 'BIMESTRAL'
WHEN periodicidade = 6 then 'TRIMESTRAL'
WHEN periodicidade = 7 then 'SEMESTRAL'
WHEN periodicidade = 8 then 'ANUAL'
WHEN periodicidade = 9 then 'INDEFINIDO'
WHEN periodicidade = 10 then 'EXTRA'
ELSE 'INDEFINIDO'
END ,
0, -- CAST(peso AS UNSIGNED),
NULL,
NULL,
NULL,
NULL,
NULL,
NULL,
NULL, -- CAST(tributacaoFiscal AS UNSIGNED),
NULL, -- slogan,
NULL, -- 
/*
CASE
WHEN tributacaoFiscal = 0 then 'TRIBUTADO'
WHEN tributacaoFiscal = 2 then 'ISENTO'
WHEN tributacaoFiscal = 3 then 'OUTROS'
ELSE 'INDEFINIDO'
END ,
*/
NULL, -- (select id from algoritmo tz where codigo =  tz.algoritmo),
NULL, -- (select id from desconto_logistica tz where tipo_desconto =  tz.tipo_desconto and fornecedorId = tz.fornecedor_id limit 1), 
(select id from editor tz where codigoEditor =  tz.codigo  limit 1), -- FIXME Remover o limit 1 assim que corrigir
IF((select id from tipo_produto tz where CAST(tz.codigo AS UNSIGNED) =  CAST(categoria AS UNSIGNED)) is NULL,16,(select id from tipo_produto tz where CAST(tz.codigo AS UNSIGNED) =  CAST(categoria AS UNSIGNED))) ,
NULL,
9, -- (select id from tipo_segmento_produto tz where tz.descricao like segmento limit 1), 
NULL,
substring(codigoProduto,1,6), 
NULL, -- grupoEditorial, 
0, -- CAST(lancamentoImediato AS UNSIGNED),
NULL, 
NULL -- subGrupoEditorial 
from CARGA_MDC_PRODUTO
where codigoProduto not in (select codigo from produto)
group by codigoProduto;

select 'PRODUTO - (PUB+PRODUTO)',count(*) from PRODUTO; -- Log

-- PRODIN PRD
insert into produto (DATA_CRIACAO,ATIVO,CODIGO,COD_CONTEXTO,DATA_DESATIVACAO,
desconto,COMPRIMENTO,ESPESSURA,LARGURA,fase,FORMA_COMERCIALIZACAO,LANCAMENTO_IMEDIATO,
NOME,NOME_COMERCIAL,numeroLancamento,ORIGEM,PACOTE_PADRAO,PEB,
PERCENTUAl_ABRANGENCIA,PERC_LIMITE_COTA_FIXACAO,PERC_LIMITE_REPARTE_FIXACAO,
PERIODICIDADE,PESO,CLASSE_SOCIAL,FAIXA_ETARIA,FORMATO_PRODUTO,SEXO,
TEMA_PRINCIPAL,TEMA_SECUNDARIO,SITUACAO_TRIBUTARIA,SLOGAN,TRIBUTACAO_FISCAL,
ALGORITMO_ID,DESCONTO_LOGISTICA_ID,EDITOR_ID,TIPO_PRODUTO_ID,DESCRICAO_DESCONTO, 
TIPO_SEGMENTO_PRODUTO_ID,DESCONTO_ID,codigo_icd,GRUPO_EDITORIAL,geracao_automatica,
FORMA_FISICA,SUB_GRUPO_EDITORIAL)
select distinct
sysdate(), -- IF(dataGeracaoArquivo = '00000000',sysdate(),str_to_date(dataGeracaoArquivo,'%Y%m%d')),
1,-- IF(status ='A',1,0), 
codigoProduto,
CAST(codContexto AS UNSIGNED),
IF(dataDesativacao = '00000000',null,str_to_date(dataDesativacao,'%Y%m%d')),
NULL, -- CAST(tipoDesconto AS UNSIGNED),
IF(comprimento = '00000',null,CAST(comprimento AS DECIMAL(6,2))),
IF(espessura = '00000',null,CAST(espessura AS DECIMAL(6,2))),
IF(largura = '00000',null,CAST(largura AS DECIMAL(6,2))),
NULL,
IF(formaComercializacao ='CON','CONSIGNADO','CONTA_FIRME'),
NULL,
IF(nome = '',trim(nomeComercial),trim(nome)),
IF(nomeComercial = '',trim(nome),trim(nomeComercial)),
NULL,
'INTERFACE',
CAST(pacotePadrao AS UNSIGNED),
CAST(peb AS UNSIGNED),
NULL, -- CAST(porcentagemAbrangencia AS UNSIGNED),
NULL, -- CAST(percentualLimiteCotasFixadas AS UNSIGNED),
NULL, -- CAST(percentualLimiteFixacaoReparte AS UNSIGNED),
CASE
WHEN periodicidade = 1 then 'DIARIO'
WHEN periodicidade = 2 then 'SEMANAL'
WHEN periodicidade = 3 then 'QUINZENAL'
WHEN periodicidade = 4 then 'MENSAL'
WHEN periodicidade = 5 then 'BIMESTRAL'
WHEN periodicidade = 6 then 'TRIMESTRAL'
WHEN periodicidade = 7 then 'SEMESTRAL'
WHEN periodicidade = 8 then 'ANUAL'
WHEN periodicidade = 9 then 'INDEFINIDO'
WHEN periodicidade = 10 then 'EXTRA'
ELSE 'INDEFINIDO'
END ,
CAST(peso AS UNSIGNED),
NULL,
NULL,
NULL,
NULL,
NULL,
NULL,
NULL, -- CAST(codigoTributacaoFiscal AS UNSIGNED),
NULL, -- slogan,
CASE
WHEN tributacaoFiscal = 0 then 'TRIBUTADO'
WHEN tributacaoFiscal = 2 then 'ISENTO'
WHEN tributacaoFiscal = 3 then 'OUTROS'
ELSE 'INDEFINIDO'
END ,
NULL, -- (select id from algoritmo tz where codigo =  tz.algoritmo),
(select id from desconto_logistica tz where tipo_desconto =  tz.tipo_desconto and fornecedorId = tz.fornecedor_id limit 1), 
(select id from editor tz where codigoEditor =  tz.codigo limit 1), -- FIXME Remover o limit 1 assim que corrigir
IF((select id from tipo_produto tz where CAST(tz.codigo AS UNSIGNED) =  CAST(categoria AS UNSIGNED)) is NULL,16,(select id from tipo_produto tz where CAST(tz.codigo AS UNSIGNED) =  CAST(categoria AS UNSIGNED))) ,
NULL,
IF(segmento not in (select lpad(id,2,'0') from tipo_segmento_produto),9,CAST(segmento AS UNSIGNED)), -- (select id from tipo_segmento_produto tz where tz.descricao like segmento limit 1),  
NULL,
substring(codigoProduto,1,6),-- codigoICD, 
NULL, -- grupoEditorial, 
0, -- NULL, CAST(lancamentoImediato AS UNSIGNED),
NULL, 
NULL -- subGrupoEditorial 
from CARGA_PRODIN_PRD
where codigoProduto not in (select codigo from produto)
group by codigoProduto;


-- PRODIN LAN
insert into produto (DATA_CRIACAO,ATIVO,CODIGO,COD_CONTEXTO,DATA_DESATIVACAO,
desconto,COMPRIMENTO,ESPESSURA,LARGURA,fase,FORMA_COMERCIALIZACAO,LANCAMENTO_IMEDIATO,
NOME,NOME_COMERCIAL,numeroLancamento,ORIGEM,PACOTE_PADRAO,PEB,
PERCENTUAl_ABRANGENCIA,PERC_LIMITE_COTA_FIXACAO,PERC_LIMITE_REPARTE_FIXACAO,
PERIODICIDADE,PESO,CLASSE_SOCIAL,FAIXA_ETARIA,FORMATO_PRODUTO,SEXO,
TEMA_PRINCIPAL,TEMA_SECUNDARIO,SITUACAO_TRIBUTARIA,SLOGAN,TRIBUTACAO_FISCAL,
ALGORITMO_ID,DESCONTO_LOGISTICA_ID,EDITOR_ID,TIPO_PRODUTO_ID,DESCRICAO_DESCONTO, 
TIPO_SEGMENTO_PRODUTO_ID,DESCONTO_ID,codigo_icd,GRUPO_EDITORIAL,geracao_automatica,
FORMA_FISICA,SUB_GRUPO_EDITORIAL)
select distinct
IF(dataGeracaoArquivo = '00000000',sysdate(),str_to_date(dataGeracaoArquivo,'%Y%m%d')),
1, -- IF(status ='A',1,0), 
codigoProduto,
CAST(codContexto AS UNSIGNED),
NULL,-- IF(dataDesativacao = '00000000',null,str_to_date(dataDesativacao,'%Y%m%d')),
NULL, -- CAST(tipoDesconto AS UNSIGNED),
NULL, -- IF(comprimento = '00000',null,CAST(comprimento AS DECIMAL(6,2))),
NULL, -- IF(espessura = '00000',null,CAST(espessura AS DECIMAL(6,2))),
NULL, -- IF(largura = '00000',null,CAST(largura AS DECIMAL(6,2))),
NULL,
'CONSIGNADO', -- IF(formaComercializacao ='CON','CONSIGNADO','CONTA_FIRME'),
NULL,
' ',
' ',
NULL,
'INTERFACE',
1, -- CAST(pacotePadrao AS UNSIGNED),
31, -- CAST(peb AS UNSIGNED),
CAST(porcentagemAbrangencia AS UNSIGNED),
NULL, -- CAST(percentualLimiteCotasFixadas AS UNSIGNED),
NULL, -- CAST(percentualLimiteFixacaoReparte AS UNSIGNED),
'INDEFINIDO', -- 
/*
CASE
WHEN periodicidade = 1 then 'DIARIO'
WHEN periodicidade = 2 then 'SEMANAL'
WHEN periodicidade = 3 then 'QUINZENAL'
WHEN periodicidade = 4 then 'MENSAL'
WHEN periodicidade = 5 then 'BIMESTRAL'
WHEN periodicidade = 6 then 'TRIMESTRAL'
WHEN periodicidade = 7 then 'SEMESTRAL'
WHEN periodicidade = 8 then 'ANUAL'
WHEN periodicidade = 9 then 'INDEFINIDO'
WHEN periodicidade = 10 then 'EXTRA'
ELSE 'INDEFINIDO'
END ,
*/
0, -- CAST(peso AS UNSIGNED),
NULL,
NULL,
NULL,
NULL,
NULL,
NULL,
NULL, -- CAST(codigoTributacaoFiscal AS UNSIGNED),
NULL, -- slogan,
NULL, -- 
/*
CASE
WHEN tributacaoFiscal = 0 then 'TRIBUTADO'
WHEN tributacaoFiscal = 2 then 'ISENTO'
WHEN tributacaoFiscal = 3 then 'OUTROS'
ELSE 'INDEFINIDO'
END ,
*/
NULL, -- (select id from algoritmo tz where codigo =  tz.algoritmo),
NULL, -- (select id from desconto_logistica tz where tipo_desconto =  tz.tipo_desconto and fornecedorId = tz.fornecedor_id limit 1), 
NULL, -- (select id from editor tz where codigoEditor =  tz.codigo),
1, -- IF((select id from tipo_produto tz where CAST(tz.codigo AS UNSIGNED) =  CAST(categoria AS UNSIGNED)) is NULL,16,(select id from tipo_produto tz where CAST(tz.codigo AS UNSIGNED) =  CAST(categoria AS UNSIGNED))) ,
NULL,
9, -- IF(segmento= '00',9,CAST(segmento AS UNSIGNED)), -- (select id from tipo_segmento_produto tz where tz.descricao like segmento limit 1), 
NULL,
substring(codigoProduto,1,6),-- codigoICD, 
NULL, -- grupoEditorial, 
0, -- NULL, CAST(lancamentoImediato AS UNSIGNED),
NULL, 
NULL -- subGrupoEditorial 
from CARGA_PRODIN_LAN
where codigoProduto not in (select codigo from produto)
group by codigoProduto;

update produto set codigo_icd = cast(codigo_icd as unsigned);


select 'PRODUTO - (PUB+PRODUTO+PRD+LAN)',count(*) from PRODUTO; -- Log

-- MDC MATRIZ
insert into produto (DATA_CRIACAO,ATIVO,CODIGO,COD_CONTEXTO,DATA_DESATIVACAO,
desconto,COMPRIMENTO,ESPESSURA,LARGURA,fase,FORMA_COMERCIALIZACAO,LANCAMENTO_IMEDIATO,
NOME,NOME_COMERCIAL,numeroLancamento,ORIGEM,PACOTE_PADRAO,PEB,
PERCENTUAl_ABRANGENCIA,PERC_LIMITE_COTA_FIXACAO,PERC_LIMITE_REPARTE_FIXACAO,
PERIODICIDADE,PESO,CLASSE_SOCIAL,FAIXA_ETARIA,FORMATO_PRODUTO,SEXO,
TEMA_PRINCIPAL,TEMA_SECUNDARIO,SITUACAO_TRIBUTARIA,SLOGAN,TRIBUTACAO_FISCAL,
ALGORITMO_ID,DESCONTO_LOGISTICA_ID,EDITOR_ID,TIPO_PRODUTO_ID,DESCRICAO_DESCONTO, 
TIPO_SEGMENTO_PRODUTO_ID,DESCONTO_ID,codigo_icd,GRUPO_EDITORIAL,geracao_automatica,
FORMA_FISICA,SUB_GRUPO_EDITORIAL)
select 
sysdate(), -- IF(dataGeracaoArquivo = '00000000',null,str_to_date(dataGeracaoArquivo,'%Y%m%d')),
1, -- IF(status ='A',1,0), 
codigoProduto,
NULL, -- CAST(codContexto AS UNSIGNED),
NULL, -- IF(dataDesativacao = '00000000',null,str_to_date(dataDesativacao,'%Y%m%d')),
NULL, -- CAST(tipoDesconto AS UNSIGNED),
NULL, -- IF(comprimento = '00000',null,CAST(comprimento AS DECIMAL(6,2))),
NULL, -- IF(espessura = '00000',null,CAST(espessura AS DECIMAL(6,2))),
NULL, -- IF(largura = '00000',null,CAST(largura AS DECIMAL(6,2))),
NULL,
IF(formaComercializacao ='CON','CONSIGNADO','CONTA_FIRME'),
NULL,
' ',
' ',
NULL,
'MANUAL',
CAST(pacotePadrao AS UNSIGNED),
CAST(peb AS UNSIGNED),
NULL, -- CAST(porcentagemAbrangencia AS UNSIGNED),
NULL, -- CAST(percentualLimiteCotasFixadas AS UNSIGNED),
NULL, -- CAST(percentualLimiteFixacaoReparte AS UNSIGNED),
CASE
WHEN periodicidade = 1 then 'DIARIO'
WHEN periodicidade = 2 then 'SEMANAL'
WHEN periodicidade = 3 then 'QUINZENAL'
WHEN periodicidade = 4 then 'MENSAL'
WHEN periodicidade = 5 then 'BIMESTRAL'
WHEN periodicidade = 6 then 'TRIMESTRAL'
WHEN periodicidade = 7 then 'SEMESTRAL'
WHEN periodicidade = 8 then 'ANUAL'
WHEN periodicidade = 9 then 'INDEFINIDO'
WHEN periodicidade = 10 then 'EXTRA'
ELSE 'INDEFINIDO'
END ,
CAST(peso AS UNSIGNED),
NULL,
NULL,
NULL,
NULL,
NULL,
NULL,
NULL, -- CAST(tributacaoFiscal AS UNSIGNED),
trim(slogan),
CASE
WHEN tributacaoFiscal = 0 then 'TRIBUTADO'
WHEN tributacaoFiscal = 2 then 'ISENTO'
WHEN tributacaoFiscal = 3 then 'OUTROS'
ELSE 'INDEFINIDO'
END ,
NULL, -- (select id from algoritmo tz where codigo =  tz.algoritmo),
1, -- (select id from desconto_logistica tz where tipo_desconto =  tz.tipo_desconto and fornecedorId = tz.fornecedor_id limit 1), 
1, -- (select id from editor tz where codigoEditor =  tz.codigo),
1, -- IF((select id from tipo_produto tz where CAST(tz.codigo AS UNSIGNED) =  CAST(categoria AS UNSIGNED)) is NULL,16,(select id from tipo_produto tz where CAST(tz.codigo AS UNSIGNED) =  CAST(categoria AS UNSIGNED))) ,
NULL,
9, -- (select id from tipo_segmento_produto tz where tz.descricao like segmento limit 1), 
NULL,
substring(codigoProduto,1,6), 
NULL, -- grupoEditorial, 
0, -- NULL,CAST(lancamentoImediato AS UNSIGNED),
NULL, 
NULL 
from CARGA_MDC_MATRIZ
where codigoProduto not in (select codigo from produto)
group by codigoProduto;

select 'PRODUTO - (PUB+PRODUTO+PRD+LAN+MATRIZ)',count(*) from PRODUTO; -- Log

-- **** UPDATES ****

-- PEB
update produto
set peb = 
(CASE
WHEN periodicidade = 'DIARIO' then 1
WHEN periodicidade = 'SEMANAL' then 9
WHEN periodicidade = 'QUINZENAL' then 18
WHEN periodicidade = 'MENSAL' then 30
WHEN periodicidade = 'BIMESTRAL' then 60
WHEN periodicidade = 'TRIMESTRAL' then 90
WHEN periodicidade = 'SEMESTRAL' then 180
WHEN periodicidade = 'ANUAL' then 360
WHEN periodicidade = 'INDEFINIDO' then 7
WHEN periodicidade = 'EXTRA' then 14
ELSE 0
END ) where peb = 0;

-- COD_CONTEXTO
update produto p, CARGA_PRODIN_PRD c
set p.cod_contexto = c.codContexto
where p.codigo = c.codigoProduto
and p.cod_contexto is not null
and trim(p.cod_contexto) <>''
and c.codContexto is not null
and trim(c.codContexto) <>'';

-- SEGMENTO
update produto p, carga_prodin_prd cpp
set p.tipo_segmento_produto_id  = 
IF(cpp.segmento not in (select lpad(id,2,'0') from tipo_segmento_produto),9,CAST(cpp.segmento AS UNSIGNED)) -- ERRO FIXME * Remover essa linha e descomentar a de baixo assim que arrumarem os segmentos
-- CAST(cpp.segmento AS UNSIGNED)
where p.codigo = cpp.codigoProduto;

select 'PRODUTO - SEGMENTO(OUTROS)',count(*) from PRODUTO where tipo_segmento_produto_id =9; -- Log

-- DESCONTO FIXME * Ajustar com o valor de desconto correto
update produto
set desconto = 25.00
where origem = 'MANUAL';

select 'PRODUTO - DESCONTO:',count(*) from PRODUTO; -- Log

--
-- PRODUTO_FORNECEDOR FIXME * Reformular a query abaixo
--

insert into produto_fornecedor
select distinct p.id,f.id from produto p , carga_prodin_pub c, fornecedor f
where p.codigo = c.codigoProduto
and f.cod_interface = c.codigoDistribuidor;

insert into produto_fornecedor
select id,1 from produto where id not in (select distinct produto_id from produto_fornecedor); -- FIXME * reconstruir essa query com fornecedor correto



-- FIXME -- SOMENTE PARA SAO JOSE
-- 


update produto_fornecedor 
set fornecedores_ID = 3
where produto_id in (select id from produto where codigo in (select codigoProduto from carga_mdc_produto where codigoFornecedorPublic <> '9999999'));




select 'PRODUTO_FORNECEDOR:',count(*) from PRODUTO_FORNECEDOR; -- Log

-- ########################################################################################################################################
-- ##################################################  PRODUTO_EDICAO  ####################################################################
-- ########################################################################################################################################

-- PRODIN PRD
insert into produto_edicao (
TIPO,ATIVO,BOLETIM_INFORMATIVO,DESCRICAO_BRINDE,VENDE_BRINDE_SEPARADO,CHAMADA_CAPA, -- 6
CODIGO_DE_BARRAS_CORPORATIVO,CODIGO_DE_BARRAS,DATA_DESATIVACAO,COMPRIMENTO,ESPESSURA,LARGURA, -- 6
EXPECTATIVA_VENDA,NOME_COMERCIAL,NUMERO_EDICAO,ORIGEM,PACOTE_PADRAO,PARCIAL,PEB,PERMITE_VALE_DESCONTO, -- 8
PESO,POSSUI_BRINDE,PRECO_CUSTO,PRECO_PREVISTO,PRECO_VENDA,REPARTE_DISTRIBUIDO,DESCONTO_LOGISTICA_ID, -- 7
PRODUTO_ID,PRODUTO_EDICAO_ID,BRINDE_ID,CLASSE_SOCIAL,FAIXA_ETARIA,FORMATO_PRODUTO,SEXO, -- 7
TEMA_PRINCIPAL,TEMA_SECUNDARIO,TIPO_LANCAMENTO,CARACTERISTICA_PRODUTO,DESCONTO,DESCRICAO_DESCONTO,GRUPO_PRODUTO, -- 7
DESCONTO_ID,FORMA_FISICA,TIPO_CLASSIFICACAO_PRODUTO_ID,HISTORICO,VINCULAR_RECOLHIMENTO) -- 5
select  
TIPO,ATIVO,BOLETIM_INFORMATIVO,DESCRICAO_BRINDE,VENDE_BRINDE_SEPARADO,CHAMADA_CAPA, -- 6
TRIM(CODIGO_DE_BARRAS_CORPORATIVO),TRIM(CODIGO_DE_BARRAS),DATA_DESATIVACAO,COMPRIMENTO,ESPESSURA,LARGURA, -- 6
EXPECTATIVA_VENDA,NOME_COMERCIAL,NUMERO_EDICAO,ORIGEM,PACOTE_PADRAO,PARCIAL,PEB,PERMITE_VALE_DESCONTO, -- 8
PESO,POSSUI_BRINDE,PRECO_CUSTO,PRECO_PREVISTO,PRECO_VENDA,REPARTE_DISTRIBUIDO,DESCONTO_LOGISTICA_ID, -- 7
PRODUTO_ID,PRODUTO_EDICAO_ID,BRINDE_ID,CLASSE_SOCIAL,FAIXA_ETARIA,FORMATO_PRODUTO,SEXO, -- 7
TEMA_PRINCIPAL,TEMA_SECUNDARIO,TIPO_LANCAMENTO,CARACTERISTICA_PRODUTO,DESCONTO,DESCRICAO_DESCONTO,GRUPO_PRODUTO, -- 7
DESCONTO_ID,FORMA_FISICA,TIPO_CLASSIFICACAO_PRODUTO_ID,HISTORICO,VINCULAR_RECOLHIMENTO
from (
select
'PRODUTO' as TIPO,
1 as ATIVO, -- IF(status ='A',1,0) as ATIVO, 
NULL as BOLETIM_INFORMATIVO,
NULL as DESCRICAO_BRINDE,
NULL as VENDE_BRINDE_SEPARADO,
chamadaCapa as CHAMADA_CAPA,
codigoBarrasCorporativo as CODIGO_DE_BARRAS_CORPORATIVO,
codigoBarras as CODIGO_DE_BARRAS,
IF(dataDesativacao = '00000000',null,str_to_date(dataDesativacao,'%Y%m%d')) as DATA_DESATIVACAO,
IF(comprimento = '00000',0,CAST(comprimento AS DECIMAL(6,2))) as COMPRIMENTO,
IF(espessura = '00000',0,CAST(espessura AS DECIMAL(6,2))) as ESPESSURA,
IF(largura = '00000',0,CAST(largura AS DECIMAL(6,2))) as LARGURA,
NULL as EXPECTATIVA_VENDA,
trim(nomeComercial) as NOME_COMERCIAL,
CAST(codigoEdicao AS UNSIGNED) as NUMERO_EDICAO, -- CAST(IF(codigoEdicao ='0000',lancamento,codigoEdicao) AS UNSIGNED),
'INTERFACE' as ORIGEM,
CAST(pacotePadrao AS UNSIGNED) as PACOTE_PADRAO,
IF(tipoRecolhimento ='P',1,0) as PARCIAL,
IF(CAST(peb AS UNSIGNED) = 0,31,CAST(peb AS UNSIGNED)) as PEB,
0 as PERMITE_VALE_DESCONTO,
CAST(peso AS UNSIGNED) PESO,
IF(contemBrinde ='S',1,0) as POSSUI_BRINDE,
(select cast(max(precoPrevisto) as unsigned)/100 from carga_prodin_lan cpl where cpl.codigoProduto = c.codigoProduto and cpl.codigoEdicao = c.codigoEdicao) as PRECO_CUSTO,-- 0, --
(select cast(max(precoPrevisto) as unsigned)/100 from carga_prodin_lan cpl where cpl.codigoProduto = c.codigoProduto and cpl.codigoEdicao = c.codigoEdicao) as PRECO_PREVISTO,-- 0, --
(select cast(max(precoPrevisto) as unsigned)/100 from carga_prodin_lan cpl where cpl.codigoProduto = c.codigoProduto and cpl.codigoEdicao = c.codigoEdicao) as PRECO_VENDA,-- 0, --
NULL as REPARTE_DISTRIBUIDO,
IF(tipoDesconto = '00',1,(select id from desconto_logistica tz where tz.tipo_desconto  = tipoDesconto and fornecedorId = tz.fornecedor_id limit 1)) as DESCONTO_LOGISTICA_ID,
IF((select id from produto tz where tz.codigo = codigoProduto limit 1) = null,1,(select id from produto tz where tz.codigo = codigoProduto limit 1)) as PRODUTO_ID,
NULL as PRODUTO_EDICAO_ID,
NULL as BRINDE_ID,
NULL as CLASSE_SOCIAL,
NULL as FAIXA_ETARIA,
NULL as FORMATO_PRODUTO,
NULL as SEXO,
NULL as TEMA_PRINCIPAL,
NULL as TEMA_SECUNDARIO,
NULL as TIPO_LANCAMENTO,
NULL as CARACTERISTICA_PRODUTO,
NULL as DESCONTO,
NULL as DESCRICAO_DESCONTO,
NULL as GRUPO_PRODUTO,
NULL as DESCONTO_ID,
NULL as FORMA_FISICA,
IF(classificacao = '00',16,(select id from tipo_classificacao_produto tcp where tcp.descricao = classificacao limit 1)) as TIPO_CLASSIFICACAO_PRODUTO_ID,
NULL as HISTORICO,
NULL as VINCULAR_RECOLHIMENTO
from CARGA_PRODIN_PRD c
group by codigoProduto,codigoEdicao

UNION ALL
-- CARGA_MDC_MATRIZ
select
'PRODUTO' as TIPO,
1 as ATIVO, -- IF(status ='A',1,0), 
NULL as BOLETIM_INFORMATIVO,
NULL as DESCRICAO_BRINDE,
NULL as VENDE_BRINDE_SEPARADO,
NULL as CHAMADA_CAPA, -- chamadaCapa,
codigoBarrasCorporativo as CODIGO_DE_BARRAS_CORPORATIVO,
codigoBarras as CODIGO_DE_BARRAS,
NULL as DATA_DESATIVACAO, -- IF(dataDesativacao = '0000000',null,str_to_date(dataDesativacao,'%Y%m%d')),
0 as COMPRIMENTO, -- IF(comprimento = '00000',null,CAST(comprimento AS DECIMAL(6,2))),
0 as ESPESSURA, -- IF(espessura = '00000',null,CAST(espessura AS DECIMAL(6,2))),
0 as LARGURA, -- IF(largura = '00000',null,CAST(largura AS DECIMAL(6,2))),
NULL as NOME,
NULL as NOME_COMERCIAL, -- nomeComercial,
CAST(IF(codigoEdicao ='0000',lancamento,codigoEdicao) AS UNSIGNED) as NUMERO_EDICAO,
'MANUAL' as ORIGEM,
CAST(pacotePadrao AS UNSIGNED) as PACOTE_PADRAO,
IF(tipoRecolhimento ='P',1,0) as PARCIAL,
IF(CAST(peb AS UNSIGNED) = 0,31,CAST(peb AS UNSIGNED)) as PEB,
0 as PERMITE_VALE_DESCONTO,
CAST(peso AS UNSIGNED) as PESO,
0 as POSSUI_BRINDE,-- CAST(contemBrinde AS UNSIGNED),
(select (max(precoPrevisto) * 1) / 10000 from carga_prodin_lan cpl where cpl.codigoProduto = c.codigoProduto and cpl.codigoEdicao = c.codigoEdicao) as PRECO_CUSTO,-- 0, --
(select (max(precoPrevisto) * 1) / 10000 from carga_prodin_lan cpl where cpl.codigoProduto = c.codigoProduto and cpl.codigoEdicao = c.codigoEdicao) as PRECO_PREVISTO,-- 0, --
(select (max(precoPrevisto) * 1) / 10000 from carga_prodin_lan cpl where cpl.codigoProduto = c.codigoProduto and cpl.codigoEdicao = c.codigoEdicao) as PRECO_VENDA,-- 0, --
NULL as REPARTE_DISTRIBUIDO,
1 as DESCONTO_LOGISTICA_ID, -- IF((tipoDesconto = '00' or tipoDesconto =NULL),1,(select id from desconto_logistica tz where tz.tipo_desconto  = tipoDesconto and fornecedorId = tz.fornecedor_id)),
IF((select id from produto tz where tz.codigo = codigoProduto limit 1) = null,1,(select id from produto tz where tz.codigo = codigoProduto limit 1))  as PRODUTO_ID,
NULL as PRODUTO_EDICAO_ID,
NULL as BRINDE_ID,
NULL as CLASSE_SOCIAL,
NULL as FAIXA_ETARIA,
NULL as FORMATO_PRODUTO,
NULL as SEXO,
NULL as TEMA_PRINCIPAL,
NULL as TEMA_SECUNDARIO,
NULL as TIPO_LANCAMENTO,
NULL as CARACTERISTICA_PRODUTO,
NULL as DESCONTO,
NULL as DESCRICAO_DESCONTO,
NULL as GRUPO_PRODUTO,
NULL as DESCONTO_ID,
NULL as FORMA_FISICA,
16  as TIPO_CLASSIFICACAO_PRODUTO_ID, -- IF(classificacao = '00',16,classificacao),
NULL as HISTORICO,
NULL as VINCULAR_RECOLHIMENTO
from CARGA_MDC_MATRIZ c
group by codigoProduto,CAST(IF(codigoEdicao ='0000',lancamento,codigoEdicao) AS UNSIGNED )) uni 
group by uni.PRODUTO_ID,NUMERO_EDICAO;

select 'PRODUTO_EDICAO DUPLICADOS:'; -- Log
select * from (
select produto_id,numero_edicao,sum(1) as qtdes 
from produto_edicao 
group by produto_id,numero_edicao) a
where a.qtdes >1;

select 'PRODUTO_EDICAO:',count(*) from PRODUTO_EDICAO; -- Log

-- Atualizar Descontos Produtos
update produto_edicao
set desconto = 30.00 -- Rio 30, Campinas 25
where origem = 'MANUAL';

-- preco_previsto * FIXME Isso está sendo feito devido a interface 118 não atualizar preço_previsto e deixa o atributo com zero
update produto_edicao set preco_previsto = preco_venda
where (preco_previsto = 0 or preco_previsto is null)
and preco_venda is not null and preco_venda > 0;


update produto_edicao
set 
codigo_de_barras_corporativo = CAST(codigo_de_barras_corporativo as unsigned),
codigo_de_barras = CAST(codigo_de_barras as unsigned)
where produto_id in (select distinct id from produto where tipo_segmento_produto_id <> 2);

-- ########################################################################################################################################
-- ######################################################  LANÇAMENTOS  ###################################################################
-- #########################################################################################################################################

-- PRODIN LAN
insert into lancamento (
ALTERADO_INTERFACE,DATA_CRIACAO,DATA_LCTO_DISTRIBUIDOR,DATA_LCTO_PREVISTA,DATA_REC_DISTRIB,
DATA_REC_PREVISTA,DATA_FIN_MAT_DISTRIB,DATA_STATUS,REPARTE,REPARTE_PROMOCIONAL,
SEQUENCIA_MATRIZ,STATUS,TIPO_LANCAMENTO,PRODUTO_EDICAO_ID,EXPEDICAO_ID,USUARIO_ID, 
NUMERO_LANCAMENTO,PERIODO_LANCAMENTO_PARCIAL_ID,NUMERO_REPROGRAMACOES,ESTUDO_ID,juramentado)

select 
ALTERADO_INTERFACE,DATA_CRIACAO,DATA_LCTO_DISTRIBUIDOR,DATA_LCTO_PREVISTA,DATA_REC_DISTRIB,
DATA_REC_PREVISTA,DATA_FIN_MAT_DISTRIB,DATA_STATUS,REPARTE,REPARTE_PROMOCIONAL,
SEQUENCIA_MATRIZ,STATUS,TIPO_LANCAMENTO,PRODUTO_EDICAO_ID,EXPEDICAO_ID,USUARIO_ID, 
NUMERO_LANCAMENTO,PERIODO_LANCAMENTO_PARCIAL_ID,NUMERO_REPROGRAMACOES,ESTUDO_ID,juramentado
from (
select
1 as ALTERADO_INTERFACE,
IF(dataGeracaoArquivo ='00000000',sysdate(),str_to_date(dataGeracaoArquivo,'%Y%m%d')) as DATA_CRIACAO,
str_to_date(dataLancamento,'%Y%m%d') as DATA_LCTO_DISTRIBUIDOR,
str_to_date(dataLancamento,'%Y%m%d') as DATA_LCTO_PREVISTA,
ADDDATE(str_to_date(dataLancamento,'%Y%m%d'),(select peb from produto_edicao where numero_edicao = CAST(codigoEdicao AS UNSIGNED) and  produto_id = (select id from produto p where p.codigo = codigoProduto limit 1) limit 1)) as DATA_REC_DISTRIB,
ADDDATE(str_to_date(dataLancamento,'%Y%m%d'),(select peb from produto_edicao where numero_edicao = CAST(codigoEdicao AS UNSIGNED) and  produto_id = (select id from produto p where p.codigo = codigoProduto limit 1) limit 1)) as DATA_REC_PREVISTA,
NULL as DATA_FIN_MAT_DISTRIB,
SYSDATE() as DATA_STATUS,
CAST(repartePrevisto AS DECIMAL(18,4)) as REPARTE,
CAST(repartePromocional AS DECIMAL(18,4)) as REPARTE_PROMOCIONAL,
NULL as SEQUENCIA_MATRIZ,
'CONFIRMADO' as STATUS, -- IF(repartePrevisto = '00000000','CANCELADO','CONFIRMADO') as STATUS,
'LANCAMENTO' as TIPO_LANCAMENTO,
(select pe.id from produto_edicao pe where pe.produto_id = (select id from produto p where p.codigo = codigoProduto limit 1) and pe.numero_edicao = codigoEdicao limit 1) as PRODUTO_EDICAO_ID,
NULL as EXPEDICAO_ID,
NULL as USUARIO_ID,
CAST(lancamento AS UNSIGNED) as NUMERO_LANCAMENTO,
NULL as PERIODO_LANCAMENTO_PARCIAL_ID,
NULL as NUMERO_REPROGRAMACOES,
NULL as ESTUDO_ID,
NULL as juramentado,
codigoProduto as CODIGO,
codigoEdicao as EDICAO
from CARGA_PRODIN_LAN

UNION

select 
1 as ALTERADO_INTERFACE,
max(sysdate()) as DATA_CRIACAO,
max(dataLancamentoRecolhimento) as DATA_LCTO_DISTRIBUIDOR,
max(dataLancamentoRecolhimento) as DATA_LCTO_PREVISTA,
max(dataMovimento) as DATA_REC_DISTRIB,
max(dataMovimento) as DATA_REC_PREVISTA,
NULL as DATA_FIN_MAT_DISTRIB,
max(SYSDATE()) as DATA_STATUS,
0 as REPARTE, -- CAST(repartePrevisto AS DECIMAL(18,4)),
0 as REPARTE_PROMOCIONAL, -- CAST(repartePromocional AS DECIMAL(18,4)),
NULL as SEQUENCIA_MATRIZ,
'FECHADO' as STATUS,
'LANCAMENTO' as TIPO_LANCAMENTO,
(select pe.id from produto_edicao pe where pe.produto_id = (select id from produto p where p.codigo = codigoProduto) and pe.numero_edicao = if(codigoEdicao <>'0000',codigoEdicao,lancamento) ) as PRODUTO_EDICAO_ID,
NULL as EXPEDICAO_ID,
NULL as USUARIO_ID,
max(CAST(lancamento AS UNSIGNED)) as NUMERO_LANCAMENTO,
NULL as PERIODO_LANCAMENTO_PARCIAL_ID,
NULL as NUMERO_REPROGRAMACOES,
NULL as ESTUDO_ID,
NULL as juramentado,
codigoProduto as CODIGO,
if(codigoEdicao <>'0000',codigoEdicao,lancamento) as EDICAO
from CARGA_MDC_MATRIZ
group by 1,7,9,10,11,12,13,14,15,16,18,19,20,21,22,23
) uni group by uni.CODIGO,uni.EDICAO;

/*
select 
ALTERADO_INTERFACE,DATA_CRIACAO,DATA_LCTO_DISTRIBUIDOR,DATA_LCTO_PREVISTA,DATA_REC_DISTRIB,
DATA_REC_PREVISTA,DATA_FIN_MAT_DISTRIB,DATA_STATUS,REPARTE,REPARTE_PROMOCIONAL,
SEQUENCIA_MATRIZ,STATUS,TIPO_LANCAMENTO,PRODUTO_EDICAO_ID,EXPEDICAO_ID,USUARIO_ID, 
NUMERO_LANCAMENTO,PERIODO_LANCAMENTO_PARCIAL_ID,NUMERO_REPROGRAMACOES,ESTUDO_ID,juramentado
from (
select
1 as ALTERADO_INTERFACE,
IF(dataGeracaoArquivo ='00000000',sysdate(),str_to_date(dataGeracaoArquivo,'%Y%m%d')) as DATA_CRIACAO,
str_to_date(dataLancamento,'%Y%m%d') as DATA_LCTO_DISTRIBUIDOR,
str_to_date(dataLancamento,'%Y%m%d') as DATA_LCTO_PREVISTA,
ADDDATE(str_to_date(dataLancamento,'%Y%m%d'),(select peb from produto_edicao where numero_edicao = CAST(codigoEdicao AS UNSIGNED) and  produto_id = (select id from produto p where p.codigo = codigoProduto limit 1) limit 1)) as DATA_REC_DISTRIB,
ADDDATE(str_to_date(dataLancamento,'%Y%m%d'),(select peb from produto_edicao where numero_edicao = CAST(codigoEdicao AS UNSIGNED) and  produto_id = (select id from produto p where p.codigo = codigoProduto limit 1) limit 1)) as DATA_REC_PREVISTA,
NULL as DATA_FIN_MAT_DISTRIB,
SYSDATE() as DATA_STATUS,
CAST(repartePrevisto AS DECIMAL(18,4)) as REPARTE,
CAST(repartePromocional AS DECIMAL(18,4)) as REPARTE_PROMOCIONAL,
NULL as SEQUENCIA_MATRIZ,
'CONFIRMADO' as STATUS, -- IF(repartePrevisto = '00000000','CANCELADO','CONFIRMADO') as STATUS,
'LANCAMENTO' as TIPO_LANCAMENTO,
(select pe.id from produto_edicao pe where pe.produto_id = (select id from produto p where p.codigo = codigoProduto limit 1) and pe.numero_edicao = codigoEdicao limit 1) as PRODUTO_EDICAO_ID,
NULL as EXPEDICAO_ID,
NULL as USUARIO_ID,
CAST(lancamento AS UNSIGNED) as NUMERO_LANCAMENTO,
NULL as PERIODO_LANCAMENTO_PARCIAL_ID,
NULL as NUMERO_REPROGRAMACOES,
NULL as ESTUDO_ID,
NULL as juramentado,
codigoProduto as CODIGO,
codigoEdicao as EDICAO
from CARGA_PRODIN_LAN

UNION

select
1 as ALTERADO_INTERFACE,
sysdate() as DATA_CRIACAO, -- str_to_date(dataGeracaoArquivo,'%Y%m%d'),
str_to_date(if(codigoEdicao='0000',dataMovimento,dataLancamentoRecolhimento),'%Y%m%d') as DATA_LCTO_DISTRIBUIDOR,
str_to_date(if(codigoEdicao='0000',dataMovimento,dataLancamentoRecolhimento),'%Y%m%d') as DATA_LCTO_PREVISTA,
-- ADDDATE(str_to_date(if(codigoEdicao='0000',dataMovimento,dataLancamentoRecolhimento),'%Y%m%d'),(select peb from produto_edicao where numero_edicao = CAST(codigoEdicao AS UNSIGNED) and  produto_id = (select id from produto p where p.codigo = codigoProduto))) as DATA_REC_DISTRIB,
-- ADDDATE(str_to_date(if(codigoEdicao='0000',dataMovimento,dataLancamentoRecolhimento),'%Y%m%d'),(select peb from produto_edicao where numero_edicao = CAST(codigoEdicao AS UNSIGNED) and  produto_id = (select id from produto p where p.codigo = codigoProduto))) as DATA_REC_PREVISTA,
if(dataMovimento <>'00000000',str_to_date(dataMovimento,'%Y%m%d'),ADDDATE(str_to_date(dataMovimento,'%Y%m%d'),(select peb from produto_edicao where numero_edicao = CAST(codigoEdicao AS UNSIGNED) and  produto_id = (select id from produto p where p.codigo = codigoProduto)))) as DATA_REC_DISTRIB,
if(dataMovimento <>'00000000',str_to_date(dataMovimento,'%Y%m%d'),ADDDATE(str_to_date(dataMovimento,'%Y%m%d'),(select peb from produto_edicao where numero_edicao = CAST(codigoEdicao AS UNSIGNED) and  produto_id = (select id from produto p where p.codigo = codigoProduto)))) as DATA_REC_PREVISTA,
NULL as DATA_FIN_MAT_DISTRIB,
SYSDATE() as DATA_STATUS,
0 as REPARTE, -- CAST(repartePrevisto AS DECIMAL(18,4)),
0 as REPARTE_PROMOCIONAL, -- CAST(repartePromocional AS DECIMAL(18,4)),
NULL as SEQUENCIA_MATRIZ,
if(codigoEdicao='0000','PLANEJADO','FECHADO') as STATUS,
'LANCAMENTO' as TIPO_LANCAMENTO,
(select pe.id from produto_edicao pe where pe.produto_id = (select id from produto p where p.codigo = codigoProduto) and pe.numero_edicao = if(codigoEdicao ='0000',lancamento,codigoEdicao) ) as PRODUTO_EDICAO_ID,
NULL as EXPEDICAO_ID,
NULL as USUARIO_ID,
CAST(lancamento AS UNSIGNED) as NUMERO_LANCAMENTO,
NULL as PERIODO_LANCAMENTO_PARCIAL_ID,
NULL as NUMERO_REPROGRAMACOES,
NULL as ESTUDO_ID,
NULL as juramentado,
codigoProduto as CODIGO,
if(codigoEdicao='0000',lancamento,codigoEdicao) as EDICAO
from CARGA_MDC_MATRIZ
) uni group by uni.CODIGO,uni.EDICAO;
*/
select 'LANCAMENTO:',count(*) from LANCAMENTO; -- Log

--
-- LANÇAMENTOS RECOLHIMENTOS
--

update produto p , produto_edicao pe ,lancamento l, CARGA_PRODIN_REC c
set l.DATA_REC_DISTRIB = c.dataRecolhimento ,l.DATA_REC_PREVISTA = c.dataRecolhimento
where p.id = pe.produto_id
and pe.id = l.produto_edicao_id
and p.codigo = c.codigoProduto
and pe.numero_edicao = c.codigoEdicao;

--
-- PRODUTO EDICAO CHAMDA CAPA 
--

update produto p , produto_edicao pe , CARGA_PRODIN_CHC c
set pe.CHAMADA_CAPA = c.chamadaCapa
where p.id = pe.produto_id
and p.codigo = c.codigoProduto
and pe.numero_edicao = c.codigoEdicao;

--
-- PRODUTO EDICAO CODIGO DE BARRAS 
--

update produto p , produto_edicao pe , CARGA_PRODIN_CDB c
set pe.CODIGO_DE_BARRAS = TRIM(c.codigoBarras)
where p.id = pe.produto_id
and p.codigo = c.codigoProduto
and pe.numero_edicao = c.codigoEdicao
and trim(c.codigoBarras) <> '';

--
-- PRODUTO EDICAO PRECO 
--

update produto p , produto_edicao pe , CARGA_MDC_PRECO c
set 
pe.PRECO_VENDA = c.preco,
pe.PRECO_PREVISTO = c.preco, 
pe.PRECO_CUSTO = c.preco - (c.preco*(select fator_desconto from distribuidor)/100)
where p.id = pe.produto_id
and p.codigo = c.codigoProduto
and pe.numero_edicao = c.codigoEdicao
and trim(LEADING '0' FROM c.preco) <> '.';

--
-- COTA PESSOA 
--

select 'PESSOA:',count(*) from pessoa; -- Log

insert into pessoa (TIPO,CNPJ,INSC_ESTADUAL,INSC_MUNICIPAL,RAZAO_SOCIAL,NOME,SOCIO_PRINCIPAL)
select distinct 
tipoPessoa,
cnpj,
inscrEstadual,
inscrMunicipal,
trim(nomeJornaleiro),
trim(nomeJornaleiro),
0
from CARGA_MDC_COTA 
where tipoPessoa ='J'
and cnpj not in (select distinct cnpj from pessoa where tipo = 'J')
group by cnpj;

select 'PESSOA (COTA JURIDICA):',count(*) from pessoa; -- Log

insert into pessoa (TIPO,CPF,RAZAO_SOCIAL,NOME,SOCIO_PRINCIPAL)
select distinct tipoPessoa,cpf,nomeJornaleiro,nomeJornaleiro,0
from CARGA_MDC_COTA 
where tipoPessoa ='F'
and cpf not in (select distinct cpf from pessoa where tipo = 'F')
group by cpf;

select 'PESSOA (COTA FISICA):',count(*) from pessoa; -- Log

--
-- COTA
--
insert into cota (INICIO_ATIVIDADE,INICIO_TITULARIDADE,NUMERO_COTA,POSSUI_CONTRATO,
SITUACAO_CADASTRO,SUGERE_SUSPENSAO,SUGERE_SUSPENSAO_DISTRIBUIDOR,PESSOA_ID,TIPO_COTA,TIPO_DISTRIBUICAO_COTA,
recebe_complementar,recebe_recolhe_parciais )
select distinct
(select data_operacao from distribuidor),
sysdate(),
codigoCota,
0,
/*
CASE
WHEN situacaoCota = 1 then 'ATIVO'
WHEN situacaoCota = 2 then 'SUSPENSO'
WHEN situacaoCota = 3 then 'INATIVO' -- 'PENDENTE'
WHEN situacaoCota = 4 then 'INATIVO'
WHEN situacaoCota = 5 then 'INATIVO' -- 'PENDENTE'
ELSE 'INATIVO' -- 'PENDENTE' -- FIXME Nao deveria entrar no else
END,
*/
CASE
WHEN situacaoCota = 1 then 'ATIVO'
WHEN situacaoCota = 2 then 'SUSPENSO'
WHEN situacaoCota = 3 then 'SUSPENSO' -- 'INATIVO' Voltar inativo depois das cargas do sisfil
WHEN situacaoCota = 4 then 'PENDENTE'
WHEN situacaoCota = 5 then 'INATIVO'
ELSE 'SUSPENSO' -- FIXME Nao deveria entrar no else
END,

1,
1,
p.id,
IF(condPrazoPagamento = 'S','CONSIGNADO','A_VISTA'),
'CONVENCIONAL',
1,
1
from CARGA_MDC_COTA c,pessoa p
where (p.cnpj = c.cnpj or p.cpf = c.cpf);

select 'COTA:',count(*) from COTA; -- Log

/*
insert into cota (INICIO_ATIVIDADE,INICIO_TITULARIDADE,NUMERO_COTA,POSSUI_CONTRATO,
SITUACAO_CADASTRO,SUGERE_SUSPENSAO,SUGERE_SUSPENSAO_DISTRIBUIDOR,PESSOA_ID,TIPO_COTA)
select distinct
(select data_operacao from distribuidor),
sysdate(),
codigoCota,
0,
'PENDENTE'
END ,
1,
1,
1,
'CONSIGNADO'
from CARGA_MDC_BANCA 
where codigoCota not in (select numero_cota from cota);

*/
select 'COTA + BANCA:',count(*) from COTA; -- Log

insert into COTA_FORNECEDOR
SELECT c.id, f.id FROM COTA c, FORNECEDOR f
where f.id in (1,2,16) ;

select 'COTA_FORNECEDOR:',count(*) from COTA_FORNECEDOR; -- Log

insert into historico_situacao_cota
(DATA_EDICAO, TIPO_EDICAO, RESTAURADO, PROCESSADO, DATA_FIM_VALIDADE,DATA_INICIO_VALIDADE,
DESCRICAO,MOTIVO,NOVA_SITUACAO,SITUACAO_ANTERIOR,USUARIO_ID,COTA_ID)
select now(),
'ALTERACAO', null, 1, null, DATE(sysdate()), 'CARGA', 'OUTROS', situacao_cadastro, situacao_cadastro, 1, id
from cota a
where id not in (select cota_id from historico_situacao_cota);

select 'historico_situacao_cota:',count(*) from historico_situacao_cota; -- Log

-- Atualiza para cotas ALTERNATIVO as cotas vindas da carga inicial.
update cota set tipo_distribuicao_cota = 'ALTERNATIVO'
where numero_cota in (select numero_cota from CARGA_MDC_COTA_ALTERNATIVAS);

select 'COTAS ALTERNATIVAS:',GROUP_CONCAT(numero_cota SEPARATOR ',') from CARGA_MDC_COTA_ALTERNATIVAS; -- Log

-- FIXME Endereco
insert into endereco (BAIRRO,CEP,CIDADE,CODIGO_BAIRRO,CODIGO_CIDADE_IBGE,CODIGO_UF,
COMPLEMENTO,LOGRADOURO,NUMERO,TIPO_LOGRADOURO,UF,PESSOA_ID)
select 
bairro,
cep,
trim(municipio),
codBairro,
codCidadeIbge,
substr(codCidadeIbge,1,2),-- ufEditor,
complemento,
trim(endereco),
numLogradouro,
tipo_logradouro,
siglaUf,
(select ct.pessoa_id from cota ct where ct.numero_cota = codigoCota limit 1)
from carga_mdc_cota;

select 'ENDERECO (COTA):',count(*) from ENDERECO; -- Log

-- FIXME Endereco Cota
insert into endereco_cota (PRINCIPAL,TIPO_ENDERECO,ENDERECO_ID,COTA_ID)
select * from (
select 
1 AS PRINCIPAL,
'COMERCIAL' AS TIPO_ENDERECO,
(select ed.id from cota ct,endereco ed where ct.numero_cota = codigoCota and ct.pessoa_id = ed.pessoa_id limit 1) as ENDERECO_ID,
(select ct.id from cota ct where ct.numero_cota = codigoCota limit 1) AS COTA_ID
from carga_mdc_cota) a where a.ENDERECO_ID is not null and a.COTA_ID is not null;

select 'ENDERECO_COTA:',count(*) from ENDERECO_COTA; -- Log

select 'TELEFONE:',count(*) from TELEFONE; -- Log

-- Telefone
insert into telefone (DDD,NUMERO,PESSOA_ID)
select distinct cmc.ddd,cmc.telefone,pes.id 
from carga_mdc_cota cmc,pessoa pes
where (cmc.cnpj = pes.cnpj or cmc.cpf = pes.cpf);

select 'TELEFONE (COTA):',count(*) from TELEFONE; -- Log

-- Telefone Cota
insert into telefone_cota (PRINCIPAL,TIPO_TELEFONE,TELEFONE_ID,COTA_ID)
select distinct 
1,
IF(tipoPessoa = 'J','COMERCIAL','RESIDENCIAL'),
tel.id,
ct.id
from carga_mdc_cota cmc,pessoa pes,cota ct,telefone tel
where (cmc.cnpj = pes.cnpj or cmc.cpf = pes.cpf)
and pes.id = ct.pessoa_id
and ct.numero_cota = cmc.codigoCota
and ct.pessoa_id = tel.pessoa_id;

select 'TELEFONE_COTA:',count(*) from TELEFONE_COTA; -- Log

--
-- BANCA - PDV
--

-- ALTER TABLE pdv
-- ADD COLUMN LINHA bigint(20);


insert into pdv (ARRENDATARIO,BALCAO_CENTRAL,PONTO_PRINCIPAL,POSSUI_CARTAO_CREDITO,
POSSUI_COMPUTADOR,POSSUI_LUMINOSO,DENTRO_OUTRO_ESTABELECIMENTO,NOME,PONTO_REFERENCIA,
PORCENTAGEM_FATURAMENTO,POSSUI_SISTEMA_IPV,QTDE_FUNCIONARIOS,STATUS_PDV,COTA_ID,TIPO_PONTO_PDV_ID,LINHA)
select 
0,
0,
0, 
0,
0,
0,
0,
(select nome from pessoa where id = (select pessoa_id from cota where numero_cota = codigoCota limit 1)),
pontoReferencia,
0,
0,
0,
'ATIVO',
(select id from cota where numero_cota = codigoCota limit 1),
1, -- CAST(tipoPontoVenda AS UNSIGNED),
linha
from CARGA_MDC_BANCA where codigoCota  in (select numero_cota from cota) ;

update pdv pdv,
(select min(id) as id from pdv group by cota_id) pr
set pdv.ponto_principal = 1 
where pdv.id =pr.id;

select 'BANCA|PDV:',count(*) from PDV; -- Log



-- Endereco
insert into endereco (BAIRRO,CEP,CIDADE,CODIGO_BAIRRO,CODIGO_CIDADE_IBGE,CODIGO_UF,
COMPLEMENTO,LOGRADOURO,NUMERO,TIPO_LOGRADOURO,UF,PESSOA_ID,LINHA)
select 
bairro,
cep,
trim(nomeMunicipio),
codBairro,
NULL,
11,-- ufEditor,
complemento,
trim(endereco),
numLogradouro, -- FIXME
tipo_logradouro,
siglaUf,
(select ct.pessoa_id from cota ct where ct.numero_cota = codigoCota limit 1),
linha
from carga_mdc_banca;

-- Endereco PDV
insert into endereco_pdv (PRINCIPAL,TIPO_ENDERECO,ENDERECO_ID,PDV_ID)
select distinct
1,
'COMERCIAL',
ed.id,
pdv.id
from carga_mdc_banca ca, cota co , endereco ed,pdv pdv
where ca.codigoCota = co.numero_cota
and co.id = pdv.cota_id
and ca.linha = ed.linha
and ca.linha = pdv.linha
and pdv.linha = ed.linha
and co.pessoa_id = ed.pessoa_id
and ed.id not in(select endereco_id from endereco_cota)
and ca.codigoCota  in (select numero_cota from cota) ;


-- Telefone
insert into telefone (DDD,NUMERO,PESSOA_ID)
select distinct cmc.ddd,cmc.telefone,pes.id 
from carga_mdc_banca cmc,pessoa pes,cota ct
where cmc.codigoCota = ct.numero_cota
and ct.pessoa_id = pes.id;

-- Telefone Banca - PDV
insert into telefone_pdv (PRINCIPAL,TIPO_TELEFONE,TELEFONE_ID,PDV_ID)
select distinct 
PONTO_PRINCIPAL,
-- IF(pes.tipo = 'J','COMERCIAL','RESIDENCIAL'), SOLITACAO MARCIO -- FIXME
'COMERCIAL',
tel.id,
p.id
from carga_mdc_banca cmc,pessoa pes,cota ct,pdv p,telefone tel
where cmc.codigoCota = ct.numero_cota
and ct.pessoa_id = pes.id
and p.cota_id = ct.id
and tel.pessoa_id = p.id;

-- ####################################################################
-- ########################### AJUSTES ################################
-- ####################################################################

-- Normalizacao de endereços (verificar os que faltam) e os tipos logradouro

update pessoa set insc_estadual = null where insc_estadual like '%ISEN%';

update endereco set tipo_logradouro = 'TRAVESSA'  where upper(substr(logradouro,1,8)) in ('TRAVESSA');

update endereco set logradouro = substring( logradouro,8) 
where upper(substring(logradouro,1,8)) in ('TRAVESSA');

update endereco set tipo_logradouro = 'ALAMEDA'  where upper(substr(logradouro,1,7)) in ('ALAMEDA');
update endereco set tipo_logradouro = 'AVENIDA'  where upper(substr(logradouro,1,7)) in ('AVENIDA');
update endereco set tipo_logradouro = 'RODOVIA'  where upper(substr(logradouro,1,7)) in ('RODOVIA');
update endereco set tipo_logradouro = 'VIADUTO'  where upper(substr(logradouro,1,7)) in ('VIADUTO');
update endereco set tipo_logradouro = 'ESTRADA'  where upper(substr(logradouro,1,7)) in ('ESTRADA');

update endereco set logradouro = substring( logradouro,7) 
where upper(substring(logradouro,1,7)) in ('ALAMEDA','AVENIDA','RODOVIA','VIADUTO','ESTRADA');

update endereco set tipo_logradouro = 'ESTRADA'  where upper(substr(logradouro,1,4)) in ('ESTR ','ESTR.');
update endereco set tipo_logradouro = 'TRAVESSA' where upper(substr(logradouro,1,5)) in ('TRAV ','TRAV.');
update endereco set tipo_logradouro = 'LARGO'    where upper(substr(logradouro,1,5)) in ('LARGO');
update endereco set tipo_logradouro = 'PRAÇA'    where upper(substr(logradouro,1,5)) in ('PRACA','PRAÇA');

update endereco set logradouro = substring( logradouro,5) 
where upper(substring(logradouro,1,5)) in ('LARGO','PRACA','PRAÇA','TRAV.','TRAV ','ESTR.','ESTR ');

update endereco set tipo_logradouro = 'ESTRADA'  where upper(substr(logradouro,1,4)) in ('EST ','EST.');

update endereco set logradouro = substring( logradouro,4) 
where upper(substring(logradouro,1,4)) in ('EST ','EST.');


-- 3 caracteres
update endereco set tipo_logradouro = 'RUA'
where upper(substr(logradouro,1,3)) in ('R. ','R..','RUA');

update endereco set tipo_logradouro = 'AVENIDA'
where upper(substr(logradouro,1,3)) in ('AV.','A. ','A..','AV ');

update endereco set tipo_logradouro = 'PRAÇA'
where upper(substr(logradouro,1,3)) in ('PC.','PCA','P..','PÇA','PC ','PÇ ');

update endereco set tipo_logradouro = 'ESTRADA'
where upper(substr(logradouro,1,3)) in ('ES.','ES ');

update endereco set tipo_logradouro = 'LARGO'
where upper(substr(logradouro,1,3)) in ('LR.','LR ');

update endereco set tipo_logradouro = 'RODOVIA'
where upper(substr(logradouro,1,3)) in ('RO.','RO ','ROD');

update endereco set tipo_logradouro = 'ALAMEDA'
where upper(substr(logradouro,1,3)) in ('AL.','AL ');

update endereco set tipo_logradouro = 'TRAVESSA'
where upper(substr(logradouro,1,3)) in ('TR.','TV.','TR ','TV ');

update endereco set logradouro = substring( logradouro,3) 
where upper(substring( logradouro,1,3)) in (
'R. ','R..','RUA',
'AV.','A. ','A..','AV ',
'PC.','PCA','P..','PÇA','PC ','PÇ ',
'ES.','ES ',
'LR.','LR ',
'RO.','RO ','ROD'
'AL.','AL ',
'TR.','TV.','TR ','TV ');

update endereco set tipo_logradouro = 'AVENIDA'  where upper(substr(logradouro,1,2)) in ('A.','A:','A ');
update endereco set tipo_logradouro = 'RUA' 	 where upper(substr(logradouro,1,2)) in ('R.','R:','R ','E ');
update endereco set tipo_logradouro = 'VIADUTO'  where upper(substr(logradouro,1,2)) in ('VD');
update endereco set tipo_logradouro = 'VILA'  where upper(substr(logradouro,1,2)) in ('VL','V ','V.');

update endereco set logradouro = substring(logradouro,2) 
where upper(substring( logradouro,1,2)) in (
'A.','A:','R.','R:','R ','VD','A ','VL','V ','V.','E '
);


update endereco set logradouro = substring( logradouro,1) 
where substring(logradouro,1,1) in (' ','.',':','-','_',',');

update endereco set logradouro = substring( logradouro,1) 
where substring(logradouro,1,1) in (' ','.',':','-','_',',');

update endereco set cidade = 'SÃO BERNARDO DO CAMPO'
where cidade = 'SAO BERNARDO DO CAMP';

update endereco set cidade = 'FERRAZ DE VASCONCELOS'
where cidade = 'FERRAZ DE VASCONCELO';

update endereco set cidade = 'VARGEM GRANDE PAULISTA'
where cidade = 'VARGEM GRANDE PAULIS';




-- FIXME Verificar os nulos


-- FIXME Remover isso assim que possivel
update telefone t
inner join telefone_cota tc on tc.TELEFONE_ID = t.id
inner join cota c on c.id = tc.COTA_ID
set t.ddd = case when trim(t.ddd) = '' then null else trim(t.ddd) end
, t.numero = case when trim(t.numero) = '' then null else trim(t.numero) end
, t.ramal = case when trim(t.ramal) = '' then null else trim(t.ramal) end
where t.ddd like '% %'
;
update endereco e
inner join endereco_cota ec on ec.ENDERECO_ID = e.id
inner join cota c on c.id = ec.COTA_ID
set e.TIPO_LOGRADOURO = substring(e.LOGRADOURO, 1, INSTR(e.LOGRADOURO, ' '))
, e.LOGRADOURO = substring(e.LOGRADOURO, INSTR(e.LOGRADOURO, ' ')+1)
where TIPO_LOGRADOURO is null
;
update endereco e
set e.TIPO_LOGRADOURO = 'RUA'
where e.TIPO_LOGRADOURO in ('PRUA', 'TRUA')
;

-- ###########################################
-- ########   AJUSTES PARA PRODUTOS   ########
-- ###########################################

delete from produto_fornecedor where produto_id not in (select distinct produto_id from produto_edicao);
delete from produto where id not in (select distinct produto_id from produto_edicao);
update produto set pacote_padrao = 10 where pacote_padrao =0;

-- ###########################################
-- ########    LIMPEZA DE MEMORIA     ########
-- ###########################################

-- DROP TABLE IF EXISTS CARGA_PRODIN_DSF;
-- DROP TABLE IF EXISTS CARGA_PRODIN_EDI;
-- DROP TABLE IF EXISTS CARGA_PRODIN_PUB;
-- DROP TABLE IF EXISTS CARGA_PRODIN_PRD;
-- DROP TABLE IF EXISTS CARGA_PRODIN_LAN;
-- DROP TABLE IF EXISTS CARGA_PRODIN_REC;
-- DROP TABLE IF EXISTS CARGA_PRODIN_CHC;
-- DROP TABLE IF EXISTS CARGA_PRODIN_CDB;

-- DROP TABLE IF EXISTS CARGA_MDC_PRODUTO;
-- DROP TABLE IF EXISTS CARGA_MDC_COTA;
-- DROP TABLE IF EXISTS CARGA_MDC_COTA_TEL;
-- DROP TABLE IF EXISTS CARGA_MDC_BANCA;
-- DROP TABLE IF EXISTS CARGA_MDC_BANCA_TEL;
DROP TABLE IF EXISTS CARGA_MDC_MATRIZ;
DROP TABLE IF EXISTS CARGA_MDC_PRECO;

-- ###########################################


update endereco 
set logradouro = substring(logradouro,2)
where substring(logradouro,1,1) = '.';

select 'FIM CARGA_PRODIN_MDC',sysdate(); -- Log

 




