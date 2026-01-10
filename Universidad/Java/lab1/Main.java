import java.io.*;
import java.util.*;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Map;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main{

    static class Voto{
        private int Id;
        private int VotId;
        private int CandId;
        private String Tst;

        public Voto(int Id, int VotId, int CandId, String Tst){
            this.Id = Id;
            this.VotId=VotId;
            this.CandId=CandId;
            this.Tst=ObtTime();
        }
        private String ObtTime(){
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
            return LocalDateTime.now().format(dtf).toString();
        }
        public int getId(){return Id;}
        public int getVotId(){return VotId;}
        public int getCandId(){return CandId;}
        public String getTst(){return Tst;}

        public void setId(int Id){
            this.Id=Id;
        }
        public void setVotId(int VotId){
            this.VotId=VotId;
        }
        public void setCantId(int CandId){
            this.CandId=CandId;
        }
        public void setTst(String Tst){
            this.Tst=Tst;
        }
    }

    static class Candidato{
        private int Id;
        private String name;
        private String partido;
        private Queue<Voto> VotoRes;

        public Candidato(int Id, String name, String partido){
            this.Id=Id;
            this.name=name;
            this.partido=partido;
            this.VotoRes= new LinkedList<>();
        }
        public int getId(){return Id;}
        public String getname(){return name;}
        public String getpartido(){return partido;}
        public Queue<Voto> getVotoRes(){return VotoRes;}

        public void setId(int Id){
            this.Id=Id;
        }
        public void setname(String name){
            this.name=name;
        }
        public void setpartido(String partido){
            this.partido=partido;
        }
        public void agregarVoto(Voto v){
            VotoRes.add(v);
        }
    }

    static class Votante{
        private int Id;
        private String name;
        private boolean listo;

        public Votante(int Id, String name){
            this.Id=Id;
            this.name=name;
        }

        public int getId(){return Id;}
        public String getname(){return name;}
        public boolean getListo(){return listo;}

        public void setId(int Id){
            this.Id=Id;
        }
        public void setname(String name){
            this.name=name;
        }
        public void setListo(boolean listo){
            this.listo=listo;
        }

        public void marcadoListo(){
            this.listo=true;
        }
    }

    static class UrnaElectoral{
        private LinkedList<Candidato> ListCand;
        private Stack<Voto> Historial;
        private Queue<Voto> VReport;
        private int Idcant;

        public UrnaElectoral(){
            ListCand=new LinkedList<>();
            Historial=new Stack<>();
            VReport=new LinkedList<>();
            Idcant=1;
        }

        public void AgregarCandidato(Candidato c){
            ListCand.add(c);
        }
        public boolean verificarVotante(Votante v){
            return !v.getListo();
        }
        public boolean registrarVoto(Votante Vot, int CandId){
            if(!verificarVotante(Vot)){
                return false;
            }
            Candidato cand = buscarCandidato(CandId);
            if(cand ==null){
                return false;
            }
            String Tst=java.time.LocalTime.now().toString();
            Voto vt = new Voto(Idcant++, Vot.getId(), CandId, Tst);
            cand.agregarVoto(vt);
            Vot.marcadoListo();
            return false;
        }
        public boolean ReportVoto(Candidato Cand, int VotoId){
            Queue<Voto> cola= Cand.getVotoRes();

            for(Voto v: cola){
                if(v.getId()==VotoId){
                    Cand.agregarVoto(v);
                    cola.remove(v);
                    return true;
                }
            }
            return false;
        }
        public Map<String, Integer> ObtenerResultados(){
            Map<String,Integer> resultados = new HashMap<>();
            for(Candidato c: ListCand){
                resultados.put(c.getname(), c.getVotoRes().size());
            }
            return resultados;
        }
        private Candidato buscarCandidato(int Id){
            for(Candidato C : ListCand){
                if(C.getId()==Id){
                    return C;
                }
            }
            return null;
        }
    }

    public static void main(String[] args){
        UrnaElectoral urna= new UrnaElectoral();
        Scanner Sc = new Scanner(System.in);

        int fin;
        do{
            System.out.println("ingrese el Id del candidato: ");
            int Id=Sc.nextInt();
            Sc.nextLine();
            System.out.println("ingrese el nombre del candidato: ");
            String name=Sc.nextLine();
            System.out.println("ingrese el partido del candidato: ");
            String part=Sc.nextLine();
            Candidato cand = new Candidato(Id, name, part);
            urna.AgregarCandidato(cand);
            System.out.println("Desea finalizar el proceso de candidadtos?(1=si/2=no): ");
            fin=Sc.nextInt();
        }while(fin!=1);
        Sc.nextLine();
        do{
            System.out.println("Ingrese el Id del votante: ");
            int VotId=Sc.nextInt();
            Sc.nextLine();
            System.out.println("Ingrese el nombre del votante: ");
            String name=Sc.nextLine();
            Votante votante = new Votante(VotId, name);
            System.out.println("Ingrese el Id del candidato por quien desea votar: ");
            int Idcand= Sc.nextInt();
            Sc.nextLine();
            urna.registrarVoto(votante, Idcand);
            System.out.println("Desa finalizar la votacion?(1=si/2=no): ");
            fin=Sc.nextInt();
            Sc.nextLine();
        }while(fin!=1);
        System.out.println("Resultados: ");
        urna.ObtenerResultados().forEach((name,vot)-> System.out.println(name+ ": "+ vot+ " Votos"));


    }
}
