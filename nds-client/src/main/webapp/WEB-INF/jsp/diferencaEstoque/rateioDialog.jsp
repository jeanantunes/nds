<div id="dialogRateioDiferencas" title="Lançamento Faltas e Sobras" style="display: none;">

	<table class="gridRateioDiferencas"></table>
	
	<br />
	
	<table width="406" border="0" cellspacing="2" cellpadding="2">
		<tr>
			<td width="293"><strong>Total:</strong></td>
			<td width="99">999.999</td>
		</tr>
	</table>
	
	<script>
		$(".gridRateioDiferencas").flexigrid({
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

		function popupRateioDiferenca(idMovimentoEstoque) {
			
			$("#dialogRateioDiferencas" ).dialog({
				resizable: false,
				height:370,
				width:440,
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
	
</div>