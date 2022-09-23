package edu.bbte.idde.vgim1978.spring.dao.jdbc;

import edu.bbte.idde.vgim1978.spring.controller.exception.MySqlException;
import edu.bbte.idde.vgim1978.spring.dao.CateringMenuDao;
import edu.bbte.idde.vgim1978.spring.model.Ingredient;
import edu.bbte.idde.vgim1978.spring.model.Menu;
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
public class JdbcMenuDao implements CateringMenuDao {

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
            log.error("Error with getting Ingredients for Menu",e);
            throw new MySqlException();
        }
    }

    private Menu getMenuFromSet(ResultSet resultSet, Ingredient ingredient) {
        Menu menu = new Menu();
        try {
            menu.setId(resultSet.getLong("menuId"));
            menu.setType(resultSet.getString("type"));
            menu.setName(resultSet.getString("name"));
            menu.setDescription(resultSet.getString("description"));
            menu.setVegetarian(resultSet.getBoolean("vegetarian"));
            menu.setCalorieWeight(resultSet.getLong("calorieweight"));
            menu.setPrice(resultSet.getLong("price"));
            menu.setMainIngredient(ingredient);

            return menu;
        } catch (SQLException e) {
            log.error("Error with getting Menu:{} from set", menu, e);
            throw new MySqlException();

        }
    }

    @Override
    public Collection<Menu> findByName(String name) {
        Collection<Menu> menus = new ArrayList<>();
        try {
            Connection connection = dataSource.getConnection();

            PreparedStatement preparedStatement = connection
                    .prepareStatement("SELECT * FROM Menus WHERE name LIKE ?");
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                long ingredientId = resultSet.getLong("mainIngredientId");
                PreparedStatement preparedStatementIngr = connection.prepareStatement(
                        "SELECT * FROM Ingredients WHERE ingredientId = ?");
                preparedStatementIngr.setLong(1, ingredientId);

                ResultSet resultSetIngr = preparedStatementIngr.executeQuery();
                if (resultSetIngr.next()) {
                    Ingredient mainIngredient = getIngredientFromSet(resultSetIngr);
                    Menu menu = getMenuFromSet(resultSet,mainIngredient);
                    menus.add(menu);
                }

            }
            log.debug("Menus {} found by name: {},",menus,name);

            return menus;

        } catch (SQLException e) {
            log.error("Error with find Menus by name: {}",name,e);
            throw new MySqlException();

        }

    }

    @Override
    public Menu saveAndFlush(Menu entity) {
        try {
            Connection connection = dataSource.getConnection();

            PreparedStatement preparedStatement = connection
                    .prepareStatement("INSERT INTO Menus "
                            + "(name, type, description, vegetarian, calorieweight, price, mainIngredientId) "
                            + "VALUES (?, ?, ?, ?, ?, ?, ?)",
                            Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setString(2, entity.getType());
            preparedStatement.setString(3, entity.getDescription());
            preparedStatement.setBoolean(4, entity.getVegetarian());
            preparedStatement.setLong(5, entity.getCalorieWeight());
            preparedStatement.setLong(6, entity.getPrice());
            preparedStatement.setLong(7, entity.getMainIngredient().getId());
            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                entity.setId(resultSet.getLong(1));
                return entity;
            }
            log.debug("Menu {} added",entity);

        } catch (SQLException e) {
            log.error("Error when creating menu: {}",entity);
            throw new MySqlException();

        }
        return null;

    }

    @Override
    public Collection<Menu> findAll() {

        Collection<Menu> menus = new ArrayList<>();
        try {
            Connection connection = dataSource.getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Menus");
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                long ingredientId = resultSet.getLong("mainIngredientId");

                PreparedStatement preparedStatementIngr = connection.prepareStatement(
                        "SELECT * FROM Ingredients WHERE ingredientId = ?");
                preparedStatementIngr.setLong(1,ingredientId);

                ResultSet resultSetIngr = preparedStatementIngr.executeQuery();

                Ingredient ingredient;
                if (resultSetIngr.next()) {
                    ingredient = getIngredientFromSet(resultSetIngr);
                    Menu menu = getMenuFromSet(resultSet, ingredient);
                    menus.add(menu);
                }
            }

            log.debug("Finding all menus: {},",menus);

        } catch (SQLException e) {
            log.error("Error with findAll Menu", e);
            throw new MySqlException();

        }

        return menus;
    }

    @Override
    public Menu getById(Long id) {
        try {
            Connection connection = dataSource.getConnection();

            PreparedStatement preparedStatement = connection
                    .prepareStatement("SELECT * FROM Menus WHERE menuId = ?");
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                long ingredientId = resultSet.getLong("mainIngredientId");
                PreparedStatement preparedStatementIngr = connection.prepareStatement(
                        "SELECT * FROM Ingredients WHERE ingredientId = ?");
                preparedStatementIngr.setLong(1,ingredientId);

                ResultSet resultSetIngr = preparedStatementIngr.executeQuery();

                Ingredient ingredient;
                if (resultSetIngr.next()) {
                    ingredient = getIngredientFromSet(resultSetIngr);
                    return getMenuFromSet(resultSet, ingredient);
                }
            }

        } catch (SQLException e) {
            log.error("Error with find Menu by id: {}",id,e);
            throw new MySqlException();

        }
        return null;
    }

    @Override
    public void deleteById(Long id) {
        try {
            Connection connection = dataSource.getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(
                    "DELETE FROM Menus WHERE menuId = ?");
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
            log.debug("Deleting menu with id:" + id);
        } catch (SQLException e) {
            log.error("Failed to delete Menu with id: {}",id, e);
            throw new MySqlException();

        }
    }

    public Menu update(Long id, Menu menu) {
        try {
            Connection connection = dataSource.getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE Menus SET name = ?, type = ?, description = ?, vegetarian = ?, "
                            + "calorieweight = ?, price = ?, mainIngredientId = ? WHERE menuId = ?");
            preparedStatement.setString(1,menu.getName());
            preparedStatement.setString(2,menu.getType());
            preparedStatement.setString(3, menu.getDescription());
            preparedStatement.setBoolean(4, menu.getVegetarian());
            preparedStatement.setLong(5, menu.getCalorieWeight());
            preparedStatement.setLong(6, menu.getPrice());
            preparedStatement.setLong(7, menu.getMainIngredient().getId());
            preparedStatement.setLong(8, id);
            preparedStatement.executeUpdate();
            log.debug("Updating Menu {} with id: {} ",menu,id);
            return menu;
        } catch (SQLException e) {
            log.error("Failed to  update Menu with id: {}",id, e);
            throw new MySqlException();

        }
    }
}
