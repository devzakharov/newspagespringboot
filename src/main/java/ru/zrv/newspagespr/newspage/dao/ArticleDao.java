package ru.zrv.newspagespr.newspage.dao;

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

    // TODO настроить адекватные события для логера
    // final static Logger logger = Logger.getLogger(ArticleDao.class);

    public ArticleDao(DataSource dataSource, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.dataSource = dataSource;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
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
            Integer limit, Integer offset, String[] tagsArray, String from, String to, String searchQuery
    ) throws SQLException {

        List<Article> filteredArticleList = new LinkedList<>();

        String generatedQuery = generateQuery(tagsArray, from, to, searchQuery);
        SqlRowSet rs = generateStatement(generatedQuery, limit, offset, tagsArray, from, to, searchQuery);

        while (rs.next()) {
            filteredArticleList.add(createArticleFromResultSet(rs));
        }

        return filteredArticleList;
    }

    @Override
    public void save(Article article) throws SQLException {

    }

    // TODO разделить на методы составление запроса, составление листа запросов
    public void save(Set<Article> articleSet) throws SQLException {

        Iterator<Article> iterator = articleSet.iterator();

        String query = "INSERT INTO articles " +
                "(id, description, news_keywords, image, article_html, front_url, title, photo, project, category, opinion_authors, anons, publish_date, parsed_date) " +
                "VALUES (?,?,?,?,?,?,?,to_json(?::json),?,?,?,?,?,?) ON CONFLICT DO NOTHING";

        Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);

        while (iterator.hasNext()) {

            Article article = iterator.next();
            // Брат, [03.11.20 12:42]
            //Можешь свои кейворды засунуть строкой в запрос
            //
            //Брат, [03.11.20 12:42]
            //А в запросе парсануть в массив
            Array array = connection.createArrayOf("TEXT", article.getNewsKeywords().split(","));

            preparedStatement.setString(1, article.getId());
            preparedStatement.setString(2, Base64.getEncoder().encodeToString(article.getDescription().getBytes()));
            preparedStatement.setArray(3, array);
            preparedStatement.setString(4, article.getImage());
            preparedStatement.setString(5, Base64.getEncoder().encodeToString(article.getArticleHtml().getBytes()));
            preparedStatement.setString(6, article.getFrontUrl());
            preparedStatement.setString(7, Base64.getEncoder().encodeToString(article.getTitle().getBytes()));
            preparedStatement.setString(8, article.getPhoto().toString());
            preparedStatement.setString(9, article.getProject());
            preparedStatement.setString(10, article.getCategory());
            preparedStatement.setString(11, article.getOpinionAuthors());
            preparedStatement.setString(12, Base64.getEncoder().encodeToString(article.getAnons().getBytes()));
            preparedStatement.setTimestamp(13, article.getPublishDate());
            preparedStatement.setTimestamp(14, article.getParsedDate());

            preparedStatement.addBatch();
        }
        preparedStatement.executeBatch();
    }

    @Override
    public void update(Article article, String[] params) {

    }

    @Override
    public void delete(Article a) {

    }

    public List<Article> getSearchResult(String searchQuery) throws SQLException {

        List<Article> articleList = new LinkedList<>();

        String query = "SELECT *, t FROM ( SELECT *, convert_from(decode(article_html, 'base64'), 'UTF-8') as t " +
                " FROM articles) foo " +
                " WHERE t LIKE ALL ( :array )";

        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        MapSqlParameterSource paramSource = new MapSqlParameterSource();

        Array array = dataSource
                .getConnection()
                .createArrayOf("VARCHAR", convertSearchQueryStringToArray(searchQuery));

        paramSource.addValue("array", array);

        SqlRowSet rs = namedParameterJdbcTemplate.queryForRowSet(query, paramSource);


        while (rs.next()) {
            articleList.add(createArticleFromResultSet(rs));
        }

        return articleList;
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

    // TODO подумать, как обойтись без этого
    private String[] convertSearchQueryStringToArray(String searchQuery) {

        String[] searchQueryArray = searchQuery.split(" ");
        for (int i = 0; i < searchQueryArray.length; i++) {
            searchQueryArray[i] = "%" + searchQueryArray[i].toLowerCase().substring(1) + "%";
        }
        return searchQueryArray;
    }

    // TODO заменить undefined на null
    private String generateQuery(
            String[] tagsArray,
            String from,
            String to,
            String searchQuery) {

        StringBuilder query = new StringBuilder();

        if (!searchQuery.equals("undefined")) {
            query.append("SELECT *, t FROM (");
        }
        query.append("SELECT *, convert_from(decode(article_html, 'base64'), 'UTF-8') as t FROM articles ");
        if (!tagsArray[0].equals("")) {
            query.append("WHERE news_keywords @> :tags ");
            if(!from.equals("undefined")||!to.equals("undefined")) {
                query.append(" AND ");
            }
        } else if(!from.equals("undefined")&&!to.equals("undefined")) {
            query.append(" WHERE ");
        }
        if (!from.equals("undefined")&&!to.equals("undefined")) {
            query.append(" publish_date BETWEEN :from ");
            query.append(" AND :to ");
        }
        if (!searchQuery.equals("undefined")) {
            query.append(" ) foo  WHERE t LIKE ALL (:search) ");
        }
        query.append(" ORDER BY publish_date DESC ");
        query.append(" LIMIT :limit OFFSET :offset ;");

        return query.toString();
    }

    private SqlRowSet generateStatement(
            String generatedQuery,
            Integer limit,
            Integer offset,
            String[] tagsArray,
            String from,
            String to,
            String searchQuery) throws SQLException {


        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        MapSqlParameterSource paramSource = new MapSqlParameterSource();

        if (!searchQuery.equals("undefined")) {
            Array array = dataSource.getConnection().createArrayOf("VARCHAR", convertSearchQueryStringToArray(searchQuery));
            paramSource.addValue("search", array);
        }

        if (!tagsArray[0].equals("")) {
            Array array = dataSource.getConnection().createArrayOf("TEXT", tagsArray);
            paramSource.addValue("tags", array);
        }

        if (!from.equals("undefined")&&!to.equals("undefined")) {
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
}
