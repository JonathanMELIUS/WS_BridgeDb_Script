import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.bridgedb.BridgeDb;
import org.bridgedb.DataSource;
import org.bridgedb.IDMapper;
import org.bridgedb.IDMapperException;
import org.bridgedb.Xref;
import org.bridgedb.bio.DataSourceTxt;

public class XrefMapTest {

	public static void main(String[] args) 
			throws ClassNotFoundException, IDMapperException, IOException {
		
		//Initialize the bridgerest driver class
		Class.forName("org.bridgedb.webservice.bridgerest.BridgeRest");
		//Initialize BridgeDb DataSource system
		DataSourceTxt.init();
		//Instantiate BridgeDb webservice rest mapper
		IDMapper mapper = BridgeDb.
				connect("idmapper-bridgerest:http://webservice.bridgedb.org:8185/Mouse");
		
		//Parse the txt file which contains 22361 ids
		//Put as parameter the number of ids you want to query
		Collection<Xref> srcXrefs = fromResource(75);		
		
		//Use the post method to query all the ids once
		query(mapper,srcXrefs,false);
		
		//Send one query for each id
		queryNormal(mapper,srcXrefs);
	}
	
	/**
	 * Post method Query"
	 * @param mapper - BridgeDb mapper 
	 * @param srcXrefs - Collection of ids to query
	 * @param p - true print the result
	 * @throws ClassNotFoundException
	 * @throws IDMapperException
	 * @throws IOException
	 */
	public static void query(IDMapper mapper, Collection<Xref> srcXrefs, boolean p)
			throws ClassNotFoundException, IDMapperException, IOException {
		System.out.println("Post method Query");
		Date date = new Date();
		System.out.println("Start:\t" + date);

		Map<Xref, Set<Xref>> res = mapper.mapID(srcXrefs);

		Date date2 = new Date();
		System.out.println("End:\t" + date2);

		long duration = date2.getTime() - date.getTime();
		long diffInMilis = TimeUnit.MILLISECONDS.toMillis(duration);
		System.out.println(diffInMilis + "ms\n");

		if (p){
			for (Map.Entry<Xref, Set<Xref>> entry : res.entrySet()){
				System.out.println(entry.getKey());
				System.out.println(entry.getValue());
			}
		}
	}
	
	/**
	 * Regular method Query"
	 * @param mapper - BridgeDb mapper 
	 * @param srcXrefs - Collection of ids to query
	 * @throws ClassNotFoundException
	 * @throws IDMapperException
	 * @throws IOException
	 */
	public static void queryNormal(IDMapper mapper, Collection<Xref> srcXrefs)
			throws ClassNotFoundException, IDMapperException, IOException {
		System.out.println("Regular method Query");
		
		Date date = new Date();
		System.out.println("Start:\t" + date);
		for (Xref xref : srcXrefs){
			mapper.mapID(xref);
		}
		Date date2 = new Date();
		System.out.println("End:\t" + date2);

		long duration = date2.getTime() - date.getTime();
		long diffInMilis = TimeUnit.MILLISECONDS.toMillis(duration);
		System.out.println(diffInMilis + "ms");
	}
	
	
	/**
	 * @param nb - number of ids to query from the resource file
	 * @return
	 * @throws IOException
	 */
	public static ArrayList<Xref> fromResource(int nb) 
			throws IOException {
		DataSource ds = DataSource.getExistingBySystemCode("En");
		String line = "";
		InputStream is = ClientPostCall.class.getClassLoader()
				.getResourceAsStream("ENSEMBLIDs.txt");
		BufferedReader br1 = new BufferedReader(new InputStreamReader(is));
		int i = 0;
		ArrayList<Xref> srcXrefs = new ArrayList<Xref>();
		while ((line = br1.readLine()) != null) {
			srcXrefs.add(new Xref(line,ds));
			i++;
			if (i==nb)break;
		}
		System.out.println("Number of ids in the file(ENSEMBLIDs):\t" + i+"\n");
		return srcXrefs;
	}
}
