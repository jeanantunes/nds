

<script type="text/javascript">

var PDV = {
		
		popup_novoPdv:function() {
			
			$( "#dialog-pdv" ).dialog({
				resizable: false,
				height:600,
				width:890,
				modal: true
			});
		}		
};

$(".PDVsGrid").flexigrid({
	
	dataType : 'json',
	colModel : [  {
		display : 'Nome PDV',
		name : 'nomePdv',
		width : 100,
		sortable : true,
		align : 'left'
	},{
		display : 'Tipo de Ponto',
		name : 'tipoPonto',
		width : 100,
		sortable : true,
		align : 'left'
	},{
		display : 'Contato',
		name : 'contato',
		width : 80,
		sortable : true,
		align : 'left'
	}, {
		display : 'Telefone',
		name : 'telefone',
		width : 80,
		sortable : true,
		align : 'left'
	}, {
		display : 'Endereço',
		name : 'endereco',
		width : 150,
		sortable : true,
		align : 'left'
	}, {
		display : 'Bairro',
		name : 'bairro',
		width : 90,
		sortable : true,
		align : 'left'
	}, {
		display : 'Cidade',
		name : 'cidade',
		width : 110,
		sortable : true,
		align : 'left'
	}, {
		display : 'Ação',
		name : 'acao',
		width : 55,
		sortable : true,
		align : 'center'
	}],
	width : 880,
	height : 150
});

</script>
	
<label><strong>PDVs Cadastrados</strong></label>
<br />
<table class="PDVsGrid"></table>
<br />
<span class="bt_novo"><a href="javascript:;" onclick="PDV.popup_novoPdv();">Novo</a></span>

<br clear="all" />
	

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
			 
	  <br clear="all" />
	
	</div>

</div>

