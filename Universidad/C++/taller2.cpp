#include <iostream>
#include <vector>
#include <map>
using namespace std;

class Payaso{
    private:
        string nombreArtisitco;
        string nombreReal;
        int edad;
    public:
        Payaso(string nombreArtisitco, string nombreReal, int edad){
            this->nombreArtisitco=nombreArtisitco;
            this->nombreReal=nombreReal;
            this->edad=edad;
        }
        string getNombreArtistico(){
            return nombreArtisitco;
        }
        string getNombreReal(){
            return nombreReal;
        }
        void getEdad(){
            cout<<"la edad es: "<<edad<<endl;
        }
        void getImprimir(){
            cout<<"el nombre artisitco es: "<<nombreArtisitco<<endl;
            cout<<"el nombre real es: "<<nombreReal<<endl;
            cout<<"la edad es: "<<edad<<endl;
        }
};
class Estacionamiento{
    private:
        map < string , vector<Payaso*> > AutosPorSeccion;
    public:
        void AgregarPayaso(string idAuto, string nombreArtisitco, string nombreReal, int edad){
            Payaso*n1=new Payaso(nombreArtisitco,nombreReal,edad);
            AutosPorSeccion[idAuto].push_back(n1);
        }
        void EliminarPayaso(string idAuto, string nombreArtisitco){
            for(int i=0;i<AutosPorSeccion[idAuto].size();i++){
                if(AutosPorSeccion[idAuto][i]->getNombreArtistico()==nombreArtisitco){
                    AutosPorSeccion.erase(idAuto);
                }
            }
        }
        Payaso BuscarPayaso(string idAuto, string nombreArtistico){
            for(int i=0;i<AutosPorSeccion[idAuto].size();i++){
                if(AutosPorSeccion[idAuto][i]->getNombreArtistico()==nombreArtistico){
                    AutosPorSeccion[idAuto][i]->getImprimir();
                }
            }
        }
        void imprimirAutos(){
            map<string, vector<Payaso*> >::iterator rec;
            for(rec=AutosPorSeccion.begin();rec!=AutosPorSeccion.end();rec++){
            	
                for(int i=0;i<rec->second.size();i++){
                    rec->second[i]->getImprimir();
                }
            }
        }
};
int main(){
    string nombreArtisitco, nombreReal, idAuto;
    int edad;
    cout<<"ingrese el nombreReal: ";cin>>nombreReal;
    cout<<"ingrese el nombreArtisitco: ";cin>>nombreArtisitco;
    cout<<"ingrese la edad: ";cin>>edad;
    cout<<"ingrese la ID del auto: "; cin>>idAuto;
    
}
