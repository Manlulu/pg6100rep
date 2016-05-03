package rs;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;

@Path("/convertion")
public class ConversionDTO {

    private String fixer = "http://api.fixer.io/latest?base=";

    @GET
    public String convert(@QueryParam("from") String from, @QueryParam("to") String to) {

        Client client = ClientBuilder.newClient();

        try {
            URI uri = new URI(fixer + from);
            WebTarget target = client.target(uri);
            Invocation invocation = target.request(MediaType.TEXT_PLAIN).buildGet();
            Response response = invocation.invoke();
            String entity = response.readEntity(String.class);
            JSONParser parser = new JSONParser();

            String getTo;

            if (response.getStatusInfo().getStatusCode() == 422)
                return "Wrong from";
            try {
                JSONObject jsonObject = (JSONObject) parser.parse(entity);
                jsonObject = (JSONObject) parser.parse(jsonObject.get("rates").toString());
                try {
                    getTo = jsonObject.get(to).toString();
                } catch (NullPointerException e) {
                    return "Wrong to";
                }
                return getTo;
            } catch (ParseException e) {
                e.printStackTrace();
            }


            return "From: " + from + ", To: " + to;
        } catch (URISyntaxException e) {
            return e.getMessage();
        }
    }

}
