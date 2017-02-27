package com.readlearncode.dukesbookshop.restclient;

import com.readlearncode.dukesbookstore.domain.*;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;

/**
 * Source code github.com/readlearncode
 *
 * @author Alex Theedom www.readlearncode.com
 * @version 1.0
 */
@ApplicationScoped
public class BookServiceImpl implements BookService {

    private static final String API_URL = "http://localhost:8081/rest-server";
    private static final String BOOKS_ENDPOINT = API_URL + "/api/books";

    private List<Book> allBooks = new ArrayList<>();

    private Client client;

    @PostConstruct
    public void initialise() {
        client = ClientBuilder.newClient();
    }

    @Override
    public List<Book> getBooks() {

//       List<Book> allBooks = new ArrayList<>();
        WebTarget target = client.target(BOOKS_ENDPOINT);

        Response rep = target.request(MediaType.APPLICATION_JSON).get();
        Set<Link> links = rep.getLinks();

        System.out.println("links: " + links);

        JsonArray response = rep.readEntity(JsonArray.class);

        System.out.println("response: " + response);


//        JsonArray response        .get(JsonArray.class);
//        Response response = target.request(MediaType.APPLICATION_JSON).get().getLinks();
//        List<Book> allBooks = response.readEntity(new GenericType<ArrayList<Book>>(){});
//        System.out.println("AllBooks: " + allBooks);


        System.out.println("JsonArray response: " + response);

        for (int i = 0; i < response.size(); i++) {
            JsonObject bookJson = response.getJsonObject(i);

            List<Author> authors = extractAuthors(bookJson.getJsonArray("authors"));
            List<LinkResource> hyperlinks = extractLinks(bookJson.getJsonArray("links"));

            Book book = new BookBuilder()
                    .setId(bookJson.getString("id"))
                    .setTitle(bookJson.getString("title"))
                    .setDescription(bookJson.getString("description"))
                    .setPrice((float) bookJson.getInt("price"))
                    .setImageFileName(API_URL + bookJson.getString("imageFileName"))
                    .setAuthors(authors)
                    .setPublished(bookJson.getString("published"))
                    .setLink(bookJson.getString("link"))
                    .setHyperlinks(hyperlinks)
                    .createBook();

            allBooks.add(book);
        }

        System.out.println(allBooks);
        return Collections.unmodifiableList(allBooks);
    }

    @Override
    public Book getBook(String id) {
        WebTarget target = client.target(BOOKS_ENDPOINT + "/" + id);
//        JsonObject response = target.request(MediaType.APPLICATION_JSON).get(JsonObject.class);


        Response rep = target.request(MediaType.APPLICATION_JSON).get();
        Set<Link> links = rep.getLinks();

        System.out.println("links: " + links);

        JsonObject response = rep.readEntity(JsonObject.class);

        System.out.println("response: " + response);

        List<Author> authors = extractAuthors(response.getJsonArray("authors"));
        List<LinkResource> hyperlinks = extractLinks(response.getJsonArray("_links"));

        Book book = new BookBuilder()
                .setId(response.getString("id"))
                .setTitle(response.getString("title"))
                .setDescription(response.getString("description"))
                .setPrice((float) response.getInt("price"))
                .setImageFileName(API_URL + response.getString("imageFileName"))
                .setAuthors(authors)
                .setPublished(response.getString("published"))
                .setLink(response.getString("link"))
                .setHyperlinks(hyperlinks)
                .createBook();

        return book;
    }


    @Override
    public void deleteBook(String isbn) {

        String uri = allBooks.stream()
                .filter(book -> book.getId().equals(isbn))
                .map(Hypermedia::getLinks)
                .findFirst()
                .get()
                .stream()
                .filter(linkResource -> linkResource.getRel().equals("delete"))
                .findFirst()
                .get()
                .getUri();

        WebTarget target = client.target(uri);

        Response response = target.request(MediaType.APPLICATION_JSON).get();

        System.out.println("Delete Book ISBN: " + isbn);
        System.out.println("Delete Book ISBN: response " + response);
    }

    @Override
    public Book saveBook(Book book) {
        return null;
    }

    /**
     * Extracts the author list form the json object
     *
     * @param authorArray the JSON Array that contains the author list
     * @return list of authors
     */
    public List<Author> extractAuthors(JsonArray authorArray) {
        List<Author> authors = new ArrayList<>();

        for (int j = 0; j < authorArray.size(); j++) {
            JsonObject jObject = authorArray.getJsonObject(j);
            String id = jObject.getString("id", "");
            String firstName = jObject.getString("firstName", "");
            String lastName = jObject.getString("lastName", "");
            String blogURL = jObject.getString("blogURL", "");
            authors.add(new Author(id, firstName, lastName, blogURL));
        }

        return Collections.unmodifiableList(authors);
    }


    /**
     * Extracts the links from the json object
     *
     * @param linkArray the JSON array that contains the link list
     * @return list of links
     */
    private List<LinkResource> extractLinks(JsonArray linkArray) {

        List<LinkResource> links = new ArrayList<>();

        for (int j = 0; j < linkArray.size(); j++) {
            JsonObject jObject = linkArray.getJsonObject(j);
            String rel = jObject.getString("rel", "");
            String type = jObject.getString("type", "");
            String uri = jObject.getString("uri", "");
            links.add(new LinkResource(rel, type, uri));
        }

        return Collections.unmodifiableList(links);
    }

    @PreDestroy
    private void destroy() {
        client.close();
    }
}