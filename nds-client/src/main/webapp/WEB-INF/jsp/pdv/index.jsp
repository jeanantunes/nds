<head>
	
	<script type="text/javascript">
		
	</script>
	
</head>
<body>
	
	<div id="dialog-pdv" title="PDV Cota">
		
		<div id="tabpdv">
		   
		  <ul>
		        <li><a href="#tabpdv-1">Dados Básicos</a></li>
		        <li><a href="#tabpdv-2">Endereços</a></li>
		        <li><a href="#tabpdv-3">Telefones</a></li>
		        <li><a href="#tabpdv-4">Caract. / Segmentação</a></li>
		        <li><a href="#tabpdv-5">Especialidade</a></li>
		        <li><a href="#tabpdv-6">Gerador de Fluxo</a></li>
		        <li><a href="#tabpdv-7">MAP</a></li>
		  </ul>
		
		   <div id="tabpdv-1"> <jsp:include page="dadosBasico.jsp"/> </div>
		   
		   <div id="tabpdv-2"> <jsp:include page="endereco.jsp"/> </div>
		   
		   <div id="tabpdv-3"> <jsp:include page="telefone.jsp"/> </div>
		   
		   <div id="tabpdv-4"> <jsp:include page="caracteristica.jsp"/> </div>
		   
		   <div id="tabpdv-5"> <jsp:include page="especialidade.jsp"/> </div>
		   
		   <div id="tabpdv-6"> <jsp:include page="geradorFluxo.jsp"/> </div>
		   
		   <div id="tabpdv-7"> <jsp:include page="map.jsp"/> </div>		
				
		</div>	
	</div>
	
</body>
