--
-- VARIAVEIS DO SISTEMA
--

-- SET max_heap_table_size = 18446744073709551615;
-- SET tmp_table_size = 18446744073709551615;

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
-- ENGINE=MEMORY
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
fornecedorId = 1; -- FIXME trocar para 2

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
-- ENGINE=MEMORY
;

LOAD DATA INFILE '/opt/rollout/load_files/00000001.edi' INTO TABLE CARGA_PRODIN_EDI CHARACTER SET UTF8
(@linha) SET 
codDistribuidor=SUBSTR(@linha,1,7),
tipoOperacao=SUBSTR(@linha,26,1),
codigoEditor=SUBSTR(@linha,28,7),	
nomeEditor=SUBSTR(@linha,35,35),	
tipoLogradouroEditor=SUBSTR(@linha,73,5),	
logradouroEditor=SUBSTR(@linha,78,30),
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
cepEntrega=SUBSTR(@linha,265,8),	
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
inscricaoEstadual=SUBSTR(@linha,420,20),
bairroEditor=SUBSTR(@linha,440,5),
bairroEntrega=SUBSTR(@linha,445,5);

LOAD DATA INFILE '/opt/rollout/load_files/00000002.edi' INTO TABLE CARGA_PRODIN_EDI CHARACTER SET UTF8
(@linha) SET 
codDistribuidor=SUBSTR(@linha,1,7),
tipoOperacao=SUBSTR(@linha,26,1),
codigoEditor=SUBSTR(@linha,28,7),	
nomeEditor=SUBSTR(@linha,35,35),	
tipoLogradouroEditor=SUBSTR(@linha,73,5),	
logradouroEditor=SUBSTR(@linha,78,30),
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
cepEntrega=SUBSTR(@linha,265,8),	
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
inscricaoEstadual=SUBSTR(@linha,420,20),
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
-- ENGINE=MEMORY
;

LOAD DATA INFILE '/opt/rollout/load_files/00000001.pub' INTO TABLE CARGA_PRODIN_PUB CHARACTER SET UTF8
(@linha) SET 
codigoDistribuidor=SUBSTR(@linha,1,7),
dataGeracaoArquivo=SUBSTR(@linha,8,8),
codContexto=SUBSTR(@linha,26,2),
codigoFornecedor=SUBSTR(@linha,28,7),
codigoProduto=SUBSTR(@linha,35,8), 
nome=SUBSTR(@linha,43,30),
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
nome=SUBSTR(@linha,43,30),
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
fornecedorId=1; -- FIXME somente para o Rio, trocar para 2

select 'CARGA_PRODIN_PUB:',count(*) from CARGA_PRODIN_PUB; -- Log 

--  CARGA_PRODIN_PRD
DROP TABLE IF EXISTS CARGA_PRODIN_PRD;

/* -- ********* Se MATRIZ

create table CARGA_PRODIN_PRD (
codDistrib VARCHAR(7),
dataGeracaoArquivo VARCHAR(8),
contextoProd VARCHAR(1),
codForncProd VARCHAR(7),
codigoProduto VARCHAR(8),
codigoEdicao VARCHAR(4),
nome VARCHAR(30),
codigoBarras VARCHAR(18),
codigoBarrasCorporativo VARCHAR(18),
peso VARCHAR(5),
tipoProd VARCHAR(3),
peb VARCHAR(3),
largura VARCHAR(5),
comprimento VARCHAR(5),
espessura VARCHAR(5),
situacaoTributaria VARCHAR(1),
tributacaoFiscal VARCHAR(1),
pacotePadrao VARCHAR(8),
tipoMaterialPromocional VARCHAR(3),
tipoMaterialDivulgacao VARCHAR(3),
tipoMaterialTroca VARCHAR(3),
valorValeDesconto VARCHAR(10),
valorMaterialTroca VARCHAR(10),
formaComercializacao VARCHAR(3),
contemBrinde VARCHAR(1),
codNBM VARCHAR(10),
descBrinde VARCHAR(300),
condVendeSeparado VARCHAR(1),
status VARCHAR(1),
dataDesativacao VARCHAR(8),
chamadaCapa VARCHAR(30),
edicao VARCHAR(4),
tipoRecolhimento VARCHAR(1),
classeSocial VARCHAR(2),
periodicidade VARCHAR(3),
formaFiscal VARCHAR(2),
sexo VARCHAR(1),
idade VARCHAR(1),
lancamento VARCHAR(1),
temaPrincipal VARCHAR(2),
temaSecundario VARCHAR(2),
categoria VARCHAR(3),
contextoProdReferencia VARCHAR(1),
codFornecProdReferencia VARCHAR(1),
codProdReferencia VARCHAR(12),
tipoDesconto VARCHAR(2),
contextoEditor VARCHAR(1),
codigoEditor VARCHAR(7),
codContexto VARCHAR(1),
codFornecPublicacao VARCHAR(7),
codColecao VARCHAR(3),
formaInclusao VARCHAR(1),
codPublicacao VARCHAR(8),
campoObscuro VARCHAR(6),
nomeComercial VARCHAR(45),
classificacao VARCHAR(30),
segmento VARCHAR(2),
fornecedorId VARCHAR(1),
KEY codigoProdutoEdicao (codigoProduto,codigoEdicao)) 
-- ENGINE=MEMORY
;

LOAD DATA INFILE '/opt/rollout/load_files/00000001.prd' INTO TABLE CARGA_PRODIN_PRD CHARACTER SET UTF8
(@linha) SET 
codDistrib=SUBSTR(@linha,1,7),
dataGeracaoArquivo=SUBSTR(@linha,8,8),
contextoProd=SUBSTR(@linha,26,1),
codForncProd=SUBSTR(@linha,27,7),
codigoProduto=SUBSTR(@linha,34,8),
codigoEdicao=SUBSTR(@linha,42,4),
nome=SUBSTR(@linha,46,30),
codigoBarras=SUBSTR(@linha,76,18),
codigoBarrasCorporativo=0,
peso=SUBSTR(@linha,94,5),
tipoProd=SUBSTR(@linha,99,3),
peb=SUBSTR(@linha,102,3),
largura=SUBSTR(@linha,105,5),
comprimento=SUBSTR(@linha,110,5),
espessura=SUBSTR(@linha,115,5),
situacaoTributaria=SUBSTR(@linha,120,1),
tributacaoFiscal=SUBSTR(@linha,121,1),
pacotePadrao=SUBSTR(@linha,122,8),
tipoMaterialPromocional=SUBSTR(@linha,130,3),
tipoMaterialDivulgacao=SUBSTR(@linha,133,3),
tipoMaterialTroca=SUBSTR(@linha,136,3),
valorValeDesconto=SUBSTR(@linha,139,10),
valorMaterialTroca=SUBSTR(@linha,149,10),
formaComercializacao=SUBSTR(@linha,159,3),
contemBrinde=SUBSTR(@linha,186,1),
codNBM=SUBSTR(@linha,187,10),
descBrinde=SUBSTR(@linha,199,300),
condVendeSeparado=SUBSTR(@linha,499,1),
status=SUBSTR(@linha,501,1),
dataDesativacao=SUBSTR(@linha,502,8),
chamadaCapa=SUBSTR(@linha,513,30),
edicao=SUBSTR(@linha,543,4),
tipoRecolhimento=SUBSTR(@linha,552,1),
classeSocial=SUBSTR(@linha,553,2),
periodicidade=SUBSTR(@linha,555,3),
formaFiscal=SUBSTR(@linha,558,2),
sexo=SUBSTR(@linha,560,1),
idade=SUBSTR(@linha,561,1),
lancamento=SUBSTR(@linha,562,1),
temaPrincipal=SUBSTR(@linha,563,2),
temaSecundario=SUBSTR(@linha,565,2),
categoria=SUBSTR(@linha,567,3),
contextoProdReferencia=SUBSTR(@linha,570,1),
codFornecProdReferencia=SUBSTR(@linha,571,1),
codProdReferencia=SUBSTR(@linha,578,12),
tipoDesconto=SUBSTR(@linha,590,2),
contextoEditor=SUBSTR(@linha,592,1),
codigoEditor=SUBSTR(@linha,593,7),
codContexto=SUBSTR(@linha,600,1),
codFornecPublicacao=SUBSTR(@linha,601,7),
codColecao=SUBSTR(@linha,608,3),
formaInclusao=SUBSTR(@linha,611,1),
codPublicacao=SUBSTR(@linha,612,8),
campoObscuro=SUBSTR(@linha,620,6),
nomeComercial=SUBSTR(@linha,626,45),
classificacao=SUBSTR(@linha,671,30),
segmento=SUBSTR(@linha,581,2),
fornecedorId=1;

LOAD DATA INFILE '/opt/rollout/load_files/00000002.prd' INTO TABLE CARGA_PRODIN_PRD CHARACTER SET UTF8
(@linha) SET 
codDistrib=SUBSTR(@linha,1,7),
dataGeracaoArquivo=SUBSTR(@linha,8,8),
contextoProd=SUBSTR(@linha,26,1),
codForncProd=SUBSTR(@linha,27,7),
codigoProduto=SUBSTR(@linha,34,8),
codigoEdicao=SUBSTR(@linha,42,4),
nome=SUBSTR(@linha,46,30),
codigoBarras=SUBSTR(@linha,76,18),
codigoBarrasCorporativo=0,
peso=SUBSTR(@linha,94,5),
tipoProd=SUBSTR(@linha,99,3),
peb=SUBSTR(@linha,102,3),
largura=SUBSTR(@linha,105,5),
comprimento=SUBSTR(@linha,110,5),
espessura=SUBSTR(@linha,115,5),
situacaoTributaria=SUBSTR(@linha,120,1),
tributacaoFiscal=SUBSTR(@linha,121,1),
pacotePadrao=SUBSTR(@linha,122,8),
tipoMaterialPromocional=SUBSTR(@linha,130,3),
tipoMaterialDivulgacao=SUBSTR(@linha,133,3),
tipoMaterialTroca=SUBSTR(@linha,136,3),
valorValeDesconto=SUBSTR(@linha,139,10),
valorMaterialTroca=SUBSTR(@linha,149,10),
formaComercializacao=SUBSTR(@linha,159,3),
contemBrinde=SUBSTR(@linha,186,1),
codNBM=SUBSTR(@linha,187,10),
descBrinde=SUBSTR(@linha,199,300),
condVendeSeparado=SUBSTR(@linha,499,1),
status=SUBSTR(@linha,501,1),
dataDesativacao=SUBSTR(@linha,502,8),
chamadaCapa=SUBSTR(@linha,513,30),
edicao=SUBSTR(@linha,543,4),
tipoRecolhimento=SUBSTR(@linha,552,1),
classeSocial=SUBSTR(@linha,553,2),
periodicidade=SUBSTR(@linha,555,3),
formaFiscal=SUBSTR(@linha,558,2),
sexo=SUBSTR(@linha,560,1),
idade=SUBSTR(@linha,561,1),
lancamento=SUBSTR(@linha,562,1),
temaPrincipal=SUBSTR(@linha,563,2),
temaSecundario=SUBSTR(@linha,565,2),
categoria=SUBSTR(@linha,567,3),
contextoProdReferencia=SUBSTR(@linha,570,1),
codFornecProdReferencia=SUBSTR(@linha,571,1),
codProdReferencia=SUBSTR(@linha,578,12),
tipoDesconto=SUBSTR(@linha,590,2),
contextoEditor=SUBSTR(@linha,592,1),
codigoEditor=SUBSTR(@linha,593,7),
codContexto=SUBSTR(@linha,600,1),
codFornecPublicacao=SUBSTR(@linha,601,7),
codColecao=SUBSTR(@linha,608,3),
formaInclusao=SUBSTR(@linha,611,1),
codPublicacao=SUBSTR(@linha,612,8),
campoObscuro=SUBSTR(@linha,620,6),
nomeComercial=SUBSTR(@linha,626,45),
classificacao=SUBSTR(@linha,671,30),
segmento=SUBSTR(@linha,581,2),
fornecedorId=2;
*/

-- ********* FILIAL
create table CARGA_PRODIN_PRD (
codDistrib VARCHAR(7),
dataGeracaoArquivo VARCHAR(8),
contextoProd VARCHAR(1),
codForncProd VARCHAR(7),
codigoProduto VARCHAR(8),
codigoEdicao VARCHAR(4),
nome VARCHAR(30),
codigoBarras VARCHAR(18),
codigoBarrasCorporativo VARCHAR(18),
peso VARCHAR(5),
tipoProd VARCHAR(3),
peb VARCHAR(3),
largura VARCHAR(5),
comprimento VARCHAR(5),
espessura VARCHAR(5),
situacaoTributaria VARCHAR(1),
tributacaoFiscal VARCHAR(1),
pacotePadrao VARCHAR(8),
tipoMaterialPromocional VARCHAR(3),
tipoMaterialDivulgacao VARCHAR(3),
tipoMaterialTroca VARCHAR(3),
valorValeDesconto VARCHAR(10),
valorMaterialTroca VARCHAR(10),
formaComercializacao VARCHAR(3),
contemBrinde VARCHAR(1),
codNBM VARCHAR(10),
descBrinde VARCHAR(300),
condVendeSeparado VARCHAR(1),
status VARCHAR(1),
dataDesativacao VARCHAR(8),
chamadaCapa VARCHAR(30),
edicao VARCHAR(4),
tipoRecolhimento VARCHAR(1),
classeSocial VARCHAR(2),
periodicidade VARCHAR(3),
formaFiscal VARCHAR(2),
sexo VARCHAR(1),
idade VARCHAR(1),
lancamento VARCHAR(1),
temaPrincipal VARCHAR(2),
temaSecundario VARCHAR(2),
categoria VARCHAR(3),
contextoProdReferencia VARCHAR(1),
codFornecProdReferencia VARCHAR(1),
codProdReferencia VARCHAR(12),
tipoDesconto VARCHAR(2),
contextoEditor VARCHAR(1),
codigoEditor VARCHAR(7),
codContexto VARCHAR(1),
codFornecPublicacao VARCHAR(7),
codColecao VARCHAR(3),
formaInclusao VARCHAR(1),
codPublicacao VARCHAR(8),
campoObscuro VARCHAR(6),
nomeComercial VARCHAR(45),
classificacao VARCHAR(30),
segmento VARCHAR(2),
fornecedorId VARCHAR(1),
KEY codigoProdutoEdicao (codigoProduto,codigoEdicao)) 
-- ENGINE=MEMORY
;

LOAD DATA INFILE '/opt/rollout/load_files/00000001.prd' INTO TABLE CARGA_PRODIN_PRD CHARACTER SET UTF8
(@linha) SET 
codDistrib=SUBSTR(@linha,1,7),
dataGeracaoArquivo=SUBSTR(@linha,8,8),
contextoProd=SUBSTR(@linha,26,1),
codForncProd=SUBSTR(@linha,27,7),
codigoProduto=SUBSTR(@linha,34,8),
codigoEdicao=SUBSTR(@linha,42,4),
nome=SUBSTR(@linha,46,30),
codigoBarras=SUBSTR(@linha,76,18),
codigoBarrasCorporativo=SUBSTR(@linha,94,18),
peso=SUBSTR(@linha,112,5),
tipoProd=SUBSTR(@linha,117,3),
peb=SUBSTR(@linha,120,3),
largura=SUBSTR(@linha,123,5),
comprimento=SUBSTR(@linha,128,5),
espessura=SUBSTR(@linha,133,5),
situacaoTributaria=SUBSTR(@linha,138,1),
tributacaoFiscal=SUBSTR(@linha,139,1),
pacotePadrao=SUBSTR(@linha,140,8),
tipoMaterialPromocional=SUBSTR(@linha,148,3),
tipoMaterialDivulgacao=SUBSTR(@linha,151,3),
tipoMaterialTroca=SUBSTR(@linha,154,3),
valorValeDesconto=SUBSTR(@linha,157,10),
valorMaterialTroca=SUBSTR(@linha,167,10),
formaComercializacao=SUBSTR(@linha,177,3),
contemBrinde=SUBSTR(@linha,204,1),
codNBM=SUBSTR(@linha,205,10),
descBrinde=SUBSTR(@linha,217,300),
condVendeSeparado=SUBSTR(@linha,517,1),
status=SUBSTR(@linha,519,1),
dataDesativacao=SUBSTR(@linha,520,8),
chamadaCapa=SUBSTR(@linha,531,30),
edicao=SUBSTR(@linha,561,4),
tipoRecolhimento=SUBSTR(@linha,570,1),
classeSocial=SUBSTR(@linha,570,2),
periodicidade=SUBSTR(@linha,573,3),
formaFiscal=SUBSTR(@linha,576,2),
sexo=SUBSTR(@linha,578,1),
idade=SUBSTR(@linha,579,1),
lancamento=SUBSTR(@linha,580,1),
temaPrincipal=SUBSTR(@linha,581,2),
temaSecundario=SUBSTR(@linha,583,2),
categoria=SUBSTR(@linha,585,3),
contextoProdReferencia=SUBSTR(@linha,588,1),
codFornecProdReferencia=SUBSTR(@linha,589,1),
codProdReferencia=SUBSTR(@linha,596,12),
tipoDesconto=SUBSTR(@linha,608,2),
contextoEditor=SUBSTR(@linha,610,1),
codigoEditor=SUBSTR(@linha,611,7),
codContexto=SUBSTR(@linha,618,1),
codFornecPublicacao=SUBSTR(@linha,619,7),
codColecao=SUBSTR(@linha,626,3),
formaInclusao=SUBSTR(@linha,629,1),
codPublicacao=SUBSTR(@linha,630,8),
campoObscuro=SUBSTR(@linha,638,6),
nomeComercial=SUBSTR(@linha,644,45),
classificacao=SUBSTR(@linha,689,30),
segmento=SUBSTR(@linha,581,2),
fornecedorId=1;

LOAD DATA INFILE '/opt/rollout/load_files/00000002.prd' INTO TABLE CARGA_PRODIN_PRD CHARACTER SET UTF8
(@linha) SET 
codDistrib=SUBSTR(@linha,1,7),
dataGeracaoArquivo=SUBSTR(@linha,8,8),
contextoProd=SUBSTR(@linha,26,1),
codForncProd=SUBSTR(@linha,27,7),
codigoProduto=SUBSTR(@linha,34,8),
codigoEdicao=SUBSTR(@linha,42,4),
nome=SUBSTR(@linha,46,30),
codigoBarras=SUBSTR(@linha,76,18),
codigoBarrasCorporativo=SUBSTR(@linha,94,18),
peso=SUBSTR(@linha,112,5),
tipoProd=SUBSTR(@linha,117,3),
peb=SUBSTR(@linha,120,3),
largura=SUBSTR(@linha,123,5),
comprimento=SUBSTR(@linha,128,5),
espessura=SUBSTR(@linha,133,5),
situacaoTributaria=SUBSTR(@linha,138,1),
tributacaoFiscal=SUBSTR(@linha,139,1),
pacotePadrao=SUBSTR(@linha,140,8),
tipoMaterialPromocional=SUBSTR(@linha,148,3),
tipoMaterialDivulgacao=SUBSTR(@linha,151,3),
tipoMaterialTroca=SUBSTR(@linha,154,3),
valorValeDesconto=SUBSTR(@linha,157,10),
valorMaterialTroca=SUBSTR(@linha,167,10),
formaComercializacao=SUBSTR(@linha,177,3),
contemBrinde=SUBSTR(@linha,204,1),
codNBM=SUBSTR(@linha,205,10),
descBrinde=SUBSTR(@linha,217,300),
condVendeSeparado=SUBSTR(@linha,517,1),
status=SUBSTR(@linha,519,1),
dataDesativacao=SUBSTR(@linha,520,8),
chamadaCapa=SUBSTR(@linha,531,30),
edicao=SUBSTR(@linha,561,4),
tipoRecolhimento=SUBSTR(@linha,570,1),
classeSocial=SUBSTR(@linha,570,2),
periodicidade=SUBSTR(@linha,573,3),
formaFiscal=SUBSTR(@linha,576,2),
sexo=SUBSTR(@linha,578,1),
idade=SUBSTR(@linha,579,1),
lancamento=SUBSTR(@linha,580,1),
temaPrincipal=SUBSTR(@linha,581,2),
temaSecundario=SUBSTR(@linha,583,2),
categoria=SUBSTR(@linha,585,3),
contextoProdReferencia=SUBSTR(@linha,588,1),
codFornecProdReferencia=SUBSTR(@linha,589,1),
codProdReferencia=SUBSTR(@linha,596,12),
tipoDesconto=SUBSTR(@linha,608,2),
contextoEditor=SUBSTR(@linha,610,1),
codigoEditor=SUBSTR(@linha,611,7),
codContexto=SUBSTR(@linha,618,1),
codFornecPublicacao=SUBSTR(@linha,619,7),
codColecao=SUBSTR(@linha,626,3),
formaInclusao=SUBSTR(@linha,629,1),
codPublicacao=SUBSTR(@linha,630,8),
campoObscuro=SUBSTR(@linha,638,6),
nomeComercial=SUBSTR(@linha,644,45),
classificacao=SUBSTR(@linha,689,30),
segmento=SUBSTR(@linha,581,2),
fornecedorId=1; -- FIXME -trocar para 2

select 'CARGA_PRODIN_PRD:',count(*) from CARGA_PRODIN_PRD; -- Log

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
-- ENGINE=MEMORY
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
-- ENGINE=MEMORY
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
-- ENGINE=MEMORY
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
-- ENGINE=MEMORY
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
codigoProduto VARCHAR(8),
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
-- ENGINE=MEMORY
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
codigoCota VARCHAR(4),
nomeJornaleiro VARCHAR(30),
qtdeCotas VARCHAR(4),
endereco VARCHAR(40),
codBairro VARCHAR(5),
municipio VARCHAR(20),
siglaUF VARCHAR(2),
cep VARCHAR(8),
ddd VARCHAR(4),
telefone VARCHAR(7),
situacaoCota VARCHAR(1),
condPrazoPagamento VARCHAR(1),
codBox VARCHAR(3),
codTipoBox VARCHAR(1),
repartePDV VARCHAR(1),
codCapataz VARCHAR(5),
cpf VARCHAR(11),
cnpj VARCHAR(14),
tipoPessoa VARCHAR(1),
numLogradouro VARCHAR(6),
codCidadeIbge VARCHAR(7),
inscrEstadual VARCHAR(20),
inscrMunicipal VARCHAR(15),
KEY codigoCota (codigoCota)) 
-- ENGINE=MEMORY
;

LOAD DATA INFILE '/opt/rollout/load_files/COTA.NEW' INTO TABLE CARGA_MDC_COTA CHARACTER SET UTF8
(@linha) SET 
codigoCota=SUBSTR(@linha,1,4),
nomeJornaleiro=SUBSTR(@linha,5,30),
qtdeCotas=SUBSTR(@linha,35,4),
endereco=SUBSTR(@linha,39,40),
codBairro=SUBSTR(@linha,79,5),
municipio=SUBSTR(@linha,84,20),
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
cpf=SUBSTR(@linha,140,11),
cnpj=SUBSTR(@linha,137,14),
tipoPessoa=SUBSTR(@linha,151,1),
numLogradouro=SUBSTR(@linha,152,6),
codCidadeIbge=SUBSTR(@linha,158,7),
inscrEstadual=SUBSTR(@linha,165,20),
inscrMunicipal=SUBSTR(@linha,185,15);

select 'CARGA_MDC_COTA:',count(*) from CARGA_MDC_COTA; -- Log

--  CARGA_MDC_BANCA
DROP TABLE IF EXISTS CARGA_MDC_BANCA;

create table CARGA_MDC_BANCA (
codigoCota VARCHAR(4),
endereco VARCHAR(40),
codBairro VARCHAR(5),
nomeMunicipio VARCHAR(20),
siglaUF VARCHAR(2),
cep VARCHAR(8),
ddd VARCHAR(4),
telefone VARCHAR(7),
tipoPontoVenda VARCHAR(2),
pontoReferencia VARCHAR(40),
KEY codigoCota (codigoCota)) 
-- ENGINE=MEMORY
;

LOAD DATA INFILE '/opt/rollout/load_files/BANCA.NEW' INTO TABLE CARGA_MDC_BANCA CHARACTER SET UTF8
(@linha) SET 
codigoCota=SUBSTR(@linha,1,4),
endereco=SUBSTR(@linha,5,40),
codBairro=SUBSTR(@linha,45,5),
nomeMunicipio=SUBSTR(@linha,50,20),
siglaUF=SUBSTR(@linha,70,2),
cep=SUBSTR(@linha,72,8),
ddd=SUBSTR(@linha,80,4),
telefone=SUBSTR(@linha,84,7),
tipoPontoVenda=SUBSTR(@linha,91,2),
pontoReferencia=SUBSTR(@linha,93,40);

select 'CARGA_MDC_BANCA:',count(*) from CARGA_MDC_BANCA; -- Log

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
-- ENGINE=MEMORY
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
-- ENGINE=MEMORY
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
str_to_date(dataGeracaoArquivo,'%Y%m%d'),
NULL,
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
update CARGA_PRODIN_EDI 
set cnpj = codigoEditor
where trim(LEADING '0' FROM cnpj) ='' ;

-- pessoa FIXME * corrigir abaixo ASSIM QUE CORRIGIREM CNPJ/INSCRICAO ESTADUAL
insert into pessoa (TIPO,CNPJ,INSC_ESTADUAL,RAZAO_SOCIAL,SOCIO_PRINCIPAL)
select max('J') ,cnpj,max(inscricaoEstadual),trim(LEADING ' ' from nomeEditor),0
from CARGA_PRODIN_EDI 
where cnpj not in ('28322873000130','03555225000100','61438248000123') -- FIXME
group by cnpj;

select 'EDITOR - PESSOA:',count(*) from PESSOA; -- Log

-- editor
insert into editor (ATIVO,CODIGO,NOME_CONTATO,ORIGEM_INTERFACE,JURIDICA_ID)
select IF(status ='S',1,0),CAST(codigoEditor AS UNSIGNED),
trim(LEADING ' ' FROM nomeContato),null,p.id
from CARGA_PRODIN_EDI c,pessoa p
where p.cnpj = c.cnpj
group by p.cnpj;

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
21, -- FIXME corrigir ufEditor,
complementoEditor,
logradouroEditor,
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
21,-- FIXME corrigir ufEntrega,
complementoEntrega,
logradouroEntrega,
numeroEntrega,
tipoLogradouroEntrega,
ufEntrega,
(select edi.juridica_id from editor edi where edi.codigo = codigoEditor limit 1) -- FIXME Remover o limit 1 assim que corrigir
from CARGA_PRODIN_EDI
group by cnpj;

select 'EDITOR - ENDERECO (EDITOR+LOCAL_ENTREGA):',count(*) from ENDERECO; -- Log

-- endereco_editor (COMERCIAL)
insert into endereco_editor (PRINCIPAL,TIPO_ENDERECO,ENDERECO_ID,EDITOR_ID)
select 
0,
'COMERCIAL',
(select ed.id from editor edi,endereco ed where edi.codigo = codigoEditor and edi.juridica_id = ed.pessoa_id and ed.CODIGO_CIDADE_IBGE = 1 limit 1), -- FIXME Remover o limit 1 assim que corrigir
(select edi.id from editor edi where edi.codigo = codigoEditor limit 1) -- FIXME Remover o limit 1 assim que corrigir
from CARGA_PRODIN_EDI
group by cnpj;

select 'EDITOR - ENDERECO_EDITOR (COMERCIAL):',count(*) from ENDERECO_EDITOR; -- Log

-- endereco_editor (LOCAL_ENTREGA)
insert into endereco_editor (PRINCIPAL,TIPO_ENDERECO,ENDERECO_ID,EDITOR_ID)
select 
0,
'LOCAL_ENTREGA',
(select ed.id from editor edi,endereco ed where edi.codigo = codigoEditor and edi.juridica_id = ed.pessoa_id and ed.CODIGO_CIDADE_IBGE = 2 limit 1),
(select edi.id from editor edi where edi.codigo = codigoEditor limit 1) -- FIXME Remover o limit 1 assim que corrigir
from CARGA_PRODIN_EDI
group by cnpj;

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
nome,
nome,
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
IF(lancamentoImediato ='N',0,1),
NULL, 
subGrupoEditorial 
from CARGA_PRODIN_PUB group by codigoProduto;

select 'PRODUTO - (PUB)',count(*) from PRODUTO; -- Log

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
IF(nome = '',nomeComercial,nome),
IF(nomeComercial = '',nome,nomeComercial),
NULL,
'INTERFACE',
CAST(pacotePadrao AS UNSIGNED),
1, -- CAST(peb AS UNSIGNED),
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
NULL, -- CAST(lancamentoImediato AS UNSIGNED),
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
IF(dataGeracaoArquivo = '00000000',sysdate(),str_to_date(dataGeracaoArquivo,'%Y%m%d')),
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
IF(nome = '',nomeComercial,nome),
IF(nomeComercial = '',nome,nomeComercial),
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
IF(segmento= '00',9,CAST(segmento AS UNSIGNED)), -- (select id from tipo_segmento_produto tz where tz.descricao like segmento limit 1), 
NULL,
substring(codigoProduto,1,6),-- codigoICD, 
NULL, -- grupoEditorial, 
NULL, -- CAST(lancamentoImediato AS UNSIGNED),
NULL, 
NULL -- subGrupoEditorial 
from CARGA_PRODIN_PRD
where codigoProduto not in (select codigo from produto)
group by codigoProduto;

select 'PRODUTO - (PUB+PRODUTO+PRD)',count(*) from PRODUTO; -- Log

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
1, -- CAST(peb AS UNSIGNED),
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
NULL, -- CAST(lancamentoImediato AS UNSIGNED),
NULL, 
NULL -- subGrupoEditorial 
from CARGA_PRODIN_LAN
where codigoProduto not in (select codigo from produto)
group by codigoProduto;

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
NULL, -- CAST(tributacaoFiscal AS UNSIGNED),
slogan,
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
NULL, -- CAST(lancamentoImediato AS UNSIGNED),
NULL, 
NULL 
from CARGA_MDC_MATRIZ
where codigoProduto not in (select codigo from produto)
group by codigoProduto;

select 'PRODUTO - (PUB+PRODUTO+PRD+LAN+MATRIZ)',count(*) from PRODUTO; -- Log

-- **** UPDATES ****

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
IF(cpp.segmento in (00,24,30,34,36,42,52,53,56,61,66,67,71,72,73,74,77,78,91,92,93,97,98),9,CAST(cpp.segmento AS UNSIGNED)) -- ERRO FIXME * Remover essa linha e descomentar a de baixo assim que arrumarem os segmentos
-- CAST(cpp.segmento AS UNSIGNED)
where p.codigo = cpp.codigoProduto;

select 'PRODUTO - SEGMENTO(OUTROS)',count(*) from PRODUTO where tipo_segmento_produto_id =9; -- Log

-- DESCONTO FIXME * Ajustar com o valor de desconto correto
update produto
set desconto = 30.00
where origem = 'MANUAL';

select 'PRODUTO - DESCONTO:',count(*) from PRODUTO; -- Log

--
-- PRODUTO_FORNECEDOR FIXME * Reformular a query abaixo
--
insert into produto_fornecedor
select id,1 from produto; -- FIXME * reconstruir essa query com fornecedor correto

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
PESO,POSSUI_BRINDE,max(PRECO_CUSTO),max(PRECO_PREVISTO),max(PRECO_VENDA),REPARTE_DISTRIBUIDO,DESCONTO_LOGISTICA_ID, -- 7
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
IF(comprimento = '00000',null,CAST(comprimento AS DECIMAL(6,2))) as COMPRIMENTO,
IF(espessura = '00000',null,CAST(espessura AS DECIMAL(6,2))) as ESPESSURA,
IF(largura = '00000',null,CAST(largura AS DECIMAL(6,2))) as LARGURA,
NULL as EXPECTATIVA_VENDA,
nomeComercial as NOME_COMERCIAL,
CAST(codigoEdicao AS UNSIGNED) as NUMERO_EDICAO, -- CAST(IF(codigoEdicao ='0000',lancamento,codigoEdicao) AS UNSIGNED),
'INTERFACE' as ORIGEM,
CAST(pacotePadrao AS UNSIGNED) as PACOTE_PADRAO,
IF(tipoRecolhimento ='P',1,0) as PARCIAL,
IF(CAST(peb AS UNSIGNED) = 0,31,CAST(peb AS UNSIGNED)) as PEB,
0 as PERMITE_VALE_DESCONTO,
CAST(peso AS UNSIGNED) PESO,
IF(contemBrinde ='S',1,0) as POSSUI_BRINDE,
(select (max(precoPrevisto) * 1) / 10000 from carga_prodin_lan cpl where cpl.codigoProduto = codigoProduto and cpl.codigoEdicao = codigoEdicao) as PRECO_CUSTO,-- 0, --
(select (max(precoPrevisto) * 1) / 10000 from carga_prodin_lan cpl where cpl.codigoProduto = codigoProduto and cpl.codigoEdicao = codigoEdicao) as PRECO_PREVISTO,-- 0, --
(select (max(precoPrevisto) * 1) / 10000 from carga_prodin_lan cpl where cpl.codigoProduto = codigoProduto and cpl.codigoEdicao = codigoEdicao) as PRECO_VENDA,-- 0, --
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
from CARGA_PRODIN_PRD
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
NULL as COMPRIMENTO, -- IF(comprimento = '00000',null,CAST(comprimento AS DECIMAL(6,2))),
NULL as ESPESSURA, -- IF(espessura = '00000',null,CAST(espessura AS DECIMAL(6,2))),
NULL as LARGURA, -- IF(largura = '00000',null,CAST(largura AS DECIMAL(6,2))),
NULL as NOME,
NULL as NOME_COMERCIAL, -- nomeComercial,
CAST(IF(codigoEdicao ='0000',lancamento,codigoEdicao) AS UNSIGNED) as NUMERO_EDICAO,
'INTERFACE' as ORIGEM,
CAST(pacotePadrao AS UNSIGNED) as PACOTE_PADRAO,
IF(tipoRecolhimento ='P',1,0) as PARCIAL,
IF(CAST(peb AS UNSIGNED) = 0,31,CAST(peb AS UNSIGNED)) as PEB,
0 as PERMITE_VALE_DESCONTO,
CAST(peso AS UNSIGNED) as PESO,
0 as POSSUI_BRINDE,-- CAST(contemBrinde AS UNSIGNED),
(select (max(precoPrevisto) * 1) / 10000 from carga_prodin_lan cpl where cpl.codigoProduto = codigoProduto and cpl.codigoEdicao = codigoEdicao) as PRECO_CUSTO,-- 0, --
(select (max(precoPrevisto) * 1) / 10000 from carga_prodin_lan cpl where cpl.codigoProduto = codigoProduto and cpl.codigoEdicao = codigoEdicao) as PRECO_PREVISTO,-- 0, --
(select (max(precoPrevisto) * 1) / 10000 from carga_prodin_lan cpl where cpl.codigoProduto = codigoProduto and cpl.codigoEdicao = codigoEdicao) as PRECO_VENDA,-- 0, --
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
from CARGA_MDC_MATRIZ
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
set desconto = 30.00
where origem = 'MANUAL';

-- preco_previsto * FIXME Isso está sendo feito devido a interface 118 não atualizar preço_previsto e deixa o atributo com zero
update produto_edicao set preco_previsto = preco_venda
where (preco_previsto = 0 or preco_previsto is null)
and preco_venda is not null and preco_venda > 0;

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
sysdate() as DATA_CRIACAO, -- str_to_date(dataGeracaoArquivo,'%Y%m%d'),
str_to_date(if(codigoEdicao='0000',dataMovimento,dataLancamentoRecolhimento),'%Y%m%d') as DATA_LCTO_DISTRIBUIDOR,
str_to_date(if(codigoEdicao='0000',dataMovimento,dataLancamentoRecolhimento),'%Y%m%d') as DATA_LCTO_PREVISTA,
ADDDATE(str_to_date(if(codigoEdicao='0000',dataMovimento,dataLancamentoRecolhimento),'%Y%m%d'),(select peb from produto_edicao where numero_edicao = CAST(codigoEdicao AS UNSIGNED) and  produto_id = (select id from produto p where p.codigo = codigoProduto))) as DATA_REC_DISTRIB,
ADDDATE(str_to_date(if(codigoEdicao='0000',dataMovimento,dataLancamentoRecolhimento),'%Y%m%d'),(select peb from produto_edicao where numero_edicao = CAST(codigoEdicao AS UNSIGNED) and  produto_id = (select id from produto p where p.codigo = codigoProduto))) as DATA_REC_PREVISTA,
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
and trim(LEADING '0' FROM c.codigoBarras) <> '';

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
select distinct tipoPessoa,cnpj,inscrEstadual,inscrMunicipal,nomeJornaleiro,nomeJornaleiro,0
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
CASE
WHEN situacaoCota = 1 then 'ATIVO'
WHEN situacaoCota = 2 then 'SUSPENSO'
WHEN situacaoCota = 3 then 'PENDENTE'
WHEN situacaoCota = 4 then 'INATIVO'
WHEN situacaoCota = 5 then 'PENDENTE'
ELSE 'PENDENTE' -- FIXME Nao deveria entrar no else
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

select 'COTA + BANCA:',count(*) from COTA; -- Log

select 'ENDERECO:',count(*) from ENDERECO; -- Log

-- Atualiza para cotas ALTERNATIVO as cotas vindas da carga inicial.
update cota set tipo_distribuicao_cota = 'ALTERNATIVO'
where numero_cota in (select numero_cota from CARGA_MDC_COTA_ALTERNATIVAS);

select 'COTAS ALTERNATIVAS:',GROUP_CONCAT(numero_cota SEPARATOR ',') from CARGA_MDC_COTA_ALTERNATIVAS; -- Log

-- FIXME Endereco
insert into endereco (BAIRRO,CEP,CIDADE,CODIGO_BAIRRO,CODIGO_CIDADE_IBGE,CODIGO_UF,
COMPLEMENTO,LOGRADOURO,NUMERO,TIPO_LOGRADOURO,UF,PESSOA_ID)
select 
'',
cep,
municipio,
codBairro,
codCidadeIbge,
21,-- ufEditor,
NULL,
endereco,
numLogradouro,
NULL,
siglaUf,
(select ct.pessoa_id from cota ct where ct.numero_cota = codigoCota)
from carga_mdc_cota;

select 'ENDERECO (COTA):',count(*) from ENDERECO; -- Log

-- FIXME Endereco Cota
insert into endereco_cota (PRINCIPAL,TIPO_ENDERECO,ENDERECO_ID,COTA_ID)
select 
1,
'COMERCIAL',
(select ed.id from cota ct,endereco ed where ct.numero_cota = codigoCota and ct.pessoa_id = ed.pessoa_id limit 1),
(select ct.id from cota ct where ct.numero_cota = codigoCota)
from carga_mdc_cota;

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

insert into pdv (ARRENDATARIO,BALCAO_CENTRAL,PONTO_PRINCIPAL,POSSUI_CARTAO_CREDITO,
POSSUI_COMPUTADOR,POSSUI_LUMINOSO,DENTRO_OUTRO_ESTABELECIMENTO,NOME,PONTO_REFERENCIA,
PORCENTAGEM_FATURAMENTO,POSSUI_SISTEMA_IPV,QTDE_FUNCIONARIOS,STATUS_PDV,COTA_ID,TIPO_PONTO_PDV_ID)
select 
0,
0,
1,
0,
0,
0,
0,
(select nome from pessoa where id = (select pessoa_id from cota where numero_cota = codigoCota)),
pontoReferencia,
0,
0,
0,
'ATIVO',
(select id from cota where numero_cota = codigoCota),
CAST(tipoPontoVenda AS UNSIGNED)
from CARGA_MDC_BANCA;

select 'BANCA|PDV:',count(*) from PDV; -- Log

-- Endereco
insert into endereco (BAIRRO,CEP,CIDADE,CODIGO_BAIRRO,CODIGO_CIDADE_IBGE,CODIGO_UF,
COMPLEMENTO,LOGRADOURO,NUMERO,TIPO_LOGRADOURO,UF,PESSOA_ID)
select 
'',
cep,
nomeMunicipio,
codBairro,
NULL,
21,-- ufEditor,
NULL,
endereco,
NULL, -- FIXME
NULL,
siglaUf,
(select ct.pessoa_id from cota ct where ct.numero_cota = codigoCota)
from carga_mdc_banca;

-- Endereco PDV
insert into endereco_pdv (PRINCIPAL,TIPO_ENDERECO,ENDERECO_ID,PDV_ID)
select 
1,
'COMERCIAL',
(select ed.id from cota ct,endereco ed where ct.numero_cota = codigoCota and ct.pessoa_id = ed.pessoa_id limit 1),
(select pdv.id from pdv where pdv.cota_id = (select ct.id from cota ct where ct.numero_cota = codigoCota) limit 1)
from carga_mdc_banca;

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
IF(pes.tipo = 'J','COMERCIAL','RESIDENCIAL'),
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
update endereco set tipo_logradouro = 'RUA'
where tipo_logradouro is null
and substr(logradouro,1,3) in ('Rua','R. ','R..','Rua');

update endereco set tipo_logradouro = 'AVENIDA'
where tipo_logradouro is null
and substr(logradouro,1,3) in ('Av.','A. ','A..');

update endereco set tipo_logradouro = 'PRAÇA'
where tipo_logradouro is null
and substr(logradouro,1,3) in ('PC.','PCA ','P..');

update endereco set tipo_logradouro = 'ESTRADA'
where tipo_logradouro is null
and substr(logradouro,1,3) in ('ES.');

update endereco set tipo_logradouro = 'LARGO'
where tipo_logradouro is null
and substr(logradouro,1,3) in ('LR.');

update endereco set tipo_logradouro = 'RODOVIA'
where tipo_logradouro is null
and substr(logradouro,1,3) in ('RO.');

update endereco set tipo_logradouro = 'ALAMENDA'
where tipo_logradouro is null
and substr(logradouro,1,3) in ('AL.');

update endereco set tipo_logradouro = 'TRAVESSA'
where tipo_logradouro is null
and substr(logradouro,1,3) in ('TR.','TV.');

update endereco set logradouro=substring( logradouro,5) 
where logradouro <> '   ' 
and substring( logradouro,1,3) in ('Rua','PC.','AV.','RUA','ES.','R..','LR.','RO.','AL.','TR.','TV.');

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

-- #################

select 'FIM CARGA_PRODIN_MDC',sysdate(); -- Log






