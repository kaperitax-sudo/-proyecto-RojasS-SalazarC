import java.util.ArrayList;
import java.util.Random;

/**
 * Una rueda de la maquina. Tiene sus simbolos en orden
 * y muestra uno solo: el simbolo visible.
 */
public class Wheel
{
    public static final int WIDTH = 34;
    public static final int HEIGHT = 40;

    private ArrayList<Symbol> symbols;
    private Rectangle window;              // el marco de la rueda
    private int current;                   // cual simbolo se ve
    private int xPosition;
    private int yPosition;
    private boolean isVisible;

    /**
     * Crea una rueda invisible con los colores dados.
     * @param colors colores de los simbolos, en orden
     */
    public Wheel(ArrayList<String> colors)
    {
        symbols = new ArrayList<Symbol>();
        for (String color : colors) {
            symbols.add(new Symbol(color));     // un Symbol por color
        }
        window = new Rectangle();
        window.changeSize(HEIGHT, WIDTH);
        window.changeColor("black");
        current = 0;
        isVisible = false;
    }

    /**
     * Adiciona un simbolo en la posicion dada.
     * @param index posicion en la lista
     * @param color color del simbolo
     */
    public void addSymbol(int index, String color)
    {
        symbols.add(index, new Symbol(color));
        refresh();
    }

    /**
     * Elimina el simbolo de ese color.
     * @param color color del simbolo
     */
    public void delSymbol(String color)
    {
        for (int i = 0; i < symbols.size(); i++) {
            if (symbols.get(i).is(color)) {
                symbols.get(i).makeInvisible();
                symbols.remove(i);
                break;                          // ya lo borro, sale
            }
        }
        refresh();
    }

    /**
     * Gira la rueda: deja visible un simbolo al azar.
     * @param random generador de la maquina
     */
    public void spin(Random random)
    {
        if (!symbols.isEmpty()) {
            current = random.nextInt(symbols.size());   // 0 hasta size-1
            refresh();
        }
    }

    /**
     * Deja visible el simbolo de ese color.
     * @param color color del simbolo
     */
    public void place(String color)
    {
        for (int i = 0; i < symbols.size(); i++) {
            if (symbols.get(i).is(color)) {
                current = i;
                refresh();
                return;
            }
        }
    }

    /**
     * Indica el color del simbolo visible.
     * @return el color, o cadena vacia si no hay simbolos
     */
    public String symbol()
    {
        if (symbols.isEmpty()) {
            return "";
        }
        return symbols.get(current).color();
    }

    /**
     * Ubica la rueda en el canvas.
     * @param x coordenada horizontal
     * @param y coordenada vertical
     */
    public void moveTo(int x, int y)
    {
        xPosition = x;
        yPosition = y;
        window.moveTo(x, y);
        refresh();
    }

    /**
     * Cambia el color del marco.
     * @param color nombre de un color CSS
     */
    public void changeColor(String color)
    {
        window.changeColor(color);
    }

    /**
     * Hace visible la rueda.
     */
    public void makeVisible()
    {
        isVisible = true;
        window.makeVisible();
        refresh();
    }

    /**
     * Hace invisible la rueda.
     */
    public void makeInvisible()
    {
        hideAll();
        window.makeInvisible();
        isVisible = false;
    }

    /**
     * Esconde todo y muestra el simbolo que toca.
     */
    private void refresh()
    {
        hideAll();
        if (symbols.isEmpty()) {
            return;
        }
        fixCurrent();
        showCurrent();
    }

    /**
     * Baja current si quedo fuera de rango al borrar simbolos.
     */
    private void fixCurrent()
    {
        if (current >= symbols.size()) {
            current = symbols.size() - 1;
        }
    }

    /**
     * Centra y muestra el simbolo visible.
     */
    private void showCurrent()
    {
        // esquina de la rueda + la mitad de lo que sobra
        int x = xPosition + (WIDTH - Symbol.SIZE) / 2;
        int y = yPosition + (HEIGHT - Symbol.SIZE) / 2;

        Symbol symbol = symbols.get(current);
        symbol.moveTo(x, y);
        if (isVisible) {
            symbol.makeVisible();
        }
    }

    /**
     * Esconde todos los simbolos.
     */
    private void hideAll()
    {
        for (Symbol symbol : symbols) {
            symbol.makeInvisible();
        }
    }
}