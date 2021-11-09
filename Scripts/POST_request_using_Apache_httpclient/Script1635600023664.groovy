// original: https://www.tutorialspoint.com/apache_httpclient/apache_httpclient_response_handlers.htm

import java.nio.charset.StandardCharsets

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.Files

Path userHome = Paths.get(System.getProperty("user.home"))
Path downloads = userHome.resolve("Downloads")
Path ks820dmg = downloads.resolve("Katalon.Studio-8.2.0beta.dmg") // 521mb
Path ks810dmg = downloads.resolve("Katalon Studio-8.1.0.dmg")     // 495mb
Path idea = downloads.resolve("ideaIC-2021.2.3.dmg") // 810mb
assert Files.exists(ks820dmg)
assert Files.exists(ks810dmg)
assert Files.exists(idea)

FileBody part_ks820dmg = new FileBody(ks820dmg.toFile(), ContentType.DEFAULT_BINARY)
FileBody part_ks810dmg = new FileBody(ks810dmg.toFile(), ContentType.DEFAULT_BINARY)
FileBody part_idea = new FileBody(idea.toFile(), ContentType.DEFAULT_BINARY)

// build multipart upload request
HttpEntity data = MultipartEntityBuilder.create()
	.setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
	.addPart("part-ks820dmb", part_ks820dmg)
	.addPart("part-ks810dmg", part_ks810dmg)
	.addPart("part-idea", part_idea)
	.build()

// build http request and assign multipart upload data
HttpUriRequest request = RequestBuilder
	.post("http://localhost:80")
	.setEntity(data)
	.build()

println("Executing request ${request.getRequestLine()}")

// Create a custom response handler
ResponseHandler responseHandler = new MyResponseHander()

CloseableHttpClient httpclient = HttpClients.createDefault()
String responseBody = httpclient.execute(request, responseHandler)

System.out.println("------------ response body ------------")
System.out.println(responseBody)


/**
 * 
 * @author kazuakiurayama
 */
class MyResponseHander implements ResponseHandler {
	
	public String handleResponse(final HttpResponse response) throws IOException {
		int status = response.getStatusLine().getStatusCode();
		if (status >= 200 && status < 300) {
			HttpEntity entity = response.getEntity();
			return entity != null ? EntityUtils.toString(entity) : null;
		} else {
			throw new ClientProtocolException("Unexpected response status: " + status);
		}
	}
}


