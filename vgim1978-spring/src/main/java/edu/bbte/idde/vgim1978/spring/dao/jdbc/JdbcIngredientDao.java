package edu.bbte.idde.vgim1978.spring.dao.jdbc;

import edu.bbte.idde.vgim1978.spring.controller.exception.MySqlException;
import edu.bbte.idde.vgim1978.spring.dao.IngredientDao;
import edu.bbte.idde.vgim1978.spring.model.Ingredient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

@Repository
@Slf4j
@Profile("jdbc")
public class JdbcIngredientDao implements IngredientDao {

    @Autowired
    private DataSource dataSource;

    private Ingredient getIngredientFromSet(ResultSet resultSet) {
        Ingredient ingredient = new Ingredient();
        try {
            ingredient.setId(resultSet.getLong("ingredientId"));
            ingredient.setName(resultSet.getString("name"));
            ingredient.setCalorieWeight(resultSet.getLong("calorieweight"));
            return ingredient;
        } catch (SQLException e) {
            log.error("Error with getting Ingredient",e);
            throw new MySqlException();

        }
    }

    @Override
    public Ingredient saveAndFlush(Ingredient entity) {
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection
                    .prepareStatement("INSERT INTO Ingredients (name, calorieweight) VALUES (?, ?)",
                            Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setLong(2, entity.getCalorieWeight());

            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                entity.setId(resultSet.getLong(1));
                return entity;
            }

            log.debug("Ingredient {} added",entity);

        } catch (SQLException e) {
            log.error("Error when creating ingredient: {}",entity);
            throw new MySqlException();
        }
        return null;
    }

    @Override
    public Collection<Ingredient> findAll() {
        Collection<Ingredient> ingredients = new ArrayList<>();

        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Ingredients");
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Ingredient ingredient = getIngredientFromSet(resultSet);
                ingredients.add(ingredient);
            }

            log.debug("Finding all ingredients: {},",ingredients);
        } catch (SQLException e) {
            log.error("Error with findAll Ingredient", e);
            throw new MySqlException();
        }
        return ingredients;
    }

    @Override
    public Ingredient getById(Long id) {
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection
                    .prepareStatement("SELECT * FROM Ingredients WHERE ingredientId = ?");
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Ingredient ingredient = getIngredientFromSet(resultSet);
                log.debug("Ingredient {} found by id: {},",ingredient,id);
                return ingredient;
            }

        } catch (SQLException e) {
            log.error("Error with find Ingredient by id: {}",id,e);
            throw new MySqlException();
        }
        return null;
    }

    @Override
    public void deleteById(Long id) {
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "DELETE FROM Ingredients WHERE ingredientId = ?");
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
            log.debug("Deleting ingredient with id: {}",id);
        } catch (SQLException e) {
            log.error("Failed to delete ingredient with id: {}",id, e);
            throw new MySqlException();
        }
    }

    public Ingredient update(Long id,Ingredient ingredient) {
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE Ingredients SET name = ?, calorieweight = ? WHERE ingredientId = ?");
            preparedStatement.setString(1,ingredient.getName());
            preparedStatement.setLong(2,ingredient.getCalorieWeight());
            preparedStatement.setLong(3,id);
            preparedStatement.executeUpdate();
            log.debug("Updating Ingredient {} with id: {} ",ingredient,id);
            return ingredient;
        } catch (SQLException e) {
            log.error("Failed to update Ingredient with id: {}",id, e);
            throw new MySqlException();

        }
    }

    @Override
    public Collection<Ingredient> findByName(String name) {
        Collection<Ingredient> ingredients = new ArrayList<>();
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection
                    .prepareStatement("SELECT * FROM Ingredients WHERE name LIKE ?");
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Ingredient ingredient = getIngredientFromSet(resultSet);
                ingredients.add(ingredient);
            }
            log.debug("Ingredients {} found by name: {},",ingredients,name);
            return ingredients;

        } catch (SQLException e) {
            log.error("Error with find Ingredients by name: {}",name,e);
            throw new MySqlException();

        }
    }
}
