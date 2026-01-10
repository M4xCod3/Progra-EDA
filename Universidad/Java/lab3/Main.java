import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.io.FileNotFoundException;

public class Main {
    private static class GenerateData {
        private Random rand = new Random();
        private String[] PLNewName;
        private String[] PlName = {"Dragon", "Empire", "Quest", "Galaxy", "Legend", "Warrior"};
        private String[] CnName = {"of", "in","on","and"};
        private String[] IniName = {"The", "Gran", "Super"};
        private String[] Cat = {"Avanetura", "Accion", "RPG", "Simulador", "MMO"};

        public GenerateData() {
            try {
                File file = new File("Lista-Nombres-Juegos.txt"); // nombre del archivo con palabras
                Scanner scanner = new Scanner(file);
                ArrayList<String> words = new ArrayList<>();
                while (scanner.hasNext()) {
                    words.add(scanner.next());
                }
                scanner.close();
                PLNewName = words.toArray(new String[0]);
            } catch (FileNotFoundException e) {
                System.out.println("Archivo palabras.txt no encontrado. Se usará PlName por defecto.");
                PLNewName = PlName; // fallback
            }
        }

        public Game genereteGame() {
            int price = rand.nextInt(70000) + 1;
            int quality = rand.nextInt(100) + 1;
            String Name = IniName[rand.nextInt(IniName.length)] + " "
                    + PLNewName[rand.nextInt(PLNewName.length)] + " " +
                    CnName[rand.nextInt(CnName.length)] + " " +
                    PlName[rand.nextInt(PlName.length)];
            String cate = Cat[rand.nextInt(Cat.length)];
            return new Game(Name, cate, price, quality);
        }
    }

    public static class Game {
        private String name;
        private String Category;
        private int price;
        private int quality; //0-100
        public static ArrayList<Game> games = new ArrayList<>();

        public Game(String name, String Category, int price, int quality) {
            this.name = name;
            this.Category = Category;
            this.price = price;
            this.quality = quality;
        }

        public String getName() {
            return name;
        }

        public String getCategory() {
            return Category;
        }

        public int getPrice() {
            return price;
        }

        public int getQuality() {
            return quality;
        }

        public String toString() {
            return "Game{" + " name= " + name + " => categoria=" + Category + " => price= " + price + " => quality= " + quality + "}";
        }

        public static void GameMain(int cant) {
            GenerateData gd = new GenerateData();
            Scanner sc = new Scanner(System.in);
            for (int i = 0; i < cant; i++) {
                Game game = gd.genereteGame();
                games.add(game);
            }
            System.out.println("Games: ");
            for (Game game : Game.games) {
                System.out.println(game);
            }
        }
    }

    public static class Dataset {
        private ArrayList<Game> Data = new ArrayList<>();
        private String sortedByAtrribute;//(precio,categoria,quality)

        public Dataset(ArrayList<Game> Data) {
            this.Data = Data;
        }

        public boolean OrdenadoPrice(){
            for(int i = 1; i < Data.size(); i++){
                if(Data.get(i -1).getPrice() > Data.get(i).getPrice()){
                    return false;
                }
            }
            return true;
        }

        public boolean OrdenadoQuality(){
            for(int i = 1; i < Data.size(); i++){
                if(Data.get(i -1).getQuality() > Data.get(i).getQuality()){
                    return false;
                }
            }
            return true;
        }

        public void getGamesByPrice(int price) {
            //entrega una arraylist con todos los juegos cuyo precio sea igual
            //al parametro entregado
            //si el dataset esta ordenado por precio, se usa busqueda binaria
            //si no, busqueda lineal
            ArrayList<Game> Reslt = new ArrayList<>();
            if(OrdenadoPrice()){//busqueda binaria
                int left=0;
                int right=Data.size()-1;
                while(left<=right){
                    int mid=(left+right)/2;
                    Game gm=Data.get(mid);
                    if(gm.getPrice()==price){
                        int temp = mid;
                        while (temp >= 0 && Data.get(temp).getPrice() == price) {
                            Reslt.add(Data.get(temp));
                            temp--;
                        }
                        temp = mid + 1;
                        while (temp < Data.size() && Data.get(temp).getPrice() == price) {
                            Reslt.add(Data.get(temp));
                            temp++;
                        }
                        break;
                    }
                    else if (gm.getPrice()<price) {
                        left=mid+1;
                    }
                    else{
                        right=mid-1;
                    }
                }
            }
            else{
                for (Game gm : Data) {
                    if (gm.getPrice() == price) {
                        Reslt.add(gm);
                    }
                }
            }
        }

        public ArrayList<Game> getGamesByPriceRange(int lowerPrice, int higherPrice) {
            //retorna todos los juegos cuyo precio este entre
            //[lowerPrice, highPrice]
            //si el dataset esta ordenado por precio, se usa busqueda binaria
            //si no, busqueda lineal
            ArrayList<Game> Reslt = new ArrayList<>();
            if(OrdenadoPrice()){//busqueda binaria
                int left = 0;
                int right = Data.size() - 1;
                while (left <= right) {
                    int mid = left + (right - left) / 2;
                    if (Data.get(mid).getPrice() < lowerPrice) {
                        left = mid + 1;
                    } else {
                        right = mid - 1;
                    }
                }
                int Ini=left;
                left = 0;
                right = Data.size() - 1;
                while (left <= right) {
                    int mid = left + (right - left) / 2;
                    if (Data.get(mid).getPrice() <= higherPrice) {
                        left = mid + 1;
                    } else {
                        right = mid - 1;
                    }
                }
                int Fn = right;
                for (int i = Ini; i <= Fn && i < Data.size(); i++) {
                    Game gm = Data.get(i);
                    if (gm.getPrice() >= lowerPrice && gm.getPrice() <= higherPrice) {
                        Reslt.add(gm);
                    }
                }
            }
            else{
                for (Game gm : Data) {
                    if (gm.getPrice() >= lowerPrice && gm.getPrice() <= higherPrice) {
                        Reslt.add(gm);
                    }
                }
            }
            return Reslt;
        }

        public ArrayList<Game> getGamesByCategory(String Category) {
            //retorna los juegos de la misma categoria
            ArrayList<Game> Reslt = new ArrayList<>();
            for (Game gm : Data) {
                if (gm.getCategory().equals(Category)) {
                    Reslt.add(gm);
                }
            }
            return Reslt;
        }

        public ArrayList<Game> getGamesByQuality(int quality) {
            //retorna los juegos con mismo nivel de calidad
            //si el dataset esta ordenado por calidad, se usa busqueda binaria
            //si no, busqueda lineal
            ArrayList<Game> Reslt = new ArrayList<>();
            if(OrdenadoQuality()){
                int left=0;
                int right=Data.size()-1;
                while(left<=right){
                    int mid=(left+right)/2;
                    Game gm=Data.get(mid);
                    if(gm.getQuality()==quality){
                        int temp = mid;
                        while (temp >= 0 && Data.get(temp).getQuality() == quality) {
                            Reslt.add(Data.get(temp));
                            temp--;
                        }
                        temp = mid + 1;
                        while (temp < Data.size() && Data.get(temp).getQuality() == quality) {
                            Reslt.add(Data.get(temp));
                            temp++;
                        }
                        break;
                    }
                    else if (gm.getQuality()<quality) {
                        left=mid+1;
                    }
                    else{
                        right=mid-1;
                    }
                }
            }
            else{
                for (Game gm : Data) {
                    if (gm.getQuality() == quality) {
                        Reslt.add(gm);
                    }
                }
            }
            return Reslt;
        }

        public void sortByAlgorithm(String algorithm, String attibute) {
            //El parametro algorithm puede tomar :
            //bubbleSort, insertionSort, SelectionSort, mergeSort, quickSort
            //si el valor no coincide con ninguno anterior, se usa Collections.sort()
            //El parametro attribute, determina por cual se ordena:
            //price, category, quiality
            //si no se reconoce, el ordenamiento base es price
            //actualizar sortedByAttribute
            sortedByAtrribute = attibute;
            Comparator<Game> comparator;

            switch (attibute.toLowerCase()) {
                case "price":
                    comparator = Comparator.comparing(Game::getPrice);
                    break;
                case "quality":
                    comparator = Comparator.comparing(Game::getQuality);
                    break;
                case "category":
                    comparator = Comparator.comparing(Game::getCategory);
                    break;
                default:
                    comparator = Comparator.comparing(Game::getPrice);
                    break;
            }
            switch (algorithm.toLowerCase()) {
                case "bublesort":
                    bubbleSort(comparator);
                    break;
                case "insertionsort":
                    insertionSort(comparator);
                    break;
                case "selectionsort":
                    selectionSort(comparator);
                    break;
                case "mergesort":
                    mergeSort(0, Data.size() - 1, comparator);
                    break;
                case "quicksort":
                    quickSort(0, Data.size() - 1, comparator);
                    break;
                default:
                    Collections.sort(Data, comparator);
                    break;
            }
        }

        private void bubbleSort(Comparator<Game> Comp) {
            int n = Data.size();
            for (int i = 0; i < n - 1; i++) {
                for (int j = 0; j < n-i-1; j++) {
                    if (Comp.compare(Data.get(j), Data.get(j+1)) > 0) {
                        Collections.swap(Data, j, j+1);
                    }
                }
            }
        }

        private void insertionSort(Comparator<Game> Comp) {
            int n = Data.size();
            for (int i = 1; i < n; i++) { // ya que el primer elemento, se considera ordenado
                Game key = Data.get(i);
                int j = i - 1;
                while (j >= 0 && Comp.compare(key, Data.get(j)) < 0) {
                    Data.set(j + 1, Data.get(j));
                    j--;
                }
                Data.set(j + 1, key);
            }
        }

        private void selectionSort(Comparator<Game> Comp) {
            int n = Data.size();
            for (int i = 0; i < n - 1; i++) {
                int min = i;
                for (int j = i + 1; j < n; j++) {
                    if (Comp.compare(Data.get(j), Data.get(min)) < 0) {
                        min = j;
                    }
                }
                if(min !=i){
                    Collections.swap(Data, i, min);
                }
            }
        }

        private void mergeSort(int left, int right, Comparator<Game> comparator) {
            if (left < right) {
                int mid = (left + right) / 2;
                mergeSort(left, mid, comparator);
                mergeSort(mid + 1, right, comparator);
                merge(left, mid, right, comparator);
            }
        }

        private void merge(int left, int mid, int right, Comparator<Game> comparator) {
            List<Game> temp = new ArrayList<>();
            int i = left, j = mid + 1;

            while (i <= mid && j <= right) {
                if (comparator.compare(Data.get(i), Data.get(j)) <= 0) {
                    temp.add(Data.get(i++));
                } else {
                    temp.add(Data.get(j++));
                }
            }

            while (i <= mid) temp.add(Data.get(i++));
            while (j <= right) temp.add(Data.get(j++));

            for (int k = 0; k < temp.size(); k++) {
                Data.set(left + k, temp.get(k));
            }
        }

        private void quickSort(int bj, int alt, Comparator<Game> Comp) {
            if (bj < alt) {
                int pi = partition(bj, alt, Comp);
                quickSort(bj, pi - 1, Comp);
                quickSort(pi + 1, alt, Comp);
            }
        }

        private int partition(int bj, int alt, Comparator<Game> Comp) {
            Game pivot = Data.get(alt);
            int i = bj - 1;
            for (int j = bj; j < alt; j++) {
                if (Comp.compare(Data.get(j), pivot) <= 0) {
                    i++;
                    Collections.swap(Data, i, j);
                }
            }
            Collections.swap(Data, i + 1, alt);
            return i + 1;
        }

        public static void DatasetMain() {
            //entrega una arraylist con todos los juegos cuyo precio sea igual
            //al parametro entregado
            //si el dataset esta ordenado por precio, se usa busqueda binaria
            //si no, busqueda lineal
            Scanner sc = new Scanner(System.in);
            Dataset dt = new Dataset(Game.games);
            System.out.println("ingrese por que atributo desa ordenar:(price/Category/quality) ");
            String atribute = sc.nextLine().toLowerCase();

            System.out.println("ingrese por que algoritmo desa ordenar:(bubbleSort, insertionSort, SelectionSort, mergeSort, quickSort) ");
            String algorithm = sc.nextLine();

            dt.sortByAlgorithm(algorithm, atribute);
            System.out.println("juegos ordenados:");

            for (Game g : Game.games) {
                System.out.println(g);
            }

            System.out.println("Desea buscar por alguna especialidad?");
            System.out.println("1) Por Precio ");
            System.out.println("2) Rango de precio");
            System.out.println("3) Categoria ");
            System.out.println("4) Calidad");
            System.out.println("5) Salir");
            int option = sc.nextInt();
            ArrayList<Game> Rest = new ArrayList<>();
            switch (option) {
                case 1:
                    System.out.println("Resultado de orden por precio: ");
                    dt.sortByAlgorithm("price", "mergeSort");
                    Rest = dt.getGamesByPriceRange(0, Integer.MAX_VALUE);
                    break;
                case 2:
                    System.out.println("Ingrese el precio minimo: ");
                    int min = sc.nextInt();
                    System.out.println("Ingrese el precio maximo: ");
                    int max = sc.nextInt();
                    Rest = dt.getGamesByPriceRange(min, max);
                    break;
                case 3:
                    System.out.println("Ingrese La categoria: ");
                    String categoria = sc.next();
                    Rest = dt.getGamesByCategory(categoria);
                    break;
                case 4:
                    System.out.println("Ingrese la Calidad: ");
                    int calidad = sc.nextInt();
                    Rest = dt.getGamesByQuality(calidad);
                    break;
                case 5:
                    System.out.println("Saliendo: ");
                    break;
                default:
                    System.out.println("Opcion no permitida");
                    break;
            }
            System.out.println("Resultado de la busqueda: ");
            for (Game g : Rest) {
                System.out.println(g);
            }

        }

        public ArrayList<Game> getData() {
            return Game.games;
        }

    }

    public static class MainSearchTime{
        public static void UserSearchTime(Dataset dataset) {
            Scanner sc = new Scanner(System.in);
            System.out.println("------------ Medición de tiempos de búsqueda ------------");
            System.out.print("Ingrese precio para buscar: ");
            int pc = sc.nextInt();
            System.out.println("calculando timepo en getGamesByPrice ");

            long startTime = System.nanoTime();
            dataset.getGamesByPrice(pc);
            long endTime = System.nanoTime();
            long linearTimePrice = (endTime - startTime) ;

            System.out.print("Ingrese precio mínimo para rango: ");
            int lowerPc = sc.nextInt();
            System.out.print("Ingrese precio máximo para rango: ");
            int higherPc = sc.nextInt();
            System.out.println("calculando timepo en getGamesBypriceRange ");

            startTime = System.nanoTime();
            dataset.getGamesByPriceRange(lowerPc, higherPc);
            endTime = System.nanoTime();
            long linearTimePriceRange = (endTime - startTime) ;

            dataset.sortByAlgorithm("mergesort", "price");//ordena para hacer busqueda binaria
            System.out.println("calculando timepo en getGamesByPrice Binaria");
            startTime = System.nanoTime();
            dataset.getGamesByPrice(pc);
            endTime = System.nanoTime();
            long binaryTimePrice = (endTime - startTime) ;

            System.out.println("calculando timepo en getGamesByPriceRange Binaria");
            startTime = System.nanoTime();
            dataset.getGamesByPriceRange(lowerPc, higherPc);
            endTime = System.nanoTime();
            long binaryTimePriceRange = (endTime - startTime) ;

            System.out.print("Ingrese categoría para búsqueda: ");
            sc.nextLine(); // limpiar buffer
            String category = sc.nextLine();

            startTime = System.nanoTime();
            dataset.getGamesByCategory(category);
            endTime = System.nanoTime();
            long linearTimeCategory = (endTime - startTime) ;

            dataset.sortByAlgorithm("mergesort", "category");

            startTime = System.nanoTime();
            dataset.getGamesByCategory(category);
            endTime = System.nanoTime();
            long BinarTimeCategory = (endTime - startTime);

            System.out.print("Ingrese calidad para búsqueda: ");
            int quality = sc.nextInt();
            System.out.println("calculando timepo en getGamesByQuality Lineal");
            startTime = System.nanoTime();
            dataset.getGamesByQuality(quality);
            endTime = System.nanoTime();
            long linearTimeQuality = (endTime - startTime) ;

            dataset.sortByAlgorithm("mergesort", "quality");
            System.out.println("calculando timepo en getGamesByQuality Binaria");
            startTime = System.nanoTime();
            dataset.getGamesByQuality(quality);
            endTime = System.nanoTime();
            long binaryTimeQuality = (endTime - startTime) ;

            System.out.println("\nCuadro 2: Tiempos de ejecución de búsqueda");
            System.out.println("Método\t\t\tAlgoritmo\t\tTiempo (milisegundos)");
            System.out.printf("getGamesByPrice\t\tlinearSearch\t\t%d\n", linearTimePrice);
            System.out.printf("getGamesByPrice\t\tbinarySearch\t\t%d\n", binaryTimePrice);
            System.out.printf("getGamesByPriceRange\tlinearSearch\t\t%d\n", linearTimePriceRange);
            System.out.printf("getGamesByPriceRange\tbinarySearch\t\t%d\n", binaryTimePriceRange);
            System.out.printf("getGamesByCategory\tlinearSearch\t\t%d\n", linearTimeCategory);
            System.out.printf("getGamesByCategory\tbinarySearch\t\t%s\n", BinarTimeCategory);
            System.out.printf("getGamesByQuality\tlinearSearch\t\t%d\n", linearTimeQuality);
            System.out.printf("getGamesByQuality\tbinarySearch\t\t%d\n", binaryTimeQuality);
        }
    }

    private static void saveData(ArrayList<Game> data, String fileName ) {
        FileWriter fw = null;
        try {
            fw = new FileWriter(fileName);
            for (Game game : data) {
                fw.write(game.getName() + "," + game.getCategory() + "," + game.getPrice() + "," + game.getQuality() + "\n");
            }
            System.out.println("Datos guardados en: " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fw != null) {
                try {
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void Leer(String NameFile) throws FileNotFoundException {
        Game.games.clear();
        System.gc();
        try {
            File Fl = new File(NameFile + ".txt");
            Scanner FR = new Scanner(Fl);
            while (FR.hasNextLine()) {
                String dt = FR.nextLine();
                String[] d = dt.split(",");
                if (d.length >= 4) {
                    String Nm = d[0].trim();
                    String ct = d[1].trim();
                    int pc = Integer.parseInt(d[2].trim());
                    int q = Integer.parseInt(d[3].trim());
                    Game games = new Game(Nm, ct, pc, q);
                    Game.games.add(games);
                }
            }
            FR.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error: Fichero no encontrado");
            e.printStackTrace();
        }
    }

    public static void RunBenchmarksGenerate(int datasetSize) {
        String[] algorithms = {"bublesort", "insertionsort", "selectionsort", "mergesort", "quicksort", "default"};
        String[] attributes = {"price", "quality", "category"};
        GenerateData gd = new GenerateData();
        StringBuilder Result = new StringBuilder();
        Result.append("Resultado de Benchmarks Generados: \n");

        // Generar juegos de ejemplo
        ArrayList<Game> originalGames = new ArrayList<>();
        for (int i = 0; i < datasetSize; i++) {
            originalGames.add(gd.genereteGame());
        }

        System.out.println("Benchmark de Ordenamientos sobre " + datasetSize + " elementos:");
        for (String attribute : attributes) {
            System.out.println("\n--- Atributo: " + attribute + " ---");
            for (String algorithm : algorithms) {
                ArrayList<Game> dataCopy = new ArrayList<>(originalGames);

                Dataset ds = new Dataset(dataCopy);
                long ini = System.nanoTime();
                if (algorithm.equals("default")) {
                    ds.sortByAlgorithm("none", attribute);
                } else {
                    ds.sortByAlgorithm(algorithm, attribute);
                }
                long fin = System.nanoTime();
                long durationMs = (fin - ini) / 1_000_000;
                Result.append("Algoritmo: ").append(algorithm)
                        .append(", Atributo: ").append(attribute)
                        .append(" => Tiempo: ").append(durationMs).append(" ms\n");

                System.out.println("Algoritmo: " + algorithm + " → Tiempo: " + durationMs + " ms");
            }
        }
        GuardarBenchmarks(String.valueOf(Result), "Benchmarks-Generados.txt");
        System.out.println("Resultados guardados en: Benchmarks-Leidos.txt");
    }

    public static void RunBenchmarksRen(String Flname) {
        String[] algorithms = {"bublesort", "insertionsort", "selectionsort", "mergesort", "quicksort", "default"};
        String[] attributes = {"price", "quality", "category"};
        StringBuilder Result = new StringBuilder();
        Result.append("Benchmarks de ordenamiento sobre Datos leídos:\n");

        // Verificamos si hay datos
        if (Game.games.isEmpty()) {
            System.out.println("No hay datos cargados desde archivo para ejecutar benchmarks.");
            return;
        }

        System.out.println("Benchmark de Ordenamientos sobre " + Game.games.size() + " elementos leídos del archivo:");

        for (String algorithm : algorithms) {
            System.out.println("\n=== Algoritmo: " + algorithm + " ===");

            for (String attribute : attributes) {
                // Crear copia fresca de los datos originales cada vez
                ArrayList<Game> dataCopy = new ArrayList<>();
                for (Game g : Game.games) {
                    dataCopy.add(new Game(g.getName(), g.getCategory(), g.getPrice(), g.getQuality()));
                }

                Dataset ds = new Dataset(dataCopy);
                long start = System.nanoTime();
                if (algorithm.equals("default")) {
                    ds.sortByAlgorithm("none", attribute);
                } else {
                    ds.sortByAlgorithm(algorithm, attribute);
                }
                long end = System.nanoTime();
                long duration = (end - start)/100000;

                Result.append("Algoritmo: ").append(algorithm)
                        .append(", Atributo: ").append(attribute)
                        .append(" => Tiempo: ").append(duration).append(" ms\n");
                System.out.println(duration);
                if(duration>=10){
                    System.out.println("Atributo: " + attribute + " → Tiempo: " +duration/10 + " ms");
                }
                else{
                    System.out.println("Atributo: " + attribute + " → Tiempo: " +"'0."+duration + " ms");
                }
            }
        }

        GuardarBenchmarks(Result.toString(), Flname + "-Benchmarks-Leidos.txt");
        System.out.println("Resultados guardados en: " + Flname + "-Benchmarks-Leidos.txt");
    }

    public static void GuardarBenchmarks(String cont, String Flname) {
        try (FileWriter writer = new FileWriter(Flname, true)) { // `true` para agregar, no sobrescribir
            writer.write(cont);
            writer.write("\n\n");
        } catch (IOException e) {
            System.out.println("Error al guardar los resultados del benchmark.");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        //reglas:
        //name:
        //Puede generarse conbinando al azar palabras desde un arreglo
        //relacionado con juegos, nombres de fantasia o titulos inventados
        // el nombre puede estar formado por 2 palabras al azar y conectandolas
        Random rd = new Random();
        Scanner sc = new Scanner(System.in);
        Dataset dt = new Dataset(Game.games);
        boolean n = true;
        while (n) {
            System.out.println("Seleccione una opcion:");
            System.out.println("1) Leer archivo:");
            System.out.println("2) Generar nuevos datos:");
            System.out.println("3) Correr Brenchmarks de rendimiento:");
            System.out.println("4) Medir timpos de busqueda");
            System.out.println("5) Salir del programa:");
            int opcion = sc.nextInt();
            switch (opcion) {
                case 1:
                    Game.games.clear();
                    System.gc();
                    System.out.println("Ingrese la nombre del archivo: ");
                    String Flname = sc.next();
                    Leer(Flname);
                    System.out.println("Desea ejecutar benchmarks(s/n): ");
                    String bn = sc.next().toLowerCase();
                    if (bn.equalsIgnoreCase("s")) {
                        RunBenchmarksRen(Flname);
                    }
                    break;
                case 2:
                    Game.games.clear();
                    System.gc();
                    System.out.println("Ingrese cuantos Datos desea obtener: ");
                    int op = sc.nextInt();
                    Game.GameMain(op);
                    System.out.println("Desea hacer una busqueda sobre los Datos? (s/n): ");
                    String bs = sc.next().toLowerCase();
                    if (bs.equalsIgnoreCase("s")) {
                        Dataset.DatasetMain();
                    }
                    ArrayList<Game> dataNum = dt.getData();
                    System.out.println("Ingrese el nombre del File en dnd lo desa guardar: ");
                    String name = sc.next();
                    saveData(dataNum, name + ".txt");
                    break;
                case 3:
                    System.out.println("Cuantos datos desaa para medir el rendimiento?: ");
                    int dtct = sc.nextInt();
                    RunBenchmarksGenerate(dtct);
                    break;
                case 4:
                    MainSearchTime.UserSearchTime(dt);
                    break;
                case 5:
                    n = false;
                    break;
                default:
                    System.out.println("Opcion no permitida");
                    break;

            }
        }
    }
}
