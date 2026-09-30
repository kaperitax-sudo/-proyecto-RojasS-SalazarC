import java.awt.Color;
import java.util.HashMap;

/**
 * Colores CSS.
 * Traduce el color dado en texto a color de java.
 */
public class Colors
{
    
    public static final int MAX_SYMBOL_COLORS = 50;   // cuantos sirven como simbolo

    // los colores que se pueden repartir como simbolo, en orden
    private static final String[] SYMBOL_COLORS = {
        "red", "blue", "green", "yellow", "orange", "purple",
        "cyan", "magenta", "brown", "pink", "lime", "navy",
        "teal", "olive", "maroon", "silver", "crimson", "salmon",
        "coral", "tomato", "chocolate", "peru", "tan", "khaki",
        "beige", "wheat", "orchid", "violet", "plum", "indigo",
        "lavender", "turquoise", "aquamarine", "skyblue", "steelblue", "royalblue",
        "slateblue", "midnightblue", "seagreen", "olivedrab", "forestgreen", "springgreen",
        "chartreuse", "darkgreen", "darkred", "darkorange", "darkviolet", "hotpink",
        "deeppink", "firebrick"
    };
    
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
    
        /**
     * Presta los primeros n colores que sirven como simbolo.
     * @param n cuantos colores se necesitan
     * @return los nombres, todos diferentes, o null si no alcanzan
     */
    public static String[] palette(int n)
    {
        if (n < 0 || n > MAX_SYMBOL_COLORS) {
            return null;                       // no hay tantos colores
        }
        String[] result = new String[n];
        for (int i = 0; i < n; i++) {
            result[i] = SYMBOL_COLORS[i];      // los primeros n de la lista
        }
        return result;
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
        colors.put("cyan",         new Color(0, 255, 255));
        colors.put("magenta",      new Color(255, 0, 255));
        colors.put("brown",        new Color(165, 42, 42));
        colors.put("pink",         new Color(255, 192, 203));
        colors.put("lime",         new Color(0, 255, 0));
        colors.put("navy",         new Color(0, 0, 128));
        colors.put("teal",         new Color(0, 128, 128));
        colors.put("olive",        new Color(128, 128, 0));
        colors.put("maroon",       new Color(128, 0, 0));
        colors.put("silver",       new Color(192, 192, 192));
        colors.put("crimson",      new Color(220, 20, 60));
        colors.put("salmon",       new Color(250, 128, 114));
        colors.put("coral",        new Color(255, 127, 80));
        colors.put("tomato",       new Color(255, 99, 71));
        colors.put("chocolate",    new Color(210, 105, 30));
        colors.put("peru",         new Color(205, 133, 63));
        colors.put("tan",          new Color(210, 180, 140));
        colors.put("khaki",        new Color(240, 230, 140));
        colors.put("beige",        new Color(245, 245, 220));
        colors.put("wheat",        new Color(245, 222, 179));
        colors.put("orchid",       new Color(218, 112, 214));
        colors.put("violet",       new Color(238, 130, 238));
        colors.put("plum",         new Color(221, 160, 221));
        colors.put("indigo",       new Color(75, 0, 130));
        colors.put("lavender",     new Color(230, 230, 250));
        colors.put("turquoise",    new Color(64, 224, 208));
        colors.put("aquamarine",   new Color(127, 255, 212));
        colors.put("skyblue",      new Color(135, 206, 235));
        colors.put("steelblue",    new Color(70, 130, 180));
        colors.put("royalblue",    new Color(65, 105, 225));
        colors.put("slateblue",    new Color(106, 90, 205));
        colors.put("midnightblue", new Color(25, 25, 112));
        colors.put("seagreen",     new Color(46, 139, 87));
        colors.put("olivedrab",    new Color(107, 142, 35));
        colors.put("forestgreen",  new Color(34, 139, 34));
        colors.put("springgreen",  new Color(0, 255, 127));
        colors.put("chartreuse",   new Color(127, 255, 0));
        colors.put("darkgreen",    new Color(0, 100, 0));
        colors.put("darkred",      new Color(139, 0, 0));
        colors.put("darkorange",   new Color(255, 140, 0));
        colors.put("darkviolet",   new Color(148, 0, 211));
        colors.put("hotpink",      new Color(255, 105, 180));
        colors.put("deeppink",     new Color(255, 20, 147));
        colors.put("firebrick",    new Color(178, 34, 34));
        return colors;
    }
}