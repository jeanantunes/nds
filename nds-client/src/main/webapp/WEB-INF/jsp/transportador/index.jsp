<head>
	<script type="text/javascript" src='<c:url value="/"/>/scripts/jquery.numeric.js'></script>
	<script type="text/javascript" src="scripts/transportador.js"></script>

	<style>
		.diasFunc label,.finceiro label {
			vertical-align: super;
		}
		
		#tabs-4 .especialidades fieldset {
			width: 220px !important;
			margin-left: 4px;
			margin-bottom:5px;
			width: 258px !important;
		}
		
		#tabs-4 .bt_novos,#tabs-4 .bt_confirmar_novo {
			margin-left: -14px !important;
		}
		
		#dialog-incluir-veiculo fieldset,#dialog-incluir-motorista fieldset {
			width: 370px !important;
		}
		
		#dialog-incluir-veiculo,#dialog-incluir-motorista,#dialog-excluir-associacao{
			display: none;
		}
		
		.associacao {
			width: 835px !important;
			margin-left: 7px !important;
			margin-bottom:5px;
		}
	</style>
</head>

<body>

	<form id="form-excluir-associacao">
	<div id="dialog-excluir-associacao" title="Veículos / Motoristas / Rota / Roteiro">
		<p>Confirma a exclusão desta associação de Veículos / Motoristas / Rota / Roteiro?</p>
	</div>
	</form>

	<form id="form-excluir-transportador">
	<div id="dialog-excluir" title="Excluir Transportador">
		<p>Confirma a exclusão deste Transportador</p>
	</div>
	</form>
	
	<form id="form-excluir-veiculo">
	<div id="dialog-excluir-veiculo" title="Excluir Veículo" style="display: none;">
		<p>Confirma a exclusão deste Veículo</p>
	</div>
	</form>
	
	<form id="form-excluir-motorista">
	<div id="dialog-excluir-motorista" title="Excluir Motorista" style="display: none;">
		<p>Confirma a exclusão deste Motorista</p>
	</div>
	</form>
	
	<form id="form-cancelar-cadastro-transportador">
	<div id="dialog-cancelar-cadastro-transportador" title="Transportadores" style="display: none;">
		<p>Dados não salvos serão perdidos. Confirma o cancelamento?</p>
	</div>
	</form>

	<form id="form-incluir-motorista">
	<div id="dialog-incluir-motorista" title="Motoristas">
		<fieldset>
			
			<jsp:include page="../messagesDialog.jsp">
		
				<jsp:param value="idModalCadastroMotorista" name="messageDialog"/>
	
			</jsp:include>
			
			<legend>Cadastrar Motorista</legend>
			<table width="350" cellpadding="2" cellspacing="2" style="text-align: left;">
				<tr>
					<td width="54">Nome:</td>
					<td width="280"><input type="text" style="width: 230px" id="nomeMotorista" maxlength="255" /></td>
				</tr>
				<tr>
					<td>CNH:</td>
					<td><input type="text" style="width: 110px" id="cnhMotorista" maxlength="255" /></td>
				</tr>
				<tr id="btnAddMotorista">
					<td>&nbsp;</td>
					<td><span class="bt_add"><a href="javascript:transportadorController.adicionarMotorista();">Incluir Novo</a></span></td>
				</tr>
			</table>
		</fieldset>
	</div>
	</form>

	<form id="form-incluir-veiculo">
	<div id="dialog-incluir-veiculo" title="Veículos">
		<fieldset>
			
			<jsp:include page="../messagesDialog.jsp">
		
				<jsp:param value="idModalCadastroVeiculo" name="messageDialog"/>
	
			</jsp:include>
			
			<legend>Cadastrar Veículos</legend>
			<table width="350" cellpadding="2" cellspacing="2" style="text-align: left;">
				<tr>
					<td width="95">Tipo de Veículo:</td>
					<td width="239"><input type="text" style="width: 230px" id="tipoVeiculo" maxlength="255" /></td>
				</tr>
				<tr>
					<td>Placa:</td>
					<td><input type="text" style="width: 110px" id="placa" maxlength="255" /></td>
				</tr>
				<tr id="btnAddVeiculo">
					<td>&nbsp;</td>
					<td><span class="bt_add"><a href="javascript:transportadorController.adicionarVeiculo();">Incluir Novo</a></span></td>
				</tr>
			</table>
		</fieldset>
	</div>
	</form>

	<form id="form-novo-transportador">
	<div id="dialog-novo" title="Novo Transportador">
	
		<jsp:include page="../messagesDialog.jsp">
		
			<jsp:param value="idModalCadastroTransportador" name="messageDialog"/>

		</jsp:include>
		
		<div id="tabs">
			<ul>
				<li><a href="#tabs-1" onclick="transportadorController.carregarCotasAtendidas();">Dados Cadastrais</a></li>
				<li><a href="#tabs-2" onclick="ENDERECO_TRANSPORTADOR.popularGridEnderecos();">Endereços</a></li>
				<li><a href="#tabs-3" onclick="TRANSPORTADOR.carregarTelefones();">Telefones</a></li>
				<li><a href="#tabs-4" onclick="transportadorController.carregarGrids();">Veículos / Motoristas</a></li>
			</ul>
			<div id="tabs-1">
				<fieldset style="width:880px; margin:5px;">
					<legend>Dados Cadastrais</legend>
					<table width="850" cellpadding="2" cellspacing="2" style="text-align: left;">
						<tr>
							<td width="98">Razão Social:</td>
							<td width="248"><input type="text" style="width: 230px" id="razaoSocial" maxlength="255" /></td>
							<td width="113">Nome Fantasia:</td>
							<td width="243"><input type="text" style="width: 230px" id="nomeFantasia" maxlength="255" /></td>
						</tr>
						<tr>
							<td>E-mail:</td>
							<td><input type="text" style="width: 230px" id="email" maxlength="255" /></td>
							<td>Responsável:</td>
							<td><input type="text" style="width: 230px" id="responsavel" maxlength="255" /></td>
						</tr>
						<tr>
							<td>CNPJ:</td>
							<td><input type="text" style="width: 150px" id="cnpj" onblur="transportadorController.buscarPessoaCNPJ(this.value);" /></td>
							<td>Insc. Estadual:</td>
							<td><input type="text" style="width: 150px" id="inscEstadual" /></td>
						</tr>
					</table>
					<table width="850" cellpadding="3" cellspacing="2">
						<tr>
							<td width="117">Cobrança</td>
							<td>
								<select style="width: 100px;" id="modelidadeCobranca">
									<option onclick="transportadorController.mostrarOpcaoTaxaFixa();" value="TAXA_FIXA">Taxa Fixa</option>
									<option onclick="transportadorController.mostrarOpcaoPercentual()" value="PERCENTUAL">Percentual</option>
								</select>
							</td>
							<td class="transpTaxaFixa">Valor R$:</td>
							<td class="transpTaxaFixa">
								<input type="text" id="valorTaxaFixa"/>
							</td>
							
							<td width="180" class="transpPercentual" style="display: none;">Percentual Faturamento Preço Capa:</td>
							<td class="transpPercentual" style="display: none;">
								<input type="text" id="valorPercentualFaturamento"/>
							</td>
							
							<td>
								<input type="checkbox" id="checkPorEntrega" />
							</td>
							<td>
								Por Entrega
							</td>
						</tr>
					</table>
					
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
					        <td width="20%">Periodicidade de Cobrança:</td>
					        <td width="3%">
					        	<input name="radioPeriodicidade" type="radio" value="DIARIO" 
					        		onclick="transportadorController.alterarPeriodicidadeCobranca(this.value);" id="radioPeridioDiario" />
					        </td>
					        <td width="7%">Diário</td>
					        <td width="3%">
					        	<input name="radioPeriodicidade" type="radio" value="SEMANAL" 
					        		onclick="transportadorController.alterarPeriodicidadeCobranca(this.value);" />
					        </td>
					        <td width="9%">Semanal</td>
					        <td width="3%">
					        	<input name="radioPeriodicidade" type="radio" value="QUINZENAL" 
					        		onclick="transportadorController.alterarPeriodicidadeCobranca(this.value);" />
					        </td>
					        <td width="10%">Quinzenal</td>
					        <td width="3%">
					        	<input name="radioPeriodicidade" type="radio" value="MENSAL" 
					        		onclick="transportadorController.alterarPeriodicidadeCobranca(this.value);" />
					        </td>
					        <td width="42%">Mensal</td>
						</tr>
					</table>
          			<table width="100%" border="0" cellspacing="1" cellpadding="1" class="perCobrancaSemanal" style="display: none;">
	            		<tr class="checksDiasSemana">
							<td width="20"><input type="checkbox" id="checkSegunda" value="SEGUNDA_FEIRA" name="diaSemanaCob" /></td>
							<td width="98">Segunda-feira</td>
							<td width="20"><input type="checkbox" id="checkTerca" value="TERCA_FEIRA" name="diaSemanaCob" /></td>
							<td width="80">Terça-feira</td>
							<td width="20"><input type="checkbox" id="checkQuarta" value="QUARTA_FEIRA" name="diaSemanaCob" /></td>
							<td width="83">Quarta-feira</td>
							<td width="20"><input type="checkbox" id="checkQuinta" value="QUINTA_FEIRA" name="diaSemanaCob" /></td>
							<td width="85">Quinta-feira</td>
							<td width="20"><input type="checkbox" id="checkSexta" value="SEXTA_FEIRA" name="diaSemanaCob" /></td>
							<td width="79">Sexta-feira</td>
							<td width="20"><input type="checkbox" id="checkSabado" value="SABADO" name="diaSemanaCob" /></td>
							<td width="54">Sábado</td>
							<td width="20"><input type="checkbox" id="checkDomingo" value="DOMINGO" name="diaSemanaCob" /></td>
							<td width="58">Domingo</td>
            			</tr>
          			</table>
          			<table width="100%" border="0" cellspacing="1" cellpadding="1" class="perCobrancaQuinzenal" style="display: none;">
        				<tr>
							<td width="396" height="24" align="right">Todo dia:&nbsp;</td>
							<td width="69">
								<input type="text" id="inputQuinzenalDiaInicio" 
									onkeyup="transportadorController.calcularDiaFimCobQuinzenal();" style="width:60px;"/>
							</td>
							<td width="21" align="center">e</td>
							<td width="271">
								<input type="text" id="inputQuinzenalDiaFim" disabled="disabled" style="width:60px;"/>
							</td>
      					</tr>
					</table>
					<table width="100%" border="0" cellspacing="1" cellpadding="1" class="perCobrancaMensal" style="display: none;">
      					<tr>
							<td width="495" align="right">Todo dia:&nbsp;</td>
							<td width="268"><input type="text" id="inputCobrancaMensal" style="width:60px;"/></td>
      					</tr>
    				</table> 
    				</fieldset>
    				<div class="linha_separa_fields">&nbsp;</div>   				
    				
					<fieldset style="width:880px; margin:5px;">
						<legend>Cotas Atendidas</legend>
						<table id="gridCotasAtendidas"></table>
						<span class="bt_novos">
							<a href="javascript:;" onclick="transportadorController.exportarArquivo('XLS');" target="_blank" rel="tipsy" title="Gerar Arquivo">
								<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
							</a>
						</span>
						
						<span class="bt_novos">
							<a href="javascript:;" onclick="transportadorController.exportarArquivo('PDF');" target="_blank" rel="tipsy" title="Imprimir">
								<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" alt="Imprimir" hspace="5" border="0" />
							</a>
						</span>
					</fieldset>
					
				</fieldset>
				<br clear="all" />
			</div>
			<div id="tabs-2">
					<jsp:include page="../endereco/index.jsp">
						<jsp:param value="ENDERECO_TRANSPORTADOR" name="telaEndereco"/>
						<jsp:param value="idModalCadastroTransportador" name="message"/>
					</jsp:include>

			</div>
			<div id="tabs-3">
					<jsp:include page="../telefone/index.jsp">
						<jsp:param value="TRANSPORTADOR" name="tela"/>
						<jsp:param value="idModalCadastroTransportador" name="message"/>
					</jsp:include>
			</div>

			<div id="tabs-4">
				<fieldset  style="width:880px; margin:5px;">
					<legend>Veículos / Motoristas</legend>
					<table border="0" cellpadding="2" cellspacing="2" align="center" width="700">
						<tr class="especialidades">
							<td valign="top">
								<fieldset>
									<legend>Veículos</legend>
									<table class="veiculosGrid"></table>
								</fieldset>
							</td>
							<td valign="top">
								<fieldset>
									<legend>Motoristas</legend>
									<table class="motoristasGrid"></table>
								</fieldset>
							</td>
							<td valign="top">
								<fieldset>
									<legend>Rota / Roteiro</legend>
									<table class="boxRotaGrid"></table>
								</fieldset>
							</td>
						</tr>
						<tr class="especialidades">
							<td valign="top">
								<span class="bt_novos">
									<a href="javascript:;" onclick="transportadorController.popup_incluir_veiculo();" rel="tipsy" title="Incluir Novo Veículo">
										<img src="${pageContext.request.contextPath}/images/ico_veiculo.gif" hspace="5" border="0" />
									</a>
								</span>
							</td>
							<td valign="top">
								<span class="bt_novos">
									<a href="javascript:;" onclick="transportadorController.popup_incluir_motorista();" rel="tipsy" title="Incluir Novo Motorista">
										<img src="${pageContext.request.contextPath}/images/ico_jornaleiro.gif" hspace="5" border="0" />
									</a>
								</span>
							</td>
							<td valign="top">
								<span class="bt_novos"	title="Confirmar">
									<a href="javascript:;" onclick="transportadorController.confirmarAssociacao();" rel="tipsy" title="Confirmar Associação">
										<img border="0" src="${pageContext.request.contextPath}/images/ico_check.gif">
									</a>
								</span>
							</td>
						</tr>
					</table>
				<br clear="all" />
				<fieldset class="associacao">
					<legend>Veículos / Motoristas / Rota / Roteiro</legend>
					<table class="associacaoGrid"></table>
				</fieldset>
				<br clear="all" />
				</fieldset>
			</div>
		</div>
	</div>
	</form>
	
	<div class="areaBts">
		<div class="area">
			<span class="bt_novos">
				<a href="javascript:;" onclick="transportadorController.limparCamposCadastroTransportador();transportadorController.popup_novo_transportador();" rel="tipsy" title="Incluir Novo Transportador">
					<img src="${pageContext.request.contextPath}/images/ico_salvar.gif"	hspace="5" border="0" />
			</a>
		</span>
		</div>
	</div>
	<div class="linha_separa_fields">&nbsp;</div>
	<fieldset class="fieldFiltro">
		<legend> Pesquisar Transportador </legend>
		<table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
			<tr>
				<td width="85">Razão Social:</td>
				<td colspan="3"><input type="text" id="razaoSocialPesquisa" style="width: 190px;" maxlength="255" /></td>
				<td width="104">Nome Fantasia:</td>
				<td width="192"><input type="text" id="nomeFantasiaPesquisa" style="width: 190px;" maxlength="255" /></td>
				<td width="42">CNPJ:</td>
				<td width="179"><input type="text" id="cnpjPesquisa" style="width: 110px;" maxlength="255" /></td>
				<td width="109"><span class="bt_novos">
					<a href="javascript:;" onclick="transportadorController.pesquisarTransportadores();"><img src="${pageContext.request.contextPath}/images/ico_pesquisar.png" border="0" /></a></span>
				</td>
			</tr>
		</table>

	</fieldset>
	<div class="linha_separa_fields">&nbsp;</div>
	<fieldset class="fieldGrid">
		<legend>Transportadores Cadastrados</legend>
		<div class="divTransportadoraGrid" style="display: none;">
			<table class="transportadoraGrid"></table>
		</div>
	</fieldset>
	
	<script>
	
	$(function() {
		
		transportadorController.init();
		
		ENDERECO_TRANSPORTADOR.init(transportadorController.workspace);
		
		TRANSPORTADOR.init(transportadorController.workspace);
		
		
	});
	
	</script>
	
			
</body>