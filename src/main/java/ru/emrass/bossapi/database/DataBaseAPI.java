package ru.emrass.bossapi.database;

import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import ru.emrass.bossapi.utils.DateFormatter;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
@NoArgsConstructor
public class DataBaseAPI {

    private static String url;
    @SneakyThrows
    private DataBaseAPI(File file){
        url = "jdbc:sqlite:" + file.getAbsolutePath();
        Class.forName("org.sqlite.JDBC").getConstructor().newInstance();
        createBossTable();
        createBossData();
        createSpawnLocationTable();
    }

    public static DataBaseAPI createDatabase(File file, String databaseName){
        return new DataBaseAPI(new File(file,databaseName + ".db"));
    }

    public Connection getConnection() throws Exception{
        return DriverManager.getConnection(url);
    }
    @SneakyThrows
    private void createBossTable(){
        Statement s = getConnection().createStatement();
        s.executeUpdate(
                String.format("CREATE TABLE IF NOT EXISTS %s (" +
                                "`id` INTEGER PRIMARY KEY," +
                                "`customname` TEXT NOT NULL," +
                                "`damage` INTEGER NOT NULL," +
                                "`health` INTEGER NOT NULL," +
                                "`movespeed` DOUBLE NOT NULL," +
                                "`armor` INTEGER NOT NULL," +
                                "`prototype` TEXT NOT NULL," +
                                "`respawnseconds` INTEGER NOT NULL," +
                                "`handid` TEXT," +
                                "`hand2id` TEXT," +
                                "`armor1id` TEXT," +
                                "`armor2id` TEXT," +
                                "`armor3id` TEXT," +
                                "`armor4id` TEXT," +
                                "`script` TEXT NOT NULL);"
                        ,"bosses"));
        s.close();
        getConnection().close();

    }
    @SneakyThrows
    private void createSpawnLocationTable(){
        Statement s = getConnection().createStatement();
        s.executeUpdate(
                String.format("CREATE TABLE IF NOT EXISTS %s (" +
                                "`id` INTEGER NOT NULL," +
                                "`spawnlocation` TEXT NOT NULL);"
                        ,"bossesspawn"));
        s.close();
        getConnection().close();

    }
    @SneakyThrows
    public void query(String query){
        Statement s = getConnection().createStatement();
        s.executeUpdate(query);
        s.close();
        getConnection().close();
    }

    @SneakyThrows
    private void createBossData(){
        Statement s = getConnection().createStatement();
        s.executeUpdate(
                String.format("CREATE TABLE IF NOT EXISTS %s (" +
                                "`id` TEXT NOT NULL," +
                                "`time` INTEGER NOT NULL," +
                                "`players` TEXT NOT NULL);"
                        ,"bossdata"));
        s.close();
        getConnection().close();

    }

    @SneakyThrows
    public void addBossKill(int bossID, long time, String killers){
        Connection con = getConnection();
        Statement state = con.createStatement();
        state.executeUpdate(String.format("INSERT INTO bossdata VALUES('%s', '%s', '%s')",bossID, DateFormatter.formatDateTime(time),killers));
        state.close();
        con.close();
    }
}
