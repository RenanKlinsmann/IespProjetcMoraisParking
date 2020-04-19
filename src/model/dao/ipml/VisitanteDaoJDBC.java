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
import model.dao.VisitanteDao;
import model.entities.Veiculo;
import model.entities.Visitante;

public class VisitanteDaoJDBC implements VisitanteDao {

	private Connection conn;

	public VisitanteDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	// insere ao banco
	public void insert(Visitante obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("INSERT INTO visitante " + "(Name, Email, Cpf, VeiculoId) "
					+ "VALUES " + "(?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setString(3, obj.getCpf());
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
	public void update(Visitante obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("UPDATE visitante "
					+ "SET Name = ?, Email = ?, Cpf = ?, VeiculoId = ? " + "WHERE Id = ?");
			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setString(3, obj.getCpf());
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
			st = conn.prepareStatement("DELETE FROM visitante WHERE Id = ?");
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
	public Visitante findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT visitante.*,veiculo.Name as VeiName " + "FROM visitante INNER JOIN veiculo "
							+ "ON visitante.VeiculoId = veiculo.Id " + "WHERE visitante.Id = ?");

			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				Veiculo vei = instantiateVeiculo(rs);
				Visitante obj = instantiateVisitante(rs, vei);
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

	private Visitante instantiateVisitante(ResultSet rs, Veiculo vei) throws SQLException {
		Visitante obj = new Visitante();
		obj.setId(rs.getInt("Id"));
		obj.setName(rs.getString("Name"));
		obj.setEmail(rs.getString("Email"));
		obj.setCpf(rs.getString("Cpf"));
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
	public List<Visitante> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT visitante.*,veiculo.Name as VeiName " + "FROM visitante INNER JOIN veiculo "
							+ "ON visitante.VeiculoId = veiculo.Id " + "ORDER BY Name");

			rs = st.executeQuery();
			List<Visitante> list = new ArrayList<>();
			Map<Integer, Veiculo> map = new HashMap<>();
			while (rs.next()) {
				Veiculo vei = map.get(rs.getInt("VeiculoId"));
				if (vei == null) {
					vei = instantiateVeiculo(rs);
					map.put(rs.getInt("VeiculoId"), vei);
				}
				Visitante obj = instantiateVisitante(rs, vei);
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
	public List<Visitante> findByVeiculo(Veiculo veiculo) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT visitante.*,veiculo.Name as VeiName " + "FROM visitante INNER JOIN veiculo "
							+ "ON visitante.VeiculoId = veiculo.Id " + "WHERE VeiculoId = ? " + "ORDER BY Name");

			st.setInt(1, veiculo.getId());
			rs = st.executeQuery();
			List<Visitante> list = new ArrayList<>();
			Map<Integer, Veiculo> map = new HashMap<>();
			while (rs.next()) {
				Veiculo vei = map.get(rs.getInt("VeiculoId"));
				if (vei == null) {
					vei = instantiateVeiculo(rs);
					map.put(rs.getInt("VeiculoId"), vei);
				}
				Visitante obj = instantiateVisitante(rs, vei);
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
