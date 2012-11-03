<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<title>Painel Monitor NFe</title>

<script language="javascript" type="text/javascript" src='${pageContext.request.contextPath}/scripts/jquery.numeric.js'></script>
<script language="javascript" type="text/javascript" src='${pageContext.request.contextPath}/scripts/painelMonitorNFE.js'></script>
<script type="text/javascript">

$(function() {
	PainelMonitorNFE.init();
});
	
</script>

</head>

<body>
<div class="areaBts">
	<div class="area">
		<span class="bt_novos">
			<a onclick="PainelMonitorNFE.cancelarNfe()" href="javascript:;" rel="tipsy" title="Cancelar NF-e">
				<img 	src="${pageContext.request.contextPath}/images/ico_bloquear.gif" 
					alt="Cancelar NF-e" width="16" 
					height="16" 
					hspace="5" border="0">
			</a>
		</span>
		
		<span  class="bt_novos">
			<a onclick="PainelMonitorNFE.imprimirDanfes(true)" href="javascript:;" rel="tipsy" title="Emitir em DEPEC">
				<img 	src="${pageContext.request.contextPath}/images/bt_expedicao.png" 	
				     	alt="Emitir em DEPEC" 
					hspace="5" border="0">
			</a>
		</span>
		
		<span class="bt_novos">
			<a 	onclick="PainelMonitorNFE.imprimirDanfes(false)" href="javascript:;" rel="tipsy" title="Imprimir Seleção">
				<img 	src="${pageContext.request.contextPath}/images/ico_impressora.gif"
						alt="Imprimir Seleção" 
						hspace="5" 
						border="0" />
			</a>
		</span>
				
		<span class="bt_arq">
			<a href="${pageContext.request.contextPath}/nfe/painelMonitorNFe/exportar?fileType=XLS" rel="tipsy" title="Gerar Arquivo">
				<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
			</a> 
		</span> 
		
		<span class="bt_arq"> 
			<a href="${pageContext.request.contextPath}/nfe/painelMonitorNFe/exportar?fileType=PDF" rel="tipsy" title="Imprimir">
				<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" /> 
			</a>
		</span>	
	</div>
</div>
<div class="linha_separa_fields">&nbsp;</div>
<fieldset class="fieldFiltro">
	
	<legend> Painel Monitor NF-e</legend>
	
	<table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
	
		<tr>
		
			<td width="94">Box:</td>

			<td width="129">
			
				<input type="text" id="box" style="width: 80px;" />

			</td>

			<td width="68">Período de:</td>

			<td width="107"><input type="text"
				id="dataInicial" style="width: 80px;" />
			</td>

			<td width="29">Até:</td>

			<td width="107"><input type="text"
				id="dataFinal" style="width: 80px;" />
			</td>

			<td colspan="3">Destinatário:</td>

			<td width="135">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td width="15%">
							<input type="radio" name="radioTipoDoc" value="cpf" />
						</td>
						<td width="34%">
							<label for="cpf">CPF</label>
						</td>
						
						<td width="15%">
							<input type="radio" name="radioTipoDoc" checked="checked" value="cnpj" />
						</td>
						
						<td width="36%">
							<label for="cnpj">CNPJ</label>
						</td>
					</tr>
				</table>
			</td>
			<td width="160">
				<input type="text" id="documento" style="width: 160px;" />
			
		</tr>
		<tr>
			<td>Tipo de Nf-e:</td>
			<td>	
				<select name="tipoNfe" id="tipoNfe" style="width: 120px;">
					<option selected="selected"></option>
				    <c:forEach items="${comboTipoNfe}" var="comboTipoNfe">
				      		<option value="${comboTipoNfe.key}">${comboTipoNfe.value}</option>	
				    </c:forEach>
				</select>
			</td>
			<td>Número:</td>
			<td>
				<input type="text" id="numeroInicial" style="width: 80px;" />
			</td>
			<td>Até:</td>
			<td>
				<input type="text" id="numeroFinal" style="width: 80px;" />
			</td>
			<td colspan="3">&nbsp;</td>
			<td>
				Chave de Acesso NF-e:
			</td>
			<td>
				<input type="text" id="chaveAcesso" style="width: 160px;" />
		</tr>
		<tr>
			<td>Situação NF-e:</td>
			<td colspan="3">
			
			
				<select name="situacaoNfe" id="situacaoNfe" style="width:285px;">
				    <option value=""  selected="selected"></option>
				    <c:forEach items="${comboStatusNfe}" var="statusNfe">
				      		<option value="${statusNfe.key}">${statusNfe.value}</option>	
				    </c:forEach>
			    </select>
			</td>
			<td>Série</td>
			<td><input type="text" id="serieNfe" style="width: 80px;" /></td>
			<td colspan="3">&nbsp;</td>
			<td>&nbsp;</td>
			<td>
				<span class="bt_pesquisar">
					<a href="javascript:;" onclick="PainelMonitorNFE.pesquisar();"><img src="${pageContext.request.contextPath}/images/ico_pesquisar.png" border="0" /></a>
				</span>
			</td>
		</tr>
	</table>

</fieldset>

<div class="linha_separa_fields">&nbsp;</div>

<fieldset class="fieldGrid">
	<legend>NF-e</legend>
	<div class="grids" style="display: none;">
		<table id="nfeGrid"></table>
		<!--<span class="bt_novos" title="Gerar Arquivo XML"><a href="javascript:;"><img src="../images/ico_xml.gif" hspace="5" border="0" />XML</a></span>-->
		
		<span class="bt_sellAll" style="float: right;">
			<label for="sel">Selecionar Todos</label>
			<input 	type="checkbox" id="sel" name="Todos" 
					onclick="PainelMonitorNFE.checkAll(this);"
					style="float: left; margin-right: 25px;" />
		</span>
		
	</div>


</fieldset>
</body>