<div id="dialogNovasDiferencas" title="Lançamento < Faltas e Sobras >" style="display: none;">

	<table class="gridNovasDiferencas"></table>
	
	<table width="465" border="0" cellspacing="2" cellpadding="2">
		<tr style="font-size: 11px;">
			<td width="329"><strong>Total Geral:</strong></td>
			<td width="71" align="right">9.999,99</td>
			<td width="45" align="right">99.999</td>
		</tr>
	</table>
	
	<script>
		$(".gridNovasDiferencas").flexigrid({
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

		function popupNovasDiferencas() {
			
			$("#dialogNovasDiferencas" ).dialog({
				resizable: false,
				height:390,
				width:590,
				modal: true,
				buttons: {
					"Confirmar": function() {
						$( this ).dialog( "close" );
						$("#effect").hide("highlight", {}, 1000, callback);
						mostrar_1();
						
					},
					"Cancelar": function() {
						$( this ).dialog( "close" );
					}
				}
			});
		};
	</script>
	
</div>