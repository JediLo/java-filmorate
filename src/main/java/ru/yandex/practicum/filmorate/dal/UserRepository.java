package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.mapper.FriendRowMapper;
import ru.yandex.practicum.filmorate.dal.mapper.ValIntegerRowMapper;
import ru.yandex.practicum.filmorate.model.Friend;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class UserRepository extends BaseRepository<User> {
    private static final String FIND_ALL_QUERY = "SELECT * FROM users";
    private static final String FIND_FRIEND = "SELECT requester_id, addressee_id " +
            "FROM friendships " +
            "WHERE requester_id = ? OR addressee_id = ?";
    private static final String FIND_ALL_FILMS_LIKES = "SELECT film_id AS val  FROM likes_films WHERE user_id = ? ";
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

    private final FriendRowMapper friendRowMapper;
    private final ValIntegerRowMapper valIntegerRowMapper;

    public UserRepository(JdbcTemplate jdbc,
                          RowMapper<User> mapper,
                          FriendRowMapper friendRowMapper,
                          ValIntegerRowMapper valIntegerRowMapper) {
        super(jdbc, mapper);
        this.valIntegerRowMapper = valIntegerRowMapper;
        this.friendRowMapper = friendRowMapper;
    }


    public List<User> findAll() {
        List<User> users = findMany(FIND_ALL_QUERY);
        addFriendAndLikesToUsers(users);
        return users;
    }

    public Optional<User> findById(int id) {
        Optional<User> user = findOne(FIND_BY_ID, id);
        user.ifPresent(e -> e.setFriendSet(getSetFriendsById(id)));
        return user;
    }

    public List<User> findAllFriendsById(int id) {

        List<User> users = findMany(FIND_ALL_FRIENDS_USER, id);
        addFriendAndLikesToUsers(users);
        return users;

    }

    // Переписать и перенести логику сбора общих друзей в сервис
    public List<User> findAllMutualFriends(int userId, int otherId) {
        Set<Integer> userFriend = getSetFriendsById(userId);
        Set<Integer> otherFriend = getSetFriendsById(otherId);
        userFriend.retainAll(otherFriend);
        return userFriend.stream()
                .map(this::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

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


    private void addLikesFilms(User user) {
        List<Integer> allLikesFilms = queryMany(FIND_ALL_FILMS_LIKES, valIntegerRowMapper, user.getId());
        Set<Integer> likesFilmsSet = new HashSet<>(allLikesFilms);
        user.setLikesFilms(likesFilmsSet);
    }

    private void addFriend(User user) {
        user.setFriendSet(getSetFriendsById(user.getId()));
    }

    private void addFriendAndLikesToUsers(List<User> users) {
        users.forEach(user -> {
            addFriend(user);
            addLikesFilms(user);
        });
    }

    private Set<Integer> getSetFriendsById(int userId) {
        List<Friend> allFriends = queryMany(FIND_FRIEND, friendRowMapper, userId, userId);
        return allFriends.stream()
                .map(e -> e.getAddresseeId() == userId ? e.getRequesterId() : e.getAddresseeId())
                .collect(Collectors.toSet());
    }
}
