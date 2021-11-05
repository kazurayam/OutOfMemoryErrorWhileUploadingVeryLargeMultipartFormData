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
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;


CloseableHttpClient httpclient = HttpClients.createDefault()

File file = new File("./server.groovy")
StringBody part_in_us_ascii = new StringBody("hello,world", new ContentType("text/plain", StandardCharsets.UTF_8))
StringBody part_in_germany = new StringBody("Grüß Gott!", new ContentType("text/plain", StandardCharsets.UTF_8))
StringBody part_in_japanese = new StringBody("こんにちは", new ContentType("text/plain", StandardCharsets.UTF_8))

// build multipart upload request
HttpEntity data = MultipartEntityBuilder.create()
	.setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
	.addPart("part-in-us-ascii", part_in_us_ascii)
	.addPart("part-in-germany", part_in_germany)
	.addPart("part-in-japanese", part_in_japanese)
	.build()

// build http request and assign multipart upload data
HttpUriRequest request = RequestBuilder
	.post("http://localhost:80")
	.setEntity(data)
	.build()

println("Executing request ${request.getRequestLine()}")

// Create a custom response handler
ResponseHandler<String> responseHandler = new MyResponseHander()

String responseBody = httpclient.execute(request, responseHandler)
System.out.println("----------------------------------------")
System.out.println(responseBody)

class MyResponseHander implements ResponseHandler<String> {
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


