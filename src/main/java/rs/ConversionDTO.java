package rs;


import com.google.gson.Gson;
import entity.ComparedIndex;
import entity.RateIndex;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;

// http://localhost:8080/pg/rs/convertion?from=NOK&to=USD
@Path("/convertion")
public class ConversionDTO {

    private String fixer = "http://api.fixer.io/latest?base=";

    @GET
    @Produces(MediaType.APPLICATION_XML)
    public ComparedIndex convert(@QueryParam("from") String from, @QueryParam("to") String to) {

        Client client = ClientBuilder.newClient();

        try {
            URI uri = new URI(fixer + from);
            WebTarget target = client.target(uri);
            Invocation invocation = target.request(MediaType.TEXT_PLAIN).buildGet();
            Response response = invocation.invoke();
            String entity = response.readEntity(String.class);

            Gson gson = new Gson();
            RateIndex rateIndex = gson.fromJson(entity, RateIndex.class);
            ComparedIndex index = new ComparedIndex(from, to, rateIndex.getRates().get(to));
            return index;
        } catch (URISyntaxException e) {
            return null;
        }
    }

}
