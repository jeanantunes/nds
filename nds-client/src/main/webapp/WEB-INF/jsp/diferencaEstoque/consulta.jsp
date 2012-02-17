<body>
	<div id="effect" style="padding: 0 .7em;"
		class="ui-state-highlight ui-corner-all">
		<p>
			<span style="float: left; margin-right: .3em;"
				class="ui-icon ui-icon-info"></span> <b>Lançamento de Faltas e
				Sobras < evento > com < status >.</b>
		</p>
	</div>

	<fieldset class="classFieldset">
		<legend>Pesquisar Faltas e Sobras</legend>
		<table width="950" border="0" cellpadding="2" cellspacing="1"
			class="filtro">
			<tr>
				<td width="59">Código:</td>
				<td colspan="3"><input type="text" name="textfield9"
					id="textfield9"
					style="width: 80px; float: left; margin-right: 5px;" /><span
					class="classPesquisar" title="Pesquisar Produto"><a
						href="javascript:;">&nbsp;</a>
				</span>
				</td>
				<td width="60">Produto:</td>
				<td width="293"><input type="text" name="textfield8"
					id="textfield8" style="width: 272px;" />
				</td>
				<td width="73">Fornecedor:</td>
				<td width="312" colspan="2"><select name="select8"
					id="select13" style="width: 280px;">
						<option selected="selected"></option>
						<option>Todos</option>
						<option>Dinap</option>
						<option>FC</option>
				</select>
				</td>
			</tr>
		</table>
		<table width="950" border="0" cellspacing="2" cellpadding="2"
			class="filtro">
			<tr>
				<td width="178">Período de Data Lançamento:</td>
				<td width="108"><input type="text" name="datepickerDe"
					id="datepickerDe" style="width: 80px;" />
				</td>
				<td width="33" align="center">Até</td>
				<td width="147"><input type="text" name="datepickerAte"
					id="datepickerAte" style="width: 80px;" />
				</td>
				<td width="134" align="right">Tipo de Difernça:</td>
				<td width="169"><select name="select9" id="select14"
					style="width: 120px;">
						<option selected="selected"></option>
						<option>Falta de</option>
						<option>Sobra de</option>
						<option>Falta em</option>
						<option>Sobra em</option>
				</select>
				</td>
				<td width="137"><span class="bt_pesquisar"><a
						href="javascript:;" onclick="mostrar();">Pesquisar</a>
				</span>
				</td>
			</tr>
		</table>
	</fieldset>
	<div class="linha_separa_fields">&nbsp;</div>

	<fieldset class="classFieldset">
		<legend>Faltas e Sobras Cadastradas</legend>
		<div class="grids" style="display: none;">
			<table class="consultaFaltasSobrasGrid"></table>
			<span class="bt_novos" title="Gerar Arquivo"><a
				href="javascript:;"><img src="../images/ico_excel.png"
					hspace="5" border="0" />Arquivo</a>
			</span> <span class="bt_novos" title="Imprimir"><a
				href="javascript:;"><img src="../images/ico_impressora.gif"
					alt="Imprimir" hspace="5" border="0" />Imprimir</a>
			</span>
		</div>

	</fieldset>
	<div class="linha_separa_fields">&nbsp;</div>

	<script>
		$(".consultaFaltasSobrasGrid").flexigrid({
			url : '../xml/consulta_faltas_sobras-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'Data',
				name : 'data',
				width : 90,
				sortable : true,
				align : 'center'
			}, {
				display : 'Código',
				name : 'codigo',
				width : 70,
				sortable : true,
				align : 'center'
			}, {
				display : 'Produto',
				name : 'produto',
				width : 170,
				sortable : true,
				align : 'left'
			}, {
				display : 'Edição',
				name : 'edicao',
				width : 80,
				sortable : true,
				align : 'center'
			}, {
				display : 'Tipo de Diferença',
				name : 'tipoDiferenca',
				width : 180,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nota',
				name : 'nota',
				width : 90,
				sortable : true,
				align : 'center'
			}, {
				display : 'Exemplar',
				name : 'exemplar',
				width : 80,
				sortable : true,
				align : 'center'
			}, {
				display : 'Status',
				name : 'status',
				width : 80,
				sortable : true,
				align : 'center'
			} ],
			sortname : "data",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 180
		});
	</script>
</body>