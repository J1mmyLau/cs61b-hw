package flik;

public class HorribleSteve {
    public static void main(String[] args) throws Exception {
        int i = 0;
        for (int j = 0; j < 500; ++i, ++j) {
            if (!Integer.valueOf(i).equals(Integer.valueOf(j))) {
                throw new Exception(
                        String.format("i:%d not same as j:%d ??", i, j));
            }
        }
        System.out.println("i is " + i);
    }
}