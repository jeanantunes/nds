<html>
<input id="permissaoAlteracao" type="hidden" value="${permissaoAlteracao}">
<head>

<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/scripts/jquery-upload/css/jquery.fileupload-ui.css">
<style type="text/css">
#dialog-imprimir {
	display: none;
}

#dialog-imprimir fieldset {
	width: 900px !important;
}

fieldset label {
	margin: 0px  !important;
	line-height: 0px;
}
</style>
<script src="${pageContext.request.contextPath}/scripts/jquery.form.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/scripts/jquery-ui-1.8.16.custom/development-bundle/ui/jquery.ui.button.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/scripts/jquery-upload/js/vendor/jquery.ui.widget.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/scripts/jquery-upload/js/jquery.iframe-transport.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/scripts/jquery-upload/js/jquery.fileupload.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/scripts/informeEncalhe.js" type="text/javascript"></script>
<script type="text/javascript">
	$(function() {
		var informeEncalhe = new InformeEncalhe();
	});
</script>
</head>
<body>
	<form id="form-imprimir">
	<div id="dialog-imprimir" title="Imprimir Informe de Encalhe">
			<input type="hidden" name="idFornecedor"/>
			<input type="hidden" name="somenteVisualizarImpressao"/>
			<input type="hidden" name="semanaRecolhimento"/>
			<input type="hidden" name="dataRecolhimento"/>
			<input type="hidden" name="sortname" value="nomeProduto"/>
			<input type="hidden" name="sortorder" value="asc"/>
			
			<fieldset>
				<legend>Tipo de Impress&atilde;o</legend>
				<div id="buttonsetTipoImpressaoCapas">
					<input type="radio" id="radioTipoImpressaoCapasNAO" name="tipoImpressao.capas" value="NAO" checked="checked" />
					<label for="radioTipoImpressaoCapasNAO">Sem Capas</label> 
					<input type="radio" id="radioTipoImpressaoCapasFIM" name="tipoImpressao.capas" value="FIM" />
					<label for="radioTipoImpressaoCapasFIM">Capas no Fim</label>
					<input type="radio" id="radioTipoImpressaoCapasPAR" name="tipoImpressao.capas" value="PAR" />
					<label for="radioTipoImpressaoCapasPAR">Capas nas Paginas Pares</label>
				</div>
			</fieldset>
			<br clear="all" /> <br />
			<fieldset>
				<legend>Colunas</legend>
				<table width="825" border="0" cellspacing="0" cellpadding="0" id="colunasTable">
					<tr>
						
						<td width="27">
							<input name="tipoImpressao.colunas[]" id="tipoImpressao.colunas.sequenciaMatriz" type="checkbox" value="sequenciaMatriz" />
						</td>
						<td width="183">
							<label for="tipoImpressao.colunas.sequenciaMatriz">Sequ&ecirc;ncia</label>
						</td>
						<td width="27">
							<input name="tipoImpressao.colunas[]" id="tipoImpressao.colunas.codigoProduto" type="checkbox" value="codigoProduto" />
						</td>
						<td width="183">
							<label for="tipoImpressao.colunas.codigoProduto">C&oacute;digo</label>
						</td>
						<td width="21">
							<input name="tipoImpressao.colunas[]" id="tipoImpressao.colunas.nomeProduto" type="checkbox" value="nomeProduto" />
						</td>
						<td width="105">
							<label for="tipoImpressao.colunas.nomeProduto">Produto</label>
						</td>
						<td width="23">
							<input name="tipoImpressao.colunas[]" id="tipoImpressao.colunas.numeroEdicao" type="checkbox" value="numeroEdicao" />
						</td>
						<td width="101">
							<label for="tipoImpressao.colunas.numeroEdicao">Edi&ccedil;&atilde;o</label>
						</td>
						<td width="23">
							<input name="tipoImpressao.colunas[]" id="tipoImpressao.colunas.chamadaCapa" type="checkbox" value="chamadaCapa" />
						</td>
						<td width="200"><label for="tipoImpressao.colunas.chamadaCapa">Chamada de Capa</label></td>					
					</tr>
					<tr>
						
						<td><input name="tipoImpressao.colunas[]" id="tipoImpressao.colunas.codigoDeBarras" type="checkbox" value="codigoDeBarras" /></td>
						<td><label for="tipoImpressao.colunas.codigoDeBarras">C&oacute;digo Barras</label></td>
						
						<td><input name="tipoImpressao.colunas[]" id="tipoImpressao.colunas.precoVenda" type="checkbox" value="precoVenda" /></td>
						<td><label for="tipoImpressao.colunas.precoVenda">Pre&ccedil;o de Capa</label></td>
						<td><input name="tipoImpressao.colunas[]" id="tipoImpressao.colunas.nomeEditor" type="checkbox" value="nomeEditor" /></td>
						<td><label for="tipoImpressao.colunas.nomeEditor">Editor</label></td>
						<td><input name="tipoImpressao.colunas[]" id="tipoImpressao.colunas.brinde" type="checkbox" value="brinde" /></td>
						<td><label for="tipoImpressao.colunas.brinde">Brinde</label></td>
						<td>
							<input name="tipoImpressao.colunas[]" id="tipoImpressao.colunas.dataLancamento" type="checkbox" value="dataLancamento" />
						</td>
						<td>
							<label for="tipoImpressao.colunas.dataLancamento">Data Lan&ccedil;amento</label>
						</td>
					</tr>
					<tr>
						<td>
							<input name="tipoImpressao.colunas[]" id="tipoImpressao.colunas.dataRecolhimento" type="checkbox" value="dataRecolhimento" />
						</td>
						<td>
							<label for="tipoImpressao.colunas.dataRecolhimento">Data Recolhimento</label>
						</td>
						<td>
							<input name="tipoImpressao.colunas[]" id="tipoImpressao.colunas.tipoLancamentoParcial" type="checkbox" value="tipoLancamentoParcial" />
						</td>
						<td>
							<label for="tipoImpressao.colunas.tipoLancamentoParcial">Recolhimento Parcial/Final</label>
						</td>
						
						<td>
							<input name="tipoImpressao.colunas[]" id="tipoImpressao.colunas.pacotePadrao" type="checkbox" value="pacotePadrao" />
						</td>			
						<td>
							<label for="tipoImpressao.colunas.pacotePadrao">Pacote Padrao</label>
						</td>
					</tr>
				</table>
			</fieldset>
	</div>
	<div class="areaBts">
		<div class="area">
			<span class="bt_arq"><a href="javascript:;"
				id="btnImprimir" rel="tipsy" title="Imprimir"><img
					src="${pageContext.request.contextPath}/images/ico_impressora.gif"
					hspace="5" border="0" alt="" /></a>
			</span>
		</div>
	</div>
	<div class="linha_separa_fields">&nbsp;</div>
	<fieldset class="fieldFiltro fieldFiltroItensNaoBloqueados">
		<legend> Pesquisar Informe de Recolhimento</legend>
		<table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
			<tr>
				<td width="74">Fornecedor:</td>
				<td width="210"><select name="select" id="idFornecdorSelect"
					style="width: 200px;">
						<option value="" selected="selected">Todos</option>
						<c:forEach items="${fornecedores}" var="fornecedor">
							<option value="${fornecedor.key }">${fornecedor.value }</option>
						</c:forEach>
				</select></td>

				<td colspan="3">Semana:</td>

				<td width="100">
					<input type="text" id="semanaRecolhimentoBox"
						style="width: 70px;" />
				</td>
				
				<td colspan="3">Sugerir Semana:</td>

				<td width="50">
					<input type="checkbox" id="sugerirSemana" />
				</td>
				
				<td colspan="3">Data Recolhimento:</td>
				
				<td width="150">
					<input type="text" id="dataRecolhimentoBox" style="width: 70px;" />
				</td>

				<td width="105">
					<span class="bt_novos">
						<a href="javascript:;" id="btnPesquisar">
							<img src="${pageContext.request.contextPath}/images/ico_pesquisar.png" border="0" />
						</a>
					</span>
				</td>

			</tr>
		</table>
	</fieldset>

	<div class="linha_separa_fields">&nbsp;</div>
	<div class="grids" style="display: none;">
		<fieldset class="fieldGrid">
			<legend>Informe de Recolhimentos Cadastrados</legend>
			<div id="consultaInformeEncalheGrid"></div>		
		</fieldset>
	</div>
</body>
</html>