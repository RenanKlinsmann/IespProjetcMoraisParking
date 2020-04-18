package InterfaceGrafica;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Veiculo;
import model.services.VeiculoServicos;

public class VeiculoListController implements Initializable {

	private VeiculoServicos servicos;

	@FXML
	private TableView<Veiculo> tableViewVeiculo;

	@FXML
	private TableColumn<Veiculo, String> tableColumnPlaca;

	@FXML
	private TableColumn<Veiculo, String> tableColumnModelo;

	@FXML
	private TableColumn<Veiculo, String> tableColumnCor;

	@FXML
	private TableColumn<Veiculo, Date> tableColumnAno;

	@FXML
	private Button btNew;

	private ObservableList<Veiculo> obsList;

	@FXML
	public void onBtNewAction() {
		System.out.println("onBtNewAction");
	}

	public void setVeiculoServicos(VeiculoServicos servico) {
		this.servicos = servico;
	}

	@Override
	public void initialize(URL localizacao, ResourceBundle recursos) {
		initializeNodes();
	}

	private void initializeNodes() {
		tableColumnPlaca.setCellValueFactory(new PropertyValueFactory<>("Placa"));
		tableColumnModelo.setCellValueFactory(new PropertyValueFactory<>("Modelo"));
		tableColumnCor.setCellValueFactory(new PropertyValueFactory<>("Cor"));
		tableColumnAno.setCellValueFactory(new PropertyValueFactory<>("Ano"));

		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewVeiculo.prefHeightProperty().bind(stage.heightProperty());

	}

	public void updateTableView() {

		if (servicos == null) {
			throw new IllegalStateException("Service was null");
		}

		List<Veiculo> list = servicos.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewVeiculo.setItems(obsList);

	}

}
