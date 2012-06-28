<div id="dialog-salvar" title="Salvar Conferência" style="display: none;">
	
	<jsp:include page="../messagesDialog.jsp">
		<jsp:param value="idModalConfirmarSalvarConf" name="messageDialog"/>
	</jsp:include>
	
	<fieldset style="width: 410px;">
		<legend>Salvar Conferência</legend>
		<p>Confima a Conferência de Encalhe?</p>
	</fieldset>
</div>

<iframe src="" id="download-iframe" style="display:none;"></iframe>

<div id="dialog-notaFiscal" title="Dados da Nota Fiscal" style="display: none;">
	<p>
		<strong>Insira os dados da Nota Fiscal</strong>
	</p>
	
	<table width="670" border="0" cellspacing="1" cellpadding="1">
		<tr>
			<td width="119">Núm. Nota Fiscal:</td>
			<td width="321"><input type="text" id="numNotaFiscal" style="width: 200px;" maxlength="255" /></td>
			<td width="106">Série:</td>
			<td width="111"><input type="text" id="serieNotaFiscal" style="width: 80px;" maxlength="255" /></td>
		</tr>
		<tr>
			<td>Data:</td>
			<td><input type="text" id="dataNotaFiscal" style="width: 80px;" /></td>
			<td>Valor Total R$:</td>
			<td><input type="text" id="valorNotaFiscal" style="width: 80px; text-align: right;" maxlength="255" /></td>
		</tr>
		<tr>
			<td>NF-e:</td>
			<td>
				<table width="300" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td width="26"><input type="radio" name="radio" id="radio" value="radio" /></td>
						<td width="71" valign="bottom">Sim</td>
						<td width="20"><input type="radio" name="radio" id="radio2" value="radio" /></td>
						<td width="183" valign="bottom">Não</td>
					</tr>
				</table>
			</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td>Chave de Acesso:</td>
			<td colspan="3"><input type="text" id="chaveAcessoNFE" style="width: 510px;" maxlength="255" /></td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td colspan="3">
				<div id="effect_1"
					style="padding: 0 .7em; width: 500px; float: right !important;"
					class="ui-state-highlight ui-corner-all">
					<p>
						<span style="float: left; margin-right: .3em;"
							class="ui-icon ui-icon-info"></span> <b>Nota Fiscal e Encalhe com diferença.</b>
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

<div id="dialog-finaliza-conferencia" title="Finalizar Conferência" style="display: none;">
	<fieldset style="width: 410px;">
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
				<input name="lstProdutos" type="text" id="lstProdutos" style="width: 280px;" maxlength="255" />
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
			<td><input type="text" style="width: 80px;" id="exemplaresNovoEncalhe" onchange="ConferenciaEncalheCont.calcularValorTotalNovoEncalhe();" maxlength="255" /></td>
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

<div id="dialog-dadosNotaFiscal" title="Dados da Nota Fiscal" style="display:none;">
	
	<jsp:include page="../messagesDialog.jsp">
		<jsp:param value="idModalDadosNotaFiscal" name="messageDialog"/>
	</jsp:include>
	
	<fieldset>
    	<legend>Nota Fiscal</legend>
        	<table width="830" border="0" cellspacing="1" cellpadding="1" style="color:#666;">
          		<tr>
		            <td width="133">Núm. Nota Fiscal:</td>
		            <td width="307" id="numeroNotaFiscalExibir"></td>
		            <td width="106">Série:</td>
		            <td width="111" id="serieExibir"></td>
          		</tr>
          		<tr>
		            <td>Data:</td>
		            <td id="dataExibir"></td>
		            <td>Valor Total R$:</td>
		            <td id="valorTotalNotaFiscalExibir"></td>
          		</tr>
          		<tr>
		            <td>Chave de Acesso:</td>
		            <td colspan="3" id="chaveAcessoExibir"></td>
          		</tr>
        	</table>
    </fieldset>
	<br clear="all" />
    <br />
    
	<fieldset>
        <legend>Produtos Nota Fiscal</legend>
        <div style="overflow: auto; height: 250px; width: 828px; border: 1px #EEEEEE solid;">
			<table class="pesqProdutosNotaGrid gridTeste" style="width: 811px;" id="dadosGridConferenciaEncalheFinalizar">
				<tr class="header_table">
					<td style="width: 50px; text-align: left;">Código</td>
					<td style="width: 100px; text-align: left;">Produto</td>
					<td style="width: 50px; text-align: center;">Edição</td>
					<td style="width: 70px; text-align: left;">Dia</td>
					<td style="width: 70px; text-align: left;">Qtde. info</td>
					<td style="width: 101px; text-align: left;">Qtde. Recebida</td>
					<td style="width: 80px; text-align: right;" nowrap="nowrap">Preço Capa R$</td>
					<td style="width: 80px; text-align: right;" nowrap="nowrap">Preço Desconto R$</td>
					<td style="width: 60px; text-align: right;">Total R$</td>
					<td style="width: 30px; text-align: center;">Ação</td>
				</tr>
			</table>
		</div>
        
		<table width="800" border="0" cellspacing="1" cellpadding="1">
			<tr>
				<td style="width: 50px;"></td>
				<td style="width: 100px;"></td>
				<td style="width: 50px;"></td>
				<td style="width: 70px;"><strong>Total:</strong></td>
				<td style="width: 70px; text-align: center;" id="somatorioQtdInformada"></td>
				<td style="width: 101px; text-align: center;" id="somatorioQtdRecebida"></td>
				<td style="width: 80px;"></td>
				<td style="width: 80px;"></td>
				<td style="width: 60px; text-align: right;" id="somatorioTotal"></td>
				<td style="width: 30px;"></td>
		  	</tr>
		</table>
	</fieldset>

</div>