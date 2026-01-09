#include <iostream>
#include <string>

using namespace std;

class Robot {
private:
    string name;
    int health;
    int attack;

public:
    Robot(string nombre, int ataque, int salud) {
        name = nombre;
        attack = ataque;
        health = salud;
    }

    int getAtack() { return attack; }
    int getHealth() { return health; }
    string getName() { return name; }

    void recibirDanio(int danio) {
        health -= danio;
        if (health < 0) health = 0; // Evita salud negativa
    }

    void imprimir() {
        cout << "Robot: " << name << " | Salud: " << health << " | Ataque: " << attack << endl;
    }
};

int main() {
    string n1, n2;
    int s1, s2, a1, a2;

    // Entrada de datos simplificada
    cout << "Datos Robot A (Nombre Salud Ataque): ";
    cin >> n1 >> s1 >> a1;
    cout << "Datos Robot B (Nombre Salud Ataque): ";
    cin >> n2 >> s2 >> a2;

    Robot* A = new Robot(n1, a1, s1);
    Robot* B = new Robot(n2, a2, s2);

    int turno = 1;
    cout << "\n--- Inicio del Combate ---" << endl;

    while (A->getHealth() > 0 && B->getHealth() > 0) {
        cout << "\nTurno " << turno << ":" << endl;

        // Ataque de A a B
        B->recibirDanio(A->getAtack());
        cout << A->getName() << " ataca a " << B->getName() << endl;

        if (B->getHealth() <= 0) {
            cout << B->getName() << " ha sido destruido! Ganador: " << A->getName() << endl;
            break;
        }

        // Ataque de B a A
        A->recibirDanio(B->getAtack());
        cout << B->getName() << " ataca a " << A->getName() << endl;

        if (A->getHealth() <= 0) {
            cout << A->getName() << " ha sido destruido! Ganador: " << B->getName() << endl;
            break;
        }

        A->imprimir();
        B->imprimir();
        turno++;
    }

    // Liberar memoria
    delete A;
    delete B;

    return 0;
}
