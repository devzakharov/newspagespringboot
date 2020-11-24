package ru.zrv.newspagespr.newspage.dao;

import org.springframework.stereotype.Repository;
import ru.zrv.newspagespr.newspage.domian.User;
import ru.zrv.newspagespr.newspage.service.SHA256CryptoService;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class UserDao implements Dao<User> {

    DataSource dataSource;
    SHA256CryptoService sh256CryptoService;

    // TODO настроить адекватные события для логера

    List<User> users = new ArrayList<>();

    public UserDao(DataSource dataSource, SHA256CryptoService sh256CryptoService) {
        this.dataSource = dataSource;
        this.sh256CryptoService = sh256CryptoService;
    }

    @Override
    public Optional<User> get(String id) {

        return Optional.ofNullable(users.get(Integer.parseInt(id)));
    }

    @Override
    public List<User> getAll() throws SQLException {

        ResultSet rs = dataSource.getConnection().prepareStatement("SELECT * FROM users").getResultSet();
        // dbcs.closeConnection();

        return getUsersList(rs);
    }

    public Integer getUsersCount(User user) throws SQLException {

        int count = 0;

        String query = "SELECT count(*) FROM users WHERE login = ? OR email = ?";
        PreparedStatement preparedStatement = dataSource.getConnection().prepareStatement(query);
        preparedStatement.setString(1, user.getLogin());
        preparedStatement.setString(2, user.getPassword());

        try {
            ResultSet rs = dataSource.getConnection().prepareStatement(query).executeQuery();
            if (rs != null && rs.next()) {
                count = rs.getInt(1);
            }
            // dbcs.closeConnection();
            return count;
        } catch (SQLIntegrityConstraintViolationException e) {
            e.printStackTrace();
        }

        return count;
    }

    @Override
    public void save(User user) throws SQLException {

        String query = "INSERT INTO users (login, email, password) VALUES (?, ?, ?)";

        PreparedStatement preparedStatement = dataSource.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, user.getLogin());
        preparedStatement.setString(2, user.getEmail());
        preparedStatement.setString(3, sh256CryptoService.getHashString(user.getPassword()));

        ResultSet rs = dataSource.getConnection().createStatement().getGeneratedKeys();

        int id = -1;

        if (rs != null && rs.next()) {
            id = rs.getInt(1);
        }

        user.setId(id);
    }

    @Override
    public void update(User user, String[] params) {

        user.setLogin(Objects.requireNonNull(params[0], "Login cannot be null"));
        user.setEmail(Objects.requireNonNull(params[1], "Email cannot be null"));
        user.setPassword(Objects.requireNonNull(params[2], "Password cannot be null"));

        users.add(user);
    }

    @Override
    public void delete(User user) {

        users.remove(user);
    }

    private List<User> getUsersList(ResultSet rs) throws SQLException {

        while (rs.next()) {
            int id = rs.getInt("id");
            String login = rs.getString("login");
            String email = rs.getString("email");
            String password = rs.getString("password");
            int role_id = rs.getInt("role_id");

            User user = new User(login, email, password);

            users.add(user);
        }

        return users;
    }
}
