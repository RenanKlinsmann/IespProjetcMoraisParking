package model.dao;

import java.util.List;

import model.entities.Veiculo;
import model.entities.Visitante;

public interface VisitanteDao {

	void insert(Visitante obj);
	void update(Visitante obj);
	void deleteById(Integer id);
	Visitante findById(Integer id);
	List<Visitante> findAll();
	List<Visitante> findByVeiculo(Veiculo veiculo);
}
