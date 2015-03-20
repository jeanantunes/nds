<form id="form-salvar">
	<div id="dialog-salvar" title="Salvar Conferência" style="display: none;">
		
		<div class="message-dialog-encalhe">
			<jsp:include page="../messagesDialog.jsp">
				<jsp:param value="idModalConfirmarSalvarConf" name="messageDialog"/>
			</jsp:include>
		</div>
		
		<fieldset style="width: 410px;">
			<legend>Salvar Conferência</legend>
			<p>Confima a Conferência de Encalhe?</p>
		</fieldset>
	</div>
</form>

<iframe src="" id="download-iframe" style="display:none;"></iframe>

<form id="form-notaFiscal">
	<div id="dialog-notaFiscal" title="Dados da Nota Fiscal" style="display: none;">
	
		<jsp:include page="../messagesDialog.jsp">
			<jsp:param value="dialog-notaFiscal" name="messageDialog"/>
		</jsp:include>
		
		<p>
			<strong>Insira os dados da Nota Fiscal</strong>
		</p>
		
		<table width="670" border="0" cellspacing="1" cellpadding="1">
			<tr>
				<td width="119">Núm. Nota Fiscal:</td>
				<td width="321"><input isEdicao="true" type="text" id="numNotaFiscal" style="width: 200px;" maxlength="255" /></td>
				<td width="106">Série:</td>
				<td width="111"><input isEdicao="true" type="text" id="serieNotaFiscal" style="width: 80px;" maxlength="255" /></td>
			</tr>
			<tr>
				<td>Data:</td>
				<td><input isEdicao="true" type="text" id="dataNotaFiscal" style="width: 80px;" /></td>
				<td>Valor Total R$:</td>
				<td><input isEdicao="true" type="text" id="contingencia-valorNotaFiscal" style="width: 80px; text-align: right;" maxlength="255" /></td>
			</tr>
			
			<tr>
				<td colspan="4">
				
					<div id="divForChaveAcessoNFE">
						<table>
						    <td>
						    	Chave de Acesso:
						    </td>
					    	<td colspan="3">
					    		<input isEdicao="true" type="text" id="chaveAcessoNFE" maxlength="44" style="width:510px;" />
					    	</td>
						</table>
					</div>				
				
				</td>
				
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
</form>

<form id="form-logado">
<div id="dialog-logado" title="Box de Encalhe" style="display:none;">
	
	<jsp:include page="../messagesDialog.jsp">
		<jsp:param value="idModalBoxRecolhimento" name="messageDialog"/>
	</jsp:include>
	
	<fieldset style="width: 410px">
		<legend>Box de Encalhe</legend>
	    <label>Confirma Box de Encalhe: </label>
	    <select name="boxes" style="width:150px;" id="boxLogado">
			<c:forEach var="item" items="${boxes}">
		       <option value="${item.id}">${item.nome}</option>
		    </c:forEach>
	    </select>
	</fieldset>
</div>
</form>

<!--  
<form id="form-alert">
<div id="dialog-alert" title="Nota Fiscal">
	<fieldset style="width: 410px;">
		<legend>Nota Fiscal</legend>
		<p>Existe Nota Fiscal para esta Cota?</p>
	</fieldset>
</div>
</form>
-->

<form id="form-finaliza-conferencia">
<div id="dialog-finaliza-conferencia" title="Finalizar Conferência" style="display: none;">
	<fieldset style="width: 410px;">
		<legend>Conferência Finalizada</legend>
		<p>Confirma a Conferência do Encalhe?</p>
	</fieldset>
</div>
</form>

<form id="form-outros-valores">
<div id="dialog-outros-valores" title="Outros Valores" style="display: none;">
	<fieldset>
		<legend>Outros Valores</legend>
		<table class="outrosVlrsGrid"></table>
	</fieldset>
</div>
</form>

<form id="form-encalhe">
<div id="dialog-encalhe" title="Novo Encalhe" >
	
	<div class="message-dialog-encalhe">
	<jsp:include page="../messagesDialog.jsp">
		<jsp:param value="idModalNovoEncalhe" name="messageDialog"/>
	</jsp:include>
	</div>
	<table width="425" border="0" cellspacing="2" cellpadding="2">
		<tr>
			<td width="125">Produto:</td>
			<td width="286">
				<input isEdicao="true" name="lstProdutos" type="text" id="lstProdutos" style="width: 280px;" maxlength="255" />
			</td>
		</tr>
		<tr>
			<td>Edição:</td>
			<td>
				<input isEdicao="true" type="text" style="width: 80px;" disabled="disabled" id="numEdicaoNovoEncalhe" />
			</td>
		</tr>
		<tr>
			<td>Preço Capa R$:</td>
			<td>
				<input isEdicao="true" type="text" style="width: 80px; text-align: right;" disabled="disabled" id="precoCapaNovoEncalhe" />
			</td>
		</tr>
		<tr>
			<td>Preço Desconto R$:</td>
			<td>
				<input isEdicao="true" type="text" style="width: 80px;text-align: right;" disabled="disabled" id="descontoNovoEncalhe" />
			</td>
		</tr>
		<tr>
			<td>Exemplares:</td>
			<td><input isEdicao="true" type="text" style="width: 80px;" id="exemplaresNovoEncalhe" onchange="ConferenciaEncalheCont.calcularValorTotalNovoEncalhe();" maxlength="255" /></td>
		</tr>
		<tr>
			<td>Valor Total R$</td>
			<td>
				<input isEdicao="true" type="text" style="width: 80px; text-align: right;" disabled="disabled" id="valorTotalNovoEncalhe"/>
			</td>
		</tr>
		<tr>
			<td class="isParcial">Juramentada:</td>
			<td>
				<input isEdicao="true" type="checkbox" name="checkbox" id="checkboxJueramentadaNovoEncalhe" class="isParcial"/>
			</td>
		</tr>
	</table>
	<br />
	
	<span class="bt_add">
		<a isEdicao="true" href="javascript:;" onclick="ConferenciaEncalheCont.adicionarEncalhe(true);">Incluir Novo</a>
	</span>
	
	<br />
</div>
</form>

<form id="form-novo">
<div id="dialog-novo" title="Conferência de Encalhe">
	<p>Confirma o Encalhe?</p>
</div>
</form>

<form id="form-reabertura">
<div id="dialog-reabertura" title="Reabertura" style="display: none;">
	<fieldset style="width: 310px;">
		<legend>Nota Fiscal</legend>
	    <p>Já existe conferencia de encalhe para esta cota.<br/> Efetuar reabertura?</p>
	</fieldset>
</div>
</form>

<form id="form-dadosNotaFiscal">

	<div id="dialog-dadosNotaFiscalContingencia" title="Dados da Nota Fiscal" style="display:none;">
		
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
			            <td id="contingencia-dataExibir"></td>
			            <td>Valor Total R$:</td>
			            <td id="contingencia-valorTotalNotaFiscalExibir"></td>
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
						<td style="width: 60px; text-align: right;" nowrap="nowrap">Preço Desconto R$</td>
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
					<td style="width: 60px; text-align: right;" id="somatorioTotalFinalNfe"></td>
					<td style="width: 30px;"></td>
			  	</tr>
			</table>
		</fieldset>
	
	</div>
</form>

<form id="form-excluir-conferencia">
	<div id="dialog-excluir-conferencia" title="Conferência" style="display: none;">
		<fieldset style="width: 350px;">
			<legend>Excluir Conferência</legend>
		    <p>Confirma a exclusão dessa conferência?</p>
		</fieldset>
	</div>
</form>

<form id="form-confirmar-regerar-cobranca">
	<div id="dialog-confirmar-regerar-cobranca" title="Regerar Cobrança" style="display: none;">
		<fieldset>
			<legend>Regerar cobrança?</legend>
			<p id="msgRegerarCobranca"></p>
		</fieldset>
	</div>
</form>
<form id="form-confirmar">
	<div id="dialog-confirmar" title="Confirma">
		<fieldset>
			<legend>Confirma</legend>
			<p id="msgConfirmar"></p>
		</fieldset>
	</div>
</form>

<form id="form-conferencia-nao-salva">
	<div id="dialog-conferencia-nao-salva" title="Confer&Ecirc;ncia de Encalhe" style="display: none;">
		<fieldset style="width: 400px;">
			<legend>Confer&ecirc;ncia de Encalhe da Cota</legend>
		    <p>Confer&ecirc;ncia de encalhe da cota atual ainda n&atilde;o foi salva.</br> 
		    Deseja realmente fechar esta aba?</p>
		</fieldset>
	</div>
</form>

<form id="form-autenticar-supervisor">
<div id="dialog-autenticar-supervisor" title="Permiss&atilde;o de Supervisor Necess&aacute;ria" style="display:none">
	<fieldset style="width: 360px;">
		<legend id="msgSupervisor"></legend>
		<div align="center">
			<table>
				<tr>
					<td>Usu&aacute;rio:</td>
					
					<input type="password" name="failAutoFill" style="display:none" />
					
					<td>
						<input type="text" id="inputUsuarioSup"/>
					</td>
				</tr>
				<tr>
					<td>Senha:</td>
					<td><input type="password" id="inputSenha" onkeypress="ConferenciaEncalheCont.enterOnSupervisorModal(event)"/>
					</td>
				</tr>
			</table>
		</div>
	</fieldset>
</div>
</form>