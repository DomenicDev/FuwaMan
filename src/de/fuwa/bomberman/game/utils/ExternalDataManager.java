package de.fuwa.bomberman.game.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

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
            e.printStackTrace();
        }
        return null;
    }

    public static List<GameField> readAllCustomMaps() {
        File usermapFolder = new File(DIRECTORY);
        File[] maps = usermapFolder.listFiles();
        if (maps != null) {
            List<GameField> list = new ArrayList<>(maps.length);
            for (File f : maps) {
                GameField s = readCustomMap(f.getName());
                if (s != null) {
                    list.add(s);
                }
            }
            return list;
        }
        return null;
    }

}
