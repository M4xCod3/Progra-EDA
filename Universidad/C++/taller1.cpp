#include <iostream>
using namespace std;
//2 clases
class RobotA{
    private:
        string name;
        int heald;
        int atack;
    public:
        RobotA(string nombre, int ataque, int salud){
            name=nombre;
            atack=ataque;
            heald=salud;
        }
        int atacar(){
            return atack;
        }
        int MenosSalud(int danio){
            heald=heald-danio;
            return heald;
        }
        void imprimir(){
            cout<<"nombre: "<<name<<endl;
            cout<<"salud: "<<heald<<endl;
            cout<<"ataque: "<<atack<<endl;
        }
        int salud(){
            return heald;
        }
};
class RobotB{
     private:
        string name;
        int heald;
        int atack;
    public:
        RobotB(string nombre, int ataque, int salud){
            name=nombre;
            atack=ataque;
            heald=salud;
        }
        int atacar(){
            return atack;
        }
        int MenosSalud(int danio){
            heald=heald-danio;
            return heald;
        }
        void imprimir(){
            cout<<"nombre: "<<name<<endl;
            cout<<"salud: "<<heald<<endl;
            cout<<"ataque: "<<atack<<endl;
        }
        int salud(){
            return heald;
        }
};

int main(){
    string nameA, nameB;
    int saludA, saludB, danioA, danioB,turno=1;
    cout<<"ingrese el nombre del Robot A: ";
    cin>>nameA;
    cout<<"ingrese la salud del Robot A: ";
    cin>>saludA;
    cout<<"ingrese el daño de ataque del RobotA: ";
    cin>>danioA;
    cout<<"ingrese el nombre del Robot B: ";
    cin>>nameB;
    cout<<"ingrese la salud del Robot B: ";
    cin>>saludB;
    cout<<"ingrese el daño de ataque del RobotB: ";
    cin>>danioB;
    RobotA*A=new RobotA(nameA, danioA, saludA);
    RobotB*B=new RobotB(nameB, danioB, saludB);
    //A->imprimir();
    //B->imprimir();
    cout<<"parte atacando el RobotA"<<endl;
    while(true){
        cout<<"turno "<<turno<<endl;
        B->MenosSalud(A->atacar());
        if(B->salud()==0){
            cout<<"gana el robotA "<<endl;
            A->imprimir();
            break;
        }
        A->MenosSalud(B->atacar());
        if(A->salud()==0){
            cout<<"gana el RobotB"<<endl;
            B->imprimir();
            break;
        }
        B->imprimir();
        A->imprimir();
        turno++;
    }
}
    
