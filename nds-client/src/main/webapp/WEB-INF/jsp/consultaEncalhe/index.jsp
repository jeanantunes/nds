<head>

<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/pesquisaCota.js"></script>

<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>

<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/consultaEncalhe.js"></script>

<script language="javascript" type="text/javascript">

var pesquisaCotaConsultaEncalhe = new PesquisaCota(ConsultaEncalhe.workspace);

$(function(){
	ConsultaEncalhe.init();
});

</script>

</head>

<body>
	<div class="areaBts">
		<div class="area">
			<span class="bt_arq">
				<a href="${pageContext.request.contextPath}/devolucao/consultaEncalhe/exportar?fileType=XLS" rel="tipsy"  title="Gerar Arquivo">
					<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
				</a> 
			</span> 
			
			<span class="bt_arq"> 
				<a href="${pageContext.request.contextPath}/devolucao/consultaEncalhe/exportar?fileType=PDF" rel="tipsy" title="Imprimir">
					<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" /> 
				</a>
			</span>

			<span class="bt_arq"> 
				<a href="javascript:;" onclick="ConsultaEncalhe.gerarSlip()" rel="tipsy" title="Reimpress&atilde;o Slip">
					<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" /> 
				</a>
			</span>
		</div>
	</div>
	<div class="linha_separa_fields">&nbsp;</div>
	<fieldset class="fieldFiltro fieldFiltroItensNaoBloqueados">

		<legend> Consulta Encalhe </legend>

		<table  border="0" cellpadding="2" cellspacing="1" class="filtro" style="width:950px;">
		
			<tr>
			
				<td width="46">Per&iacute;odo:</td>
				
				<td width="113">
					<input type="text" value="${data}" id="dataRecolhimentoInicial" class="datePicker" style="width: 80px; float: left; margin-right: 5px;" />
				</td>
				
				<td width="24">At&eacute;:</td>
				
				<td width="113">
					<input type="text" value="${data}" id="dataRecolhimentoFinal" class="datePicker" style="width: 80px; float: left; margin-right: 5px;" />
				</td>
				
				<td width="68">Fornecedor:</td>
				
				<td width="130">
				
					<select name="idFornecedor" id="idFornecedor" style="width:130px;">
					    <option value="-1"  selected="selected">Todos</option>
					    <c:forEach items="${listaFornecedores}" var="fornecedor">
					      		<option value="${fornecedor.key}">${fornecedor.value}</option>	
					    </c:forEach>
				    </select>
				
				</td>
				
				<td width="28">
					Cota:
				</td>
				
				<td width="90">

					<input 	type="text" 
							maxlength="17"
							id="consulta-encalhe-cota" onchange="pesquisaCotaConsultaEncalhe.pesquisarPorNumeroCota('#consulta-encalhe-cota', '#consulta-encalhe-nomeCota');" 
							style="width: 60px; float:left; margin-right:5px;"/>
				
				</td>
				
				<td width="37">Nome:</td>
				
				<td width="138">

		            <input type="text"
		            maxlength="255" 
		            name="consulta-encalhe-nomeCota" 
		            id="consulta-encalhe-nomeCota" 
		            onkeyup="pesquisaCotaConsultaEncalhe.autoCompletarPorNome('#consulta-encalhe-nomeCota');" 
		            onblur="pesquisaCotaConsultaEncalhe.pesquisarPorNomeCota('#consulta-encalhe-cota', '#consulta-encalhe-nomeCota');" 
		            style="width:130px;"/>
					
				</td>
				
				<td width="107">
					<span class="bt_novos">
						<a href="javascript:;" onclick="ConsultaEncalhe.pesquisar()"><img src="${pageContext.request.contextPath}/images/ico_pesquisar.png" border="0" /></a>
					</span>
				</td>
			</tr>
		</table>

	</fieldset>
	
	<div class="linha_separa_fields">&nbsp;</div>

	<fieldset class="fieldGrid">
	
		<legend>Encalhe</legend>
		
		<div class="grids" style="display: none;">
		
			<table id="gridConsultaEncalhe"></table>

			<br clear="all" />

			<table width="950" border="0" cellspacing="1" cellpadding="1">
				<tr>
					<td width="51"><strong>Reparte:</strong></td>
					<td width="85" id="totalReparte"></td>
					<td width="83"><strong> ( - ) Encalhe:</strong></td>
					<td width="87" id="totalEncalhe"></td>
					<td width="126" align="center" bgcolor="#EFEFEF" style="border: 1px solid #000;">
						<strong>( = )Valor Venda Dia:</strong>
					</td>
					<td width="80" align="center" bgcolor="#EFEFEF"	style="border: 1px solid #000;" id="consulta-encalhe-valorVendaDia"></td>
					<td width="130">&nbsp;&nbsp;
						<strong>
							<a href="javascript:;" onclick="ConsultaEncalhe.popupOutrosValores();"> ( + )Outros valores </a>:
						</strong>
					</td>
					<td width="68" id="totalOutrosValores"></td>
					<td width="125"><strong>( = )Valor a pagar R$:</strong></td>
					<td width="77" id="consulta-encalhe-valorAPagar"></td>
					<td width="17">&nbsp;</td>
				</tr>
			</table>
			
			<form name="form_detalhes_encalhe" id="form_detalhes_encalhe">
				<div id="dialog-detalhes-encalhe" title="Consulta Encalhe" style="display:none;">
					<fieldset class="fieldGrid" style="width:600px;">
						<legend>Consulta Encalhe</legend>
						<table width="423" cellspacing="2" cellpadding="2" border="0">
							<tbody>
								<tr>
									<td width="91"><strong>Data Opera&ccedil;&atilde;o:</strong></td>
									<td width="168" id="dataOperacao"></td>
									<td width="42"><strong>C&oacute;digo:</strong></td>
									<td width="96" id="codigoProduto"></td>
								</tr>
								<tr>
									<td><strong>Produto:</strong></td>
									<td id="nomeProduto"></td>
									<td><strong>Edi&ccedil;&atilde;o:</strong></td>
									<td id="edicaoProduto"></td>
								</tr>
							</tbody>
						</table>
					</fieldset>
	
				    <div class="linha_separa_fields" style="width:550px!important;">&nbsp;</div>
					<fieldset  class="fieldGrid" style="width:600px;">
						<table id="dadosDetalheEncalheGrid"></table>
					</fieldset>
				</div>
			</form> 

			<form id="form-outros-valores" name="form-outros-valores">
				<div id="dialog-outros-valores" title="Outros Valores" style="display:none;">
					<fieldset>
				        <legend>Outros Valores</legend>
				        <table id="outrosValoresGrid"></table>
				    </fieldset>
				</div>
			</form>
		</div>
		<form name="form_impressao_slip" id="form_impressao_slip">
			<iframe src="" id="download-iframe" style="display:none;"></iframe>
		</form>

	</fieldset>
</body>