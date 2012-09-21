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

	<br clear="all" />
	
	<br />

	<div class="container">

		<fieldset class="classFieldset">
			
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
					
					
						<select name="situacaoNfe" id="situacaoNfe" style="width:290px;">
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
							<a href="javascript:;" onclick="PainelMonitorNFE.pesquisar();">Pesquisar</a>
						</span>
					</td>
				</tr>
			</table>
		
		</fieldset>
		
		<div class="linha_separa_fields">&nbsp;</div>

		<fieldset class="classFieldset">
			<legend>NF-e</legend>
			<div class="grids" style="display: none;">
				<table id="nfeGrid"></table>
				<!--<span class="bt_novos" title="Gerar Arquivo XML"><a href="javascript:;"><img src="../images/ico_xml.gif" hspace="5" border="0" />XML</a></span>-->
				
				<span class="bt_novos" title="Gerar Arquivo">
					<a href="${pageContext.request.contextPath}/nfe/painelMonitorNFe/exportar?fileType=XLS">
						<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
						Arquivo
					</a> 
				</span> 
				
				<span class="bt_novos" title="Imprimir"> 
					<a href="${pageContext.request.contextPath}/nfe/painelMonitorNFe/exportar?fileType=PDF">
						<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" /> 
						Imprimir 
					</a>
				</span>				

				<span class="bt_novos" title="Cancelar NF-e">
					<a onclick="PainelMonitorNFE.cancelarNfe()" href="javascript:;">
						<img 	src="${pageContext.request.contextPath}/images/ico_bloquear.gif" 
							alt="Cancelar NF-e" width="16" 
							height="16" 
							hspace="5" border="0">
						Cancelar NF-e
					</a>
				</span>
				
				<span  class="bt_novos" title="Emitir em DEPEC">
					<a onclick="PainelMonitorNFE.imprimirDanfes(true)" href="javascript:;">
						<img 	src="${pageContext.request.contextPath}/images/bt_expedicao.png" 	
						     	alt="Emitir em DEPEC" 
							hspace="5" border="0">
						Emitir em DEPEC
					</a>
				</span>
				
				<span class="bt_novos" title="Imprimir Seleção">
					<a 	onclick="PainelMonitorNFE.imprimirDanfes(false)" href="javascript:;">
						<img 	src="${pageContext.request.contextPath}/images/ico_impressora.gif"
								alt="Imprimir Seleção" 
								hspace="5" 
								border="0" />
						Imprimir Seleção
					</a>
				</span> 
				
				<span class="bt_sellAll" style="float: right;">
					<label for="sel">Selecionar Todos</label>
					<input 	type="checkbox" id="sel" name="Todos" 
							onclick="PainelMonitorNFE.checkAll(this);"
							style="float: left; margin-right: 25px;" />
				</span>
				
			</div>


		</fieldset>
		
		<div class="linha_separa_fields">&nbsp;</div>

	</div>


</body>