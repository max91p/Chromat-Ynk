package com.example;

import java.util.ArrayList;
import java.util.List;

public class CursorManager {
    private List<Cursor> cursors;
    private Cursor currentCursor;

    public CursorManager() {
        cursors = new ArrayList<>();
        currentCursor = null;
    }

    public Cursor getCurrentCursor() {
        return currentCursor;
    }

    public void addCursor(int id) {
        if (!isCursorIdExists(id)) {
            Cursor newCursor = new Cursor(id);
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

    public void removeCursor(int id) {
        for (Cursor cursor : cursors) {
            if (cursor.getId() == id) {
                cursors.remove(cursor);
                return;
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