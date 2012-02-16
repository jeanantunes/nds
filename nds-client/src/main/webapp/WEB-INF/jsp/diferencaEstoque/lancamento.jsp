<body>
	<div class="corpo">
	
		<div class="container">

			<fieldset class="classFieldset">
			
				<legend>Lançamento Faltas e Sobras</legend>
				
				<form name="pesquisaForm" action="estoque/diferenca/lancamento/pesquisa">
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
								<select id="tipoDiferenca" name="tipoDiferenca" style="width: 220px;">
									<c:forEach var="tipoDiferenca" items="${listaTiposDiferenca}">
										<option value="${tipoDiferenca.key}">${tipoDiferenca.value}</option>
									</c:forEach>
								</select>
							</td>
							<td width="280">
								<span class="bt_pesquisar">
									<a href="javascript:;" onclick="mostrar_1();">Pesquisar</a>
								</span>
							</td>
						</tr>
					</table>
				</form>
			</fieldset>
			
			<div class="linha_separa_fields">&nbsp;</div>

			<fieldset class="classFieldset">
			
				<legend>Lançamento Faltas e Sobras</legend>
				
				<div class="grids" style="display: none;">
					<table class="gridLancamentos"></table>
				</div>
				
				<table width="931" border="0" cellspacing="1" cellpadding="1">
					<tr>
						<td width="282">
							<span class="bt_novo">
								<a href="javascript:;" onclick="popupNovasDiferencas();">Novo</a>
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
	
	<jsp:include page="novo.jsp" />
	
	<jsp:include page="rateio.jsp" />
	
	<script>
		$(".gridLancamentos").flexigrid({
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

		function popupExclusaoDiferenca() {

			$("#dialog-excluir" ).dialog({
				resizable: false,
				height:'auto',
				width:300,
				modal: true,
				buttons: {
					"Confirmar": function() {
						$( this ).dialog( "close" );
						$("#effect").hide("highlight", {}, 1000, callback);
						
					},
					"Cancelar": function() {
						$( this ).dialog( "close" );
					}
				}
			});	     
		};

	</script>
</body>