package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository extends BaseRepository<User> {
    private static final String FIND_ALL_QUERY = "SELECT * FROM users";
    private static final String FIND_BY_ID = "SELECT *  FROM users WHERE id = ? ";
    private static final String FIND_ALL_FRIENDS_USER = "SELECT * " +
            "FROM users " +
            "WHERE id IN ( " +
            " SELECT addressee_id " +
            " FROM friendships  " +
            " WHERE requester_id = ?) ";
    private static final String INSERT_USER_QUERY = "INSERT INTO users(email, username,login,birthday) " +
            "VALUES (?, ?, ?, ?)";
    private static final String UPDATE_USER_QUERY = "UPDATE users SET email = ?, username = ?, login = ?, birthday = ? " +
            "WHERE id = ?;";
    private static final String INSERT_FRIEND = "INSERT INTO friendships (requester_id, addressee_id) " +
            "VALUES (?, ?);";
    private static final String DELETE_FRIEND = "DELETE FROM friendships WHERE requester_id = ? AND addressee_id = ?;";
    private static final String FIND_MUTUAL_FRIENDS = "SELECT * FROM users u, friendships f, friendships o " +
            "WHERE (u.id = f.addressee_id AND u.id = o.addressee_id) " +
            "AND f.requester_id = ? AND o.requester_id = ?";

    public UserRepository(JdbcTemplate jdbc,
                          RowMapper<User> mapper) {
        super(jdbc, mapper);
    }

    public List<User> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    public Optional<User> findById(int id) {
        return findOne(FIND_BY_ID, id);
    }

    public List<User> findAllFriendsById(int id) {
        return findMany(FIND_ALL_FRIENDS_USER, id);
    }

    public List<User> findAllMutualFriends(int userId, int otherId) {
        return findMany(FIND_MUTUAL_FRIENDS, userId, otherId);

    }

    public User addUser(User user) {
        int id = insert(
                INSERT_USER_QUERY,
                user.getEmail(),
                user.getName(),
                user.getLogin(),
                user.getBirthday()
        );
        user.setId(id);
        return user;
    }

    public void addFriend(int requesterId, int addresseeId) {
        insertWithoutKey(INSERT_FRIEND,
                requesterId,
                addresseeId);
    }

    public void removeFriend(int requesterId, int addresseeId) {
        delete(DELETE_FRIEND, requesterId, addresseeId);
    }

    public User updateUser(User user) {
        update(UPDATE_USER_QUERY,
                user.getEmail(),
                user.getName(),
                user.getLogin(),
                user.getBirthday(),
                user.getId());
        return user;
    }
}
