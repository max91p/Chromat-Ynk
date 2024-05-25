package com.example;

/**
 * Represents a color in RGB format.
 */
public class ColorOfLine {
    private int red;
    private int green;
    private int blue;


    /**
     * Constructs a new color with the specified red, green, and blue values between 0 and 255.
     *
     * @param red the red component of the new color
     * @param green the green component of the new color
     * @param blue the blue component of the new color
     */
    public ColorOfLine(int red, int green, int blue){
        try {
            if (red < 0 || red > 255 || green < 0 || green > 255 || blue < 0 || blue > 255) {
                throw new ErrorLogger("Les valeurs de rouge, vert et bleu doivent être comprises entre 0 et 255");
            } else {
                this.red = red;
                this.green = green;
                this.blue = blue;
            }
        }catch (ErrorLogger e){
            e.logError();
        }
    }
        

    /**
     * Constructs a new color with the specified hexadecimal code.
     *
     * @param hex the hexadecimal code of the new color
     */
    public ColorOfLine(String hex){
        try{
            if(hex.length() != 7 || hex.charAt(0) != '#' || !hex.substring(1).matches("[0-9A-F]+")){
                throw new ErrorLogger("Le code hexadécimal doit être sous la forme #RRGGBB où RR, GG et BB sont des valeurs hexadécimales comprises entre 00 et FF");
            }
            else{
                this.red = Integer.parseInt(hex.substring(1, 3), 16);
                this.green = Integer.parseInt(hex.substring(3, 5), 16);
                this.blue = Integer.parseInt(hex.substring(5, 7), 16);
            }
        }catch (ErrorLogger e){
            e.logError();
        }
    }
        

    /**
    * Constructs a new color with the specified red, green, and blue values between 0 and 1.
    *
    * @param red the red component of the new color
    * @param green the green component of the new color
    * @param blue the blue component of the new color
    */
    public ColorOfLine(double red, double green, double blue){
        try{
            if(red < 0 || red > 1 || green < 0 || green > 1 || blue < 0 || blue > 1){
                throw new ErrorLogger("Les valeurs de rouge, vert et bleu doivent être comprises entre 0 et 1");
            } else {
                this.red = (int) (red * 255);
                this.green = (int) (green * 255);
                this.blue = (int) (blue * 255);
            }
        }catch (ErrorLogger e){
            e.logError();
        }
    }

    /**
     * Returns the red component of this color.
     *
     * @return the red component of this color
     */
    public int getRed(){
        return red;
    }

    /**
     * Returns the green component of this color.
     *
     * @return the green component of this color
     */
    public int getGreen(){
        return green;
    }

    /**
     * Returns the blue component of this color.
     *
     * @return the blue component of this color
     */
    public int getBlue(){
        return blue;
    }

    /**
     * Sets the red component of this color.
     *
     * @param red the new red component of this color
     */
    public void setRed(int red){
        try{
            if(red < 0 || red > 255){
                throw new ErrorLogger("La valeur de rouge doit être comprise entre 0 et 255");
            } else {
                this.red = red;
            }
        }catch (ErrorLogger e){
            e.logError();
        }
    }

    /**
     * Sets the green component of this color.
     *
     * @param green the new green component of this color
     */
    public void setGreen(int green){
        try{
            if(green < 0 || green > 255){
                throw new ErrorLogger("La valeur de vert doit être comprise entre 0 et 255");
            } else {
                this.green = green;
            }
        }catch (ErrorLogger e){
            e.logError();
        }
    }

    /**
     * Sets the blue component of this color.
     *
     * @param blue the new blue component of this color
     */
    public void setBlue(int blue){
        try{
            if(blue < 0 || blue > 255){
                throw new ErrorLogger("La valeur de bleu doit être comprise entre 0 et 255");
            } else {
                this.blue = blue;
            }
        }catch (ErrorLogger e){
            e.logError();
        }
    }

    /**
     * Returns a string representation of this color.
     *
     * @return a string representation of this color
     */
    @Override
    public String toString(){
        return "Red : " + red + ", Green : " + green + ", Blue : " + blue;
    }

    /**
     * Returns the hexadecimal code of this color.
     *
     * @return the hexadecimal code of this color
     */
    public String getHex(){
        return String.format("#%02x%02x%02x", red, green, blue);
    }

    /**
     * Returns the RGB values of this color.
     *
     * @return the RGB values of this color
     */
    public double[] getRGB(){
        return new double[]{red / 255.0, green / 255.0, blue / 255.0};
    }



}
