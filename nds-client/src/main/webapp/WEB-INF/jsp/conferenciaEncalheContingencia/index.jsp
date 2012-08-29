<head>
	
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

	<title>Conferencia Encalhe Contingência</title>

	<script type="text/javascript" src='<c:url value="/"/>/scripts/jquery.numeric.js'></script>

	<script type="text/javascript" src='<c:url value="/"/>/scripts/shortcut.js'></script>
	
	<script type="text/javascript" src='<c:url value="/"/>/scripts/scriptConferenciaEncalheCont.js'></script>
	
	<style type="text/css">
		._dados:hover{
			background: none repeat scroll 0 0 #D9EBF5;
		}
	</style>
</head>

<body>
	<jsp:include page="../messagesDialog.jsp">
		<jsp:param value="idTelaConferenciaEncalhe" name="messageDialog"/>
	</jsp:include>

	<jsp:include page="dialogs.jsp" />

	<div class="container">
	
		<fieldset class="classFieldset">
			<legend> Pesquisar Encalhe </legend>
			<table width="950" border="0" cellspacing="2" cellpadding="2" class="filtro">
				<tr>
					<td width="40" height="25">Cota:</td>
					<td width="121">
						<input type="text" id="numeroCota" style="width: 80px; float: left; margin-right: 5px;" maxlength="255" /> 
						<span class="classPesquisar">
							<a href="javascript:;" onclick="ConferenciaEncalheCont.pesquisarCota();">&nbsp;</a>
						</span>
					</td>
					<td colspan="2"><span class="dadosFiltro" id="nomeCota"/></td>
					<td width="44"><span class="dadosFiltro">Status:</span></td>
					<td width="91"><span class="dadosFiltro" id="statusCota"/></td>
					<td width="144"><span class="dadosFiltro">Valor CE Jornaleiro R$:</span></td>
					<td width="100">
						<span class="dadosFiltro">
							<input type="text" id="vlrCE" style="width: 100px; text-align: right;" maxlength="255" />
						</span>
					</td>
				</tr>
			</table>
		</fieldset>
	
		<div class="linha_separa_fields">&nbsp;</div>
	
		<div class="grids" style="display: noneA;">
			<fieldset class="classFieldset">
			<legend>Encalhes Cadastrados</legend>
			
				<table class="conferenciaEncalheContGrid"></table>
				
				<div style="overflow: auto; height: 250px; border: 1px #EEEEEE solid;">
					<table class="conferenciaEncalheGrid" style="width: 941px;" id="dadosGridConferenciaEncalhe">
						<tr class="header_table">
							<td style="width: 70px; text-align: left;">Código</td>
							<td style="width: 150px; text-align: left;">Produto</td>
							<td style="width: 80px; text-align: center;">Edição</td>
							<td style="width: 80px; text-align: center;">Recolhimento</td>
							<td style="width: 80px; text-align: right;">Preço Capa R$</td>
							<td style="width: 70px; text-align: right;">Desconto R$</td>
							<td style="width: 100px; text-align: center;">Exemplares</td>
							<td style="width: 100px; text-align: right;">Total R$</td>
							<td style="width: 70px; text-align: center;">Juramentada</td>
						</tr>
					</table>
				</div>
				
				<br />
				
				<table width="950" border="0" cellspacing="1" cellpadding="1">
					<tr>
						<td width="51"><strong>Reparte:</strong></td>
						<td width="85" id="totalReparte"></td>
						<td width="83"><strong> ( - ) Encalhe:</strong></td>
						<td width="87" id="totalEncalhe"></td>
						<td width="126" align="center" bgcolor="#EFEFEF" style="border: 1px solid #000;">
							<strong>( = )Valor Venda Dia:</strong>
						</td>
						<td width="80" align="center" bgcolor="#EFEFEF" style="border: 1px solid #000;" id="valorVendaDia"></td>
						<td width="130">&nbsp;&nbsp;<strong>
							<a href="javascript:;" onclick="ConferenciaEncalheCont.popup_outros_valores();">( + )Outros valores</a>:</strong></td>
						<td width="68" id="totalOutrosValores"></td>
						<td width="125"><strong>( = )Valor a pagar R$:</strong></td>
						<td width="77" id="valorAPagar"></td>
						<td width="17">&nbsp;</td>
					</tr>
				</table>
			</fieldset>
		</div>
	
		<div class="linha_separa_fields">&nbsp;</div>
	
		<fieldset class="classFieldset">
			<table width="950" height="32" border="0" cellpadding="1" cellspacing="1">
				<tr>
					<td width="126">&nbsp;</td>
					<td width="330">&nbsp;</td>
					<td width="63" align="center" bgcolor="#F4F4F4"><strong>Atalhos:</strong></td>
					<td width="93" bgcolor="#F8F8F8"><strong>F2</strong>-Novo Produto</td>
					<td width="120" bgcolor="#F8F8F8"><strong>F6</strong>-Nova Nota Fiscal</td>
					<td width="137" bgcolor="#F8F8F8"><strong>F8</strong>-Finalizar Conferência</td>
					<td width="62" bgcolor="#F8F8F8"><strong>F9</strong>-Salvar</td>
				</tr>
			</table>
		</fieldset>
		<div class="linha_separa_fields">&nbsp;</div>
	
	</div>
</body>
