
<head>
<script language="javascript" type="text/javascript">

	//IMPRESSÃO DO SLIP DE VENDA DE ENCALHE
	function imprimeSlipVendaEncalhe(){
		var idVendaEncalhe = 1;
	    document.location.assign("${pageContext.request.contextPath}/devolucao/vendaEncalhe/imprimeSlipVendaEncalhe?idVendaEncalhe="+idVendaEncalhe);
	}

</script>
</head>

<body>

	<span name="botaoSlipVE" id="botaoSlipVE" class="bt_imprimir">
	    <!-- BOTAO PARA IMPRESSÃO DE SLIP DE VENDA DE ENCALHE -->
	    <a href="javascript:;" onclick="imprimeSlipVendaEncalhe()">Slip VE</a>
	</span>

</body>