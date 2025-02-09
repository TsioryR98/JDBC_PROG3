package org.prog3.repository;

import org.prog3.database.DataSource;
import org.prog3.model.Group;
import org.prog3.model.Order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class GroupRepository implements GenericDAO<Group>{
    private final DataSource dataSource;

    public GroupRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<Group> showAll(int page, int size, Order order) {
        List<Group> groupList = new ArrayList<>();
        String query = "SELECT * FROM groups";

        try(PreparedStatement stm = dataSource.getConnection().prepareStatement(query)) {
            ResultSet res = stm.executeQuery();
            while(res.next()){
                Group groups = new Group();
                groups.setGroupId(res.getInt("groupId"));
                groups.setGroupName(res.getString("groupName"));
                groups.setGroupYear(res.getObject("groupYear", LocalDateTime.class));
                groups.setStudentNb(res.getInt("studentNb"));

                groupList.add(groups);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return groupList;
    }


    @Override
    public List<Group> saveOrUpdate(List<Group> groups) {
        return List.of();
    }

    @Override
    public void delete(int id) {

    }

    @Override
    public Group findById(int groupId) {
        Group groupFind = null;
        String query = "SELECT * FROM groups WHERE groupId=?";

        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            preparedStatement.setInt(1, groupId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                groupFind = new Group();
                groupFind.setGroupId(resultSet.getInt("groupId"));
                groupFind.setGroupName(resultSet.getString("groupName"));
                groupFind.setGroupYear(resultSet.getObject("groupYear", LocalDateTime.class));
                groupFind.setStudentNb(resultSet.getInt("studentNb"));
            }
            return groupFind;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}



