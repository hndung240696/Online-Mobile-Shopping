/**
 * 
 */
package com.fa.group01.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.fa.group01.connect.DatabaseConnect;
import com.fa.group01.constants.DbQuery;
import com.fa.group01.dao.UserDAO;
import com.fa.group01.entity.Country;
import com.fa.group01.entity.State;
import com.fa.group01.entity.User;

/**
 * @author nguyenthanhlinh
 *
 */
public class UserDAOImpl implements UserDAO {
	
	Connection connection = null;
	Statement statement = null;
	ResultSet resultSet = null;
	CallableStatement callabaleStatement = null;

	/**
	 * Save the register user to the database;
	 * @param user
	 * @return
	 * @throws SQLException
	 */
	@Override
	public String registerUser(User user) throws SQLException {
		int affectedRow = 0;
		String insertUserQuery = DbQuery.INSERT_USER_QUERY;
		connection = DatabaseConnect.getConnection();
		if(connection != null) {
			try {
				callabaleStatement = connection.prepareCall(insertUserQuery);
				callabaleStatement.setString(1, user.getUserName());
				callabaleStatement.setString(2, user.getUserPassword());
				callabaleStatement.setString(3, user.getUserEmail());
				callabaleStatement.setString(4, user.getUserFirstName());
				callabaleStatement.setString(5, user.getUserLastName());
				callabaleStatement.setString(6, user.getUserRole());
				callabaleStatement.setTimestamp(7, user.getUserCreateDate());
				affectedRow = callabaleStatement.executeUpdate();
				if(affectedRow > 0) {
					return "Update Successful";
				}
			} finally {
				if(connection != null) {
					connection.close();
				}
				if(callabaleStatement != null) {
					callabaleStatement.close();
				}
			}
		}
		return "Update fail";
	}

	/**
	 * Find all the user in the database
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<User> findAll() throws SQLException {
		List<User> users = new ArrayList<>();
		User user = null;
		Country country = null;
		State state = null;
		String getUserQuery = DbQuery.GET_ALL_USER_QUERY;
		connection = DatabaseConnect.getConnection();
		if(connection != null) {
			try {
				statement = connection.createStatement();
				resultSet = statement.executeQuery(getUserQuery);
				while(resultSet.next()) {
					user = new User();
					state = new State();
					country = new Country();
					user.setUserId(resultSet.getInt("UserId"));
					user.setUserName(resultSet.getString("UserName"));
					user.setUserPassword(resultSet.getString("Password"));
					user.setUserEmail(resultSet.getString("Email"));
					user.setUserFirstName(resultSet.getString("FirstName"));
					user.setUserLastName(resultSet.getString("LastName"));
					user.setUserRole(resultSet.getString("Role"));
					user.setUserCreateDate(resultSet.getTimestamp("CreateDate"));
					country.setCountryId(resultSet.getInt("CountryId"));
					state.setStateId(resultSet.getInt("StateId"));
					user.setCountry(country);
					user.setState(state);
					users.add(user);
				}
			} finally {
				if(connection != null) {
					connection.close();
				}
				if(statement != null) {
					statement.close();
				}
				if(resultSet != null) {
					resultSet.close();
				}
			}
		}
		return users;
	}

}
