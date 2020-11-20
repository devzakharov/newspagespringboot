package ru.zrv.newspagespr.newspage.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.zrv.newspagespr.newspage.domian.Tags;

import java.sql.SQLException;
import java.util.*;

@Repository
public class TagsDao implements Dao<Tags> {

    private final JdbcTemplate jdbcTemplate;

    public TagsDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Tags> get(String id) {
        // not a usable method
        return Optional.empty();
    }

    @Override
    public List<Tags> getAll() throws SQLException {
        // not a usable method
        return null;
    }

    @Override
    public void save(Tags tags) throws SQLException {
        // not a usable method
    }

    @Override
    public void update(Tags tags, String[] params) {
        // not a usable method
    }

    @Override
    public void delete(Tags tags) {
        // not a usable method
    }

    public Map<String, Integer> getTagsMap() {

        String sql = "with elements (element) as (" +
                "select unnest(news_keywords) " +
                "from articles) " +
                "select *, count(*) " +
                "from elements " +
                "group by element " +
                "order by count(*) desc " +
                "limit 40";

        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql);
        Map<String, Integer> tagsMap = new LinkedHashMap<>();

        while (sqlRowSet.next()) {
            tagsMap.put(sqlRowSet.getString("element"), sqlRowSet.getInt("count"));
        }

        return tagsMap;
    }

    public Set<String> getSuggestions(String string) {

        Set<String> suggestions = new HashSet<>();
        MapSqlParameterSource paramSource = new MapSqlParameterSource();

        String sql = "SELECT unnest FROM (SELECT unnest(news_keywords) FROM articles" +
                " GROUP BY id) foo WHERE lower(unnest) LIKE lower(:word) LIMIT 10";

        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql);
        paramSource.addValue( "word",string + "%");

        while(sqlRowSet.next()) {
            suggestions.add(sqlRowSet.getString(1));
        }

        return suggestions;
    }
}
