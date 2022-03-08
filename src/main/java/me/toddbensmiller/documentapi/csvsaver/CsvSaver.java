package me.toddbensmiller.documentapi.csvsaver;

import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.File;
import java.io.FileNotFoundException;

import org.postgresql.copy.CopyManager;
import org.postgresql.core.BaseConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CsvSaver<T extends CsvSavable> {
    private Logger log = LoggerFactory.getLogger(CsvSaver.class);
    private boolean hasData;
    private BufferedWriter writer;
    private Long index;
    private String filename;
    private String dataline;

    public CsvSaver(String filename, Long beginningIndex) {
        try {
            hasData = false;
            writer = new BufferedWriter(new FileWriter(filename));
            this.filename = filename;
            this.index = beginningIndex;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void add(T item) {
        try {
            hasData = true;
            item.setId(index++);
            this.dataline = item.getSqlString();
            writer.write(item.toCsvLine());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        if (!hasData) {
            log.info("Skipping save due to lack of data");
            return;
        }
        log.info("Entered save");
        try {
            writer.close();
        } catch (IOException e1) {
            log.error("Failed to close writer");
        }
        String username = "postgres";
        String password = "correcthorsebatterystaple"; // debug password
        String connectionString = "jdbc:postgresql://localhost:5432/documentapi";
        String query = "COPY " + dataline + " FROM stdin DELIMITER ',';";

        try {
            Connection c = DriverManager.getConnection(connectionString, username, password);
            BaseConnection pgcon = (BaseConnection) c;
            CopyManager cm = new CopyManager(pgcon);
            Reader r = new BufferedReader(new FileReader(new File(filename)));
            long rowsAffected = cm.copyIn(query, r);
            log.info("Inserted " + rowsAffected + " rows via COPY");
        } catch (SQLException e) {
            log.info("failed to upload csv");
            log.error(e.getMessage());
        } catch (FileNotFoundException e) {
            log.error("Unable to open local file " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("Exiting save");
    }
}
