package com.example;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.shape.Polygon;

public class CursorManager {
    private List<Cursor> cursors;
    private Cursor currentCursor;
    private Scene scene;

    public CursorManager(Scene scene) {
        this.scene=scene;
        cursors = new ArrayList<>();
        currentCursor = null;
    }

    public Scene getScene() {
        return scene;
    }

    public Cursor getCurrentCursor() {
        return currentCursor;
    }

    public void createCursor(int id) {
        if (!isCursorIdExists(id)) {
            Cursor newCursor = new Cursor(new Point(50,50),id,scene);
            cursors.add(newCursor);
            DrawingCanvas draw = new DrawingCanvas(scene);
            draw.drawCursor(newCursor);
            Group root = (Group)scene.getRoot();
        } else {
            throw new IllegalArgumentException("L'identifiant existe déjà!");
        }
    }

    public void addCursor(Cursor cursor) {
        if (!isCursorIdExists(cursor.getId()) && cursor!=null) {
            cursors.add(cursor);

        } else {
            throw new IllegalArgumentException("L'identifiant existe déjà!");
        }
    }

    public void selectCursor(int id) {
        for (Cursor cursor : cursors) {
            if (cursor.getId() == id) {
                currentCursor = cursor;
                Group root = (Group)scene.getRoot();
                System.out.println(root.getChildren());
            }
        }
        // Gérer l'erreur : aucun curseur avec cet identifiant n'existe
    }

    //get an access to a cursor with its id
    public Cursor getCursor(int id){
        for(Cursor cursor : cursors){
            if(cursor.getId()==id){
                return cursor;
            }
        }
        //error
        return null;
    }

    public void removeCursor(int id) {
        for (Cursor cursor : cursors) {
            if (cursor.getId() == id) {
                cursors.remove(cursor);
                cursor.removeCursorWithId();
                cursor.deconstructor();
                break;
            }
        }
        // Gérer l'erreur : aucun curseur avec cet identifiant n'existe
    }

    // Méthode pour vérifier si l'identifiant du curseur existe déjà
    public boolean isCursorIdExists(int id) {
        for (Cursor cursor : cursors) {
            if (cursor.getId() == id) {
                return true;
            }
        }
        return false;
    }
}