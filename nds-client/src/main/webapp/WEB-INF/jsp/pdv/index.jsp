
<script language="javascript" type="text/javascript" src="${pageContext.request.contextPath}/scripts/pdv.js"></script>

<script language="javascript" type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.multiselects-0.3.js"></script>

<script language="javascript" type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.form.js"></script>

<script type="text/javascript">

$(".PDVsGrid").flexigrid({
	preProcess: PDV.executarPreProcessamento,
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
		width : 170,
		sortable : true,
		align : 'left'
	}, {
		display : 'Principal',
		name : 'principal',
		width : 60,
		sortable : true,
		align : 'center'
	}, {
		display : 'Status',
		name : 'status',
		width : 60,
		sortable : true,
		align : 'center'
	}, {
		display : '% Fat.',
		name : 'faturamento',
		width : 50,
		sortable : true,
		align : 'center'
	}, {
		display : 'Ação',
		name : 'acao',
		width : 55,
		sortable : true,
		align : 'center'
	}],
	width : 880,
	height : 150,
	sortname : "nomePdv",
	sortorder : "asc"
});

$("#btnSalvarPDV").keypress(function() {
	
	var keynum = 0;
      
    if(window.event) {

        keynum = event.keyCode;
    
    } else if(event.which) {   

    	keynum = event.which;
    }

	if (keynum == 13) {
		PDV.salvarPDV();
	}
});



</script>
	
<label><strong>PDVs Cadastrados</strong></label>
<br />
<table class="PDVsGrid"></table>
<br />
<span class="bt_novo"><a href="javascript:;" onclick="PDV.poupNovoPDV();">Novo</a></span>

<br clear="all" />

<div id="dialog-excluirPdv" title="Atenção" style="display:none">
	<p>Confirmar exclusão PDV ?</p>
</div>

<div id="dialog-pdv" title="PDV Cota">
	
	<jsp:include page="../messagesDialog.jsp">
		
		<jsp:param value="idModalPDV" name="messageDialog"/>
	
	</jsp:include>
	
	<input type="hidden" name="idPDV" id="idPDV" value=""/>
	
	<div id="tabpdv">
	    <ul>
	        <li><a href="#tabpdv-1">Dados Básicos</a></li>
	        <li><a href="#tabpdv-2" onclick="ENDERECO_PDV.popularGridEnderecos();">Endereços</a></li>
	        <li><a href="#tabpdv-3" onclick="TELEFONE_PDV.carregarTelefones();">Telefones</a></li>
	        <li><a href="#tabpdv-4">Caract. / Segmentação</a></li>
	        <li><a href="#tabpdv-5">Especialidade</a></li>
	        <li><a href="#tabpdv-6">Gerador de Fluxo</a></li>
	        <li><a href="#tabpdv-7">MAP</a></li>
	       
	  </ul>
	 	   <div id="tabpdv-1"> <jsp:include page="dadosBasico.jsp"/> </div>
		   
		   <div id="tabpdv-2"> <jsp:include page="endereco.jsp"/> </div>
		   
		   <div id="tabpdv-3"> 
		   		<jsp:include page="../telefone/index.jsp">
	        		<jsp:param value="PDV" name="tela"/>
	        	</jsp:include> 
		   	</div>
		   
		   <div id="tabpdv-4"> <jsp:include page="caracteristica.jsp"/> </div>
		   
		   <div id="tabpdv-5"> <jsp:include page="especialidade.jsp"/> </div>
		   
		   <div id="tabpdv-6"> <jsp:include page="geradorFluxo.jsp"/> </div>
		   
		   <div id="tabpdv-7"> <jsp:include page="map.jsp"/> </div>				
				 
		  <br clear="all" />
	</div>
	
</div>


