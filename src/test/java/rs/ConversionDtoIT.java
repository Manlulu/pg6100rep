package rs;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

public class ConversionDtoIT {
    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void convert() throws Exception {
        Client client = ClientBuilder.newClient();
        URI uri = new URI("http://localhost:8080/pg/rs/convertion?from=USD&to=NOK");
        WebTarget target = client.target(uri);

        Invocation invocation = target.request(MediaType.APPLICATION_XML).buildGet();

        Response response = invocation.invoke();
        String entity = response.readEntity(String.class);

        assertTrue(entity.length() > 0);
    }

}