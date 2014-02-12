<input id="permissaoAlteracao" type="hidden" value="${permissaoAlteracao}">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<title>Painel Monitor NFe</title>

<script language="javascript" type="text/javascript" src='${pageContext.request.contextPath}/scripts/jquery.numeric.js'></script>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jquery.multiselect.css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.multiselect.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.multiselect.br.js"></script>
<script language="javascript" type="text/javascript" src='${pageContext.request.contextPath}/scripts//utils.js'></script>
<script language="javascript" type="text/javascript" src='${pageContext.request.contextPath}/scripts/painelMonitorNFE.js'></script>

<script type="text/javascript">

$(function() {
	PainelMonitorNFE.init();
	bloquearItensEdicao(PainelMonitorNFE.workspace);
});
	
</script>

</head>

<body>
<div class="areaBts">
	<div class="area">
		<span class="bt_novos">
			<a isEdicao="true" onclick="PainelMonitorNFE.cancelarNfe()" href="javascript:;" rel="tipsy" title="Cancelar NF-e">
				<img 	src="${pageContext.request.contextPath}/images/ico_bloquear.gif" 
					alt="Cancelar NF-e" width="16" 
					height="16" 
					hspace="5" border="0">
			</a>
		</span>
		
		<span  class="bt_novos">
			<a isEdicao="true" onclick="PainelMonitorNFE.imprimirDanfes(true)" href="javascript:;" rel="tipsy" title="Emitir em DEPEC">
				<img 	src="${pageContext.request.contextPath}/images/bt_expedicao.png" 	
				     	alt="Emitir em DEPEC" 
					hspace="5" border="0">
			</a>
		</span>
		
		<span class="bt_novos">
			<a  isEdicao="true" onclick="PainelMonitorNFE.imprimirDanfes(false)" href="javascript:;" rel="tipsy" title="Imprimir Seleção">
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
			<a  isEdicao="true" onclick="PainelMonitorNFE.imprimirDanfes(false)" href="javascript:;" rel="tipsy" title="Imprimir Sele&ccedil;&atilde;o">
				<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" alt="Imprimir Sele&ccedil;&atilde;o" hspace="5" border="0" />
			</a>
		</span>	
	</div>
</div>
<div class="linha_separa_fields">&nbsp;</div>
<fieldset class="fieldFiltro">
	
	<legend> Painel Monitor NF-e</legend>
	
	<table width="950" border="1" cellpadding="2" cellspacing="1" class="filtro">
		<tr>
			<td width="150">
				Destinat&aacute;rio:
			</td>
 			<td width="210">
				<c:forEach items="${tiposDestinatarios}" var="tipoDestinatario" varStatus="status" >
					<input type="radio" name="tipoDestinatario" id="tipoDestinatario${status.index}" value="${tipoDestinatario}" <c:if test="${status.index == 0}">checked="checked"</c:if> onchange="PainelMonitorNFE.verificarTipoDestinatario(this);" /> ${tipoDestinatario.descricao}
				</c:forEach>
			</td>
			<td>
				<select id="painelNfe-filtro-selectFornecedoresDestinatarios" name="selectFornecedores" multiple="multiple" style="width:250px">
					<c:forEach items="${fornecedoresDestinatarios}" var="fornecedor">
						<option value="${fornecedor.key }">${fornecedor.value }</option>
					</c:forEach>
				</select>
			</td>
			<td width="100">Destinatário:
				<table width="100%" border="1" cellspacing="0" cellpadding="0">
					<tr>
						<td width="15%">
							<input type="radio" name="impresaoNfe-radioTipoDoc" value="cpf" onchange="PainelMonitorNFE.verificarRadioCnpjCpf()"/>
						</td>
						<td width="34%">
							<label for="cpf">CPF</label>
						</td>
						
						<td width="15%">
							<input type="radio" name="impresaoNfe-radioTipoDoc" checked="checked" value="cnpj" onchange="PainelMonitorNFE.verificarRadioCnpjCpf()"/>
						</td>
						
						<td width="36%">
							<label for="cnpj">CNPJ</label>
						</td>
					</tr>
				</table>
			</td>
			<td width="80">
				<input type="text" id="impresaoNfe-documento" style="width: 160px;" />
			</td>
		</tr>
		<tr>
			<td width="180">Nat. de Opera&ccedil;&atilde;o:</td>
  				<td width="204">
				<select id="painelNfe-filtro-naturezaOperacao" name="naturezaOperacao" style="width:200px; font-size:11px!important" title="">
					<option value="">Todos</option>
				</select>
			</td>
			
			<td width="68" colspan="4">Período de:
				<input type="text" id="dataInicial" style="width: 80px;" />&nbsp;&nbsp;At&eacute;&nbsp;
				<input type="text" id="dataFinal" style="width: 80px;" />
			</td>
			
		</tr>
		<tr>
						
		</tr>
		<tr>
			<td>Tipo:</td>
			<td>	
				<select name="tipoNfe" id="tipoNfe" style="width: 120px;">
					<option value="" selected="selected">Todos</option>
				    <c:forEach items="${comboTipoNfe}" var="comboTipoNfe">
				      		<option value="${comboTipoNfe.key}">${comboTipoNfe.value}</option>	
				    </c:forEach>
				</select>
			</td>
			<td>N&uacute;mero:
				<input type="text" id="numeroInicial" style="width: 80px;" />At&eacute;:
				<input type="text" id="numeroFinal" style="width: 80px;" />
			</td>
			<td>
				Chave de Acesso NF-e:
				<input type="text" id="chaveAcesso" style="width: 160px;" />
			</td>
		</tr>
		<tr>
			<td>Status:</td>
			<td>
				<select name="situacaoNfe" id="situacaoNfe" style="width:250px;">
				    <option value=""  selected="selected"></option>
				    <c:forEach items="${comboStatusNfe}" var="statusNfe">
				      		<option value="${statusNfe.key}">${statusNfe.value}</option>	
				    </c:forEach>
			    </select>
			</td>
			<td width="94">Box:
				<input type="text" id="box" style="width: 80px;" />
			</td>
			<td width="85">S&eacute;rie:
				<input type="text" id="serieNfe" style="width: 100px;" />
			</td>
			<td>
				<span class="bt_pesquisar">
					<a href="javascript:;" onclick="PainelMonitorNFE.pesquisar();"><b> Pesquisar </b></a>
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
			<input isEdicao="true" 	type="checkbox" id="sel" name="Todos" 
					onclick="PainelMonitorNFE.checkAll(this);"
					style="float: left; margin-right: 25px;" />
		</span>
		
	</div>


</fieldset>
</body>