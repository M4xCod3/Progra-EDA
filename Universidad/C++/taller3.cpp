#include <iostream>
#include <vector>
#include <string>

using namespace std;

class Animal{
	protected:
		string nombre;
		int edad;
	public:
		Animal(string nombre, int edad){
			this->nombre=nombre;
			this->edad=edad;
		}
		virtual void mostrar()=0;
};

class Anfibio: virtual public Animal{
	protected:
		bool puedeRespirarBajoAgua;
	public:
		Anfibio(string nombre, int edad, bool agua):Animal(nombre,edad){
			this->nombre=nombre;
			this->edad=edad;
			this->puedeRespirarBajoAgua=agua;
		}
		void mostrar(){
			cout<<"Nombre: "<<nombre<<", edad: "<<edad<<endl;
			cout<<" puede respirar bajo el agua: "<<(puedeRespirarBajoAgua? "si":"no")<<endl;
		}
};

class Mamifero: virtual public Animal{
	protected:
		bool tienePelo;
	public:
		Mamifero(string nombre, int edad, bool tienePelo):Animal(nombre,edad){
			this->nombre=nombre;
			this->edad=edad;
			this->tienePelo=tienePelo;
		}
		void motrar(){
			cout<<"Nombre: "<<nombre<<", edad: "<<edad<<endl;
			cout<<"Tiene pelo: "<<(tienePelo? "si":"no")<<endl;
		}
};

class Hibrido: virtual public Mamifero, public Anfibio{
	protected:
		bool puedeVolar;
	public:
		Hibrido(string nombre, int edad, bool puedeVolar, bool tienePelo, bool agua):Animal(nombre,edad), Anfibio(nombre,edad,agua), Mamifero(nombre,edad, tienePelo){
			this->nombre=nombre;
			this->edad=edad;
			this->puedeVolar=puedeVolar;
			this->tienePelo=tienePelo;
			this->puedeRespirarBajoAgua=agua;
		}
		void mostrar(){
			cout<<"Hibrido"<<endl;
			cout<<"Nombre: "<<nombre<<", edad: "<<edad<<endl;
			cout<<" Agua: "<<(puedeRespirarBajoAgua? "si":"no")<<endl;
			cout<<" Pelo: "<<(tienePelo?"si":"no")<<endl;
			cout<<" Vuela: "<<(puedeVolar?"si":"no")<<endl;
		}
};

int main(){
	vector<Hibrido*> catalogo;
	int pos=0;
	
	while(pos!=3){
		cout<<"lab Darwin futuro"<<endl;
		cout<<"1.- añadir especie hibrida"<<endl;
		cout<<"2.- mostrar especies"<<endl;
		cout<<"3.- salir"<<endl;
		cin>>pos;
		
		switch(pos){
			case 1:{
				string n;
				int e;
				bool ag, pe, vo;
				cout<<"Ingrese el nombre, la edad: ";
				cin>>n>>e;
				cout<<"Puede respirar bajo el agua, tiene pelo, puede volar (1.si/ 0.no): ";
				cin>>ag>>pe>>vo;
				catalogo.push_back(new Hibrido(n,e,vo,pe,ag));
				cout<<"Especia añadidad correctamente"<<endl;
				break;
			}
			case 2:{
				if(catalogo.empty()){
					cout<<"el catalogo esta vacio"<<endl;
				}
				else{
					for(int i=0; i<catalogo.size();i++){
						catalogo[i]->mostrar();
					}
				}
				break;
			}
			case 3:{
				cout<<"saliendo del lab"<<endl;
				break;
			default:
				cout<<"opcion no valida"<<endl;
				break;
			}
		}	
	}
	for(int i=0; i<catalogo.size();i++){
		delete catalogo[i];
	}
	return 0;
}
