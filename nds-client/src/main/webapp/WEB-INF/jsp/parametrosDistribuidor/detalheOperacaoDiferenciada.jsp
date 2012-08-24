
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
                    <span class="bt_sellAll" style="float:right; margin-right:23px;"><label for="sel">Selecionar Todos</label>
                    
<!-- SELECIONAR TODOS COTAS -->
<input type="checkbox" name="Todos" id="sel" onclick="OperacaoDiferenciadaController.selecionarTodasCotas(this);"/>
					
					
					</span>                    
                    <br clear="all" />
					
                    
                    
            	</fieldset>
            </div>
            
            
            <div class="selecionarMunicipio" style="display:none;">
            	<fieldset style="width:700px!important; margin-bottom:5px;">
            		<legend>Selecione os Municipios:</legend>
                    <table class="selMunicipiosGrid"></table>
                    <span class="bt_sellAll" style="float:right; margin-right:20px"><label for="sel">Selecionar Todos</label>
                    
<!-- SELECIONAR TODOS MUNICIPIOS -->
<input type="checkbox" name="Todos" id="selecionarTodosID" onclick="OperacaoDiferenciadaController.selecionarTodosMunicipios(this);" />

						
						</span>
                    
                    <br clear="all" />
					
                    
                    
            	</fieldset>
            </div>
</div>