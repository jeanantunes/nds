<script>
	
	$(".entregadoresCotaGrid").flexigrid({
		preProcess: preProcessamentoGridCotas,
		dataType : 'json',
		colModel : [  {
			display : 'Cota',
			name : 'numeroCota',
			width : 120,
			sortable : true,
			align : 'left'
		},{
			display : 'Nome',
			name : 'nomeCota',
			width : 300,
			sortable : true,
			align : 'left'
		}, {
			display : 'Procuração Assinada',
			name : 'procuracaoAssinada',
			width : 120,
			sortable : false,
			align : 'center'
		}],
		sortname : "numeroCota",
		sortorder : "asc",
		usepager : true,
		useRp : true,
		rp : 15,
		showTableToggleBtn : true,
		disableSelect: true,
		width : 730,
		height : 150
	});
	
	function preProcessamentoGridCotas(result){
		
		$.each(result.rows, function(index, row) {
			
			var checked = "";
			if (row.cell.procuracaoAssinada){
				
				checked = "checked";
			}
			
			row.cell.procuracaoAssinada = '<input type="checkbox" disabled="disabled" '+ checked +' />';
		});
		
		return result;
	}

</script>
<div id="cotas">
	<table width="700" cellpadding="2" cellspacing="2" style="text-align:left ">
		<tr>
			<td width="70">Entregador:</td>
			<td width="216" id="nomeEntregadorAbaCota"></td>
			<td width="58">Roteiro:</td>
			<td width="216" id="nomeRoteiro"></td>
		</tr>
		<tr>
		  <td>Box</td>
		  <td id="numeroBox"></td>
		  <td>Rota:</td>
		  <td id="nomeRota"></td>
           </tr>
	  </table>	
	  <br />
      <label><strong>Cotas</strong></label><br />

      <table class="entregadoresCotaGrid"></table>
        
</div>