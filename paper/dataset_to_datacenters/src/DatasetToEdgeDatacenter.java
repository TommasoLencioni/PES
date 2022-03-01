import java.io.*;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.util.Arrays;
import java.util.List;

import org.opensextant.geodesy.*;

public class DatasetToEdgeDatacenter {

    public DatasetToEdgeDatacenter() {
    }

    public static void main(String[] args) {
        try {
            CSVReader reader = new CSVReader(new FileReader("C:\\Users\\DELL\\Desktop\\Tesi\\PES-main\\paper\\dataset_to_datacenters\\dataset.csv"));
            List<String[]> r = reader.readAll();
            r.forEach(x -> Arrays.toString(x));

            double min_lat = Double.MAX_VALUE;
            double min_long = Double.MAX_VALUE;

            double max_lat = -Double.MAX_VALUE;
            double max_long = -Double.MAX_VALUE;

            for(String[] el: r)
            {
                double lat_double = Double.parseDouble(el[1]);
                double long_double = Double.parseDouble(el[2]);

                // Calcolo coppia minima, vertice in basso a sinistra
                if ( lat_double < min_lat)
                    min_lat = lat_double;

                if ( long_double < min_long)
                    min_long = long_double;


                // Calcolo coppia massima, vertice in alto a destra

                if ( lat_double > max_lat)
                    max_lat = lat_double;

                if ( long_double > max_long)
                    max_long = long_double;
            }


            Geodetic2DPoint bottom_left_vertex = new Geodetic2DPoint(new Longitude(min_long, Angle.DEGREES), new Latitude(min_lat, Angle.DEGREES));
            Geodetic2DPoint top_right_vertex = new Geodetic2DPoint(new Longitude(max_long, Angle.DEGREES), new Latitude(max_lat, Angle.DEGREES)); //max_long, max_lat)
            Geodetic2DPoint top_left_vertex = new Geodetic2DPoint(new Longitude(min_long, Angle.DEGREES), new Latitude(max_lat, Angle.DEGREES));
            Geodetic2DPoint bottom_right_vertex = new Geodetic2DPoint(new Longitude(max_long, Angle.DEGREES), new Latitude(min_lat, Angle.DEGREES));

            Ellipsoid earth = Ellipsoid.getInstance("WGS 84");

            //double length_left_side = Math.ceil(earth.orthodromicDistance(bottom_left_vertex,top_left_vertex));
            //double length_bottom_side = Math.ceil(earth.orthodromicDistance(bottom_left_vertex,bottom_right_vertex));

            double min_angle = 10000;

            double[][] positions = new double[r.size()][2];
            int i=0;

            for(String[] el: r)
            {

                double x_pos = earth.orthodromicDistance(bottom_left_vertex, new Geodetic2DPoint(new Longitude(Double.parseDouble(el[2]), Angle.DEGREES),
                        new Latitude(min_lat, Angle.DEGREES)));
                double y_pos = earth.orthodromicDistance(bottom_left_vertex, new Geodetic2DPoint(new Longitude(min_long, Angle.DEGREES),
                        new Latitude(Double.parseDouble(el[1]), Angle.DEGREES)));

                x_pos -= 271.1805387972784;

                positions[i][0] = x_pos;
                positions[i++][1] = y_pos;

                //Traslazione a sinistra di 271.1805387972784
                double ray = Math.sqrt(x_pos * x_pos + y_pos * y_pos);

                double angle = Math.asin(y_pos/ray);

                //System.out.print("(" +x_pos + "," + y_pos + ")");

                System.out.println("Arcoseno del punto " + x_pos + " " + y_pos + " : " + angle);

                if(angle != 0)
                    min_angle = angle<min_angle?angle:min_angle;

            }

            System.out.println("Angolo minimo: " + min_angle);

            min_angle = (2*Math.PI) - min_angle;

            double sin_min_angle = Math.sin(min_angle);
            double cos_min_angle = Math.cos(min_angle);

            File file = new File("C:\\Users\\DELL\\Desktop\\Tesi\\PES-main\\paper\\dataset_to_datacenters\\edge_datacenters_new.xml");
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write( "<?xml version=\"1.0\"?>\r\n"
                    + "<edge_datacenters>\r\n");

            long min_x = Long.MAX_VALUE;
            long min_y = Long.MAX_VALUE;

            long max_x = -Long.MAX_VALUE;
            long max_y = -Long.MAX_VALUE;

            long[][] int_positions = new long[r.size()][2];
            int j=0;

            for(double[] pos: positions)
            {
                int_positions[j][0] = Math.round((((pos[0] -271.1805387972784) * cos_min_angle) - (pos[1] * sin_min_angle))); // pos_x = x*cos(ang) - y*sin(ang)
                int_positions[j][1] = Math.round(((pos[0] - 271.1805387972784) * sin_min_angle) + (pos[1] * cos_min_angle)); // pos_y = x*sin(ang) + y*cos(ang)

                // Calcolo coppia minima, vertice in basso a sinistra
                if ( int_positions[j][0] < min_x)
                    min_x = int_positions[j][0];

                if ( int_positions[j][1] < min_y)
                    min_y = int_positions[j][1];


                // Calcolo coppia massima, vertice in alto a destra

                if ( int_positions[j][0] > max_x)
                    max_x = int_positions[j][0];

                if ( int_positions[j][1] > max_y)
                    max_y = int_positions[j][1];

                j+=1;
            }

            System.out.println("Coordinate minime:"+ min_x+" "+ min_y);
            System.out.println("Coordinate massime:"+ max_x+" "+max_y);

            for(long[] int_pos : int_positions)
            {
                bw.write("	<datacenter arch=\"x86\" os=\"Linux\">\r\n"
                        + "		<idleConsumption>0</idleConsumption>\r\n"
                        + "		<maxConsumption>0</maxConsumption>\r\n"
                        + "		<isOrchestrator>true</isOrchestrator>\r\n"
                        + "		<location>\r\n"
                        + "			<x_pos>"
                        + (int_pos[0] - min_x)
                        + "</x_pos>\r\n"
                        + "			<y_pos>"
                        + (int_pos[1] - min_y)
                        + "</y_pos>\r\n"
                        + "		</location>\r\n"
                        + "		<hosts>\r\n"
                        + "			<host>\r\n"
                        + "				<core>325</core>\r\n"
                        + "				<mips>1600000</mips>\r\n"
                        + "				<ram>8000</ram>\r\n"
                        + "				<storage>200000</storage>\r\n"
                        + "				<VMs>\r\n"
                        + "					<VM>\r\n"
                        + "						<core>325</core>\r\n"
                        + "						<mips>1600000</mips>\r\n"
                        + "						<ram>8000</ram>\r\n"
                        + "						<storage>200000</storage>\r\n"
                        + "					</VM>					\r\n"
                        + "				</VMs>\r\n"
                        + "			</host>\r\n"
                        + "		</hosts>\r\n"
                        + "	</datacenter>\r\n");
                System.out.println("Coordinate vertice dopo rotazione e traslazione:"+ (int_pos[0] - min_x) +" "+ (int_pos[1] - min_y));

            }

            System.out.println("Coordinate massime x ed y dopo rotazione e traslazione:"+ (max_x - min_x) +" "+ (max_y - min_y));

            bw.write( "</edge_datacenters>");
            bw.close();

        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
}