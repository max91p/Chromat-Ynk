package com.example;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;

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
                return;
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
                Group root = new Group(scene.getRoot());
                root.getChildren().remove(cursor);
                scene.setRoot(root);
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