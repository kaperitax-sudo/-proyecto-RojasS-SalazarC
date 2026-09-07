import java.util.ArrayList;

/**
 * Un simbolo de la maquina. Se identifica por su color.
 */
public class Symbol
{
    public static final int SIZE = 22;              // tamaño del simbolo

    private String color;
    private ArrayList<Rectangle> parts;             // sus pedacitos
    private int xPosition;
    private int yPosition;

    /**
     * Crea un simbolo invisible del color dado.
     * @param color nombre de un color CSS
     */
    public Symbol(String color)
    {
        this.color = color;                          // this.color = el atributo
        parts = new ArrayList<Rectangle>();          // lista vacia
        xPosition = 0;
        yPosition = 0;
        build();                                     // arma los pedacitos
    }

    /**
     * Arma la figura. 
     */
    private void build()  //solo simbol usa esto asi que es privado
    {
        Rectangle part = new Rectangle();
        part.changeSize(SIZE, SIZE);                 // alto, ancho
        part.changeColor(color);
        parts.add(part);                             // lo mete a la lista
    }

    /**
     * Indica el color del simbolo.
     * @return nombre del color
     */
    public String color()
    {
        return color;
    }

    /**
     * Indica si el simbolo es de ese color.
     * @param other nombre de color
     * @return true si es el mismo
     */
    public boolean is(String other)
    {
        return other != null && color.equalsIgnoreCase(other);
    }

    /**
     * Ubica el simbolo en el canvas.
     * @param x coordenada horizontal
     * @param y coordenada vertical
     */
    public void moveTo(int x, int y)
    {
        xPosition = x;
        yPosition = y;
        for (Rectangle part : parts) {               // recorre los pedacitos
            part.moveTo(x, y);
        }
    }

    /**
     * Hace visible el simbolo.
     */
    public void makeVisible()
    {
        for (Rectangle part : parts) {
            part.makeVisible();
        }
    }

    /**
     * Hace invisible el simbolo.
     */
    public void makeInvisible()
    {
        for (Rectangle part : parts) {
            part.makeInvisible();
        }
    }
}