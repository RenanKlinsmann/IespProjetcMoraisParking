package model.dao;

import java.util.List;

import model.entities.Aluno;
import model.entities.Veiculo;

public interface AlunoDao {
	
	void insert(Aluno obj);
	void update(Aluno obj);
	void deleteById(Integer id);
	Aluno findById(Integer id);
	List<Aluno> findAll();
	List<Aluno> findByVeiculo(Veiculo veiculo);
}
