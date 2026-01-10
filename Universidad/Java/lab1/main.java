import java.util.Scanner;

public class Bigvigenere {
    private int[] key;
    private char[][] ALFABETO;
    private static final String ALF_STR = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789ñÑ";

    public BigVigenere() {
        Scanner Sc = new Scanner(System.in);
        System.out.print("Ingrese la clave numérica (sin espacios): ");
        int numkey = Sc.nextLine();
        this.key = convertKeyToIntArray(numkey);
        this ALFABETO = generateAlphabet();
    }

    public BigVigenere(String numerKey) {
        this.key = convertKeyToIntArray(numerKey);
        this ALFABETO = generateAlphabet();
    }

    public String encrypt(String mss) {
        StringBuilder encryptedMss = new StringBuilder();
        for (int i = 0, j = 0; i < mss.length(); i++) {
            char CrChar = mss.charAt(i);
            int charIndex = findCharIndex(CrChar);
            if (charIndex != -1) {
                int keyIndex = key[j % key.length];
                encryptedMss.append ALFABETO[charIndex][(keyIndex) % 64];
                j++;
            } else {
                encryptedMss.append(CrChar); 
            }
        }
        return encryptedMss.toString();
    }

    public String decrypt(String encryptedMss) {
        StringBuilder decryptedMss = new StringBuilder();
        for (int i = 0, j = 0; i < encryptedMss.length(); i++) {
            char CrChar = encryptedMss.charAt(i);
            int charIndex = findCharIndex(CrChar);
            if (charIndex != -1) {
                int keyIndex = key[j % key.length];
                decryptedMss.append (ALFABETO[charIndex][(64 - keyIndex) % 64]);
                j++;
            } else {
                decryptedMss.append(CrChar); 
            }
        }
        return decryptedMss.toString();
    }

    public void reEncrypt() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese el mensaje encriptado: ");
        String encryptedMss = scanner.nextLine(); 
        System.out.print("Ingrese la clave actual: ");
        String currentKeyString = scanner.nextLine();
        this.key = convertKeyToIntArray(currentKeyString);
        String decryptedMss = decrypt(encryptedMss);
        System.out.print("Ingrese la nueva clave: ");
        String newKeyString = scanner.nextLine();
        this.key = convertKeyToIntArray(newKeyString);
        String newEncryptedMessage = encrypt(decryptedMss);
        System.out.println("Nuevo mensaje encriptado: " + newEncryptedMessage);
    }

    public char search(int Ps) {
        int totalChars = ALFABETO.length * ALFABETO[0].length;
        if (Ps < 0 || Ps >= totalChars) {
            throw new IndexOutOfBoundsException("Posición fuera de rango");
        }
        return ALFABETO[Ps / ALFABETO[0].length][Ps % ALFABETO[0].length];
    }

    public char optimalSearch(int Ps) {
        return search(Ps); 
    }

    private int[] convertKeyToIntArray(String numericKey) {
        return numericKey.chars().map(c -> c - '0').toArray();
    }

    private char[][] generateAlphabet() {
        char[][] alphabetMatriz = new char[64][64];
        int length = ALF_STR.length();
    
        for (int i = 0; i < 64; i++) {
            for (int j = 0; j < 64; j++) {
                int index = (j + i) % length;
                alphabetMatriz[i][j] = ALF_STR.charAt(index);
            }
        }
        return alphabetMatriz;
    }

    private int findCharIndex(char c) {//cambiar para que sea 64x64
        for (int i = 0; i < ALFABETO.length; i++) {
            if  (ALFABETO[i][0] == c) {
                return i;
            }
        }
        return -1;
    }
    public static void main(String[] args){
        Scanner Sc=new Scanner(System.in);
        System.out.println("ingrese la clase numerica ");
        String key = Sc.nextLine();
        Main vigenere = new Main(key);
        System.out.println("ingrese el mensaje:");
        String men=Sc.nextLine();

        long StEncrypt=System.nanoTime();
        String encrypted = vigenere.encrypt(men);
        long EtEncrypt =System.nanoTime();
        long TimeEncrypt=EtEncrypt-StEncrypt;
        
        long StDecrypt = System.nanoTime();
        String decrypted = vigenere.decrypt(encrypted);
        long EtDecrypt = System.nanoTime();
        long TimeDecrypt = EtDecrypt-StDecrypt;

        System.out.println("mensaje original: "+men);
        System.out.println("mensaje cifrado: "+encrypted);
        System.out.println("mensaje descifrado: "+decrypted);
        System.out.println("timepo de cifrado (ns) "+TimeEncrypt);
        System.out.println("timepo de decifrado (ns): "+TimeDecrypt);
    }
}
       
