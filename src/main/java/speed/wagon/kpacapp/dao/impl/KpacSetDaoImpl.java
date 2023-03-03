package speed.wagon.kpacapp.dao.impl;

import org.springframework.stereotype.Repository;
import speed.wagon.kpacapp.dao.KpacDao;
import speed.wagon.kpacapp.dao.KpacSetDao;
import speed.wagon.kpacapp.model.Kpac;
import speed.wagon.kpacapp.model.KpacSet;
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
public class KpacSetDaoImpl implements KpacSetDao {
    private final ConnectionUtil connectionUtil;
    private final KpacDao kpacDao;

    public KpacSetDaoImpl(ConnectionUtil connectionUtil, KpacDao kpacDao) {
        this.connectionUtil = connectionUtil;
        this.kpacDao = kpacDao;
    }

    @Override
    public KpacSet add(KpacSet kpacSet) {
        String query = "INSERT IGNORE INTO kpac_sets (title) VALUES (?)";
        try (Connection connection = connectionUtil.getConnection();
            PreparedStatement statement =
                connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, kpacSet.getTitle());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                kpacSet.setId(resultSet.getLong(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Can't add kpac set to DB", e);
        }
        saveAllKpacsInKpacsList(kpacSet);
        return kpacSet;
    }

    @Override
    public void delete(Long id) {
        String query = "DELETE FROM kpac_sets WHERE id = ?";
        try (Connection connection = connectionUtil.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Can't delete kpac set with id=" + id + " from DB", e);
        }
        String queryForRelations = "DELETE FROM kpac_set_kpac WHERE kpac_set_id = ?";
        try (Connection connection = connectionUtil.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(queryForRelations, Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Can't delete kpac set with id=" + id + " from DB", e);
        }
    }

    @Override
    public KpacSet get(Long id) {
        String query = "SELECT * FROM kpac_sets where id = ?";
        KpacSet kpacSet = null;
        try (Connection connection = connectionUtil.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                kpacSet = parseKpacSetFromResultSet(resultSet);
                kpacSet.setKpacList(getAllKpacesByKpacSetId(id));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Can't get kpac by id=" + id + " from DB", e);
        }
        return kpacSet;
    }

    @Override
    public List<KpacSet> getAll() {
        String query = "SELECT * FROM kpac_sets";
        List<KpacSet> kpacSetList = new ArrayList<>();
        try (Connection connection = connectionUtil.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                KpacSet kpacSet = new KpacSet();
                kpacSet = parseKpacSetFromResultSet(resultSet);
                kpacSetList.add(kpacSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Can't get all kpac sets from DB", e);
        }
        return kpacSetList;
    }

    private KpacSet parseKpacSetFromResultSet(ResultSet resultSet) throws SQLException {
        KpacSet kpacSet = new KpacSet();
        kpacSet.setId(resultSet.getLong(1));
        kpacSet.setTitle(resultSet.getString(2));
        return kpacSet;
    }

    private void saveAllKpacsInKpacsList(KpacSet kpacSet) {
        String query = "INSERT IGNORE INTO kpac_set_kpac "
                + "(kpac_set_id, kpac_id) "
                + "VALUES (?, ?)";
        try (Connection connection = connectionUtil.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, kpacSet.getId());
            for (Kpac kpac : kpacSet.getKpacList()) {
                statement.setLong(2, kpac.getId());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Can't add kpac set to DB", e);
        }
    }

    private List<Kpac> getAllKpacesByKpacSetId(Long id) {
        String query = "SELECT kps.id, kps.title, kps.description, kps.creation_date " +
                "FROM kpaces kps " +
                "JOIN kpac_set_kpac kks ON kps.id = kks.kpac_id " +
                "WHERE kpac_set_id = ?";
        List<Kpac> kpacList = new ArrayList<>();
        try (Connection connection = connectionUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
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
