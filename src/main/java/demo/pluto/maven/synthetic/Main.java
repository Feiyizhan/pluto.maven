package demo.pluto.maven.synthetic;

public class Main {

    private static class Inner {
    }
    static void checkSynthetic (String name) {
        try {
            System.out.println (name + " : " + Class.forName (name).isSynthetic ());
        } catch (ClassNotFoundException exc) {
            exc.printStackTrace (System.out);
        }
    } 
    public static void main(String[] args) throws Exception
    {
        new Inner ();
        checkSynthetic (Main.class.getName());
        checkSynthetic (Main.class.getName()+"$Inner");
        checkSynthetic (Main.class.getName()+"$1");
    }
}

