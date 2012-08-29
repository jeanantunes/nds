<head>

<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/banco.js"></script>

<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>

<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.price_format.1.7.js"></script>

<script language="javascript" type="text/javascript">
$(function(){
	bancoController.init();

});
</script>
<style>
label {
	vertical-align: super;
}
</style>
</head>

<body>

	<form id="excluir-banco-form">
	<div id="dialog-excluir" title="Excluir Banco">
		<p>Confirma a exclusão deste Banco?</p>
	</div>
	</form>

	<form id="novo-banco-form">
	<div id="dialog-novo" title="Incluir Novo Banco">

        <jsp:include page="../messagesDialog.jsp" />

        <form name="formularioNovoBanco" id="formularioNovoBanco"> 
			<fieldset>
			<legend>Dados de Bancos</legend>

			<table width="626" border="0" cellpadding="2" cellspacing="1">
				<tr>
					<td width="106">Número Banco:</td>
					<td width="215"><input type="text" name="newNumero" maxlength="17"
						id="newNumero" style="width: 143px;" />
					</td>
					<td width="68">Nome:</td>
					<td width="216"><input type="text" name="newNome" maxlength="100"
						id="newNome" style="width: 215px;" />
					</td>
				</tr>
				<tr>
					<td nowrap="nowrap">Código Cedente:</td>
					<td><input type="text" name="newCodigoCedente" id="newCodigoCedente" maxlength="17"
						style="width: 143px;" />
					</td>
					<td>Agência:</td>
					<td><input maxlength="17" type="text" name="newAgencia" id="newAgencia"
						style="width: 215px;" />
					</td>
				</tr>
				<tr>
					<td>Conta / Digito:</td>
					<td><input maxlength="17" type="text" name="newConta" id="newConta"
						style="width: 80px;" /> - <input maxlength="1" type="text" name="newDigito"
						id="newDigito" style="width: 30px;" />
					</td>
					<td>Apelido:</td>
					<td width="216"><input type="text" name="newApelido" maxlength="100"
						id="newApelido" style="width: 215px;" />
					</td>
				</tr>
				<tr>
					<td>Carteira:</td>
					<td>
					
					<input type="text" name="newCarteira" maxlength="100"
						id="newCarteira" style="width: 143px;" />
													
					</td>
					<td>Juros %:</td>
					<td><input maxlength="17" type="text" name="newJuros" id="newJuros"
						style="width: 80px; text-align:right;" />
					</td>
				</tr>
				<tr>
					<td>Status:</td>
					<td><input name="newAtivo" type="checkbox" value=""
						checked="checked" id="newAtivo" /><label for="statusBco">Ativo</label>
					</td>
	
					<td>Multa %:</td>
					<td>
					    <input onchange="bancoController.limparVrMulta();" maxlength="17" type="text" name="newMulta" id="newMulta" style="width:80px; text-align:right;" />
					    ou R$: <input onchange="bancoController.limparMulta();" maxlength="17" type="text" name="newVrMulta" id="newVrMulta" style="width:80px; text-align:right;" />
					</td>

				</tr>
				<tr>
					<td>Instruções:</td>
					<td colspan="3"><textarea name="newInstrucoes" id="newInstrucoes" maxlength="200"
							style="width: 503px;"></textarea>
					</td>
				</tr>
			</table>
			</fieldset>
		</form>
		
	</div>
	</form>

    <form name="formularioAlteraBanco" id="formularioAlteraBanco"> 
    <div id="dialog-alterar" title="Alterar Banco">
    
        <jsp:include page="../messagesDialog.jsp" />

	
			<label><strong>Dados de Bancos</strong>
			</label>
	
	        <input type="hidden" id="idBanco" name="idBanco" />
	
			<table width="626" border="0" cellpadding="2" cellspacing="1">
				<tr>
					<td width="111">Número Banco:</td>
					<td width="216"><input type="text" name="alterNumero" maxlength="17"
						id="alterNumero" style="width: 143px;" />
					</td>
					<td width="73">Nome:</td>
					<td width="205"><input type="text" name="alterNome" maxlength="100"
						id="alterNome" style="width: 215px;" />
					</td>
				</tr>
				<tr>
					<td>Código Cedente:</td>
					<td><input type="text" name="alterCodigoCedente" id="alterCodigoCedente" maxlength="17"
						style="width: 143px;" />
					</td>
					<td>Agência:</td>
					<td><input maxlength="17" type="text" name="alterAgencia" id="alterAgencia"
						style="width: 215px;" />
					</td>
				</tr>
				<tr>
					<td>Conta / Digito:</td>
					<td><input maxlength="17" type="text" name="alterConta" id="alterConta"
						style="width: 97px;" /> - <input maxlength="1" type="text" name="alterDigito"
						id="alterDigito" style="width: 30px;" />
					</td>
					<td>Apelido:</td>
					<td width="205"><input type="text" name="alterApelido" maxlength="100"
						id="alterApelido" style="width: 215px;" />
					</td>
				</tr>
				<tr>
					<td>Carteira:</td>
					<td>
						
						<input type="text" name="alterCarteira" maxlength="100"
						id="alterCarteira" style="width: 215px;" />
								                
					</td>
					<td>Juros %:</td>
					<td><input maxlength="17" type="text" name="alterJuros" id="alterJuros"
						style="width: 80px;" />
					</td>
				</tr>
				<tr>
					<td>Status:</td>
					<td><input name="alterAtivo" type="checkbox" value=""
						checked="checked" id="alterAtivo" /><label for="statusBco">Ativo</label>
					</td>
					
					<td>Multa %:</td>
					<td>
					    <input onchange="bancoController.limparVrMulta();" maxlength="17" type="text" name="alterMulta" id="alterMulta" style="width:80px; text-align:right;" />
					    ou R$: <input onchange="bancoController.limparMulta();" maxlength="17" type="text" name="alterVrMulta" id="alterVrMulta" style="width:80px; text-align:right;" />
					</td>
					
				</tr>
				<tr>
					<td>Instruções:</td>
					<td colspan="3"><textarea name="alterInstrucoes" id="alterInstrucoes" maxlength="200"
							style="width: 490px;"></textarea>
					</td>
				</tr>
			</table>
			
	</div>
	</form>

    <form name="formularioFiltro" id="formularioFiltro"> 
		<div class="areaBts">
			<div class="area">
				<span class="bt_novos"><a href="javascript:;" onclick="bancoController.popup();" rel="tipsy" title="Incluir Novo Banco"><img src="${pageContext.request.contextPath}/images/ico_salvar.gif" border="0" /></a></span>
			</div>
		</div>
		<div class="linha_separa_fields">&nbsp;</div>
		<fieldset class="fieldFiltro">
			<legend> Pesquisar Bancos</legend>
			<table width="950" border="0" cellpadding="2" cellspacing="1"
				class="filtro">
				<tr>
					<td width="41">Nome:</td>
					<td colspan="3"><input type="text" name="nome"
						id="nome" style="width: 180px;" />
					</td>
					<td width="54">Número:</td>
					<td width="143"><input type="text" name="numero"
						id="numero" style="width: 130px;" />
					</td>
					<td width="56">Cedente:</td>
					<td width="160"><input type="text" name="cedente"
						id="cedente" style="width: 150px;" />
					</td>
					<td width="20"><input name="ativo" type="checkbox"
						id="ativo" style="float: left;" value="" checked="checked" />
					</td>
                    <td width="39"><label
						for="ativo">Ativo</label></td>
					<td width="211"><span class="bt_novos"><a href="javascript:;" onclick="bancoController.mostrarGridConsulta();"><img src="${pageContext.request.contextPath}/images/ico_pesquisar.png" border="0" /></a>
					</span>
					</td>
				</tr>
			</table>
	
		</fieldset>
	
	</form>
	
	<div class="linha_separa_fields">&nbsp;</div>
	<div class="grids" style="display: none;">
		<fieldset class="fieldGrid">
			<legend>Bancos Cadastrados</legend>
			
				<table class="bancosGrid"></table>
				
		</fieldset>
	</div>
</body>
