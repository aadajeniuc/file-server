package acceptance;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import util.PropertyUtil;

import java.io.IOException;


public class LoginRequiredTest extends TestCase {

    private WebClient webClient;
    private String applicationCtx;
    private String serverAddress;
    private String serverPort;

    @Before
    public void setUp() {
        webClient = new WebClient();
        applicationCtx = PropertyUtil.getPropertyValue("application-context");
        serverAddress = PropertyUtil.getPropertyValue("server-address");
        serverPort = PropertyUtil.getPropertyValue("server-port");
    }

    @Test
    public void testLoginPageRedirectWhenFileAccessed() throws IOException {
        String url = "http://" + serverAddress + ":" + serverPort + "/" + applicationCtx + "/servlet/fs";
        accessUrlAndCheckIfLoginPage(url);
    }

    @Test
    public void testLoginPageRedirectWhenFileUploadRequested() throws IOException {
        String url = "http://" + serverAddress + ":" + serverPort + "/" + applicationCtx + "/servlet/fs/upload";
        accessUrlAndCheckIfLoginPage(url);
    }

    @Test
    public void testLoginPageRedirectWhenZipUploadRequested() throws IOException {
        String url = "http://" + serverAddress + ":" + serverPort + "/" + applicationCtx + "/servlet/fs/zipupload";
        accessUrlAndCheckIfLoginPage(url);
    }

    @Test
    public void testLoginPageRedirectWhenDownloadRequested() throws IOException {
        String url = "http://" + serverAddress + ":" + serverPort + "/" + applicationCtx + "/servlet/fs/download";
        accessUrlAndCheckIfLoginPage(url);
    }

    private void accessUrlAndCheckIfLoginPage(String url) throws IOException {
        HtmlPage startPage = webClient.getPage(url);
        assertTrue(startPage.getBody().asText().indexOf("username") > 0);
        assertTrue(startPage.getBody().asText().indexOf("password") > 0);
    }
}
