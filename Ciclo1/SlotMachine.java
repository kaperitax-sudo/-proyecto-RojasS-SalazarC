import java.util.ArrayList;
import java.util.Random;
import javax.swing.JOptionPane;

/**
 * Simulador de una maquina tragamonedas.
 * La maquina tiene ruedas y todas las ruedas tienen los mismos simbolos.
 * Cada simbolo se identifica por su color y todos son diferentes.
 */
public class SlotMachine
{
    public static final int MAX_WHEELS = 6;
    public static final int MAX_SYMBOLS = 6;

    private static final int BODY_X = 10;          // esquina del cuerpo
    private static final int BODY_Y = 60;
    private static final int BODY_WIDTH = 280;     // el canvas mide 300
    private static final int BODY_HEIGHT = 110;
    private static final int GAP = 8;              // espacio entre ruedas

    private ArrayList<Wheel> wheels;
    private ArrayList<String> symbols;             // los colores, en orden
    private Rectangle body;                        // el cuerpo de la maquina
    private Random random;
    private boolean isVisible;
    private boolean ok;                            // si se pudo la ultima operacion

    /**
     * Crea una maquina vacia e invisible.
     */
    public SlotMachine()
    {
        wheels = new ArrayList<Wheel>();
        symbols = new ArrayList<String>();
        random = new Random();
        body = new Rectangle();
        body.changeSize(BODY_HEIGHT, BODY_WIDTH);
        body.moveTo(BODY_X, BODY_Y);
        body.changeColor("dimgray");
        isVisible = false;
        ok = true;
    }

    /**
     * Adiciona una rueda en la posicion dada.
     * @param pos posicion de la rueda, empezando en 1
     */
    public void addWheel(int pos)
    {
        if (wheels.size() >= MAX_WHEELS) {
            error("No puede tener mas de " + MAX_WHEELS + " ruedas.");
            return;
        }
        Wheel wheel = new Wheel(symbols);          // nace con los simbolos actuales
        wheels.add(index(pos, wheels.size() + 1), wheel);
        if (isVisible) {
            wheel.makeVisible();
        }
        layout();
        ok = true;
    }

    /**
     * Elimina la rueda de la posicion dada.
     * @param pos posicion de la rueda, empezando en 1
     */
    public void delWheel(int pos)
    {
        if (wheels.isEmpty()) {
            error("La maquina no tiene ruedas.");
            return;
        }
        Wheel wheel = wheels.remove(index(pos, wheels.size()));
        wheel.makeInvisible();
        layout();
        ok = true;
    }

    /**
     * Adiciona un simbolo en la posicion dada.
     * @param pos posicion del simbolo, empezando en 1
     * @param color nombre de un color CSS, diferente a los actuales
     */
    public void addSymbol(int pos, String color)
    {
        if (symbols.size() >= MAX_SYMBOLS) {
            error("No puede tener mas de " + MAX_SYMBOLS + " simbolos.");
            return;
        }
        if (!Colors.isValid(color)) {
            error("El color " + color + " no existe.");
            return;
        }
        String value = color.toLowerCase();
        if (symbols.contains(value)) {
            error("Los simbolos deben ser de colores diferentes.");
            return;
        }
        insert(index(pos, symbols.size() + 1), value);
        ok = true;
    }

    /**
     * Elimina el simbolo de ese color.
     * @param symbol nombre del color
     */
    public void delSymbol(String symbol)
    {
        String value = "";
        if (symbol != null) {
            value = symbol.toLowerCase();
        }
        if (!symbols.remove(value)) {              // remove avisa si no estaba
            error("La maquina no tiene el simbolo " + symbol + ".");
            return;
        }
        for (Wheel wheel : wheels) {
            wheel.delSymbol(value);
        }
        refresh();
        ok = true;
    }

    /**
     * Deja visible un simbolo en una rueda.
     * @param wheel posicion de la rueda, empezando en 1
     * @param symbol nombre del color
     */
    public void placeSymbol(int wheel, String symbol)
    {
        if (wheels.isEmpty()) {
            error("La maquina no tiene ruedas.");
            return;
        }
        String value = "";
        if (symbol != null) {
            value = symbol.toLowerCase();
        }
        if (!symbols.contains(value)) {
            error("La maquina no tiene el simbolo " + symbol + ".");
            return;
        }
        wheels.get(index(wheel, wheels.size())).place(value);
        refresh();
        ok = true;
    }

    /**
     * Gira una rueda.
     * @param wheel posicion de la rueda, empezando en 1
     */
    public void spin(int wheel)
    {
        if (!canSpin()) {
            return;
        }
        wheels.get(index(wheel, wheels.size())).spin(random);
        refresh();
        ok = true;
    }

    /**
     * Gira todas las ruedas.
     */
    public void spin()
    {
        if (!canSpin()) {
            return;
        }
        for (Wheel wheel : wheels) {
            wheel.spin(random);
        }
        refresh();
        ok = true;
    }

    /**
     * Consulta los simbolos de la maquina, en el orden de la rueda.
     * @return los colores de los simbolos
     */
    public String[] symbols()
    {
        ok = true;
        return symbols.toArray(new String[0]);     // lista -> arreglo
    }

    /**
     * Consulta cuantos colores diferentes hay en la configuracion.
     * @return numero de simbolos distintos visibles
     */
    public int distinctSymbols()
    {
        ArrayList<String> found = new ArrayList<String>();
        for (Wheel wheel : wheels) {
            String color = wheel.symbol();
            if (!color.equals("") && !found.contains(color)) {
                found.add(color);                  // solo si no estaba
            }
        }
        ok = true;
        return found.size();
    }

    /**
     * Consulta los colores visibles, de izquierda a derecha.
     * @return la configuracion actual
     */
    public String[] configuration()
    {
        String[] result = new String[wheels.size()];
        for (int i = 0; i < wheels.size(); i++) {
            result[i] = wheels.get(i).symbol();
        }
        ok = true;
        return result;
    }

    /**
     * Indica si la configuracion es la ganadora.
     * @return true si todas las ruedas muestran lo mismo
     */
    public boolean isJackpot()
    {
        ok = true;
        return jackpot();
    }

    /**
     * Hace visible el simulador.
     */
    public void makeVisible()
    {
        isVisible = true;
        body.makeVisible();
        for (Wheel wheel : wheels) {
            wheel.makeVisible();
        }
        refresh();
        ok = true;
    }

    /**
     * Hace invisible el simulador. La maquina sigue funcionando.
     */
    public void makeInvisible()
    {
        for (Wheel wheel : wheels) {
            wheel.makeInvisible();
        }
        body.makeInvisible();
        isVisible = false;
        ok = true;
    }

    /**
     * Termina el simulador.
     */
    public void exit()
    {
        makeInvisible();
        System.exit(0);
    }

    /**
     * Indica si se logro realizar la ultima operacion.
     * @return true si se pudo
     */
    public boolean ok()
    {
        return ok;
    }

    /**
     * Mete el simbolo en la maquina y en todas las ruedas.
     */
    private void insert(int position, String color)
    {
        symbols.add(position, color);
        for (Wheel wheel : wheels) {
            wheel.addSymbol(position, color);
        }
        refresh();
    }

    /**
     * Revisa si se puede girar y avisa si no.
     */
    private boolean canSpin()
    {
        if (wheels.isEmpty()) {
            error("La maquina no tiene ruedas.");
            return false;
        }
        if (symbols.isEmpty()) {
            error("La maquina no tiene simbolos.");
            return false;
        }
        return true;
    }

    /**
     * Calcula si todas las ruedas muestran el mismo simbolo.
     */
    private boolean jackpot()
    {
        if (wheels.isEmpty() || symbols.isEmpty()) {
            return false;
        }
        String first = wheels.get(0).symbol();
        for (Wheel wheel : wheels) {
            if (!first.equals(wheel.symbol())) {
                return false;                      // uno distinto y ya no gana
            }
        }
        return true;
    }

    /**
     * Reparte las ruedas centradas dentro del cuerpo.
     */
    private void layout()
    {
        int used = wheels.size() * Wheel.WIDTH + (wheels.size() - 1) * GAP;
        int x = BODY_X + (BODY_WIDTH - used) / 2;
        int y = BODY_Y + (BODY_HEIGHT - Wheel.HEIGHT) / 2;
        for (Wheel wheel : wheels) {
            wheel.moveTo(x, y);
            x = x + Wheel.WIDTH + GAP;             // la siguiente mas a la derecha
        }
        refresh();
    }

    /**
     * La maquina luce diferente si llego a un estado ganador.
     */
    private void refresh()
    {
        if (jackpot()) {
            paint("gold", "gold");
        }
        else {
            paint("dimgray", "black");
        }
    }

    /**
     * Pinta el cuerpo y los marcos de las ruedas.
     */
    private void paint(String bodyColor, String wheelColor)
    {
        body.changeColor(bodyColor);
        for (Wheel wheel : wheels) {
            wheel.changeColor(wheelColor);
            if (isVisible) {
                wheel.makeVisible();               // la redibuja encima del cuerpo
            }
        }
    }

    /**
     * Pasa una posicion del usuario (desde 1) a un indice de lista (desde 0).
     * Si es menor que 1 usa 1, si es mayor que max usa max.
     */
    private int index(int pos, int max)
    {
        int value = Math.max(1, Math.min(pos, max));
        return value - 1;
    }

    /**
     * Avisa al usuario, solo si el simulador esta visible.
     */
    private void error(String message)
    {
        ok = false;
        if (isVisible) {
            JOptionPane.showMessageDialog(null, message);
        }
    }
}