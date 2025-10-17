import java.util.List;
import java.util.Map;

public class Main{
   public static void main(String[] args) throws Exception {
      List<Map<String, String>> data = LoadData.loadData("data/listings.csv");
      System.out.println("Número de registros cargados: " + data.size());
      for (Map<String, String> record : data) {
         System.out.println("Neighborhood: " + record.get("host_neighborhood"));
         System.out.println("Amenities: " + record.get("amenities"));
         System.out.println("------------------------------------------------------------------------------------------------------------------");
      }
   }

}

