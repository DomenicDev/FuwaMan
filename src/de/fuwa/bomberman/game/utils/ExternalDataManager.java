package de.fuwa.bomberman.game.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExternalDataManager {

    private static final String DIRECTORY = "usermaps/";

    public static boolean writeCustomMap(String name, GameField data) {
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(DIRECTORY + name));
            out.writeObject(data);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static GameField readCustomMap(String name) {
        try {
            ObjectInputStream input = new ObjectInputStream(new FileInputStream(DIRECTORY + name));
            Object o = input.readObject();
            if (o instanceof GameField) {
                return (GameField) o;
            }
        } catch (IOException | ClassNotFoundException e) {
            Logger.getLogger(ExternalDataManager.class.getName()).log(Level.WARNING, "Failed to load '" + name + "'. Probably not a object file!");
        }
        return null;
    }

    public static List<NamedGameField> readAllCustomMaps() {
        File usermapFolder = new File(DIRECTORY);
        File[] maps = usermapFolder.listFiles();
        if (maps != null) {
            List<NamedGameField> list = new ArrayList<>(maps.length);
            for (File f : maps) {
                GameField field = readCustomMap(f.getName());
                String name = f.getName();
                if (field != null) {
                    list.add(new NamedGameField(name, field));
                }
            }
            return list;
        }
        return null;
    }

}
