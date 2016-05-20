import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

public class ClientPostCall {

	public static void main(String[] args) throws IOException,
			ResourceException {
		// TODO Auto-generated method stub

		// ClientResource res = new ClientResource
//			("http://localhost:8183/batch/Human/xrefs/L");
		
//		ClientResource res = new ClientResource
//				("http://localhost:8183/batch/Mouse/xrefs/En?dataSource=L");
		
		ClientResource res = new ClientResource
				("http://localhost:8183/batch/Mouse/xrefs/En");
		
//		 ClientResource res = new ClientResource
//				 ("http://webservice.bridgedb.org:8185/batch/Mouse/xrefs/En");

//		String ids = fromResource();
		
		 String ids =
		 fromFile("/home/bigcat-jonathan/Desktop/Pratical/ENSEMBLIDs5.txt");

//		 String ids = "ENSMUSG00000035967\n"
//		 + "ENSMUSG00000068732\n"
//		 + "ENSMUSG00000025102\n"
//		 + "ENSMUSG00000059934\n"
//		 + "ENSMUSG00000095832\n"
//		 + "ENSMUSG00000019214\n"
//		 + "ENSMUSG00000063455\n"
//		 + "ENSMUSG00000073823\n"
//		 + "ENSMUSG00000037031";

		postCall(res, ids, true);
	}

	public static String fromResource() throws IOException {
		String ids = "";
		String line = "";
		InputStream is = ClientPostCall.class.getClassLoader()
				.getResourceAsStream("ENSEMBLIDs4.txt");
		BufferedReader br1 = new BufferedReader(new InputStreamReader(is));
		ids = br1.readLine();
		int i = 1;
		while ((line = br1.readLine()) != null) {
			ids += "\n" + line;
			i++;
		}
		System.out.println("Number of ids in the file(ENSEMBLIDs):\t" + i);
		return ids;
	}

	public static String fromFile(String path) throws IOException {
		String ids = "";
		String line = "";
		BufferedReader br1 = new BufferedReader(new FileReader(path));
		ids = br1.readLine();
		int i = 1;
		while ((line = br1.readLine()) != null) {
			ids += "\n" + line;
			i++;
		}
		System.out.println("Number of ids in the file(" + path + "):\t" + i);

		return ids;

	}

	/**
	 * @param res - Query address
	 * @param ids - String of all ids separated with line breaker
	 * @param p - Boolean, true print the result
	 * @throws IOException
	 */
	public static void postCall(ClientResource res, String ids, boolean p)
			throws IOException {
		StringRepresentation s = new StringRepresentation(ids);
		System.out.println(s.getMediaType());
		Date date = new Date();
		Representation out = res.post(s);
		System.out.println(out.toString());
		System.out.println("Start:\t" + date);
		Date date2 = new Date();
		
		if (p)
			out.write(System.out);
		System.out.println("End:\t" + date2);
		

		long duration = date2.getTime() - date.getTime();
		long diffInMilis = TimeUnit.MILLISECONDS.toMillis(duration);
		System.out.println(diffInMilis + "ms");
		long diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(duration);
		System.out.println(diffInSeconds + "s");
	}
}
