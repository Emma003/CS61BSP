package DebugExercise;

public class disc {
    public int x, y;
    public disc (int x, int y) {
        this.x = x;
        this.y = y;
    }
        public static void switcheroo (disc a, disc b) {
            disc temp = a;
            a = b;
            b = temp; }
        public static void fliperoo (disc a, disc b) {
            disc temp = new disc(a.x, a.y);
            a.x = b.x;
            a.y = b.y;
            b.x = temp.x;
            b.y = temp.y;
        }
        public static void swaperoo (disc a, disc b) {
            disc temp = a;
            a.x = b.x;
            a.y = b.y;
            b.x = temp.x;
            b.y = temp.y;
        }

        public static void main (String[] args){
            disc foobar = new disc(10, 20);
            disc baz = new disc(30, 40);
            switcheroo(foobar, baz);
            System.out.println("switcheroo: " + foobar.x + foobar.y + baz.x + baz.y);
            fliperoo(foobar, baz);
            System.out.println("fliperoo: " + foobar.x + foobar.y + baz.x + baz.y);
            swaperoo(foobar, baz);
            System.out.println("swaperoo: " + foobar.x + foobar.y + baz.x + baz.y);
        }
}
