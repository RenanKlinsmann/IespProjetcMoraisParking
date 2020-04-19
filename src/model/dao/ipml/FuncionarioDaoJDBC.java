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
import model.dao.FuncionarioDao;
import model.entities.Funcionario;
import model.entities.Veiculo;

public class FuncionarioDaoJDBC implements FuncionarioDao {
	private Connection conn;

	public FuncionarioDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	// insere ao banco
	public void insert(Funcionario obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("INSERT INTO funcionario " + "(Name, Email, Funcao, VeiculoId) "
					+ "VALUES " + "(?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setString(3, obj.getFuncao());
			st.setInt(4, obj.getVeiculo().getId());

			int rowsAffected = st.executeUpdate();
			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}
				DB.closeResultSet(rs);
			} else {
				throw new DbException("Unexpected error! No rows affected!");
			}
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}

	}

	@Override
	// atualiza os dados
	public void update(Funcionario obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("UPDATE funcionario "
					+ "SET Name = ?, Email = ?, Funcao = ?, VeiculoId = ? " + "WHERE Id = ?");
			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setString(3, obj.getFuncao());
			st.setInt(4, obj.getVeiculo().getId());
			
			st.setInt(6, obj.getId());
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	// deleta o id selecionado
	public void deleteById(Integer id) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("DELETE FROM funcionario WHERE Id = ?");
			st.setInt(1, id);
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	// busca o departamento pelo id
	public Funcionario findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT funcionario.*,veiculo.Name as VeiName " + "FROM funcionario INNER JOIN veiculo "
							+ "ON funcionario.VeiculoId = veiculo.Id " + "WHERE funcionario.Id = ?");

			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				Veiculo vei = instantiateVeiculo(rs);
				Funcionario obj = instantiateFuncionario(rs, vei);
				return obj;
			}
			return null;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	private Funcionario instantiateFuncionario(ResultSet rs, Veiculo vei) throws SQLException {
		Funcionario obj = new Funcionario();
		obj.setId(rs.getInt("Id"));
		obj.setName(rs.getString("Name"));
		obj.setEmail(rs.getString("Email"));
		obj.setFuncao(rs.getString("Funcao"));
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
	public List<Funcionario> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT funcionario.*,veiculo.Name as VeiName " + "FROM funcionario INNER JOIN veiculo "
							+ "ON funcionario.VeiculoId = veiculo.Id " + "ORDER BY Name");

			rs = st.executeQuery();
			List<Funcionario> list = new ArrayList<>();
			Map<Integer, Veiculo> map = new HashMap<>();
			while (rs.next()) {
				Veiculo vei = map.get(rs.getInt("VeiculoId"));
				if (vei == null) {
					vei = instantiateVeiculo(rs);
					map.put(rs.getInt("VeiculoId"), vei);
				}
				Funcionario obj = instantiateFuncionario(rs, vei);
				list.add(obj);
			}
			return list;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Funcionario> findByVeiculo(Veiculo veiculo) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT funcionario.*,veiculo.Name as VeiName " 
					+ "FROM funcionario INNER JOIN veiculo "				
					+ "ON funcionario.VeiculoId = veiculo.Id " 
					+ "WHERE VeiculoId = ? " 
					+ "ORDER BY Name");

			st.setInt(1, veiculo.getId());
			rs = st.executeQuery();
			List<Funcionario> list = new ArrayList<>();
			Map<Integer, Veiculo> map = new HashMap<>();
			while (rs.next()) {
				Veiculo vei = map.get(rs.getInt("VeiculoId"));
				if (vei == null) {
					vei = instantiateVeiculo(rs);
					map.put(rs.getInt("VeiculoId"), vei);
				}
				Funcionario obj = instantiateFuncionario(rs, vei);
				list.add(obj);
			}
			return list;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}


}
