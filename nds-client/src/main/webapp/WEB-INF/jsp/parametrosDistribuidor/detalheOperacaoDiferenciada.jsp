
<div id="dialog-novo-grupo" title="Novo" style="display:none;">
<fieldset style="width:700px!important; margin-bottom:5px;">
            		<legend>Selecione:</legend>
                    
                    <table width="300" border="0" cellspacing="1" cellpadding="1">
                      <tr>
                        <td width="20"><input type="radio" name="diferenciada" id="radio" value="radio" onchange="tipoCota();" /></td>
                        <td width="56">Tipo Cota</td>
                        <td width="214"><select name="select" id="select" style="width:120px; display:none;" class="TipoCota">
                          <option selected="selected">Selecione...</option>
                          <option>Convecional</option>
                          <option>Alternativo</option>
                        </select></td>
                      </tr>
                      <tr>
                        <td><input type="radio" name="diferenciada" id="radio2" value="radio2" onchange="municipios();" /></td>
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
