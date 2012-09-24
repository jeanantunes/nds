<form id="form-transfere-rota">
<div id="dialog-transfere-rota" title="Transferir Rotas" style="display:none;">
	<fieldset>
    	<legend>Transferir Rotas para:</legend>
        <p>Pesquise ou Digite o nome de um Novo Roteiro para estas Rotas.</p>
        <input name="roteiroTranferenciaNome" type="text" id="roteiroTranferenciaNome" onkeyup="roteirizacao.autoCompletarRoteiroPorNome('#roteiroTranferenciaNome',roteirizacao.selecionaRoteiroTranferencia)" style="width:300px; float:left; margin-bottom:5px;" />
        <input name="roteiroTranferenciaSelecionadoId" type="hidden" id="roteiroTranferenciaSelecionado"  />
        <input name="roteiroTranferenciaSelecionadoNome" type="hidden" id="roteiroTranferenciaSelecionadoNome"  />
        <a href="javascript:;" onclick="roteirizacao.exibiRoteiroNovoTranferencia();"><img src="${pageContext.request.contextPath}/images/ico_add.gif" alt="Adicionar Rota" width="16" height="16" border="0" style="float:left; margin-left:5px; margin-top:5px;" /></a>
        <br clear="all" />
        <div class="roteiroNovo" style="display:none;">
        <a href="javascript:;" onclick="roteirizacao.escondeRoteiroNovoTranferencia();"><img src="${pageContext.request.contextPath}/images/ico_excluir.gif" alt="Fechar" border="0" align="right" /></a>
        <br clear="all" />
        <label>Box:</label>
        <select name="boxRoteiroTranferencia" id="boxRoteiroTranferencia" style="width:230px;  float:left; margin-bottom:5px;">
            <option>Selecione...</option>
           <c:forEach var="box" items="${listaBox}">
				<option value="${box.key}">${box.value}</option>
			</c:forEach>
        </select>
        <br clear="all" />

        <label>Ordem:</label>
        <input name="ordemRoteiroTranferencia" id="ordemRoteiroTranferencia" type="text" style="width:225px; float:left; margin-bottom:5px;" />    
        <br clear="all" />
        <label>Roteiro Especial:</label>
        <input type="checkbox" name="tipoRoteiroTranferencia" value="Especial" id="tipoRoteiroTranferencia" onclick="roteirizacao.roteiroEspecial()"  />
        <br clear="all" />  
</div>
    </fieldset>
</div>
</form>

<form id="form-transfere-cotas">
<div id="dialog-transfere-cotas" title="Transferir Cotas" style="display:none;">
	<fieldset>
    	<legend>Transferir Cotas para:</legend>
        <p>Pesquise ou Digite o nome de uma Nova Rota para estas Cotas.</p>
        <input name="lstRotaTranferencia" type="text" id="lstRotaTranferencia"  onkeyup="roteirizacao.autoCompletarRotaPorNome('#lstRotaTranferencia')" style="width:300px; float:left; margin-bottom:5px;" />
        <a href="javascript:;"   onclick="roteirizacao.exibiRotaNovaTranferencia();"><img src="${pageContext.request.contextPath}/images/ico_add.gif" alt="Adicionar Rota" width="16" height="16" border="0" style="float:left; margin-left:5px; margin-top:5px;" /></a>
        <div class="rotaNovaTransferencia" style="display:none;">
	        <a href="javascript:;" onclick="roteirizacao.escondeRotaNovaTranferencia();"><img src="${pageContext.request.contextPath}/images/ico_excluir.gif" alt="Fechar" border="0" align="right" /></a>
	        <br clear="all" />
	        <label>Ordem:</label>
	        <input name="ordemRotaTranferencia" id="ordemRotaTranferencia" type="text" style="width:225px; float:left; margin-bottom:5px;" />    
        <br clear="all" />  
</div>
    </fieldset>
</div>
</form>

<form id="form-excluir-rotas">
<div id="dialog-excluir-rotas" title="Rotas" style="display:none;">
	<fieldset>
    	<legend>Excluir</legend>
        <p>Confirma a exclus&atilde;o destas Rotas deste Roteiro</p>
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

<form id="form-rota">
<div id="dialog-rota" title="Rota" style="display:none;">
	<fieldset>
    	<legend>Nova Rota</legend>
        <label>Ordem:</label>
        <input name="ordemRotaInclusao"  id="ordemRotaInclusao"  type="text" style="width:200px; float:left; margin-bottom:5px;" />       
        <br clear="all" />
        
        <label>Nome:</label>
        <input name="nomeRotaInclusao" id="nomeRotaInclusao" type="text" style="width:200px; float:left;" />       
        <br clear="all" />
        

    </fieldset>
</div>
</form>

<form id="form-roteiro">
<div id="dialog-roteiro" title="Roteiro" style="display:none;">
<jsp:include page="../messagesDialog.jsp" /> 
	<fieldset>
    	<legend>Novo Roteiro</legend>
        <label>Box:</label>
        <select name="boxInclusaoRoteiro" id="boxInclusaoRoteiro" style="width:200px;  float:left; margin-bottom:5px;">
			<option value="" selected="selected">Selecione...</option>
			<c:forEach var="box" items="${listaBox}">
				<option value="${box.key}">${box.value}</option>
			</c:forEach>
 		</select>
        
        <br clear="all" />
        <label>Ordem:</label>
        <input name="ordemInclusaoRoteiro" id="ordemInclusaoRoteiro" type="text" style="width:200px; float:left; margin-bottom:5px;" />       
        <br clear="all" />
        
        <label>Nome:</label>
        <input name="nomeInclusaoRoteiro" id="nomeInclusaoRoteiro"  type="text" style="width:200px; float:left;" />
         <br clear="all" />
        <label>Roteiro Especial:</label>
        <input type="checkbox" name="tipoRoteiro" value="Especial" id="tipoRoteiro" onclick="roteirizacao.roteiroEspecialNovo()"  />        
        <br clear="all" />
        

    </fieldset>
</div>
</form>

<form id="form-roteirizacao">

    <div id="dialog-roteirizacao" title="Nova Roteirização" style="display:none;">

    <fieldset style="width:270px; float:left;">
        <legend>Box</legend>


        <input name="nomeBox" type="text" id="nomeBox" style="width:240px; float:left; margin-bottom:5px;" />
        <a href="javascript:;">
            <img src="${pageContext.request.contextPath}/images/ico_pesquisar.png"
                 alt="Adicionar Rota" width="16" height="16" border="0"
                 style="float:left; margin-left:5px; margin-top:5px;" /></a>
            <br/>
        <table class="boxGrid"></table>
    </fieldset>



    <fieldset style="width:270px; float:left; margin-left:15px;">
        <legend>Roteiros</legend>


        <input name="descricaoRoteiro" type="text" id="descricaoRoteiro" style="width:240px; float:left; margin-bottom:5px;" />
        <a href="javascript:;">
                <img src="${pageContext.request.contextPath}/images/ico_pesquisar.png"
                     alt="Adicionar Rota" width="16" height="16" border="0"
                     style="float:left; margin-left:5px; margin-top:5px;" /></a>
                <br/>
        <table class="roteirosGrid"></table>
    </fieldset>



    <fieldset style="width:270px; float:left; margin-left:15px;">
        <legend>Rotas</legend>

        <input name="descricaoRota" type="text" id="descricaoRota" style="width:240px; float:left; margin-bottom:5px;" />
        <a href="javascript:;">
            <img src="${pageContext.request.contextPath}/images/ico_pesquisar.png"
                 alt="Adicionar Rota" width="16" height="16" border="0"
                 style="float:left; margin-left:5px; margin-top:5px;" /></a>
            <br/>
        <table class="rotasGrid"></table>
    </fieldset>

    <fieldset style="width:875px; float:left; margin-left:5px; margin-top:10px; overflow:hidden;">
        <legend>Cotas da Rota</legend>
        <span style="float:left; margin-bottom:10px; margin-left:3px; margin-top:5px;">
            <strong>Box:</strong><span id="boxSelecionado"/><strong>- Roteiro Selecionado:</strong><span id="roteiroSelecionado"/><strong> - Rota: </strong><span id="rotaSelecionada"/>
        </span>
        <br clear="all" />
        <table class="cotasRotaGrid"></table>

        <table width="100%" border="0" cellspacing="1" cellpadding="1">
        <tr>
            <td>
                <span class="bt_novos" title="Novo"><a href="javascript:;" onclick="popup_novo_dado();">
                    <img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0"/>Novo</a>
                </span>

                 <span class="bt_novos" title="Adicionar"><a href="javascript:;" onclick="popup_cotas_ausentes();">
                    <img src="${pageContext.request.contextPath}//images/ico_add.gif" hspace="5" border="0"/>Adicionar</a>
                </span>

                <span class="bt_novos trans_cota" title="Transferência de Roteiro"><a href="javascript:;" onclick="popup_tranferir_cota();">
                     <img src="${pageContext.request.contextPath}/images/ico_integrar.png" hspace="5" border="0"/>Transferir</a>
                </span>

                <span class="bt_novos" title="Copiar Cota"><a href="javascript:;" onclick="popup_copiar_cota();">
                    <img src="${pageContext.request.contextPath}/images/ico_detalhes.png" hspace="5" border="0"/>Copiar Cota</a>
                </span>

                <span class="bt_novos" title="Excluir"><a href="javascript:;" onclick="popup_excluir();">
                    <img src="${pageContext.request.contextPath}//images/ico_excluir.gif" hspace="5" border="0"/>Excluir</a>
                </span>
            </td>
            <td>
                 <span class="bt_sellAll" style="float:right;"><label for="sel">Selecionar Todos</label>
                    <input type="checkbox" id="sel" name="Todos" onclick="checkAll();" style="float:left; margin-right:15px; "/>
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
              <td width="42">Cota:</td>
              <td width="170"><input name="numeroCotaPesquisa" type="text" id="numeroCotaPesquisa" style="width:80px; float:left; margin-right:5px;" />
              <span class="classPesquisar"><a href="javascript:;" onclick="roteirizacao.pesquisarPvsPorCota();">&nbsp;</a></span></td>
              <td width="41">Nome:</td>
              <td colspan="4"><span  id="cotaDisponivelPesquisa"> &nbsp;</span></td>
            </tr>
            <tr>
              <td>UF:</td>
              <td><select name="comboUf" id="comboUf" onchange="roteirizacao.buscalistaMunicipio()" style="width:100px;">
                
              </select></td>
              <td>Munic.</td>
              <td><select name="comboMunicipio" id="comboMunicipio" onchange="roteirizacao.buscalistaBairro()" style="width:150px;">
                <option>Todos</option>
              </select></td>
              <td>Bairro:</td>
              <td width="168"><select name="comboBairro" id="comboBairro" style="width:150px;">
                <option>Todos</option>
              </select></td>
              <td width="36">CEP:</td>
              <td width="87"><input name="cepPesquisa" type="text" id="cepPesquisa" style="width:80px;" /></td>
              <td width="79"><span class="bt_novos"><a href="javascript:;" onclick="roteirizacao.buscarPvsPorEndereco();"><img src="${pageContext.request.contextPath}/images/ico_pesquisar.png" border="0" /></a></span></td>
            </tr>
          </table>
	</fieldset>
    
    <fieldset style="width:800px; float:left; margin-top:5px;">
		<legend>Cotas Dispon&icirc;veis</legend>
		<table class="cotasDisponiveisGrid"></table>
        <table width="121" border="0" align="right" cellpadding="0" cellspacing="0">
		  <tr>
		    <td width="151" align="right"><label for="selecionaTodos" id="textoCheckAllCotas" >Marcar todos</label></td>
		    <td width="31"><input type="checkbox" name="selecionaTodos" id="selecionaTodos" onclick="roteirizacao.checarTodasCotasGrid();"/></td>
		  </tr>
		</table>
        
	</fieldset>
	<br clear="all" />
	</div>
	</form>