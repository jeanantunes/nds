
<script>

function inicializarPopup() {
	
	ENDERECO.preencherComboUF();
	
	$("#formSocioCota")[0].reset();
	
	$("#cep").mask("99999-999");
	
	popup_novo_socio();
}

function popup_novo_socio() {
	
	$( "#dialog-socio" ).dialog({
		resizable: false,
		height:340,
		width:760,
		modal: true,
		buttons: {
			"Confirmar": function() {
				
				SOCIO_COTA.incluirSocio();
			},
			"Cancelar": function() {
				$( this ).dialog( "close" );
			}
		}
	});
}

$(".sociosPjGrid").flexigrid({
	dataType : 'json',
	preProcess:SOCIO_COTA.processarResultadoConsultaSocios,
	colModel : [{
		display : 'Nome',
		name : 'nome',
		width : 120,
		sortable : false,
		align : 'left'
	},{
		display : 'Cargo',
		name : 'cargo',
		width : 100,
		sortable : false,
		align : 'left'
	}, {
		display : 'Endereco',
		name : 'endereco',
		width : 340,
		sortable : false,
		align : 'left'
	},{
		display : 'Telefone',
		name : 'telefone',
		width : 115,
		sortable : false,
		align : 'left'
	},{
		display : 'Principal',
		name : 'principalFlag',
		width : 50,
		sortable : false,
		align : 'center'
	}, {
		display : 'Ação',
		name : 'acao',
		width : 60,
		sortable : false,
		align : 'center'
	}],
	width : 880,
	height : 180
});

</script>

<jsp:include page="./novoSocio.jsp" />

<div id="dialog-excluir-socio" title="Socios" style="display: none;">
	<p>Confirma esta Exclusão?</p>
</div>

<input type="hidden" id="idSocio" value=""/>

<label><strong>Sócios Cadastrados</strong></label>
<table class="sociosPjGrid"></table>
    
<span class="bt_add"><a href="javascript:inicializarPopup();" id="btnAddSocio">Incluir Novo</a></span>

<br>

<br>



