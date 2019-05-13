package com.game.saving;

import com.game.model.Shape;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.PrintWriter;
import java.util.List;

public class Save {

    public static void SaveAll(List<Shape> shapeList) {
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .setPrettyPrinting()
                .create();

        String json = gson.toJson(shapeList);
        try {
            File JSON = new File("shapeList.json");
            JSON.delete();

            PrintWriter writer = new PrintWriter(JSON, "UTF-8");
            writer.println(json);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
