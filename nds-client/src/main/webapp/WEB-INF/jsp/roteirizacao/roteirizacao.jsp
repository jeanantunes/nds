
<head>
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/pesquisaCota.js"></script>
    <script type="text/javascript">

        var pesquisaCotaFiltroConsulta = new PesquisaCota(roteirizacao.workspace);

    </script> 
</head>

<form id="form-transfere-rota">
<div id="dialog-transfere-rota" title="Transferir Rota" style="display:none;">
	<fieldset>
    	<legend>Transferir Rota</legend>
		<table width="347" border="0" cellspacing="1" cellpadding="1">
			<tr>
           		<td>Roteiro Atual:</td>
           		<td>
           			<input type="text" id="nomeRoteiroAtual" style="width:220px;" disabled="disabled" />
           		</td>
         	</tr>
         	<tr>
           		<td>
           			<strong>Novo Roteiro:</strong>
           		</td>
           		<td>
           			<select name="select" id="selectNovoRoteiro" style="width:227px;"></select>
           		</td>
         	</tr>
       	</table>
    </fieldset>
</div>
</form>

<form id="form-transfere-cotas">
	<div id="dialog-transfere-cotas" title="Transferir Cotas" style="display:none;">
		<fieldset>
    		<legend>Transferir Cotas</legend>
           
   			<table width="347" border="0" cellspacing="1" cellpadding="1">
         		<tr>
			    	<td>Rota Atual:</td>
			        <td>
			        	<input type="text" id="nomeRotaAtual" style="width:220px;" disabled="disabled" />
			        </td>
         		</tr>
         		<tr>
	           		<td>
	           			<strong>Nova Rota:</strong>
	           		</td>
	           		<td>
	           			<select id="selectNovaRota" style="width:227px;"></select>
	           		</td>
         		</tr>
       		</table>
		</fieldset>
	</div>
</form>

<form id="form-transfere-roteiro">
	<div id="dialog-transfere-roteiro" title="Transferir Roteiro" style="display: none;">
		<fieldset>
			<legend>Transferir Roteiros</legend>
			<table width="347" border="0" cellspacing="1" cellpadding="1">
				<tr>
            		<td>Box Atual:</td>
            		<td>
            			<input type="text" id="nomeBoxAtual" style="width:220px;" disabled="disabled" />
            		</td>
          		</tr>
          		<tr>
            		<td>
            			<strong>Novo Box:</strong>
            		</td>
            		<td>
            			<select id="selectNovoBox" style="width:227px;"></select>
            		</td>
          		</tr>
        	</table>
		</fieldset>
	</div>
</form>

<form id="form-excluir-rota-roteiro">
<div id="dialog-excluir-rota-roteiro" title="Rotas" style="display:none;">
	<fieldset style="width: 360px;">
    	<legend>Excluir</legend>
        <span id="msgConfExclusaoRotaRoteiro"> Confirma a exclus&atilde;o destas Rotas deste Roteiro</span>
    </fieldset>
</div>
</form>

<form id="form-excluir-cotas">
<div id="dialog-excluir-cotas" title="Cotas" style="display:none;">
	<fieldset>
    	<legend>Excluir</legend>
        <p>Confirma a exclus&atilde;o destas Cotas desta Rota?</p>
    </fieldset>
</div>
</form>

<form id="form-excluir-pdvs">
<div id="dialog-excluir-pdvs" title="PDVs" style="display:none;">
	<fieldset>
    	<legend>Excluir</legend>
        <p>Confirma a exclus&atilde;o destas PDVs desta Rota?</p>
    </fieldset>
</div>
</form>

<form id="form-roteirizacao">

    <div id="dialog-roteirizacao" title="Nova Roteirização" style="display:none;">

    <fieldset style="width:270px; float:left;">
        <legend>Box</legend>


        <input name="nomeBox" type="text" id="nomeBox" style="width:240px; float:left; margin-bottom:5px;" />
        <a  id="lnkPesquisarBox" href="javascript:;">
            <img src="${pageContext.request.contextPath}/images/ico_pesquisar.png"
                 alt="Adicionar Rota" width="16" height="16" border="0"
                 style="float:left; margin-left:5px; margin-top:5px;" /></a>
            <br/>
        <table class="boxGrid"></table>
    </fieldset>



    <fieldset style="width:270px; float:left; margin-left:15px;">
        <legend>Roteiros</legend>


        <input name="descricaoRoteiro" type="text" id="descricaoRoteiro" style="width:240px; float:left; margin-bottom:5px;" />
        <a href="javascript:;" onclick="roteirizacao.pesquisarRoteiros();">
                <img src="${pageContext.request.contextPath}/images/ico_pesquisar.png"
                     alt="Adicionar Rota" width="16" height="16" border="0"
                     style="float:left; margin-left:5px; margin-top:5px;" /></a>
                <br/>
        <table class="roteirosGrid"></table>
    </fieldset>



    <fieldset style="width:270px; float:left; margin-left:15px;">
        <legend>Rotas</legend>

        <input name="descricaoRota" type="text" id="descricaoRota" style="width:240px; float:left; margin-bottom:5px;" />
        <a href="javascript:;" onclick="roteirizacao.pesquisarRotas();">
            <img src="${pageContext.request.contextPath}/images/ico_pesquisar.png"
                 alt="Adicionar Rota" width="16" height="16" border="0"
                 style="float:left; margin-left:5px; margin-top:5px;" /></a>
            <br/>
        <table class="rotasGrid"></table>
    </fieldset>

    <fieldset style="width:875px; float:left; margin-left:5px; margin-top:10px; overflow:hidden;">
        <legend>Cotas da Rota</legend>
        <span id="cotasRota" style="float:left; margin-bottom:10px; margin-left:3px; margin-top:5px;">
            
        </span>
        <br clear="all" />
        <table class="cotasRotaGrid" id="cotasRotaGrid"></table>

        <table width="100%" border="0" cellspacing="1" cellpadding="1">
        <tr>
            <td>
                <span class="bt_novos" title="Novo">
                	<a href="javascript:;" onclick="roteirizacao.abrirTelaNovoRoteiroRota();">
                    	<img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0"/>
                    	Novo
                    </a>
                </span>

                 <span class="bt_novos" title="Adicionar"><a href="javascript:;" onclick="roteirizacao.abrirTelaCotas();">
                    <img src="${pageContext.request.contextPath}//images/ico_add.gif" hspace="5" border="0"/>Adicionar</a>
                </span>

                <span class="bt_novos trans_cota" title="Transferência de Roteiro"><a href="javascript:;" onclick="roteirizacao.popup_tranferir();">
                     <img src="${pageContext.request.contextPath}/images/ico_integrar.png" hspace="5" border="0"/>Transferir</a>
                </span>

                <span class="bt_novos" title="Copiar Cota"><a href="javascript:;" onclick="roteirizacao.abrirPopupCopiarCotas();">
                    <img src="${pageContext.request.contextPath}/images/ico_detalhes.png" hspace="5" border="0"/>Copiar Cota</a>
                </span>

                <span class="bt_novos" title="Excluir"><a href="javascript:;" onclick="roteirizacao.popupExcluirRotaRoteiro();">
                    <img src="${pageContext.request.contextPath}//images/ico_excluir.gif" hspace="5" border="0"/>Excluir</a>
                </span>
            </td>
            <td>
                 <span class="bt_sellAll" style="float:right;"><label for="sel">Selecionar Todos</label>
                    <input type="checkbox" id="selecionarTodosPdv" onclick="roteirizacao.selecionarTodosPdvs();" style="float:left; margin-right:15px; "/>
                </span>
            </td>
            <td></td>
         </tr>
    </table>

    </fieldset>

    <div class="linha_separa_fields">&nbsp;</div>
    </div>

    </form>



	<form id="form-cotas-disponiveis">
		<div id="dialog-cotas-disponiveis" title="Cotas Dispon&icirc;veis" style="display:none;">
		    
		    <jsp:include page="../messagesDialog.jsp">
				<jsp:param value="dialogRoteirizacaoCotaDisponivel" name="messageDialog"/>
			</jsp:include> 
			
		    <fieldset style="width:800px; float:left;">
		    
				<legend>Pesquisar Cotas</legend>
				
				<table width="800" border="0" cellpadding="2" cellspacing="1" class="filtro">
				
		            <tr>
		        		<td>Tipo:</td>
		        		<td>
		        			<select name="tipoPesquisa" id="tipoPesquisa" style="width:100px;">
		        				<option selected="selected" value="">PDV</option>   	
		        				<option value="cota">Cota</option>   
		                  	</select>
		                </td>
		              	
		              	<td>Cota:</td>
			            <td>
			              	
			              <input name="cotaPesquisaPdv" 
					               id="cotaPesquisaPdv" 
					               type="text"
					               maxlength="11"
					               style="width:70px; 
					               float:left; margin-right:5px;"
					               onchange="pesquisaCotaFiltroConsulta.pesquisarPorNumeroCota('#cotaPesquisaPdv', '#nomeCotaPesquisaPdv',false,null,null);"/>
					              
			              <td>Nome:</td>
			              <td>
			              	
			              	<input  name="nomeCotaPesquisaPdv" 
						      		 id="nomeCotaPesquisaPdv" 
						      		 type="text" 
						      		 class="nome_jornaleiro" 
						      		 maxlength="255"
						      		 style="width:130px;"
						      		 onkeyup="pesquisaCotaFiltroConsulta.autoCompletarPorNome('#nomeCotaPesquisaPdv');" 
						      		 onblur="pesquisaCotaFiltroConsulta.pesquisarPorNomeCota('#cotaPesquisaPdv', '#nomeCotaPesquisaPdv',false,null,null);" />
			              
			              </td>   
		            </tr>
		            
		            <tr>
		            			            
		            
		              <td>UF:</td>	       
		              <td>
		                  <select name="comboUf" id="comboUf" onchange="roteirizacao.buscalistaMunicipio()" style="width:100px;">   
		                  </select>
		              </td>
		              
		              <td>Munic.</td>
		              <td>
			              <select name="comboMunicipio" id="comboMunicipio" onchange="roteirizacao.buscalistaBairro()" style="width:150px;">
			              </select>
		              </td>
		              
		              <td>Bairro:</td>
		              <td width="168">
			              <select name="comboBairro" id="comboBairro" style="width:150px;">
			              </select>
		              </td>
		              
		              <td width="36">CEP:</td>
		              <td width="87">
		                  <input name="cepPesquisa" type="text" id="cepPesquisa" style="width:80px;" />
		              </td>
		              
		              <td width="79">
		                  <span class="bt_novos">
		                      <a href="javascript:;" onclick="roteirizacao.buscaPdvsDisponiveis();">
		                          <img src="${pageContext.request.contextPath}/images/ico_pesquisar.png" border="0" />
		                      </a>
		                  </span>
		              </td>
		            
		            
		            </tr>
 
		        </table>
			</fieldset>
			
	        <div class="linha_separa_fields">&nbsp;</div>
			<div class="grids" style="display: none;">
				<fieldset style="width:800px; float:left; margin-top:5px;">
			        <legend></legend>
					<legend>Cotas Dispon&icirc;veis</legend>
					
					<table class="cotasDisponiveisGrid"></table>
						
				</fieldset>
			</div>
		        
		    <table width="100%">
		        <tr>
		        
		            <td width="70%"></td>
			        <td width="15%">
			            <label for="textoSelTodos" id="textoSelTodos">
                            Marcar Todos
                        </label>
			        </td>
			        
				    <td width="15%">
				                
		                 <span class="checar">
		                     <input title="Selecionar todos" type="checkbox" id="selTodos" name="selTodos" onclick="roteirizacao.selecionarTodosNovosPdvs(this.checked);" style="float:left;"/>
		                 </span>
		
		            </td>
		            
	             </tr> 
             </table>   
    
			<br clear="all" />
		</div>
	</form>
	
	<form id="formNovoDado">
		<div id="dialog-novo-dado" title="Novo" style="display:none;">
			
			<jsp:include page="../messagesDialog.jsp" /> 
			
			<fieldset style="width: 380px;">
	    	
	    		<legend>Novo</legend>
	    	
	    		<table width="347" border="0" cellspacing="1" cellpadding="1">
	    			<tr>
						<td width="85">Novo:</td>
						<td width="255">
							<select id="selectNovoRoteiroRota" style="width:220px;" onchange="roteirizacao.switchNovoRoteiroRota();">
								<option value="roteiro" selected="selected">Roteiro</option>
								<option value="rota" >Rota</option>
							</select>
						</td>
					</tr>
	    			<tr>
						<td width="85">Incluir:</td>
						<td width="255">
							<select id="selectIncluirEmRoteiro" style="width:220px; display:none;">
								<option>Selecione...</option>
							</select>
						</td>
					</tr>
				</table>
				
	        	<table width="347" border="0" cellspacing="1" cellpadding="1" id="incluirRoteiro">
	          		<tr>
	            		<td width="85">Ordem:</td>
	            		<td>
	            			<input id="inputOrdem" style="width:220px;" />
	            		</td>
	          		</tr>
	          		<tr>
	            		<td>Nome:</td>
	            		<td>
	            			<input id="inputNome" style="width:220px;" />
	            		</td>
	          		</tr>
	      		</table>
			</fieldset>
		</div>
	</form>
	
	<form id="formCopiaCotaRota">
		<jsp:include page="copiarCotaDialog.jsp"></jsp:include>
	</form>