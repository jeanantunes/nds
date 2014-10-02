/*
TINYINT		-128 , 127 /0 , 255
SMALLINT        -32768 , 32767 / 0 , 65535
MEDIUMINT	-8388608 , 8388607 / 0 , 16777215
INT		-2147483648 , 2147483647 / 0 , 4294967295
BIGINT		-9223372036854775808 , 9223372036854775807 / 0 , 18446744073709551615
*/
--
-- VARIAVEIS DO SISTEMA
--

-- SET max_heap_table_size = 1024 * 1024 * 64;
-- SET tmp_table_size = 1024 * 1024 * 64;

update distribuidor set data_operacao = date(sysdate());
insert into seq_generator (sequence_next_hi_value,sequence_name) values (1,'Movimento');

-- ################################################################################################
-- ##########################################    PRODIN    ########################################
-- ################################################################################################

/*
1 113 desconto distribuidor (dsf) (**arquivo ocasional**)
2 112 editor (edi)
3 109 edicao (pub)
4 110 produto (prd)
5 111 lançamentos  programados (lan)
6 114 recolhimentos programados (**arquivos não diários**) (rec)
7 125 chamada de capa(chc)
8 126 codigo de barras(cdb)
9 135 Nfe (nre)
10 136 Parciais (par)
11 134 Imagens de Capa
*/

--
--  CARGA_PRODIN_DSF
--

DROP TABLE IF EXISTS CARGA_PRODIN_DSF;

create table CARGA_PRODIN_DSF (
codDistribuidor VARCHAR(7), 
dataGeracaoArquivo VARCHAR(8), 
tipoDesconto VARCHAR(2),
percentualDesconto VARCHAR(7),
percentualPrestacao_servico VARCHAR(7),
dataInicioDesconto VARCHAR(8),
fornecedorId VARCHAR(1),
KEY tipoDesconto (tipoDesconto));

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

--
--  CARGA_PRODIN_EDI
-- 

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
KEY codigoEditor (codigoEditor));

LOAD DATA INFILE '/opt/rollout/load_files/00000001.edi' INTO TABLE CARGA_PRODIN_EDI
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

LOAD DATA INFILE '/opt/rollout/load_files/00000002.edi' INTO TABLE CARGA_PRODIN_EDI
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

--
--  CARGA_PRODIN_PUB
--

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
KEY codigoProduto (codigoProduto));

LOAD DATA INFILE '/opt/rollout/load_files/00000001.pub' INTO TABLE CARGA_PRODIN_PUB
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

LOAD DATA INFILE '/opt/rollout/load_files/00000002.pub' INTO TABLE CARGA_PRODIN_PUB
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

--
--  CARGA_PRODIN_PRD
--

DROP TABLE IF EXISTS CARGA_PRODIN_PRD;

/* -- ********* MATRIZ
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
KEY codigoProdutoEdicao (codigoProduto,codigoEdicao));

LOAD DATA INFILE '/opt/rollout/load_files/00000001.prd' INTO TABLE CARGA_PRODIN_PRD
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

LOAD DATA INFILE '/opt/rollout/load_files/00000002.prd' INTO TABLE CARGA_PRODIN_PRD
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
KEY codigoProdutoEdicao (codigoProduto,codigoEdicao));

LOAD DATA INFILE '/opt/rollout/load_files/00000001.prd' INTO TABLE CARGA_PRODIN_PRD
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

LOAD DATA INFILE '/opt/rollout/load_files/00000002.prd' INTO TABLE CARGA_PRODIN_PRD
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

--
--  CARGA_PRODIN_LAN
--

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
KEY codigoProdutoEdicao (codigoProduto,codigoEdicao));

LOAD DATA INFILE '/opt/rollout/load_files/00000001.lan' INTO TABLE CARGA_PRODIN_LAN
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

LOAD DATA INFILE '/opt/rollout/load_files/00000002.lan' INTO TABLE CARGA_PRODIN_LAN
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

--
--  CARGA_PRODIN_REC
--

DROP TABLE IF EXISTS CARGA_PRODIN_REC;

create table CARGA_PRODIN_REC (
codDistrib VARCHAR(7),
dataGeracaoArquivo VARCHAR(8),
codigoProduto VARCHAR(8),
codigoEdicao VARCHAR(4),
dataRecolhimento VARCHAR(8),
KEY codigoProdutoEdicao (codigoProduto,codigoEdicao));

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

--
--  CARGA_PRODIN_CHC
--

DROP TABLE IF EXISTS CARGA_PRODIN_CHC;

create table CARGA_PRODIN_CHC (
codDistrib VARCHAR(7),
dataGeracaoArquivo VARCHAR(8),
contextoProd VARCHAR(1),
codForncProd VARCHAR(7),
codigoProduto VARCHAR(8),
codigoEdicao VARCHAR(4),
chamadaCapa VARCHAR(15),
KEY codigoProdutoEdicao (codigoProduto,codigoEdicao));

LOAD DATA INFILE '/opt/rollout/load_files/00000001.chc' INTO TABLE CARGA_PRODIN_CHC
(@linha) SET 
codDistrib=SUBSTR(@linha,1,7),
dataGeracaoArquivo=SUBSTR(@linha,8,8),
contextoProd=SUBSTR(@linha,26,1),
codForncProd=SUBSTR(@linha,27,7),
codigoProduto=SUBSTR(@linha,34,8),
codigoEdicao=SUBSTR(@linha,42,4),
chamadaCapa=SUBSTR(@linha,46,15);

LOAD DATA INFILE '/opt/rollout/load_files/00000002.chc' INTO TABLE CARGA_PRODIN_CHC
(@linha) SET 
codDistrib=SUBSTR(@linha,1,7),
dataGeracaoArquivo=SUBSTR(@linha,8,8),
contextoProd=SUBSTR(@linha,26,1),
codForncProd=SUBSTR(@linha,27,7),
codigoProduto=SUBSTR(@linha,34,8),
codigoEdicao=SUBSTR(@linha,42,4),
chamadaCapa=SUBSTR(@linha,46,15);

--
--  CARGA_PRODIN_CDB
--

DROP TABLE IF EXISTS CARGA_PRODIN_CDB;

create table CARGA_PRODIN_CDB (
codDistribuidor VARCHAR(7),
dataGeracaoArquivo VARCHAR(8),
contextoProduto VARCHAR(1),
codFornecedorProduto VARCHAR(4),
codigoProduto VARCHAR(8),
codigoEdicao VARCHAR(4),
codigoBarras VARCHAR(18),
KEY codigoProdutoEdicao (codigoProduto,codigoEdicao));

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


-- ################################################################################################
-- #############################################  MDC  ############################################
-- ################################################################################################

/*
1 119 produto (PRODUTO.NEW)
2 117 cota (COTA.NEW)
3 116 banca (PDV) (BANCA.NEW)
4 108 matriz lançamento/recohimento (somente produtos não interface) (MATRIZ.NEW)
5 118 preco (PRECO.NEW)
6 106 estudo (DEAPR19.NEW)
7 107 estudo cota (DEAJO19.NEW)
*/

--
--  CARGA_MDC_PRODUTO
--

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
KEY codigoProduto (codigoProduto));

LOAD DATA INFILE '/opt/rollout/load_files/PRODUTO.NEW' INTO TABLE CARGA_MDC_PRODUTO
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

--
--  CARGA_MDC_COTA
--

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
KEY codigoCota (codigoCota));

LOAD DATA INFILE '/opt/rollout/load_files/COTA.NEW' INTO TABLE CARGA_MDC_COTA
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

--
--  CARGA_MDC_BANCA
--

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
KEY codigoCota (codigoCota));

LOAD DATA INFILE '/opt/rollout/load_files/BANCA.NEW' INTO TABLE CARGA_MDC_BANCA
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

--
--  CARGA_MDC_MATRIZ
--

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
KEY codigoProdutoEdicao (codigoProduto,codigoEdicao));

LOAD DATA INFILE '/opt/rollout/load_files/MATRIZ.NEW' INTO TABLE CARGA_MDC_MATRIZ
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

--
--  CARGA_MDC_PRECO
--

DROP TABLE IF EXISTS CARGA_MDC_PRECO;

create table CARGA_MDC_PRECO (
codigoProduto VARCHAR(8),
codigoEdicao VARCHAR(4),
preco VARCHAR(10),
condRecolhimentoFinal VARCHAR(1),
KEY codigoProdutoEdicao (codigoProduto,codigoEdicao));

LOAD DATA INFILE '/opt/rollout/load_files/PRECO.NEW' INTO TABLE CARGA_MDC_PRECO
(@linha) SET 
codigoProduto=SUBSTR(@linha,1,8),
codigoEdicao=SUBSTR(@linha,9,4),
preco=SUBSTR(@linha,13,10),
condRecolhimentoFinal=SUBSTR(@linha,23,1);

-- ################################################################################################
-- #########################################  DISTRIBUIDOR    #####################################
-- ################################################################################################
-- 
-- HVND
--

DROP TABLE IF EXISTS HVND; 

create table HVND (
COD_COTA_HVCT INT,
COD_PRODUTO_HVCT VARCHAR(12),
PRECO FLOAT,
QTDE_REPARTE_HVCT INT,
QTDE_ENCALHE_HVCT INT,
DATA_LANCAMENTO_STR VARCHAR(10),
DATA_RECOLHIMENTO_STR VARCHAR(10),
STATUS CHAR(1));

LOAD DATA INFILE '/opt/rollout/load_files/HVND.TXT' INTO TABLE HVND COLUMNS TERMINATED BY ';' LINES TERMINATED BY ';\r\n';

ALTER TABLE HVND
ADD COLUMN DATA_LANCAMENTO DATE,
ADD COLUMN DATA_RECOLHIMENTO DATE,
ADD COLUMN cod_produto VARCHAR(8),
ADD COLUMN num_edicao INT,
ADD COLUMN produto_edicao_id bigint,
ADD COLUMN cota_id bigint AFTER STATUS;

update HVND set 
cod_produto=substring(COD_PRODUTO_HVCT, -12, 8), 
num_edicao=substring(COD_PRODUTO_HVCT, -4),
DATA_LANCAMENTO = STR_TO_DATE(DATA_LANCAMENTO_STR, '%d/%m/%Y'),
DATA_RECOLHIMENTO = STR_TO_DATE(DATA_RECOLHIMENTO_STR, '%d/%m/%Y');

ALTER TABLE HVND 
DROP COD_PRODUTO_HVCT, 
-- DROP PRECO, 
DROP DATA_LANCAMENTO_STR,
DROP DATA_RECOLHIMENTO_STR;

-- 
-- ESTQBOX
--

DROP TABLE IF EXISTS ESTQBOX;

create table ESTQBOX (
linha_vazia varchar(1),
tipo int,
box int,
nome_box varchar(10),
produto varchar(8),
edicao int,
nome_produto varchar(45),
quantidade int);

LOAD DATA INFILE '/opt/rollout/load_files/ESTQBOX.NEW' INTO TABLE ESTQBOX COLUMNS TERMINATED BY '|' LINES TERMINATED BY '\r\n' IGNORE 1 LINES;

ALTER TABLE ESTQBOX
ADD COLUMN produto_edicao_id bigint AFTER quantidade;

/*
-- 
-- ESTQMOV
--

DROP TABLE IF EXISTS ESTQMOV;

create table ESTQMOV (
linha_vazia varchar(1),
tipo int,
produto varchar(8),
edicao int,
DATA date,
NRO_DOCTO int,
TIPO_MOVTO int,
ORIGEM int,
DESTINO int,
QUANTIDADE int,
PRECO_CAPA decimal(18,2),
PERC_DESCTO decimal(18,2),
DOCTO_ORIGEM int,
FLAG_ESTORNO varchar(1));

ALTER TABLE ESTQMOV
ADD COLUMN produto_edicao_id bigint AFTER FLAG_ESTORNO;

LOAD DATA LOCAL INFILE '/opt/rollout/load_files/ESTQMOV.NEW' INTO TABLE estqmov COLUMNS TERMINATED BY '|' LINES TERMINATED BY '\r\n';

-- update estqmov set produto_edicao_id = (select pe.id from produto_edicao pe, produto p 
-- where p.id = pe.produto_id 
-- and p.codigo = produto 
-- and pe.numero_edicao = edicao);
*/

-- 
-- MOV_CRED
--

DROP TABLE IF EXISTS MOV_CRED;

create table MOV_CRED (
linha_inicial varchar(1), 
data varchar(8),
numero_cota int,
tipo_credito int,
desc_credito varchar(255),
valor varchar(11));

LOAD DATA INFILE '/opt/rollout/load_files/ARQCD08.NEW' INTO TABLE MOV_CRED COLUMNS TERMINATED BY '|' LINES TERMINATED BY '\r\n';

ALTER TABLE MOV_CRED
ADD COLUMN valor_decimal decimal(11,2),
ADD COLUMN data_real date,
ADD COLUMN cota_id bigint AFTER valor;

update mov_cred set valor_decimal = (cast(valor as decimal(12.2))/100), data_real = str_to_date(data, '%Y%m%d'), cota_id = (
select c.id from cota c where c.numero_cota = mov_cred.numero_cota);

-- 
-- MOV_DEB
--

DROP TABLE IF EXISTS MOV_DEB;

create table MOV_DEB (
linha_inicial varchar(1),
data varchar(8),
numero_cota int,
tipo_debito int,
desc_debito varchar(255),
valor varchar(11));

LOAD DATA INFILE '/opt/rollout/load_files/ARQDB97.NEW' INTO TABLE MOV_DEB COLUMNS TERMINATED BY '|' LINES TERMINATED BY '\r\n';

ALTER TABLE MOV_DEB
ADD COLUMN valor_decimal decimal(11,2),
ADD COLUMN data_real date,
ADD COLUMN cota_id bigint AFTER valor;

update mov_deb set valor_decimal = (cast(valor as decimal(12.2))/100), data_real = str_to_date(data, '%Y%m%d'), cota_id = (
select c.id from cota c where c.numero_cota = mov_deb.numero_cota);

/*
--
--  CARGA_DISTRIBUIDOR_HVND
--

DROP TABLE IF EXISTS CARGA_DISTRIBUIDOR_HVND; 

create table CARGA_DISTRIBUIDOR_HVND (
COD_COTA_HVCT VARCHAR(3),
COD_PRODUTO_HVCT VARCHAR(12),
PRECO VARCHAR(6),
QTDE_REPARTE_HVCT VARCHAR(4),
QTDE_ENCALHE_HVCT VARCHAR(4),
DATA_LANCAMENTO_STR VARCHAR(10),
DATA_RECOLHIMENTO_STR VARCHAR(10),
STATUS VARCHAR(1));

LOAD DATA INFILE '/opt/rollout/load_files/HVND.TXT' INTO TABLE CARGA_DISTRIBUIDOR_HVND COLUMNS TERMINATED BY ';' LINES TERMINATED BY ';\r\n';

-- 
-- CARGA_DISTRIBUIDOR_ESTQBOX
--

DROP TABLE IF EXISTS CARGA_DISTRIBUIDOR_ESTQBOX;

create table CARGA_DISTRIBUIDOR_ESTQBOX (
tipo VARCHAR(1),
box VARCHAR(3),
nome_box VARCHAR(10),
produto VARCHAR(8),
edicao VARCHAR(4),
quantidade VARCHAR(7),
nome_produto VARCHAR(45));

LOAD DATA INFILE '/opt/rollout/load_files/ESTQBOX.NEW' INTO TABLE CARGA_DISTRIBUIDOR_ESTQBOX COLUMNS TERMINATED BY '|' LINES TERMINATED BY '\r\n' IGNORE 1 LINES;

delete from CARGA_DISTRIBUIDOR_ESTQBOX where tipo = '0';

-- 
-- CARGA_DISTRIBUIDOR_MOV_CRED
--

DROP TABLE IF EXISTS CARGA_DISTRIBUIDOR_MOV_CRED;

create table CARGA_DISTRIBUIDOR_MOV_CRED (
data VARCHAR(8),
numero_cota VARCHAR(4),
tipo_credito VARCHAR(2),
desc_credito VARCHAR(20),
valor VARCHAR(11));

LOAD DATA INFILE '/opt/rollout/load_files/ARQCD08.NEW' INTO TABLE CARGA_DISTRIBUIDOR_MOV_CRED COLUMNS TERMINATED BY '|' LINES TERMINATED BY '\r\n';

-- 
-- MOV_DEB
--

DROP TABLE IF EXISTS CARGA_DISTRIBUIDOR_MOV_DEB;

create table CARGA_DISTRIBUIDOR_MOV_DEB (
data VARCHAR(8),
numero_cota VARCHAR(4),
tipo_credito VARCHAR(2),
desc_credito VARCHAR(20),
valor VARCHAR(11));

LOAD DATA INFILE '/opt/rollout/load_files/ARQDB97.NEW' INTO TABLE CARGA_DISTRIBUIDOR_MOV_DEB COLUMNS TERMINATED BY '|' LINES TERMINATED BY '\r\n';
*/

-- ################################################################################################
-- ##########################################     OUTROS    #######################################
-- ################################################################################################

--
--  CARGA_LANCAMENTO_MDC
--

DROP TABLE IF EXISTS CARGA_LANCAMENTO_MDC;

CREATE TABLE CARGA_LANCAMENTO_MDC (
id int(11) NOT NULL,
COD_AGENTE_LANP varchar(7),
COD_PRODUTO_LANP varchar(14),
COD_PRODIN varchar(8),
NUM_EDICAO int(11),
DATA_PREVISTA_LANCAMENTO_LANP varchar(10),
DATA_REAL_LANCAMENTO_LANP varchar(10),
TIPO_STATUS_LANCTO_LANP varchar(3),
TIPO_LANCAMENTO_LANP varchar(3),
VLR_PRECO_REAL_LANP varchar(10),
COD_PRODUTO_RCPR varchar(12),
NUM_RECOLTO_RCPR varchar(11),
DATA_PREVISTA_RECOLTO_RCPR varchar(10),
DATA_REAL_RECOLTO_RCPR varchar(10),
TIPO_STATUS_RECOLTO_RCPR varchar(45),
PRIMARY KEY (id));

LOAD DATA INFILE '/opt/rollout/load_files/CARGA_LANCAMENTO_MDC.CAR' INTO TABLE CARGA_LANCAMENTO_MDC COLUMNS TERMINATED BY ';' ENCLOSED BY '"' LINES TERMINATED BY '\r\n' IGNORE 1 LINES;

ALTER TABLE CARGA_LANCAMENTO_MDC
ADD COLUMN produto_edicao_id bigint AFTER TIPO_STATUS_RECOLTO_RCPR;

/*
--
--  CARGA_LANCAMENTO_NDS
--

DROP TABLE IF EXISTS CARGA_LANCAMENTO_NDS;

CREATE TABLE carga_lancamento_nds (
codigodistribuidor varchar(8) NOT NULL,
codigoFornecedorProduto INT,
codigoProduto varchar(8),
edicaoProduto INT,
dataLancamento DATE,
repartePrevisto FLOAT,
precoPrevisto FLOAT,
repartePromocional FLOAT,
produto_edicao_id INT);

LOAD DATA INFILE '/opt/rollout/load_files/00000001.LAN' INTO TABLE carga_lancamento_nds
(@var1)
SET 
codigodistribuidor=SUBSTR(@var1,1,7),
codigoFornecedorProduto=SUBSTR(@var1,27,7),
codigoProduto=SUBSTR(@var1,34,8),
edicaoProduto=SUBSTR(@var1,42,4),
dataLancamento=STR_TO_DATE(SUBSTR(@var1,50,8),'%Y%m%d'),
repartePrevisto=SUBSTR(@var1,62,8),
precoPrevisto=SUBSTR(@var1,82,10),
repartePromocional=SUBSTR(@var1,92,8);

LOAD DATA INFILE '/opt/rollout/load_files/00000002.LAN' INTO TABLE carga_lancamento_nds
(@var1)
SET 
codigodistribuidor=SUBSTR(@var1,1,7),
codigoFornecedorProduto=SUBSTR(@var1,27,7),
codigoProduto=SUBSTR(@var1,34,8),
edicaoProduto=SUBSTR(@var1,42,4),
dataLancamento=STR_TO_DATE(SUBSTR(@var1,50,8),'%Y%m%d'),
repartePrevisto=SUBSTR(@var1,62,8),
precoPrevisto=SUBSTR(@var1,82,10),
repartePromocional=SUBSTR(@var1,92,8);

*/
-- 
-- 05
--
/*
DROP TABLE IF EXISTS ESTQBOX;

create table ESTQBOX (
linha_vazia varchar(1),
tipo int,
produto varchar(8),
edicao int,
DATA date,
NRO_DOCTO int,
TIPO_MOVTO int,
ORIGEM int,
DESTINO int,
QUANTIDADE int,
PRECO_CAPA decimal(18,2),
PERC_DESCTO decimal(18,2),
DOCTO_ORIGEM int,
FLAG_ESTORNO varchar(1),
produto_edicao_id int);

LOAD DATA LOCAL INFILE '/opt/rollout/load_files/ESTQMOV.NEW' INTO TABLE estqmov COLUMNS TERMINATED BY '|' LINES TERMINATED BY '\r\n';

-- update estqmov set produto_edicao_id = (select pe.id from produto_edicao pe, produto p 
-- where p.id = pe.produto_id 
-- and p.codigo = produto 
-- and pe.numero_edicao = edicao);
*/
