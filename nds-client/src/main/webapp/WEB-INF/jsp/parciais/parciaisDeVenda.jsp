
<div id="dialog-detalhe-venda" title="Parciais de Venda">

	<fieldset style="width:600px;">

		<legend>Parciais de Venda</legend>

	    <table class="parciaisVendaGrid"></table>

	    <span class="bt_novos" title="Gerar Arquivo">
	    	<a href="${pageContext.request.contextPath}/parciais/exportarDetalhesVenda?fileType=XLS">
	    		<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
	    		Arquivo
	    	</a>
	    </span>

		<span class="bt_novos" title="Imprimir">
			<a href="${pageContext.request.contextPath}/parciais/exportarDetalhesVenda?fileType=PDF">
				<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />
				Imprimir
			</a>
		</span>

	</fieldset>
	
</div>

<script>

$(function() {
	
	$(".parciaisVendaGrid").flexigrid({
		dataType : 'json',
		colModel : [ {
			display : 'Cota',
			name : 'cota',
			width : 70,
			sortable : true,
			align : 'left'
		}, {
			display : 'Nome',
			name : 'nome',
			width : 200,
			sortable : true,
			align : 'left'
		}, {
			display : 'Reparte',
			name : 'reparte',
			width : 70,
			sortable : true,
			align : 'center'
		}, {
			display : 'Encalhe',
			name : 'encalhe',
			width : 70,
			sortable : true,
			align : 'center'
		}, {
			display : 'Venda Juramentada',
			name : 'vendaJuramentada',
			width : 110,
			sortable : true,
			align : 'center'
		}],
		sortname : "cota",
		sortorder : "asc",
		usepager : true,
		useRp : true,
		rp : 15,
		showTableToggleBtn : true,
		width : 595,
		height : 200
	});
});

</script>
