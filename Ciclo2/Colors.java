import java.awt.Color;
import java.util.HashMap;

/**
 * Colores CSS.
 * Traduce el color dado en texto a color de java.
 */
public class Colors
{
    private static HashMap<String, Color> table = create();
    // static = una sola tabla, se llena sola la primera vez
    /**
     * Traduce el nombre 
     * @param name nombre del color
     * @return el color, o null.
     */
    public static Color toColor(String name)
    {
        if (name == null) {
            return null;
            
        }
        return table.get(name.toLowerCase());  //toLowerCase para minusculas  "Red" → "red"
    }

    /**
     * Saber si pertenece a un color CSS conocido.
     * @param name nombre del color
     * @return true si el color existe
     */
    
    public static boolean isValid(String name)
    {
        return toColor(name) != null;
    }

    /**A
     * Construye la tabla: primarios y secundarios de la rueda cromatica.
     * 
     */
    private static HashMap<String, Color> create()
    {
        HashMap<String, Color> colors = new HashMap<String, Color>();
        colors.put("yellow",  new Color(255, 255, 0));
        colors.put("red",     new Color(255, 0, 0));
        colors.put("blue",    new Color(0, 0, 255));
        colors.put("orange",  new Color(255, 165, 0));
        colors.put("green",   new Color(0, 128, 0));
        colors.put("purple",  new Color(128, 0, 128));
        colors.put("dimgray", new Color(105, 105, 105));
        colors.put("black",   new Color(0, 0, 0));
        colors.put("gold",    new Color(255, 215, 0));
        return colors;
    }
}