
<head>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/pesquisaCota.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/cotaAusente.js"></script>

<script language="javascript" type="text/javascript">

var pesquisaCotaCotaAusente = new PesquisaCota();

$(function(){
	cotaAusenteController.init();
});

</script>
<style>
.linha_1, .linha_2, .linha_3, .linha_4, .linha_5, .linha_6, .linha_21, .linha_22, .linha_31, .linha_32 {display:none;}
#dialog-suplementar fieldset{width:350px!important;}
#dialog-suplementar .linha_separa_fields{width:350px!important;}

</style>
</head>

<body>


	 <form>
		 
        <div class="area">
        
	        <span class="bt_novos">
			    <a href="javascript:;" rel="tipsy" title="Incluir Nova Cota Ausente" onclick="cotaAusenteController.popupNovaCotaAusente();">
			        <img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0"/>
			    </a>
		    </span>
	  
 		  	<span class="bt_arq">
				<a rel="tipsy" title="Gerar Arquivo" href="${pageContext.request.contextPath}/cotaAusente/exportar?fileType=XLS">
					<img src="${pageContext.request.contextPath}/images/ico_excel.png" border="0" />
				</a>
			</span>
			
 		  	<span class="bt_arq">
				<a rel="tipsy" title="Imprimir" href="${pageContext.request.contextPath}/cotaAusente/exportar?fileType=PDF">
					<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" border="0" />
				</a>
			</span>
			
	   	</div>
	   	
	 </form>


<form id="form-confirm">
<div id="dialog-confirm" title="Suplementar">
	<p>Para onde deseja direcionar o reparte desta cota?</p>
</div>
</form>

<form id="form-excluir">
<div id="dialog-excluir" title="Cota Ausente">
	<fieldset class="classFieldset" style="width:520px !important;">
		<legend>Quantidade de Produtos Estoque Suplementar</legend>
		
		<p style="padding: 10px;">Confirma a exclusão dessa Cota Ausente?</p>
		
		<div id="flexiGridProdutoEstoqueSuplementar"></div>
		
	</fieldset>
</div>
</form>


<form id="form-novo">
<div id="dialog-novo" title="Incluir Cota Ausente"> 

	<jsp:include page="../messagesDialog.jsp" />
  
 
    <table id="idCotas" width="500" border="0" cellpadding="2" cellspacing="1" class="filtro">
		
	</table>
</div>
</form>
   
<div class="container">
    
	<fieldset class="classFieldset">
   		
   		<legend> Pesquisar Cotas Ausentes</legend>
        
        <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
        	<tr>
            	<td width="35">Data:</td>
            	<td>
					<!--DATA-->
					<input id="idData" value="${data}" type="text" name="datepickerDe" style="width:80px;" />
				</td>
              
                <td width="38">Cota:</td>
                <td width="123">
					<!-- COTA -->                
					<input id="idCota" name="idCota" type="text" style="width:80px; float:left; margin-right:5px;" 
						   onchange="pesquisaCotaCotaAusente.pesquisarPorNumeroCota('#idCota', '#idNomeCota');"/>
				</td>
				
				<!-- PESQUISAR NOME COTA -->
				<td width="40">Nome:</td>
                <td width="296">
					<!-- NOME -->            
					<input id="idNomeCota" name="idNomeCota" type="text" class="nome_jornaleiro" style="width:280px;" 
						   onkeyup="pesquisaCotaCotaAusente.autoCompletarPorNome('#idNomeCota');" 
		 	   			   onblur="pesquisaCotaCotaAusente.pesquisarPorNomeCota('#idCota', '#idNomeCota');"/>
				</td>
                
                <td width="27">Box:</td>
                <td width="111">
					<!-- BOX -->
					<input id="idBox" type="text" name="textfield" id="textfield" style="width:80px;"/>
				</td>
              </tr>
              <tr>
	           		<td width="91">Roteiro:</td>
   	 				<td width="215">
   	 					<select id="selectRoteiro" style="width:200px; font-size:11px!important">
      						<option value="">Selecione...</option>
      						<c:forEach items="${roteiros}" var="roteiro">
								<option value="${roteiro.key }">${roteiro.value }</option>
							</c:forEach>
    					</select>
    				</td>
    					
    				<td width="93">Rota:</td>
    				<td>
    					<select id="selectRota" style="width:150px; font-size:11px!important">
      						<option value="">Selecione...</option>
      						<c:forEach items="${rotas}" var="rota">
								<option value="${rota.key }">${rota.value }</option>
							</c:forEach>
    					</select>
    				</td>
    				
              	<td width="114">
					<!-- PESQUISAR -->
					<span class="bt_novos">
						<a href="javascript:;" onclick="cotaAusenteController.cliquePesquisar();">
							<img src="${pageContext.request.contextPath}/images/ico_pesquisar.png" border="0" />
						</a>
					</span>
				</td>
				<td></td>
			  </tr>
		</table>

      </fieldset>
      
      <div class="linha_separa_fields">&nbsp;</div>
      
      <fieldset class="classFieldset">
       	  <legend>Cotas Ausentes Cadastradas</legend>
        <div class="grids" style="display:none;">
       	  <table class="ausentesGrid"></table>
          <br />
        </div>
      </fieldset>
      
      <div class="linha_separa_fields">&nbsp;</div>
      
    </div>
</div> 

<form id="formRateio">
	<jsp:include page="modalRateio.jsp"/>
</form>
</body>
