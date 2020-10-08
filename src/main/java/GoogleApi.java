//https://www.tutorialspoint.com/apache_httpclient/apache_httpclient_http_get_request.htm

import java.util.Scanner;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class GoogleApi {

    public static void main(String args[]) throws Exception {

        //Creating a HttpClient object
        CloseableHttpClient httpclient = HttpClients.createDefault();

        //Creating a HttpGet object
        HttpGet httpget = new HttpGet("https://translate.googleapis.com/translate_a/single?client=gtx&sl=en&tl=vi&dt=t&q=game");

        //Printing the method used
        System.out.println("Request Type: " + httpget.getMethod());

        //Executing the Get request
        HttpResponse httpresponse = httpclient.execute(httpget);

        Scanner sc = new Scanner(httpresponse.getEntity().getContent());
        //Scanner scc = new Scanner()
        System.out.println(httpresponse.getEntity().getContent());
        //Printing the status line
        System.out.println(httpresponse.getStatusLine());
//        while (sc.hasNext()) {
//            System.out.println(sc.nextLine());
//        }
        String firstLine = sc.nextLine();
        String viWord = "";
        for (int i = 4;; ++i){
            if (firstLine.charAt(i) == '\"'){
                break;
            }
            viWord += firstLine.charAt(i);

        }
        System.out.println(viWord);
    }
}