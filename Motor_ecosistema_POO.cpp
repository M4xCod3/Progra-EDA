#include <iostream>
#include <vector>
#include <cmath>
#include <cstdlib>
#include <ctime>
#include <typeinfo>
#include <windows.h>

using namespace std;

// =======================
// CLASE ENTIDAD
// =======================
class Entidad {
protected:
    int x, y;
    bool viva;

public:
    Entidad(int x, int y) : x(x), y(y), viva(true) {}
    virtual ~Entidad() {}

    virtual void actualizar() = 0;

    int getX() const { return x; }
    int getY() const { return y; }
    bool estaViva() const { return viva; }
    void morir() { viva = false; }
};

// =======================
// CLASE PLANTA
// =======================
class Planta : public Entidad {
public:
    Planta(int x, int y) : Entidad(x, y) {}

    void actualizar() {
        //cout << "?? Planta (" << x << "," << y << ")\n";
    }
};

// =======================
// CLASE ANIMAL
// =======================
class Animal : public Entidad {
protected:
    int energia;

public:
    Animal(int x, int y) : Entidad(x, y), energia(100) {}

    virtual bool puedeComer(Entidad* e) = 0;

    void moverHacia(int tx, int ty) {
        if (x < tx) x++;
        else if (x > tx) x--;
        if (y < ty) y++;
        else if (y > ty) y--;
    }
    
    void moverAleatorio() {
	    int dx = (rand() % 3) - 1; // -1, 0, 1
	    int dy = (rand() % 3) - 1;
	    x += dx;
	    y += dy;
	}

	bool puedeReproducirse() const {
	    return energia >= 30;
	}
	
	void gastarEnergia(int e) {
	    energia -= e;
	}

    void actualizar() {
        energia--;
        if (energia <= 0) {
            //cout << "?? Animal muere en (" << x << "," << y << ")\n";
            morir();
        }
    }
    void limitar(int ancho, int alto) {
	    if (x < 0) x = 0;
	    if (y < 0) y = 0;
	    if (x >= ancho) x = ancho - 1;
	    if (y >= alto) y = alto - 1;
	}


    int getEnergia() const { return energia; }
    void ganarEnergia(int e) { energia += e; }
};

// =======================
// CLASE HERBIVORO
// =======================
class Herbivoro : public Animal {
public:
    Herbivoro(int x, int y) : Animal(x, y) {}

    bool puedeComer(Entidad* e) {
        return dynamic_cast<Planta*>(e) != NULL;
    }

    void actualizar() {
        Animal::actualizar();
        if (estaViva()){
            //cout << "?? Herbivoro (" << x << "," << y << ") E=" << energia << "\n";
    	}
    }
};

// =======================
// CLASE CARNIVORO
// =======================
class Carnivoro : public Animal {
public:
    Carnivoro(int x, int y) : Animal(x, y) {}

    bool puedeComer(Entidad* e) {
        return dynamic_cast<Herbivoro*>(e) != NULL;
    }

    void actualizar() {
        Animal::actualizar();
        if (estaViva()){
            //cout << "?? Carnivoro (" << x << "," << y << ") E=" << energia << "\n";
		}
    }
};

// =======================
// CLASE MUNDO
// =======================
class Mundo {
private:
    int ancho, alto;
    vector<Entidad*> entidades;

public:
    Mundo(int w, int h) : ancho(w), alto(h) {}

    void agregarEntidad(Entidad* e) {
        entidades.push_back(e);
    }

    void actualizar() {
        for (size_t i = 0; i < entidades.size(); i++) {
            if (entidades[i]->estaViva())
                entidades[i]->actualizar();
        }
    }

    Entidad* buscarComida(Animal* animal, int rango) {
        Entidad* objetivo = NULL;
        int minDist = 9999;

        for (size_t i = 0; i < entidades.size(); i++) {
            Entidad* e = entidades[i];
            if (e != animal && e->estaViva() && animal->puedeComer(e)) {
                int dist = abs(animal->getX() - e->getX()) +
                           abs(animal->getY() - e->getY());
                if (dist < minDist && dist <= rango) {
                    minDist = dist;
                    objetivo = e;
                }
            }
        }
        return objetivo;
    }

    void interacciones() {
        for (size_t i = 0; i < entidades.size(); i++) {
            Animal* a = dynamic_cast<Animal*>(entidades[i]);
            if (!a || !a->estaViva()) continue;

            Entidad* comida = buscarComida(a, 10);
            if (comida) {
			    a->moverHacia(comida->getX(), comida->getY());
			    if (a->getX() == comida->getX() &&
			        a->getY() == comida->getY()) {
			        //cout << "??? Come en (" << a->getX() << "," << a->getY() << ")\n";
			        comida->morir();
			        a->ganarEnergia(10);
			    }
			} else {
			    a->moverAleatorio();  // ?? CLAVE
			}
			a->limitar(ancho, alto);
        }
    }
    
    void aparicionAleatoria() {
	    int prob = rand() % 100;
	
	    // 20% de probabilidad de nueva planta
	    if (prob < 20) {
	        int x = rand() % ancho;
	        int y = rand() % alto;
	        entidades.push_back(new Planta(x, y));
	        //cout << "?? Aparece planta en (" << x << "," << y << ")\n";
	    }
	}
	
	
	Animal* buscarPareja(Animal* a) {
	    for (size_t i = 0; i < entidades.size(); i++) {
	        Animal* otro = dynamic_cast<Animal*>(entidades[i]);
	        if (!otro || otro == a) continue;
	
	        if (!otro->estaViva()) continue;
	
	        // Misma especie
	        if (typeid(*a) != typeid(*otro)) continue;
	
	        // Misma posición
	        int dx = abs(a->getX() - otro->getX());
			int dy = abs(a->getY() - otro->getY());
			
			if (dx <= 1 && dy <= 1) {
			    return otro;
			}
	    }
	    return NULL;
	}

	void reproduccion() {
	    size_t sizeActual = entidades.size();
	
	    for (size_t i = 0; i < sizeActual; i++) {
	        Animal* a = dynamic_cast<Animal*>(entidades[i]);
	        if (!a || !a->estaViva()) continue;
	
	        if (!a->puedeReproducirse()) continue;
	
	        Animal* pareja = buscarPareja(a);
	        if (!pareja) continue;
	
	        int prob = rand() % 100;
	
	        // 20% de probabilidad
	        if (prob < 20) {
	            int nx = a->getX() + (rand() % 3 - 1);
	            int ny = a->getY() + (rand() % 3 - 1);
	
	            if (dynamic_cast<Herbivoro*>(a)) {
	                entidades.push_back(new Herbivoro(nx, ny));
	                //cout << "???? Nace herbivoro en (" << nx << "," << ny << ")\n";
	            }
	            else if (dynamic_cast<Carnivoro*>(a)) {
	                entidades.push_back(new Carnivoro(nx, ny));
	                //cout << "???? Nace carnivoro en (" << nx << "," << ny << ")\n";
	            }
	
	            // Costo energético para ambos
	            a->gastarEnergia(15);
	            pareja->gastarEnergia(15);
	        }
	    }
	}


    bool hayAnimalesVivos() {
	    for (size_t i = 0; i < entidades.size(); i++) {
	        if (entidades[i]->estaViva()) {
	            if (dynamic_cast<Animal*>(entidades[i]) != NULL)
	                return true;
	        }
	    }
	    return false;
	}

    bool hayVida() {
	    for (size_t i = 0; i < entidades.size(); i++) {
	        if (entidades[i]->estaViva())
	            return true;
	    }
	    return false;
	}
	
	void mostrar() {
	    vector<vector<char> > vista(alto, vector<char>(ancho, '.'));
	
	    for (size_t i = 0; i < entidades.size(); i++) {
	        if (!entidades[i]->estaViva()) continue;
	
	        int x = entidades[i]->getX();
	        int y = entidades[i]->getY();
	
	        if (dynamic_cast<Planta*>(entidades[i]))
	            vista[y][x] = 'P';
	        else if (dynamic_cast<Herbivoro*>(entidades[i]))
	            vista[y][x] = 'H';
	        else if (dynamic_cast<Carnivoro*>(entidades[i]))
	            vista[y][x] = 'C';
	    }
	
	    system("cls"); // windows
	    for (int y = 0; y < alto; y++) {
	        for (int x = 0; x < ancho; x++)
	            cout << vista[y][x] << ' ';
	        cout << '\n';
	    }
	    int p=0,h=0,c=0;
		for (size_t i = 0; i < entidades.size(); i++) {
		    if (!entidades[i]->estaViva()) continue;
		    if (dynamic_cast<Planta*>(entidades[i])) p++;
		    else if (dynamic_cast<Herbivoro*>(entidades[i])) h++;
		    else if (dynamic_cast<Carnivoro*>(entidades[i])) c++;
		}
		
		cout << "\nPlantas: " << p
		     << " Herbivoros: " << h
		     << " Carnivoros: " << c << "\n";
		
	}

    ~Mundo() {
        for (size_t i = 0; i < entidades.size(); i++)
            delete entidades[i];
    }
};

// =======================
// MAIN
// =======================
int main() {
	srand(time(NULL));

    Mundo mundo(50, 20);

    mundo.agregarEntidad(new Planta(10, 10));
    mundo.agregarEntidad(new Planta(19, 19));
    mundo.agregarEntidad(new Herbivoro(44, 5));
    mundo.agregarEntidad(new Herbivoro(40,19));
    mundo.agregarEntidad(new Carnivoro(0, 0));

    int turno = 1;
	while (mundo.hayVida() && mundo.hayAnimalesVivos()) {
    	mundo.interacciones();
	    mundo.reproduccion();
	    mundo.aparicionAleatoria();
	    mundo.actualizar();
	    mundo.mostrar();
	    Sleep(300);
	}

    return 0;
}

