<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>meuConteudo</title>

<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/pesquisaProduto.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>

<script type="text/javascript">

var pesquisaProdutoExtratoEdicao = new PesquisaProduto();

$(function(){
	jsExtratoEdicao.init(pesquisaProdutoExtratoEdicao);
});

</script>

</head>

<body>

	<div class="container">

		<fieldset class="classFieldset">
		
			<legend> Extrato de Edição </legend>
			
			<table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
				
				<tr>
					<td width="96">
						Código:
					</td>
					
					<td width="123">
					
						<input 	type="text" 
								name="codigo"
								id="codigo"
								maxlength="255"
								style="width: 70px; 
								float: left; 
								margin-right: 5px;"
								onchange="jsExtratoEdicao.pesquisarProdutoPorCodigo();" />

						
					
					</td>
					
					<td width="85">
						Produto:
					</td>
					
					<td width="236">

						<input type="text" name="produto" id="produto" style="width: 220px;" 
							   maxlength="255"
					       	   onkeyup="pesquisaProdutoExtratoEdicao.autoCompletarPorNomeProduto('#produto', false);"
					       	   onblur="jsExtratoEdicao.pesquisarProdutoPorNome();"/>

						
					
					</td>
					
					<td width="60">
						Edição:
					</td>
					
					<td width="186">
					
					<input type="text" style="width:70px;" name="edicao" id="edicao" maxlength="20"
						   onchange="jsExtratoEdicao.validarNumeroEdicao();"/>
								
					</td>
					
					<td width="128">&nbsp;</td>
				
				</tr>
				<tr>
					<td>Preço da Capa:</td>
					<td><input type="text" name="precoCapa" id="precoCapa"
						style="width: 70px; float: left; margin-right: 5px;"
						disabled="disabled" />
					</td>
					<td>Fornecedor:</td>
					<td><input type="text" name="textfield" id="nomeFornecedor"
						style="width: 220px;" disabled="disabled" />
					</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td><span class="bt_pesquisar"><a href="javascript:;"
							onclick="jsExtratoEdicao.pesquisarExtratoEdicao();">Pesquisar</a>
					</span>
					</td>
				</tr>
			</table>

		</fieldset>
		<div class="linha_separa_fields">&nbsp;</div>

		<fieldset class="classFieldset">
			<legend>Extrato de Edição</legend>
			<div class="grids" style="display: none;">
				<table class="extratoEdicaoGrid"></table>
				<table width="950" border="0" cellspacing="2" cellpadding="2">
					<tr>
						<td width="689">
							<span class="bt_novos" title="Gerar Arquivo">
								<a href="${pageContext.request.contextPath}/estoque/extratoEdicao/exportar?fileType=XLS">
									<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
									Arquivo
								</a>
							</span>
							<span class="bt_novos" title="Imprimir">
								<a href="${pageContext.request.contextPath}/estoque/extratoEdicao/exportar?fileType=PDF">
									<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" alt="Imprimir" hspace="5" border="0" />
									Imprimir
								</a>
							</span>
						</td>
						<td width="115"><strong>Saldo em Estoque:</strong>
						</td>
						<td width="126"><div id="saldoTotalExtratoEdicao"></div></td>
					</tr>
				</table>
			</div>


		</fieldset>

		<div class="linha_separa_fields">&nbsp;</div>

	</div>

</body>
