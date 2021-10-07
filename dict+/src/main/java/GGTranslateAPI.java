import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class GGTranslateAPI {
    /**
     * dich bang google
     * @param text tu can dich
     * @return tu dc dich
     * @throws IOException
     */
    public  String translate(String text) throws IOException {
        String baseUrl = "https://script.google.com/macros/s/AKfycbzdrK3Ll8WFoozIL3-Sjeqi-LOzV4Hj_kGRqpCE_4x9JaGhX0FgDv1-vxKz28arDmLyhw/exec"
                + "?q=" + URLEncoder.encode(text, "UTF-8")
                + "&target=vi&source=en";
        URL url = new URL(baseUrl);
        StringBuilder response = new StringBuilder();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("User-Agent","Mozilla/5.0");
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine ;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }
}
