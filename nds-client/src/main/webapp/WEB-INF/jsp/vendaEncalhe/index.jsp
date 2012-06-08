
<head>
<script language="javascript" type="text/javascript">

	//IMPRESSÃO DO SLIP DE VENDA DE ENCALHE
	function imprimeSlipVendaEncalhe(){
		
		//Valores dos Filtros
		var idCota = "1";
		var dataInicio = "10/10/2012";
		var dataFim = "10/10/2012";
	    
		document.location.assign("${pageContext.request.contextPath}/devolucao/vendaEncalhe/imprimeSlipVendaEncalhe?idCota="+idCota+"&dataInicio="+dataInicio+"&dataFim="+dataFim);
	}

	
	
	//IMPRESSÃO DO SLIP DE VENDA DE SUPLEMENTAR
	function imprimeSlipVendaSuplementar(){
		
		//Valores dos Filtros
		var idCota = "1";
		var dataInicio = "10/10/2012";
		var dataFim = "10/10/2012";
	    
		document.location.assign("${pageContext.request.contextPath}/devolucao/vendaEncalhe/imprimeSlipVendaSuplementar?idCota="+idCota+"&dataInicio="+dataInicio+"&dataFim="+dataFim);
	}
	
	
</script>
</head>

<body>

	<span name="botaoSlipVE" id="botaoSlipVE" class="bt_imprimir">
	    <!-- BOTAO PARA IMPRESSÃO DE SLIP DE VENDA DE ENCALHE -->
	    <a href="javascript:;" onclick="imprimeSlipVendaEncalhe()">Slip VE</a>
	</span>
	
	<span name="botaoSlipVS" id="botaoSlipVS" class="bt_imprimir">
	    <!-- BOTAO PARA IMPRESSÃO DE SLIP DE VENDA SUPLEMENTAR -->
	    <a href="javascript:;" onclick="imprimeSlipVendaSuplementar()">Slip VS</a>
	</span>

</body>