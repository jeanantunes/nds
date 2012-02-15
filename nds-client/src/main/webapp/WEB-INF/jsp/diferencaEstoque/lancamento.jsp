<body>
	<div class="corpo">
	
		<div class="container">

			<fieldset class="classFieldset">
			
				<legend>Lançamento Faltas e Sobras</legend>
				
				<table width="950" border="0" cellpadding="2" cellspacing="1"
					class="filtro">
					<tr>
						<td width="111">Data Movimento:</td>
						<td width="124">
							<input type="text" 
								   name="datepickerDe" 
								   id="datepickerDe" 
								   style="width: 70px; float: left; margin-right: 5px;" />
						</td>
						<td width="115">Tipo de Diferença:</td>
						<td width="294">
							<select name="select" id="select" style="width: 220px;"></select>
						</td>
						<td width="280">
							<span class="bt_pesquisar">
								<a href="javascript:;" onclick="mostrar_1();">Pesquisar</a>
							</span>
						</td>
					</tr>
				</table>
			</fieldset>
			
			<div class="linha_separa_fields">&nbsp;</div>

			<fieldset class="classFieldset">
			
				<legend>Lançamento Faltas e Sobras</legend>
				
				<div class="grids" style="display: none;">
					<table class="lanctoFaltasSobrasGrid"></table>
				</div>
				
				<table width="931" border="0" cellspacing="1" cellpadding="1">
					<tr>
						<td width="282">
							<span class="bt_novo">
								<a href="javascript:;" onclick="popup_novoProduto();">Novo</a>
							</span>
							<span class="total bt_confirmar">
								<a href="javascript:;" onclick="popup();">Confirmar</a>
							</span>
						</td>
						<td width="102" class="total"><strong>Total Geral:</strong></td>
						<td width="189" class="total">R$ 999.999,99</td>
						<td width="144" align="center" class="total">980</td>
						<td width="198" class="total">&nbsp;</td>
					</tr>
				</table>
			</fieldset>
			
			<div class="linha_separa_fields">&nbsp;</div>
		</div>
	</div>
	
	<script>
		$(".lanctoFaltasSobrasGrid").flexigrid({
			url : '../xml/lancamento_faltas_sobras-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'Código',
				name : 'codigo',
				width : 60,
				sortable : true,
				align : 'left'
			},{
				display : 'Produto',
				name : 'produto',
				width : 190,
				sortable : true,
				align : 'left'
			}, {
				display : 'Edição',
				name : 'edicao',
				width : 90,
				sortable : true,
				align : 'center'
			}, {
				display : 'Preço R$',
				name : 'preco',
				width : 90,
				sortable : true,
				align : 'right'
			}, {
				display : 'Pacote Padrão',
				name : 'pacote',
				width : 110,
				sortable : true,
				align : 'center'
			}, {
				display : 'Exemplares',
				name : 'exemplares',
				width : 110,
				sortable : true,
				align : 'center'
			}, {
				display : 'Tipo de Diferença',
				name : 'tipoDiferenca',
				width : 130,
				sortable : true,
				align : 'left'
			}, {
				display : 'Ação',
				name : 'acao',
				width : 60,
				sortable : true,
				align : 'center'
			}],
			sortname : "produto",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 180
		});
		
		
		$(".lanctoFaltasSobras_1Grid").flexigrid({
			url : '../xml/lancto_faltas_sobras-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'Cota',
				name : 'cota',
				width : 265,
				sortable : true,
				align : 'left'
			}, {
				display : 'Quantidade',
				name : 'quantidade',
				width : 90,
				sortable : true,
				align : 'center'
			}],
			width : 400,
			height : 180
		});
		
		$(".lanctoFaltasSobras_2Grid").flexigrid({
			url : '../xml/lancto_faltas_sobras_1-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'Código',
				name : 'codigo',
				width : 70,
				sortable : true,
				align : 'left'
			},{
				display : 'Produto',
				name : 'produto',
				width : 190,
				sortable : true,
				align : 'left'
			},{
				display : 'Edição',
				name : 'edicao',
				width : 50,
				sortable : true,
				align : 'center'
			}, {
				display : 'Preço R$',
				name : 'preco',
				width : 60,
				sortable : true,
				align : 'right'
			}, {
				display : 'Reparte',
				name : 'reparte',
				width : 50,
				sortable : true,
				align : 'center'
			}, {
				display : 'Qtde',
				name : 'qtde',
				width : 55,
				sortable : true,
				align : 'center'
			}],
			width : 570,
			height : 220
		});
	</script>
</body>