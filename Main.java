
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {

        AtomicInteger greaterThan1050000 = new AtomicInteger(0); // 1.050.000 byte'dan büyük resimler
        AtomicInteger lessThanOrEqual1050000 = new AtomicInteger(0); // 1.050.000 byte'dan küçük ya da eşit resimler

        try {

            IntStream.range(0, 100).forEach(i -> {
                try {
                    // API URL'si
                    String urlString = "https://random.dog/woof.json";
                    URL url = new URL(urlString);

                    // API'ye get yapıyorum
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");

                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String inputLine;
                    StringBuilder response = new StringBuilder();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    // JSON yanıtından resim URL alıyorum istek yapmak için
                    String responseBody = response.toString();
                    String imageUrl = responseBody.split("\"url\":\"")[1].split("\"")[0];

                    // Resim URL'sine HTTP isteği yapıyorum
                    URL imageUrlObj = new URL(imageUrl);
                    HttpURLConnection imageConnection = (HttpURLConnection) imageUrlObj.openConnection();
                    imageConnection.setRequestMethod("HEAD"); // HEAD isteği ile sadece başlıkları alırız, içeriği değil

                    // Resmin boyutunu alıyprum
                    int contentLength = imageConnection.getContentLength();

                    // Boyutu kontrol et ve uygun sayaçları artırıtğyurm
                    if (contentLength > 1050000) {
                        greaterThan1050000.incrementAndGet(); // 1.050.000 byte'dan büyükse sayacı artır
                    } else {
                        lessThanOrEqual1050000.incrementAndGet(); // 1.050.000 byte'dan küçük ya da eşitse sayacı artır
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            // Sonuçları yazdırıyorum çalıştımı diye
            System.out.println("1050000 byte'dan büyük resimler: " + greaterThan1050000.get());
            System.out.println("1050000 byte'dan küçük ya da eşit resimler: " + lessThanOrEqual1050000.get());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
