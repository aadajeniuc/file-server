package acceptance;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import util.PropertyUtil;

import java.io.File;
import java.io.IOException;

public class FileAndZipUploadTest extends TestCase {

    private WebClient webClient;
    private String applicationCtx;
    private String serverAddress;
    private String serverPort;
    private String url;
    private String username;
    private String password;

    @Before
    public void setUp() throws IOException {
        webClient = new WebClient();
        applicationCtx = PropertyUtil.getPropertyValue("application-context");
        serverAddress = PropertyUtil.getPropertyValue("server-address");
        serverPort = PropertyUtil.getPropertyValue("server-port");
        username = PropertyUtil.getPropertyValue("username");
        password = PropertyUtil.getPropertyValue("password");
        url = "http://" + serverAddress + ":" + serverPort + "/" + applicationCtx + "/servlet/fs";
        login(url);

    }

    @Test
    public void testFileUploadAndFileDelete() throws IOException {
        HtmlPage directoryPage = webClient.getPage(url);

        HtmlForm uploadForm = directoryPage.getFormByName("thisform1");
        HtmlFileInput fileInput = uploadForm.getInputByName("datafile");
        fileInput.setValueAttribute(new File("test/resources/filetest.txt").getAbsolutePath());
        HtmlSubmitInput button = uploadForm.getInputByValue("Upload");
        button.click();

        directoryPage = webClient.getPage(url);
        assertTrue(directoryPage.getBody().asText().indexOf("filetest.txt") > 0);
        //test uploaded file for delete
        String urlToDeleteFile = "http://" + serverAddress + ":" + serverPort + "/" + applicationCtx + "/servlet/delete/filetest.txt?confirm=true&Submit=OK";
        directoryPage = webClient.getPage(urlToDeleteFile);
        assertTrue(directoryPage.getBody().asText().indexOf("filetest.txt") == -1);
    }

    @Test
    public void testZipUploadAndFolderDelete() throws IOException {
        HtmlPage directoryPage = webClient.getPage(url);

        HtmlForm uploadForm = directoryPage.getFormByName("thisform2");
        HtmlFileInput fileInput = uploadForm.getInputByName("datafile");
        fileInput.setValueAttribute(new File("test/resources/ziptest.zip").getAbsolutePath());
        HtmlSubmitInput button = uploadForm.getInputByValue("Upload");
        button.click();

        directoryPage = webClient.getPage(url);
        assertTrue(directoryPage.getBody().asText().indexOf("test") > 0);

        //test uploaded zip for delete
        String urlToDeleteUnzipteFile = "http://" + serverAddress + ":" + serverPort + "/" + applicationCtx + "/servlet/delete/test?confirm=true&Submit=OK";
        directoryPage = webClient.getPage(urlToDeleteUnzipteFile);
        assertTrue(directoryPage.getBody().asText().indexOf("test") == -1);
    }

    private void login(String url) throws IOException {
        HtmlPage startPage = webClient.getPage(url);
        ((HtmlTextInput) startPage.getElementById("j_username")).setText(username);
        ((HtmlPasswordInput) startPage.getElementById("j_password")).setText(password);
        startPage.getElementById("submit").click();
    }


}
