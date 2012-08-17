<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/operacaoDiferenciada.js"></script>

<script>
	var OD = new OperacaoDiferenciada();
</script>

<div id="tabDiferenciada">
            

   <br clear="all" />
   <fieldset style="width:800px;">
   	<legend>Grupos</legend>

       <table class="gruposGrid"></table>
       
       <span class="bt_novos" title="Incluir Novo"><a href="javascript:;" onclick="OD.novoGrupo();"><img src="${pageContext.request.contextPath}/images/ico_add.gif" hspace="5" border="0" />Incluir Novo</a></span>
       
   </fieldset>
   <br clear="all" />
            
</div>
          


<div id="dialog-confirm-grupo" title="Excluir Grupo" style="display:none;">
	<fieldset style="width:350px!important;">
  		<legend>Confirmação</legend>
        <p>Confirma a exclusão deste Grupo?</p>
        
    </fieldset>
</div>

<jsp:include page="detalheOperacaoDiferenciada.jsp"></jsp:include>

<jsp:include page="../messagesDialog.jsp"></jsp:include>
<jsp:include page="popupConfirmarOpDiferenciada.jsp"></jsp:include>
          
<script>

function dialogDetalhesGrupo() {
	
	$( "#dialog-novo-grupo" ).dialog({
		resizable: false,
		height:500,
		width:750,
		modal: true,
		buttons: {
			"Confirmar": function() {
				
				dialogConfirmarGrupo();
			},
			"Cancelar": function() {
				$( this ).dialog( "close" );
			}
		}
	});

};

function dialogExcluirGrupo(index) {
	
	$( "#dialog-confirm-grupo" ).dialog({
		resizable: false,
		height:'auto',
		width:400,
		modal: true,
		buttons: {
			"Confirmar": function() {
				OD.excluirGrupo(index);				
			},
			"Cancelar": function() {
				$( this ).dialog( "close" );
			}
		}
	});
};



$(".gruposGrid").flexigrid({
	autoload : true,
	url : contextPath + '/administracao/parametrosDistribuidor/obterGrupos',
	dataType : 'json',
	preProcess: OD.processaRetornoPesquisa,
	colModel : [ {
		display : 'Nome',
		name : 'nome',
		width : 500,
		sortable : true,
		align : 'left'
	},{
		display : 'Recolhimento',
		name : 'recolhimento',
		width : 180,
		sortable : true,
		align : 'LEFT'
	},{
		display : 'Ação',
		name : 'acao',
		width : 60,
		sortable : true,
		align : 'center'
	}],
	width : 800,
	height : 150
});

</script>
