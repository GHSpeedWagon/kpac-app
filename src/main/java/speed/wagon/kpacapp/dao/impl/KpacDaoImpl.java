package speed.wagon.kpacapp.dao.impl;

import org.springframework.stereotype.Repository;
import speed.wagon.kpacapp.dao.KpacDao;
import speed.wagon.kpacapp.model.Kpac;
import speed.wagon.kpacapp.util.ConnectionUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Repository
public class KpacDaoImpl implements KpacDao {
    private final ConnectionUtil connectionUtil;

    public KpacDaoImpl(ConnectionUtil connectionUtil) {
        this.connectionUtil = connectionUtil;
    }

    @Override
    public Kpac save(Kpac kpac) {
        String query = "INSERT IGNORE INTO "
                + "kpaces (title, description, creation_date) "
                + "VALUES (?, ?, ?)";
        try (Connection connection = connectionUtil.getConnection();
            PreparedStatement statement
                    = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, kpac.getTitle());
            statement.setString(2, kpac.getDescription());
            statement.setDate(3, java.sql.Date.valueOf(kpac.getCreationDate()));
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                kpac.setId(resultSet.getLong(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Can't insert kpac to DB", e);
        }
        return kpac;
    }

    @Override
    public void delete(Long id) {
        String query = "DELETE FROM kpaces WHERE id = ?";
        try (Connection connection = connectionUtil.getConnection();
             PreparedStatement statement
                     = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Can't delete entity with id=" + id + " from DB", e);
        }
    }

    @Override
    public Kpac get(Long id) {
        Kpac kpac = new Kpac();
        String query = "SELECT * from kpaces WHERE id = ?";
        try (Connection connection = connectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                kpac = parseKpacFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Can't get entity with id=" + id + " from DB", e);
        }
        return kpac;
    }

    @Override
    public List<Kpac> getAll() {
        String query = "SELECT * FROM kpaces";
        List<Kpac> kpacList = new ArrayList<>();
        try (Connection connection = connectionUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Kpac kpac = parseKpacFromResultSet(resultSet);
                kpacList.add(kpac);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Can't get all entities from DB", e);
        }
        return kpacList;
    }

    private Kpac parseKpacFromResultSet(ResultSet resultSet) throws SQLException {
        Long kpacId = resultSet.getLong("id");
        String kpacTitle = resultSet.getString("title");
        String kpacDescription = resultSet.getString("description");
        java.sql.Date creationDate = resultSet.getDate("creation_date");
        Kpac kpac = new Kpac();
        kpac.setId(kpacId);
        kpac.setTitle(kpacTitle);
        kpac.setDescription(kpacDescription);
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        kpac.setCreationDate(creationDate.toLocalDate().format(dateFormat));
        return kpac;
    }
}
