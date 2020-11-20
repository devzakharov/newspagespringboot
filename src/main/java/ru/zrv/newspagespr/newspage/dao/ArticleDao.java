package ru.zrv.newspagespr.newspage.dao;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.zrv.newspagespr.newspage.domian.Article;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import ru.zrv.newspagespr.newspage.domian.Photo;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

@Repository
public class ArticleDao implements Dao<Article> {

    private final DataSource dataSource;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final JdbcTemplate jdbcTemplate;

    // TODO настроить адекватные события для логера
    // final static Logger logger = Logger.getLogger(ArticleDao.class);

    public ArticleDao(DataSource dataSource, NamedParameterJdbcTemplate namedParameterJdbcTemplate, JdbcTemplate jdbcTemplate) {
        this.dataSource = dataSource;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Article> get(String id) {

        String query = "SELECT * FROM articles WHERE id LIKE :id LIMIT 1";

        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("id", id);

        SqlRowSet rs = namedParameterJdbcTemplate.queryForRowSet(query, paramSource);

        if (!rs.next()) {
            return Optional.empty();
        } else {
            return Optional.of(createArticleFromResultSet(rs));
        }
    }

    @Override
    public List<Article> getAll() throws SQLException {
        return null;
    }

    public List<Article> getFilteredResult(
            Integer limit, Integer offset, String tags, String from, String to, String searchQuery
    ) throws SQLException {

        List<Article> filteredArticleList = new LinkedList<>();
        String sql = generateQuery(tags, from, to, searchQuery);
        SqlRowSet rs = setValuesAndExecute(sql, limit, offset, tags, from, to, searchQuery);

        while (rs.next()) {
            filteredArticleList.add(createArticleFromResultSet(rs));
        }

        return filteredArticleList;
    }

    @Override
    public void save(Article article) throws SQLException {

    }

    public void save(Set<Article> articleSet) {

        List<Article> articleList = new ArrayList<>(articleSet);

        String sql = "INSERT INTO articles " +
                "(id, description, news_keywords, image, article_html, front_url, title, photo, project, category, opinion_authors, anons, publish_date, parsed_date) " +
                "VALUES (?,?,string_to_array(?,', '),?,?,?,?,to_json(?::json),?,?,?,?,?,?) ON CONFLICT DO NOTHING";

        this.jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {

                preparedStatement.setString(1, articleList.get(i).getId());
                preparedStatement.setString(2, Base64.getEncoder().encodeToString(articleList.get(i).getDescription().getBytes()));
                preparedStatement.setString(3, articleList.get(i).getNewsKeywords());
                preparedStatement.setString(4, articleList.get(i).getImage());
                preparedStatement.setString(5, Base64.getEncoder().encodeToString(articleList.get(i).getArticleHtml().getBytes()));
                preparedStatement.setString(6, articleList.get(i).getFrontUrl());
                preparedStatement.setString(7, Base64.getEncoder().encodeToString(articleList.get(i).getTitle().getBytes()));
                preparedStatement.setString(8, articleList.get(i).getPhoto().toString());
                preparedStatement.setString(9, articleList.get(i).getProject());
                preparedStatement.setString(10, articleList.get(i).getCategory());
                preparedStatement.setString(11, articleList.get(i).getOpinionAuthors());
                preparedStatement.setString(12, Base64.getEncoder().encodeToString(articleList.get(i).getAnons().getBytes()));
                preparedStatement.setTimestamp(13, articleList.get(i).getPublishDate());
                preparedStatement.setTimestamp(14, articleList.get(i).getParsedDate());

            }

            @Override
            public int getBatchSize() {
                return articleList.size();
            }

        });
    }

    @Override
    public void update(Article article, String[] params) {

    }

    @Override
    public void delete(Article a) {

    }

    private Article createArticleFromResultSet(SqlRowSet rs) {

        return getFilledArticle(
                rs.getString("id"),
                rs.getString("description"),
                rs.getString("news_keywords"),
                rs.getString("image"),
                rs.getString("article_html"),
                rs.getString("front_url"),
                rs.getString("title"),
                rs.getString("project"),
                rs.getString("category"),
                rs.getString("opinion_authors"),
                rs.getString("anons"),
                rs.getTimestamp("publish_date"),
                rs.getTimestamp("parsed_date"));
    }

    private String generateQuery(
            String tags,
            String from,
            String to,
            String searchQuery) {

        StringBuilder query = new StringBuilder();

        if (!searchQuery.equals("")) {
            query.append("SELECT *, t FROM (");
        }
        query.append("SELECT *, convert_from(decode(article_html, 'base64'), 'UTF-8') as t FROM articles ");
        if (!tags.equals("")) {
            query.append("WHERE news_keywords @> string_to_array(:tags, ',') ");
            if(!from.equals("")||!to.equals("")) {
                query.append(" AND ");
            }
        } else if(!from.equals("")&&!to.equals("")) {
            query.append(" WHERE ");
        }
        if (!from.equals("")&&!to.equals("")) {
            query.append(" publish_date BETWEEN :from ");
            query.append(" AND :to ");
        }
        if (!searchQuery.equals("")) {
            query.append(" ) foo  WHERE t LIKE ALL (:search) ");
        }
        query.append(" ORDER BY publish_date DESC ");
        query.append(" LIMIT :limit OFFSET :offset ;");

        return query.toString();
    }

    private SqlRowSet setValuesAndExecute(
            String generatedQuery,
            Integer limit,
            Integer offset,
            String tags,
            String from,
            String to,
            String searchQuery) throws SQLException {

        MapSqlParameterSource paramSource = new MapSqlParameterSource();

        if (!searchQuery.equals("")) {
            //Array array = dataSource.getConnection().createArrayOf("VARCHAR", convertSearchQueryStringToArray(searchQuery));
            paramSource.addValue("search", convertSearchQueryStringToArray(searchQuery));
        }

        if (!tags.equals("")) {
            paramSource.addValue("tags", tags);
        }

        if (!from.equals("")&&!to.equals("")) {
            paramSource.addValue("from", Timestamp.valueOf(from + " 00:00:00"));
            paramSource.addValue("to", Timestamp.valueOf(to + " 00:00:00"));
        }

        paramSource.addValue("limit", limit);
        paramSource.addValue("offset", offset);

        return namedParameterJdbcTemplate.queryForRowSet(generatedQuery, paramSource);
    }

    private Article getFilledArticle(
            String id,
            String description,
            String news_keywords,
            String image,
            String article_html,
            String front_url,
            String title,
            String project,
            String category,
            String opinion_authors,
            String anons,
            Timestamp publish_date,
            Timestamp parsed_date) {

        Article article = new Article();
        Photo currentRawPhoto = new Photo();

        article.setId(id);
        article.setDescription(description);
        article.setNewsKeywords(news_keywords);
        article.setImage(image);
        article.setArticleHtml(article_html);
        article.setFrontUrl(front_url);
        article.setTitle(title);
        article.setPhoto(currentRawPhoto);
        article.setProject(project);
        article.setCategory(category);
        article.setOpinionAuthors(opinion_authors);
        article.setAnons(anons);
        article.setPublishDate(publish_date);
        article.setParsedDate(parsed_date);

        return article;
    }

    // TODO подумать, как обойтись без этого
    private String[] convertSearchQueryStringToArray(String searchQuery) {

        String[] searchQueryArray = searchQuery.split(" ");
        for (int i = 0; i < searchQueryArray.length; i++) {
            searchQueryArray[i] = "%" + searchQueryArray[i].toLowerCase().substring(1) + "%";
        }
        return searchQueryArray;
    }
}
