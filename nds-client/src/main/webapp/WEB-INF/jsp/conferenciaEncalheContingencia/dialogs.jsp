<div id="dialog-salvar" title="Salvar Conferência" style="display: none;">
	<fieldset>
		<legend>Salvar Conferência</legend>
		<p>Confima a Conferência de Encalhe?</p>
	</fieldset>
</div>

<div id="dialog-notaFiscal" title="Dados da Nota Fiscal" style="display: none;">
	<p>
		<strong>Insira os dados da Nota Fiscal</strong>
	</p>
	
	<table width="670" border="0" cellspacing="1" cellpadding="1">
		<tr>
			<td width="119">Núm. Nota Fiscal:</td>
			<td width="321"><input type="text" name="textfield"
				id="textfield" style="width: 200px;" /></td>
			<td width="106">Série:</td>
			<td width="111"><input type="text" name="textfield2"
				id="textfield2" style="width: 80px;" /></td>
		</tr>
		<tr>
			<td>Data:</td>
			<td><input type="text" name="textfield3" id="textfield3"
				style="width: 80px;" /></td>
			<td>Valor Total R$:</td>
			<td><input type="text" name="textfield4" id="textfield4"
				style="width: 80px; text-align: right;" onblur="validarVlr();" /></td>
		</tr>
		<tr>
			<td>NF-e:</td>
			<td>
				<table width="300" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td width="26"><input type="radio" name="radio" id="radio"
							value="radio" /></td>
						<td width="71" valign="bottom">Sim</td>
						<td width="20"><input type="radio" name="radio" id="radio2"
							value="radio" /></td>
						<td width="183" valign="bottom">Não</td>
					</tr>
				</table>
			</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td>Chave de Acesso:</td>
			<td colspan="3"><input type="text" name="textfield6"
				id="textfield6" style="width: 510px;" /></td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td colspan="3">
				<div id="effect_1"
					style="padding: 0 .7em; width: 500px; float: right !important;"
					class="ui-state-highlight ui-corner-all">
					<p>
						<span style="float: left; margin-right: .3em;"
							class="ui-icon ui-icon-info"></span> <b>Nota Fiscal e Encalhe
							com diferença.</b>
					</p>
				</div>
			</td>
		</tr>
	</table>
</div>

<div id="dialog-alert" title="Nota Fiscal">
	<fieldset style="width: 410px;">
		<legend>Nota Fiscal</legend>
		<p>Existe Nota Fiscal para esta Cota?</p>
	</fieldset>
</div>

<div id="dialog-finaliza-conferencia" title="Finalizar Conferência"
	style="display: none;">
	<fieldset>
		<legend>Conferência Finalizada</legend>
		<p>Confirma a Conferência do Encalhe?</p>
	</fieldset>
</div>

<div id="dialog-outros-valores" title="Outros Valores"
	style="display: none;">
	<fieldset>
		<legend>Outros Valores</legend>
		<table class="outrosVlrsGrid"></table>
	</fieldset>
</div>

<div id="dialog-encalhe" title="Novo Encalhe">
	
	<jsp:include page="../messagesDialog.jsp">
		<jsp:param value="idModalNovoEncalhe" name="messageDialog"/>
	</jsp:include>
	
	<table width="425" border="0" cellspacing="2" cellpadding="2">
		<tr>
			<td width="125">Produto:</td>
			<td width="286">
				<input name="lstProdutos" type="text" id="lstProdutos" style="width: 280px;" />
			</td>
		</tr>
		<tr>
			<td>Edição:</td>
			<td>
				<input type="text" style="width: 80px;" disabled="disabled" id="numEdicaoNovoEncalhe" />
			</td>
		</tr>
		<tr>
			<td>Preço Capa R$:</td>
			<td>
				<input type="text" style="width: 80px; text-align: right;" disabled="disabled" id="precoCapaNovoEncalhe" />
			</td>
		</tr>
		<tr>
			<td>Preço Desconto R$:</td>
			<td>
				<input type="text" style="width: 80px;" disabled="disabled" id="descontoNovoEncalhe" />
			</td>
		</tr>
		<tr>
			<td>Exemplares:</td>
			<td><input type="text" style="width: 80px;" id="exemplaresNovoEncalhe" onchange="ConferenciaEncalheCont.calcularValorTotalNovoEncalhe();" /></td>
		</tr>
		<tr>
			<td>Valor Total R$</td>
			<td>
				<input type="text" style="width: 80px; text-align: right;" disabled="disabled" id="valorTotalNovoEncalhe"/>
			</td>
		</tr>
		<tr>
			<td>Juramentada:</td>
			<td>
				<input type="checkbox" name="checkbox" id="checkboxJueramentadaNovoEncalhe" />
			</td>
		</tr>
	</table>
	<br />
	
	<span class="bt_add">
		<a href="javascript:;" onclick="ConferenciaEncalheCont.adicionarEncalhe();">Incluir Novo</a>
	</span>
	
	<br />
</div>

<div id="dialog-conferencia" title="Liberação de Encalhe">
	<p>Confirma a Liberação de Encalhe?</p>
</div>

<div id="dialog-novo" title="Conferência de Encalhe">
	<p>Confirma o Encalhe?</p>
</div>

<div id="dialog-reabertura" title="Reabertura" style="display: none;">
	<fieldset style="width: 310px;">
		<legend>Nota Fiscal</legend>
	    <p>Já existe conferencia de encalhe para esta cota.<br/>
	    Efetuar reabertura?</p>
	</fieldset>
</div>