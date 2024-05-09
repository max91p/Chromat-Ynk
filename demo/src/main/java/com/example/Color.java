package com.example;

public class Color {
    private int red;
    private int green;
    private int blue;

    public Color(int red, int green, int blue){
        if(red < 0 || red > 255 || green < 0 || green > 255 || blue < 0 || blue > 255){
            throw new IllegalArgumentException("Les valeurs de rouge, vert et bleu doivent être comprises entre 0 et 255");
        } else {
            this.red = red;
            this.green = green;
            this.blue = blue;
        }
    }

    public Color(String hex){
        if(hex.length() != 7 || hex.charAt(0) != '#' || !hex.substring(1).matches("[0-9A-F]+")){
            throw new IllegalArgumentException("Le code hexadécimal doit être sous la forme #RRGGBB où RR, GG et BB sont des valeurs hexadécimales comprises entre 00 et FF");
        }
        this.red = Integer.parseInt(hex.substring(1, 3), 16);
        this.green = Integer.parseInt(hex.substring(3, 5), 16);
        this.blue = Integer.parseInt(hex.substring(5, 7), 16);
    }

    public Color(double red, double green, double blue){
        if(red < 0 || red > 1 || green < 0 || green > 1 || blue < 0 || blue > 1){
            throw new IllegalArgumentException("Les valeurs de rouge, vert et bleu doivent être comprises entre 0 et 1");
        } else {
            this.red = (int) (red * 255);
            this.green = (int) (green * 255);
            this.blue = (int) (blue * 255);
        }
    }

    public int getRed(){
        return red;
    }

    public int getGreen(){
        return green;
    }

    public int getBlue(){
        return blue;
    }

    public void setRed(int red){
        if(red < 0 || red > 255){
            throw new IllegalArgumentException("La valeur de rouge doit être comprise entre 0 et 255");
        } else {
            this.red = red;
        }
    }

    public void setGreen(int green){
        if(green < 0 || green > 255){
            throw new IllegalArgumentException("La valeur de vert doit être comprise entre 0 et 255");
        } else {
            this.green = green;
        }
    }

    public void setBlue(int blue){
        if(blue < 0 || blue > 255){
            throw new IllegalArgumentException("La valeur de bleu doit être comprise entre 0 et 255");
        } else {
            this.blue = blue;
        }
    }

    public String toString(){
        return "Rouge : " + red + ", Vert : " + green + ", Bleu : " + blue;
    }

    public String getHex(){
        return String.format("#%02x%02x%02x", red, green, blue);
    }

    public double[] getRGB(){
        return new double[]{red / 255.0, green / 255.0, blue / 255.0};
    }



}
