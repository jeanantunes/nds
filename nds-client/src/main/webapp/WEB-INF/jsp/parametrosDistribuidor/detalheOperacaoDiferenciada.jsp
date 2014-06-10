
<div id="dialog-novo-grupo" title="Novo" style="display:none;">
	<fieldset style="width:700px!important; margin-bottom:5px;">
	            		<legend>Selecione:</legend>
	                    
	                    <table width="300" border="0" cellspacing="1" cellpadding="1">
	                      <tr>
	                        <td width="20">
	                        
	<!-- Radio Tipo Cota -->                        
	<input type="radio" name="diferenciada" id="radioTipoCota" onclick="OperacaoDiferenciadaController.selecionarPorTipoCota();" />
	
							</td>
	                        <td width="56">Tipo Cota</td>
	                        <td width="214">
	                        
	<!-- Combo Tipo Cota -->                        
	<select id="comboTipoCota" onchange="OperacaoDiferenciadaController.limparSelecoes(OperacaoDiferenciadaController.carregarTipoCota);" name="select" id="select" style="width:121px; display:none;" class="selecionarCotas">
	   <option selected="selected">Selecione...</option>
	   <option value="CONVENCIONAL">Convecional</option>
	   <option value="ALTERNATIVO">Alternativo</option>
	</select>
	
						</td>
	                      </tr>
	                      <tr>
	                        <td>
	                        
	<!-- Radio Municipios -->                        
	<input type="radio" name="diferenciada" id="radioMunicipios" onclick="OperacaoDiferenciadaController.selecionarPorMunicipio();" />
							
							</td>
	                        <td>Municipio</td>
	                        <td>&nbsp;</td>
	                      </tr>
	                    </table>
	</fieldset>
           
            <br  clear="all"/>

            <div id="selectCota" class="selecionarCotas" style="display:none;">
            	<fieldset style="width:700px!important; margin-bottom:5px;">
            		<legend>Selecione as Cotas:</legend>
                    <table class="selCotasGrid"></table>
                    <table width="150" border="0" align="right" cellpadding="0" cellspacing="0">
					    <tr>
					        <td width="115" align="right"><label for="sel">Selecionar Todos</label></td>
					        <td width="35" align="left" valign="top">
					        <span class="bt_sellAll" style="float:right; margin-right:23px;">
								<!-- SELECIONAR TODOS COTAS -->
								<input type="checkbox" name="Todos" id="sel" onclick="OperacaoDiferenciadaController.selecionarTodasCotas(this);"/>
							</span> 
							</td>
					    </tr>
					</table>                   
                    <br clear="all" />
					
                    
                    
            	</fieldset>
            </div>
            
            
            <div class="selecionarMunicipio" style="display:none;">
            	<fieldset style="width:700px!important; margin-bottom:5px;">
            		<legend>Selecione os Municipios:</legend>
                    <table class="selMunicipiosGrid"></table>
                    <table width="150" border="0" align="right" cellpadding="0" cellspacing="0">
					    <tr>
					        <td width="115" align="right"><label for="sel">Selecionar Todos</label></td>
					        <td width="35" align="left" valign="top">
					        	<span class="bt_sellAll" style="float:right; margin-right:20px">
<!-- SELECIONAR TODOS MUNICIPIOS -->
<input type="checkbox" name="Todos" id="selecionarTodosID" onclick="OperacaoDiferenciadaController.selecionarTodosMunicipios(this);" />							</span>
							</td>
					    </tr>
					</table>
                    
                    <br clear="all" />
					
                    
                    
            	</fieldset>
            </div>
</div>