// (c)2016 Flipboard Inc, All Rights Reserved.

package com.will.custom_rxandroid.presenter.cache;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.will.custom_rxandroid.BaseApp;
import com.will.custom_rxandroid.pojo.map.GankBean;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.List;

public class CacheDB {
    private static String DATA_FILE_NAME = "data.db";

    private static CacheDB INSTANCE;

    File dataFile = new File(BaseApp.getInstance().getFilesDir(), DATA_FILE_NAME);
    Gson gson = new Gson();

    private CacheDB() {
    }

    public static CacheDB getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CacheDB();
        }
        return INSTANCE;
    }

    public List<GankBean> readItems() {
        // Hard code adding some delay, to distinguish reading from memory and reading disk clearly
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            Reader reader = new FileReader(dataFile);
            return gson.fromJson(reader, new TypeToken<List<GankBean>>() {
            }.getType());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void writeItems(List<GankBean> items) {
        String json = gson.toJson(items);
        try {
            if (!dataFile.exists()) {
                try {
                    dataFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Writer writer = new FileWriter(dataFile);
            writer.write(json);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void delete() {
        dataFile.delete();
    }
}
