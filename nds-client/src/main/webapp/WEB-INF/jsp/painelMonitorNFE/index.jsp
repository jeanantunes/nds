<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<title>Painel Monitor NFe</title>

<script language="javascript" type="text/javascript" src='<c:url value="/"/>/scripts/jquery.numeric.js'></script>

<script type="text/javascript">

	$(".nfeGrid").flexigrid({
		url : '../xml/nfe-xml.xml',
		dataType : 'xml',
		colModel : [ {
			display : 'Nota',
			name : 'nota',
			width : 55,
			sortable : true,
			align : 'left'
		}, {
			display : 'Série',
			name : 'serie',
			width : 40,
			sortable : true,
			align : 'center'
		}, {
			display : 'Emissão',
			name : 'emissao',
			width : 60,
			sortable : true,
			align : 'center'
		}, {
			display : 'Tipo Emissão',
			name : 'tipoemissao',
			width : 70,
			sortable : true,
			align : 'left'
		}, {
			display : 'CNPJ Emissor',
			name : 'cnpjEmissor',
			width : 100,
			sortable : true,
			align : 'left'
		}, {
			display : 'CNPJ Destinatário',
			name : 'cnpjDestinatario',
			width : 100,
			sortable : true,
			align : 'left'
		}, {
			display : 'Status NF-e',
			name : 'statusNfe',
			width : 100,
			sortable : true,
			align : 'left'
		}, {
			display : 'Tipo NF-e',
			name : 'tipoNfe',
			width : 80,
			sortable : true,
			align : 'left'
		}, {
			display : 'Movimento Integração',
			name : 'movimentoIntegrador',
			width : 140,
			sortable : true,
			align : 'left'
		}, {
			display : ' ',
			name : 'imprimirLinha',
			width : 30,
			sortable : true,
			align : 'center'
		}, {
			display : ' ',
			name : 'sel',
			width : 30,
			sortable : true,
			align : 'center'
		} ],
		sortname : "codigo",
		sortorder : "asc",
		usepager : true,
		useRp : true,
		rp : 15,
		showTableToggleBtn : true,
		width : 960,
		height : 180
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
				
					<td width="94">CNPJ:</td>

					<td width="129"><input type="text" style="width: 120px;" />

					</td>

					<td width="68">Período de:</td>

					<td width="107"><input name="datepickerDe" type="text"
						id="datepickerDe" style="width: 80px;" />
					</td>

					<td width="29">Até:</td>

					<td width="107"><input name="datepickerAte" type="text"
						id="datepickerAte" style="width: 80px;" />
					</td>

					<td colspan="3">Destinatário:</td>

					<td width="135">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td width="15%"><input type="radio" name="radio" id="cpf"
									value="radio" />
								</td>
								<td width="34%"><label for="cpf">CPF</label>
								</td>
								<td width="15%"><input type="radio" name="radio" id="cnpj"
									value="radio" />
								</td>
								<td width="36%"><label for="cnpj">CNPJ</label>
								</td>
							</tr>
						</table>
					</td>
					<td width="160"><input type="text" style="width: 160px;" />
				</tr>
				<tr>
					<td>Tipo de Nf-e:</td>
					<td><select name="select" id="select" style="width: 120px;">
							<option selected="selected"></option>
							<option>Todos</option>
							<option>Entrada</option>
							<option>Saída</option>
					</select>
					</td>
					<td>Número:</td>
					<td><input type="text" style="width: 80px;" />
					</td>
					<td>Até:</td>
					<td><input type="text" style="width: 80px;" />
					</td>
					<td colspan="3">&nbsp;</td>
					<td>Chave de Acesso NF-e:</td>
					<td><input type="text" style="width: 160px;" />
				</tr>
				<tr>
					<td>Situação NF-e:</td>
					<td colspan="3"><select name="select2" id="select2"
						style="width: 290px;">
							<option></option>
							<option>Aguardando Processamento</option>
							<option>Em Processamento</option>
							<option>Processamento Rejeitado</option>
							<option>Aguardando Ação do Usuário</option>
							<option>NF-e Autorizada</option>
							<option>NF-e Rejeitada</option>
							<option>NF-e Denegada</option>
					</select>
					</td>
					<td>Box:</td>
					<td><input name="box" type="text" id="box"
						style="width: 80px;" />
					</td>
					<td colspan="3">&nbsp;</td>
					<td>&nbsp;</td>
					<td><span class="bt_pesquisar"><a href="javascript:;"
							onclick="mostrar();">Pesquisar</a>
					</span>
					</td>
				</tr>
			</table>
		
		</fieldset>
		
		<div class="linha_separa_fields">&nbsp;</div>

		<fieldset class="classFieldset">
			<legend>NF-e</legend>
			<div class="grids" style="display: none;">
				<table class="nfeGrid"></table>
				<!--<span class="bt_novos" title="Gerar Arquivo XML"><a href="javascript:;"><img src="../images/ico_xml.gif" hspace="5" border="0" />XML</a></span>-->
				<span class="bt_novos" title="Gerar Arquivo"><a
					href="javascript:;"><img src="../images/ico_excel.png"
						hspace="5" border="0" />Arquivo</a>
				</span> <span class="bt_novos" title="Imprimir"><a
					href="javascript:;"><img src="../images/ico_impressora.gif"
						alt="Imprimir" hspace="5" border="0" />Imprimir</a>
				</span> <span class="bt_novos" title="Imprimir Seleção"><a
					href="javascript:;"><img src="../images/ico_impressora.gif"
						alt="Imprimir Seleção" hspace="5" border="0" />Imprimir Seleção</a>
				</span> <span class="bt_sellAll" style="float: right;"><label
					for="sel">Selecionar Todos</label><input type="checkbox" id="sel"
					name="Todos" onclick="checkAll();"
					style="float: left; margin-right: 25px;" />
				</span>
			</div>


		</fieldset>
		
		<div class="linha_separa_fields">&nbsp;</div>

	</div>


</body>