
<div id="dialog-detalhe-venda" title="Parciais de Venda">

	<fieldset style="width:600px;">

		<legend>Parciais de Venda</legend>

	    <table class="parcial-parciaisVendaGrid"></table>

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
