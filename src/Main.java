package src;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main{
   
   //Comprobar si la entrada es correcta
   public static void main(String[] args) throws Exception {
      if (args.length < 2){
         System.out.println("Uso incorrecto:");
         System.out.println("Ejemplo de uso: <directorio> <-d/-l/-t>");
         System.exit(1);
      }
      System.setProperty("log4j.configurationFile", "lib/log4j2.xml");
      //Comprobar la opción marcada
      String opcion = args[1];

      //Barajar las distinas opciones que se puedan dar con un switch
      switch(opcion){
         //Tabla con nombre del dichero, tipo, codificación e idioma
         case "-d":
         case "-l":
            //Comprobar si la ruta corresponde con un directorio existente
            Path dir = Paths.get(args[0]);
            if (!dir.toFile().exists() || !dir.toFile().isDirectory()){
               System.out.println("El directorio no existe o no es un directorio");
               System.exit(1);
            }

            File dirFile = dir.toFile();
            File[] files = dirFile.listFiles();
            if (files != null) {
               if(opcion.equals("-d")){
                  System.out.println("\n  ╔═══════════════════════════════════════════════╗");
                  System.out.println("  ║                  Opción --d                   ║");
                  System.out.println("  ╚═══════════════════════════════════════════════╝");
                  System.out.println("   Generar una tabla para cada fichero con el nombre, tipo, codificación e idioma \n");
                        
                  for (File f : files) {
                     try{ CaseD.process(f);}
                     catch (Exception e) { System.err.println("Error al procesar el archivo " + f.getName() + ": " + e.getMessage());}
                  }
               }
               else if(opcion.equals("-l")){
         
                  System.out.println("\n  ╔═══════════════════════════════════════════════╗");
                  System.out.println("  ║                  Opción -l                    ║");
                  System.out.println("  ╚═══════════════════════════════════════════════╝");
                  System.out.println("   Extraer enlaces de los documentos \n");
                  
                  for (File f : files) {
                     try{ CaseL.process(f);}
                     catch (Exception e) { System.err.println("Error al procesar el archivo " + f.getName() + ": " + e.getMessage());}
                  }
               }
            }
         break;

         //Generar fichero (csv) con las ocurrencias de cada término (Exclusivo de Yerma.txt)
         case "-t":
            File f = new File(args[0]);
            if(!f.exists() || !f.isFile()){
               System.out.println("El archivo no existe o no es un archivo");
               System.exit(1);
            }

            System.out.println("\n  ╔═══════════════════════════════════════════════╗");
            System.out.println("  ║                  Opción -t                    ║");
            System.out.println("  ╚═══════════════════════════════════════════════╝");
            System.out.println("   Generar un fichero con las currencias de cada término \n");
            
            //Manejamos la funcionalidad de la opción -t
            try{
               String out = "output/" + f.getName().replace(".txt", "_ocurrencias.csv");
               CaseT.process(f, out);

               System.out.println("Archivo de ocurrencias generado: " + out);
            }
            catch (Exception e) { System.err.println("Error al procesar el archivo " + f.getName() + ": " + e.getMessage());}
            
         break;
         
         //Si no es ninguna de las opciones anteriores, error
         default:
            System.out.println("Opción no válida");
            System.exit(1);
      }
   }
}
