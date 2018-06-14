package com.softserve.mosquito.repo.impl;

import com.softserve.mosquito.entities.User;
import org.dbunit.database.DatabaseDataSourceConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfiguration.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserRepoImplTest {

    @Autowired
    UserRepoImpl userRepo;

    @Autowired
    DataSource dataSource;

    /*@Before
    public void setUp() throws Exception {
        IDatabaseConnection dbConn = new DatabaseDataSourceConnection(
                dataSource);
        DatabaseOperation.CLEAN_INSERT.execute(dbConn, getDataSet());
    }

    private IDataSet getDataSet() throws Exception{
        IDataSet dataSet = new FlatXmlDataSet(this.getClass().getClassLoader().getResourceAsStream("User.xml"));
        return dataSet;
    }*/

    @Test
    public void create() throws Exception {
        User user = new User();
        user.setEmail("dfhdfh");
        user.setPassword("dfhdfh");
        user.setFirstName("dfhdfh");
        user.setLastName("dfhdfh");
        user.setConfirmed(true);

        User created = userRepo.create(user);
        assertNotNull(created.getId());
    }

    @Test
    public void read() throws Exception {
        User user = userRepo.read(1L);
        //assertNotNull(user);
    }

    @Test
    public void update() throws Exception {
    }

    @Test
    public void delete() throws Exception {
    }

    @Test
    public void getAll() throws Exception {
    }

}