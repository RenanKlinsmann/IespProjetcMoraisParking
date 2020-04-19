package InterfaceGrafica;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import InterfaceGrafica.util.Alerts;
import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.services.VeiculoServicos;

public class ControleTelaPrincipal implements Initializable {

	// Cadastros
	@FXML
	private MenuItem menuItemAluno;
	@FXML
	private MenuItem menuItemVisitante;
	@FXML
	private MenuItem menuItemFuncionario;

	@FXML
	public void onMenuItemAlunoAction() {
		loadView("/InterfaceGrafica/VeiculoList.fxml", x -> {} );
	}

	@FXML
	public void onMenuItemVisitanteAction() {
		loadView("/InterfaceGrafica/VeiculoList.fxml", x -> {});
	}

	@FXML
	public void onMenuItemFuncionarioAction() {
		loadView("/InterfaceGrafica/VeiculoList.fxml", x -> {});
	}

	@Override
	public void initialize(URL localizacao, ResourceBundle recursos) {
	}

	// Ocorrências
	@FXML
	private MenuItem menuItemObservacao;
	@FXML
	private MenuItem menuItemAdvertencia;
	@FXML
	private MenuItem menuItemSinistro;

	@FXML
	public void onMenuItemObservacaoAction() {
	}

	@FXML
	public void onMenuItemAdvertenciaAction() {
	}

	@FXML
	public void onMenuItemSinistroAction() {
	}

	// Verificar Veiculos
	@FXML
	private MenuItem menuItemConsultarVeiculo;
	@FXML
	private MenuItem menuItemStatusEstacionamento;

	@FXML
	public void onMenuItemConsultarVeiculoAction() {
		loadView("/InterfaceGrafica/VeiculoList.fxml",  (VeiculoListController controller) -> {
			controller.setVeiculoServicos(new VeiculoServicos());
			controller.updateTableView();
		});
	}

	@FXML
	public void onMenuItemStatusEstacionamentoAction() {
	}

	// Cadastrar Eventos
	@FXML
	private MenuItem menuItemEstacionamentoPrivado;
	@FXML
	private MenuItem menuItemEstacionamentoPrincipal;
	@FXML
	private MenuItem menuItemEstacionamentoBlocos;

	@FXML
	public void onMenuItemEstacionamentoPrivadoAction() {
	}

	@FXML
	public void onMenuItemEstacionamentoPrincipalAction() {
	}

	@FXML
	public void onMenuItemEstacionamentoBlocosAction() {
	}

	// Relatorios
	@FXML
	private MenuItem menuItemRelatorioDiario;
	@FXML
	private MenuItem menuItemRelatorioMensal;
	@FXML
	private MenuItem menuItemRelatorioAnual;

	@FXML
	public void onMenuItemRelatorioDiarioAction() {
	}

	@FXML
	public void onMenuItemRelatorioMensalAction() {
	}

	@FXML
	public void onMenuItemRelatorioAnualAction() {
	}

	// Help!
	@FXML
	private MenuItem menuItemSuporte;
	@FXML
	private MenuItem menuItemSair;

	@FXML
	public void onMenuItemSuporteAction() {
		System.out.println("(83)99999-9999");
	}

	@FXML
	public void onMenuItemSairAction() {
	}

	private synchronized <T> void loadView(String absoluteName, Consumer<T> acaoDeInicializacao ) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVBox = loader.load();

			Scene mainScene = Main.getMainScene();
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();

			Node mainMenu = mainVBox.getChildren().get(0);
			mainVBox.getChildren().clear();
			mainVBox.getChildren().add(mainMenu);
			mainVBox.getChildren().addAll(newVBox.getChildren());
			
			T controller = loader.getController();
			acaoDeInicializacao.accept(controller);

		} catch (IOException e) {
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}

}
