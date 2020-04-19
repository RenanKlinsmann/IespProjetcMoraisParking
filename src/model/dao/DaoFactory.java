package model.dao;

import db.DB;
import model.dao.ipml.AlunoDaoJDBC;
import model.dao.ipml.FuncionarioDaoJDBC;
import model.dao.ipml.VeiculoDaoJDBC;
import model.dao.ipml.VisitanteDaoJDBC;

public class DaoFactory {

	public static VeiculoDao createVeiculoDao() {
		return new VeiculoDaoJDBC(DB.getConnection());
	}
	
	public static AlunoDao createAlunoDao() {
		return new AlunoDaoJDBC(DB.getConnection());
	}
	
	public static FuncionarioDao createFuncionarioDao() {
		return new FuncionarioDaoJDBC(DB.getConnection());
	}
	
	public static VisitanteDao createVisitanteDao() {
		return new VisitanteDaoJDBC(DB.getConnection());
	}
	
}
