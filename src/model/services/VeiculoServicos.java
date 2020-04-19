package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.VeiculoDao;
import model.entities.Veiculo;

public class VeiculoServicos {
	
	
	private VeiculoDao dao = DaoFactory.createVeiculoDao();

	public List<Veiculo> findAll() {
		return dao.findAll();
	}
}