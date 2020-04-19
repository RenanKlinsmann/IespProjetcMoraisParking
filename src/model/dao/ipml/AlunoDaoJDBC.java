package model.dao.ipml;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.AlunoDao;
import model.entities.Aluno;
import model.entities.Veiculo;

public class AlunoDaoJDBC implements AlunoDao {

	private Connection conn;

	public AlunoDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	// insere ao banco
	public void insert(Aluno obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("INSERT INTO aluno " 
								            + "(Name, Email, Matricula, VeiculoId) "
											+ "VALUES " + "(?, ?, ?, ?)", 
											Statement.RETURN_GENERATED_KEYS);

			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setInt(3, obj.getMatricula());
			st.setInt(4, obj.getVeiculo().getId());

			int rowsAffected = st.executeUpdate();
			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}
				DB.closeResultSet(rs);
			} 
			else {
				throw new DbException("Unexpected error! No rows affected!");
			}
		} 
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		} 
		finally {
			DB.closeStatement(st);
		}

	}

	@Override
	// atualiza os dados
	public void update(Aluno obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("UPDATE aluno "
										+ "SET Name = ?, "
										+ "Email = ?, "
										+ "Matricula = ?, "
										+ "VeiculoId = ? "
										+ "WHERE Id = ?");
			
			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setInt(3, obj.getMatricula());
			st.setInt(4, obj.getVeiculo().getId());
			
			st.setInt(6, obj.getId());
			st.executeUpdate();
		} 
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		} 
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	// deleta o id selecionado
	public void deleteById(Integer id) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("DELETE FROM aluno WHERE Id = ?");
			st.setInt(1, id);
			st.executeUpdate();
		} 
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		} 
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	// busca o departamento pelo id
	public Aluno findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
										"SELECT aluno.*,veiculo.Name as VeiName " 
												+ "FROM aluno INNER JOIN veiculo "
												+ "ON aluno.VeiculoId = veiculo.Id " 
												+ "WHERE aluno.Id = ?");

			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				Veiculo vei = instantiateVeiculo(rs);
				Aluno obj = instantiateAluno(rs, vei);
				return obj;
			}
			return null;
		} 
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		} 
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	private Aluno instantiateAluno(ResultSet rs, Veiculo vei) throws SQLException {
		Aluno obj = new Aluno();
		obj.setId(rs.getInt("Id"));
		obj.setName(rs.getString("Name"));
		obj.setEmail(rs.getString("Email"));
		obj.setMatricula(rs.getInt("Matricula"));
		obj.setVeiculo(vei);
		return obj;
	}

	private Veiculo instantiateVeiculo(ResultSet rs) throws SQLException {
		Veiculo vei = new Veiculo();
		vei.setId(rs.getInt("VeiculoId"));
		vei.setModelo(rs.getString("VeiModelo"));
		vei.setPlaca(rs.getString("VeiPlaca"));
		vei.setCor(rs.getString("VeiCor"));
		vei.setAno(rs.getString("VeiAno"));
		return vei;
	}

	@Override
	// busca todos os vendedores(Seller)
	public List<Aluno> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
							           "SELECT aluno.*,veiculo.Name as VeiName " 
													+ "FROM aluno INNER JOIN veiculo "
													+ "ON aluno.VeiculoId = veiculo.Id " 
													+ "ORDER BY Name");

			rs = st.executeQuery();
			List<Aluno> list = new ArrayList<>();
			Map<Integer, Veiculo> map = new HashMap<>();
			while (rs.next()) {
				Veiculo vei = map.get(rs.getInt("VeiculoId"));
				if (vei == null) {
					vei = instantiateVeiculo(rs);
					map.put(rs.getInt("VeiculoId"), vei);
				}
				Aluno obj = instantiateAluno(rs, vei);
				list.add(obj);
			}
			return list;
		} 
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		} 
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Aluno> findByVeiculo(Veiculo veiculo) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
										"SELECT aluno.*,veiculo.Name as VeiName "
											+ "FROM aluno INNER JOIN veiculo "				
											+ "ON aluno.VeiculoId = veiculo.Id " 
											+ "WHERE VeiculoId = ? " 
											+ "ORDER BY Name");

			st.setInt(1, veiculo.getId());
			rs = st.executeQuery();
			List<Aluno> list = new ArrayList<>();
			Map<Integer, Veiculo> map = new HashMap<>();
			while (rs.next()) {
				Veiculo vei = map.get(rs.getInt("VeiculoId"));
				if (vei == null) {
					vei = instantiateVeiculo(rs);
					map.put(rs.getInt("VeiculoId"), vei);
				}
				Aluno obj = instantiateAluno(rs, vei);
				list.add(obj);
			}
			return list;
		} 
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		} 
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

}
