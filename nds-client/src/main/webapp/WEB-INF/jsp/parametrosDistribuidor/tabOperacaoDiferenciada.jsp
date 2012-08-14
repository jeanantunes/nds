 <div id="tabDiferenciada">
            
   <br clear="all" />
   <fieldset style="width:800px;">
   	<legend>Grupos</legend>

       <table class="gruposGrid"></table>
       
       <span class="bt_novos" title="Incluir Novo"><a href="javascript:;" onclick="incluirGrupo();"><img src="../images/ico_add.gif" hspace="5" border="0" />Incluir Novo</a></span>
       
   </fieldset>
   <br clear="all" />
            
</div>
            

<script>
	
	$(".gruposGrid").flexigrid({
			url : '../xml/gruposDistrib-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'Nome',
				name : 'nome',
				width : 500,
				sortable : true,
				align : 'left'
			},{
				display : 'Recolhimento',
				name : 'recolto',
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
		
	$(".selMunicipiosGrid").flexigrid({
			url : '../xml/selMunicipios-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'Municipio',
				name : 'municipio',
				width : 525,
				sortable : true,
				align : 'left'
			},{
				display : 'Qtde Cotas',
				name : 'qtdeCotas',
				width : 100,
				sortable : true,
				align : 'center'
			}, {
				display : '',
				name : 'sel',
				width : 20,
				sortable : true,
				align : 'center'
			}],
			width : 700,
			height : 150
		});


	$(".selCotasGrid").flexigrid({
			url : '../xml/selCotas-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'Cota',
				name : 'cota',
				width : 50,
				sortable : true,
				align : 'left'
			},{
				display : 'Nome',
				name : 'nome',
				width : 160,
				sortable : true,
				align : 'left'
			}, {
				display : 'Município',
				name : 'municipio',
				width : 90,
				sortable : true,
				align : 'left'
			}, {
				display : 'Endereço',
				name : 'endereco',
				width : 300,
				sortable : true,
				align : 'left'
			}, {
				display : '',
				name : 'sel',
				width : 20,
				sortable : true,
				align : 'center'
			}],
			width : 700,
			height : 150
		});

</script>