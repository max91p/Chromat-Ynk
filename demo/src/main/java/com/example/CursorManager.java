package com.example;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.Group;
import javafx.scene.Scene;


/**
 * Manages the cursors in the scene.
 */
public class CursorManager {
    private List<Cursor> cursors;
    private Cursor currentCursor;
    private Scene scene;

    /**
     * Constructs a new cursor manager for the specified scene.
     *
     * @param scene the scene where the cursors are printed
     */
    public CursorManager(Scene scene) {
        this.scene=scene;
        cursors = new ArrayList<>();
        currentCursor = null;
    }

    /**
     * Returns the scene where the cursors are printed.
     *
     * @return the scene where the cursors are printed
     */
    public Scene getScene() {
        return scene;
    }

    /**
     * Return the selected cursor.
     *
     * @return the current cursor
     */
    public Cursor getCurrentCursor() {
        return currentCursor;
    }

    /**
     * Creates a new cursor with the specified id.
     * @param id the id of the new cursor
     */
    public void createCursor(int id) throws ErrorLogger {
        if (!isCursorIdExists(id)) {
            Cursor newCursor = new Cursor(new Point(50,50),id,scene);
            cursors.add(newCursor);
            DrawingCanvas draw = new DrawingCanvas(scene);
            draw.drawCursor(newCursor);
            Group root = (Group)scene.getRoot();
        } else {
            throw new ErrorLogger(("This ID already exist"));
        }
    }

    /**
     * Adds the specified cursor to the list of cursors.
     *
     * @param cursor the cursor to add
     */
    public void addCursor(Cursor cursor) throws ErrorLogger{
        if (!isCursorIdExists(cursor.getId()) && cursor!=null) {
            cursors.add(cursor);

        } else {
            throw new ErrorLogger(("This ID already exist or the cursor is null"));
        }
    }

    /**
     * Selects the cursor with the specified id.
     *
     * @param id the id of the cursor to select
     */
    public void selectCursor(int id) throws ErrorLogger{
        for (Cursor cursor : cursors) {
            if (cursor.getId() == id) {
                currentCursor = cursor;
                Group root = (Group)scene.getRoot();
                System.out.println(root.getChildren());
            }



        }throw new ErrorLogger(("There is no cursor existing with this ID"));
        // Gérer l'erreur : aucun curseur avec cet identifiant n'existe
    }

    /**
     * Returns the cursor with the specified id.
     *
     * @param id the id of the cursor to return
     * @return the cursor with the specified id
     */
    public Cursor getCursor(int id) throws ErrorLogger{
        for(Cursor cursor : cursors){
            if(cursor.getId()==id){
                return cursor;
            }




        }throw new ErrorLogger("There is no cursor existing with this ID");

        //error
        //return null;
    }

    /**
     * Removes the cursor with the specified id.
     *
     * @param id the id of the cursor to remove
     */
    public void removeCursor(int id) throws ErrorLogger{
        for (Cursor cursor : cursors) {
            if (cursor.getId() == id) {
                cursors.remove(cursor);
                cursor.removeCursorWithId();
                cursor.deconstructor();
                break;
            }


        }throw new ErrorLogger(("There is no cursor existing with this ID"));
        // Gérer l'erreur : aucun curseur avec cet identifiant n'existe
    }

    /**
     * verifies if the cursor with the specified id exists.
     * @param id the id of the cursor to verify
     * @return true if the cursor exists, false otherwise
     */
    public boolean isCursorIdExists(int id) {
        for (Cursor cursor : cursors) {
            if (cursor.getId() == id) {
                return true;
            }
        }
        return false;
    }

    public List<Cursor> getListCursor(){
        return cursors;
    }
}