
<div id="dialog-novo-grupo" title="Novo" style="display:none;">
<fieldset style="width:700px!important; margin-bottom:5px;">
            		<legend>Selecione:</legend>
                    
                    <table width="300" border="0" cellspacing="1" cellpadding="1">
                      <tr>
                        <td width="20">
                        
<!-- Radio Tipo Cota -->                        
<input type="radio" name="diferenciada" id="radioTipoCota" onclick="OD.selecionarPorTipoCota();" />

						</td>
                        <td width="56">Tipo Cota</td>
                        <td width="214">
                        
<!-- Combo Tipo Cota -->                        
<select id="comboTipoCota" onchange="OD.carregarTipoCota();" name="select" id="select" style="width:121px; display:none;" class="selecionarCotas">
   <option selected="selected">Selecione...</option>
   <option value="CONVENCIONAL">Convecional</option>
   <option value="ALTERNATIVO">Alternativo</option>
</select>

					</td>
                      </tr>
                      <tr>
                        <td>
                        
<!-- Radio Municipios -->                        
<input type="radio" name="diferenciada" id="radioMunicipios" onclick="OD.carregarMunicipios();" />
						
						</td>
                        <td>Municipio</td>
                        <td>&nbsp;</td>
                      </tr>
                    </table>
            </fieldset>
            <br  clear="all"/>

            <div class="selecionarCotas" style="display:none;">
            	<fieldset style="width:700px!important; margin-bottom:5px;">
            		<legend>Selecione as Cotas:</legend>
                    <table class="selCotasGrid"></table>
                    <span class="bt_sellAll" style="float:right; margin-right:23px;"><label for="sel">Selecionar Todos</label><input type="checkbox" name="Todos" id="sel" onclick="checkAll();"/></span>
                    
                    <br clear="all" />
					
                    
                    
            	</fieldset>
            </div>
            
            
            <div class="selecionarMunicipio" style="display:none;">
            	<fieldset style="width:700px!important; margin-bottom:5px;">
            		<legend>Selecione os Municipios:</legend>
                    <table class="selMunicipiosGrid"></table>
                    <span class="bt_sellAll" style="float:right; margin-right:20px"><label for="sel">Selecionar Todos</label><input type="checkbox" name="Todos" id="sel" onclick="checkAll();"/></span>
                    
                    
                    <br clear="all" />
					
                    
                    
            	</fieldset>
            </div>
</div>

<script>

	function selecionarPorTipoCota() {
		
		$('.selecionarCotas').hide();
		$('.selecionarMunicipio').hide();
		$('#comboTipoCota').show();
	}
	
	$(".selMunicipiosGrid").flexigrid({
			autoload : false,
			url : contextPath + '/administracao/parametrosDistribuidor/obterMunicipios',
			dataType : 'json',
			preProcess: OD.processaMunicipios,
			colModel : [ {
				display : 'Municipio',
				name : 'municipio',
				width : 525,
				sortable : true,
				align : 'left'
			},{
				display : 'Qtde Cotas',
				name : 'qtde',
				width : 100,
				sortable : true,
				align : 'center'
			}, {
				display : '',
				name : 'selecionado',
				width : 20,
				sortable : true,
				align : 'center'
			}],
			sortname : "municipio",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			width : 700,
			height : 150
		});
	
	
	$(".selCotasGrid").flexigrid({
			autoload : false,
			url : contextPath + '/administracao/parametrosDistribuidor/obterCotas',
			dataType : 'json',
			preProcess: OD.processaCotas,
			colModel : [ {
				display : 'Cota',
				name : 'numCota',
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
				name : 'selecionado',
				width : 20,
				sortable : true,
				align : 'center'
			}],
			sortname : "numCota",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			width : 700,
			height : 150
		});
	
</script>